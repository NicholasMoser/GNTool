# FPK Tool

This Windows program allows you to modify files contained inside FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made.

![FPK Tool](/example/fpktool.png?raw=true "FPK Tool")

## Getting Started

To use this tool, first satisfy the prerequisites listed below and then run the latest executable jar from the release section of this repository. 

### Prerequisites

1. Windows
2. [Java 8+](https://java.com/en/download/)
3. [GameCube Rebuilder 1.1+](https://www.romhacking.net/utilities/619/)[1]
4. [Naruto GNT4 ISO File](https://wiki.dolphin-emu.org/index.php?title=Ripping_Games)

[1] Only required for ISO extracting and importing.

### How to Use

After satisfying the prerequisites listed above, make sure that gcr.exe (GameCube Rebuilder) and the executable jar for this tool are in the same directory. While you are using this tool, there are many things to be aware of:
* Do not modify the name of directories or files as you use this program. It is designed to look at specific directories and files within them, so only modify the contents of each file.
* The options in the tool are listed top to bottom in the expected order of use. The next section gives an example workflow.

#### Example Workflow

1. **Export Files from ISO**
Choose a clean copy of the ISO for the input file, and then a directory of your choice for the second. I suggest choosing a new directory named something like "packed", since it will represent the folder of packed files.

2. **Unpack FPKs in Directory**
Choose the root folder inside of the directory we previously chose, "packed" in this case. Then pick a new folder outside of the first folder to unpack to. I suggest choosing a new sibling directory named something like "unpacked" since it will represent the folder of unpacked files.

At this point, you will actually modify files themselves in folder named "unpacked". More information about the various files you will come across can be found here: [File Formats](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/formats.md).

3. **Repack FPKs in Directory**
First choose the root folder inside of the directory that you modified the files in, "unpacked" in this case. Then select the root folder inside of the directory we wish to repack into, "packed" in this case. Please be aware that the repacking process compresses files that you modified, and in some cases can take minutes to do a single FPK file. You will be alerted if the program encounters an error, so just let it continue to process.

4. **Import Files into ISO (Not yet working)**
Choose the root folder of the newly repacked directory, "packed" in this case. Name your ISO image file. Please be aware that this does not correctly modify the game.toc file yet, so you may want to perform this option within GameCube Rebuilder manually.

#### How it Works

ISO extracting and importing entirely uses the GameCube Rebuilder command line tool. Unpacking FPK files simply copies the input directory to the output directory and extracts each FPK file. The files inside of an FPK archive have paths as part of the filename, so those paths are preserved for the output file. Each file is also uncompressed (PRS). Repacking FPK files is a bit more complicated. First, you use the original files from ISO extraction so that we only repack files that were actually changed. We are able to know which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check).

### Logging

This tool has logging implemented with it to allow easier debugging of issues. To access the logs, go to your home directory and look for a log file that starts with fpk (fpkjava0.log.0 for example). This tool will store 5 log files at a time with different numbers at the end for each instance. Make sure to include all of these for any bug reports.

## Future Features

* Fix **Import Files into ISO** to modify the game.toc
* ISO Utils built-in (remove gcr.exe dependency)
* Loading bars for operations
* Support for games other than GNT4

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on the process for submitting pull requests. This is also what you'll want to read if you have idea suggestions or bug reports.

## Authors

* **Nicholas Moser**

## Special Thanks

* **tpu** - Wrote original PRS uncompression
* **RupertAvery** - Wrote original PRS compression
* **Luigi Auriemma** - Ported compression/uncompression to QuickBMS

## License

This project is licensed under the GPL-3.0 - see the [LICENSE.md](LICENSE.md) file for details
