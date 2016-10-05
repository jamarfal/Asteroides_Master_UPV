package org.example.asteroides.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.example.asteroides.R;

import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Vector<String> list;
    protected View.OnClickListener onClickListener;

    public MyCustomAdapter(Context context, Vector<String> list) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.element_score_list_layout, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.title.setText(list.get(i));
        switch (Math.round((float) Math.random() * 3)) {
            case 0:
                holder.icon.setImageResource(R.drawable.asteroide);
                break;
            case 1:
                holder.icon.setImageResource(R.drawable.asteroide_1);
                break;
            default:
                holder.icon.setImageResource(R.drawable.asteroide_2);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle_text_view);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
