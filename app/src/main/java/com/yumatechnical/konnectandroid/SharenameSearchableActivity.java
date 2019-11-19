package com.yumatechnical.konnectandroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

//import com.yumatechnical.konnectandroid.Adapter.SearchAdapter;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Model.ItemObject;

import java.util.ArrayList;
import java.util.List;


public class SharenameSearchableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
/*
	private SearchAdapter mSearchAdapter;
	private ListView listView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable);

		listView = findViewById(R.id.lv_shares);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
		handleIntent(getIntent());
	}

	@Override
	public void setIntent(Intent newIntent) {
		handleIntent(newIntent);
	}
/*
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
*/
	private void handleIntent(Intent intent) {
		// Get the intent, verify the action and get the query
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search_share, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.search:
				onSearchRequested();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	/*
example:
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

		return true;
	}
 */


	private void doMySearch(String query) {
//		SharesnameContractContract.AUTHORITY
//		List<ItemObject> dictionaryObject = databaseObject.searchDictionaryWords(query);
//		List<ItemObject> dictionaryObject = null;
//		mSearchAdapter = new SearchAdapter(SharenameSearchableActivity.this, dictionaryObject);
//		listView.setAdapter(mSearchAdapter);
	}

	private Cursor provideSearchData(String host, String domain, String username, String password) {
		String[] columns = new String[] {   "_id", "item" };
		MatrixCursor cursor = new MatrixCursor(columns);
		SMBoperation smb = new SMBoperation();
		smb.listShares(host, username, password, domain, new SMBoperation.OnSMBshares() {
			@Override
			public void OnListShares(ArrayList<String> shares) {
				startManagingCursor(cursor);

				int iterator = 1;
				for (String share : shares) {
					cursor.addRow(new Object[]    {   iterator++, share}   );
				}
				Context context = null;
//				SimpleCursorAdapter adapter =
//						new SimpleCursorAdapter(context, R.layout.dialog_list, cursor);
//
//				setListAdapter(adapter);
//				return matrixCursor;
			}
		});
		return cursor;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
//		friendListAdapter.getFilter().filter(newText);
		return true;
	}
/*
	public class ExampleProvider extends ContentProvider {

		// Defines a handle to the Room database
		private AppDatabase appDatabase;

		// Defines a Data Access Object to perform the database operations
		private UserDao userDao;

		// Defines the database name
		private static final String DBNAME = "mydb";


		public boolean onCreate() {

			// Creates a new database object.
			appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, DBNAME).build();

			// Gets a Data Access Object to perform the database operations
			userDao = appDatabase.getUserDao();

			return true;
		}

		@Nullable
		@Override
		public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
			return null;
		}

		@Nullable
		@Override
		public String getType(@NonNull Uri uri) {
			return null;
		}

		@Nullable
		@Override
		public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
			return null;
		}

		@Override
		public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
			return 0;
		}

		@Override
		public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
			return 0;
		}
	}
*/
}
