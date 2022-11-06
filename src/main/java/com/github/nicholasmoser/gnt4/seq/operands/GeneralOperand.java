package com.github.nicholasmoser.gnt4.seq.operands;

import java.nio.ByteBuffer;

public abstract class GeneralOperand implements Operand {
    private final int index;
    private boolean pointer;
    private final StringBuilder infoBuilder;
    private int fieldOffset;
    private Object obj;

    public GeneralOperand(int index, boolean isPointer) {
        this.index = index;
        this.pointer = isPointer;
        this.infoBuilder = new StringBuilder();
        this.fieldOffset = -1;
        this.obj = null;
    }

    public GeneralOperand(int index, boolean isPointer, Object obj) {
        this.index = index;
        this.pointer = isPointer;
        this.infoBuilder = new StringBuilder();
        this.fieldOffset = -1;
        this.obj = obj;
    }

    @Override
    public int get() { return getIndex(); }

    @Override
    public void addInfo(String info) {
        infoBuilder.append(info);
    }

    @Override
    public void withField(int fieldOffset) {
        this.fieldOffset = fieldOffset;
    }

    public int getFieldOffset() { return fieldOffset; }

    public int getIndex() {
        return index;
    }

    public Object getValue() {
        if (pointer)
            return index;
        if (fieldOffset != -1)
            if (obj instanceof ByteBuffer bb)
                return bb.position(fieldOffset);
            else
                throw new IllegalStateException("We only do bytebuffers here");
        return obj;
    }

    public boolean isPointer() {
        return pointer;
    }

    @Override
    abstract public String toString();
}
