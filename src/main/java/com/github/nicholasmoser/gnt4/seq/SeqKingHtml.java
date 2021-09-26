package com.github.nicholasmoser.gnt4.seq;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
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

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.SectionTitle;
import j2html.Config;
import j2html.tags.ContainerTag;
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
    ContainerTag body = div();
    Optional<ContainerTag> toc = getTableOfContents(opcodes);
    if (toc.isPresent()) {
      body.with(toc.get());
    }
    ContainerTag subroutine = p();
    for (Opcode opcode : opcodes) {
      subroutine.with(opcode.toHTML());
      if (opcode instanceof BranchLinkReturn) {
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
    String css = getCSS() + getTooltipCSS("b");
    return head(
        title("SEQ Report"),
        style(css),
        script(getTooltipJavascript("b", "Unconditional branch to an offset."))
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
        .filter(SectionTitle.class::isInstance)
        .findAny()
        .isPresent();
    if (!hasSection) {
      return Optional.empty();
    }
    ContainerTag list = ul();
    for (Opcode opcode : opcodes) {
      if (opcode instanceof SectionTitle) {
        SectionTitle sectionTitle = (SectionTitle) opcode;
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
    return "body {\n"
        + "background-color: #1E1E1E;\n"
        + "color: #D4D4D4;\n"
        + "font-family: Lucida Console;\n"
        + "padding-left: 10%;\n"
        + "}\n"
        + "h1 {\n"
        + "text-align: center;\n"
        + "padding-left: 0;\n"
        + "}\n"
        + ".focus {\n"
        + "background-color: #264F78;\n"
        + "}\n"
        + "a:link {"
        + "color: #4E94C3;"
        + "}"
        + "a:visited {"
        + "color: #4E94C3;"
        + "}"
        + ".tooltip {\n"
        + "  position: relative;\n"
        + "  display: inline-block;\n"
        + "  border-bottom: 1px dotted black;\n"
        + "}\n";
  }

  /**
   * Returns CSS for the tooltip of the given opcode.
   *
   * @param opcode The opcode to get the tooltip CSS for.
   * @return The tooltip CSS.
   */
  private static String getTooltipCSS(String opcode) {
    return "." + opcode + " ." + opcode + "text {"
        + " visibility: hidden;"
        + " width: 120px;"
        + " background-color: black;"
        + " color: #fff;"
        + " text-align: center;"
        + " border-radius: 6px;"
        + " padding: 5px 0;"
        + " position: absolute;"
        + " z-index: 1;"
        + " }"
        + " ." + opcode + ":hover ." + opcode + "text {"
        + " visibility: visible;"
        + " }\n";
  }

  /**
   * Returns a javascript code block that adds a tooltip for the given opcode. The opcode is
   * referenced as an HTML class. The tooltip displays the given text parameter.
   *
   * @param opcode The opcode of the tooltip.
   * @param text The text to display in the tooltip.
   * @return The javascript code block of the tooltip.
   */
  private static String getTooltipJavascript(String opcode, String text) {
    return "document.addEventListener('DOMContentLoaded', (event) => {\n"
        + "var elements = document.getElementsByClassName('" + opcode + "');\n"
        + "for (var i = 0; i < elements.length; i++) {\n"
        + "    var span = document.createElement('span');\n"
        + "    span.textContent = '" + text + "';\n"
        + "    span.className = '" + opcode + "text'; \n"
        + "    elements[i].appendChild(span);\n"
        + "}"
        + "})";
  }
}
