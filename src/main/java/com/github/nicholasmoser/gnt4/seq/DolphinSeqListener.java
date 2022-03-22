package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.MarkableString;
import com.github.nicholasmoser.utils.Sockets;
import com.google.common.collect.Queues;
import java.awt.Desktop;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DolphinSeqListener {

  public static final int SEQ_LISTENER_PORT = 12198;
  public static final int DEFAULT_MESSAGE_BUFFER_SIZE = 500;
  public static final int MESSAGES_PER_FRAME = 500;
  public static final String DOLPHIN_LUA_DOWNLOAD = "https://github.com/NicholasMoser/dolphin/releases/download/lua-dolphin-1.0/Lua-Dolphin.zip";
  private static final Logger LOGGER = Logger.getLogger(DolphinSeqListener.class.getName());
  public Label leftStatus;
  public Label rightStatus;
  public ListView<MarkableString> messages;
  public TextField bufferSize;
  private Stage stage;
  private int messageCount = 0;
  private ConcurrentLinkedQueue<String> queue = Queues.newConcurrentLinkedQueue();
  private Thread producer;
  private AnimationTimer consumer;

  public void quit() {
    killListener();
    stage.close();
  }

  public void downloadLuaDolphin() {
    try {
      Desktop.getDesktop().browse(new URI(DOLPHIN_LUA_DOWNLOAD));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Dolphin Lua Download", e);
      Message.error("Error Opening Dolphin Lua Download", e.getMessage());
    }
  }

  public void aboutDolphinSEQListener() {
  }

  public void gotoLine() {
    MarkableString message = messages.getSelectionModel().getSelectedItem();
    if (message == null) {
      Message.info("No Message Selected", "Please select a message.");
      return;
    }
    gotoLine(message.toString());
  }

  public void selectMessage(MouseEvent mouseEvent) {
    EventTarget target =  mouseEvent.getTarget();
    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
      // Left click (double click)
      if(mouseEvent.getClickCount() == 2) {
        if (target instanceof Labeled) {
          Labeled label = (Labeled) target;
          gotoLine(label.getText());
        }
        else if (target instanceof Text) {
          Text text = (Text) target;
          gotoLine(text.getText());
        }
      }
    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      // Right click (context menu)
      ContextMenu menu = new ContextMenu();
      MenuItem gotoLine = new MenuItem("Goto Line");
      gotoLine.setOnAction(event -> {
        if (target instanceof Labeled) {
          Labeled label = (Labeled) target;
          gotoLine(label.getText());
        }
        else if (target instanceof Text) {
          Text text = (Text) target;
          gotoLine(text.getText());
        }
      });
      menu.getItems().add(gotoLine);
      menu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
  }

  private void gotoLine(String message) {
    try {
      int offset = Integer.decode("0x" + message.substring(0, 8));
      System.out.printf("%08X\n", offset);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Line", e);
      Message.error("Error Opening Line", e.getMessage());
    }
  }

  public void init(Stage stage) {
    this.stage = stage;
    this.rightStatus.setText("Message Count: " + messageCount);
    this.bufferSize.setText(Integer.toString(DEFAULT_MESSAGE_BUFFER_SIZE));
    messages.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    startListener();
    updateMessageListFont();
  }

  private void updateMessageListFont() {
    messages.setCellFactory(cell -> new ListCell<>() {
      @Override
      protected void updateItem(MarkableString item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
          setText(item.toString());
          if (item.isMarked()) {
            setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
            setStyle("-fx-text-fill: #BB86FC");
          } else {
            setFont(Font.font("Monospaced", 16));
            setStyle("-fx-text-fill: white");
          }
        }
      }
    });
  }

  public void mark() {
    List<MarkableString> allItems = messages.getItems();
    List<MarkableString> selectedItems = messages.getSelectionModel().getSelectedItems();
    // Mark each selected message
    for (MarkableString item : selectedItems) {
      item.toggleMark();
    }
    // If no messages selected, mark the last one if it exists
    if (selectedItems.isEmpty() && !allItems.isEmpty()) {
      allItems.get(allItems.size() - 1).toggleMark();
    }
    messages.refresh();
  }

  public void gotoMark() {
    List<MarkableString> allItems = messages.getItems();
    int startingIndex = messages.getSelectionModel().getSelectedIndex() - 1;
    if (startingIndex < 0) {
      startingIndex = allItems.size() - 1;
    }
    for (int i = startingIndex; i >= 0; i--) {
      MarkableString item = allItems.get(i);
      if (item.isMarked()) {
        messages.getSelectionModel().clearSelection();
        messages.getSelectionModel().select(i);
        messages.scrollTo(i);
        break;
      }
    }
  }

  public void clear() {
    messages.getItems().clear();
  }

  public void startListener() {
    int bufferSizeValue = DEFAULT_MESSAGE_BUFFER_SIZE;
    try {
      bufferSizeValue = Integer.decode(bufferSize.getText());
    } catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, "Error Setting Buffer Size", e);
      Message.error("Error Setting Buffer Size", e.getMessage());
    }
    if (Sockets.isPortAvailable(SEQ_LISTENER_PORT)) {
      initMessageProducer();
      initMessageConsumer(bufferSizeValue);
      leftStatus.setText("Connected");
    } else {
      leftStatus.setText("Failed to Connect to Dolphin");
      Message.error("Failed to Connect to Dolphin", "Please restart GNTool.");
    }
  }

  public void killListener() {
    try {
      if (producer != null && !producer.isInterrupted()) {
        producer.interrupt();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to stop Dolphin message producer", e);
    }
    try {
      if (consumer != null) {
        consumer.stop();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to stop Dolphin message consumer", e);
    }
    leftStatus.setText("Disconnected");
  }

  private void initMessageProducer() {
    try {
      producer = new ProducerThread(new DatagramSocket(SEQ_LISTENER_PORT));
      producer.start();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running Dolphin Listener", e);
      Message.error("Error Running Dolphin Listener", e.getMessage());
    }
  }

  private void initMessageConsumer(int bufferSize) {
    consumer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        List<MarkableString> messageList = messages.getItems();
        String message;
        int num = 0;
        while ((message = queue.poll()) != null && num < MESSAGES_PER_FRAME) {
          messageList.add(new MarkableString(message));
          num++;
          // Make sure we only display message up to the buffer size
          if (messageList.size() > bufferSize) {
            messageList.remove(0);
          }
          messages.scrollTo(messageList.size() - 1);
        }
        if (num > 0) {
          System.out.println("Consumed " + num);
        }
        messageCount += num;
        rightStatus.setText("Message Count: " + messageCount);
      }
    };
    consumer.start();
  }

  /**
   * A thread with a UDP socket that will close the socket upon interruption. It will consume
   * UDP packets and produce messages from them that will be stored in the {@link #queue}.
   */
  private class ProducerThread extends Thread {

    private final DatagramSocket socket;
    private final byte[] buf;

    public ProducerThread(DatagramSocket socket) {
      this.socket = socket;
      this.buf = new byte[256];
    }

    @Override
    public void run() {
      try {
        while (true) {
          DatagramPacket packet = new DatagramPacket(buf, buf.length);
          socket.receive(packet);
          String text = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
          queue.add(text);
        }
      } catch (Exception e) {
        LOGGER.log(Level.INFO,
            "Producer thread threw an exception, this is expected in most cases due to interrupts",
            e);
      }
    }

    @Override
    public void interrupt() {
      super.interrupt();
      this.socket.close();
    }
  }
}
