package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Message;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DolphinSeqListener {

  public static final int SEQ_LISTENER_PORT = 12198;
  public static final int DEFAULT_MESSAGE_BUFFER_SIZE = 500;
  public static final String DOLPHIN_LUA_DOWNLOAD = "https://github.com/NicholasMoser/dolphin/releases/download/lua-dolphin-1.0/Lua-Dolphin.zip";
  private static final Logger LOGGER = Logger.getLogger(DolphinSeqListener.class.getName());
  public Label leftStatus;
  public Label rightStatus;
  public ListView<String> messages;
  private Stage stage;
  private int messageCount = 0;
  private ConcurrentLinkedQueue<String> queue = Queues.newConcurrentLinkedQueue();
  private Thread producer;
  private AnimationTimer consumer;
  private int messageBufferSize = DEFAULT_MESSAGE_BUFFER_SIZE;

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

  public void selectMessage(MouseEvent mouseEvent) {
  }

  public void init(Stage stage) {
    this.stage = stage;
    this.rightStatus.setText("Message Count: " + messageCount);
    startListener();
  }

  public void mark() {
  }

  public void gotoMark() {
  }

  public void clear() {
    messages.getItems().clear();
  }

  public void startListener() {
    if (Sockets.isPortAvailable(SEQ_LISTENER_PORT)) {
      initMessageProducer();
      initMessageConsumer();
      leftStatus.setText("Connected");
    } else {
      Message.error("Failed to Connect to Dolphin", "Please restart GNTool.");
      leftStatus.setText("Failed to Connect to Dolphin");
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
      Message.error("Error Running Dolphin Listener", e.getMessage());
      LOGGER.log(Level.SEVERE, "Error Running Dolphin Listener", e);
    }
  }

  private void initMessageConsumer() {
    consumer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        List<String> messageList = messages.getItems();
        String message;
        int num = 0;
        // Grab messages from the queue until the total buffer size is hit
        while ((message = queue.poll()) != null && num < messageBufferSize) {
          messageList.add(message);
          num++;
          // Make sure we only display message up to the buffer size
          if (messageList.size() > messageBufferSize) {
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
          byte[] bytes = Arrays.copyOf(packet.getData(), packet.getLength());
          String text = new String(bytes, StandardCharsets.UTF_8);
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
