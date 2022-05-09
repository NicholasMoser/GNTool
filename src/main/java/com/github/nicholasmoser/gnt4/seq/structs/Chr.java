package com.github.nicholasmoser.gnt4.seq.structs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Chr {
  private static Map<Integer, String> OFFSET_TO_FIELD;

  public static Optional<String> getField(int offset) {
    if (OFFSET_TO_FIELD == null) {
      OFFSET_TO_FIELD = initOffsetToField();
    }
    return Optional.ofNullable(OFFSET_TO_FIELD.get(offset));
  }

  private static Map<Integer, String> initOffsetToField() {
    Map<Integer, String> offsetToField = new HashMap<>();
    offsetToField.put(0x0, "flags");
    offsetToField.put(0x4, "player_id");
    offsetToField.put(0x10, "cpu_flags");
    offsetToField.put(0x14, "player_id_2");
    offsetToField.put(0x18, "player_id_3");
    offsetToField.put(0x1C, "chr_id");
    offsetToField.put(0x30, "health_multiplier");
    offsetToField.put(0x38, "seq_struct");
    offsetToField.put(0x3C, "foe_seq_struct");
    offsetToField.put(0x40, "foe_chr_p");
    offsetToField.put(0x110, "movement_stuff");
    offsetToField.put(0x114, "animation_stuff");
    offsetToField.put(0x118, "knockback_function_curve_1");
    offsetToField.put(0x11C, "knockback_function_curve_2");
    offsetToField.put(0x120, "knockback_speed");
    offsetToField.put(0x124, "mf_flags");
    offsetToField.put(0x128, "af_flags");
    offsetToField.put(0x12C, "pf_flags");
    offsetToField.put(0x130, "nf_flags");
    offsetToField.put(0x134, "n2f_flags");
    offsetToField.put(0x138, "kf_flags");
    offsetToField.put(0x13C, "k2f_flags");
    offsetToField.put(0x140, "df_flags");
    offsetToField.put(0x144, "d2f_flags");
    offsetToField.put(0x148, "ef_flags");
    offsetToField.put(0x14C, "rf_flags");
    offsetToField.put(0x150, "cf_flags");
    offsetToField.put(0x154, "chr_modifier_flag");
    offsetToField.put(0x15C, "active_hitbox_check");
    offsetToField.put(0x164, "combo_count_1");
    offsetToField.put(0x166, "combo_count_2");
    offsetToField.put(0x19C, "x_pos");
    offsetToField.put(0x1A0, "y_pos");
    offsetToField.put(0x1A4, "z_pos");
    offsetToField.put(0x1BC, "facing_opponent");
    offsetToField.put(0x1CC, "movement_flags");
    offsetToField.put(0x1D4, "horizontal_push_speed");
    offsetToField.put(0x1D8, "horizontal_push_acceleration");
    offsetToField.put(0x1DC, "vertical_push_speed");
    offsetToField.put(0x1E0, "vertical_push_acceleration");
    offsetToField.put(0x1E8, "horizontal_air_speed");
    offsetToField.put(0x1F4, "knockback_speed");
    offsetToField.put(0x1F8, "knockback_acceleration");
    offsetToField.put(0x230, "chr_tbl");
    offsetToField.put(0x234, "chr_cam");
    offsetToField.put(0x238, "act_counter_difference");
    offsetToField.put(0x23C, "act_id");
    offsetToField.put(0x240, "act_id_2");
    offsetToField.put(0x258, "act_counter");
    offsetToField.put(0x25C, "current_recoverable_damage");
    offsetToField.put(0x260, "current_damage");
    offsetToField.put(0x264, "current_rec");
    offsetToField.put(0x26C, "health_frame_counter");
    offsetToField.put(0x27C, "max_damage");
    offsetToField.put(0x280, "new_damage");
    offsetToField.put(0x288, "last_damage");
    offsetToField.put(0x28C, "current_chakra");
    offsetToField.put(0x290, "new_chakra");
    offsetToField.put(0x294, "current_block_guard");
    offsetToField.put(0x298, "max_block_guard");
    offsetToField.put(0x29C, "GRD");
    offsetToField.put(0x2A0, "confusion_flag");
    offsetToField.put(0x2A4, "confusion_timer");
    offsetToField.put(0x2A8, "idle_counter_2");
    offsetToField.put(0x2AC, "hitbox_removal_timer");
    offsetToField.put(0x2B0, "hitbox_appearance_timer");
    offsetToField.put(0x2B4, "sync_timer_after_hitbox");
    offsetToField.put(0x2B8, "attack_angle");
    offsetToField.put(0x2BC, "POW");
    offsetToField.put(0x2C0, "ANG");
    offsetToField.put(0x2C4, "atk_multiplier");
    offsetToField.put(0x2C8, "DMG");
    offsetToField.put(0x2D0, "REV");
    offsetToField.put(0x2D4, "REV2");
    offsetToField.put(0x2D8, "block_stun");
    offsetToField.put(0x2DC, "stand_up_timer");
    offsetToField.put(0x2EC, "inactionable_timer");
    offsetToField.put(0x2F0, "intangible_timer");
    offsetToField.put(0x2F8, "grab_break_counter");
    offsetToField.put(0x54C, "active_throws");
    offsetToField.put(0x7D8, "starting_string_offset");
    offsetToField.put(0x7E2, "next_attack_id");
    offsetToField.put(0x854, "transformation_flag");
    offsetToField.put(0x87C, "sf_flags");
    offsetToField.put(0x894, "air_fall_combo_counter");
    offsetToField.put(0x8C8, "synchronous_timer");
    offsetToField.put(0x954, "throw_target");
    offsetToField.put(0x960, "foe_target");
    offsetToField.put(0x974, "foe_struct");
    return offsetToField;
  }
}
