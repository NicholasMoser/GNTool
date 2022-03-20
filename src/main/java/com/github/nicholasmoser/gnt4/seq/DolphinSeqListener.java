package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.Sockets;
import com.google.common.collect.Queues;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
  private static final Logger LOGGER = Logger.getLogger(DolphinSeqListener.class.getName());
  public Label leftStatus;
  public Label rightStatus;
  public ListView<String> messages;
  private Stage stage;
  private int messageCount = 0;
  private String leftStatusMessage = null;
  private ConcurrentLinkedQueue<String> queue = Queues.newConcurrentLinkedQueue();
  private Thread producer;
  private AnimationTimer consumer;

  public void quit() {
    killListener();
    stage.close();
  }

  public void aboutDolphinSEQListener() {
  }

  public void selectMessage(MouseEvent mouseEvent) {
  }

  public void init(Stage stage) {
    this.stage = stage;
    this.rightStatus.setText("Message Count: " + messageCount);
    this.leftStatus.setText("");
    if (Sockets.isPortAvailable(SEQ_LISTENER_PORT)) {
      initMessageProducer();
      initMessageConsumer();
    } else {
      Message.error("Unable to Connect to Dolphin", "Please restart GNTool.");
      this.leftStatus.setText("Unable to Connect to Dolphin");
    }
  }

  private void initMessageConsumer() {
    consumer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        List<String> messageList = messages.getItems();
        String message;
        int num = 0;
        while ((message = queue.poll()) != null) {
          messageList.add(message);
          num++;
          if (messageList.size() > 500) {
            messageList.remove(0);
          }
          messages.scrollTo(messageList.size() - 1);
        }
        if (num > 0) {
          System.out.println("Consumed " + num);
        }
        // Update left status
        if (leftStatusMessage != null) {
          leftStatus.setText(leftStatusMessage);
        }
        rightStatus.setText("Message Count: " + messageCount);
      }
    };
    consumer.start();
  }

  public class InterruptableUDPThread extends Thread{

    private final DatagramSocket socket;

    public InterruptableUDPThread(DatagramSocket socket){
      this.socket = socket;
    }
    @Override
    public void interrupt(){
      super.interrupt();
      this.socket.close();
    }
  }

  private void initMessageProducer() {
    producer = new Thread(() -> {
      byte[] buf = new byte[256];
      DatagramSocket socket = null;
      try {
        socket = new DatagramSocket(SEQ_LISTENER_PORT);
        while(true) {
          DatagramPacket packet
              = new DatagramPacket(buf, buf.length);
          socket.receive(packet);
          byte[] bytes = Arrays.copyOf(packet.getData(), packet.getLength());
          String text = new String(bytes, StandardCharsets.UTF_8);
          queue.add(text);
        }
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error Running Dolphin Listener", e);
        leftStatusMessage = e.getMessage();
      } finally {
        System.out.println("close");
        socket.close();
      }
    });
    producer.start();
  }

  public void killListener() {
    if (producer != null) {
      producer.interrupt();
    }
    if (consumer != null) {
      consumer.stop();
    }
  }
}
