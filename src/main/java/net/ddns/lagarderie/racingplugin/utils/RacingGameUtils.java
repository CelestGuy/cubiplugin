package net.ddns.lagarderie.racingplugin.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.PowerBlock;
import net.ddns.lagarderie.racingplugin.game.PowerBlocksManager;
import net.ddns.lagarderie.racingplugin.game.RacingGame;
import net.ddns.lagarderie.racingplugin.game.Track;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RacingGameUtils {
    public static void loadGameParameters() {
        File gameParametersFile = new File("./plugins/racing/game_parameters.json");

        if (gameParametersFile.exists()) {
            try {
                FileReader reader = new FileReader(gameParametersFile);

                Gson gson = new Gson();
                RacingGame.setInstance(gson.fromJson(reader, RacingGame.class));

                reader.close();
            } catch (IOException e) {
                System.err.println("Impossible de charger le fichier game_parameters.json : " + e);
            }
        }
    }

    public static void saveGameParameters() {
        File gameParametersFile = new File("./plugins/racing/game_parameters.json");

        if (gameParametersFile.exists()) {
            if (gameParametersFile.delete()) {
                System.out.println("Le fichier des parametres de jeu a ete supprime");
            } else {
                System.err.println("Impossible de supprimer le fichier des parametres de jeu");
            }
        }

        try {
            Gson jo = new Gson();
            String json = jo.toJson(RacingGame.getInstance());
            FileWriter file = new FileWriter(gameParametersFile.getPath());

            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            System.err.println("Impossible de charger le fichier game_parameters.json : " + e);
        }
    }

    public static ArrayList<PowerBlock> loadPowerBlocks() {
        ArrayList<PowerBlock> powerBlocks = new ArrayList<>();

        File powerBlocksFile = new File("./plugins/racing/power_blocks.json");

        if (powerBlocksFile.exists()) {
            try {
                FileReader reader = new FileReader(powerBlocksFile);

                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<PowerBlock>>(){}.getType();

                powerBlocks = gson.fromJson(reader, arrayListType);

                reader.close();
            } catch (IOException e) {
                System.err.println("Impossible de charger le fichier power_blocks.json : " + e);
            }
        }

        return powerBlocks;
    }

    public static void savePowerBlocks() {
        File powerBlocksFile = new File("./plugins/racing/power_blocks.json");

        if (powerBlocksFile.exists()) {
            if (powerBlocksFile.delete()) {
                System.out.println("Le fichier des PowerBlocks a ete supprime");
            } else {
                System.err.println("Impossible de supprimer le fichier des PowerBlocks");
            }
        }

        try {
            Gson jo = new Gson();
            String json = jo.toJson(PowerBlocksManager.getInstance().getPowerBlocks());
            FileWriter file = new FileWriter(powerBlocksFile.getPath());

            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            System.err.println("Impossible de charger le fichier power_blocks.json : " + e);
        }
    }

    public static ArrayList<Track> loadTracks() {
        ArrayList<Track> tracks = new ArrayList<>();

        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File trackFile : files) {
                try {
                    FileReader reader = new FileReader(trackFile);

                    Gson gson = new Gson();
                    Track track = gson.fromJson(reader, Track.class);
                    tracks.add(track);

                    reader.close();
                } catch (IOException e) {
                    System.err.println("Impossible de charger la course " + trackFile.getName() + ": " + e);
                }
            }
        } else {
            System.err.println("Aucun fichier de courses dans le dossier");
        }

        return tracks;
    }

    public static void saveTrack(Track track) {
        File trackFile = new File("./plugins/racing/tracks/" + track.getId() + ".json");

        if (trackFile.exists()) {
            if (trackFile.delete()) {
                System.out.println("Fichier " + trackFile.getName() + " supprime");
            } else {
                System.err.println("Impossible de supprimer le fichier de course");
            }
        }

        try {
            Gson jo = new Gson();
            String json = jo.toJson(track);
            FileWriter file = new FileWriter(trackFile.getPath());

            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveTracks(ArrayList<Track> tracks) {
        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Les fichiers de courses ont ete supprime");
                }
            }

            for (Track track : tracks) {
                saveTrack(track);
            }
        }
    }
}
