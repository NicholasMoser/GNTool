package com.github.nicholasmoser.mot;

public class TrackFlag {
  public static final int TRANSLATE = 0x01;
  public static final int SCALE = 0x02;
  public static final int ROTATE = 0x08;
  public static final int ENABLED = 0x20;
  public static final int DISABLED = 0x40;

  public static boolean isTranslate(short trackFlag) {
    return (trackFlag & TRANSLATE) == TRANSLATE;
  }

  public static boolean isScale(short trackFlag) {
    return (trackFlag & SCALE) == SCALE;
  }

  public static boolean isRotate(short trackFlag) {
    return (trackFlag & ROTATE) == ROTATE;
  }

  public static boolean isEnabled(short trackFlag) {
    return (trackFlag & ENABLED) == ENABLED;
  }

  public static boolean isDisabled(short trackFlag) {
    return (trackFlag & DISABLED) == DISABLED;
  }
}
