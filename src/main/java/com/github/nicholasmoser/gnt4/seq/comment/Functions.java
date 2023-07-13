package com.github.nicholasmoser.gnt4.seq.comment;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle functions on SEQ files.
 */
public class Functions {

  private static final Logger LOGGER = Logger.getLogger(Functions.class.getName());
  private static Map<String, Map<Integer, Function>> FUNCTIONS;

  /**
   * Get a mapping of all functions for the given SEQ file. The mapping is of the SEQ file offset to
   * the function.
   *
   * @param fileName The name of the SEQ file to get functions for.
   * @return The SEQ file offset to function mapping.
   */
  public static Map<Integer, Function> getFunctions(String fileName) {
    if (FUNCTIONS == null) {
      FUNCTIONS = getAllFunctions();
    }
    Map<Integer, Function> functions = FUNCTIONS.get(fileName);
    if (functions == null) {
      return Collections.emptyMap();
    }
    return functions;
  }

  private static Map<String, Map<Integer, Function>> getAllFunctions() {
    Map<String, Map<Integer, Function>> allFunctions = new HashMap<>();

    Function costumeModelPhysics = new Function("costume_model_physics",
        List.of("// Branch table on costume id for model physics, e.g. Ino hair, Temari sash"));

    Map<Integer, Function> functions = getChrBaseFunctions(Seqs.ANK_0000);
    functions.put(0x25004, costumeModelPhysics);
    allFunctions.put(Seqs.ANK_0000, functions);

    functions = getChrBaseFunctions(Seqs.BOU_0000);
    functions.put(0x27ABC, costumeModelPhysics);
    allFunctions.put(Seqs.BOU_0000, functions);

    functions = getChrBaseFunctions(Seqs.CHO_0000);
    functions.put(0x26788, costumeModelPhysics);
    allFunctions.put(Seqs.CHO_0000, functions);

    functions = getChrBaseFunctions(Seqs.DOG_0000);
    functions.put(0x1C018, costumeModelPhysics);
    allFunctions.put(Seqs.DOG_0000, functions);

    functions = getChrBaseFunctions(Seqs.GAI_0000);
    functions.put(0x24204, costumeModelPhysics);
    allFunctions.put(Seqs.GAI_0000, functions);

    functions = getChrBaseFunctions(Seqs.GAR_0000);
    functions.put(0x27FBC, costumeModelPhysics);
    allFunctions.put(Seqs.GAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.HAK_0000);
    functions.put(0x24560, costumeModelPhysics);
    allFunctions.put(Seqs.HAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.HI2_0000);
    functions.put(0x23EC8, costumeModelPhysics);
    allFunctions.put(Seqs.HI2_0000, functions);

    functions = getChrBaseFunctions(Seqs.HIN_0000);
    functions.put(0x255B0, costumeModelPhysics);
    allFunctions.put(Seqs.HIN_0000, functions);

    functions = getChrBaseFunctions(Seqs.INO_0000);
    functions.put(0x265B4, costumeModelPhysics);
    allFunctions.put(Seqs.INO_0000, functions);

    functions = getChrBaseFunctions(Seqs.IRU_0000);
    functions.put(0x22C7C, costumeModelPhysics);
    allFunctions.put(Seqs.IRU_0000, functions);

    functions = getChrBaseFunctions(Seqs.ITA_0000);
    functions.put(0x28D94, costumeModelPhysics);
    allFunctions.put(Seqs.ITA_0000, functions);

    functions = getChrBaseFunctions(Seqs.JIR_0000);
    functions.put(0x26158, costumeModelPhysics);
    allFunctions.put(Seqs.JIR_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAB_0000);
    functions.put(0x24C4C, costumeModelPhysics);
    allFunctions.put(Seqs.KAB_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAK_0000);
    functions.put(0x31F1C, costumeModelPhysics);
    allFunctions.put(Seqs.KAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAN_0000);
    functions.put(0x24D84, costumeModelPhysics);
    allFunctions.put(Seqs.KAN_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAR_0000);
    functions.put(0x1F948, costumeModelPhysics);
    allFunctions.put(Seqs.KAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIB_0000);
    functions.put(0x288B4, costumeModelPhysics);
    allFunctions.put(Seqs.KIB_0000, functions);

    functions = getChrBaseFunctions(Seqs.KID_0000);
    functions.put(0x263BC, costumeModelPhysics);
    allFunctions.put(Seqs.KID_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIM_0000);
    functions.put(0x26B94, costumeModelPhysics);
    allFunctions.put(Seqs.KIM_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIS_0000);
    functions.put(0x24C34, costumeModelPhysics);
    allFunctions.put(Seqs.KIS_0000, functions);

    functions = getChrBaseFunctions(Seqs.LOC_0000);
    functions.put(0x277A8, costumeModelPhysics);
    allFunctions.put(Seqs.LOC_0000, functions);

    functions = getChrBaseFunctions(Seqs.MIZ_0000);
    functions.put(0x22E1C, costumeModelPhysics);
    allFunctions.put(Seqs.MIZ_0000, functions);

    functions = getChrBaseFunctions(Seqs.NA9_0000);
    functions.put(0x24360, costumeModelPhysics);
    allFunctions.put(Seqs.NA9_0000, functions);

    functions = getChrBaseFunctions(Seqs.NAR_0000);
    functions.put(0x2F340, costumeModelPhysics);
    allFunctions.put(Seqs.NAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.NEJ_0000);
    functions.put(0x2563C, costumeModelPhysics);
    allFunctions.put(Seqs.NEJ_0000, functions);

    functions = getChrBaseFunctions(Seqs.OBO_0000);
    functions.put(0x1A088, costumeModelPhysics);
    allFunctions.put(Seqs.OBO_0000, functions);

    functions = getChrBaseFunctions(Seqs.ORO_0000);
    functions.put(0x26920, costumeModelPhysics);
    allFunctions.put(Seqs.ORO_0000, functions);

    functions = getChrBaseFunctions(Seqs.SA2_0000);
    functions.put(0x25DF8, costumeModelPhysics);
    allFunctions.put(Seqs.SA2_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAK_0000);
    functions.put(0x26940, costumeModelPhysics);
    allFunctions.put(Seqs.SAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAR_0000);
    functions.put(0x24CE8, costumeModelPhysics);
    allFunctions.put(Seqs.SAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAS_0000);
    functions.put(0x2908C, costumeModelPhysics);
    allFunctions.put(Seqs.SAS_0000, functions);

    functions = getChrBaseFunctions(Seqs.SIK_0000);
    functions.put(0x264DC, costumeModelPhysics);
    allFunctions.put(Seqs.SIK_0000, functions);

    functions = getChrBaseFunctions(Seqs.SIN_0000);
    functions.put(0x252E4, costumeModelPhysics);
    allFunctions.put(Seqs.SIN_0000, functions);

    functions = getChrBaseFunctions(Seqs.SKO_0000);
    functions.put(0x259C0, costumeModelPhysics);
    allFunctions.put(Seqs.SKO_0000, functions);

    functions = getChrBaseFunctions(Seqs.TA2_0000);
    functions.put(0x191B8, costumeModelPhysics);
    allFunctions.put(Seqs.TA2_0000, functions);

    functions = getChrBaseFunctions(Seqs.TAY_0000);
    functions.put(0x27D90, costumeModelPhysics);
    allFunctions.put(Seqs.TAY_0000, functions);

    functions = getChrBaseFunctions(Seqs.TEM_0000);
    functions.put(0x29B14, costumeModelPhysics);
    allFunctions.put(Seqs.TEM_0000, functions);

    functions = getChrBaseFunctions(Seqs.TEN_0000);
    functions.put(0x2B108, costumeModelPhysics);
    allFunctions.put(Seqs.TEN_0000, functions);

    functions = getChrBaseFunctions(Seqs.TSU_0000);
    functions.put(0x255A4, costumeModelPhysics);
    allFunctions.put(Seqs.TSU_0000, functions);

    functions = getChrBaseFunctions(Seqs.ZAB_0000);
    functions.put(0x22EFC, costumeModelPhysics);
    allFunctions.put(Seqs.ZAB_0000, functions);

    functions = new HashMap<>();
    functions.put(0x5670, new Function("change_duplicate_costume",
        List.of("// Change costume if duplicate costume is selected", "7FFFFF25: Opponent costume offset", "7FFFFF24: This costume offset")));
    allFunctions.put(Seqs.CHARSEL, functions);

    return allFunctions;
  }

