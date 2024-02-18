package com.github.nicholasmoser.gnt4.seq.ext;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.SeqSection;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SectionTitle;
import com.github.nicholasmoser.gnt4.seq.opcodes.SeqEditOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SeqSectionTest {

  @Test
  public void testNoSeqExtension() throws Exception {
    ByteStream bs = getByteStream("seq_v1_no_edits.seq");
    assertThat(SeqSection.isSeqExtension(bs)).isFalse();
    List<Opcode> opcodes = SeqSection.handleSeqExtension(bs);
    assertThat(opcodes).isEmpty();
  }

  @Test
  public void testOneV1SeqEdit() throws Exception {
    ByteStream bs = getByteStream("seq_v1_one_edit.seq");
    assertThat(SeqSection.isSeqExtension(bs)).isTrue();
    List<Opcode> opcodes = SeqSection.handleSeqExtension(bs);
    assertThat(opcodes.size()).isEqualTo(3);
    assertThat(opcodes.get(0)).isInstanceOf(SectionTitle.class);
    assertThat(opcodes.get(2)).isInstanceOf(SectionTitle.class);
    Opcode opcode = opcodes.get(1);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
    SeqEditOpcode editOpcode = (SeqEditOpcode) opcode;
    SeqEdit edit = editOpcode.getSeqEdit();
    assertThat(edit.getNewBytes()).isEqualTo(ByteUtils.hexTextToBytes("01000002 3D0A0000"));
    assertThat(edit.getOldBytes()).isEqualTo(ByteUtils.hexTextToBytes("01000001 3D0A0000"));
    assertThat(edit.getName()).isEqualTo("Edit One");
    assertThat(edit.getNewBytesWithBranchBack()).isEqualTo(ByteUtils.hexTextToBytes("01000002 3D0A0000 01320000 0000017C"));
  }

  @Test
  public void testThreeV1SeqEdits() throws Exception {
    ByteStream bs = getByteStream("seq_v1_three_edits.seq");
    assertThat(SeqSection.isSeqExtension(bs)).isTrue();
    List<Opcode> opcodes = SeqSection.handleSeqExtension(bs);
    assertThat(opcodes.size()).isEqualTo(5);
    assertThat(opcodes.get(0)).isInstanceOf(SectionTitle.class);
    assertThat(opcodes.get(4)).isInstanceOf(SectionTitle.class);
    Opcode opcode = opcodes.get(1);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
    opcode = opcodes.get(2);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
    opcode = opcodes.get(3);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
  }

  @Test
  public void testV1LocalBranchSeqEdit() throws Exception {
    ByteStream bs = getByteStream("seq_v1_local_branch_edit.seq");
    assertThat(SeqSection.isSeqExtension(bs)).isTrue();
    List<Opcode> opcodes = SeqSection.handleSeqExtension(bs);
    assertThat(opcodes.size()).isEqualTo(3);
    assertThat(opcodes.get(0)).isInstanceOf(SectionTitle.class);
    assertThat(opcodes.get(2)).isInstanceOf(SectionTitle.class);
    Opcode opcode = opcodes.get(1);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
  }

  @Test
  public void testV1AbsoluteBranchSeqEdit() throws Exception {
    ByteStream bs = getByteStream("seq_v1_absolute_branch_edit.seq");
    assertThat(SeqSection.isSeqExtension(bs)).isTrue();
    List<Opcode> opcodes = SeqSection.handleSeqExtension(bs);
    assertThat(opcodes.size()).isEqualTo(3);
    assertThat(opcodes.get(0)).isInstanceOf(SectionTitle.class);
    assertThat(opcodes.get(2)).isInstanceOf(SectionTitle.class);
    Opcode opcode = opcodes.get(1);
    assertThat(opcode).isInstanceOf(SeqEditOpcode.class);
  }

  private ByteStream getByteStream(String fileName) throws IOException {
    Path dir = Paths.get("src/test/resources/gnt4/seq/ext");
    Path file = dir.resolve(fileName);
    byte[] bytes = Files.readAllBytes(file);
    ByteStream bs = new ByteStream(bytes);
    bs.seek(0x210);
    return bs;
  }
}
