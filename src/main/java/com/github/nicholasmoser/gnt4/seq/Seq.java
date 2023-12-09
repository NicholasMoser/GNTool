package com.github.nicholasmoser.gnt4.seq;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.Set;

public class Seq {

  // The size of the largest SEQ file, m_title.seq
  public static final int MAX_SIZE = 0x492A0;

  // SEQ File Sections
  public static final String CHR_TBL = "chr_tbl";
  public static final String CHR_ACT = "chr_act";
  public static final String CHR_CAM = "chr_cam";
  public static final String CHR_SUB02 = "chr_sub02";
  public static final String CHR_MODEL = "chr_model";
  public static final String CHR_MOT = "chr_mot";
  public static final String CHR_HIRA = "chr_hira";
  public static final String CHR_SEL = "chr_sel";
  public static final String CHR_SHOT = "chr_shot";
  public static final String CHR_FACE = "chr_face";
  public static final String CHR_DATA = "chr_data";
  public static final String CHR_VISUAL2D = "chr_visual2d";
  public static final String SEQ_EXT = "seq_ext";

  // Notable Action IDs
  public static final int ACTION_5B = 0x0A0;
  public static final int ACTION_6B = 0x0A1;
  public static final int ACTION_4B = 0x0A2;
  public static final int ACTION_2B = 0x0A3;
  public static final int ACTION_RB = 0x0A5;
  public static final int ACTION_5A = 0x0B0;
  public static final int ACTION_6A = 0x0B1;
  public static final int ACTION_4A = 0x0B2;
  public static final int ACTION_2A = 0x0B3;
  public static final int ACTION_RA = 0x0B5;
  public static final int ACTION_RKNJ_GROUND = 0x0C0;
  public static final int ACTION_RKNJ_AIR = 0x0C1;
  public static final int ACTION_LKNJ = 0x0C2;
  public static final int ACTION_ZKNJ_INCOMING = 0x0C3;
  public static final int ACTION_5Z_OUTGOING = 0x0C6;
  public static final int ACTION_4Z_INCOMING = 0x0C7;
  public static final int ACTION_5Z_INCOMING = 0x0C8;
  public static final int ACTION_ZKNJ_OUTGOING = 0x0CB;
  public static final int ACTION_4Z_OUTGOING = 0x0CF;
  public static final int ACTION_JB = 0x0E0;
  public static final int ACTION_JA = 0x0E1;
  public static final int ACTION_8B = 0x0E2;
  public static final int ACTION_8A = 0x0E3;
  public static final int ACTION_5X = 0x121;
  public static final int ACTION_2X = 0x122;
  public static final int ACTION_COMBO_START = 0x130;
  public static final int ACTION_GROUND_THROW = 0x190;
  public static final int ACTION_BACK_GROUND_THROW = 0x191;
  public static final int ACTION_AIR_THROW = 0x192;
  public static final int ACTION_ACTIVATED_X = 0x193;

  public static final int PROGRESS_THROUGH_FRAMES_UNTIL_END = 0x13060020;
  public static final String PROGRESS_THROUGH_FRAMES_UNTIL_END_DESCRIPTION =
      "Single command, no value";

  public static final int ANIMATION_FLAG_ID = 0x0402023F;
  public static final String ANIMATION_FLAG_DESCRIPTION = "Animation Flag";

  public static final int X_MOVE_METER_REQ_ID = 0x227F2000;
  public static final String X_MOVE_METER_REQ_DESCRIPTION = "Meter Requirement for X Moves";

  public static final int HORIZ_MOBILITY_JUMP_MOVES_ID = 0x2414010B;
  public static final String HORIZ_MOBILITY_JUMP_MOVES_DESCRIPTION =
      "Horizontal Mobility for Jump Moves";

  public static final int VERT_MOBILITY_JUMP_MOVES_ID = 0x2414020B;
  public static final String VERT_MOBILITY_JUMP_MOVES_DESCRIPTION =
      "Vertical Mobility for Jump Moves";

  public static final int PROJ_DMG_STUN_GUARD_ID = 0x47040000;
  public static final String PROJ_DMG_STUN_GUARD_DESCRIPTION =
      "Projectile damage, stun, guard damage, etc";

  public static final int SYS_SOUND_EFFECT_ID = 0x24170000;
  public static final String SYS_SOUND_EFFECT_DESCRIPTION = "System Sound Effect";

  public static final int GFX_ID = 0x2A002626;
  public static final String GFX_DESCRPITION = "Graphics";

  public static final int CHR_SOUND_EFFECT_ID = 0x24170B00;
  public static final String CHR_SOUND_EFFECT_DESCRIPTION = "Character Sound Effect";

  public static final int CHR_ACT_ID = 0x013C0000;
  public static final String CHR_ACT_DESCRIPION = "Character Action";

  public static final int HITBOX_ACTIVE_FRAMES_ID = 0x21070026;
  public static final String HITBOX_ACTIVE_FRAMES_DESCRIPTION = "Hitbox Active Frames";

  public static final int HITBOX_LOC_SIZE_ID = 0x21040026;
  public static final String HITBOX_LOC_SIZE_DESCRIPTION = "Hitbox Locations and Sizes";

  public static final int POW_DMG_GRD_ID = 0x21050026;
  public static final String POW_DMG_GRD_DESCRIPTION = "POW, DMG, and GRD of a move";

  public static final int ANG_DIR_ID = 0x21060026;
  public static final String ANG_DIR_DESCRIPTION = "ANG and DIR of a move";

  // Synchronous timer will literally execute for the given amount.
  // e.g. the hex 2011263F_00000009_20120026 just no-ops in a loop for 9 frames.
  public static final int SYNCH_TIMER_BEGIN_ID = 0x2011263F;
  public static final String SYNCH_TIMER_BEGIN_DESCRIPTION = "Synchronous Timer Begin";

  public static final int SYNCH_TIMER_END_ID = 0x20120026;
  public static final String SYNCH_TIMER_END_DESCRIPTION = "Synchronous Timer End";

