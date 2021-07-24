package me.brecher.blackjack.server.scoring;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.*;
import java.util.Map;

public class ScoreSaverImpl implements ScoreSaver {
    private String saveFile;

    @Inject
    ScoreSaverImpl(@Named("SaveFile") String saveFile) {
        this.saveFile = saveFile;
    }

    @Override
    public void save(Map<Integer, Long> scoreMap) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));

            oos.writeObject(scoreMap);

            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Integer, Long> load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));

            Map<Integer, Long> scores =(Map<Integer,Long>)ois.readObject();

            ois.close();

            return scores;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean saveExists() {
        File checkFile = new File(saveFile);

        System.out.println(checkFile.getAbsolutePath());
        return checkFile.exists();
    }
}
