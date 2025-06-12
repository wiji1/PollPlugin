package dev.wiji.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {
	private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
			.tags(TagResolver.builder().resolver(StandardTags.defaults()).build())
			.strict(false)
			.emitVirtuals(false)
			.postProcessor((component) -> component.decoration(TextDecoration.ITALIC, false))
			.build();

	private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER =
			LegacyComponentSerializer.builder()
					.character('§')
					.hexColors()
					.useUnusualXRepeatedCharacterHexFormat()
					.build();

	public static TextComponent colorize(String message) {
		if (message == null) return Component.text("null text");

		if (message.contains("<") && message.contains(">")) {
			return (TextComponent) MINI_MESSAGE.deserialize(message);
		} else {
			message = message.replace("&", "§");
			Component legacyComponent = LEGACY_COMPONENT_SERIALIZER.deserialize(message);
			return (TextComponent) legacyComponent.decoration(TextDecoration.ITALIC, false);
		}
	}
}