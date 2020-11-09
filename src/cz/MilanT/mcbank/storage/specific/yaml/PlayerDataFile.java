package cz.MilanT.mcbank.storage.specific.yaml;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Class for storage player data to YAML files (one player = one file)
 */
public class PlayerDataFile {
    private final String playerName;
    private final File file;
    private FileConfiguration fileConfiguration;

    public PlayerDataFile(String playerName, File file, FileConfiguration fileConfiguration) {
        this.playerName = playerName;
        this.file = file;
        this.fileConfiguration = fileConfiguration;
    }

    public void setFileConfiguration(FileConfiguration fileConfiguration) { this.fileConfiguration = fileConfiguration; }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public String getPlayerName() {
        return playerName;
    }
}
