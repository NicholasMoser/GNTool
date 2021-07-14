# GNTool

![GNTool Logo](/docs/logo.png?raw=true "GNTool Logo")

This program contains tools that allow you to modify the Naruto GNT/Clash of Ninja games. Primarily, it allows you to modify files contained inside FPK files for Naruto GNT4. [FPK files](https://github.com/NicholasMoser/Naruto-GNT-Hacking/blob/master/gnt4/docs/file_formats/fpk.md) are archives that contain various game related files. Each entry is compressed with an Eighting specific PRS compression algorithm. This tool can both unpack and repack FPK files with any changes that you've made. Also contained are other generic tools such as a generic ISO extractor and FPK repacker.

## Table of Contents

- [Prerequisites](#Prerequisites)
  - [Windows](#Windows)
- [How to Use](#How-to-Use)
- [Features](#Features)
  - [Tools](#Tools)
  - [GNT4 Options](#GNT4-Options)
- [Code Injection](#Code-Injection)
- [How it Works](#How-it-Works)
- [Logging](#Logging)
- [Contributing](#Contributing)
- [Authors](#Authors)
- [Special Thanks](#Special-Thanks)
- [License](#License)

## Prerequisites

The [ISO File](https://wiki.dolphin-emu.org/index.php?title=Ripping_Games) you wish to work with.

### Windows

Windows is only required for a few specific features. These include:

- Replacing texture graphics, due to usage of `txg2tpl.exe`
- Replacing sound effects and music, due to usage of `ffmpeg.exe`

With that being said, only Windows binaries are currently being released. If there is a request for additional operating systems to support please log an issue for it.

## How to Use

To run the tool, download the latest release zip file from the [GNTool releases](https://github.com/NicholasMoser/GNTool/releases). Then extract the zip file and run `GNTool.bat`

![Launch](/docs/launch.png?raw=true "Launch")

Most of the functionality of GNTool is centered around the usage of workspaces. Only games in the top combobox are supported for workspaces. There are a handful of tools found under the wrench button that support all GameCube and Wii games. For more information on these tools see [Tools](#Tools).

If this is your first time using the application you will want to create a new workspace. Otherwise, you can load your existing workspace. Workspaces are created via extracting files from an ISO located on your file system.

![Workspace Example](/docs/workspace.png?raw=true "Workspace Example")

A workspace will contain a directory named `compressed` and a directory named `uncompressed`. Files should only be manually modified by you in the `uncompressed` directory. GNTool is responsible for managing the `compressed` directory and `workspace.bin` file. Refreshing the workspace will detect any changes you've made to files in the `uncompressed` directory. You can make these changes through options in GNTool or through manual edits outside of GNTool.

The **Menu** tab contains features for making general modifications to the game. You can find explanations of each feature under [Features](#Features).

The **Changed Files** tab contains the list of files you or GNTool have modified in the `uncompressed` directory. These files will be recompressed and moved upon building an ISO from the **File** menu. You can also right click on any changed files in the **Changed Files** view to open them, open their directory, or revert the changes.

![Right Click Example](/docs/rightclick.png?raw=true "Right Click Example")

The **Missing Files** tab contains the list of files that have been removed from the `uncompressed` directory. These must be added back or you will not be able to build an ISO.

The **Characters**, **Audio**, **Graphics**, and **Seqence** tabs contain tools and options for managing each of their respective files.

You can build ISOs by selecting *Build ISO* under the File menu at the top. It is recommended to also select *Parallel Build* if using a multi-core CPU. By default, the build will push non-system files to the back of the ISO such that the ISO is exactly 1,459,978,240 bytes (~1.35 GB). If you wish for non-system files to immediately be placed after system files, disable the **Push Files to Back of ISO** setting under the File menu.

:warning: Please be aware of the following :warning:

- Do not rename or delete the `workspace.bin` file or any file in the `compressed` or `uncompressed` directory.
- Do not modify the contents of `workspace.bin` or any files in the `compressed` directory.
- The compression algorithm used by GNTool does not perfectly match Eighting's, therefore newly compressed files may be larger than they otherwise would be.

## Features

### Tools

Even if you have a GameCube or Wii ISO that does not yet have workspace support, there are a number of tools that still may be used. These can be accessed by clicking the wrench button in the main GNTool menu.

#### ISO Patcher for GameCube

This allows you to patch a GameCube ISO using a zip file. The contents of the zip file must match the directory of a GameCube ISO, that is, at the root must have two directories: `files` and `sys`. All files must be in the expected directory relative to `files` and `sys`.

#### ISO Extractor for GameCube

This allows you to extract the files from any GameCube ISO.

#### ISO Compare for GameCube

This allows you to compare two ISOs to find differences between them. It will allow you to save a report of files only in each ISO and files that have been changed between the two ISO files.

#### FPK Unpacker for GameCube

This allows you to unpack an FPK file for most Eighting titles on the GameCube.

#### FPK Unpacker for Wii

This allows you to unpack an FPK file for most Eighting titles on the Wii.

#### FPK Unpacker for PS2/PSP

This allows you to unpack an FPK file for most Eighting titles on the PS2 and PSP.

#### FPK Repacker for GameCube

This allows you to repack an FPK file for most Eighting titles on the GameCube.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

#### FPK Repacker for Wii

This allows you to repack an FPK file for most Eighting titles on the Wii.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

#### FPK Repacker for PS2/PSP

This allows you to repack an FPK file for most Eighting titles on the PS2 and PSP.

There is an optional button at the bottom of the window titled **Load Template**. This button will
allow you to load a .txt file with file paths separated by newlines. This allows quicker repacking
for FPKs you intend to repack more than once.

#### TXG2TPL

This tool allows you to run [TXG2TPL](https://github.com/Struggleton/TXG2TPL) with a GUI to guide you.

### GNT4 Options

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

#### Widescreen 16:9

Changes the aspect ratio from 4:3 to 16:9 (Widescreen).

#### X Does Not Break Throws

A, B, Y, and X all can break throws in GNT4. Since X can be used to break throws with no punishment when you have no chakra, this code will entirely disable X as a throw break button.

#### Allow duplicate characters in 4-player battles

This patch allows you to select multiples of the same character in 4-player battle mode.

#### ZTK Damage Taken Multiplier

By default, Naruto's ZTK transformation takes 1.5x damage. This setting allows you to change this value.

#### Ukon Damage Taken Multiplier

By default, Ukon takes 1.2x damage. This setting allows you to change this value. Sakon is not affected, only Ukon.

#### Fix Kabuto 2A Scaling for Player 2

When playing as Kabuto on the player 1 side, Kabuto does unscaled damage with 2A. On the player 2 side however,
this damage is scaled. Player 1 unscaled damage is 48, while player 2 scaled damage is 26. Using this fix in
GNTool will use unscaled damage for both player 1 and player 2.

#### Fix Phantom Sword for Kisame Player 2

When playing as Kisame on the player 1 side, you can make JA **unblockable** by landing with it on a specific frame;
this is called Phantom Sword. Kisame on the player 2 side is unable to perform Phantom Sword. Using this fix in
GNTool will allow player 2 to also perform Phantom Sword.

#### Fix Phantom Sword for Zabuza Player 2

When playing as Zabuza on the player 1 side, you can make JA **unblockable** by landing with it on a specific frame;
this is called Phantom Sword. Zabuza on the player 2 side is unable to perform Phantom Sword. Using this fix in
GNTool will allow player 2 to also perform Phantom Sword.

#### Sound Effects

Sound effects are stored inside .sam and .sdi files, and therefore require unpacking to listen to or replace. By using the **Extract** button you can extract a single .sam file selected by the combobox to the right. It will then open the folder that the sound effects were extracted to. After it has been extracted you can hit the randomize button to change the order of the sounds of that .sam file. When you are finished with the files, you will need to **Import** to create new .sam and .sdi files for changes to take place in the game.

You can also hit **Extract All** to extract all .sam files for a given workspace. **Import All** will then import all of the sound effects back into their respective .sam and .sdi files.

**Replace** will allow you to replace a single sound effect. In order to use this functionality it will ask you to select DSPADPCM.exe from the Nintendo GameCube SDK. This file can be found in the SDK under `NINTENDO GameCube SDK 1.0/X86/bin/DSPADPCM.exe`. The audio you select will first be modified into a 32k frequency .wav file and then into a .dsp file.

#### Music

Music files are easier to work with than sound effects since they are stored separately from each other. **Randomize** will randomize the music of similar lengths. So short songs, like from the VS screen, will be randomized together. Then the longer songs, like the stage songs, will be randomized together. The songs from the story mode with dialog will not be included.

**Replace** will allow you to replace a single music file. In order to use this functionality it will ask you to select dtkmake.exe from the Nintendo GameCube SDK. This file can be found in the SDK under `NINTENDO GameCube SDK 1.0/X86/bin/dtkmake.exe`. The audio you select will first be modified into a 48k frequency .wav file and then into a .trk file.

#### Textures

To replace textures in the game, first extract a specific texture archive or extract all of them. A folder matching the name of the texture archive will be created in the directory of the texture. In the folder will be one or more [.tpl files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/txg.md#tpl-header). These files can be viewed and modified with [BrawlBox](https://github.com/libertyernie/brawltools). When you are finished modifying .tpl files, make sure to import the specific texture in GNTool for it to appear in the game.

### Code Injection

The Code Injection tab allows you to inject Gecko Codes directly into the dol. This behaves similarly to [Melee Mod Manager](https://smashboards.com/threads/melee-code-manager-v4-3-easily-add-mods-to-your-game.416437/).

![Code Injection](/docs/codeinjection.png?raw=true "Code Injection")

Only the following Gecko code types are currently supported:

- [32 bits Write (04)](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md#32-bits-write)
- [Insert ASM (C2)](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md#insert-asm)

For Insert ASM (C2) codes, the Dolphin emulator inserts the additional assembly between the addresses of `0x80001800` and `0x80003000`. Dolphin then creates a branch from the original instruction to this new code region and branches back when complete.

GNTool cannot add Gecko codes in this region, therefore GNTool will instead overwrite existing code with the inserted assembly. The overwritten code is training mode recording functionality that is unused by the game. The recording code that is overwritten exists between addresses `0x80086F58` and `0x800873FC`, constituting 1188 bytes (297 assembly instructions).

The **Validate Codes** button will tell you whether the Gecko codes you wish to insert are valid.

The **Add Codes** button will attempt to inject your Gecko codes into the dol. A Code Name is required so that the code can be labeled and listed on the right hand list box.

The **Remove Code** button will attempt to undo the selected code in the right hand list box from the dol.

If you attempt to load an ISO or workspace with no codes.json file and codes are detected in the dol, GNTool will attempt to recreate a codes.json for you.

#### Warning

- Please avoid modifying or deleting the `codes.json` file in the workspace directory.
- Please avoid modifying code in the dol between addresses `0x80086F58` and `0x800873FC`, which are at dol offsets `0x83F58` and `0x843FC` respectively.

## How it Works

There are multiple steps involved in the execution of this program. First, the contents of the ISO are dumped to the `compressed` folder in the workspace. The contents are then copied to the `uncompressed` folder. In the `uncompressed` folder, each FPK is uncompressed using the Eighting PRS uncompression algorithm. The FPK files in the `uncompressed` folder are then deleted. This will leave the `compressed` folder as the compressed version of the game files and the `uncompressed` folder as the uncompressed version of the game files.

Both of these directories are fully functional copies of the game when run through Dolphin. You can launch either by opening `sys/main.dol` in Dolphin.

When you are ready to rebuild the ISO, only modified files are repacked. This is the benefit of keeping the `compressed` folder, the original FPKs are maintained in the workspace state. GNTool calculates which files have changed by comparing each file to its expected [CRC32 value](https://en.wikipedia.org/wiki/Cyclic_redundancy_check).

## Logging

This tool has logging implemented with it to allow easier debugging of issues. To access the logs, go to your home directory (e.g. `C:\Users\yourname`) and look for a log file that starts with fpk (fpkjava0.log.0 for example). This tool will store 5 log files at a time with different numbers at the end for each instance. Make sure to include all of these for any bug reports.

## Contributing

If you have enhancement ideas, defect requests, or generally want to contribute to the project, please read [CONTRIBUTING.md](CONTRIBUTING.md).

## Authors

- **Nicholas Moser**

## Special Thanks

- **tpu** - Wrote original PRS uncompression algorithm.
- **RupertAvery** - Wrote original PRS compression algorithm.
- **[Luigi Auriemma](https://aluigi.altervista.org/quickbms.htm)** - Ported PRS compression/uncompression algorithms to QuickBMS.
- **[Nisto](https://github.com/Struggleton)** - For writing musyx-extract for sound file reading and writing.
- **[Struggleton](https://github.com/Struggleton)** - For writing TXG2TPL for txg file reading and writing.
- **[The Dueling Potato](https://github.com/mitchellhumphrey)** - For writing seq-kage for seq file reading and writing.

## License

This project is licensed under the GPL-3.0 - see the [LICENSE](LICENSE) file for details.
