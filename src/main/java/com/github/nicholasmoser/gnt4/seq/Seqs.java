package com.github.nicholasmoser.gnt4.seq;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ChoiceDialog;

public class Seqs {

  public static final String ANK_0000 = "files/chr/ank/0000.seq";
  public static final String ANK_0010 = "files/chr/ank/0010.seq";
  public static final String ANK_1000 = "files/chr/ank/1000.seq";
  public static final String BOU_0000 = "files/chr/bou/0000.seq";
  public static final String BOU_0010 = "files/chr/bou/0010.seq";
  public static final String BOU_1000 = "files/chr/bou/1000.seq";
  public static final String CHO_0000 = "files/chr/cho/0000.seq";
  public static final String CHO_0010 = "files/chr/cho/0010.seq";
  public static final String CHO_1000 = "files/chr/cho/1000.seq";
  public static final String CMN_1000 = "files/chr/cmn/1000.seq";
  public static final String DOG_0000 = "files/chr/dog/0000.seq";
  public static final String DOG_0010 = "files/chr/dog/0010.seq";
  public static final String DOG_1000 = "files/chr/dog/1000.seq";
  public static final String GAI_0000 = "files/chr/gai/0000.seq";
  public static final String GAI_0010 = "files/chr/gai/0010.seq";
  public static final String GAI_1000 = "files/chr/gai/1000.seq";
  public static final String GAR_0000 = "files/chr/gar/0000.seq";
  public static final String GAR_0010 = "files/chr/gar/0010.seq";
  public static final String GAR_1000 = "files/chr/gar/1000.seq";
  public static final String HAK_0000 = "files/chr/hak/0000.seq";
  public static final String HAK_0010 = "files/chr/hak/0010.seq";
  public static final String HAK_1000 = "files/chr/hak/1000.seq";
  public static final String HI2_0000 = "files/chr/hi2/0000.seq";
  public static final String HI2_0010 = "files/chr/hi2/0010.seq";
  public static final String HI2_1000 = "files/chr/hi2/1000.seq";
  public static final String HIN_0000 = "files/chr/hin/0000.seq";
  public static final String HIN_0010 = "files/chr/hin/0010.seq";
  public static final String HIN_1000 = "files/chr/hin/1000.seq";
  public static final String INO_0000 = "files/chr/ino/0000.seq";
  public static final String INO_0010 = "files/chr/ino/0010.seq";
  public static final String INO_1000 = "files/chr/ino/1000.seq";
  public static final String IRU_0000 = "files/chr/iru/0000.seq";
  public static final String IRU_0010 = "files/chr/iru/0010.seq";
  public static final String IRU_1000 = "files/chr/iru/1000.seq";
  public static final String ITA_0000 = "files/chr/ita/0000.seq";
  public static final String ITA_0010 = "files/chr/ita/0010.seq";
  public static final String ITA_1000 = "files/chr/ita/1000.seq";
  public static final String JIR_0000 = "files/chr/jir/0000.seq";
  public static final String JIR_0010 = "files/chr/jir/0010.seq";
  public static final String JIR_1000 = "files/chr/jir/1000.seq";
  public static final String KAB_0000 = "files/chr/kab/0000.seq";
  public static final String KAB_0010 = "files/chr/kab/0010.seq";
  public static final String KAB_1000 = "files/chr/kab/1000.seq";
  public static final String KAK_0000 = "files/chr/kak/0000.seq";
  public static final String KAK_0010 = "files/chr/kak/0010.seq";
  public static final String KAK_1000 = "files/chr/kak/1000.seq";
  public static final String KAN_0000 = "files/chr/kan/0000.seq";
  public static final String KAN_0010 = "files/chr/kan/0010.seq";
  public static final String KAN_1000 = "files/chr/kan/1000.seq";
  public static final String KAR_0000 = "files/chr/kar/0000.seq";
  public static final String KAR_0010 = "files/chr/kar/0010.seq";
  public static final String KAR_1000 = "files/chr/kar/1000.seq";
  public static final String KIB_0000 = "files/chr/kib/0000.seq";
  public static final String KIB_0010 = "files/chr/kib/0010.seq";
  public static final String KIB_1000 = "files/chr/kib/1000.seq";
  public static final String KID_0000 = "files/chr/kid/0000.seq";
  public static final String KID_0010 = "files/chr/kid/0010.seq";
  public static final String KID_1000 = "files/chr/kid/1000.seq";
  public static final String KIM_0000 = "files/chr/kim/0000.seq";
  public static final String KIM_0010 = "files/chr/kim/0010.seq";
  public static final String KIM_1000 = "files/chr/kim/1000.seq";
  public static final String KIS_0000 = "files/chr/kis/0000.seq";
  public static final String KIS_0010 = "files/chr/kis/0010.seq";
  public static final String KIS_1000 = "files/chr/kis/1000.seq";
  public static final String LOC_0000 = "files/chr/loc/0000.seq";
  public static final String LOC_0010 = "files/chr/loc/0010.seq";
  public static final String LOC_1000 = "files/chr/loc/1000.seq";
  public static final String MIZ_0000 = "files/chr/miz/0000.seq";
  public static final String MIZ_0010 = "files/chr/miz/0010.seq";
  public static final String MIZ_1000 = "files/chr/miz/1000.seq";
  public static final String NA9_0000 = "files/chr/na9/0000.seq";
  public static final String NA9_0010 = "files/chr/na9/0010.seq";
  public static final String NA9_1000 = "files/chr/na9/1000.seq";
  public static final String NAR_0000 = "files/chr/nar/0000.seq";
  public static final String NAR_0010 = "files/chr/nar/0010.seq";
  public static final String NAR_1000 = "files/chr/nar/1000.seq";
  public static final String NEJ_0000 = "files/chr/nej/0000.seq";
  public static final String NEJ_0010 = "files/chr/nej/0010.seq";
  public static final String NEJ_1000 = "files/chr/nej/1000.seq";
  public static final String OBO_0000 = "files/chr/obo/0000.seq";
  public static final String OBO_0010 = "files/chr/obo/0010.seq";
  public static final String OBO_1000 = "files/chr/obo/1000.seq";
  public static final String ORO_0000 = "files/chr/oro/0000.seq";
  public static final String ORO_0010 = "files/chr/oro/0010.seq";
  public static final String ORO_1000 = "files/chr/oro/1000.seq";
  public static final String SA2_0000 = "files/chr/sa2/0000.seq";
  public static final String SA2_0010 = "files/chr/sa2/0010.seq";
  public static final String SA2_1000 = "files/chr/sa2/1000.seq";
  public static final String SAK_0000 = "files/chr/sak/0000.seq";
  public static final String SAK_0010 = "files/chr/sak/0010.seq";
  public static final String SAK_1000 = "files/chr/sak/1000.seq";
  public static final String SAR_0000 = "files/chr/sar/0000.seq";
  public static final String SAR_0010 = "files/chr/sar/0010.seq";
  public static final String SAR_1000 = "files/chr/sar/1000.seq";
  public static final String SAS_0000 = "files/chr/sas/0000.seq";
  public static final String SAS_0010 = "files/chr/sas/0010.seq";
  public static final String SAS_1000 = "files/chr/sas/1000.seq";
  public static final String SIK_0000 = "files/chr/sik/0000.seq";
  public static final String SIK_0010 = "files/chr/sik/0010.seq";
  public static final String SIK_1000 = "files/chr/sik/1000.seq";
  public static final String SIN_0000 = "files/chr/sin/0000.seq";
  public static final String SIN_0010 = "files/chr/sin/0010.seq";
  public static final String SIN_1000 = "files/chr/sin/1000.seq";
  public static final String SKO_0000 = "files/chr/sko/0000.seq";
  public static final String SKO_0010 = "files/chr/sko/0010.seq";
  public static final String SKO_1000 = "files/chr/sko/1000.seq";
  public static final String TA2_0000 = "files/chr/ta2/0000.seq";
  public static final String TA2_0010 = "files/chr/ta2/0010.seq";
  public static final String TA2_1000 = "files/chr/ta2/1000.seq";
  public static final String TAY_0000 = "files/chr/tay/0000.seq";
  public static final String TAY_0010 = "files/chr/tay/0010.seq";
  public static final String TAY_1000 = "files/chr/tay/1000.seq";
  public static final String TEM_0000 = "files/chr/tem/0000.seq";
  public static final String TEM_0010 = "files/chr/tem/0010.seq";
  public static final String TEM_1000 = "files/chr/tem/1000.seq";
  public static final String TEN_0000 = "files/chr/ten/0000.seq";
  public static final String TEN_0010 = "files/chr/ten/0010.seq";
  public static final String TEN_1000 = "files/chr/ten/1000.seq";
  public static final String TSU_0000 = "files/chr/tsu/0000.seq";
  public static final String TSU_0010 = "files/chr/tsu/0010.seq";
  public static final String TSU_1000 = "files/chr/tsu/1000.seq";
  public static final String ZAB_0000 = "files/chr/zab/0000.seq";
  public static final String ZAB_0010 = "files/chr/zab/0010.seq";
  public static final String ZAB_1000 = "files/chr/zab/1000.seq";
  public static final String F_CAMERA = "files/furu/f_camera.seq";
  public static final String CAMERA_00 = "files/game/camera00.seq";
  public static final String CAMERA_01 = "files/game/camera01.seq";
  public static final String GAME_00 = "files/game/game00.seq";
  public static final String M_ENTRY = "files/game/m_entry.seq";
  public static final String M_VS = "files/game/m_vs.seq";
  public static final String PLAYER_00 = "files/game/player00.seq";
  public static final String BUTTON = "files/kuro/button.seq";
  public static final String LOADING = "files/kuro/loading.seq";
  public static final String T_MODE = "files/kuro/tmode.seq";
  public static final String CHARSEL_4 = "files/maki/charsel4.seq";
  public static final String CHARSEL = "files/maki/char_sel.seq";
  public static final String M_GAL = "files/maki/m_gal.seq";
  public static final String M_NFILE = "files/maki/m_nfile.seq";
  public static final String M_NSIKI = "files/maki/m_nsiki.seq";
  public static final String M_SNDPLR = "files/maki/m_sndplr.seq";
  public static final String M_TITLE = "files/maki/m_title.seq";
  public static final String M_VIEWER = "files/maki/m_viewer.seq";
  public static final String STG_001_0000 = "files/stg/001/0000.seq";
  public static final String STG_001_0100 = "files/stg/001/0100.seq";
  public static final String STG_002_0000 = "files/stg/002/0000.seq";
  public static final String STG_002_0100 = "files/stg/002/0100.seq";
  public static final String STG_003_0000 = "files/stg/003/0000.seq";
  public static final String STG_003_0100 = "files/stg/003/0100.seq";
  public static final String STG_004_0000 = "files/stg/004/0000.seq";
  public static final String STG_004_0100 = "files/stg/004/0100.seq";
  public static final String STG_005_0000 = "files/stg/005/0000.seq";
  public static final String STG_005_0100 = "files/stg/005/0100.seq";
  public static final String STG_006_0000 = "files/stg/006/0000.seq";
  public static final String STG_006_0100 = "files/stg/006/0100.seq";
  public static final String STG_007_0000 = "files/stg/007/0000.seq";
  public static final String STG_007_0100 = "files/stg/007/0100.seq";
  public static final String STG_008_0000 = "files/stg/008/0000.seq";
  public static final String STG_008_0100 = "files/stg/008/0100.seq";
  public static final String STG_009_0000 = "files/stg/009/0000.seq";
  public static final String STG_009_0100 = "files/stg/009/0100.seq";
  public static final String STG_010_0000 = "files/stg/010/0000.seq";
  public static final String STG_010_0100 = "files/stg/010/0100.seq";
  public static final String STG_011_0000 = "files/stg/011/0000.seq";
  public static final String STG_011_0100 = "files/stg/011/0100.seq";
  public static final String STG_012_0000 = "files/stg/012/0000.seq";
  public static final String STG_012_0100 = "files/stg/012/0100.seq";
  public static final String STG_013_0000 = "files/stg/013/0000.seq";
  public static final String STG_013_0100 = "files/stg/013/0100.seq";
  public static final String STG_014_0000 = "files/stg/014/0000.seq";
  public static final String STG_014_0100 = "files/stg/014/0100.seq";
  public static final String STG_015_0000 = "files/stg/015/0000.seq";
  public static final String STG_015_0100 = "files/stg/015/0100.seq";
  public static final String STG_016_0000 = "files/stg/016/0000.seq";
  public static final String STG_016_0100 = "files/stg/016/0100.seq";
  public static final String STG_017_0000 = "files/stg/017/0000.seq";
  public static final String STG_017_0100 = "files/stg/017/0100.seq";
  public static final String STG_019_0000 = "files/stg/019/0000.seq";
  public static final String STG_019_0100 = "files/stg/019/0100.seq";
  public static final String STG_020_0000 = "files/stg/020/0000.seq";
  public static final String STG_020_0100 = "files/stg/020/0100.seq";
  public static final String STG_021_0000 = "files/stg/021/0000.seq";
  public static final String STG_021_0100 = "files/stg/021/0100.seq";
  public static final String STG_022_0000 = "files/stg/022/0000.seq";
  public static final String STG_022_0100 = "files/stg/022/0100.seq";
  public static final String STG_023_0000 = "files/stg/023/0000.seq";
  public static final String STG_023_0100 = "files/stg/023/0100.seq";
  public static final String STG_024_0000 = "files/stg/024/0000.seq";
  public static final String STG_024_0100 = "files/stg/024/0100.seq";
  public static final String STG_025_0000 = "files/stg/025/0000.seq";
  public static final String STG_025_0100 = "files/stg/025/0100.seq";
  public static final String STG_026_0000 = "files/stg/026/0000.seq";
  public static final String STG_026_0100 = "files/stg/026/0100.seq";
  public static final String STG_027_0000 = "files/stg/027/0000.seq";
  public static final String STG_027_0100 = "files/stg/027/0100.seq";
  public static final String STG_028_0000 = "files/stg/028/0000.seq";
  public static final String STG_028_0100 = "files/stg/028/0100.seq";
  public static final String STG_029_0000 = "files/stg/029/0000.seq";
  public static final String STG_029_0100 = "files/stg/029/0100.seq";
  public static final String STG_030_0000 = "files/stg/030/0000.seq";
  public static final String STG_030_0100 = "files/stg/030/0100.seq";
  public static final String STG_031_0000 = "files/stg/031/0000.seq";
  public static final String STG_031_0100 = "files/stg/031/0100.seq";
  public static final String STG_032_0000 = "files/stg/032/0000.seq";
  public static final String STG_032_0100 = "files/stg/032/0100.seq";
  public static final String S_00 = "files/story/s00.seq";
  public static final String S_01 = "files/story/s01.seq";
  public static final String S_02 = "files/story/s02.seq";
  public static final String S_03 = "files/story/s03.seq";
  public static final String S_04 = "files/story/s04.seq";
  public static final String S_05 = "files/story/s05.seq";
  public static final String S_06 = "files/story/s06.seq";
  public static final String S_07 = "files/story/s07.seq";
  public static final String S_08 = "files/story/s08.seq";
  public static final String S_09 = "files/story/s09.seq";
  public static final String S_0E = "files/story/s0e.seq";
  public static final String S_10 = "files/story/s10.seq";
  public static final String S_11 = "files/story/s11.seq";
  public static final String S_12 = "files/story/s12.seq";
  public static final String S_13 = "files/story/s13.seq";
  public static final String S_14 = "files/story/s14.seq";
  public static final String S_15 = "files/story/s15.seq";
  public static final String S_16 = "files/story/s16.seq";
  public static final String S_17 = "files/story/s17.seq";
  public static final String S_18 = "files/story/s18.seq";
  public static final String S_19 = "files/story/s19.seq";
  public static final String S_1E = "files/story/s1e.seq";
  public static final String S_20 = "files/story/s20.seq";
  public static final String S_21 = "files/story/s21.seq";
  public static final String S_22 = "files/story/s22.seq";
  public static final String S_23 = "files/story/s23.seq";
  public static final String S_24 = "files/story/s24.seq";

