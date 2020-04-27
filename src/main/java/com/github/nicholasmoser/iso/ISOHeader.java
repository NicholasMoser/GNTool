package com.github.nicholasmoser.iso;

import java.util.List;
import java.util.Objects;

/**
 * The header for an ISO. This describes the sys directory, sys files, and files and directories
 * under the files directory. This information is required to build or read an ISO.
 */
public class ISOHeader {

  /* The sys directory. */
  private ISODirectory sys;

  /* The disk header file. */
  private ISOFile bootBin;

  /* The disk header information file. */
  private ISOFile bi2Bin;

  /* The apploader. */
  private ISOFile apploaderImg;

  /* The PowerPC executable code of the game. */
  private ISOFile mainDol;

  /* The file system table of the disk. */
  private ISOFile fstBin;

  /* All files and directories under the files directory. */
  private List<ISOItem> files;

  /**
   * Creates a new ISOHeader.
   *
   * @param sys          The sys directory.
   * @param bootBin      The boot.bin file.
   * @param bi2Bin       The bi2.bin file.
   * @param apploaderImg The apploader.img file.
   * @param mainDol      The main.dol file.
   * @param fstBin       The fst.bin file.
   * @param files        The files and directories under the files directory.
   */
  private ISOHeader(ISODirectory sys, ISOFile bootBin, ISOFile bi2Bin, ISOFile apploaderImg,
      ISOFile mainDol, ISOFile fstBin, List<ISOItem> files) {
    if (sys == null) {
      throw new IllegalArgumentException("sys cannot be null");
    } else if (bootBin == null) {
      throw new IllegalArgumentException("bootBin cannot be null");
    } else if (bi2Bin == null) {
      throw new IllegalArgumentException("bi2Bin cannot be null");
    } else if (apploaderImg == null) {
      throw new IllegalArgumentException("apploaderImg cannot be null");
    } else if (mainDol == null) {
      throw new IllegalArgumentException("mainDol cannot be null");
    } else if (fstBin == null) {
      throw new IllegalArgumentException("fstBin cannot be null");
    } else if (files == null) {
      throw new IllegalArgumentException("files cannot be null");
    }
    this.sys = sys;
    this.bootBin = bootBin;
    this.bi2Bin = bi2Bin;
    this.apploaderImg = apploaderImg;
    this.mainDol = mainDol;
    this.fstBin = fstBin;
    this.files = files;
  }

  /**
   * @return The sys directory.
   */
  public ISODirectory getSys() {
    return sys;
  }

  /**
   * @return The boot.bin file.
   */
  public ISOFile getBootBin() {
    return bootBin;
  }

  /**
   * @return The bi2.bin file.
   */
  public ISOFile getBi2Bin() {
    return bi2Bin;
  }

  /**
   * @return The apploader.img file.
   */
  public ISOFile getApploaderImg() {
    return apploaderImg;
  }

  /**
   * @return The main.dol file.
   */
  public ISOFile getMainDol() {
    return mainDol;
  }

  /**
   * @return The fst.bin file.
   */
  public ISOFile getFstBin() {
    return fstBin;
  }

  /**
   * @return The files and directories under the files directory.
   */
  public List<ISOItem> getFiles() {
    return files;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ISOHeader isoHeader = (ISOHeader) o;
    return sys.equals(isoHeader.sys) &&
        bootBin.equals(isoHeader.bootBin) &&
        bi2Bin.equals(isoHeader.bi2Bin) &&
        apploaderImg.equals(isoHeader.apploaderImg) &&
        mainDol.equals(isoHeader.mainDol) &&
        fstBin.equals(isoHeader.fstBin) &&
        files.equals(isoHeader.files);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sys, bootBin, bi2Bin, apploaderImg, mainDol, fstBin, files);
  }

  @Override
  public String toString() {
    return "ISOHeader{" +
        "\nsys=" + sys +
        ", \nbootBin=" + bootBin +
        ", \nbi2Bin=" + bi2Bin +
        ", \napploaderImg=" + apploaderImg +
        ", \nmainDol=" + mainDol +
        ", \nfstBin=" + fstBin +
        ", \nfiles=" + files +
        '}';
  }

  /**
   * Builder class for ISOHeader.
   */
  public static class Builder {

    private ISODirectory sys;
    private ISOFile bootBin;
    private ISOFile bi2Bin;
    private ISOFile apploaderImg;
    private ISOFile mainDol;
    private ISOFile fstBin;
    private List<ISOItem> files;

    public Builder setSys(ISODirectory sys) {
      this.sys = sys;
      return this;
    }

    public Builder setBootBin(ISOFile bootBin) {
      this.bootBin = bootBin;
      return this;
    }

    public Builder setBi2Bin(ISOFile bi2Bin) {
      this.bi2Bin = bi2Bin;
      return this;
    }

    public Builder setApploaderImg(ISOFile apploaderImg) {
      this.apploaderImg = apploaderImg;
      return this;
    }

    public Builder setMainDol(ISOFile mainDol) {
      this.mainDol = mainDol;
      return this;
    }

    public Builder setFstBin(ISOFile fstBin) {
      this.fstBin = fstBin;
      return this;
    }

    public Builder setFiles(List<ISOItem> files) {
      this.files = files;
      return this;
    }

    public ISOHeader createISOHeader() {
      return new ISOHeader(sys, bootBin, bi2Bin, apploaderImg, mainDol, fstBin, files);
    }
  }
}