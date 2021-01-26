package at.htlgkr.steamgameapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String GAMES_CSV = "games.csv";


    private ListView listView;
    private GameAdapter listViewAdapter;

    private SteamBackend sb = new SteamBackend();


    private ArrayAdapter<ReportTypeSpinnerItem> spinnerAdapter;
    private ArrayList<ReportTypeSpinnerItem> spinnerItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadGamesIntoListView();

        setUpReportSelection();
        setUpSearchButton();
        setUpAddGameButton();
        setUpSaveButton();
    }



    private void loadGamesIntoListView() {
        listView = findViewById(R.id.gamesList);
        try {
            AssetManager assets = getAssets();
            sb.loadGames(assets.open(GAMES_CSV));
            bindAdapterToListView(listView);
        } catch (IOException e) {
        }
    }

    private void setUpReportSelection() {
        Spinner spinner = findViewById(R.id.chooseReport);
        spinner.setOnItemSelectedListener(this);
        fillSpinnerItems();
        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                spinnerItems
        );
        spinner.setAdapter(spinnerAdapter);
    }



    private void setUpSearchButton(){
        Button but = findViewById(R.id.search);
            but.setOnClickListener(
                    view -> {
                        final EditText editText = new EditText(this);
                        editText.setId(R.id.dialog_search_field);

                        new AlertDialog.Builder(this)
                                .setTitle(SteamGameAppConstants.ENTER_SEARCH_TERM)
                                .setView(editText)
                                .setPositiveButton("OK", (dialog, which) -> {
                                    listViewAdapter.getFilter().filter(editText.getText().toString());
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
            );

    }

    private void setUpAddGameButton() {
        Button but = findViewById(R.id.addGame);
        but.setOnClickListener(view ->  {
            SimpleDateFormat sdf = new SimpleDateFormat(Game.DATE_FORMAT);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            final EditText editName = new EditText(this);
            editName.setId(R.id.dialog_name_field);
            final EditText editDate = new EditText(this);
            editDate.setId(R.id.dialog_date_field);
            final EditText editPrice = new EditText(this);
            editPrice.setId(R.id.dialog_price_field);
            linearLayout.addView(editName);
            linearLayout.addView(editDate);
            linearLayout.addView(editPrice);

            new AlertDialog.Builder(this)
                    .setTitle(SteamGameAppConstants.NEW_GAME_DIALOG_TITLE)
                    .setView(linearLayout)
                    .setPositiveButton("OK", (dialog, which) -> {
                        try {
                            Game game = new Game(
                                    editName.getText().toString(),
                                    sdf.parse(editDate.getText().toString()),
                                    Double.parseDouble(editPrice.getText().toString()));
                            sb.addGame(game);
                            listViewAdapter.addGame(game);
                            listViewAdapter.notifyDataSetChanged();
                        } catch (ParseException e) {
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void setUpSaveButton() {
        Button but = findViewById(R.id.save);
        but.setOnClickListener(view -> {
                try {
                    FileOutputStream fos = openFileOutput(SteamGameAppConstants.SAVE_GAMES_FILENAME, MODE_PRIVATE);
                    sb.store(fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

        });
        Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
    }

    private void bindAdapterToListView(ListView listView) {
        listViewAdapter = new GameAdapter(this, R.layout.game_item_layout, sb.getGames());
        listView.setAdapter(listViewAdapter);
    }

    private void fillSpinnerItems() {
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.NONE, SteamGameAppConstants.SELECT_ONE_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.SUM_GAME_PRICES, SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.AVERAGE_GAME_PRICES, SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.UNIQUE_GAMES, SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.MOST_EXPENSIVE_GAMES, SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT));
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ReportTypeSpinnerItem rtsi = (ReportTypeSpinnerItem) adapterView.getItemAtPosition(i);

            String message;

            switch (rtsi.getType()){
                case SUM_GAME_PRICES:
                    message = SteamGameAppConstants.ALL_PRICES_SUM + sb.sumGamePrices();
                    break;
                case AVERAGE_GAME_PRICES:
                    message = SteamGameAppConstants.ALL_PRICES_AVERAGE + sb.averageGamePrice();
                    break;
                case UNIQUE_GAMES:
                    message = SteamGameAppConstants.UNIQUE_GAMES_COUNT + sb.getUniqueGames().size();
                    break;
                case MOST_EXPENSIVE_GAMES:
                    message = SteamGameAppConstants.MOST_EXPENSIVE_GAMES;
                    List<Game> topGames = sb.selectTopNGamesDependingOnPrice(3);
                    for (int j = 0; j < topGames.size(); j++) {
                        message += "\n" + topGames.get(j).toString();
                    }
                    break;

                default:
                    return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(rtsi.getDisplayText())
                    .setMessage(message)
                    .setNeutralButton("OK", null)
                    .show();
        }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}









