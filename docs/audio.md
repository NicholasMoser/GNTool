# Audio Options

- [Extract All Sound Effects](#extract-all-sound-effects)
- [Import All Sound Effects](#import-all-sound-effects)
- [Create TRK File from Music File](#create-trk-file-from-music-file)
- [Create DSP File from Sound Effects](#create-dsp-file-from-sound-effects)
- [Randomize All Music](#randomize-all-music)
- [Extract Sound Effects](#extract-sound-effects)
- [Randomize Sound Effects](#randomize-sound-effects)
- [Import Sound Effects](#import-sound-effects)

![Audio](/docs/audio.png?raw=true "Audio")

Sound effects for the GNT games are stored inside `.sam` and `.sdi` files, which contain one or more sound effects. They require being unpacked to listen to the individual sound effect(s). For more info on these files, inluding how to listen to them, see [SAM, SDI, POO, and PRO Files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/sound.md).

Music for the GNT games are stored in `.trk` files. For more info on these files, including how to listen to them, see [TRK Files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/trk.md).

## Extract All Sound Effects

Extracts all `.sam` and `.sdi` files into their respective `.dsp` sound effects. The sound effects for each will be stored in a new directory named the same as the `.sam` file (e.g. `2000.sam` extracts to `/2000`).

## Import All Sound Effects

Import all `.dsp` sound effects into their respective `.sam` and `.sdi` files. The sound effects for each must be stored in a directory named the same as the `.sam` file (e.g. `/2000` is imported into `2000.sam`.

## Create TRK File from Music File

This allows you to pass a music file and convert it to a `.trk` file that can be used in GNT games.

It works by first calling `ffmpeg` to make the music file a `.wav` file with the appropriate characteristics. The command run is:

`ffmpeg.exe -y -i {input} -ar 48000 -ac 2 -acodec pcm_s16le -bitexact -loglevel quiet {output}`

- -y
  - Overwrite output files without asking. We do this since `ffmpeg` is run in the background.
- -i
  - input file url
- -ar
  - Set the audio sampling frequency. We use **48000 Hz**.
- -ac
  - Set the number of audio channels. We use **2 channels**.
- -acodec
  - Set the audio codec. We use **PCM signed 16-bit little-endian**, which is also the default for muxing into WAV files.
- -bitexact
  - Enable bitexact mode for (de)muxer and (de/en)coder. Only write platform-, build- and time-independent data. This ensures that file and data checksums are reproducible and match between platforms. Its primary use is for regression testing.
- -loglevel
  - Set logging level and flags used by the library. We use quiet since this is run in the background.

This is followed by calling the `dtkmake.exe` from the Nintendo SDK on the WAV file. You can find this under `/NINTENDO GameCube SDK 1.0/X86/bin/dtkmake.exe` of the SDK. The final output is a `.trk` file readable by the GNT games.

## Create DSP File from Sound Effects

Import `.dsp` sound effects into `.sam` and `.sdi` files.

## Randomize All Music

Randomize all of the `.trk` files with each other. This is accomplished by simply changing each of their filenames with a different filename. `m002.trk`, `m003.trk`, `m004.trk`, and `m006.trk` are very short, therefore those four are only swapped with each other.

## Extract Sound Effects

Extract the selected `.sam` and `.sdi` files into their respective `.dsp` sound effects. The sound effects for each will be stored in a new directory named the same as the `.sam` file (e.g. `2000.sam` extracts to `/2000`).

## Randomize Sound Effects

Randomize all of the `.dsp` files for the selected `.sam` and `.sdi` files that have already been extracted. This will simply swap the filenames of each sound effect with each other.

## Import Sound Effects

Import the extracted `.dsp` files into the selected `.sam` and `.sdi` files. The sound effects for each must be stored in a new directory named the same as the `.sam` file (e.g. `2000.sam` extracts to `/2000`).
