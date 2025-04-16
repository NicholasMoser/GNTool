package com.github.nicholasmoser.gnt4.seq.util;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComboListTest {
    @Test
    public void testReadAnkoCombos() throws Exception {
        Path uncompressed = Prereqs.getUncompressedGNT4();
        String expectedCombos = """
                連弾１
                00BBBBBB
                連弾２
                00BBBBBA
                連弾３
                00BBBBA
                連弾４
                00BBBA
                連弾５
                006BBB
                連弾６
                006BBAG
                連弾７
                006BA
                連弾８
                004BB
                連弾９
                004BAG
                連弾１０
                002BBBBB
                連弾１１
                002BBBBA
                連弾１２
                002BBBA
                連弾１３
                002BBA
                連弾１４
                002BAAA
                連弾１５
                008BA
                連弾１６
                006AAA
                連弾１７
                00DBB
                連弾１８
                00DBA
                連弾１９
                00HB
                連弾２０
                00HA
                """.strip();
        byte[] bytes = Files.readAllBytes(uncompressed.resolve(Seqs.ANK_0000));
        String actualCombos = ComboList.readCombos(bytes);
        assertThat(actualCombos).isEqualTo(expectedCombos);
    }

    @Test
    public void testReadNarutoCombos() throws Exception {
        Path uncompressed = Prereqs.getUncompressedGNT4();
        String expectedCombos = """
                連弾１
                00BBBBBB
                連弾２
                00BBBBBA
                連弾３
                00BAAAA
                連弾４
                00BAABBBBB
                連弾５
                00BAABBBBA
                連弾６
                00BABB
                連弾７
                00BABA
                連弾８
                006BB
                連弾９
                006BA
                連弾１０
                002BAAA
                連弾１１
                002BABBBBB
                連弾１２
                002BABBBBA
                連弾１３
                00DAAAA
                連弾１４
                00DAABBBBB
                連弾１５
                00DAABBBBA
                連弾１６
                00DABB
                連弾１７
                00DABA
                ----------------------TABLE TWO----------------------
                連弾１
                00BBBBBB
                連弾２
                00BBBBA
                連弾３
                00BBBA
                連弾４
                00BAAAA
                連弾５
                00BABB
                連弾６
                00BAABBBB
                連弾７
                00BAABA
                連弾８
                00BAABBA
                連弾９
                00AAAA
                連弾１０
                00AABBBB
                連弾１１
                00AABBA
                連弾１２
                00AABA
                連弾１３
                00ABB
                連弾１４
                006BBBBBB
                連弾１５
                006BBBBA
                連弾１６
                006BBBA
                連弾１７
                006BAA
                連弾１８
                002BBBBBB
                連弾１９
                002BBBBA
                連弾２０
                002BBBA
                連弾２１
                002BAA
                連弾２２
                008BBBB
                連弾２３
                008BBA
                連弾２４
                006AAA
                連弾２５
                006ABBBB
                連弾２６
                006ABBA
                連弾２７
                006ABA
                連弾２８
                008AA
                """.strip();
        byte[] bytes = Files.readAllBytes(uncompressed.resolve(Seqs.NAR_0000));
        String actualCombos = ComboList.readCombos(bytes);
        assertThat(actualCombos).isEqualTo(expectedCombos);
    }

    @Test
    public void testReadOboroCombos() throws Exception {
        Path uncompressed = Prereqs.getUncompressedGNT4();
        String expectedCombos = """
                コンボその１
                0 B B B B B B B
                コンボその２
                0 0 0 0 0 B B A
                コンボその３
                0 0 0 0 B B B A
                コンボその４
                0 0 0 B B B B A
                コンボその５
                0 0 0 0 6 B B B
                コンボその６
                0 0 0 0 4 B B B
                コンボその７
                0 0 0 0 2 B B B
                コンボその８
                0 0 0 0 2 B B A
                コンボその９
                0 2 A B B B B B
                コンボその１０
                0 0 0 0 2 A B A
                コンボその１１
                0 0 0 2 A B B A
                """.strip();
        byte[] bytes = Files.readAllBytes(uncompressed.resolve(Seqs.OBO_0000));
        String actualCombos = ComboList.readCombos(bytes);
        assertThat(actualCombos).isEqualTo(expectedCombos);
    }

    @Test
    public void testReadStartOffset() throws Exception {
        Path uncompressed = Prereqs.getUncompressedGNT4();
        testReadStartOffset(uncompressed.resolve(Seqs.ANK_0000), 0x25CA0);
        testReadStartOffset(uncompressed.resolve(Seqs.BOU_0000), 0x28710);
        testReadStartOffset(uncompressed.resolve(Seqs.CHO_0000), 0x273A0);
        testReadStartOffset(uncompressed.resolve(Seqs.DOG_0000), 0x1C580);
        testReadStartOffset(uncompressed.resolve(Seqs.GAI_0000), 0x24C90);
        testReadStartOffset(uncompressed.resolve(Seqs.GAR_0000), 0x28BC0);
        testReadStartOffset(uncompressed.resolve(Seqs.HAK_0000), 0x25180);
        testReadStartOffset(uncompressed.resolve(Seqs.HI2_0000), 0x24AC0);
        testReadStartOffset(uncompressed.resolve(Seqs.HIN_0000), 0x26050);
        testReadStartOffset(uncompressed.resolve(Seqs.INO_0000), 0x27570);
        testReadStartOffset(uncompressed.resolve(Seqs.IRU_0000), 0x23700);
        testReadStartOffset(uncompressed.resolve(Seqs.ITA_0000), 0x29B30);
        testReadStartOffset(uncompressed.resolve(Seqs.JIR_0000), 0x275B0);
        testReadStartOffset(uncompressed.resolve(Seqs.KAB_0000), 0x25C90);
        testReadStartOffset(uncompressed.resolve(Seqs.KAK_0000), 0x32FEC);
        testReadStartOffset(uncompressed.resolve(Seqs.KAN_0000), 0x25CE0);
        testReadStartOffset(uncompressed.resolve(Seqs.KAR_0000), 0x20100);
        testReadStartOffset(uncompressed.resolve(Seqs.KIB_0000), 0x293D0);
        testReadStartOffset(uncompressed.resolve(Seqs.KID_0000), 0x273F0);
        testReadStartOffset(uncompressed.resolve(Seqs.KIM_0000), 0x27D70);
        testReadStartOffset(uncompressed.resolve(Seqs.KIS_0000), 0x25CE0);
        testReadStartOffset(uncompressed.resolve(Seqs.LOC_0000), 0x28290);
        testReadStartOffset(uncompressed.resolve(Seqs.MIZ_0000), 0x23880);
        testReadStartOffset(uncompressed.resolve(Seqs.NA9_0000), 0x25F50);
        testReadStartOffset(uncompressed.resolve(Seqs.NAR_0000), 0x30F9C);
        testReadStartOffset(uncompressed.resolve(Seqs.NEJ_0000), 0x26190);
        testReadStartOffset(uncompressed.resolve(Seqs.OBO_0000), 0x1A470);
        testReadStartOffset(uncompressed.resolve(Seqs.ORO_0000), 0x27C00);
        testReadStartOffset(uncompressed.resolve(Seqs.SA2_0000), 0x26F50);
        testReadStartOffset(uncompressed.resolve(Seqs.SAK_0000), 0x27710);
        testReadStartOffset(uncompressed.resolve(Seqs.SAR_0000), 0x26300);
        testReadStartOffset(uncompressed.resolve(Seqs.SAS_0000), 0x29CDC);
        testReadStartOffset(uncompressed.resolve(Seqs.SIK_0000), 0x27100);
        testReadStartOffset(uncompressed.resolve(Seqs.SIN_0000), 0x25E80);
        testReadStartOffset(uncompressed.resolve(Seqs.SKO_0000), 0x26A9C);
        testReadStartOffset(uncompressed.resolve(Seqs.TA2_0000), 0x195B0);
        testReadStartOffset(uncompressed.resolve(Seqs.TAY_0000), 0x28FA0);
        testReadStartOffset(uncompressed.resolve(Seqs.TEM_0000), 0x2ABE0);
        testReadStartOffset(uncompressed.resolve(Seqs.TEN_0000), 0x2BD10);
        testReadStartOffset(uncompressed.resolve(Seqs.TSU_0000), 0x26100);
        testReadStartOffset(uncompressed.resolve(Seqs.ZAB_0000), 0x23F30);
        byte[] bytes = Files.readAllBytes(uncompressed.resolve(Seqs.ZAB_1000));
        assertThrows(IOException.class, () -> ComboList.readStartOffset(bytes));
    }

    private void testReadStartOffset(Path seqPath, int expected) throws IOException {
        byte[] bytes = Files.readAllBytes(seqPath);
        int length = ComboList.readStartOffset(bytes);
        assertThat(length).isEqualTo(expected);
    }

    @Test
    public void testReadCombosLength() throws Exception {
        Path uncompressed = Prereqs.getUncompressedGNT4();
        testReadCombosLength(uncompressed.resolve(Seqs.ANK_0000), 0x184);
        testReadCombosLength(uncompressed.resolve(Seqs.BOU_0000), 0xFC);
        testReadCombosLength(uncompressed.resolve(Seqs.CHO_0000), 0xE8);
        testReadCombosLength(uncompressed.resolve(Seqs.DOG_0000), 0x78);
        testReadCombosLength(uncompressed.resolve(Seqs.GAI_0000), 0x160);
        testReadCombosLength(uncompressed.resolve(Seqs.GAR_0000), 0x180);
        testReadCombosLength(uncompressed.resolve(Seqs.HAK_0000), 0x234);
        testReadCombosLength(uncompressed.resolve(Seqs.HI2_0000), 0x1B8);
        testReadCombosLength(uncompressed.resolve(Seqs.HIN_0000), 0xA4);
        testReadCombosLength(uncompressed.resolve(Seqs.INO_0000), 0x108);
        testReadCombosLength(uncompressed.resolve(Seqs.IRU_0000), 0x154);
        testReadCombosLength(uncompressed.resolve(Seqs.ITA_0000), 0x174);
        testReadCombosLength(uncompressed.resolve(Seqs.JIR_0000), 0xEC);
        testReadCombosLength(uncompressed.resolve(Seqs.KAB_0000), 0x16C);
        testReadCombosLength(uncompressed.resolve(Seqs.KAK_0000), 0x2B0);
        testReadCombosLength(uncompressed.resolve(Seqs.KAN_0000), 0x1C4);
        testReadCombosLength(uncompressed.resolve(Seqs.KAR_0000), 0xBC);
        testReadCombosLength(uncompressed.resolve(Seqs.KIB_0000), 0x184);
        testReadCombosLength(uncompressed.resolve(Seqs.KID_0000), 0x140);
        testReadCombosLength(uncompressed.resolve(Seqs.KIM_0000), 0x14C);
        testReadCombosLength(uncompressed.resolve(Seqs.KIS_0000), 0x160);
        testReadCombosLength(uncompressed.resolve(Seqs.LOC_0000), 0x160);
        testReadCombosLength(uncompressed.resolve(Seqs.MIZ_0000), 0x154);
        testReadCombosLength(uncompressed.resolve(Seqs.NA9_0000), 0x24C);
        testReadCombosLength(uncompressed.resolve(Seqs.NAR_0000), 0x39C);
        testReadCombosLength(uncompressed.resolve(Seqs.NEJ_0000), 0x18C);
        testReadCombosLength(uncompressed.resolve(Seqs.OBO_0000), 0x168);
        testReadCombosLength(uncompressed.resolve(Seqs.ORO_0000), 0x120);
        testReadCombosLength(uncompressed.resolve(Seqs.SA2_0000), 0x244);
        testReadCombosLength(uncompressed.resolve(Seqs.SAK_0000), 0x114);
        testReadCombosLength(uncompressed.resolve(Seqs.SAR_0000), 0x1AC);
        testReadCombosLength(uncompressed.resolve(Seqs.SAS_0000), 0x268);
        testReadCombosLength(uncompressed.resolve(Seqs.SIK_0000), 0x138);
        testReadCombosLength(uncompressed.resolve(Seqs.SIN_0000), 0x160);
        testReadCombosLength(uncompressed.resolve(Seqs.SKO_0000), 0x240);
        testReadCombosLength(uncompressed.resolve(Seqs.TA2_0000), 0x168);
        testReadCombosLength(uncompressed.resolve(Seqs.TAY_0000), 0x118);
        testReadCombosLength(uncompressed.resolve(Seqs.TEM_0000), 0x130);
        testReadCombosLength(uncompressed.resolve(Seqs.TEN_0000), 0x154);
        testReadCombosLength(uncompressed.resolve(Seqs.TSU_0000), 0x150);
        testReadCombosLength(uncompressed.resolve(Seqs.ZAB_0000), 0x1D0);
        byte[] bytes = Files.readAllBytes(uncompressed.resolve(Seqs.ZAB_1000));
        assertThrows(IOException.class, () -> ComboList.readCombosLength(bytes));
    }

    private void testReadCombosLength(Path seqPath, int expected) throws IOException {
        byte[] bytes = Files.readAllBytes(seqPath);
        int length = ComboList.readCombosLength(bytes);
        assertThat(length).isEqualTo(expected);
    }
}
