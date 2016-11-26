package org.example.asteroides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.example.asteroides.adapter.MyCustomAdapter;
import org.example.asteroides.logic.PoinstStorageExternalFileApi8;
import org.example.asteroides.logic.PoinstStorageJson;
import org.example.asteroides.logic.PoinstStorageSW_PHP;
import org.example.asteroides.logic.PointsStorage;
import org.example.asteroides.logic.PointsStorageArray;
import org.example.asteroides.logic.PointsStorageAssetsResources;
import org.example.asteroides.logic.PointsStorageExternalFile;
import org.example.asteroides.logic.PointsStorageGson;
import org.example.asteroides.logic.PointsStorageInternalFile;
import org.example.asteroides.logic.PointsStoragePreferences;
import org.example.asteroides.logic.PointsStorageProvider;
import org.example.asteroides.logic.PointsStorageRawResources;
import org.example.asteroides.logic.PointsStorageSW_PHP_Asynctask;
import org.example.asteroides.logic.PointsStorageSocket;
import org.example.asteroides.logic.PointsStorageSqliteRel;
import org.example.asteroides.logic.PointsStorageXML_SAX;
import org.example.asteroides.logic.StorageOperations;
import org.example.asteroides.logic.StorageProvider;
import org.example.asteroides.preferences.GamePreferences;

import java.util.Vector;

public class ScoreActivity extends AppCompatActivity implements StorageOperations {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyCustomAdapter myCustomAdapter;
    private Vector<String> scoreList;
    private PointsStorage pointsStorage;
    private GamePreferences gamePreferences;
    private StorageProvider storageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        gamePreferences = new GamePreferences(this);
        storageProvider = new StorageProvider();
        pointsStorage = storageProvider.createStorage(gamePreferences.getSaveMethod(), this);
        configView();
        pointsStorage.getScore(10, this);
    }

    private void configView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myCustomAdapter = new MyCustomAdapter(this, new Vector<String>());
        myCustomAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScore(v);
            }
        });
        recyclerView.setAdapter(myCustomAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void showScore(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        String score = scoreList.get(pos);
        Toast.makeText(ScoreActivity.this, "Selección: " + pos
                + " - " + score, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnDowloadScoreComplete(Vector<String> scoreList) {
        myCustomAdapter.setList(scoreList);
    }

    @Override
    public void OnSaveScoreComplete() {

    }
}
