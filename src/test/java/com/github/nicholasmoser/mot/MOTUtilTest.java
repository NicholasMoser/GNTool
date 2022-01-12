package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.gnt4.mot.Mots;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class MOTUtilTest {
  @Test
  public void test() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path file = uncompressed.resolve(Mots.ANK_0000);
    MOTUtil.unpack(file, Paths.get("D:\\GNT\\aaa\\uncompressed\\files\\chr\\ank\\asdIntellij"));
  }
}
