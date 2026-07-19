package com.github.nicholasmoser.gnt4.seq.util;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.ByteUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComboListTest {
    @Test
    public void testWriteAddComboTableTwo() throws Exception {
        String original = """
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
                ----------------------NEXT COMBO TABLE----------------------
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
        String modified = """
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
                ----------------------NEXT COMBO TABLE----------------------
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
                連弾２９
                008AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                """.strip();
        byte[] expectedBytes = ComboList.comboStringToBytes(original, 0);
        byte[] actualBytes = Arrays.copyOf(expectedBytes, expectedBytes.length);
        byte[] output = ComboList.writeCombos(modified, actualBytes, true);
        String actual = ComboList.comboBytesToString(output);
        assertThat(modified).isEqualTo(actual);

        // It should throw an error if not forcing, since the new codes are larger
        assertThrows(NoCodeSpaceException.class, () -> ComboList.writeCombos(modified, actualBytes, false));
    }

    @Test
    public void testWriteAddComboTableOne() throws Exception {
        String original = """
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
        String modified = """
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
                連弾２１
                00HAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                """.strip();
        byte[] expectedBytes = ComboList.comboStringToBytes(original, 0);
        byte[] actualBytes = Arrays.copyOf(expectedBytes, expectedBytes.length);
        byte[] output = ComboList.writeCombos(modified, actualBytes, true);
        String actual = ComboList.comboBytesToString(output);
        assertThat(modified).isEqualTo(actual);

        // It should throw an error if not forcing, since the new codes are larger
        assertThrows(NoCodeSpaceException.class, () -> ComboList.writeCombos(modified, actualBytes, false));
    }

    @Test
    public void testWriteRemoveComboTableOne() throws Exception {
        String original = """
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
        String modified = """
                連弾１
                00BBBBBB
                連弾２
                00BBBBBA
                連弾３
                00BBBBA
                連弾２０
                00HA
                """.strip();
        byte[] expectedBytes = ComboList.comboStringToBytes(original, 0);
        byte[] actualBytes = Arrays.copyOf(expectedBytes, expectedBytes.length);
        byte[] output = ComboList.writeCombos(modified, actualBytes, false);
        String actual = ComboList.comboBytesToString(output);
        assertThat(modified).isEqualTo(actual);
    }

    @Test
    public void testWriteSameCombosOneTable() throws Exception {
        String expected = """
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
        byte[] expectedBytes = ComboList.comboStringToBytes(expected, 0);
        byte[] actualBytes = Arrays.copyOf(expectedBytes, expectedBytes.length);
        String combos = ComboList.comboBytesToString(expectedBytes);
        byte[] output = ComboList.writeCombos(combos, actualBytes, false);
        String actual = ComboList.comboBytesToString(output);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void testWriteSameCombosTwoTables() throws Exception {
        String expected = """
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
                ----------------------NEXT COMBO TABLE----------------------
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
        byte[] expectedBytes = ComboList.comboStringToBytes(expected, 0xC);
        byte[] actualBytes = Arrays.copyOf(expectedBytes, expectedBytes.length);
        String combos = ComboList.comboBytesToString(expectedBytes);
        byte[] output = ComboList.writeCombos(combos, actualBytes, false);
        String actual = ComboList.comboBytesToString(output);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void testCountUnusedBytes() throws Exception {
        byte[] bytes = ByteUtils.hexStringToBytes("0000000011111111");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(4);
        bytes = ByteUtils.hexStringToBytes("000000000000000011111111");
        assertThat(ComboList.countUnusedBytes(bytes, 4)).isEqualTo(4);
        bytes = ByteUtils.hexStringToBytes("12345678123456780000000010000000");
        assertThat(ComboList.countUnusedBytes(bytes, 8)).isEqualTo(4);
        bytes = ByteUtils.hexStringToBytes("BBBBBBBBCCCCCCCC00000000");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(0xC);
        bytes = ByteUtils.hexStringToBytes("00000000CCCCCCCCBBBBBBBB");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(0xC);
        bytes = ByteUtils.hexStringToBytes("CCCCCCCC00000000BBBBBBBB");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(0xC);
        bytes = ByteUtils.hexStringToBytes("CCCCCCCCFFFFFFFFFFFFFFFF");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(0xC);
        bytes = ByteUtils.hexStringToBytes("CCCCCCCCFFFFFFFFFFFFFFFF33333333");
        assertThat(ComboList.countUnusedBytes(bytes, 0)).isEqualTo(0xC);
    }

    @Test
    public void testAnkoComboStringToBytes() throws Exception {
        String input = """
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
        byte[] expected = ByteUtils.hexStringToBytes("0000001498419265825000003030424242424242000000009841926582510000" +
                "303042424242424100000000984192658252000030304242424241009841926582530000303042424241000098419265" +
                "825400003030364242420000984192658255000030303642424147009841926582560000303036424100000098419265" +
                "82570000303034424200000098419265825800003030344241470000984192658250824F000000003030324242424242" +
                "000000009841926582508250000000003030324242424241000000009841926582508251000000003030324242424100" +
                "984192658250825200000000303032424241000098419265825082530000000030303242414141009841926582508254" +
                "000000003030384241000000984192658250825500000000303036414141000098419265825082560000000030304442" +
                "420000009841926582508257000000003030444241000000984192658250825800000000303048420000000098419265" +
                "8251824F000000003030484100000000FFFFFFFF");
        byte[] actual = ComboList.comboStringToBytes(input, 0x25CA0);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testNarutoComboStringToBytes() throws Exception {
        String input = """
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
                ----------------------NEXT COMBO TABLE----------------------
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
        byte[] expected = ByteUtils.hexStringToBytes("0000001198419265825000003030424242424242000000009841926582510000" +
                "303042424242424100000000984192658252000030304241414141009841926582530000303042414142424242420000" +
                "984192658254000030304241414242424241000098419265825500003030424142420000984192658256000030304241" +
                "424100009841926582570000303036424200000098419265825800003030364241000000984192658250824F00000000" +
                "303032424141410098419265825082500000000030303242414242424242000098419265825082510000000030303242" +
                "414242424241000098419265825082520000000030304441414141009841926582508253000000003030444141424242" +
                "424200009841926582508254000000003030444141424242424100009841926582508255000000003030444142420000" +
                "9841926582508256000000003030444142410000FFFFFFFF0000000000000000000000000000001C9841926582500000" +
                "303042424242424200000000984192658251000030304242424241009841926582520000303042424241000098419265" +
                "825300003030424141414100984192658254000030304241424200009841926582550000303042414142424242000000" +
                "984192658256000030304241414241009841926582570000303042414142424100000000984192658258000030304141" +
                "41410000984192658250824F000000003030414142424242000000009841926582508250000000003030414142424100" +
                "984192658250825100000000303041414241000098419265825082520000000030304142420000009841926582508253" +
                "000000003030364242424242420000009841926582508254000000003030364242424241000000009841926582508255" +
                "000000003030364242424100984192658250825600000000303036424141000098419265825082570000000030303242" +
                "4242424242000000984192658250825800000000303032424242424100000000984192658251824F0000000030303242" +
                "424241009841926582518250000000003030324241410000984192658251825100000000303038424242420098419265" +
                "825182520000000030303842424100009841926582518253000000003030364141410000984192658251825400000000" +
                "303036414242424200000000984192658251825500000000303036414242410098419265825182560000000030303641" +
                "424100009841926582518257000000003030384141000000FFFFFFFF");
        byte[] actual = ComboList.comboStringToBytes(input, 0x30F9C);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testCountCombosInEachTable() throws Exception {
        String combosOneTable = """
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
        assertThat(ComboList.countCombosInEachTable(combosOneTable)).isEqualTo(List.of(20));

        String combosTwoTables = """
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
                ----------------------NEXT COMBO TABLE----------------------
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
        assertThat(ComboList.countCombosInEachTable(combosTwoTables)).isEqualTo(List.of(17, 28));

        String brokenComboTable = """
                連弾１
                00BBBBBB
                連弾２
                00BBBBBA
                連弾３
                """.strip();
        assertThrows(IOException.class, () -> ComboList.countCombosInEachTable(brokenComboTable));
    }

    @Test
    public void testAnkoCombosBytesToString() throws Exception {
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
        String actualCombos = ComboList.comboBytesToString(bytes);
        assertThat(actualCombos).isEqualTo(expectedCombos);
    }

    @Test
    public void testNarutoComboBytesToString() throws Exception {
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
                ----------------------NEXT COMBO TABLE----------------------
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
        String actualCombos = ComboList.comboBytesToString(bytes);
        assertThat(actualCombos).isEqualTo(expectedCombos);
    }

    @Test
    public void testOboroComboBytesToString() throws Exception {
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
        String actualCombos = ComboList.comboBytesToString(bytes);
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
