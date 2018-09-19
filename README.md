# GNTool

This program allows you to modify files contained inside .FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made.

![FPK Tool](/example/fpktool.png?raw=true "FPK Tool")

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
Currently that is [1.0-SNAPSHOT](https://github.com/NicholasMoser/FPKTool/releases/download/1.0-SNAPSHOT/fpk-tool-1.0-SNAPSHOT.jar)

### Prerequisites

1. Windows
2. [Java 8+](https://java.com/en/download/)
3. [GameCube Rebuilder 1.1+](https://www.romhacking.net/utilities/619/)
4. [Naruto GNT4 ISO File](https://wiki.dolphin-emu.org/index.php?title=Ripping_Games)

### How to Use

After satisfying the prerequisites listed above, verify that gcr.exe (GameCube Rebuilder) and the executable jar for this tool are in the same directory. While you are using this tool, there are many things to be aware of:
* Do not rename directories or files as you use this program. The program will look for specifically named directories and files within them, and therefore will not work correctly if these names have changed. Only modify the contents of them.
* The options in the tool are listed top to bottom in the expected order of use. The next section gives an example workflow.
* Compressing files can take a while depending on which .FPKs were modified. If every file in the game was modified you should expect it to take over 2 hours on a fast PC.
* The files inside of an FPK archive have paths as part of the filename, so those paths are preserved for the output file.
* The compression algorithm does not perfectly match Eighting's, therefore newly compressed files may be larger than they otherwise would be. This should not be an issue in most cases.

#### Example Workflow

1. **Export Files from ISO**  
Select a clean copy of the GNT ISO for the input file, and then a directory of your choice for the second. I suggest creating a new directory named "packed", since this folder will represent the folder of packed FPK files.

2. **Unpack FPKs in Directory**  
Select the root folder inside of the directory we previously chose, named "packed" in this example. Then select a new folder outside of the first folder to unpack to. I suggest creating a new sibling directory named "unpacked" since it will represent the folder of unpacked FPK files.

3. **Modify Files**  
At this point, you will actually modify files themselves in the unpacked folder, the folder named "unpacked" in this example. More information about the various files you will come across can be found here: [File Formats](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/formats.md).

4. **Repack FPKs in Directory**  
First choose the root folder inside of the directory that you modified the files in, named "unpacked" in this example. Then select the root folder inside of the directory we wish to repack into, named "packed" in this example. Please be aware that the repacking process compresses files that you modified, and in some cases can take minutes to do a single FPK file. You will be alerted if the program encounters an error, so let it run until it finishes or displays an error dialog.

5. **Import Files into ISO (Not yet working)**  
Choose the root folder of the newly repacked directory, named "packed" in this example. Name your ISO image file. You should now have your newly modified and compressed ISO.

## How it Works

There are multiple steps involved in the execution of this program. Extracting and creating a new ISO all occurs within GameCube Rebuilder. GameCube Rebuilder is called via the command line, which is why version 1.1 is required (the version that added command line support). Unpacking FPK files simply copies the "unpacked" directory to the "packed" directory and extracts each FPK file in the "packed" directory. Each file extracted from the FPK archive file is uncompressed using a PRS uncompression algorithm. Repacking FPK files is designed to be as efficient and fast as possible. This is why only modified files are repacked, original FPK archive files are preserved if untouched. We are able to know which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check). While repacking, the Start.dol file is also patched to fix an issue with audio file offsets in the game.toc. Since GameCube Rebuilder modifies the game.toc with new file sizes, it is possible that audio file offsets change in that file. There is a line of code in GNT4 that checks that the offets end with either 0x0000 or 0x8000. I'm not entirely sure of the purpose of this line of code, but it can safely be removed. Therefore this is the current fix for this issue until GameCube Rebuilder is replaced or can be patched (it is not open source as far as I'm aware).

### Logging

This tool has logging implemented with it to allow easier debugging of issues. To access the logs, go to your home directory and look for a log file that starts with fpk (fpkjava0.log.0 for example). This tool will store 5 log files at a time with different numbers at the end for each instance. Make sure to include all of these for any bug reports.

## Future Features

* ISO Utils built-in (remove gcr.exe dependency)
* Loading bars and in-program logs
* Support for games other than GNT4

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
