package com.github.nicholasmoser.iso;

import java.util.ArrayList;
import java.util.List;

public class TOC {

  public List<TOCItemFil> fils;

  public int totalLen;

  public int dataStart;

  public int startIdx;

  public int endIdx;

  public int lastIdx;

  public int dirCount = 1;

  public int filCount = 4;

  public TOC(String resPath)
  {
    fils = new ArrayList<TOCItemFil>();
    fils.add(new TOCItemFil(0, 0, 0, 99999, true, "root", "", resPath));
    fils.add(new TOCItemFil(1, 0, 0, 6, true, "&&SystemData", "&&systemdata\\", resPath + "&&systemdata\\"));
    fils.add(new TOCItemFil(2, 1, 0, 99999, false, "ISO.hdr", "&&SystemData\\iso.hdr", resPath + "&&SystemData\\iso.hdr"));
    fils.add(new TOCItemFil(3, 1, 9280, 99999, false, "AppLoader.ldr", "&&SystemData\\apploader.ldr", resPath + "&&SystemData\\apploader.ldr"));
    fils.add(new TOCItemFil(4, 1, 0, 99999, false, "Start.dol", "&&SystemData\\start.dol", resPath + "&&SystemData\\start.dol"));
    fils.add(new TOCItemFil(5, 1, 0, 99999, false, "Game.toc", "&&SystemData\\game.toc", resPath + "&&SystemData\\game.toc"));
    totalLen = 0;
    dataStart = this.totalLen;
    startIdx = this.totalLen;
  }

  public int Compare(TOCItemFil x, TOCItemFil y)
  {
    if (x.pos > y.pos)
    {
      return 1;
    }
    if (x.pos < y.pos)
    {
      return -1;
    }
    return 0;
  }
}
