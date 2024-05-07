# Sequence Options

- [Disassemble SEQ](#disassemble-seq)
- [Modify SEQ](#modify-seq)
  - [Context](#context)
  - [Usage](#usage)
  - [Schema](#schema)
  - [Export and Import](#export-and-import)
- [Dolphin SEQ Listener](#dolphin-seq-listener)

![Sequence](/docs/sequence.png?raw=true "Sequence")

The main code of GNT games can be found in the file `main.dol`, which is PowerPC assembly.
Contained in the main code of the game is an interpreter for an entirely different kind of assembly
informally called **Sequence**. Sequence assembly can be found in `.seq` files. It is a proprietary
format created by the company Eighting. It has no public documentation, therefore all provided
documentation has been written via reverse engineering.

## Disassemble SEQ

This will attempt to [disassemble](https://en.wikipedia.org/wiki/Disassembler) the selected SEQ
file bytecode into human-readable opcodes. The output of this can either be an HTML or plain text
(TXT) report.

## Modify SEQ

This opens an SEQ Editor that allows you to modify SEQ files. Much like
[Gecko Codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md),

### Context

The PowerPC assembly in `main.dol` can be edited through the usage of
[Gecko Codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md),
specifically Insert ASM codes.
[GNTool Code Injection](/docs/code_injection.md) can also be used if Gecko codes are
not desirable. There is no way however to easily modify an SEQ file. You must manually modify it
with a hex editor.

Modifying an SEQ file with a hex editor is problematic for a number of reasons:

1. SEQ files contain references to offsets in the file. If you add new bytes in the middle of the
   SEQ file it will break any instruction that references offsets after your change.
2. There is not an easy way to track your changes. Since SEQ files are binary data, traditional SCM
   tools don't help much for showing the details of a specific change.
3. Because you cannot track your changes easily, reverting changes is difficult (especially in cases
   where two different changes have overlapping opcodes).

The SEQ Editor is a solution to the above three problems. It adds a new section to the end of the
SEQ file called the **SEQ Extension** section. These edits are stored as binary data, but can be
viewed with the SEQ Editor.

It solves the above issues because:

1. All new opcode bytes are appended to the end. The code insertion location branches to the new
   opcode bytes and branches back when complete. Therefore, all previous offsets are still valid
   and do not need to be updated.
2. Using the name field, changes can be documented. The before and after bytes will also be easily
   accessible to see what actually changed.
3. Changes can easily be reverted with the click of a button.

### Usage

TODO

### Schema

#### Version 2

Version 2 SEQ extensions start with a 0x10 byte header:

| Offset | Type   | Description           |
|--------|--------|-----------------------|
| 0x0    | byte[] | The String "seq_ext2" |
| 0x4    | u32    | The number of symbols |
| 0xc    | u32    | Version (currently 0) |
| 0x10   | u32    | Flags (currently 0)   |

Each symbol will follow the header. There are different symbol types of different lengths,
each 16-byte aligned.

##### Binary

| Type   | Description             |
|--------|-------------------------|
| String | Name (16 byte aligned)  |
| u32    | Type of symbol          |
| u32    | Length of this symbol   |
| u32    | Bytes length            |
| u32    | Null (unused)           |
| byte[] | Bytes (16 byte aligned) |

##### ExistingBinary

| Type   | Description            |
|--------|------------------------|
| String | Name (16 byte aligned) |
| u32    | Type of symbol         |
| u32    | Length of this symbol  |
| u32    | Binary offset          |
| u32    | Binary length          |

##### Function

| Type   | Description             |
|--------|-------------------------|
| String | Name (16 byte aligned)  |
| u32    | Type of symbol          |
| u32    | Length of this symbol   |
| u32    | Bytes length            |
| u32    | Number of inner labels  |
| byte[] | Bytes (16 byte aligned) |

Then for each inner label

| Type   | Description                  |
|--------|------------------------------|
| u32    | Label offset                 |
| String | Label name (16 byte aligned) |

##### ExistingFunction

| Type   | Description            |
|--------|------------------------|
| String | Name (16 byte aligned) |
| u32    | Type of symbol         |
| u32    | Length of this symbol  |
| u32    | Function offset        |
| u32    | Function length        |

##### InsertASM

##### Footer

The Version 1 schema had a footer, but in Version 2 there is no longer a footer.
Instead, 256 zero bytes are added to the end. This is to allow new symbols to be inserted
while the game is running, since space in memory must already be allocated for the symbols.

#### Version 1

Version 1 SEQ extensions start with "seq_ext\n" and end with "seq_end\n". Between these two Strings
is an arbitrary number of edits, each with the schema:

| Type   | Description                                                                  |
|--------|------------------------------------------------------------------------------|
| String | Name                                                                         |
| u32    | Offset                                                                       |
| byte[] | Old bytes (the original bytes overridden in the SEQ file)                    |
| String | The delimiter "stop" to know when to stop parsing old bytes                  |
| byte[] | New bytes (the new bytes to execute in the SEQ file)                         |
| byte[] | SEQ code to branch back after the location where this code was branched from |
| String | The delimiter "stop" to know when to stop parsing new bytes                  |

### Export and Import

You can also export and import SEQ edits as individual files. This allows you to share an edit
easier and even track changes of it using version control. Tracking changes is made easier by the
fact that the output format is plain text.

SEQ Extension version 1 edits look like so:

```seqedit
kis/0000.seq

SeqEdit
Name:
  Kisame Phantom Sword Fix
Offset:
  0x1C308
Position:
  0x26074
Old Bytes:
  0x241A0900410000002406040005550000241A090002000000
New Bytes:
  0x241A0900430000002406040005550000241A1200101042A0
New Bytes with branch back:
  0x241A0900430000002406040005550000241A1200101042A0013200000001C320
```

An example of the binary can be seen here:

![SEQ Extension](/docs/seqext.png?raw=true "SEQ Editor")

SEQ Extension Version 2 edits look like so:

TODO

## Dolphin SEQ Listener

This tool allows you to observe exactly what SEQ commands in which files GNT4 executes while it is playing. For more information on how it works, see the accompanying [Dolphin SEQ Listener Documentation](/docs/seq_listener.md).
