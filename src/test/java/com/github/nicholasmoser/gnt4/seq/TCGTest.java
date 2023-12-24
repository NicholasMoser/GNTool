package com.github.nicholasmoser.gnt4.seq;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TCGTest {

  @Test
  public void testReadAndWriteValue() {
    int expected = 0x7FFFFF00;
    String actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x0]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFF01;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFF4B;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x4B]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFF47;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x47]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFF6D;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x6D]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFFFF;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0xFF]");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x7FFFFEFF;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0x7FFFFEFF");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0x0");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 1;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0x1");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0x1F;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0x1F");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0xFFFFFFFF;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0xFFFFFFFF");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);

    expected = 0xffffffd3;
    actual = TCG.readValue(expected);
    assertThat(actual).isEqualTo("0xFFFFFFD3");
    assertThat(TCG.writeValue(actual)).isEqualTo(expected);
  }

  @Test
  public void testReadAndWritePointer() {
    int expected = 0x7FFFFF00;
    String actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x0]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFF01;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFF4B;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x4B]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFF47;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x47]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFF6D;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x6D]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFFFF;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0xFF]");
    assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x7FFFFEFF;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x7FFFFEFF]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x0]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 1;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0x1F;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0x1F]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0xFFFFFFFF;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0xFFFFFFFF]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);

    expected = 0xffffffd3;
    actual = TCG.readPointer(expected);
    assertThat(actual).isEqualTo("s_lpCTD->vars[0xFFFFFFD3]");
    // See TCG.writePointer(...) for why this is commented out
    //assertThat(TCG.writePointer(actual)).isEqualTo(expected);
  }
}
