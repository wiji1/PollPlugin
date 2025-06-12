package dev.wiji.features.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum PluginSound {

	CLICK(Sound.UI_BUTTON_CLICK, 1.0f, 1.4f),
	SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.4f),
	ERROR(Sound.ENTITY_VILLAGER_NO, 1.0f, 1.4f),
	;

	private final Sound sound;
	private final float volume;
	private final float pitch;

	PluginSound(Sound sound, float volume, float pitch) {
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}

	public Sound getSound() {
		return sound;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void play(Player player) {
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
}
