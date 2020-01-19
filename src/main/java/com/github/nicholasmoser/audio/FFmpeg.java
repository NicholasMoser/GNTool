package com.github.nicholasmoser.audio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FFmpeg {

  /**
   * Prepares an audio file to be ran with dspadpcm.exe This means setting the audio sampling
   * frequency to 32000, setting the audio codec to pcm_s16le, and setting the audio channel id 0 to
   * the output.
   *
   * @param input  The input path.
   * @param output The output path.
   * @return The output text of the ffmpeg process.
   * @throws IOException If an I/O error occurs
   */
  public static String run(Path input, Path output) throws IOException {
    if (!Files.isRegularFile(Paths.get("ffmpeg.exe"))) {
      throw new IOException("ffmpeg.exe cannot be found.");
    }
    try {
      Process process = new ProcessBuilder("ffmpeg.exe", "-y", "-i", input.toString(), "-ar",
          "32000", "-acodec", "pcm_s16le", "-map_channel", "0.0.0", output.toString()).start();
      process.waitFor();
      return new String(process.getErrorStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
