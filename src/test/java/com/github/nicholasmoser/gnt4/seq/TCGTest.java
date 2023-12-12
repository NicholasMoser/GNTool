package com.github.nicholasmoser.gnt4.seq;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TCGTest {
  @Test
  public void testReadAndWrite() {
    int expected = 0x7FFFFF34;
    String actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0xD0]");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0x7FFFFF4B;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x12C]");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0x7FFFFF47;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x11C]");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0x7FFFFF6D;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1B4]");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0x7FFFFF6D;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1B4]");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("0x0");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 1;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("0x1");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0x1F;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("0x1F");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0xFFFFFFFF;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("0xFFFFFFFF");
    assertThat(TCG.write1(actual)).isEqualTo(expected);

    expected = 0xffffffd3;
    actual = TCG.read1(expected);
    assertThat(actual).isEqualTo("0xFFFFFFD3");
    assertThat(TCG.write1(actual)).isEqualTo(expected);
  }
}
