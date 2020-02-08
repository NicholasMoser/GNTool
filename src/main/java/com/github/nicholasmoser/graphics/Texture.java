package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.util.Arrays;

/**
 * A single texture file inside of a .tpl file.
 */
public class Texture {

  private byte[] data;
  private int imageHeaderOffset;
  // palette is not supported yet
  private int paletteHeaderOffset;
  private int height;
  private int width;
  private int format;
  private int imageDataAddress;
  private int wrapS;
  private int wrapT;
  private int minFilter;
  private int magFilter;
  private float lodBias;
  private int edgeLODEnable;
  private int minLOD;
  private int maxLOD;
  private int unpacked;

  /**
   * Private constructor for a Texture.
   *
   * @param imageHeaderOffset   The offset of the image header.
   * @param paletteHeaderOffset The offset of the palette header or NULL.
   * @param height              The Height.
   * @param width               The Width.
   * @param format              The Format (http://wiki.tockdom.com/wiki/Image_Formats).
   * @param imageDataAddress    The Image Data Address.
   * @param wrapS               The WrapS.
   * @param wrapT               The WrapT.
   * @param minFilter           The MinFilter.
   * @param magFilter           The MagFilter.
   * @param lodBias             The LODBias.
   * @param edgeLODEnable       The EdgeLODEnable.
   * @param minLOD              The MinLOD.
   * @param maxLOD              The MaxLOD.
   * @param unpacked            The Unpacked.
   */
  private Texture(int imageHeaderOffset, int paletteHeaderOffset, int height, int width, int format,
      int imageDataAddress, int wrapS, int wrapT, int minFilter,
      int magFilter, float lodBias, int edgeLODEnable, int minLOD, int maxLOD, int unpacked) {
    this.imageHeaderOffset = imageHeaderOffset;
    this.paletteHeaderOffset = paletteHeaderOffset;
    this.height = height;
    this.width = width;
    this.format = format;
    this.imageDataAddress = imageDataAddress;
    this.wrapS = wrapS;
    this.wrapT = wrapT;
    this.minFilter = minFilter;
    this.magFilter = magFilter;
    this.lodBias = lodBias;
    this.edgeLODEnable = edgeLODEnable;
    this.minLOD = minLOD;
    this.maxLOD = maxLOD;
    this.unpacked = unpacked;
    data = new byte[0];
  }

  /**
   * Creates a copy of the current texture to allow duplicating it for a TPL file. The
   * imageDataAddress must also be specified since it will now be different.
   *
   * @param texture          The texture to create a new instance of.
   * @return The new texture instance.
   */
  public static Texture newInstance(Texture texture) {
    Texture.Builder builder = new Texture.Builder();
    Texture newTexture = builder.setImageHeaderOffset(texture.getImageHeaderOffset())
        .setPaletteHeaderOffset(texture.getPaletteHeaderOffset())
        .setHeight(texture.getHeight())
        .setWidth(texture.getWidth())
        .setFormat(texture.getFormat())
        .setWrapS(texture.getWrapS())
        .setWrapT(texture.getWrapT())
        .setMinFilter(texture.getMinFilter())
        .setMagFilter(texture.getMagFilter())
        .setLodBias(texture.getLodBias())
        .setEdgeLODEnable(texture.getEdgeLODEnable())
        .setMinLOD(texture.getMinLOD())
        .setMaxLOD(texture.getMaxLOD())
        .setUnpacked(texture.getUnpacked())
        .build();
    byte[] data = texture.getData();
    newTexture.setData(Arrays.copyOf(data, data.length));
    return newTexture;
  }

  /**
   * Sets the data for the texture. The data must be set separately from the rest of the fields
   * because it is parsed after creation of all texture headers.
   *
   * @param data The data.
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Sets the ImageDataAddress for the texture. This is left mutable since adding new textures
   * to a .tpl file will change this value.
   *
   * @param imageDataAddress The image data address.
   */
  public void setImageDataAddress(int imageDataAddress) {
    this.imageDataAddress = imageDataAddress;
  }

  /**
   * Sets the ImageHeaderOffset for the texture. This is left mutable since adding new textures
   * to a .tpl file will change this value.
   *
   * @param imageHeaderOffset The image header offset;
   */
  public void setImageHeaderOffset(int imageHeaderOffset) {
    this.imageHeaderOffset = imageHeaderOffset;
  }

  public byte[] getData() {
    return data;
  }

  public int getImageHeaderOffset() {
    return imageHeaderOffset;
  }

