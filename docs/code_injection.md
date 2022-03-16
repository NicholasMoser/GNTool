# Code Injection

The Code Injection tab allows you to inject Gecko Codes directly into the dol. This behaves similarly to [Melee Mod Manager](https://smashboards.com/threads/melee-code-manager-v4-3-easily-add-mods-to-your-game.416437/).

![Code Injection](/docs/codeinjection.png?raw=true "Code Injection")

Only the following Gecko code types are currently supported:

- [32 bits Write (04)](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md#32-bits-write)
- [Insert ASM (C2)](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md#insert-asm)

For Insert ASM (C2) codes, the Dolphin emulator inserts the additional assembly between the addresses of `0x80001800` and `0x80003000`. Dolphin then creates a branch from the original instruction to this new code region and branches back when complete.

GNTool cannot add Gecko codes in this region, therefore GNTool will instead overwrite existing code with the inserted assembly. The overwritten code is training mode recording functionality that is unused by the game. The recording code that is overwritten exists between addresses `0x80086F58` and `0x800873FC`, which is 1188 bytes (297 assembly instructions).

If you attempt to load an ISO or workspace with no codes.json file and codes are detected in the dol, GNTool will attempt to recreate a codes.json for you.

## Validate Codes

Will tell you whether the Gecko codes you wish to insert are valid.

## Add Codes

Will attempt to inject your Gecko codes into the dol. A Code Name is required so that the code can be labeled and listed on the right hand list box.

## Remove Code

Will attempt to undo the selected code in the right hand list box from the dol.