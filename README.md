# GNTool

![GNTool Logo](/example/logo.png?raw=true "GNTool Logo")

This program allows you to modify files contained inside .FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made.

## Table of Contents

1. **[Getting Started](#getting-started)**
2. **[Prerequisites](#prerequisites)**
3. **[How to Use](#how-to-use)**
4. **[Example Workflow](#example-workflow)**
5. **[How it Works](#how-it-works)**
6. **[Logging](#logging)**
7. **[Future Features](#future-features)**
8. **[Contributing](#contributing)**
9. **[Authors](#authors)**
10. **[Special Thanks](#special-thanks)**
11. **[License](#license)**

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

## How it Works

There are multiple steps involved in the execution of this program. Extracting and creating a new ISO all occurs within the program GameCube Rebuilder. After the contents of the ISO is dumped to the root folder, it is copied to the uncompressed folder. Then in the uncompressed folder each FPK is uncompressed using a PRS uncompression algorithm. When you are ready to rebuild the ISO, only modified files are repacked. Original FPK archive files are preserved if untouched. We are able to know which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check).

A recommended modification for the dol file is patch an issue with audio file offsets in the game.toc. Since GameCube Rebuilder modifies the game.toc with new file sizes, it is possible that audio file offsets change in that file. There is a line of code in GNT4 that checks that the offets end with either 0x0000 or 0x8000. I'm not entirely sure of the purpose of this line of code, but it seems to be safe to remove.

### Logging

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
