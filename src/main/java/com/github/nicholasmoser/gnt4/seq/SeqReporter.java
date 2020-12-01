package com.github.nicholasmoser.gnt4.seq;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static j2html.TagCreator.style;
import static j2html.TagCreator.text;
import static j2html.TagCreator.title;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeqReporter {

  public static void generate(Path seqPath, Path outputPath) throws IOException {
    generate(seqPath, outputPath, null);
  }

  public static void generate(Path seqPath, Path outputPath, Path executionPath)
      throws IOException {
    byte[] bytes = Files.readAllBytes(seqPath);
    List<Integer> executionOffsets = executionPath == null ? Collections.emptyList()
        : SeqExecution.getOffsets(executionPath, seqPath);
    String html = html(
        getHead(),
        body(
            h1(seqPath.getFileName().toString()),
            h2("Hex Display"),
            each(getHexRows(bytes, executionOffsets), row ->
                div(attrs(".hex-row"),
                    row
                )
            )
        )
    ).withLang("en").render();
    Files.writeString(outputPath, html);
  }

  /**
   * Returns the rows of hex for the seq file as a list of HTML paragraph tags.
   *
   * @param bytes The bytes of the seq file.
   * @param executionOffsets The execution offsets.
   * @return The rows of hex for the seq file as a list of HTML paragraph tags.
   */
  private static List<ContainerTag> getHexRows(byte[] bytes, List<Integer> executionOffsets) {
    List<ContainerTag> rows = new ArrayList<>();
    ContainerTag currentRow = p(attrs(".hex-row"));
    currentRow.withText("0x00000000 ");
    for (int i = 0; i < bytes.length; i += 4) {
      byte[] word = Arrays.copyOfRange(bytes, i, i + 4);
      String hexString = ByteUtils.bytesToHexString(word);
      if (executionOffsets.contains(i)) {
        currentRow.with(span(
            attrs(".focus"),
            text(hexString)
        ));
      } else {
        currentRow.withText(hexString);
      }
      if ((i + 4) % 16 == 0 || i + 4 >= bytes.length) {
        // End of row or end of file
        rows.add(currentRow);
        currentRow = p(
            attrs(".hex-row"),
            text(String.format("0x%08X ", i + 4))
        );
      } else {
        currentRow.withText(" ");
      }
    }
    return rows;
  }

  /**
   * @return The HTML head of the document.
   */
  private static ContainerTag getHead() {
    return head(
        title("SEQ Report"),
        style(getCSS())
    );
  }

  /**
   * @return The CSS for the entire document.
   */
  private static String getCSS() {
    return "body {\n"
        + "background-color: black;\n"
        + "color: white;\n"
        + "text-align: center;\n"
        + "}\n"
        + ".hex-row {\n"
        + "font-family: monospace;\n"
        + "}\n"
        + ".focus {\n"
        + "background-color: #1496BB;\n"
        + "}\n";
  }
}
