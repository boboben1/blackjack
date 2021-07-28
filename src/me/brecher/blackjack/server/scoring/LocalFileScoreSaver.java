package me.brecher.blackjack.server.scoring;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LocalFileScoreSaver<K, V> implements ScoreSaver<K, V> {
    private String saveFile;

    private final Class<? extends K> keyClass;
    private final Class<? extends V> valueClass;

    private final Map<K, V> scores;

    private final V defaultMoney;

    @Inject @SuppressWarnings("unchecked")
    LocalFileScoreSaver(@Named("SaveFile") String saveFile, @Named("DefaultMoney") V defaultMoney, TypeLiteral<K> keyType, TypeLiteral<V> valueType) {
        this.saveFile = saveFile;
        this.defaultMoney = defaultMoney;

        this.keyClass = (Class<? extends K>) keyType.getRawType();

        this.valueClass = (Class<? extends V>) valueType.getRawType();

        this.scores = load();
    }

    private void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));

            oos.writeObject(scores);

            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<K, V> load() {

        Map<K, V> output = new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));)
        {
            Map scores =(Map)ois.readObject();

            for (Object key : scores.keySet()) {
                if (key == null || keyClass.isAssignableFrom(key.getClass())) {
                    Object value = scores.get(key);
                    if (value == null || valueClass.isAssignableFrom(value.getClass())) {
                        output.put(keyClass.cast(key), valueClass.cast(value));
                    } else {
                        throw new AssertionError("ScoreSaverImpl<" + keyClass.getSimpleName()
                                + ", " + valueClass.getSimpleName() + ">::load ERROR: value " + value
                                + " is not a " + valueClass.getSimpleName() + ".");
                    }
                } else {
                    throw new AssertionError("ScoreSaverImpl<" + keyClass.getSimpleName()
                            + ", " + valueClass.getSimpleName() + ">::load ERROR: key " + key
                            + " is not a " + keyClass.getSimpleName() + ".");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
        } finally {
            return output;
        }
    }

    protected boolean saveExists() {
        File checkFile = new File(saveFile);

        System.out.println(checkFile.getAbsolutePath());
        return checkFile.exists();
    }

    @Override
    public V getScoreForPlayer(K player) {
        return this.scores.getOrDefault(player, defaultMoney);
    }

    @Override
    public void putScoreForPlayer(K player, V score) {
        this.scores.put(player, score);

        save();
    }
}
