package com.github.nicholasmoser.gnt4.seq.operands;

public class GlobalOperand implements Operand {

  private final StringBuilder infoBuilder;

  public enum Value {
    HITBOX_IDENTITY_MATRIX,
    CONTROLLERS,
    PRIMARY_CONTROLLER,
    DISPLAY,
    SAVE_DATA,
    DEBUG_MODE,
    PAUSE_GAME,
    GAME_INFO,
    UNUSED
  }

  private final Value value;

  public GlobalOperand(Value value) {
    this.value = value;
    this.infoBuilder = new StringBuilder();
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
    return String.format("EA: %s%s", getName(), infoBuilder);
  }
}
