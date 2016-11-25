package org.example.asteroides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.example.asteroides.adapter.MyCustomAdapter;
import org.example.asteroides.logic.DowloaderScore;

import java.util.Vector;

public class ScoreActivity extends AppCompatActivity implements DowloaderScore {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyCustomAdapter myCustomAdapter;
    private Vector<String> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        configView();
    }

    private void configView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        MainActivity.pointsStorage.scoreList(10, this);
        myCustomAdapter = new MyCustomAdapter(this, scoreList);
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
    }
}
