package org.example.asteroides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.example.asteroides.adapter.MyCustomAdapter;

public class ScoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        configView();
    }

    private void configView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myCustomAdapter = new MyCustomAdapter(this, MainActivity.pointsStorage.scoreList(10));
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
        String score = MainActivity.pointsStorage.scoreList(10).get(pos);
        Toast.makeText(ScoreActivity.this, "Selección: " + pos
                + " - " + score, Toast.LENGTH_LONG).show();
    }
}
