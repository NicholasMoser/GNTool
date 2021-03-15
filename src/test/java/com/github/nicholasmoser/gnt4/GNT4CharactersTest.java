package com.github.nicholasmoser.gnt4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GNT4CharactersTest {

    /**
     * Tests that the order of the CSS is correct for the CSS related data structures.
     */
    @Test
    public void testCSSOrder() {
        List<String> actualCSS = new ArrayList<>();
        for (int index : GNT4Characters.CSS_CHR_ID_ORDER) {
            int chrId = GNT4Characters.CSS_CHR_IDS.get(index);
            actualCSS.add(GNT4Characters.INTERNAL_CHAR_ORDER.inverse().get(chrId));
        }
        assertEquals(GNT4Characters.DEFAULT_CSS_ORDER, actualCSS);
    }
}
