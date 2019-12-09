package com.github.nicholasmoser.gnt4;

import java.nio.file.Path;
import com.github.nicholasmoser.Code;

public class GNT4Code implements Code {
  enum ID {
    AUDIO_FIX,
    UNLOCK_EVERYTHING,
    SKIP_CUTSCENES,
    ASPECT_RATIO
  }
  
  private Path filePath;
}
