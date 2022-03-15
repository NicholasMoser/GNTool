# Sequence Options

- [Disassemble SEQ](#disassemble-seq)
- [Modify SEQ](#modify-seq)
- [Expand SEQ](#expand-seq)

![Sequence](/docs/sequence.png?raw=true "Sequence")

The main code of GNT games can be found in the file `main.dol`, which is PowerPC assembly. Contained in the main code of the game is an interpreter for an entirely different kind of assembly informally called **Sequence**. Sequence assembly can be found in `.seq` files. It is a proprietary format created by the company Eighting. It has no public documentation, therefore all provided documentation has been written via reverse engineering.

## Disassemble SEQ

This will [disassemble](https://en.wikipedia.org/wiki/Disassembler) the selected SEQ file bytecode into human-readable opcodes. The output of this can either be an HTML or plain text (TXT) report.

## Modify SEQ

This opens an SEQ Editor that allows you to modify SEQ files. Much like [Gecko Codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md), these modifications will branch from a certain offset in the SEQ file, run your new bytecode, and branch back.

![SEQ Editor](/docs/seqeditor.png?raw=true "SEQ Editor")

To create a new edit, go to `File->New Edit` or press `Ctrl + N`.

Here are the fields to fill out for a new code:

- **Name**: The name/description of your code. There is no size limit.
- **Offset**: Where in the SEQ file to branch from. Make sure that it is at the start of an opcode.
- **Hijacked Bytes Length**: This is how long the branch opcode should be (the branch to your new inserted code). It must be a multiple of 4 bytes and a minimum of 8 bytes. This is because a [Branch Instruction](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/opcode_group/01.md#0132---b) is 8 bytes long (e.g. `01320000 00002310`). The reason that more than 8 bytes is allowed here is because SEQ bytecode allows variable length opcodes, so 8 bytes may end in the middle of an opcode. By specifying a larger number, padding is inserted to prevent accidentally parsing an opcode in the middle of the opcode.
- **New Bytes**: The SEQ bytecode to insert (in hex).

The **Hijacked Bytes** show what bytes will be overriden based on your **Hijacked Bytes Length**. This will help you make sure you're not ending the branch in the middle of an opcode. The **Opcodes** pane on the right side will show the disassembled view of the new bytes you are inserting in the **New Bytes** field.

When you are done writing the code, hit the **Apply** button or press `Ctrl + X` to apply the code. Once it is applied, you can open the code by double clicking it on the left pane or selecting it and pressing `Ctrl + O`. **Reset Changes** will clear all of the fields. To delete a code, right click on it in the left pane and select **Delete Code** or select it and press `Ctrl + D`.

## Expand SEQ

This will attempt to open the selected SEQ file with [SEQ Kage](https://github.com/mitchellhumphrey/seq-kage/releases). SEQ Kage allows you to expand the SEQ file and add more space near the end of the file to add new SEQ bytecode.
