# Tools

- [ISO Patcher for GameCube](#iso-patcher-for-gamecube)
- [ISO Extractor for GameCube](#iso-extractor-for-gamecube)
- [ISO Compare for GameCube](#iso-compare-for-gamecube)
- [FPK Unpacker for GameCube](#fpk-unpacker-for-gamecube)
- [FPK Unpacker for Wii](#fpk-unpacker-for-wii)
- [FPK Unpacker for PS2/PSP](#fpk-unpacker-for-ps2psp)
- [FPK Repacker for GameCube](#fpk-repacker-for-gamecube)
- [FPK Repacker for Wii](#fpk-repacker-for-wii)
- [FPK Repacker for PS2/PSP](#fpk-repacker-for-ps2psp)
- [TXG2TPL](#txg2tpl)
- [SEQ Disassembler](#seq-disassembler)
- [SEQ Editor](#seq-editor)
- [MOT Unpacker](#mot-unpacker)
- [MOT Repacker](#mot-repacker)
- [GNTA Editor](#gnta-editor)
- [Diff Workspace](#diff-workspace)

![Tools](/docs/tools.png?raw=true "Tools")

## ISO Patcher for GameCube

This allows you to patch a GameCube ISO using a zip file. The contents of the zip file must match the directory of a GameCube ISO, that is, at the root must have two directories: `files` and `sys`. All files must be in the expected directory relative to `files` and `sys`.

## ISO Extractor for GameCube

This allows you to extract the files from any GameCube ISO.

## ISO Compare for GameCube

This allows you to compare two ISOs to find differences between them. It will allow you to save a report of files only in each ISO and files that have been changed between the two ISO files.

## FPK Unpacker for GameCube

This allows you to unpack an FPK file for most Eighting titles on the GameCube.

## FPK Unpacker for Wii

This allows you to unpack an FPK file for most Eighting titles on the Wii.

## FPK Unpacker for PS2/PSP

This allows you to unpack an FPK file for most Eighting titles on the PS2 and PSP.

## FPK Repacker for GameCube

This allows you to repack an FPK file for most Eighting titles on the GameCube.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

## FPK Repacker for Wii

This allows you to repack an FPK file for most Eighting titles on the Wii.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

## FPK Repacker for PS2/PSP

This allows you to repack an FPK file for most Eighting titles on the PS2 and PSP.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

## TXG2TPL

This tool provides a GUI for running Struggleton's [TXG2TPL](https://github.com/Struggleton/TXG2TPL) application. TXG2TPL allows you to extract the TPL graphic files from a TXG file. For more information on working with graphics, see [Graphics Options](/docs/graphics.md).

## SEQ Disassembler

Disassembles the bytecode of an SEQ file and creates an HTML or TXT report output. This report documents info such as the opcodes and sections of the SEQ file. For more information on working with SEQ files, see [Sequence Options](/docs/sequence.md).

## SEQ Editor

Opens an interactive editor for SEQ files, allowing you to add new bytecode into the file. This is accomplished by branching from an original location in the SEQ file to the end of the file, running your new code, and branching back (much like a Gekko code). For more information on working with SEQ files, see [Sequence Options](/docs/sequence.md).

## MOT Unpacker

Unpacks a MOT file into its respective GNTA file(s). For more information on working with animations, see [Animation Options](/docs/animation.md).

## MOT Repacker

Repacks a MOT file from its respective GNTA file(s). For more information on working with animations, see [Animation Options](/docs/animation.md).

## GNTA Editor

Opens an interactive editor for GNTA files. These files represent a single animation in the game, and the editor lets you modify the properties of the animation to change it. For more information on working with animations, see [Animation Options](/docs/animation.md).

## Diff Workspace

A tool to find the differences between two GNTool workspaces. Also available from the workspace
file menu.

It will create an HTML report of file differences like so:

![Diff Report](/docs/diff.PNG?raw=true "Diff Report")
