package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyRSSItemListViewAdapter extends ArrayAdapter {


    private final Context context;
    private ArrayList<RSSItem> items;

    public MyRSSItemListViewAdapter(@NonNull Context context, ArrayList<RSSItem> items) {
        super(context, R.layout.rss_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rss_item, parent, false);
        RSSItem item = items.get(position);
        //TODO: image
        TextView title = view.findViewById(R.id.title_view);
        TextView date  = view.findViewById(R.id.date_view);
        TextView preview = view.findViewById(R.id.preview_view);
        ImageView image = view.findViewById(R.id.image_view);
        title.setText(item.getTitle());
        date.setText(item.getDate());
        preview.setText(item.getPreview());
        if (item.getBitmap() != null) {
            //image.setLayoutParams(new LinearLayout.LayoutParams(L));
            image.setImageBitmap(item.getBitmap());
        }
        else
            image.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        return view;
    }

    public ArrayList<RSSItem> getItems() {
        return items;
    }

    public void setArray(ArrayList<RSSItem> list)
    {
        items.clear();
        for (RSSItem item : list)
            items.add(item);
        notifyDataSetChanged();
    }



}
