# Menu Options

- [Audio Fix](#audio-fix)
- [Skip Cutscenes](#skip-cutscenes)
- [Unlock Everything](#unlock-everything)
- [Widescreen 16:9](#widescreen-169)
- [Main Menu Character](#main-menu-character)
- [Title Timeout to Demo](#title-timeout-to-demo)
- [Translate Text in the Game to English](#translate-text-in-the-game-to-english)
- [Allow duplicate characters in 4-player battles](#allow-duplicate-characters-in-4-player-battles)
- [Character Selection Speed](#character-selection-speed)
- [Frames Until Model is Loaded in Character Select Screen](#frames-until-model-is-loaded-in-character-select-screen)
- [Play Audio While Paused](#play-audio-while-paused)
- [No Slow Down on Kill](#no-slow-down-on-kill)
- [X Does Not Break Throws](#x-does-not-break-throws)

![Menu](/docs/workspace.png?raw=true "Menu")

## Audio Fix

Patches `main.dol` to fix an audio offset issue. The audio issue is that if the offset for an audio file in the fst.bin does not end with 0x0000 or 0x8000 the game will crash. This is because the instruction at 0x8016fc08 is `rlwinm. r0, r0, 0, 17, 31 (00007fff)` and the instruction at 0x8016fc0c is `beq- 0x8016FC2C`. The end result of these two operations is that it will not branch if the offset is something like 0x0c3e7800 and will instead enter OSPanic (crash).

The reason that the offset can be changed in the fst.bin is because rebuilding the ISO can use new offsets for the audio files if the files placed before them in the ISO have a larger size than they previously did. To fix this, we can modify the game code.

Modifying the game code does not seem to have any side effects, so this is the currently accepted solution. More precisely, the instruction at 0x8016fc08 is a conditional branch. The fix is to change it to an unconditional branch so that it always branches. Therefore it will never encounter the OSPanic from this method under any circumstances.

May be related to [Report DTK Audio in Increments of 0x8000](https://dolphin-emu.org/blog/2019/02/01/dolphin-progress-report-dec-2018-and-jan-2019/#50-9232-report-dtk-audio-in-increments-of-0x8000-by-booto).

## Skip Cutscenes

Patches `main.dol` to skip the three intro cutscenes. This will boot the game straight to the title screen.

## Unlock Everything

Patches `main.dol` to unlock everything (e.g. characters and stages).

## Widescreen 16:9

Changes the aspect ratio from 4:3 to 16:9 (Widescreen).

## Main Menu Character

The main menu character for GNT4 can be changed. Normally it is Sakura, but you can change it to any character except Kiba, Kankuro, and Tayuya. Kiba, Kankuro, and Tayuya cause errors since they spawn additional characters.

Each character has a customized sound effect for when a menu option is selected. This can be further customized by changing the byte at offset `0x1BE67` in `files/maki/m_title.seq`. The description of each menu option will still be the original voice clips read by Sakura. The eye texture upon menu option selection is the 2nd texture in `3.tpl` of each character's `1300.txg` file. Some characters do not have a `3.tpl` or 2nd texture, so this will be created upon selecting the character in GNTool.

## Title Timeout to Demo

When at the title screen of GNT4, after ten seconds the game will transition to the demo screen. The demo screen will load a CPU fight and upon the player pressing start will go back to the title screen. You can edit the number of seconds before it transitions to the demo screen. By selecting the Max button you can set it to a day, effectively disabling it.

## Translate Text in the Game to English

Translates text in the game to English using the translations created by Kosheh.

## Allow duplicate characters in 4-player battles

This patch allows you to select multiples of the same character in 4-player battle mode.

## Character Selection Speed

The character selection speed can be edited by changing two values. The **Character Selection Initial Speed** is the speed that the character cursor moves when you begin to hold up or down in the character select screen. After holding it for a few moments, it changes to a faster speed, known as the **Character Selection Max Speed**. If you make both of these values the same number then the speed will never change and will also be the same speed while moving.

The value can be set between 1 and 15. 1 is extremely fast and 15 is extremely slow. The game by default has it set to 12 for the initial speed and 8 for the max speed.

## Frames Until Model is Loaded in Character Select Screen

This allows you to change the number of frames before a model is loaded in the character select
screen (CSS). In vanilla GNT4 the model for a character will be loaded 60 frames after hovering
over the character. The game runs in 60 frames per second, so the default is approximately 1 second.

Loading the model causes the game to freeze for a quick moment, especially so when the game is
loaded from your local file system instead of via an ISO/disc. This freeze can resort in a worse
quality of life while playing the game. By raising the number of frames, you can make the game wait
longer (or basically forever) before it tries to load the model.

The maximum is 2147483391 frames, which would require waiting ~14 months.

## Play Audio While Paused

Continues playing background music when the game is paused.

## No Slow Down on Kill

Avoids the slowdown when a player is killed in the game.

## X Does Not Break Throws

A, B, Y, and X all can break throws in GNT4. Since X can be used to break throws with no punishment when you have no chakra, this code will entirely disable X as a throw break button.
