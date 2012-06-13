package com.almuramc.backpack.spout.config;

import java.io.File;

import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class BackpackConfiguration extends ConfigurationHolderConfiguration {
	public static final ConfigurationHolder USE_SQL = new ConfigurationHolder(false, "general", "use-sql");

	public BackpackConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "config.yml")));
	}
}
