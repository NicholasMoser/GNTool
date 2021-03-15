package com.github.nicholasmoser.gnt4.css;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CSS {
    private static final Logger LOGGER = Logger.getLogger(CSS.class.getName());
    // The begin and end of where new CSS values are written. Approximately 0xAB8 bytes.
    private static final int OVERRIDE_BEGIN_ADDRESS = 0x80196824;
    private static final int OVERRIDE_END_ADDRESS = 0x801972dc;
    private static final int OVERRIDE_BEGIN_OFFSET = 0x193824;
    private static final int OVERRIDE_END_OFFSET = 0x1942DC;
    private static final int OVERRIDE_LENGTH = 0xAB8; // 2744

    // The terminator for the new override data structures
    private static final byte[] TERMINATOR = new byte[]{0x45, 0x4E, 0x44, 0x21};

    // The single instruction of EXI2_Init, which is the instruction at the beginning of the override area.
    // Used to determine if the dol has had this area overridden yet.
    private static final int OVERRIDE_BEGIN_DEFAULT = 0x4e800020;

    // The dol offset of the instruction that sets the number of characters as well as the default values of
    // this instruction. The value 0xd is multiplied by 3 to get the total number, 39.
    private static final int NUMBER_OF_CHARS_OFFSET = 158820;
    private static final int NUMBER_OF_CHARS_DEFAULT = 0x3800000d;

    // lis r3, 0x8019
    private static final byte[] LIS_R3_8019 = new byte[]{0x3C, (byte) 0x60, (byte) 0x80, 0x19};
    // lis r4, 0x8019
    private static final byte[] LIS_R4_8019 = new byte[]{0x3C, (byte) 0x80, (byte) 0x80, 0x19};
    // lis r6, 0x8019
    private static final byte[] LIS_R6_8019 = new byte[]{0x3C, (byte) 0xC0, (byte) 0x80, 0x19};
    // nop
    private static final byte[] NOP = new byte[]{0x60, 0x00, 0x00, 0x00};


    private Path dolPath;
    private boolean isUsingOverride;
    private List<String> chrFolderOrder;
    private List<Integer> cssChrIds;
    private List<Integer> cssChrIdOrder;
    private int currentChrIndex;

    public static CSS of(Path dolPath) throws Exception {
        byte[] dol = Files.readAllBytes(dolPath);
        int overrideFirstWord = ByteUtils.toInt32(dol, OVERRIDE_BEGIN_OFFSET);
        if (overrideFirstWord == OVERRIDE_BEGIN_DEFAULT) {
            LOGGER.info("Loaded CSS has not yet been modified.");
            return new CSS(dolPath);
        }
        LOGGER.info("Loaded CSS has been modified.");
        return null; // TODO: Create CSS for modified dol
    }

    /**
     * Creates a default CSS, in cases where the CSS has not yet been overridden.
     *
     * @param dolPath The path to the dol file.
     */
    private CSS(Path dolPath) {
        this.dolPath = dolPath;
        this.isUsingOverride = false;
        this.chrFolderOrder = new ArrayList<>(GNT4Characters.DEFAULT_CHR_FOLDER_ORDER);
        this.cssChrIds = new ArrayList<>(GNT4Characters.CSS_CHR_IDS);
        this.cssChrIdOrder = new ArrayList<>(GNT4Characters.CSS_CHR_ID_ORDER);
        this.currentChrIndex = 0x2a;
    }

    /**
     * @return The modifiable list of the chr folder order.
     */
    public List<String> getChrFolderOrder() {
        return chrFolderOrder;
    }

    public boolean isUsingOverride() {
        return isUsingOverride;
    }

    public void addNewChr(String chr) {
        if (chr.getBytes(StandardCharsets.US_ASCII).length != 3) {
            throw new IllegalArgumentException(chr + " does not have a length of 3 ASCII characters");
        }
        chrFolderOrder.add(chr);
        // TODO: Use currentChrIndex
        currentChrIndex++;
    }

    /**
     * Apply the CSS data to the dol file.
     *
     * @throws IOException If the dol file cannot be modified.
     */
    public void apply() throws IOException {
        verifyValidity();
        byte[] dol = Files.readAllBytes(dolPath);
        clearOverride(dol);
        int length = 0;
        length += addChrFolderOrder(dol);
        length += addPtrChrFolderOrder(dol, length);
        length += addCssChrIds(dol, length);
        length += addCssChrIdOrder(dol, length);
        addActualCssChrIdOrder(dol, length);
        updateNumberOfCharacters(dol);
        disableCharacterUnlockChecks(dol);
        Files.write(dolPath, dol);
        isUsingOverride = true;
    }

    /**
     * Validates that the CSS data is valid and throws an IllegalStateException if not.
     */
    private void verifyValidity() {
        // Due to the way the code works, characters must be added in batches of three. Validate this is the case.
        if (chrFolderOrder.size() % 3 != 0) {
            throw new IllegalStateException("The number of new characters must be added in batches of threes.");
        }
        // Validate each of the chr folders it expects to exist do in fact exist.
        Path chrDirectory = dolPath.getParent().getParent().resolve("files/chr");
        for (String chrFolder : chrFolderOrder) {
            Path chrFolderPath = chrDirectory.resolve(chrFolder);
            if (!Files.isDirectory(chrFolderPath)) {
                throw new IllegalStateException(chrFolderPath + " does not exist for character.");
            }
        }
        // Validate that there is enough space for the CSS override
        int totalSize = (chrFolderOrder.size() * 4) + 4;
        totalSize += (chrFolderOrder.size() * 4) + 4; // Calculate a second time for PTR_CHAR_ORDER
        totalSize += (cssChrIds.size() * 4) + 4;
        totalSize += (cssChrIdOrder.size() * 4) + 4;
        totalSize += (cssChrIdOrder.size() * 4) + 4; // Calculate a second time for ACTUAL_CSS_CHR_ID_ORDER
        if (totalSize > OVERRIDE_LENGTH) {
            String message = "CSS override is too large. ";
            message += String.format("It uses 0x%08x bytes out of a max of 0x%03x", totalSize, OVERRIDE_LENGTH);
            throw new IllegalStateException(message);
        }
        LOGGER.info(String.format("CSS override will use 0x%03x bytes out of 0x%03x", totalSize, OVERRIDE_LENGTH));
        // Validate each chr folder is 3 ASCII bytes in length
        for (String chrFolder : chrFolderOrder) {
            if (chrFolder.getBytes(StandardCharsets.US_ASCII).length != 3) {
                throw new IllegalStateException(chrFolder + " does not have a length of 3 ASCII characters");
            }
        }
    }

    /**
     * Adds the data for the chr folder order to the dol. The original location of this data is at address 0x802788c8
     * or dol offset 0x220CC8.
     *
     * @param dol The dol bytes to write to.
     * @return The length of this portion of the override area.
     */
    private int addChrFolderOrder(byte[] dol) {
        int startOffset = OVERRIDE_BEGIN_OFFSET;
        for (int i = 0; i < chrFolderOrder.size(); i++) {
            int currentOffset = startOffset + (i * 4);
            String chrFolder = chrFolderOrder.get(i);
            byte[] chrFolderBytes = chrFolder.getBytes(StandardCharsets.US_ASCII);
            System.arraycopy(chrFolderBytes, 0, dol, currentOffset, 3);
        }
        int size = chrFolderOrder.size() * 4;
        System.arraycopy(TERMINATOR, 0, dol, startOffset + size, 4);
        return size + 4;
    }

    /**
     * Adds the data for the pointers to the chr folder order to the dol. This will also update the related instructions
     * that operate on this data. The original location of this data is at address 0x80208800 or dol offset 0x205800.
     *
     * @param dol    The dol bytes to write to.
     * @param length The current length of the override area.
     * @return The length of this portion of the override area.
     */
    private int addPtrChrFolderOrder(byte[] dol, int length) {
        int startAddress = OVERRIDE_BEGIN_ADDRESS + length;
        int startOffset = OVERRIDE_BEGIN_OFFSET + length;
        for (int i = 0; i < chrFolderOrder.size(); i++) {
            int currentOffset = startOffset + (i * 4);
            byte[] bytes = ByteUtils.fromInt(OVERRIDE_BEGIN_ADDRESS + (i * 4));
            System.arraycopy(bytes, 0, dol, currentOffset, 4);
        }
        int size = chrFolderOrder.size() * 4;
        System.arraycopy(TERMINATOR, 0, dol, startOffset + size, 4);
        updatePtrChrFolderOrderInstructions(dol, startAddress);
        return size + 4;
    }

    /**
     * Update the instructions that use the pointers to the chr folder order.
     *
     * @param dol          The dol bytes to write to.
     * @param startAddress The start address of the pointers to the chr folder order.
     */
    private void updatePtrChrFolderOrderInstructions(byte[] dol, int startAddress) {
        // New instruction: addi r5, r4, {offset from 0x80190000}
        byte[] addi_instruction = new byte[]{0x38, (byte) 0xA4, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 80040198 lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D198, 4);
        // 800401a0 subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D1A0, 4);
        // 800401f8 lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D1F8, 4);
        // 80040200 subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D200, 4);
        // 80040230 lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D230, 4);
        // 80040238 subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D238, 4);
        // 8004041c lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D41C, 4);
        // 80040424 subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D424, 4);
        // 8004047c lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D47C, 4);
        // 80040484 subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D484, 4);
        // 800406c4 lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x3D6C4, 4);
        // 800406cc subi	r5, r4, 30720
        System.arraycopy(addi_instruction, 0, dol, 0x3D6CC, 4);
    }

    /**
     * Adds the data for the css chr ids to the dol. This will also update the related instructions that operate
     * on this data. The original location of this data is at address 0x80214018 or dol offset 0x211018.
     *
     * @param dol    The dol bytes to write to.
     * @param length The current length of the override area.
     * @return The length of this portion of the override area.
     */
    private int addCssChrIds(byte[] dol, int length) {
        int startAddress = OVERRIDE_BEGIN_ADDRESS + length;
        int startOffset = OVERRIDE_BEGIN_OFFSET + length;
        for (int i = 0; i < cssChrIds.size(); i++) {
            int currentOffset = startOffset + (i * 4);
            byte[] bytes = ByteUtils.fromInt(cssChrIds.get(i));
            System.arraycopy(bytes, 0, dol, currentOffset, 4);
        }
        int size = cssChrIds.size() * 4;
        System.arraycopy(TERMINATOR, 0, dol, startOffset + size, 4);
        updateCssChrIdsInstructions(dol, startAddress);
        return size + 4;
    }

    /**
     * Update the instructions that use the css chr ids.
     *
     * @param dol          The dol bytes to write to.
     * @param startAddress The start address of the css chr ids.
     */
    private void updateCssChrIdsInstructions(byte[] dol, int startAddress) {
        // New instruction: addi r4, r4, {offset from 0x80190000}
        byte[] addi_instruction = new byte[]{0x38, (byte) 0x84, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 8015b824 lis	r4, 0x8021
        System.arraycopy(LIS_R4_8019, 0, dol, 0x158824, 4);
        // 8015b834 addi	r4, r4, 16408
        System.arraycopy(addi_instruction, 0, dol, 0x158834, 4);
    }

    /**
     * Adds the data for the css chr id order to the dol. This will also update the related instructions that operate
     * on this data. The original location of this data is at address 0x80213f7c or dol offset 0x210F7C.
     *
     * @param dol    The dol bytes to write to.
     * @param length The current length of the override area.
     * @return The length of this portion of the override area.
     */
    private int addCssChrIdOrder(byte[] dol, int length) {
        int startAddress = OVERRIDE_BEGIN_ADDRESS + length;
        int startOffset = OVERRIDE_BEGIN_OFFSET + length;
        for (int i = 0; i < cssChrIdOrder.size(); i++) {
            int currentOffset = startOffset + (i * 4);
            byte[] bytes = ByteUtils.fromInt(cssChrIdOrder.get(i));
            System.arraycopy(bytes, 0, dol, currentOffset, 4);
        }
        int size = cssChrIdOrder.size() * 4;
        System.arraycopy(TERMINATOR, 0, dol, startOffset + size, 4);
        updateCssChrIdOrderInstructions(dol, startAddress);
        return size + 4;
    }

    /**
     * Update the instructions that use the css chr id order.
     *
     * @param dol          The dol bytes to write to.
     * @param startAddress The start address of the css chr id order.
     */
    private void updateCssChrIdOrderInstructions(byte[] dol, int startAddress) {
        // New instruction: addi r8, r6, {offset from 0x80190000}
        byte[] addi_instruction = new byte[]{0x39, (byte) 0x06, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 8015b818 lis	r6, 0x8021
        System.arraycopy(LIS_R6_8019, 0, dol, 0x158818, 4);
        // 8015b828 addi	r8, r6, 16252
        System.arraycopy(addi_instruction, 0, dol, 0x158828, 4);
    }

    /**
     * Adds the data for the actual css chr id order to the dol. This will also update the related instructions that operate
     * on this data. The original location of this data is at address 0x80243c60 in the data section (therefore there
     * is no dol offset).
     *
     * @param dol    The dol bytes to write to.
     * @param length The current length of the override area.
     * @return The length of this portion of the override area.
     */
    private int addActualCssChrIdOrder(byte[] dol, int length) {
        int startAddress = OVERRIDE_BEGIN_ADDRESS + length;
        int startOffset = OVERRIDE_BEGIN_OFFSET + length;
        // This chunk in the dol is used as a data section and is therefore not initialized to anything.
        // The game will write to this space while it is running.
        int size = cssChrIdOrder.size() * 4;
        System.arraycopy(TERMINATOR, 0, dol, startOffset + size, 4);
        updateActualCssChrIdOrderInstructions(dol, startAddress);
        return size + 4;
    }

    /**
     * Update the instructions that use the actual css chr id order.
     *
     * @param dol          The dol bytes to write to.
     * @param startAddress The start address of the actual css chr id order.
     */
    private void updateActualCssChrIdOrderInstructions(byte[] dol, int startAddress) {
        // New instruction: addi r5, r4, {offset from 0x80190000}
        byte[] addi_instruction = new byte[]{0x38, (byte) 0xa4, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 8015b814 lis	r4, 0x8024
        System.arraycopy(LIS_R4_8019, 0, dol, 0x158814, 4);
        // 8015b81c addi	r5, r4, 15456
        System.arraycopy(addi_instruction, 0, dol, 0x15881C, 4);
        // New instruction: addi r0, r3, {offset from 0x80190000}
        addi_instruction = new byte[]{0x38, (byte) 0x03, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 8015a8f8 lis	r3, 0x8024
        System.arraycopy(LIS_R3_8019, 0, dol, 0x1578F8, 4);
        // 8015a948 addi	r0, r3, 15456
        System.arraycopy(addi_instruction, 0, dol, 0x157948, 4);
        // 8015b1c4 lis	r3, 0x8024
        System.arraycopy(LIS_R3_8019, 0, dol, 0x1581C4, 4);
        // 8015b1f4 addi	r0, r3, 15456
        System.arraycopy(addi_instruction, 0, dol, 0x1581F4, 4);
        // New instruction: addi r4, r4, {offset from 0x80190000}
        addi_instruction = new byte[]{0x38, (byte) 0x84, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)};
        // 8015b9bc lis	r4, 0x8024
        System.arraycopy(LIS_R4_8019, 0, dol, 0x1589BC, 4);
        // 8015b9c4 addi	r4, r4, 15456
        System.arraycopy(addi_instruction, 0, dol, 0x1589C4, 4);
    }

    /**
     * Updates the number of characters to read into the CSS. The number of characters must be a multiple of three,
     * as characters are read into the CSS in batches of three.
     *
     * @param dol The bytes of the dol.
     */
    private void updateNumberOfCharacters(byte[] dol) {
        int base = 0xd;
        int newChars = (cssChrIdOrder.size() - GNT4Characters.PLAYABLE_CHARACTERS) / 3;
        if (newChars > 0) {
            LOGGER.info((newChars * 3) + " new characters found.");
        } else {
            LOGGER.info("No new characters found.");
        }
        byte[] instruction = new byte[]{0x38, 0x00, 0x00, (byte) (base + newChars)};
        // 8015b820 li	r0, 0xd
        System.arraycopy(instruction, 0, dol, 0x158820, 4);
    }

    /**
     * Disable the checks for whether specific characters are unlocked or not. This is necessary for new characters
     * to show up in the CSS.
     *
     * @param dol The bytes of the dol.
     */
    private void disableCharacterUnlockChecks(byte[] dol) {
        // 8015b864 beq-	 ->0x8015B87C
        System.arraycopy(NOP, 0, dol, 0x158864, 4);
        // 8015b8a0 beq-	 ->0x8015B8B8
        System.arraycopy(NOP, 0, dol, 0x1588A0, 4);
        // 8015b8dc beq-	 ->0x8015B8F4
        System.arraycopy(NOP, 0, dol, 0x1588DC, 4);
    }

    /**
     * Zeroes out the override section for the CSS.
     *
     * @param dol The bytes of the dol.
     */
    private void clearOverride(byte[] dol) {
        for (int i = OVERRIDE_BEGIN_OFFSET; i < OVERRIDE_END_OFFSET; i++) {
            dol[i] = 0;
        }
    }
}
