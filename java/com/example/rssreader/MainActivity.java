package com.example.rssreader;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String INTERNET_PERMISSION = Manifest.permission.INTERNET;
    private static final int REQUEST_INTERNET_PERMISSION_CODE = 228;

    private static final String TAG = "MainActivity";

    private MyRSSItemListViewAdapter rssListAdapter;
    private Repository repository;
    private AsyncImageLoad asyncImageLoad;
    private AsyncDatabaseImageSave asyncDatabaseImageSave;
    private MyAsyncHTTPSTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isPermissionGranted(INTERNET_PERMISSION)) {
            requestPermission(INTERNET_PERMISSION, REQUEST_INTERNET_PERMISSION_CODE);
        }





        ActionBar actionBar = getSupportActionBar();;
        if (actionBar != null)
            actionBar.hide();

        repository = new Repository(this);
        repository.open();

        EditText edit = findViewById(R.id.edit_rss_source);
        edit.setText(repository.getLastUrl());


        ArrayList<RSSItem> array = repository.getItems();

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' hh.mm", Locale.getDefault());
        String currentDate = sdf.format(new Date());*/
        rssListAdapter = new MyRSSItemListViewAdapter(this, array);
        ListView rss_list = findViewById(R.id.rss_list);
        rss_list.setAdapter(rssListAdapter);
        rss_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToBrowser(((RSSItem)rssListAdapter.getItem(position)).getLink());
            }
        });
    }

    public void onClick(View view) {
        if (isNetworkAvailable())
        {
            task = new MyAsyncHTTPSTask(this);
            EditText edit = findViewById(R.id.edit_rss_source);
            String str = edit.getText().toString();
            if (!str.startsWith("https://") && !str.startsWith("http://"))
                str = "https://" + str;

            Log.d(TAG, str);
            task.execute(str);
            }
    }

    public void onLoadFinished(ArrayList<RSSItem> result)
    {
        if (result != null)
        {
            rssListAdapter.setArray(result);
            ListView listView = findViewById(R.id.rss_list);
            asyncImageLoad = new AsyncImageLoad(listView, this);
            asyncImageLoad.execute(rssListAdapter.getItems());
        }
        else
        {
            Toast.makeText(this, "Something went wrong(", Toast.LENGTH_LONG).show();
        }
    }

    public void goToBrowser(String url)
    {
        Intent intent = new Intent(this, MyBrowser.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void saveImagesToDatabaseAsync(ArrayList<RSSItem> list)
    {
        asyncDatabaseImageSave = new AsyncDatabaseImageSave(repository);
        asyncDatabaseImageSave.execute(list);
    }


    @Override
    protected void onDestroy() {
        repository.close();
        if (task != null)
            task.cancel(true);
        if (asyncImageLoad != null)
            asyncImageLoad.cancel(true);
        if (asyncDatabaseImageSave != null)
            asyncDatabaseImageSave.cancel(true);
        super.onDestroy();
    }

    private boolean isPermissionGranted(String permission) {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode == REQUEST_INTERNET_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Разрешения получены", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Разрешения не получены", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

     public void saveToDb(ArrayList<RSSItem> result, String str) {
        repository.clear();
        repository.setLastUrl(str);
        repository.insertItems(result);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
