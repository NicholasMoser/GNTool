package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.utils.Browser;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.utils.MarkableString;
import com.github.nicholasmoser.utils.Sockets;
import com.google.common.collect.Queues;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
  private String lastSearch = "";
  private boolean isListenerRunning = false;

  public void quit() {
    killListener();
    stage.close();
  }

  public void downloadLuaDolphin() {
    GUIUtils.browse(DOLPHIN_LUA_DOWNLOAD);
  }

  public void aboutDolphinSEQListener() {
    GUIUtils.browse(ABOUT_URL);
  }

  public void disassembleLine() {
    MarkableString message = messages.getSelectionModel().getSelectedItem();
    if (message == null) {
      Message.info("No Message Selected", "Please select a message.");
      return;
    }
    disassembleLine(message.toString());
  }

  public void copy() {
    List<MarkableString> selected = messages.getSelectionModel().getSelectedItems();
    if (selected.isEmpty()) {
      return;
    }
    String text = selected.stream()
        .map(MarkableString::toString)
        .collect(Collectors.joining("\n"));
    copy(text);
  }

  /**
   * Open a popup to search for a specific offset in the message list.
   */
  public void search() {
    try {
      Stage popup = new Stage();
      Text text = new Text("Offset: ");
      text.setFont(Font.font(18));
      text.getStyleClass().add("text-id");
      TextField search = new TextField();
      search.setText(lastSearch);
      search.setFont(Font.font(18));
      Button button = new Button("Search");
      button.setFont(Font.font(18));
      button.setOnAction(event -> {
        lastSearch = search.getText();
        int offset = Integer.decode("0x" + search.getText());
        List<MarkableString> allItems = messages.getItems();
        int startingIndex = messages.getSelectionModel().getSelectedIndex() - 1;
        if (startingIndex < 0) {
          startingIndex = allItems.size() - 1;
        }
        for (int i = startingIndex; i >= 0; i--) {
          MarkableString item = allItems.get(i);
          int currentOffset = Integer.decode("0x" + item.toString().substring(0, 8));
          if (currentOffset == offset) {
            messages.getSelectionModel().clearSelection();
            messages.getSelectionModel().select(i);
            messages.scrollTo(i);
            break;
          }
        }
      });
      GridPane searchPane = new GridPane();
      searchPane.add(text, 0, 0);
      searchPane.add(search, 1, 0);
      searchPane.add(button, 2, 0);
      Scene scene = new Scene(searchPane);
      GUIUtils.initDarkMode(scene);
      GUIUtils.setIcons(popup);
      popup.setScene(scene);
      popup.setTitle("Search Offset");
      popup.centerOnScreen();
      popup.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to search", e);
      Message.error("Failed to search", e.getMessage());
    }
  }

  public void selectMessage(MouseEvent mouseEvent) {
    EventTarget target = mouseEvent.getTarget();
    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
      // Left click (double click)
      if (mouseEvent.getClickCount() == 2) {
        if (target instanceof Labeled label) {
          disassembleLine(label.getText());
        } else if (target instanceof Text text) {
          disassembleLine(text.getText());
        }
      }
    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      // Right click (context menu)
      ContextMenu menu = new ContextMenu();
      MenuItem gotoLine = new MenuItem("Disassemble Line");
      gotoLine.setOnAction(event -> {
        if (target instanceof Labeled label) {
          disassembleLine(label.getText());
        } else if (target instanceof Text text) {
          disassembleLine(text.getText());
        }
      });
      MenuItem copy = new MenuItem("Copy");
      copy.setOnAction(event -> {
        if (target instanceof Labeled label) {
          copy(label.getText());
        } else if (target instanceof Text text) {
          copy(text.getText());
        }
      });
      menu.getItems().addAll(gotoLine, copy);
      menu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
  }

  public void init(Stage stage, Path gnt4Files) {
    this.stage = stage;
    this.gnt4Files = gnt4Files;
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
      isListenerRunning = true;
    } else if (!isListenerRunning) {
      leftStatus.setText("Failed to Connect to Dolphin");
      Message.error("Failed to Connect to Dolphin", "Please restart GNTool.");
    }
  }

  public void killListener() {
    try {
      if (producer != null && !producer.isInterrupted()) {
        producer.interrupt();
        isListenerRunning = false;
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

  /**
   * Opens the disassembled HTML report of the seq of a message and jumps to the offset from that
   * message.
   *
   * @param message The message to parse and go to.
   */
  private void disassembleLine(String message) {
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
        Optional<String> fileName = Seqs.getFileName(seqPath);
        if (fileName.isEmpty()) {
          fileName = Seqs.requestFileName();
          if (fileName.isEmpty()) {
            return;
          }
        }
        SeqKing.generateHTML(seqPath, fileName.get(), outputHTML, false, true);
      }
      int offset = Integer.decode("0x" + message.substring(0, 8));
      String fileUri = "file:///" + outputHTML + String.format("#%X", offset);
      if (System.getProperty("os.name").startsWith("Windows")) {
        Browser.open(fileUri);
      } else {
        GUIUtils.browse(fileUri);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Disassemble Line", e);
      Message.error("Failed to Disassemble Line", e.getMessage());
    }
  }

  private void copy(String text) {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    Map<DataFormat, Object> output = new HashMap<>();
    output.put(DataFormat.PLAIN_TEXT, text);
    clipboard.setContent(output);
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

  /**
   * Initialize a {@link ProducerThread} as a daemon thread and start it.
   */
  private void initMessageProducer() {
    try {
      producer = new ProducerThread(new DatagramSocket(SEQ_LISTENER_PORT));
      producer.setDaemon(true);
      producer.start();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running Dolphin Listener", e);
      Message.error("Error Running Dolphin Listener", e.getMessage());
    }
  }

  /**
   * Initialize an animation timer that runs each frame to consume and update the message list with
   * new messages.
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
      this.buf = new byte[65535];
      Thread.currentThread().setName("SEQ UDP Packer Receiver");
    }

    @Override
    public void run() {
      try {
        while (true) {
          DatagramPacket packet = new DatagramPacket(buf, buf.length);
          socket.receive(packet);
          int length = packet.getLength();
          ByteStream stream = new ByteStream(packet.getData());
          while (stream.offset() < length) {
            int offset = stream.readWord();
            int opcode = stream.readWord();
            int pc = stream.readWord();
            String fileName = stream.readCString();
            String text = String.format("%08x %08x %08x %s", offset, opcode, pc, fileName);
            queue.add(text);
          }
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
