package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * Helper methods relating to the usage of sockets.
 */
public class Sockets {

  public static final int MIN_PORT_NUMBER = 1100;

  public static final int MAX_PORT_NUMBER = 49151;

  /**
   * Checks to see if a specific port is available. Borrowed from Apache Camel and licensed under
   * the Apache 2.0 license: http://www.apache.org/licenses/LICENSE-2.0
   * http://svn.apache.org/viewvc/camel/trunk/components/camel-test/src/main/java/org/apache/camel/test/AvailablePortFinder.java?view=markup#l130
   *
   * @param port the port to check for availability
   */
  public static boolean isPortAvailable(int port) {
    if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
      throw new IllegalArgumentException("Invalid start port: " + port);
    }

    ServerSocket ss = null;
    DatagramSocket ds = null;
    try {
      ss = new ServerSocket(port);
      ss.setReuseAddress(true);
      ds = new DatagramSocket(port);
      ds.setReuseAddress(true);
      return true;
    } catch (IOException e) {
    } finally {
      if (ds != null) {
        ds.close();
      }

      if (ss != null) {
        try {
          ss.close();
        } catch (IOException e) {
          /* should not be thrown */
        }
      }
    }

    return false;
  }
}
