package cz.MilanT.mcbank.storage.specific;

import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.system.player.Account;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class YAMLStorage implements IStorage {
    private final HashMap<String, FileConfiguration> playersFilesMap;
    private final File folder;

    public YAMLStorage(Plugin plugin) {
        playersFilesMap = new HashMap<>();
        folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "players");
        if(!folder.exists()) folder.mkdirs();
        File[] filesInFolder = folder.listFiles();
        if(filesInFolder != null) {
            for(File fileInFolder:filesInFolder) {
                String fileName = fileInFolder.getName();
                if(!playersFilesMap.containsKey(fileName))
                    playersFilesMap.put(fileInFolder.getName(), YamlConfiguration.loadConfiguration(fileInFolder));
            }
        }
    }

    @Override
    public Account getPlayerAccount(String name) {
        FileConfiguration fileConfiguration = playersFilesMap.get(name);
        return new Account(name, fileConfiguration.getDouble("balance"));
    }

    @Override
    public boolean hasPlayerAccount(String name) {
        return playersFilesMap.containsKey(name);
    }

    @Override
    public boolean createPlayerAccount(Account account) throws IOException {
        String nickname = account.getNickname();
        if(!playersFilesMap.containsKey(nickname)) {
            File playerFile = new File(folder.getAbsolutePath() + File.separator + nickname + ".yml");
            if(!playerFile.exists()) {
                if(!playerFile.createNewFile()) return false;
            }
            playersFilesMap.put(nickname, YamlConfiguration.loadConfiguration(playerFile));
        }

        FileConfiguration fileConfiguration = playersFilesMap.get(nickname);
        fileConfiguration.set("nickname", nickname);
        fileConfiguration.set("balance", account.getBalance());

        playersFilesMap.put("nickname", fileConfiguration);

        return true;
    }

    @Override
    public void setPlayerBalance(String name, double balance) {
        playersFilesMap.get(name).set("balance", balance);
    }

    public void onPlayerQuit(String nick) throws IOException {
        playersFilesMap.get(nick)
                .save(nick + ".yml");
        playersFilesMap.remove(nick);
    }

    @Override
    public void onDisable() {
        playersFilesMap.clear();
    }
}