  public int getPaletteHeaderOffset() {
    return paletteHeaderOffset;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getFormat() {
    return format;
  }

  public int getImageDataAddress() {
    return imageDataAddress;
  }

  public int getWrapS() {
    return wrapS;
  }

  public int getWrapT() {
    return wrapT;
  }

  public int getMinFilter() {
    return minFilter;
  }

  public int getMagFilter() {
    return magFilter;
  }

  public float getLodBias() {
    return lodBias;
  }

  public int getEdgeLODEnable() {
    return edgeLODEnable;
  }

  public int getMinLOD() {
    return minLOD;
  }

  public int getMaxLOD() {
    return maxLOD;
  }

  public int getUnpacked() {
    return unpacked;
  }

  public byte[] getImageOffsetTableEntryBytes() {
    return Bytes.concat(ByteUtils.fromUint32(imageHeaderOffset),
        ByteUtils.fromUint32(paletteHeaderOffset));
  }

  public byte[] getImageHeaderBytes() {
    byte[] heightBytes = ByteUtils.fromUint16(height);
    byte[] widthBytes = ByteUtils.fromUint16(width);
    byte[] formatBytes = ByteUtils.fromUint32(format);
    byte[] imageDataAddressBytes = ByteUtils.fromUint32(imageDataAddress);
    byte[] wrapSBytes = ByteUtils.fromUint32(wrapS);
    byte[] wrapTBytes = ByteUtils.fromUint32(wrapT);
    byte[] minFilterBytes = ByteUtils.fromUint32(minFilter);
    byte[] magFilterBytes = ByteUtils.fromUint32(magFilter);
    byte[] lodBiasBytes = ByteUtils.fromFloat(lodBias);
    byte[] edgeLODEnableBytes = new byte[]{(byte) edgeLODEnable};
    byte[] minLODBytes = new byte[]{(byte) minLOD};
    byte[] maxLODBytes = new byte[]{(byte) maxLOD};
    byte[] unpackedBytes = new byte[]{(byte) unpacked};
    return Bytes.concat(heightBytes, widthBytes, formatBytes, imageDataAddressBytes, wrapSBytes,
        wrapTBytes, minFilterBytes, magFilterBytes, lodBiasBytes, edgeLODEnableBytes, minLODBytes,
        maxLODBytes, unpackedBytes);
  }

  @Override
  public String toString() {
    return "Texture{" +
        "imageHeaderOffset=" + imageHeaderOffset +
        ", paletteHeaderOffset=" + paletteHeaderOffset +
        ", height=" + height +
        ", width=" + width +
        ", format=" + format +
        ", imageDataAddress=" + imageDataAddress +
        ", wrapS=" + wrapS +
        ", wrapT=" + wrapT +
        ", minFilter=" + minFilter +
        ", magFilter=" + magFilter +
        ", lodBias=" + lodBias +
        ", edgeLODEnable=" + edgeLODEnable +
        ", minLOD=" + minLOD +
        ", maxLOD=" + maxLOD +
        ", unpacked=" + unpacked +
        ", dataLength=" + data.length +
        '}';
  }

  /**
   * Builder class for a Texture.
   */
  public static class Builder {

    private int imageHeaderOffset;
    private int paletteHeaderOffset;
    private int height;
    private int width;
    private int format;
    private int imageDataAddress;
    private int wrapS;
    private int wrapT;
    private int minFilter;
    private int magFilter;
    private float lodBias;
    private int edgeLODEnable;
    private int minLOD;
    private int maxLOD;
    private int unpacked;

    public Builder setImageHeaderOffset(int imageHeaderOffset) {
      this.imageHeaderOffset = imageHeaderOffset;
      return this;
    }

    public Builder setPaletteHeaderOffset(int paletteHeaderOffset) {
      this.paletteHeaderOffset = paletteHeaderOffset;
      return this;
    }

    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    public Builder setFormat(int format) {
      this.format = format;
      return this;
    }

    public Builder setImageDataAddress(int imageDataAddress) {
      this.imageDataAddress = imageDataAddress;
      return this;
    }

    public Builder setWrapS(int wrapS) {
      this.wrapS = wrapS;
      return this;
    }

    public Builder setWrapT(int wrapT) {
      this.wrapT = wrapT;
      return this;
    }

    public Builder setMinFilter(int minFilter) {
      this.minFilter = minFilter;
      return this;
    }

    public Builder setMagFilter(int magFilter) {
      this.magFilter = magFilter;
      return this;
    }

    public Builder setLodBias(float lodBias) {
      this.lodBias = lodBias;
      return this;
    }

    public Builder setEdgeLODEnable(int edgeLODEnable) {
      this.edgeLODEnable = edgeLODEnable;
      return this;
    }

    public Builder setMinLOD(int minLOD) {
      this.minLOD = minLOD;
      return this;
    }

    public Builder setMaxLOD(int maxLOD) {
      this.maxLOD = maxLOD;
      return this;
    }

    public Builder setUnpacked(int unpacked) {
      this.unpacked = unpacked;
      return this;
    }

    public Texture build() {
      return new Texture(imageHeaderOffset, paletteHeaderOffset, height, width, format,
          imageDataAddress, wrapS, wrapT, minFilter, magFilter, lodBias, edgeLODEnable, minLOD,
          maxLOD, unpacked);
    }
  }
}