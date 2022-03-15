# Animation Options

![Animation](/docs/animation.png?raw=true "Animation")

Animations for the GNT games are stored in `.mot` files. Each of these `.mot` files contains one or more `.gnta` files packed inside. Each `.gnta` file is a single animation and is named after its animation ID. For more information on animations, see [MOT Files](https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/file_formats/mot.md).

## Unpack MOT File

Extracts all `.gnta` files from a `.mot` file.

## Repack MOT File

Imports `.gnta` files into a `.mot` file.

## Modify GNTA File

Opens an editor to modify `.gnta` files.

![GNTA Editor](/docs/gntaeditor.png?raw=true "GNTA Editor")

The left pane contains information on the full animation, such as its **Animation ID** and the **End Time (in Frames)**. It also has a line chart representing the key frame progression. This shows how many frames last between each key.

Each `.gnta` file animation contains one or more **Bone Animations**, which are in the middle pane. These are movements of a specific bone (joint) on the model. It is currently unknown what **Flags 1** is for.

Each Bone Animation has one or more **Key Frames**, which are on the right pane. These key frames are the coordinates to move that bone to at a specific frame.

Most of the above data can be modified, such as the X, Y, Z, and W coordinates of a specific key at a specific frame.

### HSD Raw

You can see how animations actually look on a model by using [HSDRaw Viewer](https://github.com/Ploaj/HSDLib). For example, view Lee's model by opening `files/chr/loc/0000.dat`. Under **Nodes** navigate to `scene_data/JOBJDescs/Array_0/RootJoint` and double-click on `RootJoint` to open the model.

![HSD Dat](/docs/hsddat.png?raw=true "HSD Dat")

To import and view an animation, open the respective `.gnta` file by navigating to `Animation->Import` in the RootJoint file menu:

![Animation Import](/docs/animationimport.png?raw=true "Animation Import")

It will then request the respective `.jcv` (Joint Connector Value) file that is paired with this model, which should be named the same as the `.dat` file in the same directory. You then can use the play buttons at the bottom of the application to play the animation.

![Animation Play](/docs/animationplay.png?raw=true "Animation Play")
