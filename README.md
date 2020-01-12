# GNTool

![GNTool Logo](/docs/logo.png?raw=true "GNTool Logo")

This program allows you to modify files contained inside .FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made.

## Table of Contents

1. **[Getting Started](#getting-started)**
2. **[Prerequisites](#prerequisites)**
3. **[How to Use](#how-to-use)**
4. **[Options](#options)**
5. **[How it Works](#how-it-works)**
6. **[Logging](#logging)**
7. **[Contributing](#contributing)**
8. **[Authors](#authors)**
9. **[Special Thanks](#special-thanks)**
10. **[License](#license)**

## Getting Started

To use this tool, first satisfy the prerequisites listed below and then run the latest executable jar from the release section of this repository.

### Prerequisites

1. Windows
2. [Java 11+](https://adoptopenjdk.net/)
3. [Naruto GNT4 ISO File](https://wiki.dolphin-emu.org/index.php?title=Ripping_Games)

### How to Use

To run the tool, extract the zip file and run `run.bat`. Before it runs the program it will verify that [JAVA_HOME has been set](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html).

If this is your first time using the application you will want to create a new workspace. This workspace is created from a provided GNT4 ISO. The workspace will contain a folder titled uncompressed; this is where you can modify the contents of files.

Files cannot be deleted from this directory or it will prevent you from rebuilding the ISO. Refreshing the workspace will detect any changes you made to files outside of the application. You can also right click on any changed files in the Changed Files view to open them in your system editor.

Some things to be aware of while using the application:

* Do not rename directories or files as you use this program. The program will look for specifically named directories and files within them, and therefore will not work correctly if these names have changed.
* Do not modify the contents of any files in the folder titled root. This folder is needed to rebuild the game.
* Compressing files can take a while depending on which .FPKs were modified. It can potentially take hours depending on how many files were modified.
* The compression algorithm does not perfectly match Eighting's, therefore newly compressed files may be larger than they otherwise would be.

### Options

#### Audio Fix

Patches the Start.dol file to fix an audio offset issue. The audio issue is that if the offset for an audio file in the game.toc does not end with 0x0000 or 0x8000 the game will crash. This is because the instruction at 0x8016fc08 is `rlwinm. r0, r0, 0, 17, 31 (00007fff)` and the instruction at 0x8016fc0c is `beq- 0x8016FC2C`. The end result of these two operations is that it will not branch if the offset is something like 0x0c3e7800 and will instead enter OSPanic (crash).

The reason that the offset can be changed in the game.toc is because GCRebuilder.exe will use new offsets for the audio files if the files placed before them in the ISO have a larger size than they previously did. Since GCRebuilder.exe cannot be modified, the only two solutions right now are to try and use padding or modify the game code.

Modifying the game code does not seem to have any side effects, so this is the currently accepted solution. More precisely, the instruction at 0x8016fc08 is a conditional branch. The fix is to change it to an unconditional branch so that it always branches. Therefore it will never encounter the OSPanic from this method under any circumstances.

May be related to [Report DTK Audio in Increments of 0x8000](https://dolphin-emu.org/blog/2019/02/01/dolphin-progress-report-dec-2018-and-jan-2019/#50-9232-report-dtk-audio-in-increments-of-0x8000-by-booto).

#### Skip Cutscenes

Patches the Start.dol file to skip the three intro cutscenes. This will boot the game straight to the title screen.

#### Character Selection Speed

The character selection speed can be edited by changing two values. The **Character Selection Initial Speed** is the speed that the character cursor moves when you begin to hold up or down in the character select screen. After holding it for a few moments, it changes to a faster speed, known as the **Character Selection Max Speed**. If you make both of these values the same number then the speed will never change and will also be the same speed while moving.

The value can be set between 1 and 15. 1 is extremely fast and 15 is extremely slow. The game by default has it set to 12 for the initial speed and 8 for the max speed.

#### Title Timeout to Demo

When at the title screen of GNT4, after ten seconds the game will transition to the demo screen. The demo screen will load a CPU fight and upon the player pressing start will go back to the title screen. You can edit the number of seconds before it transitions to the demo screen. By selecting the Max button you can set it to a day, effectively disabling it.

### How it Works

There are multiple steps involved in the execution of this program. Extracting and creating a new ISO all occurs within the program GameCube Rebuilder. After the contents of the ISO is dumped to the root folder, it is copied to the uncompressed folder. Then in the uncompressed folder each FPK is uncompressed using a PRS uncompression algorithm. When you are ready to rebuild the ISO, only modified files are repacked. Original FPK archive files are preserved if untouched. We are able to know which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check).

A recommended modification for the dol file is patch an issue with audio file offsets in the game.toc. Since GameCube Rebuilder modifies the game.toc with new file sizes, it is possible that audio file offsets change in that file. There is a line of code in GNT4 that checks that the offets end with either 0x0000 or 0x8000. I'm not entirely sure of the purpose of this line of code, but it seems to be safe to remove.

## Logging

This tool has logging implemented with it to allow easier debugging of issues. To access the logs, go to your home directory and look for a log file that starts with fpk (fpkjava0.log.0 for example). This tool will store 5 log files at a time with different numbers at the end for each instance. Make sure to include all of these for any bug reports.

## Contributing

If you have enhancement ideas, defect requests, or generally want to contribute to the project, please read [CONTRIBUTING.md](CONTRIBUTING.md).

## Authors

* **Nicholas Moser**

## Special Thanks

* **tpu** - Wrote original PRS uncompression algorithm.
* **RupertAvery** - Wrote original PRS compression algorithm.
* **Luigi Auriemma** - Ported PRS compression/uncompression algorithms to QuickBMS.

## License

This project is licensed under the GPL-3.0 - see the [LICENSE](LICENSE) file for details.
