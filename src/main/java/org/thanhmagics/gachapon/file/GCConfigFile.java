package org.thanhmagics.gachapon.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.thanhmagics.gachapon.Gachapon;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class GCConfigFile extends AbstractFile{

    private File file;
    private FileConfiguration config;

    @Override
    public void load() {
        this.file = new File(Gachapon.getInstance().getDataFolder(), "GachaConfig.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Gachapon.getInstance().saveResource("GachaConfig.yml",true);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public FileConfiguration getConfig() {
        return config != null ? config : YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
