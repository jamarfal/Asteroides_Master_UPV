package org.example.asteroides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myCustomAdapter = new MyCustomAdapter(this, MainActivity.storageArray.scoreList(10));
        myCustomAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerView.getChildAdapterPosition(v);
                String s = MainActivity.storageArray.scoreList(10).get(pos);
                Toast.makeText(ScoreActivity.this, "Selección: " + pos
                        + " - " + s, Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(myCustomAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
