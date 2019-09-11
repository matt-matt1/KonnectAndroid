package com.yumatechnical.konnectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import info.androidhive.fontawesome.FontDrawable;

public class MainActivity extends AppCompatActivity {

    FrameLayout left, right, base;
    private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
    RecyclerView recyclerViewLeft, recyclerViewRight;
    String[] leftList, rightlist;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private static final String TAG = "KonnectAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        left = findViewById(R.id.left_frame);
        fillLeft();
        right = findViewById(R.id.right_frame);
        fillRight();
//        base = findViewById(R.id.base_frame);

        Iconify.with(new FontAwesomeModule());
    }

    void fillLeft() {
        leftList = getResources().getStringArray(R.array.first);
        recyclerViewLeft = findViewById(R.id.rv_left);
        LinearLayoutManager layoutManagerLeft =
                new LinearLayoutManager(this);//, LinearLayoutManager.VERTICAL, false);
        recyclerViewLeft.setLayoutManager(layoutManagerLeft);
        leftAdapter = new LeftAdapter();
        recyclerViewLeft.setAdapter(leftAdapter);
        leftAdapter.setData(leftList);
    }

    void fillRight() {
        ProgressBar spinner = findViewById(R.id.pb_spinner);
//        rightlist = getResources().getStringArray()
        recyclerViewRight = findViewById(R.id.rv_right);
        LinearLayoutManager layoutManagerRight =
                new LinearLayoutManager(this);
        recyclerViewRight.setLayoutManager(layoutManagerRight);
        rightAdapter = new RightAdapter();
        recyclerViewRight.setAdapter(rightAdapter);
        rightAdapter.setData(rightlist);
    }
    /**
     * Methods for setting up the menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: menu size: " + menu.size());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visualizer_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuAdd = menu.findItem(R.id.add_connect);
        menuAdd.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.White).actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_connect) {
            Intent startSettingsActivity = new Intent(this, AddConnectionActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onPause Cleanup audio stream
     **/
    @Override
    protected void onPause() {
        super.onPause();
//        if (mAudioInputReader != null) {
//            mAudioInputReader.shutdown(isFinishing());
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAudioInputReader != null) {
//            mAudioInputReader.restart();
//        }
    }

    /**
     * App Permissions for Audio
     **/
    private void setupPermissions() {
        // If we don't have the record audio permission...
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{ Manifest.permission.RECORD_AUDIO };
                requestPermissions(permissionsWeNeed, MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE);
            }
//        } else {
            // Otherwise, permissions were granted and we are ready to go!
//            mAudioInputReader = new AudioInputReader(mVisualizerView, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission was granted! Start up the visualizer!
//                    mAudioInputReader = new AudioInputReader(mVisualizerView, this);

                } else {
                    Toast.makeText(this, getString(R.string.no_perm_audio), Toast.LENGTH_LONG).show();
                    finish();
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
            // Other permissions could go down here

        }
    }

}