  public static final int PROJECTILE_INFO_ID = 0x47000026;
  public static final String PROJECTILE_INFO_DESCRIPTION = "Projectile Info";

  // Instructions that End the Action
  // 0x00000000_xxxxxxxx
  // 0x013C0000_00000000
  // 0x01320000 00012880 (Team super animation)
  // 0x01320000 0000A2C0
  // 0x01320000 0001D558 (Enter fall animation)
  // 0x01320000 0001BAC4 (Also fall animation)
  // 0x01320000 0001ACCC (Enter and attack in 3-man cell)

  public static final BiMap<Integer, String> MF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "GUARD")
          .put(0x00000002, "MUTEKI")
          .put(0x00000004, "BUTT")
          .put(0x00000008, "DEBUG")
          .put(0x00000010, "DEKA")
          .put(0x00000020, "TIBI")
          .put(0x00000040, "UDE")
          .put(0x00000080, "TYAKURA")
          .put(0x00000100, "TYAKURAREC")
          .put(0x00000200, "09")
          .put(0x00000400, "10")
          .put(0x00000800, "11")
          .put(0x00001000, "12")
          .put(0x00002000, "13")
          .put(0x00004000, "14")
          .put(0x00008000, "15")
          .put(0x00010000, "16")
          .put(0x00020000, "17")
          .put(0x00040000, "18")
          .put(0x00080000, "19")
          .put(0x00100000, "20")
          .put(0x00200000, "21")
          .put(0x00400000, "22")
          .put(0x00800000, "23")
          .put(0x01000000, "24")
          .put(0x02000000, "25")
          .put(0x04000000, "26")
          .put(0x08000000, "27")
          .put(0x10000000, "28")
          .put(0x20000000, "29")
          .put(0x40000000, "30")
          .put(0x80000000, "31")
          .build();

  public static final Map<String, Integer> MF_FLAGS_GET = MF_FLAGS.inverse();

  public static final BiMap<Integer, String> AF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "STAND")
          .put(0x00000002, "FORWARD")
          .put(0x00000004, "BACK")
          .put(0x00000008, "DASH")
          .put(0x00000010, "SIT")
          .put(0x00000020, "FUSE")
          .put(0x00000040, "UKEMI")
          .put(0x00000080, "KIRI")
          .put(0x00000100, "SPMDMG")
          .put(0x00000200, "SLANT")
          .put(0x00000400, "QUICK")
          .put(0x00000800, "FLOAT")
          .put(0x00001000, "JUMP")
          .put(0x00002000, "FALL")
          .put(0x00004000, "SMALL")
          .put(0x00008000, "DAMAGE")
          .put(0x00010000, "DOWNU")
          .put(0x00020000, "DOWNO")
          .put(0x00040000, "GETUP")
          .put(0x00080000, "TURN")
          .put(0x00100000, "TDOWN")
          .put(0x00200000, "CANTACT")
          .put(0x00400000, "SDEF")
          .put(0x00800000, "BDEF")
          .put(0x01000000, "BEAST")
          .put(0x02000000, "UKI")
          .put(0x04000000, "BUTT")
          .put(0x08000000, "NDOWN")
          .put(0x10000000, "DEF")
          .put(0x20000000, "TFAIL")
          .put(0x40000000, "THROW")
          .put(0x80000000, "ATTACK")
          .build();

  public static final Map<String, Integer> AF_FLAGS_GET = AF_FLAGS.inverse();

  public static final BiMap<Integer, String> PF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "DEFOK")
          .put(0x00000002, "BDEFOK")
          .put(0x00000004, "BGUARD")
          .put(0x00000008, "HIT")
          .put(0x00000010, "REVERSAL")
          .put(0x00000020, "GHIT")
          .put(0x00000040, "COMBO")
          .put(0x00000080, "FLOAT")
          .put(0x00000100, "FALL")
          .put(0x00000200, "ENEDMG")
          .put(0x00000400, "DIRNOGRD")
          .put(0x00000800, "ENEDWN")
          .put(0x00001000, "ENEATK")
          .put(0x00002000, "BDEF")
          .put(0x00004000, "THROWOK")
          .put(0x00008000, "BTNOMOVE")
          .put(0x00010000, "NECKTURN")
          .put(0x00020000, "ABSTURN")
          .put(0x00040000, "AIR")
          .put(0x00080000, "RINGOUT")
          .put(0x00100000, "TURN")
          .put(0x00200000, "ZOMBIE")
          .put(0x00400000, "BACK")
          .put(0x00800000, "BODY")
          .put(0x01000000, "M_KABE")
          .put(0x02000000, "GUARD")
          .put(0x04000000, "DAMAGE")
          .put(0x08000000, "ABSTURNR")
          .put(0x10000000, "NORMAL")
          .put(0x20000000, "DMG")
          .put(0x40000000, "DEF")
          .put(0x80000000, "OUT")
          .build();

  public static final Map<String, Integer> PF_FLAGS_GET = PF_FLAGS.inverse();

  public static final BiMap<Integer, String> NF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "KAMAE")
          .put(0x00000002, "DISP")
          .put(0x00000004, "TDMG")
          .put(0x00000008, "JUMP2")
          .put(0x00000010, "LEVERDIR")
          .put(0x00000020, "GETUP")
          .put(0x00000040, "HITEFT")
          .put(0x00000080, "NFOG")
          .put(0x00000100, "TAKEON")
          .put(0x00000200, "FOG")
          .put(0x00000400, "BDRIVESLEEP")
          .put(0x00000800, "JUMP")
          .put(0x00001000, "FALL")
          .put(0x00002000, "JSPD")
          .put(0x00004000, "SHOTDEF")
          .put(0x00008000, "MOVE")
          .put(0x00010000, "ATTACK")
          .put(0x00020000, "BUTTON")
          .put(0x00040000, "COMBO")
          .put(0x00080000, "DISP_N")
          .put(0x00100000, "KABEHIT")
          .put(0x00200000, "BODYTOUCH")
          .put(0x00400000, "AGUARD")
          .put(0x00800000, "DAMAGE")
          .put(0x01000000, "GUARD")
          .put(0x02000000, "AUTODIR")
          .put(0x04000000, "ENEAUTO")
          .put(0x08000000, "NJPTURN")
          .put(0x10000000, "RINGOUT")
          .put(0x20000000, "KABE")
          .put(0x40000000, "TDOWN")
          .put(0x80000000, "LEVER")
          .build();

  public static final Map<String, Integer> NF_FLAGS_GET = NF_FLAGS.inverse();

  public static final Map<Integer, String> N2F_FLAGS = new ImmutableMap.Builder<Integer, String>()
          .put(0x00000001, "KAMAE")
          .put(0x00000002, "KAWARIMI")
          .put(0x00000004, "NAGENUKE")
          .put(0x00000008, "PUSH")
          .put(0x00000010, "DEFEFT")
          .put(0x00000020, "HITSHOCK")
          .put(0x00000040, "DEFSHOCK")
          .put(0x00000080, "GAGE")
          .put(0x00000100, "TYAKURA")
          .put(0x00000200, "CAMERAOFF")
          .put(0x00000400, "CUTOFF")
          .put(0x00000800, "KABEHITSP")
          .put(0x00001000, "NULL")
          .put(0x00002000, "NULL")
          .put(0x00004000, "NULL")
          .put(0x00008000, "NULL")
          .put(0x00010000, "NULL")
          .put(0x00020000, "NULL")
          .put(0x00040000, "NULL")
          .put(0x00080000, "NULL")
          .put(0x00100000, "NULL")
          .put(0x00200000, "NULL")
          .put(0x00400000, "NULL")
          .put(0x00800000, "NULL")
          .put(0x01000000, "NULL")
          .put(0x02000000, "NULL")
          .put(0x04000000, "NULL")
          .put(0x08000000, "NULL")
          .put(0x10000000, "NULL")
          .put(0x20000000, "NULL")
          .put(0x40000000, "NULL")
          .put(0x80000000, "NULL")
          .build();

  public static final Map<String, Integer> N2F_FLAGS_GET = new ImmutableMap.Builder<String, Integer>()
          .put("KAMAE", 0x00000001)
          .put("KAWARIMI", 0x00000002)
          .put("NAGENUKE", 0x00000004)
          .put("PUSH", 0x00000008)
          .put("DEFEFT", 0x00000010)
          .put("HITSHOCK", 0x00000020)
          .put("DEFSHOCK", 0x00000040)
          .put("GAGE", 0x00000080)
          .put("TYAKURA", 0x00000100)
          .put("CAMERAOFF", 0x00000200)
          .put("CUTOFF", 0x00000400)
          .put("KABEHITSP", 0x00000800)
          .build();

  public static final BiMap<Integer, String> KF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "REPLAY")
          .put(0x00000002, "BDRIVE")
          .put(0x00000004, "SHOT")
          .put(0x00000008, "POW_W")
          .put(0x00000010, "POW_M")
          .put(0x00000020, "POW_S")
          .put(0x00000040, "LOW")
          .put(0x00000080, "MIDDLE")
          .put(0x00000100, "HIGH")
          .put(0x00000200, "PUNCH")
          .put(0x00000400, "KICK")
          .put(0x00000800, "THROW")
          .put(0x00001000, "OIUCHI")
          .put(0x00002000, "SPECIAL")
          .put(0x00004000, "NOGUARD")
          .put(0x00008000, "TDOWN")
          .put(0x00010000, "SPTATA")
          .put(0x00020000, "BREAK")
          .put(0x00040000, "COMBO")
          .put(0x00080000, "DOWN")
          .put(0x00100000, "YORO")
          .put(0x00200000, "BUTT")
          .put(0x00400000, "UKI")
          .put(0x00800000, "FURI")
          .put(0x01000000, "KORO")
          .put(0x02000000, "REACH_L")
          .put(0x04000000, "TATA")
          .put(0x08000000, "NOSPEEP")
          .put(0x10000000, "BEAST")
          .put(0x20000000, "FREEZE")
          .put(0x40000000, "CANCEL")
          .put(0x80000000, "ATKCAN")
          .build();

  public static final Map<String, Integer> KF_FLAGS_GET = KF_FLAGS.inverse();

  public static final Map<Integer, String> K2F_FLAGS = new ImmutableMap.Builder<Integer, String>()
          .put(0x00000001, "YORO2")
          .put(0x00000002, "HIKI")
          .put(0x00000004, "HIKI2")
          .put(0x00000008, "MISSION")
          .put(0x00000010, "NATEMI")
          .put(0x00000020, "SUPERARMOR")
          .put(0x00000040, "MOTO2")
          .put(0x00000080, "ATKALLCAN")
          .put(0x00000100, "TOJI")
          .put(0x00000200, "HASA")
          .put(0x00000400, "SHAVE")
          .put(0x00000800, "NEMU")
          .put(0x00001000, "WING")
          .put(0x00002000, "NULL")
          .put(0x00004000, "NULL")
          .put(0x00008000, "NULL")
          .put(0x00010000, "NULL")
          .put(0x00020000, "NULL")
          .put(0x00040000, "NULL")
          .put(0x00080000, "NULL")
          .put(0x00100000, "NULL")
          .put(0x00200000, "NULL")
          .put(0x00400000, "NULL")
          .put(0x00800000, "NULL")
          .put(0x01000000, "NULL")
          .put(0x02000000, "NULL")
          .put(0x04000000, "NULL")
          .put(0x08000000, "NULL")
          .put(0x10000000, "NULL")
          .put(0x20000000, "NULL")
          .put(0x40000000, "NULL")
          .put(0x80000000, "NULL")
          .build();

  public static final Map<String, Integer> K2F_FLAGS_GET = new ImmutableMap.Builder<String, Integer>()
          .put("YORO2", 0x00000001)
          .put("HIKI", 0x00000002)
          .put("HIKI2", 0x00000004)
          .put("MISSION", 0x00000008)
          .put("NATEMI", 0x00000010)
          .put("SUPERARMOR", 0x00000020)
          .put("MOTO2", 0x00000040)
          .put("ATKALLCAN", 0x00000080)
          .put("TOJI", 0x00000100)
          .put("HASA", 0x00000200)
          .put("SHAVE", 0x00000400)
          .put("NEMU", 0x00000800)
          .put("WING", 0x00001000)
          .build();

  public static final BiMap<Integer, String> DF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "F")
          .put(0x00000002, "B")
          .put(0x00000004, "R")
          .put(0x00000008, "L")
          .put(0x00000010, "W")
          .put(0x00000020, "M")
          .put(0x00000040, "S")
          .put(0x00000080, "SPECIAL")
          .put(0x00000100, "DOWN")
          .put(0x00000200, "YORO")
          .put(0x00000400, "BUTT")
          .put(0x00000800, "UKI")
          .put(0x00001000, "FURI")
          .put(0x00002000, "KORO")
          .put(0x00004000, "TATA")
          .put(0x00008000, "NODIS")
          .put(0x00010000, "A_LOW")
          .put(0x00020000, "A_MIDDLE")
          .put(0x00040000, "A_HIGH")
          .put(0x00080000, "BREAK")
          .put(0x00100000, "NBREAK")
          .put(0x00200000, "OIUCHI")
          .put(0x00400000, "TESCAPE")
          .put(0x00800000, "MEKURI")
          .put(0x01000000, "BDRIVE")
          .put(0x02000000, "COUNTER_N")
          .put(0x04000000, "SHOT")
          .put(0x08000000, "COUNTER")
          .put(0x10000000, "HITCNT")
          .put(0x20000000, "HITCNT2")
          .put(0x40000000, "OFFSET")
          .put(0x80000000, "SPMDMG")
          .build();

  public static final Map<String, Integer> DF_FLAGS_GET = DF_FLAGS.inverse();

  public static final Map<Integer, String> D2F_FLAGS = new ImmutableMap.Builder<Integer, String>()
          .put(0x00000001, "MATO")
          .put(0x00000002, "HIKI")
          .put(0x00000004, "HIKI2")
          .put(0x00000008, "MISSION")
          .put(0x00000010, "BDGUARD")
          .put(0x00000020, "MOTO2")
          .put(0x00000040, "TOJI")
          .put(0x00000080, "HASA")
          .put(0x00000100, "NEMU")
          .put(0x00000200, "NULL")
          .put(0x00000400, "NULL")
          .put(0x00000800, "NULL")
          .put(0x00001000, "NULL")
          .put(0x00002000, "NULL")
          .put(0x00004000, "NULL")
          .put(0x00008000, "NULL")
          .put(0x00010000, "NULL")
          .put(0x00020000, "NULL")
          .put(0x00040000, "NULL")
          .put(0x00080000, "NULL")
          .put(0x00100000, "NULL")
          .put(0x00200000, "NULL")
          .put(0x00400000, "NULL")
          .put(0x00800000, "NULL")
          .put(0x01000000, "NULL")
          .put(0x02000000, "NULL")
          .put(0x04000000, "NULL")
          .put(0x08000000, "NULL")
          .put(0x10000000, "NULL")
          .put(0x20000000, "NULL")
          .put(0x40000000, "NULL")
          .put(0x80000000, "NULL")
          .build();

  public static final Map<String, Integer> D2F_FLAGS_GET = new ImmutableMap.Builder<String, Integer>()
          .put("MATO", 0x00000001)
          .put("HIKI", 0x00000002)
          .put("HIKI2", 0x00000004)
          .put("MISSION", 0x00000008)
          .put("BDGUARD", 0x00000010)
          .put("MOTO2", 0x00000020)
          .put("TOJI", 0x00000040)
          .put("HASA", 0x00000080)
          .put("NEMU", 0x00000100)
          .build();

  public static final BiMap<Integer, String> EF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "KABE")
          .put(0x00000002, "KABEN")
          .put(0x00000004, "KABEC")
          .put(0x00000008, "PAUSE")
          .put(0x00000010, "COMNUKE")
          .put(0x00000020, "RESCAPE")
          .put(0x00000040, "HOKAN")
          .put(0x00000080, "WARPHIP")
          .put(0x00000100, "TDOWNFAIL")
          .put(0x00000200, "NULL")
          .put(0x00000400, "BKOUT")
          .put(0x00000800, "ATK")
          .put(0x00001000, "SPOSE")
          .put(0x00002000, "LEVERREV")
          .put(0x00004000, "ATKCAN")
          .put(0x00008000, "OFFBEAST")
          .put(0x00010000, "HOPUP")
          .put(0x00020000, "WARP")
          .put(0x00040000, "FIX")
          .put(0x00080000, "TAKEON")
          .put(0x00100000, "RINGOUT")
          .put(0x00200000, "TFAIL")
          .put(0x00400000, "THROW")
          .put(0x00800000, "TDOWN")
          .put(0x01000000, "COMBO0")
          .put(0x02000000, "COMBO1")
          .put(0x04000000, "TESCAPE")
          .put(0x08000000, "BDRIVE")
          .put(0x10000000, "FLAG0")
          .put(0x20000000, "FLAG1")
          .put(0x40000000, "FLAG2")
          .put(0x80000000, "31")
          .build();

  public static final Map<String, Integer> EF_FLAGS_GET = EF_FLAGS.inverse();

  public static final BiMap<Integer, String> RF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "COLOR")
          .put(0x00000002, "TYAKURASUB")
          .put(0x00000004, "HAZIKI")
          .put(0x00000008, "HAZIKIR")
          .put(0x00000010, "ALLGUARD")
          .put(0x00000020, "EFTREV")
          .put(0x00000040, "TARGETDIRA")
          .put(0x00000080, "GCANCELCHK")
          .put(0x00000100, "GCANCELOK")
          .put(0x00000200, "GCANCEL")
          .put(0x00000400, "GATTACK")
          .put(0x00000800, "NKAWARIMI")
          .put(0x00001000, "AUTOMOTION")
          .put(0x00002000, "EVENT00")
          .put(0x00004000, "SHADOWOFF")
          .put(0x00008000, "NOBACK")
          .put(0x00010000, "NSOUSAI")
          .put(0x00020000, "TAG2SP")
          .put(0x00040000, "TAG3SP")
          .put(0x00080000, "VANISH")
          .put(0x00100000, "INTRUDE")
          .put(0x00200000, "NOSTIFF")
          .put(0x00400000, "MOTIONREG")
          .put(0x00800000, "BDRIVEDEFDMG")
          .put(0x01000000, "ATTACKOK")
          .put(0x02000000, "DEFAULTTEXREV")
          .put(0x04000000, "PARDIR")
          .put(0x08000000, "ATKCHANGE")
          .put(0x10000000, "KAWARIMI")
          .put(0x20000000, "ACTCAN")
          .put(0x40000000, "30")
          .put(0x80000000, "31")
          .build();

  public static final Map<String, Integer> RF_FLAGS_GET = RF_FLAGS.inverse();

  public static final BiMap<Integer, String> CF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "DMGOFF")
          .put(0x00000002, "TYAKURASUB")
          .put(0x00000004, "DISPOFF")
          .put(0x00000008, "TAKEONOFF")
          .put(0x00000010, "CHGNOATTACK")
          .put(0x00000020, "CHGNODMG")
          .put(0x00000040, "ANMCHG")
          .put(0x00000080, "COPYPFHIT")
          .put(0x00000100, "CLR")
          .put(0x00000200, "TYAKURAADD")
          .put(0x00000400, "PARENTMOVE")
          .put(0x00000800, "PINCH")
          .put(0x00001000, "CAMERAON")
          .put(0x00002000, "COMBOONLY")
          .put(0x00004000, "TARGETPARENT")
          .put(0x00008000, "NORESULT")
          .put(0x00010000, "TARGETPARENT2")
          .put(0x00020000, "PARDMGOFFCOPYTHROW")
          .put(0x00040000, "19")
          .put(0x00080000, "20")
          .put(0x00100000, "21")
          .put(0x00200000, "22")
          .put(0x00400000, "23")
          .put(0x00800000, "24")
          .put(0x01000000, "25")
          .put(0x02000000, "26")
          .put(0x04000000, "27")
          .put(0x08000000, "28")
          .put(0x10000000, "29")
          .put(0x20000000, "30")
          .put(0x40000000, "31")
          .put(0x80000000, "(empty)")
          .build();

  public static final Map<String, Integer> CF_FLAGS_GET = CF_FLAGS.inverse();

  public static final BiMap<Integer, String> SF_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "HIT")
          .put(0x00000002, "GHIT")
          .put(0x00000004, "DAMAGE")
          .put(0x00000008, "GUARD")
          .put(0x00000010, "DEFOK")
          .put(0x00000020, "CATCH")
          .put(0x00000040, "06")
          .put(0x00000080, "07")
          .put(0x00000100, "08")
          .put(0x00000200, "09")
          .put(0x00000400, "10")
          .put(0x00000800, "11")
          .put(0x00001000, "12")
          .put(0x00002000, "13")
          .put(0x00004000, "14")
          .put(0x00008000, "15")
          .put(0x00010000, "16")
          .put(0x00020000, "17")
          .put(0x00040000, "18")
          .put(0x00080000, "19")
          .put(0x00100000, "20")
          .put(0x00200000, "21")
          .put(0x00400000, "22")
          .put(0x00800000, "23")
          .put(0x01000000, "24")
          .put(0x02000000, "25")
          .put(0x04000000, "26")
          .put(0x08000000, "27")
          .put(0x10000000, "28")
          .put(0x20000000, "29")
          .put(0x40000000, "30")
          .put(0x80000000, "31")
          .build();

  public static final Map<String, Integer> SF_FLAGS_GET = SF_FLAGS.inverse();

  public static final BiMap<Integer, String> CHR_MOD_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
          .put(0x00000001, "Attack Boost Lv1")
          .put(0x00000002, "Attack Boost Lv2")
          .put(0x00000004, "Disable Chakra Gain")
          .put(0x00000008, "Auto-Recover Chakra")
          .put(0x00000010, "Special Jutsu Boost")
          .put(0x00000020, "Health Absorption")
          .put(0x00000040, "Reverse Directions")
          .put(0x00000080, "Health Boost Small")
          .put(0x00000100, "Health Boost Medium")
          .put(0x00000200, "Health Boost Large")
          .put(0x00000400, "Auto-Throw Escape")
          .put(0x00000800, "Auto-Ground Tech")
          .put(0x00001000, "Super Armor")
          .put(0x00002000, "Auto-Recover Health")
          .put(0x00004000, "Invincibility for 10 Seconds")
          .put(0x00008000, "Absolute Defense")
          .put(0x00010000, "Halve Chakra Consumption")
          .put(0x00020000, "Disable Ground Tech")
          .put(0x00040000, "Disable Substitution")
          .put(0x00080000, "Disable Sidestep")
          .put(0x00100000, "Disable B Button")
          .put(0x00200000, "Disable A Button")
          .put(0x00400000, "Disable X Button")
          .put(0x00800000, "Disable Y Button")
          .put(0x01000000, "Disable Throw Escape")
          .put(0x02000000, "Disable Chakra Use")
          .put(0x04000000, "Disable Jump")
          .put(0x08000000, "Disable Guard")
          .put(0x10000000, "Disable Projectiles")
          .put(0x20000000, "Health Drain")
          .put(0x40000000, "Halve Attack Power")
          .put(0x80000000, "Delete HP and Chakra Guard")
          .build();

  public static final Map<String, Integer> CHR_MOD_FLAGS_GET = CHR_MOD_FLAGS.inverse();

  public static BiMap<Integer, String> ACTION_DESCRIPTIONS;

  public static String getActionDescription(int actionId) {
    if (ACTION_DESCRIPTIONS == null) {
      ACTION_DESCRIPTIONS = initActionDescriptions();
    }
    String description = ACTION_DESCRIPTIONS.get(actionId);
    return description != null ? description : String.format("unknown_0x%X", actionId);
  }

  public static int getActionId(String actionDescription) {
    if (ACTION_DESCRIPTIONS == null) {
      ACTION_DESCRIPTIONS = initActionDescriptions();
    }
    actionDescription = actionDescription.replace(" (unused)", "");
    if (actionDescription.startsWith("unknown_")) {
      return Integer.decode(actionDescription.replace("unknown_", ""));
    }
    Integer actionId = ACTION_DESCRIPTIONS.inverse().get(actionDescription);
    if (actionId == null) {
      throw new IllegalArgumentException("Unable to find action for description: " + actionDescription);
    }
    return actionId;
  }

  public static Set<String> getActions() {
    if (ACTION_DESCRIPTIONS == null) {
      ACTION_DESCRIPTIONS = initActionDescriptions();
    }
    return ACTION_DESCRIPTIONS.values();
  }

  private static BiMap<Integer, String> initActionDescriptions() {
    return new ImmutableBiMap.Builder<Integer, String>()
    .put(0x001, "stand")
    .put(0x002, "walk_forward")
    .put(0x003, "walk_backward")
    .put(0x004, "running_start")
    .put(0x005, "running")
    .put(0x006, "running_end")
    .put(0x007, "back_dash")
    .put(0x013, "jumping_start")
    .put(0x014, "jumping_up")
    .put(0x015, "jumping_forward")
    .put(0x016, "jumping_backward")
    .put(0x017, "jumping_up_2") // duplicate?
    .put(0x018, "jumping_forward_2") // duplicate?
    .put(0x019, "jumping_backward_2") // duplicate?
    .put(0x01A, "weird_jump_1")
    .put(0x01B, "weird_jump_2")
    .put(0x01C, "weird_jump_3")
    .put(0x01D, "jumping_end")
    .put(0x01E, "sidestep_right") // towards back of the screen
    .put(0x01F, "sidestep_left") // towards the front of the screen
    .put(0x020, "sidestep_right_dashing") // towards back of the screen
    .put(0x021, "sidestep_left_dashing") // towards the front of the screen
    .put(0x025, "y-cancel")
    .put(0x026, "attacked_in_air")
    .put(0x028, "intro_animation_1") // Action after zoom in
    .put(0x029, "intro_animation_2") // Stand during zoom in
    .put(0x02A, "round_win_animation")
    .put(0x02B, "battle_win_animation_1")
    .put(0x02C, "battle_win_animation_2")
    .put(0x02E, "battle_lose_animation")
    .put(0x030, "block_soft") // e.g. by Naruto 5B
    .put(0x031, "block_medium") // e.g. by Naruto 6B
    .put(0x032, "block_low") // e.g. by Naruto 2B
    .put(0x033, "block_air")
    .put(0x034, "block_with_lift") // e.g. by Naruto 6A
    .put(0x037, "block_hard_slide") // e.g. by Naruto 4B
    .put(0x039, "hit")
    .put(0x03A, "hit_2")
    .put(0x03B, "hit_3")
    .put(0x03C, "hit_4")
    .put(0x03D, "hit_5")
    .put(0x03E, "hit_6")
    .put(0x03F, "hit_7")
    .put(0x040, "hit_high") // e.g. by Naruto 5B
    .put(0x041, "hit_medium") // e.g. by Naruto 5BB
    .put(0x042, "hit_low") // e.g. by Temari 2B
    .put(0x043, "hit_8")
    .put(0x044, "hit_9")
    .put(0x045, "hit_behind") // hit from behind e.g. by Naruto 5B
    .put(0x046, "hard_knockdown_land") // e.g. by Sasuke DB
    .put(0x047, "hard_knockdown")
    .put(0x048, "nemu") // Sleep, e.g. Kabuto 2X
    .put(0x049, "hard_knockdown_2")
    .put(0x04A, "land_on_ground_chest") // e.g. by Hinata 2B
    .put(0x04B, "hit_with_special") // lose all chakra
    .put(0x04C, "hiki2") // Drop into ground, e.g. by Shikamaru 2X
    .put(0x04D, "hiki") // Suck into ground, e.g. by Jiraya 2A
    .put(0x04E, "body_trap") // e.g. by Kidomaru 5A1C
    .put(0x04F, "hit_stagger") // e.g. by Naruto first charge of 5A
    .put(0x050, "hit_hard_front") // e.g. by Sasuke DB
    .put(0x051, "hit_hard_behind") // e.g. by Sasuke DB
    .put(0x052, "feet_trap") // e.g. by Kidomaru 2A
    .put(0x053, "hit_stagger_behind") // e.g. by OTK 5AA
    .put(0x054, "hit_towards_ground") // e.g. by Tayuya 8A
    .put(0x055, "hit_into_air") // e.g. by Sakura 2B sometimes
    .put(0x056, "hit_into_air_2") // e.g. by Sakura 2B sometimes
    .put(0x057, "hit_towards_ground_from_behind") // e.g. by Tayuya 8A
    .put(0x058, "hit_medium_into_air") // e.g. by Naruto 8B
    .put(0x059, "hit_into_air_3") // e.g. by Temari 4B
    .put(0x05A, "landing_from_soft_hit")
    .put(0x05B, "hit_soft_into_air") // e.g. by Naruto 6A
    .put(0x05C, "fall_to_knees_head_in_hands") // unused?
    .put(0x05D, "hard_knockdown_3")
    .put(0x05E, "furi") // turns you around, from BR
    .put(0x05F, "furi_2") // turns you around, from BR
    .put(0x060, "trip") // e.g. Hinata 2B
    .put(0x061, "hard_knockdown_4")
    .put(0x065, "guard_break")
    .put(0x066, "landing_back")
    .put(0x067, "laying_on_back")
    .put(0x068, "laying_on_chest")
    .put(0x069, "hit_on_ground") // e.g. CS2 6A OTG
    .put(0x06A, "hard_knockdown_5")
    .put(0x06B, "guard_break_2")
    .put(0x06C, "hard_knockdown_6")
    .put(0x06D, "hit_10")
    .put(0x06E, "hit_11")
    .put(0x06F, "hard_knockdown_7")
    .put(0x070, "hard_knockdown_8")
    .put(0x071, "stand_off_ground")
    .put(0x072, "roll_forward")
    .put(0x073, "roll_forward_2")
    .put(0x074, "roll_backward")
    .put(0x075, "roll_right")
    .put(0x076, "roll_left")
    .put(0x077, "get_up")
    .put(0x078, "get_up_roll_forward")
    .put(0x079, "get_up_roll_forward_2")
    .put(0x07A, "get_up_roll_backward")
    .put(0x07B, "get_up_roll_right")
    .put(0x07C, "get_up_roll_left")
    .put(0x07D, "throw_break")
    .put(0x07E, "throw_break_2")
    .put(0x07F, "throw_break_3")
    .put(0x080, "hit_hard")
    .put(0x081, "hit_hard_2")
    .put(0x082, "hit_hard_3")
    .put(0x083, "hit_hard_4")
    .put(0x084, "hard_knockdown_left")
    .put(0x085, "hard_knockdown_right")
    .put(0x086, "hit_12")
    .put(0x087, "hit_13")
    .put(0x088, "hard_knockdown_back")
    .put(0x089, "hard_knockdown_forward")
    .put(0x08C, "hit_hard_5")
    .put(0x08D, "tripping")
    .put(0x08E, "hard_knockdown_bounce") // e.g. by OTK jA
    .put(0x08F, "hard_knockdown_bounce_2") // e.g. by last hit of Sasuke 4BBB
    .put(0x090, "hard_hit_downward") // e.g. by Kabuto 8B
    .put(0x091, "stuck_in_air") // Can be hit out
    .put(0x092, "hard_knockdown_slow_bounce")
    .put(0x093, "hard_knockdown_slow_bounce_2") // e.g. by Kabuto 8B
    .put(0x095, "block_low_2")
    .put(0x096, "get_up_2")
    .put(0x097, "guard_break_low")
    .put(0x098, "weird_land")
    .put(0x099, "weird_land_2")
    .put(0x09A, "weird_land_3")
    .put(0x09B, "weird_land_4")
    .put(0x09C, "weird_land_5")
    .put(0x09D, "weird_land_6")
    .put(0x09E, "weird_land_7")
    .put(0x09F, "weird_land_8")
    .put(0x0A0, "5B")
    .put(0x0A1, "6B")
    .put(0x0A2, "4B")
    .put(0x0A3, "2B")
    .put(0x0A4, "DB") // Duplicate
    .put(0x0A5, "DB_2") // Duplicate?
    .put(0x0B0, "5A")
    .put(0x0B1, "6A")
    .put(0x0B2, "4A")
    .put(0x0B3, "2A")
    .put(0x0B4, "DA")
    .put(0x0B5, "DA_2") // Duplicate?
    .put(0x0BE, "weird_land_9")
    .put(0x0BF, "stuck_in_air_2") // Can be hit out
    .put(0x0C0, "RKnJ_ground")
    .put(0x0C1, "RKnJ_air")
    .put(0x0C2, "LKnJ")
    .put(0x0C3, "ZKnJ_incoming")
    .put(0x0C6, "5Z_outgoing")
    .put(0x0C7, "4Z_incoming")
    .put(0x0C8, "5Z_incoming")
    .put(0x0C9, "invisible") // revert when hit
    .put(0x0CA, "Z_switch")
    .put(0x0CB, "ZKnJ_outgoing")
    .put(0x0CC, "teleport_behind_opponent")
    .put(0x0CF, "4Z_outgoing")
    .put(0x0E0, "JB")
    .put(0x0E1, "JA")
    .put(0x0E2, "8B")
    .put(0x0E3, "8A")
    .put(0x0E4, "hit_with_smoke")
    .put(0x0FE, "stuck_in_air_3") // Can be hit out
    .put(0x10A, "stuck_in_air_4") // Can be hit out
    .put(0x116, "stuck_in_air_5") // Can be hit out
    .put(0x120, "stuck_in_air_6") // Can be hit out
    .put(0x121, "5X")
    .put(0x122, "2X")
    .put(0x123, "5X_transformation")
    .put(0x124, "transform")
    .put(0x125, "2X_animation")
    .put(0x126, "head_movement")
    .put(0x127, "head_movement_2")
    .put(0x128, "remove_character")
    .put(0x129, "3_Man_X_1")
    .put(0x12A, "3_Man_X_2")
    .put(0x12B, "3_Man_X_3")
    .put(0x12C, "3_Man_X_4")
    .put(0x12D, "3_Man_X_5")
    .put(0x12E, "3_Man_X_6")
    .put(0x12F, "3_Man_X_7")
    .put(0x130, "combo_move_1") // character-specific
    .put(0x131, "combo_move_2") // character-specific
    .put(0x132, "combo_move_3") // character-specific
    .put(0x133, "combo_move_4") // character-specific
    .put(0x134, "combo_move_5") // character-specific
    .put(0x135, "combo_move_6") // character-specific
    .put(0x136, "combo_move_7") // character-specific
    .put(0x137, "combo_move_8") // character-specific
    .put(0x138, "combo_move_9") // character-specific
    .put(0x139, "combo_move_10") // character-specific
    .put(0x13A, "combo_move_11") // character-specific
    .put(0x13B, "combo_move_12") // character-specific
    .put(0x13C, "combo_move_13") // character-specific
    .put(0x13D, "combo_move_14") // character-specific
    .put(0x13E, "combo_move_15") // character-specific
    .put(0x13F, "combo_move_16") // character-specific
    .put(0x140, "combo_move_17") // character-specific
    .put(0x141, "combo_move_18") // character-specific
    .put(0x142, "combo_move_19") // character-specific
    .put(0x143, "combo_move_20") // character-specific
    .put(0x144, "combo_move_21") // character-specific
    .put(0x145, "combo_move_22") // character-specific
    .put(0x146, "combo_move_23") // character-specific
    .put(0x147, "combo_move_24") // character-specific
    .put(0x148, "combo_move_25") // character-specific
    .put(0x149, "combo_move_26") // character-specific
    .put(0x170, "unused_1") // points to chr_act
    .put(0x171, "unused_2") // points to chr_act
    .put(0x172, "unused_3") // points to chr_act
    .put(0x173, "unused_4") // points to chr_act
    .put(0x174, "unused_5") // points to chr_act
    .put(0x175, "unused_6") // points to chr_act
    .put(0x176, "unused_7") // points to chr_act
    .put(0x177, "unused_8") // points to chr_act
    .put(0x178, "unused_9") // points to chr_act
    .put(0x179, "unused_10") // points to chr_act
    .put(0x17A, "unused_11") // points to chr_act
    .put(0x17B, "unused_12") // points to chr_act
    .put(0x17C, "unused_13") // points to chr_act
    .put(0x17D, "unused_14") // points to chr_act
    .put(0x17E, "unused_15") // points to chr_act
    .put(0x17F, "unused_16") // points to chr_act
    .put(0x180, "unused_17") // points to chr_act
    .put(0x181, "unused_18") // points to chr_act
    .put(0x182, "unused_19") // points to chr_act
    .put(0x183, "unused_20") // points to chr_act
    .put(0x184, "unused_21") // points to chr_act
    .put(0x185, "unused_22") // points to chr_act
    .put(0x186, "unused_23") // points to chr_act
    .put(0x187, "unused_24") // points to chr_act
    .put(0x188, "unused_25") // points to chr_act
    .put(0x189, "unused_26") // points to chr_act
    .put(0x18A, "unused_27") // points to chr_act
    .put(0x18B, "unused_28") // points to chr_act
    .put(0x18C, "unused_29") // points to chr_act
    .put(0x18D, "unused_30") // points to chr_act
    .put(0x18E, "unused_31") // points to chr_act
    .put(0x18F, "unused_32") // points to chr_act
    .put(0x190, "5Y")
    .put(0x191, "2Y")
    .put(0x192, "JY")
    .put(0x193, "Activated_X")
    .put(0x195, "4X")
    .put(0x196, "Activated_2X")
    .put(0x1D0, "activated_3_Man_X_1")
    .put(0x1D1, "activated_3_Man_X_2")
    .put(0x1D2, "activated_3_Man_X_3")
    .put(0x1D3, "activated_3_Man_X_4")
    .put(0x1D5, "activated_3_Man_X_5")
    .put(0x1DF, "activated_3_Man_X_6")
    .put(0x1E0, "Y_capture_state")
    .put(0x1E1, "2Y_capture_state")
    .put(0x1E2, "JY_capture_state")
    .put(0x1E3, "hit_by_super") // e.g. by Naruto 5X
    .put(0x1E4, "hit_by_big_throw") // e.g. by Choji 5A
    .put(0x1E5, "hit_by_underground_throw") // e.g. by Kakashi/Kabuto 2A
    .put(0x1E6, "hit_by_super_2") // e.g. by Naruto 2X
    .put(0x1E7, "hit_by_big_air_grab") // e.g. by OTK 2AA
    .put(0x223, "hit_by_normal_team_super")
    .put(0x225, "hit_by_special_team_super") // e.g. team NarSasSak
    .build();
  }

  public static BiMap<Integer, String> BUTTON_DESCRIPTIONS;

  public static Set<String> getButtonDescriptions() {
    if (BUTTON_DESCRIPTIONS == null) {
      BUTTON_DESCRIPTIONS = initButtonDescriptions();
    }
    return BUTTON_DESCRIPTIONS.values();
  }

  public static String getButtonDescriptions(int buttonBitFields) {
    if (BUTTON_DESCRIPTIONS == null) {
      BUTTON_DESCRIPTIONS = initButtonDescriptions();
    }
    List<String> buttons = new ArrayList<>();
    for (Entry<Integer, String> entry : BUTTON_DESCRIPTIONS.entrySet()) {
      int button = entry.getKey();
      if ((buttonBitFields & button) == button) {
        buttons.add(entry.getValue());
      }
    }
    if (buttons.isEmpty()) {
      return null;
    }
    return String.join("; ", buttons);
  }

  public static OptionalInt getButtonBitfield(String buttonDescriptions) {
    if (BUTTON_DESCRIPTIONS == null) {
      BUTTON_DESCRIPTIONS = initButtonDescriptions();
    }
    String[] buttons = buttonDescriptions.replace(" ", "").split(";");
    int bitfield = 0;
    for (String button : buttons) {
      Integer value = BUTTON_DESCRIPTIONS.inverse().get(button);
      if (value != null) {
        bitfield |= value;
      }
    }
    return bitfield != 0 ? OptionalInt.of(bitfield) : OptionalInt.empty();
  }

  private static BiMap<Integer, String> initButtonDescriptions() {
    return new ImmutableBiMap.Builder<Integer, String>()
    .put(0x0001, "Forward")
    .put(0x0002, "Back")
    .put(0x0004, "Up")
    .put(0x0008, "Down")
    .put(0x0010, "B")
    .put(0x0020, "A")
    .put(0x0100, "Facing_Left") // Otherwise, Facing Right
    .put(0x0200, "Y")
    .put(0x0800, "L")
    .put(0x1000, "R")
    .put(0x2000, "X")
    .put(0x4000, "Z")
    .build();
  }
}