  /**
   * @return Offset to function mappings all characters have in common.
   */
  private static Map<Integer, Function> getChrBaseFunctions(String chrSeq) {
    Map<Integer, Function> baseFunctions = new HashMap<>();

    baseFunctions.put(0xA1C,
        new Function("CallAction", List.of("// Many actions end by branch and linking to here")));

    Function callAnimation = new Function("CallAnimation", List.of("// gpr2 = The animation ID"));
    baseFunctions.put(getCallAnimationOffset(chrSeq), callAnimation);

    Function darkenScreen = new Function("DarkenScreen", Collections.emptyList());
    baseFunctions.put(getDarkenScreenOffset(chrSeq), darkenScreen);

    Function lightenScreen = new Function("LightenScreen", Collections.emptyList());
    baseFunctions.put(getLightenScreenOffset(chrSeq), lightenScreen);

    baseFunctions.putAll(getChakraSubtractFunctions(chrSeq));

    Function increaseChrModelSize = new Function("IncreaseChrModelSize",
        List.of("// Scale chr model larger by 1.1x"));
    baseFunctions.put(getIncreaseChrModelSizeOffset(chrSeq), increaseChrModelSize);

    return baseFunctions;
  }

  private static int getDarkenScreenOffset(String chrSeq) {
    switch (chrSeq) {
      case Seqs.BOU_0000, Seqs.HI2_0000, Seqs.HIN_0000, Seqs.NAR_0000 -> {
        return 0x1348;
      }
      case Seqs.CHO_0000 -> {
        return 0x13D8;
      }
      case Seqs.DOG_0000, Seqs.KAR_0000 -> {
        return 0x1230;
      }
      case Seqs.KID_0000, Seqs.KIM_0000, Seqs.SA2_0000 -> {
        return 0x125C;
      }
      case Seqs.SKO_0000 -> {
        return 0x12C0;
      }
      default -> {
        return 0x1250;
      }
    }
  }

