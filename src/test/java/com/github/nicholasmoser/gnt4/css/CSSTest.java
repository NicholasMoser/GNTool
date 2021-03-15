package com.github.nicholasmoser.gnt4.css;

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
}
