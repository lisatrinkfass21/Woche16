package at.htlgkr.steamgameapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.htlgkr.steam.Game;

public class GameAdapter extends BaseAdapter implements Filterable {


    private int listViewItemLayoutId;
    private List<Game> games = new ArrayList<>();
    private List<Game> gamesFilter  =new ArrayList<>();
    private LayoutInflater inflater;


    public GameAdapter(Context context, int listViewItemLayoutId, List<Game> games) {
        this. listViewItemLayoutId = listViewItemLayoutId;
        this.games.addAll(games);
        this.gamesFilter.addAll(games);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View givenView, ViewGroup parent) {
        SimpleDateFormat sdf = new SimpleDateFormat(Game.DATE_FORMAT);
        Game game = games.get(position);
        View listItem = (givenView == null)?
                inflater.inflate(this.listViewItemLayoutId,null) : givenView;
        ((TextView) listItem.findViewById(R.id.name)).setText(game.getName());
        ((TextView) listItem.findViewById(R.id.date)).setText(sdf.format(game.getReleaseDate()));
        ((TextView) listItem.findViewById(R.id.price)).setText(String.valueOf(game.getPrice()));
        return listItem;
    }


    public Filter getFilter() { // für die Search-Methode in Mainactivity
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String input = charSequence.toString().toLowerCase(); //Groß und Kleinschreibung soll nicht beachtet werden

                if(input.equals("")){ // alle Elemente werden ausgegeben
                    filterResults.count = gamesFilter.size();
                    filterResults.values = gamesFilter;
                } else{
                    ArrayList<Game> filteredItems = new ArrayList<>();

                    for (int i = 0; i < gamesFilter.size(); i++) {// alle elemente die "input" im Namen haben werden angezeigt
                        if(gamesFilter.get(i).getName().toLowerCase().contains(input)){
                            filteredItems.add(gamesFilter.get(i));
                        }
                    }
                    filterResults.count = filteredItems.size();
                    filterResults.values = filteredItems;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                games = (ArrayList<Game>) filterResults.values; //gefilterten Elemente
                notifyDataSetChanged();//damit tests funktionieren
            }
        };

        return filter;
    }

    public void addGame(Game g){
        games.add(g);
        gamesFilter.add(g);
        notifyDataSetChanged(); //damit tests funktionieren
    }

}
