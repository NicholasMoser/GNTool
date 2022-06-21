package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A single edit made to a seq file.
 */
public class SeqEdit {

  private static final Logger LOGGER = Logger.getLogger(SeqEdit.class.getName());
  // stop, used to signal end of old and new bytes
  public static final byte[] STOP = { 0x73, 0x74, 0x6F, 0x70 };

  private final String name;
  private final int offset;
  private final int size;
  private final byte[] oldBytes;
  private final byte[] newBytes;
  private final byte[] newBytesWithBranchBack;
  private final byte[] branchBack;
  private final List<Opcode> newCodes;
  private int position;

  /**
   * Constructor for a seq edit.
   *
   * @param name The name of this seq edit (do not include null bytes).
   * @param offset The offset in the seq file of this seq edit.
   * @param oldBytes The old bytes that this seq edit overrides.
   * @param newBytes The new bytes that this seq edit executes.
   */
  public SeqEdit(String name, int offset, byte[] oldBytes, byte[] newBytes) {
    if (name == null) {
      throw new IllegalArgumentException("name cannot be null");
    } else if (oldBytes == null) {
      throw new IllegalArgumentException("oldBytes cannot be null");
    } else if (newBytes == null) {
      throw new IllegalArgumentException("newBytes cannot be null");
    } else if (oldBytes.length % 4 != 0) {
      throw new IllegalArgumentException("Original bytes size must be multiple of 4");
    } else if (newBytes.length % 4 != 0) {
      throw new IllegalArgumentException("New bytes size must be multiple of 4");
    } else if (containsStop(oldBytes)) {
      throw new IllegalArgumentException("Original bytes contain string \"stop\"");
    } else if (containsStop(newBytes)) {
      throw new IllegalArgumentException("New bytes contain string \"stop\"");
    } else if (oldBytes.length < 8) {
      // The branch takes 8 bytes at a minimum, therefore you cannot have less than 8
      throw new IllegalArgumentException("Hijacked bytes must be 8 or greater.");
    } else if (newBytes.length < 4) {
      // No pointer in adding a seq edit if there are no actual edits
      throw new IllegalArgumentException("New bytes must be 4 or greater.");
    }
    this.name = name;
    this.offset = offset;
    this.oldBytes = oldBytes;
    this.newBytes = newBytes;
    // Add the branch back
    byte[] branch = new byte[] { 0x01, 0x32, 0x00, 0x00 };
    byte[] branchOffset = ByteUtils.fromInt32(offset + oldBytes.length);
    this.branchBack = Bytes.concat(branch, branchOffset);
    this.newBytesWithBranchBack = Bytes.concat(newBytes, branchBack);
    this.newCodes = new LinkedList<>();
    ByteStream bs = new ByteStream(newBytes);
    while (bs.bytesAreLeft()) {
      try {
        Opcode op = SeqHelper.getSeqOpcode(bs,bs.peekBytes(2)[0],bs.peekBytes(2)[1]);
        this.newCodes.add(op);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Unable to get seq opcode.", e);
      }
    }
    this.size = newBytes.length;
  }

  /**
   * Constructor for a seq edit.
   *
   * @param name The name of this seq edit (do not include null bytes).
   * @param offset The offset in the seq file of this seq edit.
   * @param oldBytes The old bytes that this seq edit overrides.
   * @param newCodes The new codes that this seq edit executes.
   */
  public SeqEdit(String name, int offset, byte[] oldBytes, List<Opcode> newCodes, int size) {
    if (name == null) {
      throw new IllegalArgumentException("name cannot be null");
    } else if (oldBytes == null) {
      throw new IllegalArgumentException("oldBytes cannot be null");
    } else if (newCodes == null) {
      throw new IllegalArgumentException("newCodes cannot be null");
    } else if (oldBytes.length % 4 != 0) {
      throw new IllegalArgumentException("Original bytes size must be multiple of 4");
    } else if (containsStop(oldBytes)) {
      throw new IllegalArgumentException("Original bytes contain string \"stop\"");
    } else if (oldBytes.length < 8) {
      // The branch takes 8 bytes at a minimum, therefore you cannot have less than 8
      throw new IllegalArgumentException("Hijacked bytes must be 8 or greater.");
    } else if (newCodes.size() < 1) {
      // No pointer in adding a seq edit if there are no actual edits
      throw new IllegalArgumentException("New codes length must be 1 or greater.");
    }
    this.name = name;
    this.offset = offset;
    this.oldBytes = oldBytes;
    this.newCodes = newCodes;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (Opcode opcode : newCodes) {
      try {
        baos.write(opcode.getBytes(offset, size));
      } catch (Exception e) {

      }
    }
    this.newBytes = baos.toByteArray();
    //this.newBytes = null;
    // Add the branch back
    byte[] branch = new byte[] { 0x01, 0x32, 0x00, 0x00 };
    byte[] branchOffset = ByteUtils.fromInt32(offset + oldBytes.length);
    //this.newBytesWithBranchBack = Bytes.concat(newBytes, branch, branchOffset);
    this.newBytesWithBranchBack = null;
    this.branchBack = Bytes.concat(branch, branchOffset);
    this.size = size;
  }

