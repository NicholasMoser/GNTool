package com.github.nicholasmoser.gnt4.seq.structs;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Map;
import java.util.Optional;

public class Chr {
  private static BiMap<Integer, String> OFFSET_TO_FIELD;
  private static Map<String, Integer> FIELD_TO_OFFSET;

  public static Optional<String> getField(int offset) {
    if (OFFSET_TO_FIELD == null) {
      OFFSET_TO_FIELD = initOffsetToField();
    }
    return Optional.ofNullable(OFFSET_TO_FIELD.get(offset));
  }

  public static Optional<Integer> getOffset(String field) {
    if (FIELD_TO_OFFSET == null) {
      FIELD_TO_OFFSET = initFieldToOffset();
    }
    return Optional.ofNullable(FIELD_TO_OFFSET.get(field));
  }

  private static BiMap<Integer, String> initOffsetToField() {
    return new ImmutableBiMap.Builder<Integer, String>()
      .put(0x0, "flags")
      .put(0x4, "player_id")
      .put(0x10, "cpu_flags")
      .put(0x14, "player_id_2")
      .put(0x18, "player_id_3")
      .put(0x1C, "chr_id")
      .put(0x20, "costume_id")
      .put(0x30, "health_multiplier")
      .put(0x38, "seq_struct")
      .put(0x3C, "foe_seq_struct")
      .put(0x40, "foe_chr_p")
      .put(0x110, "movement_stuff")
      .put(0x114, "animation_stuff")
      .put(0x118, "knockback_function_curve_1")
      .put(0x11C, "knockback_function_curve_2")
      .put(0x120, "knockback_speed1")
      .put(0x124, "mf_flags")
      .put(0x128, "af_flags")
      .put(0x12C, "pf_flags")
      .put(0x130, "nf_flags")
      .put(0x134, "n2f_flags")
      .put(0x138, "kf_flags")
      .put(0x13C, "k2f_flags")
      .put(0x140, "df_flags")
      .put(0x144, "d2f_flags")
      .put(0x148, "ef_flags")
      .put(0x14C, "rf_flags")
      .put(0x150, "cf_flags")
      .put(0x154, "chr_modifier_flag")
      .put(0x15C, "active_hitbox_check")
      .put(0x164, "combo_count_1")
      .put(0x166, "combo_count_2")
      .put(0x19C, "x_pos")
      .put(0x1A0, "y_pos")
      .put(0x1A4, "z_pos")
      .put(0x1BC, "facing_opponent")
      .put(0x1CC, "movement_flags")
      .put(0x1D4, "horizontal_push_speed")
      .put(0x1D8, "horizontal_push_acceleration")
      .put(0x1DC, "vertical_push_speed")
      .put(0x1E0, "vertical_push_acceleration")
      .put(0x1E8, "horizontal_air_speed")
      .put(0x1F4, "knockback_speed2")
      .put(0x1F8, "knockback_acceleration")
      .put(0x230, "chr_tbl")
      .put(0x234, "chr_cam")
      .put(0x238, "act_counter_difference")
      .put(0x23C, "act_id")
      .put(0x240, "act_id_2")
      .put(0x258, "act_counter")
      .put(0x25C, "current_recoverable_damage")
      .put(0x260, "current_damage")
      .put(0x264, "current_rec")
      .put(0x26C, "health_frame_counter")
      .put(0x27C, "max_damage")
      .put(0x280, "new_damage")
      .put(0x288, "last_damage")
      .put(0x28C, "current_chakra")
      .put(0x290, "new_chakra")
      .put(0x294, "current_block_guard")
      .put(0x298, "max_block_guard")
      .put(0x29C, "GRD")
      .put(0x2A0, "confusion_flag")
      .put(0x2A4, "confusion_timer")
      .put(0x2A8, "idle_counter_2")
      .put(0x2AC, "hitbox_removal_timer")
      .put(0x2B0, "hitbox_appearance_timer")
      .put(0x2B4, "sync_timer_after_hitbox")
      .put(0x2B8, "attack_angle")
      .put(0x2BC, "POW")
      .put(0x2C0, "ANG")
      .put(0x2C4, "atk_multiplier")
      .put(0x2C8, "DMG")
      .put(0x2D0, "REV")
      .put(0x2D4, "REV2")
      .put(0x2D8, "block_stun")
      .put(0x2DC, "stand_up_timer")
      .put(0x2EC, "inactionable_timer")
      .put(0x2F0, "intangible_timer")
      .put(0x2F8, "grab_break_counter")
      .put(0x3BE, "current_buttons_held")
      .put(0x54C, "active_throws")
      .put(0x7D8, "starting_string_offset")
      .put(0x7E2, "next_attack_id")
      .put(0x854, "transformation_flag")
      .put(0x87C, "sf_flags")
      .put(0x894, "air_fall_combo_counter")
      .put(0x8C8, "synchronous_timer")
      .put(0x954, "throw_target")
      .put(0x960, "foe_target")
      .put(0x974, "foe_struct")
      .build();
  }

  private static Map<String, Integer> initFieldToOffset() {
    if (OFFSET_TO_FIELD == null) {
      OFFSET_TO_FIELD = initOffsetToField();
    }
    return OFFSET_TO_FIELD.inverse();
  }
}
