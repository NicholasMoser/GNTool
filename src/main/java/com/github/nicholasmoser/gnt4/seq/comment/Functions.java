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

  // TODO: Add notes
  // 0xB30 For Iruka, this is where the Action ID offset is read from the Action ID table

  private static final Logger LOGGER = Logger.getLogger(Functions.class.getName());
  private static Map<String, Map<Integer, Function>> FUNCTIONS;

  /**
   * Get a mapping of all functions for the given SEQ file. The mapping is of the SEQ file offset
   * to the function.
   *
   * @param fileName The name of the SEQ file to get functions for.
   * @return The SEQ file offset to function mapping.
   */
  public static Map<Integer, Function> getFunctions(String fileName) {
    if (FUNCTIONS == null) {
      FUNCTIONS = getAllFunctions();
    }
    Map<Integer, Function> functions =  FUNCTIONS.get(fileName);
    if (functions == null) {
      LOGGER.log(Level.INFO, "No functions found for file " + fileName);
      return Collections.emptyMap();
    }
    return functions;
  }

  private static Map<String, Map<Integer, Function>> getAllFunctions() {
    Map<String, Map<Integer, Function>> allFunctions = new HashMap<>();
    Function increaseChrModelSize = new Function("IncreaseChrModelSize", List.of("// Scale chr model larger by 1.1x"));

    Map<Integer, Function> functions = getChrBaseFunctions(Seqs.ANK_0000);
    functions.put(0x5D50, increaseChrModelSize);
    allFunctions.put(Seqs.ANK_0000, functions);

    functions = getChrBaseFunctions(Seqs.BOU_0000);
    functions.put(0x5E60, increaseChrModelSize);
    allFunctions.put(Seqs.BOU_0000, functions);

    functions = getChrBaseFunctions(Seqs.CHO_0000);
    functions.put(0x5EF0, increaseChrModelSize);
    allFunctions.put(Seqs.CHO_0000, functions);

    functions = getChrBaseFunctions(Seqs.DOG_0000);
    functions.put(0x5D30, increaseChrModelSize);
    allFunctions.put(Seqs.DOG_0000, functions);

    functions = getChrBaseFunctions(Seqs.GAI_0000);
    functions.put(0x5C90, increaseChrModelSize);
    allFunctions.put(Seqs.GAI_0000, functions);

    functions = getChrBaseFunctions(Seqs.GAR_0000);
    functions.put(0x5CE0, increaseChrModelSize);
    allFunctions.put(Seqs.GAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.HAK_0000);
    functions.put(0x61A0, increaseChrModelSize);
    allFunctions.put(Seqs.HAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.HI2_0000);
    functions.put(0x5E40, increaseChrModelSize);
    allFunctions.put(Seqs.HI2_0000, functions);

    functions = getChrBaseFunctions(Seqs.HIN_0000);
    functions.put(0x5E10, increaseChrModelSize);
    allFunctions.put(Seqs.HIN_0000, functions);

    functions = getChrBaseFunctions(Seqs.INO_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.INO_0000, functions);

    functions = getChrBaseFunctions(Seqs.IRU_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.IRU_0000, functions);

    functions = getChrBaseFunctions(Seqs.ITA_0000);
    functions.put(0x5DB0, increaseChrModelSize);
    allFunctions.put(Seqs.ITA_0000, functions);

    functions = getChrBaseFunctions(Seqs.JIR_0000);
    functions.put(0x5CC0, increaseChrModelSize);
    allFunctions.put(Seqs.JIR_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAB_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.KAB_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAK_0000);
    functions.put(0x5D80, increaseChrModelSize);
    allFunctions.put(Seqs.KAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAN_0000);
    functions.put(0x5D30, increaseChrModelSize);
    allFunctions.put(Seqs.KAN_0000, functions);

    functions = getChrBaseFunctions(Seqs.KAR_0000);
    functions.put(0x5CD0, increaseChrModelSize);
    allFunctions.put(Seqs.KAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIB_0000);
    functions.put(0x5C90, increaseChrModelSize);
    allFunctions.put(Seqs.KIB_0000, functions);

    functions = getChrBaseFunctions(Seqs.KID_0000);
    functions.put(0x5D60, increaseChrModelSize);
    allFunctions.put(Seqs.KID_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIM_0000);
    functions.put(0x5DA0, increaseChrModelSize);
    allFunctions.put(Seqs.KIM_0000, functions);

    functions = getChrBaseFunctions(Seqs.KIS_0000);
    functions.put(0x5D30, increaseChrModelSize);
    allFunctions.put(Seqs.KIS_0000, functions);

    functions = getChrBaseFunctions(Seqs.LOC_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.LOC_0000, functions);

    functions = getChrBaseFunctions(Seqs.MIZ_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.MIZ_0000, functions);

    functions = getChrBaseFunctions(Seqs.NA9_0000);
    functions.put(0x5D00, increaseChrModelSize);
    allFunctions.put(Seqs.NA9_0000, functions);

    functions = getChrBaseFunctions(Seqs.NAR_0000);
    functions.put(0x5EE0, increaseChrModelSize);
    allFunctions.put(Seqs.NAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.NEJ_0000);
    functions.put(0x5D30, increaseChrModelSize);
    allFunctions.put(Seqs.NEJ_0000, functions);

    functions = getChrBaseFunctions(Seqs.OBO_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.OBO_0000, functions);

    functions = getChrBaseFunctions(Seqs.ORO_0000);
    functions.put(0x5D40, increaseChrModelSize);
    allFunctions.put(Seqs.ORO_0000, functions);

    functions = getChrBaseFunctions(Seqs.SA2_0000);
    functions.put(0x5DA0, increaseChrModelSize);
    allFunctions.put(Seqs.SA2_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAK_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.SAK_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAR_0000);
    functions.put(0x5D40, increaseChrModelSize);
    allFunctions.put(Seqs.SAR_0000, functions);

    functions = getChrBaseFunctions(Seqs.SAS_0000);
    functions.put(0x5D60, increaseChrModelSize);
    allFunctions.put(Seqs.SAS_0000, functions);

    functions = getChrBaseFunctions(Seqs.SIK_0000);
    functions.put(0x5CF0, increaseChrModelSize);
    allFunctions.put(Seqs.SIK_0000, functions);

    functions = getChrBaseFunctions(Seqs.SIN_0000);
    functions.put(0x5CC0, increaseChrModelSize);
    allFunctions.put(Seqs.SIN_0000, functions);

    functions = getChrBaseFunctions(Seqs.SKO_0000);
    functions.put(0x5DB0, increaseChrModelSize);
    allFunctions.put(Seqs.SKO_0000, functions);

    functions = getChrBaseFunctions(Seqs.TA2_0000);
    functions.put(0x5C90, increaseChrModelSize);
    allFunctions.put(Seqs.TA2_0000, functions);

    functions = getChrBaseFunctions(Seqs.TAY_0000);
    functions.put(0x5E00, increaseChrModelSize);
    allFunctions.put(Seqs.TAY_0000, functions);

    functions = getChrBaseFunctions(Seqs.TEM_0000);
    functions.put(0x5D80, increaseChrModelSize);
    allFunctions.put(Seqs.TEM_0000, functions);

    functions = getChrBaseFunctions(Seqs.TEN_0000);
    functions.put(0x5D30, increaseChrModelSize);
    allFunctions.put(Seqs.TEN_0000, functions);

    functions = getChrBaseFunctions(Seqs.TSU_0000);
    functions.put(0x5CA0, increaseChrModelSize);
    allFunctions.put(Seqs.TSU_0000, functions);

    functions = getChrBaseFunctions(Seqs.ZAB_0000);
    functions.put(0x5D40, increaseChrModelSize);
    allFunctions.put(Seqs.ZAB_0000, functions);

    return allFunctions;
  }

  /**
   * @return Offset to function mappings all characters have in common.
   */
  private static Map<Integer, Function> getChrBaseFunctions(String chrSeq) {
    Map<Integer, Function> baseFunctions = new HashMap<>();
    baseFunctions.put(0xA1C, new Function("CallAction", List.of("// Many actions end by branch and linking to here")));
    baseFunctions.put(0xC40, new Function("CallAnimation", List.of("// gpr2 = The animation ID")));
    baseFunctions.putAll(getChakraSubtractFunctions(chrSeq));
    return baseFunctions;
  }

  /**
   * Return a mapping of the functions that subtract chakra.
   *
   * @param chrSeq The chr seq file.
   * @return a mapping of the functions that subtract chakra
   */
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
