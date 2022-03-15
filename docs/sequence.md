# Sequence Options

- [Disassemble SEQ](#disassemble-seq)
- [Modify SEQ](#modify-seq)
  - [Context](#context)
  - [Usage](#usage)
  - [References](#references)
- [Expand SEQ](#expand-seq)

![Sequence](/docs/sequence.png?raw=true "Sequence")

The main code of GNT games can be found in the file `main.dol`, which is PowerPC assembly. Contained in the main code of the game is an interpreter for an entirely different kind of assembly informally called **Sequence**. Sequence assembly can be found in `.seq` files. It is a proprietary format created by the company Eighting. It has no public documentation, therefore all provided documentation has been written via reverse engineering.

## Disassemble SEQ

This will [disassemble](https://en.wikipedia.org/wiki/Disassembler) the selected SEQ file bytecode into human-readable opcodes. The output of this can either be an HTML or plain text (TXT) report.

## Modify SEQ

This opens an SEQ Editor that allows you to modify SEQ files. Much like [Gecko Codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md), these modifications will branch from a certain offset in the SEQ file, run your new bytecode, and branch back.

### Context

The PowerPC assembly in `main.dol` can be edited through the usage of [Gecko Codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md), specifically Insert ASM codes. [GNTool Code Injection](/docs/code_injection.md) can also be used if Gecko codes are not desirable. There is no way however to easily modify an SEQ file. You must manually modify it with a hex editor.

Modifying an SEQ file with a hex editor is problematic for a number of reasons:

1. SEQ files contain references to offsets in the file. If you add new bytes in the middle of the SEQ file it will break any instruction that references offsets after your change.
2. There is not an easy way to track your changes. Since SEQ files are binary data, traditional SCM tools don't help much for showing the details of a specific change.
3. Because you cannot track your changes easily, reverting changes is difficult (especially in cases where two different changes have overlapping opcodes).

The SEQ Editor is a solution to the above three problems. It adds a new section to the end of the SEQ file called the **SEQ Extension** section. These edits are stored as binary data, but can be viewed with the SEQ Editor.

It solves the above issues because:

1. All new opcode bytes are appended to the end. The code insertion location branches to the new opcode bytes and branches back when complete. Therefore, all previous offsets are still valid and do not need to be updated.
2. Using the name field, changes can be documented. The before and after bytes will also be easily accessible to see what actually changed.
3. Changes can easily be reverted with the click of a button.


### Usage

![SEQ Editor](/docs/seqeditor.png?raw=true "SEQ Editor")

To create a new edit, go to `File->New Edit` or press `Ctrl + N`.

Here are the fields to fill out for a new code:

- **Name**: The name/description of your code. There is no size limit.
- **Offset**: Where in the SEQ file to branch from. Make sure that it is at the start of an opcode.
- **Hijacked Bytes Length**: This is how long the branch opcode should be (the branch to your new inserted code). It must be a multiple of 4 bytes and a minimum of 8 bytes. This is because a [Branch Instruction](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/opcode_group/01.md#0132---b) is 8 bytes long (e.g. `01320000 00002310`). The reason that more than 8 bytes is allowed here is because SEQ bytecode allows variable length opcodes, so 8 bytes may end in the middle of an opcode. By specifying a larger number, padding is inserted to prevent accidentally parsing an opcode in the middle of the opcode.
- **New Bytes**: The SEQ bytecode to insert (in hex).

The **Hijacked Bytes** show what bytes will be overriden based on your **Hijacked Bytes Length**. This will help you make sure you're not ending the branch in the middle of an opcode. The **Opcodes** pane on the right side will show the disassembled view of the new bytes you are inserting in the **New Bytes** field.

When you are done writing the code, hit the **Apply** button or press `Ctrl + X` to apply the code. Once it is applied, you can open the code by double clicking it on the left pane or selecting it and pressing `Ctrl + O`. **Reset Changes** will clear all of the fields. To delete a code, right click on it in the left pane and select **Delete Code** or select it and press `Ctrl + D`.

### References

[1] The binary format of a SEQ Extension section is as follows: begin with `"seq_ext\n"` (0x7365715F 6578740A). Then include one or more SEQ edits. Each edit begins with a name encoded in UTF-8 bytes and is terminated at a 4-byte alignment with at least one null byte. The name of the edit is followed by the 4-byte integer offset of where the edit occurs in the SEQ file. The offset is followed by the original opcode bytes of the SEQ file that were overridden with the branch to the new opcode bytes. These bytes are terminated by `"stop"`, (0x73746F70). After that is the new opcode bytes that will be branched to in the seq file. This will also be terminated by `"stop"`, (0x73746F70). The second stop terminator is the end of the SEQ edit. Once all edits are complete, the SEQ extension section is terminated with `"seq_end\n"` (0x7365715F 656E640A).

An example of the binary can be seen here:

![SEQ Extension](/docs/seqext.png?raw=true "SEQ Editor")

## Expand SEQ

This will attempt to open the selected SEQ file with [SEQ Kage](https://github.com/mitchellhumphrey/seq-kage/releases). SEQ Kage allows you to expand the SEQ file and add more space near the end of the file to add new SEQ bytecode.
