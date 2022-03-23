package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.utils.Browser;
import com.github.nicholasmoser.utils.MarkableString;
import com.github.nicholasmoser.utils.Sockets;
import com.google.common.collect.Queues;
import java.awt.Desktop;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
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

/**
 * A GUI to listen for SEQ position messages from Lua Dolphin using the udp_gnt4.lua script.
 */
public class DolphinSeqListener {

  public static final int SEQ_LISTENER_PORT = 12198;
  public static final int DEFAULT_MESSAGE_BUFFER_SIZE = 500;
  public static final int MESSAGES_PER_FRAME = 500;
  public static final String DOLPHIN_LUA_DOWNLOAD = "https://github.com/NicholasMoser/dolphin/releases/download/lua-dolphin-1.0/Lua-Dolphin.zip";
  private static final String ABOUT_URL = "https://github.com/NicholasMoser/GNTool/blob/master/docs/seq_listener.md";
  private static final Logger LOGGER = Logger.getLogger(DolphinSeqListener.class.getName());
  private final ConcurrentLinkedQueue<String> queue = Queues.newConcurrentLinkedQueue();
  private final Map<String, Path> seqToSeqReport = new HashMap<>();
  public Label leftStatus;
  public Label rightStatus;
  public ListView<MarkableString> messages;
  public TextField bufferSize;
  private int messageCount = 0;
  private Thread producer;
  private AnimationTimer consumer;
  private Stage stage;
  private Path gnt4Files;

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
    try {
      Desktop.getDesktop().browse(new URI(ABOUT_URL));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening About Page", e);
      Message.error("Error Opening About Page", e.getMessage());
    }
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
    EventTarget target = mouseEvent.getTarget();
    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      // Left click (double click)
      if (mouseEvent.getClickCount() == 2) {
        if (target instanceof Labeled label) {
          gotoLine(label.getText());
        } else if (target instanceof Text text) {
          gotoLine(text.getText());
        }
      }
    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      // Right click (context menu)
      ContextMenu menu = new ContextMenu();
      MenuItem gotoLine = new MenuItem("Goto Line");
      gotoLine.setOnAction(event -> {
        if (target instanceof Labeled label) {
          gotoLine(label.getText());
        } else if (target instanceof Text text) {
          gotoLine(text.getText());
        }
      });
      menu.getItems().add(gotoLine);
      menu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
  }

  /**
   * Opens the disassembled HTML report of the seq of a message and jumps to the offset from that
   * message.
   *
   * @param message The message to parse and go to.
   */
  private void gotoLine(String message) {
    try {
      // Get path to seq files
      if (gnt4Files == null) {
        Message.info("Select Workspace", "Please select a GNTool workspace directory.");
        Optional<Path> path = Choosers.getInputWorkspaceDirectory(GNTool.USER_HOME);
        if (path.isEmpty()) {
          return;
        }
        gnt4Files = path.get().resolve("uncompressed/files");
      }
      // Get path to input SEQ file
      GNT4FileNames fileNames = new GNT4FileNames();
      String brokenName = message.substring(27);
      if (brokenName.isBlank()) {
        throw new IOException("Unknown file name!");
      }
      String seqName = fileNames.fix(brokenName);
      Path seqPath = gnt4Files.resolve(seqName);
      if (!Files.exists(seqPath)) {
        throw new IOException(seqPath + " does not exist.");
      }
      // See if the output HTML is already cached
      Path outputHTML = seqToSeqReport.get(seqName);
      if (outputHTML == null) {
        // Get path to output HTML file and generate it
        Optional<Path> output = Choosers.getOutputHTML(gnt4Files.toFile());
        if (output.isEmpty()) {
          return;
        }
        outputHTML = output.get();
        seqToSeqReport.put(seqName, outputHTML);
        SeqKing.generateHTML(seqPath, outputHTML, false);
      }
      int offset = Integer.decode("0x" + message.substring(0, 8));
      String fileUri = "file:///" + outputHTML + String.format("#%X", offset);
      Browser.open(fileUri);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Line", e);
      Message.error("Error Opening Line", e.getMessage());
    }
  }

  public void init(Stage stage, Path uncompressedFiles) {
    this.stage = stage;
    this.gnt4Files = uncompressedFiles;
    this.rightStatus.setText("Message Count: " + messageCount);
    this.bufferSize.setText(Integer.toString(DEFAULT_MESSAGE_BUFFER_SIZE));
    messages.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    startListener();
    updateMessageListFont();
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

  private void initMessageProducer() {
    try {
      producer = new ProducerThread(new DatagramSocket(SEQ_LISTENER_PORT));
      producer.start();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running Dolphin Listener", e);
      Message.error("Error Running Dolphin Listener", e.getMessage());
    }
  }

  /**
   * An animation timer that runs each frame to consume and update the message list with new
   * messages.
   *
   * @param bufferSize The size of the message buffer.
   */
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
        messageCount += num;
        rightStatus.setText("Message Count: " + messageCount);
      }
    };
    consumer.start();
  }

  /**
   * A thread with a UDP socket that will close the socket upon interruption. It will consume UDP
   * packets and produce messages from them that will be stored in the {@link #queue}.
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
