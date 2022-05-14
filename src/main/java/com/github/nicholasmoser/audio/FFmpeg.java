package com.github.nicholasmoser.audio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FFmpeg {

  /**
   * Prepares an audio file to be ran with dspadpcm.exe This means setting the audio sampling
   * frequency to 32000, the audio codec to pcm_s16le, and the audio channel id 0 to the output.
   *
   * @param input  The input path.
   * @param output The output path.
   * @return The output text of the ffmpeg process.
   * @throws IOException If an I/O error occurs.
   */
  public static String prepareSoundEffect(Path input, Path output) throws IOException {
    String ffmpeg;
    if (Platform.isWindows()) {
      ffmpeg = "ffmpeg.exe";
      if (!Files.isRegularFile(Paths.get("ffmpeg.exe"))) {
        throw new IOException("ffmpeg.exe cannot be found.");
      }
    } else {
      try {
        ffmpeg = "ffmpeg";
        Runtime.getRuntime().exec("ffmpeg");
      } catch (Exception e) {
        throw new IOException("ffmpeg cannot be found.");
      }
    }
    try {
      Process process = new ProcessBuilder(ffmpeg, "-y", "-i", input.toString(), "-ar",
          "32000", "-acodec", "pcm_s16le", "-map_channel", "0.0.0", "-loglevel", "quiet", output.toString()).start();
      process.waitFor();
      return new String(process.getErrorStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * Prepares an audio file to be ran with dtkmake.exe This means setting the audio sampling
   * frequency to 48000, the audio codec to pcm_s16le, the audio channel to stereo, and using
   * bitexact mode to do so.
   *
   * @param input  The input path.
   * @param output The output path.
   * @return The output text of the ffmpeg process.
   * @throws IOException If an I/O error occurs.
   */
  public static String prepareMusic(Path input, Path output) throws IOException {
    String ffmpeg;
    if (Platform.isWindows()) {
      ffmpeg = "ffmpeg.exe";
      if (!Files.isRegularFile(Paths.get("ffmpeg.exe"))) {
        throw new IOException("ffmpeg.exe cannot be found.");
      }
    } else {
      try {
        ffmpeg = "ffmpeg";
        Runtime.getRuntime().exec("ffmpeg");
      } catch (Exception e) {
        throw new IOException("ffmpeg cannot be found.");
      }
    }
    try {
      Process process = new ProcessBuilder(ffmpeg, "-y", "-i", input.toString(), "-ar",
          "48000", "-ac", "2", "-acodec", "pcm_s16le", "-bitexact", "-loglevel", "quiet", output.toString()).start();
      process.waitFor();
      return new String(process.getErrorStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
