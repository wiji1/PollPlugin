package dev.wiji;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.apache.maven.model.Repository;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;


public class PollPluginLoader implements PluginLoader {
	@Override
	public void classloader(PluginClasspathBuilder pluginClasspathBuilder) {

		MavenLibraryResolver resolver = new MavenLibraryResolver();
		resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());
		resolver.addDependency(new Dependency(new DefaultArtifact("redis.clients:jedis:6.0.0"), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("com.google.code.gson:gson:2.10.1"), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.xerial:sqlite-jdbc:3.46.0.0"), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("mysql:mysql-connector-java:8.0.24"), null));

		pluginClasspathBuilder.addLibrary(resolver);

	}
}
