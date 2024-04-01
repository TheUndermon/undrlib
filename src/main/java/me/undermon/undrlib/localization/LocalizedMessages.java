/*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

package me.undermon.undrlib.localization;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bukkit.plugin.Plugin;

public class LocalizedMessages {
	private static final String BASE_NAME = "messages";
	private static final String EXTENSION = "properties";
	private final Plugin plugin;
	private URLClassLoader classloader;

	public LocalizedMessages(Plugin plugin, List<Locale> locales) {
		this.plugin = plugin;

		this.saveToDataFolderIfAbsent("%s.%s".formatted(BASE_NAME, EXTENSION));

		for (Locale locale : locales) {
			this.saveToDataFolderIfAbsent("%s_%s.%s".formatted(BASE_NAME, locale, EXTENSION));
		}

		this.classloader = getClassloader();
	}

	private void saveToDataFolderIfAbsent(String file) {
		Path filePath = Paths.get(this.plugin.getDataFolder().getAbsolutePath(), file);
		
		if (!Files.exists(filePath)) {
			this.plugin.saveResource(file, false);
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
