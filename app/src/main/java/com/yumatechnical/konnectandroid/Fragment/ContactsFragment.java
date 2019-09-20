package com.yumatechnical.konnectandroid.Fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yumatechnical.konnectandroid.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		AdapterView.OnItemClickListener {

	/*
	 * Defines an array that contains column names to move from
	 * the Cursor to the ListView.
	 */
	@SuppressLint("InlinedApi")
	private final static String[] FROM_COLUMNS = {
			Build.VERSION.SDK_INT
					>= Build.VERSION_CODES.HONEYCOMB ?
					ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
					ContactsContract.Contacts.DISPLAY_NAME
	};
	@SuppressLint("InlinedApi")
	private static final String[] PROJECTION = {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.LOOKUP_KEY,
			Build.VERSION.SDK_INT
					>= Build.VERSION_CODES.HONEYCOMB ?
					ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
					ContactsContract.Contacts.DISPLAY_NAME
			/*
			{
            / *
             * The detail data row ID. To make a ListView work,
             * this column is required.
             * /
			ContactsContract.Data._ID,
			// The primary display name
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
					ContactsContract.Data.DISPLAY_NAME_PRIMARY :
					ContactsContract.Data.DISPLAY_NAME,
			// The contact's _ID, to construct a content URI
			ContactsContract.Data.CONTACT_ID,
			// The contact's LOOKUP_KEY, to construct a content URI
			ContactsContract.Data.LOOKUP_KEY // A permanent link to the contact
	}
			 */
	};
	// Defines the text expression
	@SuppressLint("InlinedApi")
	private static final String SELECTION =
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
					ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
					ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
	// Defines a variable for the search string
	private String searchString;
	// Defines the array to hold values that replace the ?
	private String[] selectionArgs = { searchString };

	/*
	 * Defines an array that contains resource ids for the layout views
	 * that get the Cursor column contents. The id is pre-defined in
	 * the Android framework, so it is prefaced with "android.R.id"
	 */
	private final static int[] TO_IDS = {
			android.R.id.text1
	};
	// Define global mutable variables
	// Define a ListView object
	private ListView contactsList;
	// Define variables for the contact the user selects
	// The contact's _ID value
	long contactId;
	// The contact's LOOKUP_KEY
	String contactKey;
	// A content URI for the selected contact
	Uri contactUri;
	// An adapter that binds the result Cursor to the ListView
	private SimpleCursorAdapter cursorAdapter;
	// The column index for the _ID column
	private static final int CONTACT_ID_INDEX = 0;
	// The column index for the CONTACT_KEY column
	private static final int CONTACT_KEY_INDEX = 1;


	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	public ContactsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ContactsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ContactsFragment newInstance(String param1, String param2) {
		ContactsFragment fragment = new ContactsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_contacts, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Gets the ListView from the View list of the parent activity
//		contactsList = getActivity().findViewById(R.layout.contact_list_view);
		// Gets a CursorAdapter
		cursorAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.contact_list_item,
				null,
				FROM_COLUMNS, TO_IDS,
				0);
		if (contactsList != null) {
			// Sets the adapter for the ListView
			contactsList.setAdapter(cursorAdapter);
			contactsList.setOnItemClickListener(this);
		}
		// Initializes the loader
		getLoaderManager().initLoader(0, null, this);
	}
/*
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	@NonNull
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*		// Get the Cursor
		Cursor cursor = parent.getAdapter().getCursor();
		// Move to the selected contact
		cursor.moveToPosition(position);
		// Get the _ID value
		contactId = cursor.getLong(CONTACT_ID_INDEX);
		// Get the selected LOOKUP KEY
		contactKey = cursor.getString(CONTACT_KEY_INDEX);
		// Create the contact's content Uri
		contactUri = ContactsContract.Contacts.getLookupUri(contactId, mContactKey);*/
		/*
		 * You can use contactUri as the content URI for retrieving
		 * the details for a contact.
		 */
	}

	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		/*
		 * Makes search string into pattern and
		 * stores it in the selection array
		 */
		selectionArgs[0] = "%" + searchString + "%";
		// Starts the query
		return new CursorLoader(
				getActivity(),
				ContactsContract.Contacts.CONTENT_URI,
				PROJECTION,
				SELECTION,
				selectionArgs,
				null
		);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
		// Put the result Cursor in the adapter for the ListView
//		cursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		// Delete the reference to the existing Cursor
		cursorAdapter.swapCursor(null);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
