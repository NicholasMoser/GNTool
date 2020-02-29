package com.github.nicholasmoser.iso;

public class Iso {

  private int maxImageSize = 1459978240;

  private int filesMod = 2048;

  private char region = 'n';

  private void calcNextFileIds()
  {
    this.toc.fils[2].pos = this.toc.fils.Count + 1;
    this.toc.fils[0].nextIdx = 1;
    this.toc.fils[1].nextIdx = 2;
    this.toc.fils[2].nextIdx = 3;
    this.toc.fils[3].nextIdx = 4;
    this.toc.fils[4].nextIdx = 5;
    this.toc.fils[5].nextIdx = 6;
    MainForm.TOCClass tocclass = (MainForm.TOCClass)this.toc.Clone();
    tocclass.fils.Sort(tocclass);
    this.toc.fils[2].pos = 0;
    int[] array = new int[tocclass.filCount];
    int num = 0;
    for (int i = 0; i < tocclass.fils.Count - 1; i++)
    {
      if (!tocclass.fils[i].isDir)
      {
        array[num] = tocclass.fils[i + 1].TOCIdx;
        num++;
      }
    }
    num = 0;
    for (int i = 0; i < this.toc.fils.Count; i++)
    {
      if (!this.toc.fils[i].isDir)
      {
        this.toc.fils[i].nextIdx = array[num];
        num++;
        if (num == tocclass.filCount)
        {
          this.toc.endIdx = array[num - 2];
          this.toc.lastIdx = i;
        }
      }
    }
    num = this.toc.fils[2].nextIdx;
    for (int i = 0; i < this.toc.fils.Count - 1; i++)
    {
      if (!this.toc.fils[i].isDir)
      {
        this.toc.fils[num].prevIdx = i;
        int num2 = i + 1;
        while (num2 < this.toc.fils.Count - 1 && this.toc.fils[num2].isDir)
        {
          i++;
          num2++;
        }
        num = this.toc.fils[i + 1].nextIdx;
      }
    }
    this.toc.fils[this.toc.fils.Count - 1].nextIdx = this.toc.fils.Count - 1;
  }
}
