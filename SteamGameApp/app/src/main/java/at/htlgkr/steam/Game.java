package at.htlgkr.steam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Game {
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private String name = "";
    private Date releaseDate;
    private double price = 0.0;

    public Game() {
        // dieser Konstruktor muss existieren
    }

    public Game(String name, Date releaseDate, double price) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price=price;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(this.DATE_FORMAT);
        return "["+sdf.format(this.releaseDate)+"] "+ this.name + " "+this.price ;
    }

    @Override
    public boolean equals(Object o) {
        Game g = (Game) o;
        if(this.name.equals(g.name)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
       return Objects.hash(name, releaseDate, price);
    }
}

