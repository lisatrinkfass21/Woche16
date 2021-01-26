package at.htlgkr.steam;


import android.text.method.SingleLineTransformationMethod;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SteamBackend {

    private List<Game> games = new ArrayList<>();

    public SteamBackend() {
        // Implementieren Sie diesen Konstruktor.
    }

    public void loadGames(InputStream inputStream) {
        SimpleDateFormat sdf = new SimpleDateFormat(Game.DATE_FORMAT);
        try {
            games.clear();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                String[] spiele = line.split(";");
                Date date = sdf.parse(spiele[1]);
                double price = Double.parseDouble(spiele[2]);
                Game game = new Game(spiele[0], date, price);
                this.games.add(game);
            }
        } catch (IOException e) {
            System.out.println("Lesen einer Zeile hat nicht funktioniert");
        } catch (ParseException e) {
            System.out.println("Date parsen hat nicht funktioniert");
        }
    }

    public void store(OutputStream fileOutputStream) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {
            for (int i = 0; i < games.size(); i++) {
                bw.write(games.get(i).serialize());
                bw.newLine();
            }
        } catch (IOException e) {
        }
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games.clear();
        this.games.addAll(games);
    }

    public void addGame(Game newGame) {
        this.games.add(newGame);
    }

    public double sumGamePrices() {
        return games.stream()
                .mapToDouble(a -> a.getPrice())
                .sum();

    }

    public double averageGamePrice() {
        //   return sumGamePrices()/this.games.size();
        return games.stream()
                .mapToDouble(a -> a.getPrice())
                .average().getAsDouble();
    }

    public List<Game> getUniqueGames() {
        return games.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Game> selectTopNGamesDependingOnPrice(int n) {
        return games.stream()
                .sorted((Game a, Game b) -> Double.compare(b.getPrice(), a.getPrice()))
                .limit(n).collect(Collectors.toList());

    }
}
