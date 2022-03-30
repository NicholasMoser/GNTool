package com.github.nicholasmoser.gnt4.seq;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.p;
import static j2html.TagCreator.script;
import static j2html.TagCreator.style;
import static j2html.TagCreator.title;
import static j2html.TagCreator.ul;
import static j2html.TagCreator.li;

import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SectionTitle;
import com.github.nicholasmoser.gnt4.seq.opcodes.SeqEditOpcode;
import j2html.Config;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.PTag;
import j2html.tags.specialized.UlTag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * HTML utilities for SeqKing.
 */
public class SeqKingHtml {

  /**
   * Generates an HTML report using the file name of the parsed SEQ file and the list of opcodes
   * parsed from it. The file will be saved to the given output path.
   *
   * @param fileName The name of the seq file the opcodes were parsed from.
   * @param opcodes The opcodes of the
   * @param outputPath The output path of the HTML report.
   * @throws IOException If an I/O error occurs.
   */
  public static void generate(String fileName, List<Opcode> opcodes, Path outputPath) throws IOException {
    Config.textEscaper = text -> text;
    String html = html(
        getHead(),
        body(
            h1(fileName),
            button("Toggle Hide Bytes").attr("onclick", "toggleHideBytes()"),
            getBody(opcodes)
        )
    ).withLang("en").render();
    Files.writeString(outputPath, html);
  }

  /**
   * Gets the HTML body for a list of opcodes.
   *
   * @param opcodes The list of opcodes.
   * @return The HTML body.
   */
  private static ContainerTag getBody(List<Opcode> opcodes) {
    ContainerTag<DivTag> body = div();
    Optional<ContainerTag> toc = getTableOfContents(opcodes);
    toc.ifPresent(body::with);
    ContainerTag<PTag> subroutine = p();
    for (Opcode opcode : opcodes) {
      subroutine.with(opcode.toHTML());
      if (opcode instanceof BranchLinkReturn ||
          opcode instanceof SeqEditOpcode) {
        body.with(subroutine);
        subroutine = p();
      }
    }
    body.with(subroutine);
    return body;
  }

  /**
   * @return The HTML head of the document.
   */
  private static ContainerTag getHead() {
    String css = getCSS();
    return head(
        title("SEQ Report"),
        style(css),
        script(getToggleHideBytes())
    );
  }

  /**
   * Returns a table of contents if there are any sections. Otherwise returns an empty optional.
   *
   * @param opcodes The opcodes to parse for sections.
   * @return A table or contents or empty optional.
   */
  private static Optional<ContainerTag> getTableOfContents(List<Opcode> opcodes) {
    boolean hasSection = opcodes.stream()
        .anyMatch(SectionTitle.class::isInstance);
    if (!hasSection) {
      return Optional.empty();
    }
    ContainerTag<UlTag> list = ul();
    for (Opcode opcode : opcodes) {
      if (opcode instanceof SectionTitle sectionTitle) {
        String text = String.format("0x%05X %s", sectionTitle.getOffset(), sectionTitle.getTitle());
        String dest = String.format("#%X", sectionTitle.getOffset());
        ContainerTag entry = a(text).withHref(dest);
        list.with(li(entry));
      }
    }
    return Optional.of(list);
  }

  /**
   * @return The CSS for the entire document.
   */
  private static String getCSS() {
    return """
        body {
          background-color: #1E1E1E;
          color: #D4D4D4;
          font-family: Lucida Console;
          padding-left: 10%;
        }
        h1 {
          text-align: center;
          padding-left: 0;
        }
        .focus {
          background-color: #264F78;
        }
        a:link {
          color: #4E94C3;
        }
        a:visited {
          color: #4E94C3;
        }
        .g {
          color: DimGray;
        }
        .v {
          color: #1E1E1E;
        }
        """;
  }

  private static String getToggleHideBytes() {
    return """
        function toggleHideBytes() {
          const isVisible = document.querySelector('.g');
          if (isVisible != null) {
            const elements = document.getElementsByClassName("g");
            while(elements.length > 0) {
              elements[0].className = 'v';
            }
          } else {
            const elements = document.getElementsByClassName("v");
            while(elements.length > 0) {
              elements[0].className = 'g';
            }
          }
        }
        """;
  }
}
