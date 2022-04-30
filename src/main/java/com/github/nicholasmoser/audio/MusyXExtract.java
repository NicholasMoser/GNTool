package com.github.nicholasmoser.audio;

import com.google.common.io.CountingInputStream;
import com.google.common.io.CountingOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Java port of the audio file converter MusyXExtract, originally written by Nisto.
 * Is able to convert between .sam/.sdi and .dsp.
 * The original code can be found here: https://github.com/Nisto/musyx-extract
 * The original code does not provide a license but does adhere to the Github TOS.
 * Therefore it is understood to be usable for private use at the very least.
 * https://choosealicense.com/no-permission/
 */
public class MusyXExtract {

  private static final Pattern DSP_ID_REGEX = Pattern
      .compile("^\\d{5} \\(0x([\\dA-F]{4})\\).dsp$", Pattern.CASE_INSENSITIVE);

  private static final byte[] TERMINAL = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
      (byte) 0xFF};

  /**
   * Inserts bytes into a buffer at an offset.
   *
   * @param buf   The buffer to insert bytes into.
   * @param off   The offset to put the bytes.
   * @param bytes The bytes to put into the buffer.
   */
  private static void put_binary(byte[] buf, int off, byte[] bytes) {
    System.arraycopy(bytes, 0, buf, off, bytes.length);
  }

  /**
   * Inserts an unsigned 16-bit big-endian short into a buffer at an offset.
   *
   * @param buf  The buffer to insert the short into.
   * @param off  The offset to put the bytes.
   * @param data The short to put into the buffer.
   */
  private static void put_u16_be(byte[] buf, int off, short data) {
    byte[] bytes = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(data).array();
    System.arraycopy(bytes, 0, buf, off, bytes.length);
  }

  /**
   * Inserts an unsigned 32-bit big-endian integer into a buffer at an offset.
   *
   * @param buf  The buffer to insert the integer into.
   * @param off  The offset to put the bytes.
   * @param data The integer to put into the buffer.
   */
  private static void put_u32_be(byte[] buf, int off, int data) {
    byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(data).array();
    System.arraycopy(bytes, 0, buf, off, bytes.length);
  }

  /**
   * Gets an array of bytes of a certain size at a certain offset from a buffer.
   *
   * @param buf  The buffer to retrieve the bytes from.
   * @param off  The offset in the buffer to retrieve bytes from.
   * @param size The amount of bytes to retrieve from the buffer.
   * @return The array of bytes.
   */
  private static byte[] get_binary(byte[] buf, int off, int size) {
    byte[] bytes = new byte[size];
    System.arraycopy(buf, off, bytes, 0, size);
    return bytes;
  }

  /**
   * Gets an unsigned 16-bit big-endian short from a buffer.
   * This method will return it as an int.
   *
   * @param buf The buffer to retrieve the short from.
   * @param off The offset in the buffer to retrieve the short from.
   * @return The 16-bit big-endian short as an int.
   */
  private static int get_u16_be(byte[] buf, int off) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
    return byteBuffer.getShort(off);
  }

  /**
   * Gets an unsigned 32-bit big-endian integer from a buffer.
   *
   * @param buf The buffer to retrieve the integer from.
   * @param off The offset in the buffer to retrieve the integer from.
   * @return The 32-bit big-endian int.
   */
  private static int get_u32_be(byte[] buf, int off) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
    return byteBuffer.getInt(off);
  }

  /**
   * Converts samples to nibbles. This conversion will result in a larger output than the input.
   * There will be a loss in accuracy if you convert back and forth between nibbles and samples.
   *
   * @param samples The samples to convert.
   * @return The samples in nibbles.
   */
  private static int samples_to_nibbles(int samples) {
    int whole_frames = samples / 14;
    int remainder = samples % 14;
    if (remainder > 0) {
      return (whole_frames * 16) + remainder + 2;
    }
    return whole_frames * 16;
  }

  /**
   * Converts nibbles to samples. This conversion will result in a smaller output than the input.
   * There will be a loss in accuracy if you convert back and forth between nibbles and samples.
   *
   * @param nibbles The nibbles to convert.
   * @return The nibbles in samples.
   */
  private static int nibbles_to_samples(int nibbles) {
    int whole_frames = nibbles / 16;
    int remainder = nibbles % 16;
    remainder -= 2;

    if (remainder > 0) {
      return (whole_frames * 14) + remainder;
    }
    return whole_frames * 14;
  }

  /**
   * Converts samples to bytes.
   *
   * @param samples The samples to convert.
   * @return The sample in bytes.
   */
  private static int samples_to_bytes(int samples) {
    int nibbles = samples_to_nibbles(samples);
    int raw_bytes = (nibbles / 2) + (nibbles % 2);
    if (raw_bytes % 8 != 0) {
      raw_bytes += 8 - (raw_bytes % 8);
    }
    return raw_bytes;
  }

  /**
   * Reads the header from a .dsp file. This will not close the InputStream.
   *
   * @param dsp    The .dsp InputStream.
   * @param id     The id of the file.
   * @param offset The offset of the file.
   * @return The metadata from the .dsp file.
   * @throws IOException If there is an I/O issue reading the dsp header.
   */
  private static Meta read_dsp_header(InputStream dsp, int id, int offset) throws IOException {
    byte[] header = new byte[96];
    if (dsp.read(header) != 96) {
      throw new IOException("Was not able to fully read dsp header.");
    }
    return new Meta.Builder()
        .withId(id)
        .withOffset(offset)
        .withSamples(get_u32_be(header, 0x00))
        .withNibbles(get_u32_be(header, 0x04))
        .withRate(get_u32_be(header, 0x08))
        .withLoopFlag(get_u16_be(header, 0x0C))
        .withLoopStart(get_u32_be(header, 0x10))
        .withLoopEnd(get_u32_be(header, 0x14))
        .withCoeffs(get_binary(header, 0x1C, 32))
        .withPs(get_binary(header, 0x3E, 2))
        .withLps(get_binary(header, 0x44, 2))
        .withLyn1(get_binary(header, 0x46, 2))
        .withLyn2(get_binary(header, 0x48, 2))
        .build();
  }

  /**
   * Writes the .dsp header to an OutputStream. This will not close the OutputStream.
   *
   * @param dsp  The .dsp OutputStream.
   * @param meta The metadata for the .dsp file.
   * @throws IOException If an I/O error occurs.
   */
  private static void write_dsp_header(OutputStream dsp, Meta meta) throws IOException {
    int loop_length = meta.getLoopLength();
    int loop_start = meta.getLoopStart();
    int samples = meta.getSamples();
    int rate = meta.getRate();
    byte[] coeffs = meta.getCoeffs();
    byte[] ps = meta.getPs();
    byte[] lps = meta.getLps();
    byte[] lyn1 = meta.getLyn1();
    byte[] lyn2 = meta.getLyn2();

    int loop_flag;
    int loop_end;

    if (loop_length > 1 && loop_start + loop_length <= samples) {
      loop_flag = 1;
      loop_start = samples_to_nibbles(loop_start);
      loop_end = samples_to_nibbles(loop_start + loop_length) - 1;
    } else {
      loop_flag = 0;
      loop_start = 2; // As per the DSPADPCM docs: "If not looping, specify 2, which is the top sample."
      loop_end = 0;
    }

    byte[] header = new byte[96];

    int nibbles = samples_to_nibbles(samples);

    put_u32_be(header, 0x00, samples);                      // raw samples
    put_u32_be(header, 0x04, nibbles);                      // nibbles
    put_u32_be(header, 0x08, rate);                         // sample rate
    put_u16_be(header, 0x0C, (short) loop_flag);            // loop flag
    put_u16_be(header, 0x0E, (short) 0);                    // format (always zero - ADPCM)
    put_u32_be(header, 0x10, loop_start);                   // loop start address (in nibbles)
    put_u32_be(header, 0x14, loop_end);                     // loop end address (in nibbles)
    put_u32_be(header, 0x18, 2);                      // initial offset value (in nibbles)
    put_binary(header, 0x1C, coeffs);                       // coefficients
    put_u16_be(header, 0x3C, (short) 0);                    // gain (always zero for ADPCM)
    put_u16_be(header, 0x3E, ps.length > 0 ? ps[0] : 0);    // predictor/scale
    put_u16_be(header, 0x40, (short) 0);                    // sample history (not specified?)
    put_u16_be(header, 0x42, (short) 0);                    // sample history (not specified?)
    put_u16_be(header, 0x44, lps.length > 0 ? lps[0] : 0);  // predictor/scale for loop context
    put_binary(header, 0x46, lyn1);                         // sample history (n-1) for loop context
    put_binary(header, 0x48, lyn2);                         // sample history (n-2) for loop context

    dsp.write(header);
  }

  /**
   * Reads the .sdir InputStream into a list of metadata objects. references:
   * http://www.metroid2002.com/retromodding/wiki/AGSC_(File_Format) https://github.com/AxioDL/amuse
   *
   * @param sdir The .sdir InputStream.
   * @return The list of metadata objects read from the .sdir InputStream.
   * @throws IOException If an I/O error occurs.
   */
  private static List<Meta> read_sdir(InputStream sdir) throws IOException {
    byte[] sdirbuf = readFully(sdir);
    int tbl1_offset = 0;
    List<Meta> metas = new ArrayList<>();
    while (isNotTerminal(sdirbuf, tbl1_offset)) {
      byte[] record = get_binary(sdirbuf, tbl1_offset, 0x20);
      Meta.Builder builder = new Meta.Builder()
          .withId(get_u16_be(record, 0x00))
          .withOffset(get_u32_be(record, 0x04))
          .withRate(get_u16_be(record, 0x0E))
          .withSamples(get_u32_be(record, 0x10))
          .withLoopStart(get_u32_be(record, 0x14))
          .withLoopLength(get_u32_be(record, 0x18));

      tbl1_offset += 0x20;

      int tbl2_offset = get_u32_be(record, 0x1C);

      record = get_binary(sdirbuf, tbl2_offset, 0x28);
      builder = builder.withPs(Arrays.copyOfRange(record, 0x02, 0x03))
          .withLps(Arrays.copyOfRange(record, 0x03, 0x04))
          .withLyn1(Arrays.copyOfRange(record, 0x04, 0x06))
          .withLyn2(Arrays.copyOfRange(record, 0x06, 0x08))
          .withCoeffs(Arrays.copyOfRange(record, 0x08, 0x28));
      metas.add(builder.build());
    }
    return metas;
  }

  /**
   * Writes to .sdir data to an OutputStream given a list of metadata objects.
   *
   * @param sdir  The .sdir OutputStream.
   * @param metas The list of metadata objects.
   * @throws IOException If an I/O error occurs.
   */
  private static void write_sdir(OutputStream sdir, List<Meta> metas) throws IOException {
    byte[] sdirbuf = new byte[72 * metas.size() + 4];

    int tbl1_offset = 0;
    int tbl2_offset = 32 * metas.size() + 4;

    for (Meta meta : metas) {
      int loop_start = nibbles_to_samples(meta.getLoopStart());
      int loop_end = nibbles_to_samples(meta.getLoopEnd());
      int loop_length = loop_end - loop_start;

      if (loop_length != 0) {
        loop_length += 1;
      }

      put_u16_be(sdirbuf, tbl1_offset, (short) meta.getId());
      put_u32_be(sdirbuf, tbl1_offset + 0x04, meta.getOffset());
      put_binary(sdirbuf, tbl1_offset + 0x0C, new byte[]{0x3C});
      put_u16_be(sdirbuf, tbl1_offset + 0x0E, (short) meta.getRate());
      put_u32_be(sdirbuf, tbl1_offset + 0x10, meta.getSamples());
      put_u32_be(sdirbuf, tbl1_offset + 0x14, loop_start);
      put_u32_be(sdirbuf, tbl1_offset + 0x18, loop_length);
      put_u32_be(sdirbuf, tbl1_offset + 0x1C, tbl2_offset);

      put_binary(sdirbuf, tbl2_offset, new byte[]{0x00, 0x08});
      put_binary(sdirbuf, tbl2_offset + 0x02, Arrays.copyOfRange(meta.getPs(), 1, 2));
      put_binary(sdirbuf, tbl2_offset + 0x03, Arrays.copyOfRange(meta.getLps(), 1, 2));
      put_binary(sdirbuf, tbl2_offset + 0x04, meta.getLyn1());
      put_binary(sdirbuf, tbl2_offset + 0x06, meta.getLyn2());
      put_binary(sdirbuf, tbl2_offset + 0x08, meta.getCoeffs());

      tbl1_offset += 0x20;

      tbl2_offset += 0x28;
    }

    put_binary(sdirbuf, tbl1_offset, TERMINAL);

    sdir.write(sdirbuf);
  }

  /**
   * Reads a specified number of bytes from an InputStream into an OutputStream.
   *
   * @param src       The InputStream to read from.
   * @param dst       The OutputStream to write to.
   * @param todo_size The number of bytes to transfer.
   * @throws IOException If an I/O error occurs.
   */
  private static void extract_data(InputStream src, OutputStream dst, int todo_size)
      throws IOException {
    while (todo_size > 0) {
      int read_size = Math.min(4096, todo_size);
      byte[] bytes = new byte[read_size];
      src.read(bytes);
      dst.write(bytes);
      todo_size -= read_size;
    }
  }

  /**
   * Extracts .dsp files from a .sdi file and a .sam file. The .dsp files will be saved to the
   * outputPath.
   *
   * @param sdiPath    The path to the .sdi file.
   * @param samPath    The path to the .sam file.
   * @param outputPath The output directory to write the .dsp files to.
   * @throws IOException If an I/O error occurs.
   */
  public static void extract_samples(Path sdiPath, Path samPath, Path outputPath)
      throws IOException {
    if (!sdiPath.toString().endsWith(".sdi")) {
      throw new IllegalArgumentException("sdiPath is not an .sdi file");
    }
    if (!samPath.toString().endsWith(".sam")) {
      throw new IllegalArgumentException("samPath is not an .sam file");
    }
    if (!Files.isDirectory(outputPath)) {
      throw new IllegalArgumentException("outputPath is not a directory");
    }

    List<Meta> metas;
    try (InputStream is = Files.newInputStream(sdiPath)) {
      metas = read_sdir(is);
    }

    try (CountingInputStream is = new CountingInputStream(Files.newInputStream(samPath))) {
      for (int i = 0; i < metas.size(); i++) {
        Meta meta = metas.get(i);
        int offset = meta.getOffset();
        int skipAmount = offset - (int) is.getCount();
        if (is.skip(skipAmount) != skipAmount) {
          throw new IOException("Was not able to skip to offset " + offset);
        }

        String filename = String.format("%05d (0x%04X).dsp", i, meta.getId());
        Path outputFile = outputPath.resolve(filename);

        try (OutputStream os = Files.newOutputStream(outputFile)) {
          write_dsp_header(os, meta);
          int sample_size = samples_to_bytes(meta.getSamples());
          extract_data(is, os, sample_size);
        }
      }
    }
  }

  /**
   * Packs .dsp files from a given directory to the output .sdi file and .sam file.
   *
   * @param inputPath The input directory to read the .dsp files from.
   * @param sdiPath   The output path to the .sdi file.
   * @param samPath   The output path to the .sam file.
   * @throws IOException If an I/O error occurs.
   */
  public static void pack_samples(Path inputPath, Path sdiPath, Path samPath) throws IOException {
    if (!sdiPath.toString().endsWith(".sdi")) {
      throw new IllegalArgumentException("sdiPath is not an .sdi file");
    }
    if (!samPath.toString().endsWith(".sam")) {
      throw new IllegalArgumentException("samPath is not an .sam file");
    }
    if (!Files.isDirectory(inputPath)) {
      throw new IllegalArgumentException("inputPath is not a directory");
    }
    List<Meta> metas = new ArrayList<>();
    try (CountingOutputStream os = new CountingOutputStream(Files.newOutputStream(samPath))) {
      List<Path> dspPaths = Files.list(inputPath)
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".dsp"))
          .collect(Collectors.toList());
      for (Path dspPath : dspPaths) {
        int id = getId(dspPath);
        padZeroes(os, os.getCount());
        try (InputStream is = Files.newInputStream(dspPath)) {
          Meta meta = read_dsp_header(is, id, (int) os.getCount());
          int sample_size = samples_to_bytes(meta.getSamples());
          extract_data(is, os, sample_size);
          metas.add(meta);
        }
      }
    }
    try (OutputStream os = Files.newOutputStream(sdiPath)) {
      write_sdir(os, metas);
    }
  }

  /**
   * Reads the id from the path of a .dsp file.
   *
   * @param dspPath The path to the .dsp file.
   * @return The id of the file.
   * @throws IOException If an I/O error occurs.
   */
  public static int getId(Path dspPath) throws IOException {
    String fileName = dspPath.getFileName().toString();
    Matcher matcher = DSP_ID_REGEX.matcher(fileName);
    if (!matcher.matches()) {
      throw new IOException("Invalid .dsp filename: " + fileName);
    }
    String match = matcher.group(1);
    int result = Integer.parseInt(match, 16);
    System.out.printf("  ID is %d (0x%X) for file %s\n", result, result, dspPath);
    return result;
  }

  /**
   * Checks if the current index of the OutputStream is aligned to 32-bytes. If it is not, zeroes
   * will be writen to the OutputStream until it is 32-byte aligned.
   *
   * @param os    The OutputStream to write to.
   * @param index The current index of the OutputStream.
   * @throws IOException If an I/O error occurs.
   */
  private static void padZeroes(OutputStream os, long index) throws IOException {
    if (index % 32 != 0) {
      long remainder = 32 - (index % 32);
      for (int i = 0; i < remainder; i++) {
        os.write(0);
      }
    }
  }

  /**
   * Validates that 4 bytes from a byte array at a given offset do not represent terminal bytes.
   * This is indicated by 4 bytes of 0xFF.
   *
   * @param bytes  The full byte array.
   * @param offset The offset in the full byte array to check four bytes at.
   * @return If the 4-byte word at the given offset is not terminal.
   */
  private static boolean isNotTerminal(byte[] bytes, int offset) {
    byte[] subsection = new byte[4];
    System.arraycopy(bytes, offset, subsection, 0, 4);
    return !Arrays.equals(subsection, TERMINAL);
  }

  /**
   * Reads an InputStream fully into a byte array using a buffer.
   *
   * @param input The InputStream to read from.
   * @return The full bytes of the InputStream.
   * @throws IOException If an I/O error occurs.
   */
  private static byte[] readFully(InputStream input) throws IOException {
    byte[] buffer = new byte[8192];
    int bytesRead;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    while ((bytesRead = input.read(buffer)) != -1) {
      output.write(buffer, 0, bytesRead);
    }
    return output.toByteArray();
  }
}
