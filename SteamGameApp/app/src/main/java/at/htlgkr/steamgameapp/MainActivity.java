package at.htlgkr.steamgameapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.ReportType;
import at.htlgkr.steam.SteamBackend;

public class MainActivity extends AppCompatActivity {
    private static final String GAMES_CSV = "games.csv";

    private List<Game> games = new ArrayList<>();
    private ListView mListView;
    private GameAdapter mAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.gamesList);
        loadGamesIntoListView();
        mAdap = new GameAdapter(this, R.layout.game_item_layout, games);
        mListView.setAdapter(mAdap);


        setUpReportSelection();
        setUpSearchButton();
        setUpAddGameButton();
        setUpSaveButton();
    }

    private void loadGamesIntoListView() {
        try {
            SteamBackend sb = new SteamBackend();
            FileInputStream fis = openFileInput(MainActivity.GAMES_CSV);
            sb.loadGames(fis);
            games = sb.getGames();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setUpReportSelection() {
        // Implementieren Sie diese Methode.
    }

    private void setUpSearchButton() {
        // Implementieren Sie diese Methode.
    }

    private void setUpAddGameButton() {
        // Implementieren Sie diese Methode.
    }

    private void setUpSaveButton() {
        // Implementieren Sie diese Methode.
    }

    public void search(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(SteamGameAppConstants.ENTER_SEARCH_TERM);
        final EditText mTextView = new EditText(this);
        mTextView.setId(R.id.dialog_search_field);
        alert.setView(mTextView);
        alert.setPositiveButton("search", new View.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //noch zu programmieren
            }
        });
        alert.setNegativeButton("cancel", null);

    }

    public void addGame(View view){

    }

    public void save(View view){
        try {
            FileOutputStream fos = openFileOutput(SteamGameAppConstants.SAVE_GAMES_FILENAME, MODE_PRIVATE );
            SteamBackend sb = new SteamBackend();
            sb.store();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
