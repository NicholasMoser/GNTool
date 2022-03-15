# Graphics Options

- [Extract All Textures](#extract-all-textures)
- [Import All Textures](#extract-all-textures)
- [Extract Texture](#extract-all-textures)
- [Import Texture](#extract-all-textures)

![Graphics](/docs/graphics.png?raw=true "Graphics")

Textures for the GNT games are stored in `.txg` files. Each of these files contains one or more `.tpl` files packed inside. `.tpl` files can be opened with [BrawlCrate](https://github.com/soopercool101/BrawlCrate). For more information on graphics, see [TXG Files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/txg.md). Each of the operations involving `.txg` files use [TXG2TPL](https://github.com/Struggleton/TXG2TPL).

## Extract All Textures

Extracts all `.txg` files into their respective `.tpl` textures. The `.tpl files` for each will be stored in a new directory named the same as the `.txg` file (e.g. `2000.txg` extracts to `/2000`).

## Import All Textures

Imports all `.tpl` files into their respective `.txg` file. The `.tpl files` for each must be stored in the new directory named the same as the `.txg` file (e.g. `/2000` is imported into `2000.txg`).

## Extract Texture

Extract all `.tpl` textures from a `.txg` file.
