package com.github.nicholasmoser.gnt4;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class GNT4Stages {

  public static final String KONOHA_HOT_SPRINTS = "Konoha Hot Springs";
  public static final String SUNAGAKURE = "Sunagakure";
  public static final String DEEP_FOREST_EMERGENCY = "Deep Forest Emergence";
  public static final String TANZAKU_CASTLE = "Tanzaku Castle";
  public static final String NINE_TAILED_FOX_ROOM = "Nine-Tailed Fox Room";
  public static final String THREE_WAY_DEADLOCK = "Three-Way Deadlock";
  public static final String AMID_TOADS_1 = "Amid Toads: 1";
  public static final String ACADEMY_ROOFTOP_DAY = "Academy Rooftop: Day";
  public static final String ICHIRAKU_RAMEN_NIGHT = "Ichiraku Ramen: Night";
  public static final String KONOHA_GATE_DAY = "Konoha Gate: Day";
  public static final String ACADEMY_SCHOOLYARD_SUNSET = "Academy Schoolyard: Sunset";
  public static final String FOREST_OF_DEATH_NIGHT = "Forest of Death: Night";
  public static final String GREAT_NARUTO_BRIDGE_CLEAR = "Great Naruto Bridge: Clear";
  public static final String GREAT_NARUTO_BRIDGE_FOG = "Great Naruto Bridge: Fog";
  public static final String AMID_TOADS_2 = "Amid Toads: 2";
  public static final String ACADEMY_ROOFTOP_NIGHT = "Academy Rooftop: Night";
  public static final String ICHIRAKU_RAMEN_SUNSET = "Ichiraku Ramen: Sunset";
  public static final String KONOHA_GATE_NIGHT = "Konoha Gate: Night";
  public static final String ACADEMY_SCHOOLYARD_DAY = "Academy Schoolyard: Day";
  public static final String FOREST_OF_DEATH_MORNING = "Forest of Death: Morning";
  public static final String CHUNIN_EXAMS_QUALIFIERS = "Chunin Exams: Qualifiers";
  public static final String CHUNIN_EXAMS_FINALS = "Chunin Exams: Finals";
  public static final String CHUNIN_EXAMS_ROOFTOP = "Chunin Exams: Rooftop";
  public static final String KONOHA_HOSPITAL_ROOFTOPS = "Konoha Hospital Rooftops";
  public static final String KONOHA_SHRINE = "Konoha Shrine";
  public static final String KONOHA_POLICE_DEPARTMENT = "Konoha Police Department";
  public static final String ABANDONED_UCHIHA_DISTRICT = "Abandoned Uchiha District";
  public static final String SNOW_VALLEY = "Snow Valley";
  public static final String OROCHIMARUS_LAID = "Orochimaru's Lair";
  public static final String SHAMBLED_PLAINS = "Shambled Plains";
  public static final String FINAL_VALLEY = "Final Valley";
  public static final String RANDOM = "Random Select";

  public static final BiMap<Integer, String> STAGE_DISPLAY_IDS = new ImmutableBiMap.Builder<Integer, String>()
      .put(0x00000000, KONOHA_HOSPITAL_ROOFTOPS)
      .put(0x00000001, KONOHA_POLICE_DEPARTMENT)
      .put(0x00000002, SNOW_VALLEY)
      .put(0x00000003, FINAL_VALLEY)
      .put(0x00000004, GREAT_NARUTO_BRIDGE_FOG)
      .put(0x00000005, CHUNIN_EXAMS_FINALS)
      .put(0x00000006, THREE_WAY_DEADLOCK)
      .put(0x00000007, KONOHA_HOT_SPRINTS)
      .put(0x00000008, SUNAGAKURE)
      .put(0x00000009, KONOHA_SHRINE)
      .put(0x0000000A, AMID_TOADS_1)
      .put(0x0000000B, ACADEMY_ROOFTOP_DAY)
      .put(0x0000000C, ICHIRAKU_RAMEN_SUNSET)
      .put(0x0000000D, GREAT_NARUTO_BRIDGE_CLEAR)
      .put(0x0000000E, KONOHA_GATE_NIGHT)
      .put(0x0000000F, DEEP_FOREST_EMERGENCY)
      .put(0x00000010, ABANDONED_UCHIHA_DISTRICT)
      .put(0x00000011, ACADEMY_SCHOOLYARD_SUNSET)
      .put(0x00000012, FOREST_OF_DEATH_NIGHT)
      .put(0x00000013, AMID_TOADS_2)
      .put(0x00000014, CHUNIN_EXAMS_QUALIFIERS)
      .put(0x00000015, ACADEMY_ROOFTOP_NIGHT)
      .put(0x00000016, TANZAKU_CASTLE)
      .put(0x00000017, OROCHIMARUS_LAID)
      .put(0x00000018, ICHIRAKU_RAMEN_NIGHT)
      .put(0x00000019, KONOHA_GATE_DAY)
      .put(0x0000001A, ACADEMY_SCHOOLYARD_DAY)
      .put(0x0000001B, CHUNIN_EXAMS_ROOFTOP)
      .put(0x0000001C, FOREST_OF_DEATH_MORNING)
      .put(0x0000001D, NINE_TAILED_FOX_ROOM)
      .put(0x0000001E, SHAMBLED_PLAINS)
      .put(0x0000001F, RANDOM)
      .build();

  public static final BiMap<Integer, String> STAGE_IDS = new ImmutableBiMap.Builder<Integer, String>()
      .put(0x00000001, AMID_TOADS_1)
      .put(0x00000002, ACADEMY_ROOFTOP_DAY)
      .put(0x00000003, ICHIRAKU_RAMEN_NIGHT)
      .put(0x00000004, KONOHA_GATE_DAY)
      .put(0x00000005, ACADEMY_SCHOOLYARD_SUNSET)
      .put(0x00000006, FOREST_OF_DEATH_NIGHT)
      .put(0x00000007, GREAT_NARUTO_BRIDGE_CLEAR)
      .put(0x00000008, GREAT_NARUTO_BRIDGE_FOG)
      .put(0x00000009, AMID_TOADS_2)
      .put(0x0000000A, ACADEMY_ROOFTOP_NIGHT)
      .put(0x0000000B, ICHIRAKU_RAMEN_SUNSET)
      .put(0x0000000C, KONOHA_GATE_NIGHT)
      .put(0x0000000D, ACADEMY_SCHOOLYARD_DAY)
      .put(0x0000000E, FOREST_OF_DEATH_MORNING)
      .put(0x0000000F, CHUNIN_EXAMS_QUALIFIERS)
      .put(0x00000010, CHUNIN_EXAMS_FINALS)
      .put(0x00000011, CHUNIN_EXAMS_ROOFTOP)
      // .put(0x00000012, "UNKNOWN") <- Unknown and invalid
      .put(0x00000013, KONOHA_HOT_SPRINTS)
      .put(0x00000014, SUNAGAKURE)
      .put(0x00000015, DEEP_FOREST_EMERGENCY)
      .put(0x00000016, TANZAKU_CASTLE)
      .put(0x00000017, NINE_TAILED_FOX_ROOM)
      .put(0x00000018, THREE_WAY_DEADLOCK)
      .put(0x00000019, KONOHA_HOSPITAL_ROOFTOPS)
      .put(0x0000001A, KONOHA_SHRINE)
      .put(0x0000001B, KONOHA_POLICE_DEPARTMENT)
      .put(0x0000001C, ABANDONED_UCHIHA_DISTRICT)
      .put(0x0000001D, SNOW_VALLEY)
      .put(0x0000001E, OROCHIMARUS_LAID)
      .put(0x0000001F, SHAMBLED_PLAINS)
      .put(0x00000020, FINAL_VALLEY)
      .build();
}
