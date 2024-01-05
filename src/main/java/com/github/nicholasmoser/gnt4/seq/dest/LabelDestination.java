package com.github.nicholasmoser.gnt4.seq.dest;

import com.github.nicholasmoser.utils.ByteUtils;
import java.util.Map;

/**
 * A destination represented by a symbol label. To resolve the label you may pass a label mapping,
 * where each label is mapped to each offset.
 */
public class LabelDestination implements Destination {
  private final String label;
  private boolean isResolved;
  private int offset;

  public LabelDestination(String label) {
    this.label = label;
    this.offset = 0xFFFFFFFF;
    this.isResolved = false;
  }

  @Override
  public int offset() {
    return offset;
  }

  @Override
  public byte[] bytes() {
    return ByteUtils.fromInt32(offset);
  }

  @Override
  public String toString() {
    if (isResolved) {
      return String.format("0x%X", offset);
    }
    return label;
  }

  public void resolve(Map<String, Integer> labelMap) {
    Integer newOffset = labelMap.get(label);
    if (newOffset == null) {
      throw new IllegalStateException("Unable to find branching label: " + label);
    }
    offset = newOffset;
    isResolved = true;
  }
}
