package com.github.nicholasmoser.gnt4.seq.operands;

public class GlobalOperand implements Operand {

  private final StringBuilder infoBuilder;

  public enum Value {
    HITBOX_IDENTITY_MATRIX, // Init to size 0xc78 at 0x8001521c
    CONTROLLERS, // Init to size 0x100 at 0x80014160
    PRIMARY_CONTROLLER,
    DISPLAY, // Init to size 0x40 at 0x80015230
    SAVE_DATA, // 1c0?
    DEBUG_MODE, // 84?
    PAUSE_GAME,
    GAME_INFO,
    UNUSED
  }

  private final Value value;

  public GlobalOperand(Value value) {
    this.value = value;
    this.infoBuilder = new StringBuilder();
  }

  @Override
  public int get() {
    return getAddress();
  }

  public String getName() {
    return value.toString();
  }

  public Value getValue() {
    return value;
  }

  public int getAddress() {
    return switch(value) {
      case HITBOX_IDENTITY_MATRIX -> 0x80223428;
      case CONTROLLERS -> 0x80222eb0;
      case PRIMARY_CONTROLLER -> 0x80222e70;
      case DISPLAY -> 0x802231a8;
      case SAVE_DATA -> 0x802231e8;
      case DEBUG_MODE -> 0x802233a8;
      case PAUSE_GAME -> 0x80222fb0;
      case GAME_INFO -> 0x802261d8;
      case UNUSED -> 0x80222e64;
    };
  }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public String toString() {
    return String.format("%s%s", getName(), infoBuilder);
  }
}
