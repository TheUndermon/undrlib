package me.undermon.undrlib;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bukkit.plugin.Plugin;

public class LocalizedMessages {
	private static final String BASE_NAME = "messages";
	private static final String EXTENSION = "properties";
	private final Plugin plugin;
	private URLClassLoader classloader;

	public LocalizedMessages(Plugin plugin) {
		this.plugin = plugin;

		this.saveLocaleFiles(new String[] {""});
		
		this.classloader = getClassloader();
	}

	private void saveLocaleFiles(String[] locales) {
		for (String locale : locales) {
			this.saveToDataFolderIfNotPresent(locale);
		}
	}

	private void saveToDataFolderIfNotPresent(String locale) {
		String localeFile = getLocaleFileName(locale);
		Path filePath = Paths.get(this.plugin.getDataFolder().getAbsolutePath(), localeFile);
		
		if (!Files.exists(filePath)) {
			this.plugin.saveResource(localeFile, false);
		}
	}

	private URLClassLoader getClassloader() {
		URL[] urls = new URL[1];

		try {
			urls[0] = this.plugin.getDataFolder().toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return new URLClassLoader(urls);
	}

	private String getLocaleFileName(String locale) {
		return (locale.isBlank()) ? "%s.%s".formatted(BASE_NAME, EXTENSION) : "%s_%s.%s".formatted(BASE_NAME, locale, EXTENSION); 
	}

	public final void reload() {
		this.classloader = getClassloader();
	}

	public final String get(String entry, Locale locale) {
		try {
			return ResourceBundle.getBundle(BASE_NAME, locale, this.classloader).getString(entry.toLowerCase());
		} catch (MissingResourceException e) {
			this.plugin.getSLF4JLogger().warn("Couldn't find message: %s".formatted(e.getKey()));
			return "<not found: %s>".formatted(e.getKey());
		}
	}

}
