package com.github.nicholasmoser.iso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TOC {

  public List<TOCItem> items;

  public int totalLen;

  public int dataStart;

  public int startIdx;

  public int endIdx;

  public int lastIdx;

  public int directoryCount;

  public int fileCount;

  public Map<Integer, String> directoryIndexToPath;

  public TOC() {
    items = new ArrayList<>();
    items.add(new TOCItem(0, 0, 0, 99999, true, "root", ""));
    items.add(new TOCItem(1, 0, 0, 6, true, "&&SystemData", "&&systemdata/"));
    totalLen = 0;
    dataStart = this.totalLen;
    startIdx = this.totalLen;
    directoryCount = 1;
    fileCount = 4;
  }

  public int size() {
    return items.size();
  }

  public void addItem(TOCItem item) {
    items.add(item);
    if (item.isDir) {
      directoryCount++;
    } else {
      fileCount++;
    }
  }

  public TOCItem getItem(int index) {
    return items.get(index);
  }

  @Override
  public String toString() {
    return "TOC{" +
        "items=" + items +
        ", totalLen=" + totalLen +
        ", dataStart=" + dataStart +
        ", startIdx=" + startIdx +
        ", endIdx=" + endIdx +
        ", lastIdx=" + lastIdx +
        ", directoryCount=" + directoryCount +
        ", fileCount=" + fileCount +
        '}';
  }
}
