# GNTool

![GNTool Logo](/docs/logo.png?raw=true "GNTool Logo")

This program contains tools that allow you to modify the Naruto GNT/Clash of Ninja games. Primarily, it allows you to modify files contained inside .FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made. Also contained are other generic tools such as a generic ISO extractor and FPK repacker.

## Table of Contents

1. **[Getting Started](#getting-started)**
2. **[Prerequisites](#prerequisites)**
3. **[How to Use](#how-to-use)**
4. **[Features](#features)**
5. **[Audio](#audio)**
6. **[Graphics](#graphics)**
7. **[How it Works](#how-it-works)**
8. **[Logging](#logging)**
9. **[Contributing](#contributing)**
10. **[Authors](#authors)**
11. **[Special Thanks](#special-thanks)**
12. **[License](#license)**

## Getting Started

### Prerequisites

1. Windows
2. [Naruto GNT4 ISO File](https://wiki.dolphin-emu.org/index.php?title=Ripping_Games)

### How to Use

To run the tool, download the latest release zip file from the [GNTool releases](https://github.com/NicholasMoser/GNTool/releases). Then extract the zip file and under the `bin` folder run `GNTool.bat`.

![Launch](/docs/launch.png?raw=true "Launch")

If this is your first time using the application you will want to create a new workspace. Otherwise, you can load your existing workspace. Workspaces are created from a GNT4 ISO located on your file system.

![Workspace Example](/docs/workspace.png?raw=true "Workspace Example")

A workspace will contain a folder named `compressed` and a folder named `uncompressed`. Files should only be modified by you in the `uncompressed` folder. GNTool is responsible for managing the `compressed` folder and `workspace.bin` file. Refreshing the workspace will detect any changes you've made to files outside of the application

The **Menu** tab contains features for modifying the game in a general sense. You can find explanations of each feature under [Features](#features).

The **Changed Files** tab contains the list of files you or GNTool has modified in the `uncompressed` directory. These files will be moved or recompressed upon building an ISO from the File menu. You can also right click on any changed files in the Changed Files view to open them, open their directory, or revert the changes.

The **Missing Files** tab contains the list of files that have been removed from the `uncompressed` directory. These must be added back or you will not be able to build an ISO.

The **Audio**, **Graphics**, and **Seqence** tabs contain tools and options for managing each of their respective files.

You can build ISOs by selecting *Build ISO* under the File menu at the top. It is recommended to also select *Parallel Build* if using a multi-core CPU.

:warning: Please be aware of the following :warning:

* Do not rename or delete the `workspace.bin` file or any file in the `compressed` or `uncompressed` directory.
* Do not modify the contents of any files in the `compressed` directory.
* The compression algorithm used by GNTool does not match Eighting's, therefore newly compressed files may be larger than they otherwise would be.

### Features

#### Tools

#### ISO Extractor (GameCube)

This allows you to extract the files from any GameCube ISO.

In the main menu of GNTool, you can click on the wrench icon to open the list of generic tools. These are tools that can be used across a variety of games.

#### FPK Unpacker (GameCube)

This allows you to unpack an FPK file for any Eighting title on the GameCube.

#### FPK Unpacker (Wii)

This allows you to unpack an FPK file for any Eighting title on the Wii.

#### FPK Repacker (GameCube)

This allows you to repack an FPK file for any Eighting title on the GameCube.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

#### FPK Repacker (Wii)

This allows you to repack an FPK file for any Eighting title on the Wii.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

#### Audio Fix

Patches the Start.dol file to fix an audio offset issue. The audio issue is that if the offset for an audio file in the fst.bin does not end with 0x0000 or 0x8000 the game will crash. This is because the instruction at 0x8016fc08 is `rlwinm. r0, r0, 0, 17, 31 (00007fff)` and the instruction at 0x8016fc0c is `beq- 0x8016FC2C`. The end result of these two operations is that it will not branch if the offset is something like 0x0c3e7800 and will instead enter OSPanic (crash).

The reason that the offset can be changed in the fst.bin is because rebuilding the ISO can use new offsets for the audio files if the files placed before them in the ISO have a larger size than they previously did. To fix this, we can modify the game code.

Modifying the game code does not seem to have any side effects, so this is the currently accepted solution. More precisely, the instruction at 0x8016fc08 is a conditional branch. The fix is to change it to an unconditional branch so that it always branches. Therefore it will never encounter the OSPanic from this method under any circumstances.

May be related to [Report DTK Audio in Increments of 0x8000](https://dolphin-emu.org/blog/2019/02/01/dolphin-progress-report-dec-2018-and-jan-2019/#50-9232-report-dtk-audio-in-increments-of-0x8000-by-booto).

#### Skip Cutscenes

Patches the Start.dol file to skip the three intro cutscenes. This will boot the game straight to the title screen.

#### Character Selection Speed

The character selection speed can be edited by changing two values. The **Character Selection Initial Speed** is the speed that the character cursor moves when you begin to hold up or down in the character select screen. After holding it for a few moments, it changes to a faster speed, known as the **Character Selection Max Speed**. If you make both of these values the same number then the speed will never change and will also be the same speed while moving.

The value can be set between 1 and 15. 1 is extremely fast and 15 is extremely slow. The game by default has it set to 12 for the initial speed and 8 for the max speed.

#### Title Timeout to Demo

When at the title screen of GNT4, after ten seconds the game will transition to the demo screen. The demo screen will load a CPU fight and upon the player pressing start will go back to the title screen. You can edit the number of seconds before it transitions to the demo screen. By selecting the Max button you can set it to a day, effectively disabling it.

#### Main Menu Character

The main menu character for GNT4 can be changed. Normally it is Sakura, but you can change it to any character except Kiba, Kankuro, and Tayuya. Kiba, Kankuro, and Tayuya cause errors since they spawn additional characters.

Each character has a customized sound effect for when a menu option is selected. This can be further customized by changing the byte at offset `0x1BE67` in `files/maki/m_title.seq`. The description of each menu option will still be the original voice clips read by Sakura. The eye texture upon menu option selection is the 2nd texture in `3.tpl` of each character's `1300.txg` file. Some characters do not have a `3.tpl` or 2nd texture, so this will be created upon selecting the character in GNTool.

#### Play Audio While Paused

Continues playing background music when the game is paused.

#### No Slow Down on Kill

Avoids the slowdown when a player is killed in the game.

### Audio

There are multiple settings related to audio. They can be found under the Audio tab in your workspace. Some of the tools require executables from the Nintendo GameCube SDK. You can find a copy of the SDK by searching online for it.

#### Sound Effects

Sound effects are stored inside .sam and .sdi files, and therefore require unpacking to listen to or replace. By using the **Extract** button you can extract a single .sam file selected by the combobox to the right. It will then open the folder that the sound effects were extracted to. After it has been extracted you can hit the randomize button to change the order of the sounds of that .sam file. When you are finished with the files, you will need to **Import** to create new .sam and .sdi files for changes to take place in the game.

You can also hit **Extract All** to extract all .sam files for a given workspace. **Import All** will then import all of the sound effects back into their respective .sam and .sdi files.

**Replace** will allow you to replace a single sound effect. In order to use this functionality it will ask you to select DSPADPCM.exe from the Nintendo GameCube SDK. This file can be found in the SDK under `NINTENDO GameCube SDK 1.0/X86/bin/DSPADPCM.exe`. The audio you select will first be modified into a 32k frequency .wav file and then into a .dsp file.

#### Music

Music files are easier to work with than sound effects since they are stored separately from each other. **Randomize** will randomize the music of similar lengths. So short songs, like from the VS screen, will be randomized together. Then the longer songs, like the stage songs, will be randomized together. The songs from the story mode with dialog will not be included.

**Replace** will allow you to replace a single music file. In order to use this functionality it will ask you to select dtkmake.exe from the Nintendo GameCube SDK. This file can be found in the SDK under `NINTENDO GameCube SDK 1.0/X86/bin/dtkmake.exe`. The audio you select will first be modified into a 48k frequency .wav file and then into a .trk file.

### Graphics

#### Textures

To replace textures in the game, first extract a specific texture archive or extract all of them. A folder matching the name of the texture archive will be created in the directory of the texture. In the folder will be one or more [.tpl files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/txg.md#tpl-header). These files can be viewed and modified with [BrawlBox](https://github.com/libertyernie/brawltools). When you are finished modifying .tpl files, make sure to import the specific texture in GNTool for it to appear in the game.

### How it Works

There are multiple steps involved in the execution of this program. First, the contents of the ISO are dumped to the `compressed` folder in the workspace. The contents are then copied to the `uncompressed` folder. In the `uncompressed` folder, each FPK is uncompressed using a PRS uncompression algorithm. The FPKs in the `uncompressed` folder are then deleted. This will leave the `compressed` folder as the compressed version of the game files and the `uncompressed` folder as the uncompressed version of the game files.

When you are ready to rebuild the ISO, only modified files are repacked. This is the benefit of keeping the `compressed` folder, the original FPKs are maintained in the workspace state. GNTool calculates which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check).

## Logging

This tool has logging implemented with it to allow easier debugging of issues. To access the logs, go to your home directory (e.g. `C:\Users\yourname`) and look for a log file that starts with fpk (fpkjava0.log.0 for example). This tool will store 5 log files at a time with different numbers at the end for each instance. Make sure to include all of these for any bug reports.

## Contributing

If you have enhancement ideas, defect requests, or generally want to contribute to the project, please read [CONTRIBUTING.md](CONTRIBUTING.md).

## Authors

* **Nicholas Moser**

## Special Thanks

* **tpu** - Wrote original PRS uncompression algorithm.
* **RupertAvery** - Wrote original PRS compression algorithm.
* **[Luigi Auriemma](https://aluigi.altervista.org/quickbms.htm)** - Ported PRS compression/uncompression algorithms to QuickBMS.
* **[Nisto](https://github.com/Struggleton)** - For writing musyx-extract for sound file reading and writing.
* **[Struggleton](https://github.com/Struggleton)** - For writing TXG2TPL for txg file reading and writing.
* **[The Dueling Potato](https://github.com/mitchellhumphrey)** - For writing seq-kage for seq file reading and writing.

## License

This project is licensed under the GPL-3.0 - see the [LICENSE](LICENSE) file for details.
