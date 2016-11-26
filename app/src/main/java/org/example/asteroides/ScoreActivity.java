package org.example.asteroides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.example.asteroides.adapter.MyCustomAdapter;

import org.example.asteroides.logic.storage_operations.PointsStorage;
import org.example.asteroides.logic.storage_operations.StorageOperations;
import org.example.asteroides.logic.provider.StorageProvider;
import org.example.asteroides.logic.storage_operations.StorageSingleton;
import org.example.asteroides.preferences.GamePreferences;

import java.util.Vector;

public class ScoreActivity extends AppCompatActivity implements StorageOperations {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyCustomAdapter myCustomAdapter;
    private Vector<String> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        configView();
        StorageSingleton.getInstance().getPointsStorage().getScore(10, this);
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
        this.scoreList = scoreList;
        myCustomAdapter.setList(scoreList);
    }

    @Override
    public void OnSaveScoreComplete() {

    }
}
