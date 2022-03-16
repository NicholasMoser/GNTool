# GNTool Workspace

![Workspace](/docs/workspace.png?raw=true "Workspace")

![Workspace Directory](/docs/workspacedir.png?raw=true "Workspace Directory")

A workspace will contain a directory named `compressed` and a directory named `uncompressed`. **You should only modify files in the `uncompressed` directory**. GNTool is responsible for managing the `compressed` directory, `workspace.bin` file, and the optional `codes.json` file. Refreshing the workspace will detect any changes you've made to files in the `uncompressed` directory. Changes are detected by calculating the CRC32 hash of each file and comparing it against the last known CRC32 hash.