  public static final List<String> ALL = List
      .of(ANK_0000, ANK_0010, ANK_1000, BOU_0000, BOU_0010, BOU_1000, CHO_0000, CHO_0010, CHO_1000,
          CMN_1000, DOG_0000, DOG_0010, DOG_1000, GAI_0000, GAI_0010, GAI_1000, GAR_0000, GAR_0010,
          GAR_1000, HAK_0000, HAK_0010, HAK_1000, HI2_0000, HI2_0010, HI2_1000, HIN_0000, HIN_0010,
          HIN_1000, INO_0000, INO_0010, INO_1000, IRU_0000, IRU_0010, IRU_1000, ITA_0000, ITA_0010,
          ITA_1000, JIR_0000, JIR_0010, JIR_1000, KAB_0000, KAB_0010, KAB_1000, KAK_0000, KAK_0010,
          KAK_1000, KAN_0000, KAN_0010, KAN_1000, KAR_0000, KAR_0010, KAR_1000, KIB_0000, KIB_0010,
          KIB_1000, KID_0000, KID_0010, KID_1000, KIM_0000, KIM_0010, KIM_1000, KIS_0000, KIS_0010,
          KIS_1000, LOC_0000, LOC_0010, LOC_1000, MIZ_0000, MIZ_0010, MIZ_1000, NA9_0000, NA9_0010,
          NA9_1000, NAR_0000, NAR_0010, NAR_1000, NEJ_0000, NEJ_0010, NEJ_1000, OBO_0000, OBO_0010,
          OBO_1000, ORO_0000, ORO_0010, ORO_1000, SA2_0000, SA2_0010, SA2_1000, SAK_0000, SAK_0010,
          SAK_1000, SAR_0000, SAR_0010, SAR_1000, SAS_0000, SAS_0010, SAS_1000, SIK_0000, SIK_0010,
          SIK_1000, SIN_0000, SIN_0010, SIN_1000, SKO_0000, SKO_0010, SKO_1000, TA2_0000, TA2_0010,
          TA2_1000, TAY_0000, TAY_0010, TAY_1000, TEM_0000, TEM_0010, TEM_1000, TEN_0000, TEN_0010,
          TEN_1000, TSU_0000, TSU_0010, TSU_1000, ZAB_0000, ZAB_0010, ZAB_1000, F_CAMERA, CAMERA_00,
          CAMERA_01, GAME_00, M_ENTRY, M_VS, PLAYER_00, BUTTON, LOADING, T_MODE, CHARSEL_4, CHARSEL,
          M_GAL, M_NFILE, M_NSIKI, M_SNDPLR, M_TITLE, M_VIEWER, STG_001_0000, STG_001_0100,
          STG_002_0000, STG_002_0100, STG_003_0000, STG_003_0100, STG_004_0000, STG_004_0100,
          STG_005_0000, STG_005_0100, STG_006_0000, STG_006_0100, STG_007_0000, STG_007_0100,
          STG_008_0000, STG_008_0100, STG_009_0000, STG_009_0100, STG_010_0000, STG_010_0100,
          STG_011_0000, STG_011_0100, STG_012_0000, STG_012_0100, STG_013_0000, STG_013_0100,
          STG_014_0000, STG_014_0100, STG_015_0000, STG_015_0100, STG_016_0000, STG_016_0100,
          STG_017_0000, STG_017_0100, STG_019_0000, STG_019_0100, STG_020_0000, STG_020_0100,
          STG_021_0000, STG_021_0100, STG_022_0000, STG_022_0100, STG_023_0000, STG_023_0100,
          STG_024_0000, STG_024_0100, STG_025_0000, STG_025_0100, STG_026_0000, STG_026_0100,
          STG_027_0000, STG_027_0100, STG_028_0000, STG_028_0100, STG_029_0000, STG_029_0100,
          STG_030_0000, STG_030_0100, STG_031_0000, STG_031_0100, STG_032_0000, STG_032_0100, S_00,
          S_01, S_02, S_03, S_04, S_05, S_06, S_07, S_08, S_09, S_0E, S_10, S_11, S_12, S_13, S_14,
          S_15, S_16, S_17, S_18, S_19, S_1E, S_20, S_21, S_22, S_23, S_24);

  /**
   * Tries to find the unique path name of the SEQ file from the given SEQ path. Will return an
   * empty optional in such cases as the SEQ file being renamed.
   *
   * @param seqPath The path to the SEQ file.
   * @return An optional unique file name of the SEQ file.
   */
  public static Optional<String> getFileName(Path seqPath) {
    String fixedPath = seqPath.toString().replace("\\", "/");
    for (String path : ALL) {
      if (fixedPath.endsWith(path)) {
        return Optional.of(path);
      }
    }
    return Optional.empty();
  }

  /**
   * Request an SEQ file name from the user.
   *
   * @return An optional SEQ file name.
   */
  public static Optional<String> requestFileName() {
    ChoiceDialog<String> dialog = new ChoiceDialog<>(Seqs.ALL.get(0), Seqs.ALL);
    dialog.setTitle("Select SEQ File");
    dialog.setHeaderText("Please select which SEQ file this is.");
    dialog.setContentText("File:");
    return dialog.showAndWait();
  }
}
