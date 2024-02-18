package com.github.nicholasmoser.gnt4.seq;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.li;
import static j2html.TagCreator.p;
import static j2html.TagCreator.script;
import static j2html.TagCreator.style;
import static j2html.TagCreator.title;
import static j2html.TagCreator.ul;

import com.github.nicholasmoser.gnt4.seq.comment.Comments;
import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActionID;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SectionTitle;
import com.github.nicholasmoser.gnt4.seq.opcodes.SeqEditOpcode;
import com.google.common.collect.Multimap;
import j2html.Config;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.PTag;
import j2html.tags.specialized.UlTag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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
            getBody(opcodes, fileName)
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
  private static ContainerTag getBody(List<Opcode> opcodes, String fileName) {
    Map<Integer, Function> offsetToFunction = Functions.getFunctions(fileName);
    Multimap<Integer, String> offsetToComments = Comments.getComments(fileName);
    ContainerTag<DivTag> body = div();
    Optional<ContainerTag> toc = getTableOfContents(opcodes);
    toc.ifPresent(body::with);
    ContainerTag<PTag> subroutine = p();
    for (int i = 0; i < opcodes.size(); i++) {
      Opcode opcode = opcodes.get(i);
      // Maybe get function
      if (offsetToFunction.containsKey(opcode.getOffset())) {
        String functionName = offsetToFunction.get(opcode.getOffset()).name();
        subroutine.with(div(functionName + ":").withClass("c"));
        for (String comment : offsetToFunction.get(opcode.getOffset()).comments()) {
          subroutine.with(div(comment).withClass("c"));
        }
      }
      // Maybe get comments
      if (offsetToComments.containsKey(opcode.getOffset())) {
        for (String comment : offsetToComments.get(opcode.getOffset())) {
          subroutine.with(div(comment).withClass("c"));
        }
      }
      // Get opcode HTML
      subroutine.with(opcode.toHTML());
      // Maybe break for end of function
      if (isFunctionEnd(i, opcodes)) {
        body.with(subroutine);
        subroutine = p();
      }
    }
    body.with(subroutine);
    return body;
  }

  public static boolean isFunctionEnd(int index, List<Opcode> opcodes) {
    if ((index == 0) || (index + 1 == opcodes.size())) {
      // Start or end of file, ignore
      return false;
    }
    Opcode currentOpcode = opcodes.get(index);
    Opcode nextOpcode = opcodes.get(index + 1);
    // Break before and after binary data and seq edits
    if (currentOpcode instanceof BinaryData) {
      return true;
    }
    if (nextOpcode instanceof BinaryData) {
      return true;
    }
    if (currentOpcode instanceof SeqEditOpcode) {
      return true;
    }
    if (nextOpcode instanceof SeqEditOpcode) {
      return true;
    }
    // Check for branching or referential patterns that indicate a function
    int offset = nextOpcode.getOffset();
    for (Opcode opcode : opcodes) {
      if (currentOpcode instanceof BranchLinkReturn && opcode instanceof BranchLink bl) {
        // It's a new function if there is a branch and link to the first instruction after a blr
        if (offset == bl.getDestination().offset()) {
          return true;
        }
      } else if (opcode instanceof ActionID actionId) {
        // Break where an action starts
        if (offset == actionId.getActionOffset()) {
          return true;
        }
      }
    }
    return false;
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
        .c {
          color: #6A8759;
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
