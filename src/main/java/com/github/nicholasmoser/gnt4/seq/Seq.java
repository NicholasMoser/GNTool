package com.github.nicholasmoser.gnt4.seq;

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

  public static final Map<Integer, String> MF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "GUARD"),
          new AbstractMap.SimpleEntry<>(0x00000002, "MUTEKI"),
          new AbstractMap.SimpleEntry<>(0x00000004, "BUTT"),
          new AbstractMap.SimpleEntry<>(0x00000008, "DEBUG"),
          new AbstractMap.SimpleEntry<>(0x00000010, "DEKA"),
          new AbstractMap.SimpleEntry<>(0x00000020, "TIBI"),
          new AbstractMap.SimpleEntry<>(0x00000040, "UDE"),
          new AbstractMap.SimpleEntry<>(0x00000080, "TYAKURA"),
          new AbstractMap.SimpleEntry<>(0x00000100, "TYAKURAREC"),
          new AbstractMap.SimpleEntry<>(0x00000200, "09"),
          new AbstractMap.SimpleEntry<>(0x00000400, "10"),
          new AbstractMap.SimpleEntry<>(0x00000800, "11"),
          new AbstractMap.SimpleEntry<>(0x00001000, "12"),
          new AbstractMap.SimpleEntry<>(0x00002000, "13"),
          new AbstractMap.SimpleEntry<>(0x00004000, "14"),
          new AbstractMap.SimpleEntry<>(0x00008000, "15"),
          new AbstractMap.SimpleEntry<>(0x00010000, "16"),
          new AbstractMap.SimpleEntry<>(0x00020000, "17"),
          new AbstractMap.SimpleEntry<>(0x00040000, "18"),
          new AbstractMap.SimpleEntry<>(0x00080000, "19"),
          new AbstractMap.SimpleEntry<>(0x00100000, "20"),
          new AbstractMap.SimpleEntry<>(0x00200000, "21"),
          new AbstractMap.SimpleEntry<>(0x00400000, "22"),
          new AbstractMap.SimpleEntry<>(0x00800000, "23"),
          new AbstractMap.SimpleEntry<>(0x01000000, "24"),
          new AbstractMap.SimpleEntry<>(0x02000000, "25"),
          new AbstractMap.SimpleEntry<>(0x04000000, "26"),
          new AbstractMap.SimpleEntry<>(0x08000000, "27"),
          new AbstractMap.SimpleEntry<>(0x10000000, "28"),
          new AbstractMap.SimpleEntry<>(0x20000000, "29"),
          new AbstractMap.SimpleEntry<>(0x40000000, "30"),
          new AbstractMap.SimpleEntry<>(0x80000000, "31")
      );

  public static final Map<Integer, String> AF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "STAND"),
          new AbstractMap.SimpleEntry<>(0x00000002, "FORWARD"),
          new AbstractMap.SimpleEntry<>(0x00000004, "BACK"),
          new AbstractMap.SimpleEntry<>(0x00000008, "DASH"),
          new AbstractMap.SimpleEntry<>(0x00000010, "SIT"),
          new AbstractMap.SimpleEntry<>(0x00000020, "FUSE"),
          new AbstractMap.SimpleEntry<>(0x00000040, "UKEMI"),
          new AbstractMap.SimpleEntry<>(0x00000080, "KIRI"),
          new AbstractMap.SimpleEntry<>(0x00000100, "SPMDMG"),
          new AbstractMap.SimpleEntry<>(0x00000200, "SLANT"),
          new AbstractMap.SimpleEntry<>(0x00000400, "QUICK"),
          new AbstractMap.SimpleEntry<>(0x00000800, "FLOAT"),
          new AbstractMap.SimpleEntry<>(0x00001000, "JUMP"),
          new AbstractMap.SimpleEntry<>(0x00002000, "FALL"),
          new AbstractMap.SimpleEntry<>(0x00004000, "SMALL"),
          new AbstractMap.SimpleEntry<>(0x00008000, "DAMAGE"),
          new AbstractMap.SimpleEntry<>(0x00010000, "DOWNU"),
          new AbstractMap.SimpleEntry<>(0x00020000, "DOWNO"),
          new AbstractMap.SimpleEntry<>(0x00040000, "GETUP"),
          new AbstractMap.SimpleEntry<>(0x00080000, "TURN"),
          new AbstractMap.SimpleEntry<>(0x00100000, "TDOWN"),
          new AbstractMap.SimpleEntry<>(0x00200000, "CANTACT"),
          new AbstractMap.SimpleEntry<>(0x00400000, "SDEF"),
          new AbstractMap.SimpleEntry<>(0x00800000, "BDEF"),
          new AbstractMap.SimpleEntry<>(0x01000000, "BEAST"),
          new AbstractMap.SimpleEntry<>(0x02000000, "UKI"),
          new AbstractMap.SimpleEntry<>(0x04000000, "BUTT"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NDOWN"),
          new AbstractMap.SimpleEntry<>(0x10000000, "DEF"),
          new AbstractMap.SimpleEntry<>(0x20000000, "TFAIL"),
          new AbstractMap.SimpleEntry<>(0x40000000, "THROW"),
          new AbstractMap.SimpleEntry<>(0x80000000, "ATTACK")
      );

  public static final Map<Integer, String> PF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "DEFOK"),
          new AbstractMap.SimpleEntry<>(0x00000002, "BDEFOK"),
          new AbstractMap.SimpleEntry<>(0x00000004, "BGUARD"),
          new AbstractMap.SimpleEntry<>(0x00000008, "HIT"),
          new AbstractMap.SimpleEntry<>(0x00000010, "REVERSAL"),
          new AbstractMap.SimpleEntry<>(0x00000020, "GHIT"),
          new AbstractMap.SimpleEntry<>(0x00000040, "COMBO"),
          new AbstractMap.SimpleEntry<>(0x00000080, "FLOAT"),
          new AbstractMap.SimpleEntry<>(0x00000100, "FALL"),
          new AbstractMap.SimpleEntry<>(0x00000200, "ENEDMG"),
          new AbstractMap.SimpleEntry<>(0x00000400, "DIRNOGRD"),
          new AbstractMap.SimpleEntry<>(0x00000800, "ENEDWN"),
          new AbstractMap.SimpleEntry<>(0x00001000, "ENEATK"),
          new AbstractMap.SimpleEntry<>(0x00002000, "BDEF"),
          new AbstractMap.SimpleEntry<>(0x00004000, "THROWOK"),
          new AbstractMap.SimpleEntry<>(0x00008000, "BTNOMOVE"),
          new AbstractMap.SimpleEntry<>(0x00010000, "NECKTURN"),
          new AbstractMap.SimpleEntry<>(0x00020000, "ABSTURN"),
          new AbstractMap.SimpleEntry<>(0x00040000, "AIR"),
          new AbstractMap.SimpleEntry<>(0x00080000, "RINGOUT"),
          new AbstractMap.SimpleEntry<>(0x00100000, "TURN"),
          new AbstractMap.SimpleEntry<>(0x00200000, "ZOMBIE"),
          new AbstractMap.SimpleEntry<>(0x00400000, "BACK"),
          new AbstractMap.SimpleEntry<>(0x00800000, "BODY"),
          new AbstractMap.SimpleEntry<>(0x01000000, "M_KABE"),
          new AbstractMap.SimpleEntry<>(0x02000000, "GUARD"),
          new AbstractMap.SimpleEntry<>(0x04000000, "DAMAGE"),
          new AbstractMap.SimpleEntry<>(0x08000000, "ABSTURNR"),
          new AbstractMap.SimpleEntry<>(0x10000000, "NORMAL"),
          new AbstractMap.SimpleEntry<>(0x20000000, "DMG"),
          new AbstractMap.SimpleEntry<>(0x40000000, "DEF"),
          new AbstractMap.SimpleEntry<>(0x80000000, "OUT")
      );

  public static final Map<Integer, String> NF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "KAMAE"),
          new AbstractMap.SimpleEntry<>(0x00000002, "DISP"),
          new AbstractMap.SimpleEntry<>(0x00000004, "TDMG"),
          new AbstractMap.SimpleEntry<>(0x00000008, "JUMP2"),
          new AbstractMap.SimpleEntry<>(0x00000010, "LEVERDIR"),
          new AbstractMap.SimpleEntry<>(0x00000020, "GETUP"),
          new AbstractMap.SimpleEntry<>(0x00000040, "HITEFT"),
          new AbstractMap.SimpleEntry<>(0x00000080, "NFOG"),
          new AbstractMap.SimpleEntry<>(0x00000100, "TAKEON"),
          new AbstractMap.SimpleEntry<>(0x00000200, "FOG"),
          new AbstractMap.SimpleEntry<>(0x00000400, "BDRIVESLEEP"),
          new AbstractMap.SimpleEntry<>(0x00000800, "JUMP"),
          new AbstractMap.SimpleEntry<>(0x00001000, "FALL"),
          new AbstractMap.SimpleEntry<>(0x00002000, "JSPD"),
          new AbstractMap.SimpleEntry<>(0x00004000, "SHOTDEF"),
          new AbstractMap.SimpleEntry<>(0x00008000, "MOVE"),
          new AbstractMap.SimpleEntry<>(0x00010000, "ATTACK"),
          new AbstractMap.SimpleEntry<>(0x00020000, "BUTTON"),
          new AbstractMap.SimpleEntry<>(0x00040000, "COMBO"),
          new AbstractMap.SimpleEntry<>(0x00080000, "DISP_N"),
          new AbstractMap.SimpleEntry<>(0x00100000, "KABEHIT"),
          new AbstractMap.SimpleEntry<>(0x00200000, "BODYTOUCH"),
          new AbstractMap.SimpleEntry<>(0x00400000, "AGUARD"),
          new AbstractMap.SimpleEntry<>(0x00800000, "DAMAGE"),
          new AbstractMap.SimpleEntry<>(0x01000000, "GUARD"),
          new AbstractMap.SimpleEntry<>(0x02000000, "AUTODIR"),
          new AbstractMap.SimpleEntry<>(0x04000000, "ENEAUTO"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NJPTURN"),
          new AbstractMap.SimpleEntry<>(0x10000000, "RINGOUT"),
          new AbstractMap.SimpleEntry<>(0x20000000, "KABE"),
          new AbstractMap.SimpleEntry<>(0x40000000, "TDOWN"),
          new AbstractMap.SimpleEntry<>(0x80000000, "LEVER")
      );

  public static final Map<Integer, String> N2F_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "KAMAE"),
          new AbstractMap.SimpleEntry<>(0x00000002, "KAWARIMI"),
          new AbstractMap.SimpleEntry<>(0x00000004, "NAGENUKE"),
          new AbstractMap.SimpleEntry<>(0x00000008, "PUSH"),
          new AbstractMap.SimpleEntry<>(0x00000010, "DEFEFT"),
          new AbstractMap.SimpleEntry<>(0x00000020, "HITSHOCK"),
          new AbstractMap.SimpleEntry<>(0x00000040, "DEFSHOCK"),
          new AbstractMap.SimpleEntry<>(0x00000080, "GAGE"),
          new AbstractMap.SimpleEntry<>(0x00000100, "TYAKURA"),
          new AbstractMap.SimpleEntry<>(0x00000200, "CAMERAOFF"),
          new AbstractMap.SimpleEntry<>(0x00000400, "CUTOFF"),
          new AbstractMap.SimpleEntry<>(0x00000800, "KABEHITSP"),
          new AbstractMap.SimpleEntry<>(0x00001000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00002000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00004000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00010000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00020000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00040000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00080000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00100000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00200000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00400000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00800000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x01000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x02000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x04000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x10000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x20000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x40000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x80000000, "NULL")
      );

  public static final Map<Integer, String> KF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "REPLAY"),
          new AbstractMap.SimpleEntry<>(0x00000002, "BDRIVE"),
          new AbstractMap.SimpleEntry<>(0x00000004, "SHOT"),
          new AbstractMap.SimpleEntry<>(0x00000008, "POW_W"),
          new AbstractMap.SimpleEntry<>(0x00000010, "POW_M"),
          new AbstractMap.SimpleEntry<>(0x00000020, "POW_S"),
          new AbstractMap.SimpleEntry<>(0x00000040, "LOW"),
          new AbstractMap.SimpleEntry<>(0x00000080, "MIDDLE"),
          new AbstractMap.SimpleEntry<>(0x00000100, "HIGH"),
          new AbstractMap.SimpleEntry<>(0x00000200, "PUNCH"),
          new AbstractMap.SimpleEntry<>(0x00000400, "KICK"),
          new AbstractMap.SimpleEntry<>(0x00000800, "THROW"),
          new AbstractMap.SimpleEntry<>(0x00001000, "OIUCHI"),
          new AbstractMap.SimpleEntry<>(0x00002000, "SPECIAL"),
          new AbstractMap.SimpleEntry<>(0x00004000, "NOGUARD"),
          new AbstractMap.SimpleEntry<>(0x00008000, "TDOWN"),
          new AbstractMap.SimpleEntry<>(0x00010000, "SPTATA"),
          new AbstractMap.SimpleEntry<>(0x00020000, "BREAK"),
          new AbstractMap.SimpleEntry<>(0x00040000, "COMBO"),
          new AbstractMap.SimpleEntry<>(0x00080000, "DOWN"),
          new AbstractMap.SimpleEntry<>(0x00100000, "YORO"),
          new AbstractMap.SimpleEntry<>(0x00200000, "BUTT"),
          new AbstractMap.SimpleEntry<>(0x00400000, "UKI"),
          new AbstractMap.SimpleEntry<>(0x00800000, "FURI"),
          new AbstractMap.SimpleEntry<>(0x01000000, "KORO"),
          new AbstractMap.SimpleEntry<>(0x02000000, "REACH_L"),
          new AbstractMap.SimpleEntry<>(0x04000000, "TATA"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NOSPEEP"),
          new AbstractMap.SimpleEntry<>(0x10000000, "BEAST"),
          new AbstractMap.SimpleEntry<>(0x20000000, "FREEZE"),
          new AbstractMap.SimpleEntry<>(0x40000000, "CANCEL"),
          new AbstractMap.SimpleEntry<>(0x80000000, "ATKCAN")
      );

  public static final Map<Integer, String> K2F_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "YORO2"),
          new AbstractMap.SimpleEntry<>(0x00000002, "HIKI"),
          new AbstractMap.SimpleEntry<>(0x00000004, "HIKI2"),
          new AbstractMap.SimpleEntry<>(0x00000008, "MISSION"),
          new AbstractMap.SimpleEntry<>(0x00000010, "NATEMI"),
          new AbstractMap.SimpleEntry<>(0x00000020, "SUPERARMOR"),
          new AbstractMap.SimpleEntry<>(0x00000040, "MOTO2"),
          new AbstractMap.SimpleEntry<>(0x00000080, "ATKALLCAN"),
          new AbstractMap.SimpleEntry<>(0x00000100, "TOJI"),
          new AbstractMap.SimpleEntry<>(0x00000200, "HASA"),
          new AbstractMap.SimpleEntry<>(0x00000400, "SHAVE"),
          new AbstractMap.SimpleEntry<>(0x00000800, "NEMU"),
          new AbstractMap.SimpleEntry<>(0x00001000, "WING"),
          new AbstractMap.SimpleEntry<>(0x00002000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00004000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00010000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00020000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00040000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00080000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00100000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00200000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00400000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00800000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x01000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x02000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x04000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x10000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x20000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x40000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x80000000, "NULL")
      );

  public static final Map<Integer, String> DF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "F"),
          new AbstractMap.SimpleEntry<>(0x00000002, "B"),
          new AbstractMap.SimpleEntry<>(0x00000004, "R"),
          new AbstractMap.SimpleEntry<>(0x00000008, "L"),
          new AbstractMap.SimpleEntry<>(0x00000010, "W"),
          new AbstractMap.SimpleEntry<>(0x00000020, "M"),
          new AbstractMap.SimpleEntry<>(0x00000040, "S"),
          new AbstractMap.SimpleEntry<>(0x00000080, "SPECIAL"),
          new AbstractMap.SimpleEntry<>(0x00000100, "DOWN"),
          new AbstractMap.SimpleEntry<>(0x00000200, "YORO"),
          new AbstractMap.SimpleEntry<>(0x00000400, "BUTT"),
          new AbstractMap.SimpleEntry<>(0x00000800, "UKI"),
          new AbstractMap.SimpleEntry<>(0x00001000, "FURI"),
          new AbstractMap.SimpleEntry<>(0x00002000, "KORO"),
          new AbstractMap.SimpleEntry<>(0x00004000, "TATA"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NODIS"),
          new AbstractMap.SimpleEntry<>(0x00010000, "A_LOW"),
          new AbstractMap.SimpleEntry<>(0x00020000, "A_MIDDLE"),
          new AbstractMap.SimpleEntry<>(0x00040000, "A_HIGH"),
          new AbstractMap.SimpleEntry<>(0x00080000, "BREAK"),
          new AbstractMap.SimpleEntry<>(0x00100000, "NBREAK"),
          new AbstractMap.SimpleEntry<>(0x00200000, "OIUCHI"),
          new AbstractMap.SimpleEntry<>(0x00400000, "TESCAPE"),
          new AbstractMap.SimpleEntry<>(0x00800000, "MEKURI"),
          new AbstractMap.SimpleEntry<>(0x01000000, "BDRIVE"),
          new AbstractMap.SimpleEntry<>(0x02000000, "COUNTER_N"),
          new AbstractMap.SimpleEntry<>(0x04000000, "SHOT"),
          new AbstractMap.SimpleEntry<>(0x08000000, "COUNTER"),
          new AbstractMap.SimpleEntry<>(0x10000000, "HITCNT"),
          new AbstractMap.SimpleEntry<>(0x20000000, "HITCNT2"),
          new AbstractMap.SimpleEntry<>(0x40000000, "OFFSET"),
          new AbstractMap.SimpleEntry<>(0x80000000, "SPMDMG")
      );

  public static final Map<Integer, String> D2F_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "MATO"),
          new AbstractMap.SimpleEntry<>(0x00000002, "HIKI"),
          new AbstractMap.SimpleEntry<>(0x00000004, "HIKI2"),
          new AbstractMap.SimpleEntry<>(0x00000008, "MISSION"),
          new AbstractMap.SimpleEntry<>(0x00000010, "BDGUARD"),
          new AbstractMap.SimpleEntry<>(0x00000020, "MOTO2"),
          new AbstractMap.SimpleEntry<>(0x00000040, "TOJI"),
          new AbstractMap.SimpleEntry<>(0x00000080, "HASA"),
          new AbstractMap.SimpleEntry<>(0x00000100, "NEMU"),
          new AbstractMap.SimpleEntry<>(0x00000200, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00000400, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00000800, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00001000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00002000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00004000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00010000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00020000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00040000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00080000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00100000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00200000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00400000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00800000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x01000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x02000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x04000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x08000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x10000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x20000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x40000000, "NULL"),
          new AbstractMap.SimpleEntry<>(0x80000000, "NULL")
      );

  public static final Map<Integer, String> EF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "KABE"),
          new AbstractMap.SimpleEntry<>(0x00000002, "KABEN"),
          new AbstractMap.SimpleEntry<>(0x00000004, "KABEC"),
          new AbstractMap.SimpleEntry<>(0x00000008, "PAUSE"),
          new AbstractMap.SimpleEntry<>(0x00000010, "COMNUKE"),
          new AbstractMap.SimpleEntry<>(0x00000020, "RESCAPE"),
          new AbstractMap.SimpleEntry<>(0x00000040, "HOKAN"),
          new AbstractMap.SimpleEntry<>(0x00000080, "WARPHIP"),
          new AbstractMap.SimpleEntry<>(0x00000100, "TDOWNFAIL"),
          new AbstractMap.SimpleEntry<>(0x00000200, "NULL"),
          new AbstractMap.SimpleEntry<>(0x00000400, "BKOUT"),
          new AbstractMap.SimpleEntry<>(0x00000800, "ATK"),
          new AbstractMap.SimpleEntry<>(0x00001000, "SPOSE"),
          new AbstractMap.SimpleEntry<>(0x00002000, "LEVERREV"),
          new AbstractMap.SimpleEntry<>(0x00004000, "ATKCAN"),
          new AbstractMap.SimpleEntry<>(0x00008000, "OFFBEAST"),
          new AbstractMap.SimpleEntry<>(0x00010000, "HOPUP"),
          new AbstractMap.SimpleEntry<>(0x00020000, "WARP"),
          new AbstractMap.SimpleEntry<>(0x00040000, "FIX"),
          new AbstractMap.SimpleEntry<>(0x00080000, "TAKEON"),
          new AbstractMap.SimpleEntry<>(0x00100000, "RINGOUT"),
          new AbstractMap.SimpleEntry<>(0x00200000, "TFAIL"),
          new AbstractMap.SimpleEntry<>(0x00400000, "THROW"),
          new AbstractMap.SimpleEntry<>(0x00800000, "TDOWN"),
          new AbstractMap.SimpleEntry<>(0x01000000, "COMBO0"),
          new AbstractMap.SimpleEntry<>(0x02000000, "COMBO1"),
          new AbstractMap.SimpleEntry<>(0x04000000, "TESCAPE"),
          new AbstractMap.SimpleEntry<>(0x08000000, "BDRIVE"),
          new AbstractMap.SimpleEntry<>(0x10000000, "FLAG0"),
          new AbstractMap.SimpleEntry<>(0x20000000, "FLAG1"),
          new AbstractMap.SimpleEntry<>(0x40000000, "FLAG2"),
          new AbstractMap.SimpleEntry<>(0x80000000, "31")
      );

  public static final Map<Integer, String> RF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "COLOR"),
          new AbstractMap.SimpleEntry<>(0x00000002, "TYAKURASUB"),
          new AbstractMap.SimpleEntry<>(0x00000004, "HAZIKI"),
          new AbstractMap.SimpleEntry<>(0x00000008, "HAZIKIR"),
          new AbstractMap.SimpleEntry<>(0x00000010, "ALLGUARD"),
          new AbstractMap.SimpleEntry<>(0x00000020, "EFTREV"),
          new AbstractMap.SimpleEntry<>(0x00000040, "TARGETDIRA"),
          new AbstractMap.SimpleEntry<>(0x00000080, "GCANCELCHK"),
          new AbstractMap.SimpleEntry<>(0x00000100, "GCANCELOK"),
          new AbstractMap.SimpleEntry<>(0x00000200, "GCANCEL"),
          new AbstractMap.SimpleEntry<>(0x00000400, "GATTACK"),
          new AbstractMap.SimpleEntry<>(0x00000800, "NKAWARIMI"),
          new AbstractMap.SimpleEntry<>(0x00001000, "AUTOMOTION"),
          new AbstractMap.SimpleEntry<>(0x00002000, "EVENT00"),
          new AbstractMap.SimpleEntry<>(0x00004000, "SHADOWOFF"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NOBACK"),
          new AbstractMap.SimpleEntry<>(0x00010000, "NSOUSAI"),
          new AbstractMap.SimpleEntry<>(0x00020000, "TAG2SP"),
          new AbstractMap.SimpleEntry<>(0x00040000, "TAG3SP"),
          new AbstractMap.SimpleEntry<>(0x00080000, "VANISH"),
          new AbstractMap.SimpleEntry<>(0x00100000, "INTRUDE"),
          new AbstractMap.SimpleEntry<>(0x00200000, "NOSTIFF"),
          new AbstractMap.SimpleEntry<>(0x00400000, "MOTIONREG"),
          new AbstractMap.SimpleEntry<>(0x00800000, "BDRIVEDEFDMG"),
          new AbstractMap.SimpleEntry<>(0x01000000, "ATTACKOK"),
          new AbstractMap.SimpleEntry<>(0x02000000, "DEFAULTTEXREV"),
          new AbstractMap.SimpleEntry<>(0x04000000, "PARDIR"),
          new AbstractMap.SimpleEntry<>(0x08000000, "ATKCHANGE"),
          new AbstractMap.SimpleEntry<>(0x10000000, "KAWARIMI"),
          new AbstractMap.SimpleEntry<>(0x20000000, "ACTCAN"),
          new AbstractMap.SimpleEntry<>(0x40000000, "30"),
          new AbstractMap.SimpleEntry<>(0x80000000, "31")
      );

  public static final Map<Integer, String> CF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "DMGOFF"),
          new AbstractMap.SimpleEntry<>(0x00000002, "TYAKURASUB"),
          new AbstractMap.SimpleEntry<>(0x00000004, "DISPOFF"),
          new AbstractMap.SimpleEntry<>(0x00000008, "TAKEONOFF"),
          new AbstractMap.SimpleEntry<>(0x00000010, "CHGNOATTACK"),
          new AbstractMap.SimpleEntry<>(0x00000020, "CHGNODMG"),
          new AbstractMap.SimpleEntry<>(0x00000040, "ANMCHG"),
          new AbstractMap.SimpleEntry<>(0x00000080, "COPYPFHIT"),
          new AbstractMap.SimpleEntry<>(0x00000100, "CLR"),
          new AbstractMap.SimpleEntry<>(0x00000200, "TYAKURAADD"),
          new AbstractMap.SimpleEntry<>(0x00000400, "PARENTMOVE"),
          new AbstractMap.SimpleEntry<>(0x00000800, "PINCH"),
          new AbstractMap.SimpleEntry<>(0x00001000, "CAMERAON"),
          new AbstractMap.SimpleEntry<>(0x00002000, "COMBOONLY"),
          new AbstractMap.SimpleEntry<>(0x00004000, "TARGETPARENT"),
          new AbstractMap.SimpleEntry<>(0x00008000, "NORESULT"),
          new AbstractMap.SimpleEntry<>(0x00010000, "TARGETPARENT2"),
          new AbstractMap.SimpleEntry<>(0x00020000, "PARDMGOFFCOPYTHROW"),
          new AbstractMap.SimpleEntry<>(0x00040000, "19"),
          new AbstractMap.SimpleEntry<>(0x00080000, "20"),
          new AbstractMap.SimpleEntry<>(0x00100000, "21"),
          new AbstractMap.SimpleEntry<>(0x00200000, "22"),
          new AbstractMap.SimpleEntry<>(0x00400000, "23"),
          new AbstractMap.SimpleEntry<>(0x00800000, "24"),
          new AbstractMap.SimpleEntry<>(0x01000000, "25"),
          new AbstractMap.SimpleEntry<>(0x02000000, "26"),
          new AbstractMap.SimpleEntry<>(0x04000000, "27"),
          new AbstractMap.SimpleEntry<>(0x08000000, "28"),
          new AbstractMap.SimpleEntry<>(0x10000000, "29"),
          new AbstractMap.SimpleEntry<>(0x20000000, "30"),
          new AbstractMap.SimpleEntry<>(0x40000000, "31"),
          new AbstractMap.SimpleEntry<>(0x80000000, "(empty)")
      );

  public static final Map<Integer, String> SF_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "HIT"),
          new AbstractMap.SimpleEntry<>(0x00000002, "GHIT"),
          new AbstractMap.SimpleEntry<>(0x00000004, "DAMAGE"),
          new AbstractMap.SimpleEntry<>(0x00000008, "GUARD"),
          new AbstractMap.SimpleEntry<>(0x00000010, "DEFOK"),
          new AbstractMap.SimpleEntry<>(0x00000020, "CATCH"),
          new AbstractMap.SimpleEntry<>(0x00000040, "06"),
          new AbstractMap.SimpleEntry<>(0x00000080, "07"),
          new AbstractMap.SimpleEntry<>(0x00000100, "08"),
          new AbstractMap.SimpleEntry<>(0x00000200, "09"),
          new AbstractMap.SimpleEntry<>(0x00000400, "10"),
          new AbstractMap.SimpleEntry<>(0x00000800, "11"),
          new AbstractMap.SimpleEntry<>(0x00001000, "12"),
          new AbstractMap.SimpleEntry<>(0x00002000, "13"),
          new AbstractMap.SimpleEntry<>(0x00004000, "14"),
          new AbstractMap.SimpleEntry<>(0x00008000, "15"),
          new AbstractMap.SimpleEntry<>(0x00010000, "16"),
          new AbstractMap.SimpleEntry<>(0x00020000, "17"),
          new AbstractMap.SimpleEntry<>(0x00040000, "18"),
          new AbstractMap.SimpleEntry<>(0x00080000, "19"),
          new AbstractMap.SimpleEntry<>(0x00100000, "20"),
          new AbstractMap.SimpleEntry<>(0x00200000, "21"),
          new AbstractMap.SimpleEntry<>(0x00400000, "22"),
          new AbstractMap.SimpleEntry<>(0x00800000, "23"),
          new AbstractMap.SimpleEntry<>(0x01000000, "24"),
          new AbstractMap.SimpleEntry<>(0x02000000, "25"),
          new AbstractMap.SimpleEntry<>(0x04000000, "26"),
          new AbstractMap.SimpleEntry<>(0x08000000, "27"),
          new AbstractMap.SimpleEntry<>(0x10000000, "28"),
          new AbstractMap.SimpleEntry<>(0x20000000, "29"),
          new AbstractMap.SimpleEntry<>(0x40000000, "30"),
          new AbstractMap.SimpleEntry<>(0x80000000, "31")
      );

  public static final Map<Integer, String> CHR_MOD_FLAGS =
      Map.ofEntries(new AbstractMap.SimpleEntry<>(0x00000001, "Attack Boost Lv1"),
          new AbstractMap.SimpleEntry<>(0x00000002, "Attack Boost Lv2"),
          new AbstractMap.SimpleEntry<>(0x00000004, "Disable Chakra Gain"),
          new AbstractMap.SimpleEntry<>(0x00000008, "Auto-Recover Chakra"),
          new AbstractMap.SimpleEntry<>(0x00000010, "Special Jutsu Boost"),
          new AbstractMap.SimpleEntry<>(0x00000020, "Health Absorption"),
          new AbstractMap.SimpleEntry<>(0x00000040, "Reverse Directions"),
          new AbstractMap.SimpleEntry<>(0x00000080, "Health Boost Small"),
          new AbstractMap.SimpleEntry<>(0x00000100, "Health Boost Medium"),
          new AbstractMap.SimpleEntry<>(0x00000200, "Health Boost Large"),
          new AbstractMap.SimpleEntry<>(0x00000400, "Auto-Throw Escape"),
          new AbstractMap.SimpleEntry<>(0x00000800, "Auto-Ground Tech"),
          new AbstractMap.SimpleEntry<>(0x00001000, "Super Armor"),
          new AbstractMap.SimpleEntry<>(0x00002000, "Auto-Recover Health"),
          new AbstractMap.SimpleEntry<>(0x00004000, "Invincibility for 10 Seconds"),
          new AbstractMap.SimpleEntry<>(0x00008000, "Absolute Defense"),
          new AbstractMap.SimpleEntry<>(0x00010000, "Halve Chakra Consumption"),
          new AbstractMap.SimpleEntry<>(0x00020000, "Disable Ground Tech"),
          new AbstractMap.SimpleEntry<>(0x00040000, "Disable Substitution"),
          new AbstractMap.SimpleEntry<>(0x00080000, "Disable Sidestep"),
          new AbstractMap.SimpleEntry<>(0x00100000, "Disable B Button"),
          new AbstractMap.SimpleEntry<>(0x00200000, "Disable A Button"),
          new AbstractMap.SimpleEntry<>(0x00400000, "Disable X Button"),
          new AbstractMap.SimpleEntry<>(0x00800000, "Disable Y Button"),
          new AbstractMap.SimpleEntry<>(0x01000000, "Disable Throw Escape"),
          new AbstractMap.SimpleEntry<>(0x02000000, "Disable Chakra Use"),
          new AbstractMap.SimpleEntry<>(0x04000000, "Disable Jump"),
          new AbstractMap.SimpleEntry<>(0x08000000, "Disable Guard"),
          new AbstractMap.SimpleEntry<>(0x10000000, "Disable Projectiles"),
          new AbstractMap.SimpleEntry<>(0x20000000, "Health Drain"),
          new AbstractMap.SimpleEntry<>(0x40000000, "Halve Attack Power"),
          new AbstractMap.SimpleEntry<>(0x80000000, "Delete HP and Chakra Guard")
      );

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
    actionDescriptions.put(0x002, "Walking Forward");
    actionDescriptions.put(0x003, "Walking Backward");
    actionDescriptions.put(0x004, "Start Running");
    actionDescriptions.put(0x005, "Running");
    actionDescriptions.put(0x006, "End Running");
    actionDescriptions.put(0x007, "Back Dash");
    actionDescriptions.put(0x008, "Freeze character (Can be thrown out)");

    actionDescriptions.put(0x013, "Start Jumping");
    actionDescriptions.put(0x014, "Jumping Up");
    actionDescriptions.put(0x015, "Jumping Forward");
    actionDescriptions.put(0x016, "Jumping Backwards");
    actionDescriptions.put(0x017, "Jump");
    actionDescriptions.put(0x018, "Jump forward");
    actionDescriptions.put(0x019, "Jump backwards");
    actionDescriptions.put(0x01A, "JB");
    actionDescriptions.put(0x01B, "JB");
    actionDescriptions.put(0x01C, "JB");
    actionDescriptions.put(0x01D, "End Jumping");
    actionDescriptions.put(0x01E, "R");
    actionDescriptions.put(0x01F, "L");
    actionDescriptions.put(0x020, "R (Dashing)");
    actionDescriptions.put(0x021, "L (Dashing)");
    actionDescriptions.put(0x022, "Land");
    actionDescriptions.put(0x023, "JB");
    actionDescriptions.put(0x024, "Freeze character (Can be thrown out)");
    actionDescriptions.put(0x025, "Y-cancel");
    actionDescriptions.put(0x026, "Hit out of air");
    actionDescriptions.put(0x027, "Freeze character (Can be thrown out)");
    actionDescriptions.put(0x028, "Intro Animation");
    actionDescriptions.put(0x029, "Disable all your attacks except throw");
    actionDescriptions.put(0x02A, "Win Animation (Disable all your attacks)");
    actionDescriptions.put(0x02B, "Win Animation (Including camera,  softlocks)");
    actionDescriptions.put(0x02C, "Win Animation (Including camera,  softlock");
    actionDescriptions.put(0x02D, "Missing animation? (softlock)");
    actionDescriptions.put(0x02E, "Lose round animation (softlock character)");
    actionDescriptions.put(0x02F,
        "Intro animation? (Can be thrown out,  only throws will work after)");
    actionDescriptions.put(0x030, "Block soft hit (e.g. by Naruto 5B)");
    actionDescriptions.put(0x031, "Block medium hit (e.g. by Naruto 6B)");
    actionDescriptions.put(0x032, "Crash");
    actionDescriptions.put(0x033, "Block in Air");
    actionDescriptions.put(0x034, "Crash");

    actionDescriptions.put(0x036, "Crash");
    actionDescriptions.put(0x037, "Block hard hit and slide (e.g. by Naruto 4B)");
    actionDescriptions.put(0x038, "Crash");
    actionDescriptions.put(0x039, "Getting hit animation");
    actionDescriptions.put(0x03A, "Getting hit animation");
    actionDescriptions.put(0x03B, "Getting hit animation");
    actionDescriptions.put(0x03C, "Getting hit animation");
    actionDescriptions.put(0x03D, "Getting hit animation");
    actionDescriptions.put(0x03E, "Getting hit animation");
    actionDescriptions.put(0x03F, "Getting hit animation");
    actionDescriptions.put(0x040, "Getting hit (e.g. by Naruto 5B)");
    actionDescriptions.put(0x041, "Getting hit animation");
    actionDescriptions.put(0x042, "Getting hit animation");
    actionDescriptions.put(0x043, "Getting hit animation");
    actionDescriptions.put(0x044, "Getting hit animation");
    actionDescriptions.put(0x045, "Getting hit animation");
    actionDescriptions.put(0x046, "Hard knockdown animation");
    actionDescriptions.put(0x047, "Hard knockdown animation");
    actionDescriptions.put(0x048, "Round loss animation");
    actionDescriptions.put(0x049, "Hard knockdown animation");
    actionDescriptions.put(0x04A, "Hard knockdown animation");
    actionDescriptions.put(0x04B, "Hit with Special (lose all chakra)");
    actionDescriptions.put(0x04C,
        "Hit with hiki2 (Sink into the ground and fall from above. Ex, Shika 2X)");
    actionDescriptions.put(0x04D,
        "Hit with hiki (Sink into the ground and pop out. Ex, Jiraya 2A)");
    actionDescriptions.put(0x04E, "Hit with mato2 (Trapped. Ex, Shino 2A)");
    actionDescriptions.put(0x04F, "Hit hard");
    actionDescriptions.put(0x050, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x051, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x052, "Hit with yoro2 (Feet trapped. Ex, Kidomaru 5A1C)");
    actionDescriptions.put(0x053, "Hit hard");
    actionDescriptions.put(0x054, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x055, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x056, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x057, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x058, "Hit up in the air (e.g. by Naruto 8B)");
    actionDescriptions.put(0x059, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x05A, "Hit and stuck in ground (Can be hit out)");
    actionDescriptions.put(0x05B, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x05C, "Hit hard");
    actionDescriptions.put(0x05D, "Hard knockdown");
    actionDescriptions.put(0x05E,
        "Hit with Furi (Turns opponents around on hit. Leftover from Bloody Roar; not used.)");
    actionDescriptions.put(0x05F,
        "Hit with Furi (Turns opponents around on hit. Leftover from Bloody Roar; not used.)");
    actionDescriptions.put(0x060, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x061, "Hard knockdown");

    actionDescriptions.put(0x064, "Crash");
    actionDescriptions.put(0x065, "Guard Break");
    actionDescriptions.put(0x066, "Landing on ground");
    actionDescriptions.put(0x067, "Laying on ground");
    actionDescriptions.put(0x068, "Getting hit");
    actionDescriptions.put(0x069, "Hard knockdown");
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

    actionDescriptions.put(0x08C, "Hit hard");
    actionDescriptions.put(0x08D, "Tripping");
    actionDescriptions.put(0x08E, "Hard knockdown bounce");
    actionDescriptions.put(0x08F, "Hard knockdown bounce");
    actionDescriptions.put(0x090, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x091, "Hit and stuck in air (Can be hit out)");
    actionDescriptions.put(0x092, "Hard knockdown slow bounce");
    actionDescriptions.put(0x093, "Hard knockdown slow bounce");
    actionDescriptions.put(0x094, "Crash");
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
    actionDescriptions.put(0x0A4, "DB");
    actionDescriptions.put(0x0A5, "DB");

    actionDescriptions.put(0x0B0, "5A");
    actionDescriptions.put(0x0B1, "6A");
    actionDescriptions.put(0x0B2, "4A");
    actionDescriptions.put(0x0B3, "2A");
    actionDescriptions.put(0x0B4, "DA");
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

    actionDescriptions.put(0x170, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x171, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x172, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x173, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x174, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x175, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x176, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x177, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x178, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x179, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17A, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17B, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17C, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17D, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17E, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x17F, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x180, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x181, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x182, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x183, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x184, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x185, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x186, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x187, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x188, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x189, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18A, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18B, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18C, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18D, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18E, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x18F, "(UNUSED - Points to chr_act)");
    actionDescriptions.put(0x190, "Ground Throw");
    actionDescriptions.put(0x191, "Back Ground Throw");
    actionDescriptions.put(0x192, "Air Throw");
    actionDescriptions.put(0x193, "Activated X");
    actionDescriptions.put(0x194, "Crash");
    actionDescriptions.put(0x195, "Activated X (Transformation)");
    actionDescriptions.put(0x196, "Activated 2X");
    actionDescriptions.put(0x197, "Crash");

    actionDescriptions.put(0x1D0, "Activated 3-Man X #1");
    actionDescriptions.put(0x1D1, "Activated 3-Man X #2");
    actionDescriptions.put(0x1D2, "Activated 3-Man X #3");
    actionDescriptions.put(0x1D3, "Activated 3-Man X #4");

    actionDescriptions.put(0x1D5, "Activated 3-Man X #5");

    actionDescriptions.put(0x1DF, "Activated 3-Man X #6");
    actionDescriptions.put(0x1E0, "Getting thrown");
    actionDescriptions.put(0x1E1, "Getting thrown");
    actionDescriptions.put(0x1E2, "Thrown on the ground");
    actionDescriptions.put(0x1E3, "Hit by super");

    actionDescriptions.put(0x1E5, "Hit by small attack");
    actionDescriptions.put(0x1E6, "Hit by super");

    actionDescriptions.put(0x223, "Getting hit by super");

    actionDescriptions.put(0x225, "Getting hit by super");

    return actionDescriptions;
  }
}
