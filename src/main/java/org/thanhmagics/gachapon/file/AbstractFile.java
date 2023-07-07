package org.thanhmagics.gachapon.file;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public abstract class AbstractFile {

    public abstract void load();

    public abstract FileConfiguration getConfig();

    public abstract void save();
}