  private static int getLightenScreenOffset(String chrSeq) {
    switch (chrSeq) {
      case Seqs.BOU_0000, Seqs.HI2_0000, Seqs.HIN_0000, Seqs.NAR_0000 -> {
        return 0x13D4;
      }
      case Seqs.CHO_0000 -> {
        return 0x1464;
      }
      case Seqs.DOG_0000, Seqs.KAR_0000 -> {
        return 0x12BC;
      }
      case Seqs.KID_0000, Seqs.KIM_0000, Seqs.SA2_0000 -> {
        return 0x12E8;
      }
      case Seqs.SKO_0000 -> {
        return 0x134C;
      }
      default -> {
        return 0x12DC;
      }
    }
  }

  private static int getCallAnimationOffset(String chrSeq) {
    switch (chrSeq) {
      case Seqs.DOG_0000, Seqs.KAR_0000 -> {
        return 0xC20;
      }
      case Seqs.KID_0000, Seqs.KIM_0000, Seqs.SA2_0000 -> {
        return 0xC4C;
      }
      case Seqs.SKO_0000 -> {
        return 0xCB0;
      }
      default -> {
        return 0xC40;
      }
    }
  }

  private static int getIncreaseChrModelSizeOffset(String chrSeq) {
    switch (chrSeq) {
      case Seqs.HIN_0000 -> {
        return 0x5E10;
      }
      case Seqs.HI2_0000 -> {
        return 0x5E40;
      }
      case Seqs.GAI_0000, Seqs.KIB_0000, Seqs.TA2_0000 -> {
        return 0x5C90;
      }
      case Seqs.TSU_0000 -> {
        return 0x5CA0;
      }
      case Seqs.JIR_0000, Seqs.SIN_0000 -> {
        return 0x5CC0;
      }
      case Seqs.KAR_0000 -> {
        return 0x5CD0;
      }
      case Seqs.GAR_0000 -> {
        return 0x5CE0;
      }
      case Seqs.NA9_0000 -> {
        return 0x5D00;
      }
      case Seqs.DOG_0000, Seqs.KAN_0000, Seqs.KIS_0000, Seqs.NEJ_0000, Seqs.TEN_0000 -> {
        return 0x5D30;
      }
      case Seqs.ORO_0000, Seqs.SAR_0000, Seqs.ZAB_0000 -> {
        return 0x5D40;
      }
      case Seqs.ANK_0000 -> {
        return 0x5D50;
      }
      case Seqs.KID_0000, Seqs.SAS_0000 -> {
        return 0x5D60;
      }
      case Seqs.KAK_0000, Seqs.TEM_0000 -> {
        return 0x5D80;
      }
      case Seqs.KIM_0000, Seqs.SA2_0000 -> {
        return 0x5DA0;
      }
      case Seqs.ITA_0000, Seqs.SKO_0000 -> {
        return 0x5DB0;
      }
      case Seqs.TAY_0000 -> {
        return 0x5E00;
      }
      case Seqs.BOU_0000 -> {
        return 0x5E60;
      }
      case Seqs.NAR_0000 -> {
        return 0x5EE0;
      }
      case Seqs.CHO_0000 -> {
        return 0x5EF0;
      }
      case Seqs.HAK_0000 -> {
        return 0x61A0;
      }
      default -> {
        return 0x5CF0;
      }
    }
  }

