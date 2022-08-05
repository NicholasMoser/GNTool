# User Interface Options

- [Main Menu Character](#main-menu-character)
- [Title Timeout to Demo](#title-timeout-to-demo)
- [Translate Text in the Game to English](#translate-text-in-the-game-to-english)
- [Allow duplicate characters in 4-player battles](#allow-duplicate-characters-in-4-player-battles)
- [Character Selection Speed](#character-selection-speed)
- [Frames Until Model is Loaded in Character Select Screen](#frames-until-model-is-loaded-in-character-select-screen)
- [Reorder Characters](#reorder-characters)
- [Reorder Stages](#reorder-stages)

![User Interface](/docs/ui.png?raw=true "User Interface")

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

## Reorder Characters

Allows you to reorder characters in the character select screen. One large caveat, is that changing
the order of the characters will break the
[Random Select Gecko codes](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#add-random-select-to-character-select-screen-nick),
so before you are able to change the order it first asks to remove these random select codes if they are being used.

## Reorder Stages

Allows you to reorder the stages in the character select screen.
