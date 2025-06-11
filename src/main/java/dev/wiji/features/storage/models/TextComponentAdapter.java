package dev.wiji.features.storage.models;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.lang.reflect.Type;

public class TextComponentAdapter implements JsonSerializer<TextComponent>, JsonDeserializer<TextComponent> {
	private final GsonComponentSerializer serializer = GsonComponentSerializer.gson();

	@Override
	public JsonElement serialize(TextComponent src, Type typeOfSrc, JsonSerializationContext context) {
		String json = serializer.serialize(src);
		return JsonParser.parseString(json);
	}

	@Override
	public TextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Component component = serializer.deserialize(json.toString());
		return (TextComponent) component;
	}
}