  /**
   * @return The name of the edit, also can serve as a description.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The offset in the seq file where the seq edit begins.
   */
  public int getOffset() {
    return offset;
  }

  /**
   * @return The original, overridden opcode bytes at the offset used for this seq edit.
   */
  public byte[] getOldBytes() {
    return oldBytes;
  }

  public int getSize() {
    return size;
  }

  public int getPosition() {
    return position;
  }

  public SeqEdit setPosition(int position) {
    this.position = position;
    return this;
  }

  /**
   * @return The opcode bytes to execute with this seq edit, not including the branch back to the
   * origin.
   */
  public byte[] getNewBytes() {
    if (newBytes != null) {
      return newBytes;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (Opcode op : newCodes) {
      try {
        baos.write(op.getBytes());
      } catch (Exception e) {

      }
    }
    return baos.toByteArray();
  }

  public List<Opcode> getNewCodes() {
    return newCodes;
  }


  /**
   * @return The opcode bytes to execute with this seq edit, including the branch back to the
   * origin.
   */
  public byte[] getNewBytesWithBranchBack() {
    return newBytesWithBranchBack;
  }

  /**
   * @return The full bytes for this seq edit, to be appended to the seq extension section of the
   * seq file.
   */
  public byte[] getFullBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(getNameBytes());
      baos.write(ByteUtils.fromInt32(offset));
      baos.write(oldBytes);
      baos.write(STOP);
      //if (newBytesWithBranchBack != null) {
      //  baos.write(newBytesWithBranchBack);
      //} else {
        for (Opcode op : newCodes) {
          baos.write(op.getBytes(position, size));
        }
        baos.write(branchBack);
      //}
      baos.write(STOP);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return The full bytes for this seq edit, to be appended to the seq extension section of the
   * seq file.
   */
  public byte[] getFullBytes(int offset) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(getNameBytes());
      baos.write(ByteUtils.fromInt32(this.offset));
      baos.write(oldBytes);
      baos.write(STOP);
      if (newBytesWithBranchBack != null) {
        baos.write(newBytesWithBranchBack);
      } else {
        for (Opcode op : newCodes) {
          baos.write(op.getBytes(offset, size));
        }
        baos.write(branchBack);
      }
      baos.write(STOP);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * The offset in this seq edit where the new bytes begin. This should be used to find where to
   * branch to when editing a seq file.
   *
   * @return The offset in this seq edits bytes where the new bytes are.
   * @throws IOException If an I/O error occurs.
   */
  public int getNewBytesOffset() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(getNameBytes());
    baos.write(ByteUtils.fromInt32(offset));
    baos.write(oldBytes);
    baos.write(STOP);
    return baos.size();
  }

  /**
   *
   * @return The 4-byte aligned, UTF-8 encoded bytes of the name of this seq edit.
   * @throws IOException If any I/O exception occurs.
   */
  private byte[] getNameBytes() throws IOException {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(nameBytes);
    int nullBytes = 4 - (nameBytes.length % 4); // 4-byte aligned
    baos.write(new byte[nullBytes]);
    return baos.toByteArray();
  }

  /**
   * Returns if the given bytes contain the String "stop", which is a magic word used to separate
   * parts of the seq edit binary data. If the binary data naturally contains the String, it will
   * break parsing.
   *
   * @param bytes The bytes to check for the String "stop".
   * @return If the bytes contain the String "stop".
   */
  private boolean containsStop(byte[] bytes) {
    return Bytes.indexOf(bytes, STOP) != -1;
  }

  @Override
  public String toString() {
    return "SeqEdit\nName: "
        + name
        + "\nOffset: "
        + String.format("0x%X", offset)
        + "\nOld Bytes: 0x"
        + ByteUtils.bytesToHexString(oldBytes)
        + "\nNew Bytes: 0x"
        + ByteUtils.bytesToHexString(newBytes)
        + "\nNew Bytes with Branch Back: 0x"
        + ByteUtils.bytesToHexString(newBytesWithBranchBack)
        + "\nFull Bytes: 0x"
        + ByteUtils.bytesToHexString(getFullBytes());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeqEdit seqEdit = (SeqEdit) o;
    return offset == seqEdit.offset && Objects.equals(name, seqEdit.name)
        && Arrays.equals(oldBytes, seqEdit.oldBytes) && Arrays.equals(newBytes,
        seqEdit.newBytes) && Arrays.equals(newBytesWithBranchBack,
        seqEdit.newBytesWithBranchBack);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(name, offset);
    result = 31 * result + Arrays.hashCode(oldBytes);
    result = 31 * result + Arrays.hashCode(newBytes);
    result = 31 * result + Arrays.hashCode(newBytesWithBranchBack);
    return result;
  }
}
