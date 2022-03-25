# SEQ Dolphin Listener

Using the SEQ Dolphin Listener, it is possible to observe exactly what SEQ commands in which files GNT4 executes while it is playing.

## Prerequisites

- [Latest release of Dolphin with Lua support](https://github.com/NicholasMoser/dolphin/releases)
- [GNTool 4.1.0+](https://github.com/NicholasMoser/GNTool/releases)
- A decent computer since it is CPU intensive.

## Usage

Extract the Dolphin with Lua support release mentioned in the prerequisites above. Add the path of your GNT4 ISO backup to `Options->Configurations->Paths` so that Dolphin can find your GNT4 ISO. Right click on the GNT4 ISO in Dolphin and select **Properties**. Go to **Scripts** and add the `udp_gnt4.lua` script found in the extracted zip file. Click on the checkbox to enable the script.

![Scripts](/docs/scripts.png?raw=true "scripts")

Now when you launch GNT4 it will be configured to broadcast the SEQ position. Launch GNTool and open the **Dolphin SEQ Listener** from either the tools menu or a loaded workspace. The view in the middle will start to be flooded with rows as the game runs. You can clear all of the rows by pressing the **Clear** button.

![SEQ Listener](/docs/seq_listener.png?raw=true "SEQ Listener")

By default, only 500 rows will be stored in the application at a time. To change this, change **Buffer Size** on the bottom, press the **Stop** button and then press the **Start** button to restart it.

If you press the **Mark Line** button, it will mark the currently selected lines in bold and purple. This will allow you to easily visualize certain lines as you run through lines of code or frames of the game. You can jump to the next mark above your currently selected row by hitting the **Goto Mark** button.

![Mark](/docs/mark.png?raw=true "Mark")

## Disassemble Line

You can jump to the disassembled line of any row by either:

- Double clicking on a row.
- Right clicking on a row and selecting **Disassemble Line**
- Hitting **Disassemble Line** in the file menu.

This will attempt to disassemble the SEQ file of that row, open the HTML report, and jump to the line of the row you selected.

![Disassemble Line](/docs/disassemble_line.png?raw=true "Disassemble Line")
