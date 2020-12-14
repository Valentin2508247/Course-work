package com.example.rssreader;

import android.os.AsyncTask;

import java.util.ArrayList;

public class



AsyncDatabaseImageSave extends AsyncTask<ArrayList<RSSItem>, Integer, Integer>{

    private Repository repository;

        public AsyncDatabaseImageSave(Repository repository) {
            this.repository = repository;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(ArrayList<RSSItem>... arrayLists) {
            ArrayList<RSSItem> list = arrayLists[0];
            repository.clearImages();
            for (RSSItem item: list)
            {
                if (item.getBitmap() != null)
                {
                    repository.insertBitmap(item.getBitmap(), item.getImageUrl());
                }
            }
            return 0;
        }


        @Override
        protected void onPostExecute(Integer i) {

            super.onPostExecute(i);
        }

    }

