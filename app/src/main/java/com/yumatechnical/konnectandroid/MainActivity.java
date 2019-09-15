package com.yumatechnical.konnectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.yumatechnical.konnectandroid.Adapter.LeftAdapter;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Model.KeyStrValueStr;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener,
        LeftAdapter.OnItemClickListener, RightAdapter.ListItemClickListener {

	private static String[] FROM_COLUMNS, TO_IDS;
	ListView contactsList;
	ListAdapter cursorAdapter;
	FrameLayout left, right, base;
    private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
    RecyclerView recyclerViewLeft, recyclerViewRight;
    List<String> leftList, rightlist;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private ProgressBar spinner;
    private static final String TAG = "KonnectAndroid";
    Boolean show_hidden, save_pass, rem_local, rem_remote;

    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;


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
        SetupSharedPrefs();
    }

    void fillLeft() {
        leftList = Arrays.asList(getResources().getStringArray(R.array.first));
        recyclerViewLeft = findViewById(R.id.rv_left);
        LinearLayoutManager layoutManagerLeft =
                new LinearLayoutManager(this);//, LinearLayoutManager.VERTICAL, false);
        recyclerViewLeft.setLayoutManager(layoutManagerLeft);
        leftAdapter = new LeftAdapter(leftList, this);
        recyclerViewLeft.setAdapter(leftAdapter);
        leftAdapter.setData(leftList);
    }

    void fillRight() {
        spinner = findViewById(R.id.pb_spinner);
//        rightlist = getResources().getStringArray()
        recyclerViewRight = findViewById(R.id.rv_right);
        LinearLayoutManager layoutManagerRight =
                new LinearLayoutManager(this);
        recyclerViewRight.setLayoutManager(layoutManagerRight);
        rightAdapter = new RightAdapter(null, this);
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
        MenuItem menuItem;

        menuItem = menu.findItem(R.id.add_connect);
        menuItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.White).actionBarSize());

        menuItem = menu.findItem(R.id.settings);
        menuItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_navicon)
                .colorRes(R.color.White).actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add_connect:
                intent = new Intent(this, AddConnectionActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public void onContactsActivityCreated() {
		// Gets the ListView from the View list of the parent activity
//		contactsList =
//				(ListView) findViewById(R.layout.contact_list_view);
		// Gets a CursorAdapter
		cursorAdapter = new SimpleCursorAdapter(
				this,
				R.layout.contact_list_item,
				null,
				null/*FROM_COLUMNS*/, null/*TO_IDS*/,
				0);
		// Sets the adapter for the ListView
		contactsList.setAdapter(cursorAdapter);
//		contactsList.setOnItemClickListener(this);
	}

	void SetupSharedPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        show_hidden = sharedPreferences.getBoolean(getString(R.string.PREFS_show_hidden),
                getResources().getBoolean(R.bool.PREFS_show_hidden_default));
        save_pass = sharedPreferences.getBoolean(getString(R.string.PREFS_save_pass),
                getResources().getBoolean(R.bool.PREFS_save_pass_default));
        rem_local = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_local),
                getResources().getBoolean(R.bool.PREFS_rem_local_default));
        rem_remote = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_remote),
                getResources().getBoolean(R.bool.PREFS_rem_remote_default));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.PREFS_show_hidden))) {
            show_hidden = sharedPreferences.getBoolean(getString(R.string.PREFS_show_hidden),
                    getResources().getBoolean(R.bool.PREFS_show_hidden_default));
        } else
        if (key.equals(getString(R.string.PREFS_save_pass))) {
            save_pass = sharedPreferences.getBoolean(getString(R.string.PREFS_save_pass),
                    getResources().getBoolean(R.bool.PREFS_save_pass_default));
        } else
        if (key.equals(getString(R.string.PREFS_rem_local))) {
            rem_local = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_local),
                    getResources().getBoolean(R.bool.PREFS_rem_local_default));
        } else
        if (key.equals(getString(R.string.PREFS_rem_remote))) {
            rem_remote = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_remote),
                    getResources().getBoolean(R.bool.PREFS_rem_remote_default));
        }
//        SetupSharedPrefs();
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

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
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
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "The permission was granted! Start up the visualizer!");
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

    private void AccessContact(List<KeyStrValueStr> permissions)
    {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        for (int i = 0; i < permissions.size(); i++) {
            if (!addPermission(permissionsList, permissions.get(i).getKey())) {
                permissionsNeeded.add(permissions.get(i).getValue());
            }
        }
//        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
//            permissionsNeeded.add("Read Contacts");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
//            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                StringBuilder message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));
                showMessageOKCancel(message.toString(),
                        (dialog, which) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_MULTIPLE_PERMISSIONS);
            }
        }
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                return shouldShowRequestPermissionRationale(permission);
            }
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onItemClick(String item) {
        /*left*/
        if (item.equals(getString(R.string.photos))) {
            List<KeyStrValueStr> permissions = new ArrayList<>();
            permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
            permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
            AccessContact(permissions);
            Intent service = new Intent(MainActivity.this, MyService.class);
            startService(service);
        } else
        if (item.equals(getString(R.string.music))) {
            List<KeyStrValueStr> permissions = new ArrayList<>();
            permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
            permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
            AccessContact(permissions);
            getMediaList();
//            startActivity(new Intent(this, PlayMedia.class));
        } else
        if (item.equals(getString(R.string.contacts))) {
            List<KeyStrValueStr> permissions = new ArrayList<>();
            permissions.add(new KeyStrValueStr(Manifest.permission.READ_CONTACTS, "Read Contacts"));
            permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_CONTACTS, "Write Contacts"));
            AccessContact(permissions);
            startActivity(new Intent(this, PutOnRight.class));
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//            startActivityForResult(intent, PICK_CONTACT);
        } else
        if (item.equals(getString(R.string.files))) {
            List<KeyStrValueStr> permissions = new ArrayList<>();
            permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
            permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
            AccessContact(permissions);
        }
    }

    @Override
    public void onListItemClick(String item) {
        /*right*/
        Log.d(TAG, "right: " + item);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        try {
                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                String cNumber = phones.getString(phones.getColumnIndex("data1"));
                                System.out.println("number is:" + cNumber);
//                                txtphno.setText("Phone Number is: "+cNumber);
                            }
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                            txtname.setText("Name is: "+name);
                        }
                        catch (Exception ex)
                        {
//                            st.getMessage();
                        }
                    }
                }
                break;
        }
    }

    void getMediaList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null,
                null, null);

        if (musicCursor!=null && musicCursor.moveToFirst()) {
            //get columns
            int titleCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int albumCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idCol);
                String thisTitle = musicCursor.getString(titleCol);
                String thisArtist = musicCursor.getString(artistCol);
                String thisAlbum = musicCursor.getString(albumCol);
                rightlist.add(thisId + ":" + thisTitle + ":" + thisArtist + ":" + thisAlbum);
            }
            while (musicCursor.moveToNext());
            fillRight();
        }
    }

}

