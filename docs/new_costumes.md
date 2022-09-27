# Adding New Costumes

This document will explain how to add new costumes for characters.

## Background

At the very least, each character in GNT4 has the following files:

- 0000.dat
- 0000.jcv
- 0100.dat
- 0100.jcv
- 1300.txg

The `.dat` file is the model, the `.jcv` file is the joints for the model, and the
`.txg` file is the eye texture for the model. The `.dat` and `.jcv` files associate with
each other and each filename is a specific costume:

- Costume 1: `0000`
- Costume 2: `0100`
- Costume 3: `0200`
- Costume 4: `0300`

Ino, Sakura, and Sasuke are the only characters with costumes 3 and 4. Sasuke's costumes
3 and 4 are not selectable in-game and are only used in story mode. Haku has a pseudo-costume
3 and 4 that just remove the mask model.

By default all models will use `1300.txg` for the eye textures. Sasuke and Kankuro have logic at
instruction 0x800ab61c in the dol to use `1301.txg` for all other models.

## How to Add New Costumes

Using GNTool, we can now do two things necessary for adding new costumes:

- Allow Costume 3 and/or 4 to be selectable for specific characters
- Use different eye textures for models that require them

### Model

First we need to create new models. If you are simply replacing textures you can copy `0000.dat`
and `0000.jcv` and create `0200.dat` and `0200.jcv` for costume 3, and `0300.dat` and `0300.jcv`
for costume 4.

Using [DAT Texture Wizard](https://github.com/DRGN-DRC/DAT-Texture-Wizard), you can replace the
textures on the model. If you will be using this model on actual hardware (GameCube/Wii), you
likely will want to make sure your new texture resolution matches the original texture
resolution.

![DAT Texture Wizard](/docs/dat_texture_wizard.png?raw=true "DAT Texture Wizard")

### Eye Texture

Now we must create new eye textures for costumes that require it. Create `1301.txg`, `1302.txg`,
and `1303.txg` as needed. Please note that with GNTool you can have specific models use a
specific eye texture, so you can have costumes 1 and 3 use `1300.txg` and costume 2 use
`1303.txg` for example.

Use [TXG2TPL](https://github.com/Struggleton/TXG2TPL) to extract `1300.txg`. Using
[BrawlCrate](https://github.com/soopercool101/BrawlCrate), modify each `.tpl` file to use the
respective eye textures from your new costume.

![BrawlCrate](/docs/brawlcrate.PNG?raw=true "BrawlCrate")

When you right click on a texture you can select
**Replace** to replace the texture. IF you will be using this model on actual hardware
(GameCube/Wii), you will likely want to change the dimensions in **BrawlCrate** to the original
texture dimensions and hit **Apply** to save changes.

If any `.tpl` files have no textures in them, you will likely want to add a dummy texture or
TXG2TPL may fail to run.

### GNTool Configuration

Now, we must use GNTool to modify the dol to allow additional costumes. Under the **User Interface**
tab of the workspace, select **Modify Costumes**.

![GNTool Logo](/docs/modify_costumes.png?raw=true "Modify Costumes")

In the above window, you can add characters to the left or right side using the **Add** buttons.
If a character is on the left side, they will have a costume 3 which will reference `0200.dat` and
`0200.jcv`. If a character is on the right side, they will have a costume 4 which will reference
`0300.dat` and `0300.jcv`.

You must hit the **Save** button on the left side to save costume 3 characters and **Save** button
on the right side to save costume 4 characters.

If we go back to the **User Interface** tab of the workspace, we can select **Modify Eyes** to
define which costumes will use which eye textures.

![GNTool Logo](/docs/modify_eyes.png?raw=true "Modify Eyes")

In the above image you can see that each character costume can select between `1300.txg`
(the default), `1301.txg`, `1302.txg`, and `1303.txg`. Costume 1 will always use `1300.txg`.
When you are done hit **Save**.

Your costumes should now be selectable with Y and/or Z in-game.

## Troubleshooting

If the colors of the texture seem to be off, check the texture image type:

![CI8](/docs/CI8.png?raw=true "CI8")

In this example, the textures are in the CI8 format, where ach value is an 8 bit palette index.
This is different than the CMPR format that most GNT4 model textures seem to use.

You can use applications like Photoshop to change the textures from RGB Color to Indexed Color.
