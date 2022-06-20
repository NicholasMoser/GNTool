package com.github.nicholasmoser.gnt4.seq;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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

  public static final BiMap<Integer, String> N2F_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
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

  public static final Map<String, Integer> N2F_FLAGS_GET = N2F_FLAGS.inverse();

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

  public static final BiMap<Integer, String> K2F_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
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

  public static final Map<String, Integer> K2F_FLAGS_GET = K2F_FLAGS.inverse();

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

  public static final BiMap<Integer, String> D2F_FLAGS = new ImmutableBiMap.Builder<Integer, String>()
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

  public static final Map<String, Integer> D2F_FLAGS_GET = D2F_FLAGS.inverse();

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

  public static Map<Integer, String> ACTION_DESCRIPTIONS;

  public static String getActionDescription(int actionId) {
    if (ACTION_DESCRIPTIONS == null) {
      ACTION_DESCRIPTIONS = initActionDescriptions();
    }
    String description = ACTION_DESCRIPTIONS.get(actionId);
    return description != null ? description : "Unknown";
  }

  private static Map<Integer, String> initActionDescriptions() {
    Map<Integer, String> actionDescriptions = new HashMap<>();
    actionDescriptions.put(0x001, "Stand");
    actionDescriptions.put(0x002, "Walking forward");
    actionDescriptions.put(0x003, "Walking backward");
    actionDescriptions.put(0x004, "Start running");
    actionDescriptions.put(0x005, "Running");
    actionDescriptions.put(0x006, "End running");
    actionDescriptions.put(0x007, "Back dash");
    actionDescriptions.put(0x008, "Unknown");

    actionDescriptions.put(0x013, "Start jumping");
    actionDescriptions.put(0x014, "Jumping up");
    actionDescriptions.put(0x015, "Jumping forward");
    actionDescriptions.put(0x016, "Jumping backwards");
    actionDescriptions.put(0x017, "Jumping up (Duplicate)");
    actionDescriptions.put(0x018, "Jumping forward (Duplicate)");
    actionDescriptions.put(0x019, "Jumping backwards (Duplicate)");
    actionDescriptions.put(0x01A, "Weird jump (Duplicate)");
    actionDescriptions.put(0x01B, "Weird jump");
    actionDescriptions.put(0x01C, "Weird jump (Duplicate)");
    actionDescriptions.put(0x01D, "End jumping");
    actionDescriptions.put(0x01E, "R");
    actionDescriptions.put(0x01F, "L");
    actionDescriptions.put(0x020, "R (Dashing)");
    actionDescriptions.put(0x021, "L (Dashing)");
    actionDescriptions.put(0x022, "Unknown");
    actionDescriptions.put(0x023, "Unknown");
    actionDescriptions.put(0x024, "Unknown");
    actionDescriptions.put(0x025, "Y-cancel");
    actionDescriptions.put(0x026, "Hit out of air");
    actionDescriptions.put(0x027, "Unknown");
    actionDescriptions.put(0x028, "Intro animation (Action after zoom in)");
    actionDescriptions.put(0x029, "Intro animation (Stand during zoom in)");
    actionDescriptions.put(0x02A, "Win animation for round");
    actionDescriptions.put(0x02B, "Win animation for battle (Duplicate)");
    actionDescriptions.put(0x02C, "Win animation for battle");
    actionDescriptions.put(0x02D, "Unknown");
    actionDescriptions.put(0x02E, "Lose animation");
    actionDescriptions.put(0x02F, "Unknown");
    actionDescriptions.put(0x030, "Block soft hit (e.g. by Naruto 5B)");
    actionDescriptions.put(0x031, "Block medium hit (e.g. by Naruto 6B)");
    actionDescriptions.put(0x032, "Block low hit (e.g. by Naruto 2B)");
    actionDescriptions.put(0x033, "Block in air");
    actionDescriptions.put(0x034, "Block with lift (e.g. by Naruto 6A)");

    actionDescriptions.put(0x036, "Unknown");
    actionDescriptions.put(0x037, "Block hard hit and slide (e.g. by Naruto 4B)");
    actionDescriptions.put(0x038, "Unknown");
    actionDescriptions.put(0x039, "Hit");
    actionDescriptions.put(0x03A, "Hit");
    actionDescriptions.put(0x03B, "Hit");
    actionDescriptions.put(0x03C, "Hit");
    actionDescriptions.put(0x03D, "Hit");
    actionDescriptions.put(0x03E, "Hit");
    actionDescriptions.put(0x03F, "Hit");
    actionDescriptions.put(0x040, "Hit high (e.g. by Naruto 5B)");
    actionDescriptions.put(0x041, "Hit medium (e.g. by Naruto 5BB");
    actionDescriptions.put(0x042, "Hit low (e.g. by Temari 2B)");
    actionDescriptions.put(0x043, "Hit");
    actionDescriptions.put(0x044, "Hit");
    actionDescriptions.put(0x045, "Hit from behind (e.g. by Naruto 5B)");
    actionDescriptions.put(0x046, "Landing on ground; hard knockdown (e.g. by Sasuke DB)");
    actionDescriptions.put(0x047, "Hard knockdown animation");
    actionDescriptions.put(0x048, "Sleep (e.g. Kabuto 2X)");
    actionDescriptions.put(0x049, "Hard knockdown animation");
    actionDescriptions.put(0x04A, "Land on ground; on chest (e.g. by Hinata 2B)");
    actionDescriptions.put(0x04B, "Hit with special (lose all chakra)");
    actionDescriptions.put(0x04C, "Hit with hiki2 (e.g. by Shika 2X)");
    actionDescriptions.put(0x04D, "Hit with hiki (e.g. by Jiraya 2A)");
    actionDescriptions.put(0x04E, "Hit with mato2 (e.g. by Shino 2A)");
    actionDescriptions.put(0x04F, "Hit with stagger (e.g. by Naruto first charge of 5A");
    actionDescriptions.put(0x050, "Hit hard (e.g. by Sasuke DB");
    actionDescriptions.put(0x051, "Hit hard from behind (e.g. by Sasuke DB");
    actionDescriptions.put(0x052, "Hit with yoro2 (e.g. by Kidomaru 5A1C)");
    actionDescriptions.put(0x053, "Hit with stagger from behind (e.g. by OTK 5AA)");
    actionDescriptions.put(0x054, "Hit towards ground (e.g. by Tayuya 8A");
    actionDescriptions.put(0x055, "Hit into the air (e.g. by Sakura 2B sometimes)");
    actionDescriptions.put(0x056, "Hit into the air (e.g. by Sakura 2B sometimes)");
    actionDescriptions.put(0x057, "Hit towards ground from behind (e.g. by Tayuya 8A");
    actionDescriptions.put(0x058, "Hit medium into the air (e.g. by Naruto 8B)");
    actionDescriptions.put(0x059, "Hit into the air (e.g. by Temari 4B)");
    actionDescriptions.put(0x05A, "Landing from hit Soft into the Air");
    actionDescriptions.put(0x05B, "Hit soft into the air (e.g. by Naruto 6A)");
    actionDescriptions.put(0x05C, "Hit hard");
    actionDescriptions.put(0x05D, "Hard knockdown");
    actionDescriptions.put(0x05E, "Hit with furi (turns you around, from BR)");
    actionDescriptions.put(0x05F, "Hit with furi (turns you around, from BR)");
    actionDescriptions.put(0x060, "Hit; trip onto ground (e.g. Hinata 2B)");
    actionDescriptions.put(0x061, "Hard knockdown");

    actionDescriptions.put(0x064, "Unknown");
    actionDescriptions.put(0x065, "Guard break");
    actionDescriptions.put(0x066, "Landing on ground on back");
    actionDescriptions.put(0x067, "Laying on ground on back");
    actionDescriptions.put(0x068, "Laying on ground on chest");
    actionDescriptions.put(0x069, "Getting hit on the ground (e.g. CS2 6A OTG)");
    actionDescriptions.put(0x06A, "Hard knockdown");
    actionDescriptions.put(0x06B, "Guard break");
    actionDescriptions.put(0x06C, "Hard knockdown");
    actionDescriptions.put(0x06D, "Getting hit");
    actionDescriptions.put(0x06E, "Getting hit");
    actionDescriptions.put(0x06F, "Hard knockdown");
    actionDescriptions.put(0x070, "Hard knockdown");
    actionDescriptions.put(0x071, "Stand up off ground");
    actionDescriptions.put(0x072, "Roll forward");
    actionDescriptions.put(0x073, "Roll forward");
    actionDescriptions.put(0x074, "Roll backwards");
    actionDescriptions.put(0x075, "Roll right");
    actionDescriptions.put(0x076, "Roll left");
    actionDescriptions.put(0x077, "Get up");
    actionDescriptions.put(0x078, "Get up roll forward");
    actionDescriptions.put(0x079, "Get up roll forward");
    actionDescriptions.put(0x07A, "Get up roll backwards");
    actionDescriptions.put(0x07B, "Get up roll right");
    actionDescriptions.put(0x07C, "Get up roll left");
    actionDescriptions.put(0x07D, "Throw break 1");
    actionDescriptions.put(0x07E, "Throw break 2");
    actionDescriptions.put(0x07F, "Throw break 3");
    actionDescriptions.put(0x080, "Hit hard");
    actionDescriptions.put(0x081, "Hit hard");
    actionDescriptions.put(0x082, "Hit hard");
    actionDescriptions.put(0x083, "Hit hard");
    actionDescriptions.put(0x084, "Hard knockdown to the left");
    actionDescriptions.put(0x085, "Hard knockdown to the right");
    actionDescriptions.put(0x086, "Getting hit");
    actionDescriptions.put(0x087, "Getting hit");
    actionDescriptions.put(0x088, "Hard knockdown back");
    actionDescriptions.put(0x089, "Hard knockdown forward");
    actionDescriptions.put(0x08A, "Unknown");
    actionDescriptions.put(0x08B, "Unknown");
    actionDescriptions.put(0x08C, "Hit hard");
    actionDescriptions.put(0x08D, "Tripping");
    actionDescriptions.put(0x08E, "Hard knockdown bounce (e.g. by OTK jA");
    actionDescriptions.put(0x08F, "Hard knockdown bounce (e.g. by last hit of Sasuke 4BBB");
    actionDescriptions.put(0x090, "Hard hit downward (e.g. by Kabuto 8B)");
    actionDescriptions.put(0x091, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x092, "Hard knockdown slow bounce");
    actionDescriptions.put(0x093, "Hard knockdown slow bounce (e.g. by Kabuto 8B)");
    actionDescriptions.put(0x094, "Unknown");
    actionDescriptions.put(0x095, "Block low");
    actionDescriptions.put(0x096, "Get up");
    actionDescriptions.put(0x097, "Guard break low");
    actionDescriptions.put(0x098, "Weird land");
    actionDescriptions.put(0x099, "Weird land");
    actionDescriptions.put(0x09A, "Weird land");
    actionDescriptions.put(0x09B, "Weird land");
    actionDescriptions.put(0x09C, "Weird land");
    actionDescriptions.put(0x09D, "Weird land");
    actionDescriptions.put(0x09E, "Weird land");
    actionDescriptions.put(0x09F, "Weird land");
    actionDescriptions.put(0x0A0, "5B");
    actionDescriptions.put(0x0A1, "6B");
    actionDescriptions.put(0x0A2, "4B");
    actionDescriptions.put(0x0A3, "2B");
    actionDescriptions.put(0x0A4, "DB (Duplicate)");
    actionDescriptions.put(0x0A5, "DB");

    actionDescriptions.put(0x0B0, "5A");
    actionDescriptions.put(0x0B1, "6A");
    actionDescriptions.put(0x0B2, "4A");
    actionDescriptions.put(0x0B3, "2A");
    actionDescriptions.put(0x0B4, "DA (Duplicate)");
    actionDescriptions.put(0x0B5, "DA");

    actionDescriptions.put(0x0BE, "Weird land");
    actionDescriptions.put(0x0BF, "Stuck in air (Can be hit out)");
    actionDescriptions.put(0x0C0, "RKnJ (Ground)");
    actionDescriptions.put(0x0C1, "RKnJ (Air)");
    actionDescriptions.put(0x0C2, "LKnJ");
    actionDescriptions.put(0x0C3, "ZKnJ incoming");

    actionDescriptions.put(0x0C6, "5Z outgoing");
    actionDescriptions.put(0x0C7, "4Z incoming");
    actionDescriptions.put(0x0C8, "5Z incoming");
    actionDescriptions.put(0x0C9, "Become invisible (revert when hit)");
    actionDescriptions.put(0x0CA, "Z switch");
    actionDescriptions.put(0x0CB, "ZKnJ outgoing");
    actionDescriptions.put(0x0CC, "Teleport behind opponent");

    actionDescriptions.put(0x0CF, "4Z outgoing");

    actionDescriptions.put(0x0E0, "JB");
    actionDescriptions.put(0x0E1, "JA");
    actionDescriptions.put(0x0E2, "8B");
    actionDescriptions.put(0x0E3, "8A");
    actionDescriptions.put(0x0E4, "Hit with smoke");

    actionDescriptions.put(0x0FE, "Stuck in air (Can be hit out)");

    actionDescriptions.put(0x10A, "Stuck in air (Can be hit out)");

    actionDescriptions.put(0x116, "Stuck in air (Can be hit out)");

    actionDescriptions.put(0x120, "Stuck in air (Can be hit out)");
    actionDescriptions.put(0x121, "5X");
    actionDescriptions.put(0x122, "2X");
    actionDescriptions.put(0x123, "5X (Transformation)");
    actionDescriptions.put(0x124, "Transform");
    actionDescriptions.put(0x125, "2X (Animation)");
    actionDescriptions.put(0x126, "Head movement?");
    actionDescriptions.put(0x127, "Head movement?");
    actionDescriptions.put(0x128, "Remove character from game");
    actionDescriptions.put(0x129, "3-Man X #1");
    actionDescriptions.put(0x12A, "3-Man X #2");
    actionDescriptions.put(0x12B, "3-Man X #3");
    actionDescriptions.put(0x12C, "3-Man X #4");
    actionDescriptions.put(0x12D, "3-Man X #5");
    actionDescriptions.put(0x12E, "3-Man X #6");
    actionDescriptions.put(0x12F, "3-Man X #7");
    actionDescriptions.put(0x130, "Combo Moves (character-specific)");
    actionDescriptions.put(0x131, "Combo Moves (character-specific)");
    actionDescriptions.put(0x132, "Combo Moves (character-specific)");
    actionDescriptions.put(0x133, "Combo Moves (character-specific)");
    actionDescriptions.put(0x134, "Combo Moves (character-specific)");
    actionDescriptions.put(0x135, "Combo Moves (character-specific)");
    actionDescriptions.put(0x136, "Combo Moves (character-specific)");
    actionDescriptions.put(0x137, "Combo Moves (character-specific)");
    actionDescriptions.put(0x138, "Combo Moves (character-specific)");
    actionDescriptions.put(0x139, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13A, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13B, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13C, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13D, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13E, "Combo Moves (character-specific)");
    actionDescriptions.put(0x13F, "Combo Moves (character-specific)");
    actionDescriptions.put(0x140, "Combo Moves (character-specific)");
    actionDescriptions.put(0x141, "Combo Moves (character-specific)");
    actionDescriptions.put(0x142, "Combo Moves (character-specific)");
    actionDescriptions.put(0x143, "Combo Moves (character-specific)");
    actionDescriptions.put(0x144, "Combo Moves (character-specific)");
    actionDescriptions.put(0x145, "Combo Moves (character-specific)");
    actionDescriptions.put(0x146, "Combo Moves (character-specific)");
    actionDescriptions.put(0x147, "Combo Moves (character-specific)");
    actionDescriptions.put(0x148, "Combo Moves (character-specific)");
    actionDescriptions.put(0x149, "Combo Moves (character-specific)");

    actionDescriptions.put(0x170, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x171, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x172, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x173, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x174, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x175, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x176, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x177, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x178, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x179, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17A, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17B, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17C, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17D, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17E, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x17F, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x180, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x181, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x182, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x183, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x184, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x185, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x186, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x187, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x188, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x189, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18A, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18B, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18C, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18D, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18E, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x18F, "(UNUSED - points to chr_act)");
    actionDescriptions.put(0x190, "Ground Throw");
    actionDescriptions.put(0x191, "Back Ground Throw");
    actionDescriptions.put(0x192, "Air Throw");
    actionDescriptions.put(0x193, "Activated X");
    actionDescriptions.put(0x194, "Unknown");
    actionDescriptions.put(0x195, "Activated X (Transformation)");
    actionDescriptions.put(0x196, "Activated 2X");
    actionDescriptions.put(0x197, "Unknown");

    actionDescriptions.put(0x1D0, "Activated 3-Man X #1");
    actionDescriptions.put(0x1D1, "Activated 3-Man X #2");
    actionDescriptions.put(0x1D2, "Activated 3-Man X #3");
    actionDescriptions.put(0x1D3, "Activated 3-Man X #4");

    actionDescriptions.put(0x1D5, "Activated 3-Man X #5");

    actionDescriptions.put(0x1DF, "Activated 3-Man X #6");
    actionDescriptions.put(0x1E0, "Getting thrown (front)");
    actionDescriptions.put(0x1E1, "Getting thrown (back)");
    actionDescriptions.put(0x1E2, "Getting thrown (air)");
    actionDescriptions.put(0x1E3, "Hit by super (e.g. by Naruto 5X)");
    actionDescriptions.put(0x1E4, "Hit by big throw (e.g. by Choji 5A)");
    actionDescriptions.put(0x1E5, "Hit by underground throw (e.g. by Kakashi/Kabuto 2A)");
    actionDescriptions.put(0x1E6, "Hit by super (e.g. by Naruto 2X)");
    actionDescriptions.put(0x1E7, "Hit by big air grab (e.g. by OTK 2AA)");

    actionDescriptions.put(0x223, "Hit by normal team super");

    actionDescriptions.put(0x225, "Hit by special team super (e.g. team NarSasSak)");

    return actionDescriptions;
  }
}
