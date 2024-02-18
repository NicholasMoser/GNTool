package com.github.nicholasmoser.gnt4.seq.ext.parser;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.Binary;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.ExistingBinary;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.ExistingFunction;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.Function;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.InsertAsm;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.Symbol;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.collect.Maps;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolParser {
  private static final Logger LOGGER = Logger.getLogger(SymbolParser.class.getName());

  /**
   * Parse a known number of symbols from a byte stream.
   *
   * @param bs The byte stream to read from.
   * @param count The number of symbols to read.
   * @return The symbols.
   * @throws IOException If any I/O issues occur
   */
  public static List<Symbol> parse(ByteStream bs, int count) throws IOException {
    List<Symbol> symbols = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int start = bs.offset();
      String name = parseName(bs);
      ByteUtils.alignSkip(bs, 16);
      int type = bs.readWord();
      int length = bs.readWord();
      Map<String, Integer> innerLabels;
      switch (type) {
        case Binary.TYPE -> {
          int bytesLength = bs.readWord();
          bs.skipWord();
          byte[] bytes = new byte[bytesLength];
          if (bs.read(bytes) != bytesLength) {
            throw new IOException(
                "Failed to read " + bytesLength + " bytes from offsets " + bs.offset());
          }
          symbols.add(new Binary(name, bytes));
        }
        case ExistingBinary.TYPE -> {
          int binaryOffset = bs.readWord();
          int binaryLength = bs.readWord();
          symbols.add(new ExistingBinary(name, binaryOffset, binaryLength));
        }
        case ExistingFunction.TYPE -> {
          int functionOffset = bs.readWord();
          int functionLength = bs.readWord();
          symbols.add(new ExistingFunction(name, functionOffset, functionLength));
        }
        case Function.TYPE -> {
          int funcLength = bs.readWord();
          int innerLabelsCount = bs.readWord();
          byte[] func = new byte[funcLength];
          if (bs.read(func) != funcLength) {
            throw new IOException(
                "Failed to read " + funcLength + " bytes from offsets " + bs.offset());
          }
          ByteUtils.alignSkip(bs, 16);
          innerLabels = readInnerLabels(bs, innerLabelsCount);
          List<Opcode> opcodes = SeqHelper.getAllOpcodes(func);
          symbols.add(new Function(name, opcodes, innerLabels));
        }
        case InsertAsm.TYPE -> {
          int hijackOffset = bs.readWord();
          int newBytesLength = bs.readWord();
          int oldBytesLength = bs.readWord();
          int innerLabelsCount = bs.readWord();
          bs.skipWord();
          bs.skipWord();
          byte[] newBytes = new byte[newBytesLength];
          if (bs.read(newBytes) != newBytesLength) {
            throw new IOException(
                "Failed to read " + newBytesLength + " bytes from offsets " + bs.offset());
          }
          ByteUtils.alignSkip(bs, 16);
          byte[] oldBytes = new byte[oldBytesLength];
          if (bs.read(oldBytes) != oldBytesLength) {
            throw new IOException(
                "Failed to read " + oldBytesLength + " bytes from offsets " + bs.offset());
          }
          ByteUtils.alignSkip(bs, 16);
          innerLabels = readInnerLabels(bs, innerLabelsCount);
          List<Opcode> newCodes = SeqHelper.getAllOpcodes(newBytes);
          try {
            List<Opcode> oldCodes = SeqHelper.getAllOpcodes(oldBytes);
            symbols.add(new InsertAsm(name, hijackOffset, newCodes, oldCodes, innerLabels));
          } catch (Exception e) {
            // Unable to parse old codes, handle case where they are broken bytes or binary data
            LOGGER.log(Level.SEVERE, "Unable to parse old codes", e);
            symbols.add(new InsertAsm(name, hijackOffset, newCodes, oldBytes, innerLabels));
          }
        }
      }
      ByteUtils.alignSkip(bs, 16);
    }
    return Collections.unmodifiableList(symbols);
  }

  private static Map<String, Integer> readInnerLabels(ByteStream bs, int count) throws IOException {
    Map<String, Integer> innerLabels = Maps.newHashMapWithExpectedSize(count);
    for (int j = 0; j < count; j++) {
      int labelOffset = bs.readWord();
      String name = parseName(bs);
      ByteUtils.alignSkip(bs, 16);
      innerLabels.put(name, labelOffset);
    }
    return innerLabels;
  }

  private static String parseName(ByteStream bs) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1];
    if (bs.read(buffer) != 1) {
      throw new IOException("Failed to parse byte at offset " + bs.offset());
    }
    while(buffer[0] != 0) {
      baos.write(buffer);
      if (bs.read(buffer) != 1) {
        throw new IOException("Failed to parse byte at offset " + bs.offset());
      }
    }
    return baos.toString(StandardCharsets.UTF_8);
  }
}
