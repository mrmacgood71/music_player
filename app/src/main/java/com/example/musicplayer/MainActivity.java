package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.songs_list);

        runtimePermission();


    }

    public void runtimePermission() {
        Dexter
                .withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public List<File> findSong(File song) {
        List<File> songsList = new ArrayList<>();
        File[] files = song.listFiles();
        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory() && !currentFile.isHidden()) {
                    songsList.addAll(findSong(currentFile));
                } else {
                    if (currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".wav")) {
                        songsList.add(currentFile);
                    }
                }
            }
        }
        return songsList;
    }

    void displaySongs() {
        final List<File> userSongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[userSongs.size()];

        for (int i = 0; i < userSongs.size(); i++) {
            items[i] = userSongs
                    .get(i)
                    .getName()
                    .replace(".mp3", "")
                    .replace(".wav", "");
        }

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View itemView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView songName = itemView.findViewById(R.id.song_name);
            songName.setSelected(true);
            songName.setText(items[position]);

            return itemView;
        }
    }
}