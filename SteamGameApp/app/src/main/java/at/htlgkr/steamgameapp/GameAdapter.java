package at.htlgkr.steamgameapp;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import at.htlgkr.steam.Game;

public class GameAdapter extends BaseAdapter {


    private int listViewItemLayoutId;
    private List<Game> games;
    private LayoutInflater inflater;


    public GameAdapter(Context context, int listViewItemLayoutId, List<Game> games) {
        this. listViewItemLayoutId = listViewItemLayoutId;
        this.games = games;
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
}
