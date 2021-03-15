package com.github.nicholasmoser.gnt4;

import static java.util.Map.entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GNT4Characters {

    public static final int PLAYABLE_CHARACTERS = 39;
    public static final String AKAMARU = "Akamaru";
    public static final String ANKO = "Anko";
    public static final String CHOJI = "Choji";
    public static final String GAARA = "Gaara";
    public static final String HAKU = "Haku";
    public static final String HINATA = "Hinata";
    public static final String HINATA_AWAKENED = "Hinata (Awakened)";
    public static final String INO = "Ino";
    public static final String IRUKA = "Iruka";
    public static final String ITACHI = "Itachi";
    public static final String JIRAIYA = "Jiraiya";
    public static final String JIROBO = "Jirobo";
    public static final String KABUTO = "Kabuto";
    public static final String KAKASHI = "Kakashi";
    public static final String KANKURO = "Kankuro";
    public static final String KARASU = "Karasu";
    public static final String KIBA = "Kiba";
    public static final String KIDOMARU = "Kidomaru";
    public static final String KIMIMARO = "Kimimaro";
    public static final String KISAME = "Kisame";
    public static final String LEE = "Lee";
    public static final String MIGHT_GUY = "Might Guy";
    public static final String MIZUKI = "Mizuki";
    public static final String NARUTO = "Naruto";
    public static final String NARUTO_OTK = "Naruto (OTK)";
    public static final String NEJI = "Neji";
    public static final String OBORO = "Oboro";
    public static final String OROCHIMARU = "Orochimaru";
    public static final String SAKON = "Sakon";
    public static final String SAKURA = "Sakura";
    public static final String SARUTOBI = "Sarutobi";
    public static final String SASUKE = "Sasuke";
    public static final String SASUKE_CS2 = "Sasuke (CS2)";
    public static final String SHIKAMARU = "Shikamaru";
    public static final String SHINO = "Shino";
    public static final String TAYUYA = "Tayuya";
    public static final String TAYUYA_DOKI = "Tayuya (Doki Demon)";
    public static final String TEMARI = "Temari";
    public static final String TENTEN = "Tenten";
    public static final String TSUNADE = "Tsunade";
    public static final String ZABUZA = "Zabuza";

    /**
     * All of the characters in the game in alphabetic order.
     */
    public static final List<String> CHARACTERS = List.of(
            AKAMARU,
            ANKO,
            CHOJI,
            GAARA,
            HAKU,
            HINATA,
            HINATA_AWAKENED,
            INO,
            IRUKA,
            ITACHI,
            JIRAIYA,
            JIROBO,
            KABUTO,
            KAKASHI,
            KANKURO,
            KARASU,
            KIBA,
            KIDOMARU,
            KIMIMARO,
            KISAME,
            LEE,
            MIGHT_GUY,
            MIZUKI,
            NARUTO,
            NARUTO_OTK,
            NEJI,
            OBORO,
            OROCHIMARU,
            SAKON,
            SAKURA,
            SARUTOBI,
            SASUKE,
            SASUKE_CS2,
            SHIKAMARU,
            SHINO,
            TAYUYA,
            TAYUYA_DOKI,
            TEMARI,
            TENTEN,
            TSUNADE,
            ZABUZA);

    /**
     * A mapping of each character to their internal integer representation. This number is determined
     * by when they were added to the game.
     */
    public static final BiMap<String, Integer> INTERNAL_CHAR_ORDER = new ImmutableBiMap.Builder<String, Integer>()
            .put(SASUKE, 0x01)
            .put(HAKU, 0x02)
            .put(KAKASHI, 0x03)
            .put(LEE, 0x04)
            .put(IRUKA, 0x05)
            .put(ZABUZA, 0x06)
            .put(SAKURA, 0x07)
            .put(NARUTO, 0x08)
            .put(INO, 0x09)
            .put(SHIKAMARU, 0x0A)
            .put(NEJI, 0x0B)
            .put(HINATA, 0x0C)
            .put(MIGHT_GUY, 0x0D)
            .put(KANKURO, 0x0E)
            .put(KARASU, 0x0F)
            .put(KIBA, 0x10)
            .put(AKAMARU, 0x11)
            .put(GAARA, 0x12)
            .put(OROCHIMARU, 0x13)
            .put(OBORO, 0x14)
            .put(MIZUKI, 0x15)
            .put(ANKO, 0x16)
            .put(JIRAIYA, 0x17)
            .put(CHOJI, 0x18)
            .put(TENTEN, 0x19)
            .put(TEMARI, 0x1A)
            .put(SHINO, 0x1B)
            .put(ITACHI, 0x1C)
            .put(TSUNADE, 0x1D)
            .put(SARUTOBI, 0x1E)
            .put(KIMIMARO, 0x1F)
            .put(JIROBO, 0x20)
            .put(KIDOMARU, 0x21)
            .put(SAKON, 0x22)
            .put(TAYUYA, 0x23)
            .put(KISAME, 0x24)
            .put(SASUKE_CS2, 0x25)
            .put(NARUTO_OTK, 0x26)
            .put(KABUTO, 0x27)
            .put(HINATA_AWAKENED, 0x28)
            .put(TAYUYA_DOKI, 0x29)
            .build();

    /**
     * A mapping of each character to their chr folder name.
     */
    public static final Map<String, String> CHAR_FOLDER = Map.ofEntries(
            entry(SASUKE, "sas" ),
            entry(HAKU, "hak" ),
            entry(KAKASHI, "kak" ),
            entry(LEE, "loc" ),
            entry(IRUKA, "iru" ),
            entry(ZABUZA, "zab" ),
            entry(SAKURA, "sak" ),
            entry(NARUTO, "nar" ),
            entry(INO, "ino" ),
            entry(SHIKAMARU, "sik" ),
            entry(NEJI, "nej" ),
            entry(HINATA, "hin" ),
            entry(MIGHT_GUY, "gai" ),
            entry(KANKURO, "kan" ),
            entry(KARASU, "kar" ),
            entry(KIBA, "kib" ),
            entry(AKAMARU, "dog" ),
            entry(GAARA, "gar" ),
            entry(OROCHIMARU, "oro" ),
            entry(OBORO, "obo" ),
            entry(MIZUKI, "miz" ),
            entry(ANKO, "ank" ),
            entry(JIRAIYA, "jir" ),
            entry(CHOJI, "cho" ),
            entry(TENTEN, "ten" ),
            entry(TEMARI, "tem" ),
            entry(SHINO, "sin" ),
            entry(ITACHI, "ita" ),
            entry(TSUNADE, "tsu" ),
            entry(SARUTOBI, "sar" ),
            entry(KIMIMARO, "kim" ),
            entry(JIROBO, "bou" ),
            entry(KIDOMARU, "kid" ),
            entry(SAKON, "sko" ),
            entry(TAYUYA, "tay" ),
            entry(KISAME, "kis" ),
            entry(SASUKE_CS2, "sa2" ),
            entry(NARUTO_OTK, "na9" ),
            entry(KABUTO, "kab" ),
            entry(HINATA_AWAKENED, "hi2" ),
            entry(TAYUYA_DOKI, "ta2" )
    );

    /**
     * All of the characters in the game except Tayuya, Kankuro, and Kiba in alphabetic order. Tayuya,
     * Kankuro, and Kiba cannot be main menu characters. This is due to the fact that they load
     * separate models that cause errors.
     */
    public static final List<String> MAIN_MENU_CHARS = List.of(
            AKAMARU,
            ANKO,
            CHOJI,
            GAARA,
            HAKU,
            HINATA,
            HINATA_AWAKENED,
            INO,
            IRUKA,
            ITACHI,
            JIRAIYA,
            JIROBO,
            KABUTO,
            KAKASHI,
            KARASU,
            KIDOMARU,
            KIMIMARO,
            KISAME,
            LEE,
            MIGHT_GUY,
            MIZUKI,
            NARUTO,
            NARUTO_OTK,
            NEJI,
            OBORO,
            OROCHIMARU,
            SAKON,
            SAKURA,
            SARUTOBI,
            SASUKE,
            SASUKE_CS2,
            SHIKAMARU,
            SHINO,
            TAYUYA_DOKI,
            TEMARI,
            TENTEN,
            TSUNADE,
            ZABUZA);

    /**
     * A mapping of each character to their best main menu selection sound.
     */
    public static final Map<String, Byte> CHAR_SEL_SOUND = Map.ofEntries(
            entry(SASUKE, (byte) 0x04),
            entry(HAKU, (byte) 0x22),
            entry(KAKASHI, (byte) 0x14),
            entry(LEE, (byte) 0x2A),
            entry(IRUKA, (byte) 0x14),
            entry(ZABUZA, (byte) 0x1B),
            entry(SAKURA, (byte) 0x27),
            entry(NARUTO, (byte) 0x13),
            entry(INO, (byte) 0x1A),
            entry(SHIKAMARU, (byte) 0x20),
            entry(NEJI, (byte) 0x24),
            entry(HINATA, (byte) 0x05),
            entry(MIGHT_GUY, (byte) 0x28),
            //entry(KANKURO, ?),
            entry(KARASU, (byte) 0x00),
            //entry(KIBA, ?),
            entry(AKAMARU, (byte) 0x03),
            entry(GAARA, (byte) 0x1B),
            entry(OROCHIMARU, (byte) 0x18),
            entry(OBORO, (byte) 0x16),
            entry(MIZUKI, (byte) 0x14),
            entry(ANKO, (byte) 0x23),
            entry(JIRAIYA, (byte) 0x26),
            entry(CHOJI, (byte) 0x1B),
            entry(TENTEN, (byte) 0x23),
            entry(TEMARI, (byte) 0x26),
            entry(SHINO, (byte) 0x17),
            entry(ITACHI, (byte) 0x14),
            entry(TSUNADE, (byte) 0x14),
            entry(SARUTOBI, (byte) 0x25),
            entry(KIMIMARO, (byte) 0x1B),
            entry(JIROBO, (byte) 0x1B),
            entry(KIDOMARU, (byte) 0x1B),
            entry(SAKON, (byte) 0x05),
            //entry(TAYUYA, ?),
            entry(KISAME, (byte) 0x14),
            entry(SASUKE_CS2, (byte) 0x1A),
            entry(NARUTO_OTK, (byte) 0x20),
            entry(KABUTO, (byte) 0x05),
            entry(HINATA_AWAKENED, (byte) 0x14),
            entry(TAYUYA_DOKI, (byte) 0x00)
    );

    /**
     * A map containing the height adjustments for main menu characters. The default value is 0xE5
     */
    public static final Map<String, Byte> CHAR_HEIGHT_ADJUST = Map.ofEntries(
            entry(SASUKE, (byte) 0xDF),
            entry(HAKU, (byte) 0xD6),
            entry(KAKASHI, (byte) 0xD0),
            entry(LEE, (byte) 0xD8),
            entry(IRUKA, (byte) 0xD0),
            entry(ZABUZA, (byte) 0xC2),
            entry(SAKURA, (byte) 0xE5),
            entry(NARUTO, (byte) 0xE5),
            entry(INO, (byte) 0xE5),
            entry(SHIKAMARU, (byte) 0xE2),
            entry(NEJI, (byte) 0xDE),
            entry(HINATA, (byte) 0xE5),
            entry(MIGHT_GUY, (byte) 0xCD),
            //entry(KANKURO, ?),
            entry(KARASU, (byte) 0xD0),
            //entry(KIBA, ?),
            entry(AKAMARU, (byte) 0xE5),
            entry(GAARA, (byte) 0xE7),
            entry(OROCHIMARU, (byte) 0xCF),
            entry(OBORO, (byte) 0xD8),
            entry(MIZUKI, (byte) 0xD0),
            entry(ANKO, (byte) 0xD5),
            entry(JIRAIYA, (byte) 0xB6),
            entry(CHOJI, (byte) 0xED),
            entry(TENTEN, (byte) 0xE2),
            entry(TEMARI, (byte) 0xE1),
            entry(SHINO, (byte) 0xDE),
            entry(ITACHI, (byte) 0xD6),
            entry(TSUNADE, (byte) 0xD6),
            entry(SARUTOBI, (byte) 0xD7),
            entry(KIMIMARO, (byte) 0xD8),
            entry(JIROBO, (byte) 0xCA),
            entry(KIDOMARU, (byte) 0xD6),
            entry(SAKON, (byte) 0xD8),
            //entry(TAYUYA, ?),
            entry(KISAME, (byte) 0xB8),
            entry(SASUKE_CS2, (byte) 0xDF),
            entry(NARUTO_OTK, (byte) 0xE5),
            entry(KABUTO, (byte) 0xD0),
            entry(HINATA_AWAKENED, (byte) 0xE5),
            entry(TAYUYA_DOKI, (byte) 0x88)
    );

    /**
     * This is the default order of the chr folders found at address 0x802788c8 (dol offset 220CC8).
     */
    public static final List<String> DEFAULT_CHR_FOLDER_ORDER = List.of(
            "cmn",
            "sas",
            "hak",
            "kak",
            "loc",
            "iru",
            "zab",
            "sak",
            "nar",
            "ino",
            "sik",
            "nej",
            "hin",
            "gai",
            "kan",
            "kar",
            "kib",
            "dog",
            "gar",
            "oro",
            "obo",
            "miz",
            "ank",
            "jir",
            "cho",
            "ten",
            "tem",
            "sin",
            "ita",
            "tsu",
            "sar",
            "kim",
            "bou",
            "kid",
            "sko",
            "tay",
            "kis",
            "sa2",
            "na9",
            "kab",
            "hi2",
            "ta2"
    );

    /**
     * This list holds the character IDs for the CSS. The order is determined by indices in {@link #CSS_CHR_ID_ORDER}.
     * Contained at address 0x80214018 or dol offset 0x211018.
     */
    public static final List<Integer> CSS_CHR_IDS = List.of(
            0x08,
            0x01,
            0x07,
            0x03,
            0x05,
            0x06,
            0x02,
            0x04,
            0x0E,
            0x12,
            0x0B,
            0x0D,
            0x09,
            0x0A,
            0x10,
            0x0C,
            0x13,
            0x11,
            0x19,
            0x1B,
            0x18,
            0x1A,
            0x17,
            0x1D,
            0x1E,
            0x1C,
            0x0F,
            0x15,
            0x16,
            0x1F,
            0x22,
            0x23,
            0x20,
            0x21,
            0x24,
            0x25,
            0x26,
            0x27,
            0x28
    );

    /**
     * This list is the order of the characters in the CSS. Each value is an index for {@link #CSS_CHR_IDS}, which
     * holds the character ID.
     * Contained at address 0x80213f7c or dol offset 0x210F7C.
     */
    public static final List<Integer> CSS_CHR_ID_ORDER = List.of(
            0x00,
            0x01,
            0x02,
            0x03,
            0x0A,
            0x07,
            0x12,
            0x0B,
            0x0D,
            0x14,
            0x0C,
            0x13,
            0x0E,
            0x0F,
            0x11,
            0x09,
            0x08,
            0x15,
            0x1A,
            0x06,
            0x05,
            0x04,
            0x1B,
            0x1C,
            0x18,
            0x16,
            0x17,
            0x10,
            0x20,
            0x21,
            0x1E,
            0x1F,
            0x1D,
            0x25,
            0x19,
            0x22,
            0x23,
            0x24,
            0x26
    );

    /**
     * This is the order of characters in the CSS.
     */
    public static final List<String> DEFAULT_CSS_ORDER = List.of(
            NARUTO,
            SASUKE,
            SAKURA,
            KAKASHI,
            NEJI,
            LEE,
            TENTEN,
            MIGHT_GUY,
            SHIKAMARU,
            CHOJI,
            INO,
            SHINO,
            KIBA,
            HINATA,
            AKAMARU,
            GAARA,
            KANKURO,
            TEMARI,
            KARASU,
            HAKU,
            ZABUZA,
            IRUKA,
            MIZUKI,
            ANKO,
            SARUTOBI,
            JIRAIYA,
            TSUNADE,
            OROCHIMARU,
            JIROBO,
            KIDOMARU,
            SAKON,
            TAYUYA,
            KIMIMARO,
            KABUTO,
            ITACHI,
            KISAME,
            SASUKE_CS2,
            NARUTO_OTK,
            HINATA_AWAKENED
    );
}
