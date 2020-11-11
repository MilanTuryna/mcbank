package cz.MilanT.mcbank.storage.specific.yaml;

import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.system.player.Account;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class YAMLStorage implements IStorage {
    private final HashMap<String, PlayerDataFile> playersFilesMap;
    private final File folder;

    public YAMLStorage(Plugin plugin) {
        playersFilesMap = new HashMap<>();
        folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "players");
        if(!folder.exists()) folder.mkdirs();
        File[] filesInFolder = folder.listFiles();
        if(filesInFolder != null) {
            for(File fileInFolder:filesInFolder) {
                String fileName = FilenameUtils.removeExtension(fileInFolder.getName()).toLowerCase();
                if(!playersFilesMap.containsKey(fileName)) {
                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileInFolder);
                    if(!fileConfiguration.contains("nickname")) {
                        fileConfiguration.set("nickname", fileName);
                        fileConfiguration.set("balance", 0);
                    }
                    PlayerDataFile playerDataFile = new PlayerDataFile(fileName, fileInFolder,fileConfiguration);
                    playersFilesMap.put(fileName, playerDataFile);
                }

            }
        }
    }

    @Override
    public Account getPlayerAccount(String name) {
        PlayerDataFile playerDataFile = playersFilesMap.get(name.toLowerCase());
        FileConfiguration fileConfiguration = playerDataFile.getFileConfiguration();
        return new Account(name, fileConfiguration.getDouble("balance"));
    }

    @Override
    public boolean hasPlayerAccount(String name) {
        name = name.toLowerCase();
        return playersFilesMap.containsKey(name)
                || new File(folder.getAbsolutePath() + File.separator + name + ".yml").exists();
    }

    @Override
    public boolean createPlayerAccount(Account account) throws IOException {
        String nickname = account.getNickname().toLowerCase();
        if(!playersFilesMap.containsKey(nickname)) {
            File playerFile = new File(folder.getAbsolutePath() + File.separator + nickname + ".yml");
            if(!playerFile.exists()) {
                if(!playerFile.createNewFile()) return false;
            }
            PlayerDataFile playerDataFile = new PlayerDataFile(nickname, playerFile, YamlConfiguration.loadConfiguration(playerFile));
            playersFilesMap.put(nickname, playerDataFile);
        }

        PlayerDataFile playerDataFile = playersFilesMap.get(nickname);
        FileConfiguration fileConfiguration = playerDataFile.getFileConfiguration();
        fileConfiguration.set("nickname", nickname);
        fileConfiguration.set("balance", account.getBalance());
        playerDataFile.setFileConfiguration(fileConfiguration);

        playersFilesMap.replace(nickname, playerDataFile);

        return true;
    }

    @Override
    public void setPlayerBalance(String name, double balance) {
        playersFilesMap.get(name.toLowerCase()).getFileConfiguration().set("balance", balance);
    }

    public void onPlayerQuit(String nick) throws IOException {
        nick = nick.toLowerCase();
        PlayerDataFile playerDataFile = playersFilesMap.get(nick);
        playerDataFile.getFileConfiguration()
                .save(playerDataFile.getFile());
        playersFilesMap.remove(nick);
    }

    @Override
    public void onDisable() throws IOException {
        if(!playersFilesMap.isEmpty()) {
            for(PlayerDataFile playerDataFile:playersFilesMap.values())
            {
                FileConfiguration fileConfiguration = playerDataFile.getFileConfiguration();
                String nickname = fileConfiguration.getString("nickname");
                if(nickname == null) {
                    fileConfiguration.set("nickname", playerDataFile.getPlayerName().toLowerCase());
                    fileConfiguration.set("balance", 0.0);
                }
                fileConfiguration.save(playerDataFile.getFile());
            }
        }
        playersFilesMap.clear();
    }
}
