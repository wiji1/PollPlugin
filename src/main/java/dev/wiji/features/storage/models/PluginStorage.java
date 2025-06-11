package dev.wiji.features.storage.models;

import dev.wiji.features.poll.models.Poll;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class PluginStorage {

	public abstract void init();

	public abstract void loadPolls(Consumer<List<Poll>> pollConsumer);

	public abstract void savePolls(Supplier<List<Poll>> pollSupplier);

	public abstract void close();
}