  private static Map<Integer, Function> getChakraSubtractFunctions(String chrSeq) {
    Map<Integer, Function> functions = new HashMap<>();
    Function remove100Meter = new Function("Remove100%Meter", Collections.emptyList());
    Function remove75Meter = new Function("Remove75%Meter", Collections.emptyList());
    Function remove50Meter = new Function("Remove50%Meter", Collections.emptyList());
    Function remove25Meter = new Function("Remove25%Meter", Collections.emptyList());
    switch (chrSeq) {
      case Seqs.KID_0000, Seqs.KIM_0000, Seqs.SA2_0000 -> {
        functions.put(0x10E8, remove100Meter);
        functions.put(0x1100, remove75Meter);
        functions.put(0x1118, remove50Meter);
        functions.put(0x1130, remove25Meter);
      }
      case Seqs.BOU_0000, Seqs.HI2_0000, Seqs.HIN_0000, Seqs.NAR_0000 -> {
        functions.put(0x11D4, remove100Meter);
        functions.put(0x11EC, remove75Meter);
        functions.put(0x1204, remove50Meter);
        functions.put(0x121C, remove25Meter);
      }
      case Seqs.CHO_0000 -> {
        functions.put(0x1264, remove100Meter);
        functions.put(0x127C, remove75Meter);
        functions.put(0x1294, remove50Meter);
        functions.put(0x12AC, remove25Meter);
      }
      case Seqs.DOG_0000, Seqs.KAR_0000 -> {
        functions.put(0x10BC, remove100Meter);
        functions.put(0x10D4, remove75Meter);
        functions.put(0x10EC, remove50Meter);
        functions.put(0x1104, remove25Meter);
      }
      case Seqs.SKO_0000 -> {
        functions.put(0x114C, remove100Meter);
        functions.put(0x1164, remove75Meter);
        functions.put(0x117C, remove50Meter);
        functions.put(0x1194, remove25Meter);
      }
      default -> {
        functions.put(0x10DC, remove100Meter);
        functions.put(0x10F4, remove75Meter);
        functions.put(0x110C, remove50Meter);
        functions.put(0x1124, remove25Meter);
      }
    }
    return functions;
  }
}
