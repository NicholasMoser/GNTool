package com.github.nicholasmoser.gnt4.css;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.google.common.collect.BiMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CSSTest {

    /**
     * Tests modifing a vanilla dol to use the CSS override, but with no characters added.
     *
     * @throws Exception If any Exception occurs.
     */
    @Test
    public void testOverrideDefault() throws Exception {
        Path testDirectory = Paths.get("D:/GNT/vanilla/uncompressed/sys");
        Path baseDol = testDirectory.resolve("base.dol");
        Path testDol = testDirectory.resolve("main.dol");
        Files.copy(baseDol, testDol, StandardCopyOption.REPLACE_EXISTING);
        CSS css = CSS.of(testDol);
        css.apply();
    }

    @Test
    public void createNewCharacterOrder() {
        List<Integer> cssChrIdOrder = GNT4Characters.CSS_CHR_ID_ORDER;
        List<Integer> cssChrIds = GNT4Characters.CSS_CHR_IDS;
        BiMap<String, Integer> chrToId = GNT4Characters.INTERNAL_CHAR_ORDER;

        // The new order
        List<String> newChrs = new ArrayList<>();
        newChrs.add(GNT4Characters.NARUTO);
        newChrs.add(GNT4Characters.NARUTO_OTK);
        newChrs.add(GNT4Characters.SASUKE);
        newChrs.add(GNT4Characters.SASUKE_CS2);
        newChrs.add(GNT4Characters.SAKURA);
        newChrs.add(GNT4Characters.KAKASHI);
        newChrs.add(GNT4Characters.NEJI);
        newChrs.add(GNT4Characters.LEE);
        newChrs.add(GNT4Characters.TENTEN);
        newChrs.add(GNT4Characters.MIGHT_GUY);
        newChrs.add(GNT4Characters.SHIKAMARU);
        newChrs.add(GNT4Characters.CHOJI);
        newChrs.add(GNT4Characters.INO);
        newChrs.add(GNT4Characters.SHINO);
        newChrs.add(GNT4Characters.KIBA);
        newChrs.add(GNT4Characters.HINATA);
        newChrs.add(GNT4Characters.HINATA_AWAKENED);
        newChrs.add(GNT4Characters.GAARA);
        newChrs.add(GNT4Characters.TEMARI);
        newChrs.add(GNT4Characters.KANKURO);
        newChrs.add(GNT4Characters.KARASU);
        newChrs.add(GNT4Characters.HAKU);
        newChrs.add(GNT4Characters.ZABUZA);
        newChrs.add(GNT4Characters.IRUKA);
        newChrs.add(GNT4Characters.MIZUKI);
        newChrs.add(GNT4Characters.ANKO);
        newChrs.add(GNT4Characters.SARUTOBI);
        newChrs.add(GNT4Characters.JIRAIYA);
        newChrs.add(GNT4Characters.TSUNADE);
        newChrs.add(GNT4Characters.OROCHIMARU);
        newChrs.add(GNT4Characters.KABUTO);
        newChrs.add(GNT4Characters.JIROBO);
        newChrs.add(GNT4Characters.KIDOMARU);
        newChrs.add(GNT4Characters.SAKON);
        newChrs.add(GNT4Characters.TAYUYA);
        newChrs.add(GNT4Characters.KIMIMARO);
        newChrs.add(GNT4Characters.ITACHI);
        newChrs.add(GNT4Characters.KISAME);
        newChrs.add(GNT4Characters.AKAMARU);

        // Create Gecko code for new indices
        for (int i = 0; i < newChrs.size(); i++) {
            String newChr = newChrs.get(i);
            int id = chrToId.get(newChr);
            int oldId = cssChrIds.get(cssChrIdOrder.get(i));
            if (id != oldId) {
                int index = cssChrIds.indexOf(id);
                if (index == -1) {
                    throw new RuntimeException("Unable to find " + newChr + " with id " + id);
                }
                int base = 0x3f7c;
                int offset = i * 4;
                System.out.printf("0421%X %08X\n", base + offset, index);
            }
        }
    }
}
