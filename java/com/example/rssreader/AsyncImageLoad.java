package com.example.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;

public class AsyncImageLoad extends AsyncTask<ArrayList<RSSItem>, Integer, ArrayList<RSSItem>> {
    private  ListView listView;
    private  int current;
    private  MainActivity mainActivity;

    public AsyncImageLoad(ListView listView, MainActivity mainActivity) {
        this.listView = listView;
        this.mainActivity = mainActivity;
        current = 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<RSSItem> doInBackground(ArrayList<RSSItem>... arrayLists) {
        ArrayList<RSSItem> list = arrayLists[0];
        String url = "";
        Bitmap bmp;

        try {
            for (RSSItem item : list) {
                url = item.getImageUrl();
                bmp = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                item.setBitmap(bmp);
                publishProgress(current);
                current++;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        View itemView = getViewByPosition(values[0]);
        ImageView imageView = itemView.findViewById(R.id.image_view);
        imageView.setImageBitmap(((RSSItem)(listView.getAdapter().getItem(values[0]))).getBitmap());
        //repository.insertBitmap(((RSSItem)(listView.getAdapter().getItem(values[0]))).getBitmap() , ((RSSItem)(listView.getAdapter().getItem(values[0]))).getImageurl());
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> list) {
        mainActivity.saveImagesToDatabaseAsync(list);
        super.onPostExecute(list);
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
