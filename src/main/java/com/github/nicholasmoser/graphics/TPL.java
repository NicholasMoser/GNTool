package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TPL {

  private int fileId;
  private int numOfImages;
  private int imageTableOffset;
  private List<Texture> textures;

  public TPL(byte[] bytes) {
    textures = new ArrayList<>();
    this.fileId = ByteUtils.toUint32(bytes, 0);
    this.numOfImages = ByteUtils.toUint32(bytes, 4);
    this.imageTableOffset = ByteUtils.toUint32(bytes, 8);
    for (int i = 0; i < numOfImages; i++) {
      Texture texture = createTexture(bytes, i);
      textures.add(texture);
    }
    for (int i = 0; i < textures.size(); i++) {
      Texture texture = textures.get(i);
      boolean isLast = i == textures.size() - 1;
      int start = texture.getImageDataAddress();
      int end;
      if (isLast) {
        end = bytes.length;
      } else {
        end = textures.get(i + 1).getImageDataAddress();
      }
      texture.setData(Arrays.copyOfRange(bytes, start, end));
    }
  }

  /**
   * @return The list of textures for this .tpl file.
   */
  public List<Texture> getTextures() {
    return textures;
  }

  /**
   * Adds a texture to the list of textures. It will be appended to the end of the list.
   *
   * @param newTexture The texture to add to the list of textures.
   */
  public void addTexture(Texture newTexture) {
    numOfImages++;
    textures.add(newTexture);
    int totalHeaderSize = getTotalHeaderSize();
    int tplHeaderSize = getTPLHeaderSize();
    for (int i = 0; i < textures.size(); i++) {
      Texture texture = textures.get(i);
      boolean isFirst = i == 0;
      if (isFirst) {
        texture.setImageDataAddress(totalHeaderSize);
      } else {
        Texture previous = textures.get(i - 1);
        int newAddress = previous.getImageDataAddress() + previous.getData().length;
        texture.setImageDataAddress(newAddress);
      }
      texture.setImageHeaderOffset(tplHeaderSize + (i * 36));
    }
  }

  /**
   * Calculates the TPL header size. This is the file header plus the image offset table. The value
   * Will be increased until it is 16-byte aligned. The file header is 12 bytes and the image offset
   * table is the number of images times 8 bytes.
   *
   * @return The TPL header size.
   */
  private int getTPLHeaderSize() {
    int size = 12 + textures.size() * 8;
    int mod = size % 16;
    if (mod != 0) {
      size += 16 - mod;
    }
    return size;
  }

  /**
   * Calculates the total header size. This is the TPL header size plus the palette header and image
   * header. The value Will be increased until it is 16-byte aligned. The palette header is 12 bytes
   * for each image and the image header is 36 bytes for each image.
   *
   * @return The total header size.
   */
  private int getTotalHeaderSize() {
    int size = getTPLHeaderSize();
    size += textures.size() * 36;
    long texturesWithPalette = textures.stream()
        .filter(t -> t.getPaletteHeaderOffset() != 0)
        .count();
    size += texturesWithPalette * 12;
    int mod = size % 16;
    if (mod != 0) {
      size += 16 - mod;
    }
    return size;
  }

  /**
   * Retrieves the texture from a TPL at the given index.
   *
   * @param bytes The TPL bytes.
   * @param index The index of the texture.
   * @return The texture.
   */
  private Texture createTexture(byte[] bytes, int index) {
    Texture.Builder builder = new Texture.Builder();
    int entryOffset = imageTableOffset + (index * 8);
    int imgHeaderOffset = ByteUtils.toUint32(bytes, entryOffset);
    int paletteHeaderOffset = ByteUtils.toUint32(bytes, entryOffset + 4);
    return builder.setImageHeaderOffset(imgHeaderOffset)
        .setPaletteHeaderOffset(paletteHeaderOffset)
        .setHeight(ByteUtils.toUint16(bytes, imgHeaderOffset))
        .setWidth(ByteUtils.toUint16(bytes, imgHeaderOffset + 2))
        .setFormat(ByteUtils.toUint32(bytes, imgHeaderOffset + 4))
        .setImageDataAddress(ByteUtils.toUint32(bytes, imgHeaderOffset + 8))
        .setWrapS(ByteUtils.toUint32(bytes, imgHeaderOffset + 12))
        .setWrapT(ByteUtils.toUint32(bytes, imgHeaderOffset + 16))
        .setMinFilter(ByteUtils.toUint32(bytes, imgHeaderOffset + 20))
        .setMagFilter(ByteUtils.toUint32(bytes, imgHeaderOffset + 24))
        .setLodBias(ByteUtils.toFloat(bytes, imgHeaderOffset + 28))
        .setEdgeLODEnable(bytes[imgHeaderOffset + 32] & 0xFF)
        .setMinLOD(bytes[imgHeaderOffset + 33] & 0xFF)
        .setMaxLOD(bytes[imgHeaderOffset + 34] & 0xFF)
        .setUnpacked(bytes[imgHeaderOffset + 35] & 0xFF)
        .build();
  }

  public byte[] getBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ByteUtils.fromUint32(fileId));
    baos.write(ByteUtils.fromUint32(numOfImages));
    baos.write(ByteUtils.fromUint32(imageTableOffset));
    for (Texture texture : textures) {
      baos.write(ByteUtils.fromUint32(texture.getImageHeaderOffset()));
      baos.write(ByteUtils.fromUint32(texture.getPaletteHeaderOffset()));
    }
    int mod = baos.size() % 16;
    if (mod != 0) {
      baos.write(new byte[16 - mod]);
    }
    for (Texture texture : textures) {
      baos.write(ByteUtils.fromUint16(texture.getHeight()));
      baos.write(ByteUtils.fromUint16(texture.getWidth()));
      baos.write(ByteUtils.fromUint32(texture.getFormat()));
      baos.write(ByteUtils.fromUint32(texture.getImageDataAddress()));
      baos.write(ByteUtils.fromUint32(texture.getWrapS()));
      baos.write(ByteUtils.fromUint32(texture.getWrapT()));
      baos.write(ByteUtils.fromUint32(texture.getMinFilter()));
      baos.write(ByteUtils.fromUint32(texture.getMagFilter()));
      baos.write(ByteUtils.fromFloat(texture.getLodBias()));
      baos.write(texture.getEdgeLODEnable());
      baos.write(texture.getMinLOD());
      baos.write(texture.getMaxLOD());
      baos.write(texture.getUnpacked());
    }
    mod = baos.size() % 16;
    if (mod != 0) {
      baos.write(new byte[16 - mod]);
    }
    for (Texture texture : textures) {
      baos.write(texture.getData());
    }
    return baos.toByteArray();
  }

  @Override
  public String toString() {
    return "TPL{" +
        "fileId=" + fileId +
        ", numOfImages=" + numOfImages +
        ", imageTableOffset=" + imageTableOffset +
        ", textures=" + textures +
        '}';
  }
}
