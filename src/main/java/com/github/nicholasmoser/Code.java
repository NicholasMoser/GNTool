package com.github.nicholasmoser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.primitives.Bytes;

/**
 * A code to modify a file. Consists of a series of insert, overwrite, and/or delete actions.
 */
public class Code {
  enum ActionType {
    INSERT, OVERWRITE, DELETE
  }

  private List<Action> actions;

  private Code() {
    actions = new ArrayList<Action>();
  }

  /**
   * @return A builder for a Code.
   */
  public static Code getBuilder() {
    return new Code();
  }

  /**
   * 
   * @param offset The offset to execute the action at.
   * @param bytes The bytes to insert.
   * @return The code.
   */
  public Code withInsert(int offset, byte[] bytes) {
    Action insertAction = new Action(ActionType.INSERT, offset, bytes);
    actions.add(insertAction);
    return this;
  }

  /**
   * 
   * @param offset The offset to execute the action at.
   * @param bytes The bytes to overwrite with.
   * @return The code.
   */
  public Code withOverwrite(int offset, byte[] bytes) {
    Action insertAction = new Action(ActionType.OVERWRITE, offset, bytes);
    actions.add(insertAction);
    return this;
  }

  /**
   * Adds a delete action to the list of actions.
   * 
   * @param offset The offset to execute the action at.
   * @param numBytes The number of bytes to delete.
   * @return The code.
   */
  public Code withDelete(int offset, int numBytes) {
    Action insertAction = new Action(ActionType.DELETE, offset, numBytes);
    actions.add(insertAction);
    return this;
  }

  /**
   * Executes the currently built series of actions on a given file path. It will overwrite the file
   * with the new changes.
   * 
   * @param filePath The path to the file to execute the code on.
   * @throws IOException If an I/O error occurs when working with the file.
   */
  public void execute(Path filePath) throws IOException {
    if (!Files.isRegularFile(filePath)) {
      throw new IllegalArgumentException(filePath + " is not a file.");
    }
    byte[] bytes = Files.readAllBytes(filePath);
    List<Byte> byteList = Bytes.asList(bytes);
    for (Action action : actions) {
      ActionType actionType = action.getActionType();
      int offset = action.getOffset();
      if (actionType == ActionType.INSERT) {
        insert(byteList, offset, action.getBytes());
      } else if (actionType == ActionType.OVERWRITE) {
        overwrite(byteList, offset, action.getBytes());
      } else if (actionType == ActionType.DELETE) {
        delete(byteList, offset, action.getNumBytes());
      }
    }
    Files.write(filePath, Bytes.toArray(byteList));
  }

  private void insert(List<Byte> bytes, int offset, byte[] bytesToInsert) {
    int newByteIndex = 0;
    for (int i = offset; newByteIndex < bytesToInsert.length; i++) {
      bytes.add(i, bytesToInsert[newByteIndex]);
      newByteIndex++;
    }
  }

  private void overwrite(List<Byte> bytes, int offset, byte[] bytesToOverwrite) {
    int newByteIndex = 0;
    for (int i = offset; newByteIndex < bytesToOverwrite.length; i++) {
      bytes.set(i, bytesToOverwrite[newByteIndex]);
      newByteIndex++;
    }
  }

  private void delete(List<Byte> bytes, int offset, int numBytesToDelete) {
    for (Iterator<Byte> iter = bytes.listIterator(offset); numBytesToDelete > 0;) {
      if (!iter.hasNext()) {
        throw new RuntimeException("Code is attempting to delete more bytes than is available.");
      }
      iter.next();
      iter.remove();
      numBytesToDelete--;
    }
  }

  /**
   * A single action for a code. For insert and overwrite the byte array is used to represent the
   * bytes that should be inserted or overwritten at a location. For delete only an int is used to
   * represent the number of bytes that should be removed at a location.
   */
  private class Action {
    private ActionType actionType;

    private int offset;

    private byte[] bytes;

    private int numBytes;

    /**
     * Creates a new insert or overwrite action.
     * 
     * @param actionType The action type. Must be ActionType.INSERT or ActionType.OVERWRITE
     * @param offset The offset to execute the action at.
     * @param bytes The bytes to insert/overwrite.
     */
    private Action(ActionType actionType, int offset, byte[] bytes) {
      if (actionType == ActionType.DELETE) {
        throw new IllegalArgumentException("Delete actions cannot use byte array argument.");
      }
      this.actionType = actionType;
      this.offset = offset;
      this.bytes = bytes;
    }

    /**
     * Creates a new delete action.
     * 
     * @param actionType The action type. Must be ActionType.DELETE
     * @param offset The offset to execute the action at.
     * @param numBytes The number of bytes to remove.
     */
    private Action(ActionType actionType, int offset, int numBytes) {
      if (actionType == ActionType.INSERT || actionType == ActionType.OVERWRITE) {
        throw new IllegalArgumentException("Insert/Overwrite actions cannot use int argument.");
      }
      this.actionType = actionType;
      this.offset = offset;
      this.numBytes = numBytes;
    }

    /**
     * @return The action type.
     */
    public ActionType getActionType() {
      return actionType;
    }

    /**
     * @return The offset in the file for the action.
     */
    public int getOffset() {
      return offset;
    }

    /**
     * @return The bytes to insert or overwrite into the file.
     */
    public byte[] getBytes() {
      return bytes;
    }

    /**
     * @return The number of bytes to delete from the file.
     */
    public int getNumBytes() {
      return numBytes;
    }
  }
}

