package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.Ranges;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class SeqEditPatcher {

  private static final Logger LOGGER = Logger.getLogger(SeqEditPatcher.class.getName());

  public static void askExportToFile(SeqEdit edit, Path seqFile, Path initialDir) throws IOException {
    Optional<Path> output = Choosers.getOutputSeqEdit(initialDir);
    if (output.isEmpty()) {
      return;
    }
    exportToFile(edit, seqFile, output.get());
  }

  public static void exportToFile(SeqEdit edit, Path seqFile, Path output) throws IOException {
    String path = seqFile.getParent().getFileName() + "/" + seqFile.getFileName();
    String editText = path + "\n\n" + edit.toString();
    Files.writeString(output, editText);
  }

  public static void askImportFromFile(Path initialDir, Path seqPath) throws IOException {
    Optional<Path> input = Choosers.getInputSeqEdit(initialDir);
    if (input.isEmpty()) {
      return;
    }
    try {
      importFromFile(input.get(), seqPath, false);
    } catch (IllegalArgumentException e) {
      // Treat IllegalArgumentException as a recoverable exception
      String msg = e.getMessage() + "Continue anyways?";
      if (Message.warnConfirmation("Error Importing Edit", msg)) {
        importFromFile(input.get(), seqPath, true);
      }
    }
  }

  public static void importFromFile(Path input, Path seqPath, boolean force) throws IOException {
    // Validate edit file headers exist
    List<String> lines = Files.readAllLines(input);
    if (!lines.get(2).equals("SeqEdit")) { // Change this in the future for new versions of the file spec
      throw new IOException("Missing SeqEdit header");
    } else if (!lines.get(3).equals("Name:")) {
      throw new IOException("Missing Name header");
    } else if (!lines.get(5).equals("Offset:")) {
      throw new IOException("Missing Offset header");
    } else if (!lines.get(7).equals("Position:")) {
      throw new IOException("Missing Position header");
    } else if (!lines.get(9).equals("Old Bytes:")) {
      throw new IOException("Missing Old Bytes header");
    } else if (!lines.get(11).equals("New Bytes:")) {
      throw new IOException("Missing New Bytes header");
    } else if (!lines.get(13).equals("New Bytes with branch back:")) {
      throw new IOException("Missing New Bytes with branch back header");
    }

    // Validate that this edit is being used on the correct file
    String actualPath = seqPath.toString().replace("\\", "/");
    String expectedPath = lines.get(0);
    if (!actualPath.endsWith(expectedPath)) {
      LOGGER.info("File " + actualPath + " has different name than " + expectedPath);
    }

    // Read edit values
    String name = lines.get(4).substring(2);
    int offset = Integer.decode(lines.get(6).substring(2));
    int position = Integer.decode(lines.get(8).substring(2));
    byte[] oldBytes = ByteUtils.hexStringToBytes(lines.get(10).substring(4));
    byte[] newBytes = ByteUtils.hexStringToBytes(lines.get(12).substring(4));

    // Validate that the edit does not conflict with any existing edits
    for (SeqEdit edit : SeqExt.getEdits(seqPath)) {
      int existingStart = edit.getOffset();
      int existingEnd = existingStart + edit.getOldBytes().length;
      if (Ranges.haveOverlap(existingStart, existingEnd, offset, offset + oldBytes.length)) {
        throw new IOException("New edit location conflicts with existing edit: " + edit.getName());
      }
    }

    // Validate that old bytes match the old bytes from the provided seq
    byte[] actualOldBytes = new byte[oldBytes.length];
    try(RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
      raf.seek(offset);
      raf.read(actualOldBytes);
      if (!Arrays.equals(actualOldBytes, oldBytes) && !force) {
        throw new IllegalArgumentException("Original replaced bytes have changed, possible merge conflict. ");
      }
    }

    // Add the new edit
    SeqEdit edit = new SeqEdit(name, offset, position, oldBytes, newBytes);
    SeqExt.addEdit(edit, seqPath);
  }
}
