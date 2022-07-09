# General Options

- [Audio Fix](#audio-fix)
- [Skip Cutscenes](#skip-cutscenes)
- [Unlock Everything](#unlock-everything)
- [Widescreen 16:9](#widescreen-169)
- [Play Audio While Paused](#play-audio-while-paused)
- [No Slow Down on Kill](#no-slow-down-on-kill)
- [X Does Not Break Throws](#x-does-not-break-throws)

![General](/docs/workspace.png?raw=true "General")

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

## Play Audio While Paused

Continues playing background music when the game is paused.

## No Slow Down on Kill

Avoids the slowdown when a player is killed in the game.

## X Does Not Break Throws

A, B, Y, and X all can break throws in GNT4. Since X can be used to break throws with no punishment when you have no chakra, this code will entirely disable X as a throw break button.
