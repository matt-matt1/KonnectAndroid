package com.yumatechnical.konnectandroid.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;
import com.yumatechnical.konnectandroid.Vars;

import org.parceler.Parcels;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLeftListFragmentInteractionListener}
 * interface.
 */
public class LeftItemFragment extends Fragment {

	private static final String TAG = LeftItemFragment.class.getSimpleName();
	private OnLeftListFragmentInteractionListener mListener;
//	public LeftArrayAdapter leftAdapter;
//	private MyViewModel model;


	public LeftItemFragment() {}


	public static Fragment newInstance() {
		LeftItemFragment fragment = new LeftItemFragment();
//		Bundle args = new Bundle();
//		args.putInt(ARG_COLUMN_COUNT, columnCount);
//		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		model = ViewModelProviders.of((FragmentActivity) getActivity()).get(MyViewModel.class);
//		if (getArguments() != null) {
//			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//		}
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*
//		outState.putParcelable("leftList", Parcels.wrap(((Vars)getActivity().getApplication()).leftList));
		outState.putParcelable("conns", Parcels.wrap(((Vars)getActivity().getApplication()).getConnectionItems()));
//		outState.putParcelable("conns", Parcels.wrap(model.getConnectionItems().getValue()));
		outState.putSerializable("leftList", ((Vars)getActivity().getApplication()).leftList);
//		outState.putSerializable("leftList", model.getLeftList().getValue());
		outState.putSerializable("conns", ((Vars)getActivity().getApplication()).getConnectionItems());
//		outState.putInt("iconSize", model.getIconSize().getValue());
		*/
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_left_list, container, false);

		if (savedInstanceState != null) {
//			((Vars)getActivity().getApplication()).leftList = Parcels.unwrap(
//					savedInstanceState.getParcelable("leftList"));
			((Vars)getActivity().getApplication()).setConnectionItems(
					(ArrayList<ConnectionItem>)savedInstanceState.getSerializable("conns"));
			((Vars)getActivity().getApplication()).leftList =
			    (ArrayList<ListItem>)savedInstanceState.getSerializable("leftList");
//			model.setLeftList((ArrayList<ListItem>)savedInstanceState.getSerializable("leftList"));
//			((Vars)getActivity().getApplication()).setConnectionItems(
//					Parcels.unwrap(savedInstanceState.getParcelable("conns")));
//			model.setConnectionItems(Parcels.unwrap(savedInstanceState.getParcelable("conns")));
//		}else{
//			getPersonArguments();
		}
		// Set the adapter
//		if (view instanceof RecyclerView) {
//			Context context = view.getContext();
//			RecyclerView recyclerView = (RecyclerView) view;
//			if (mColumnCount <= 1) {
//				recyclerView.setLayoutManager(new LinearLayoutManager(context));
//			} else {
//				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//			}
//			recyclerView.setAdapter(new LeftItemRecyclerViewAdapter(LeftPanelContent.leftList, mListener));
//		}
		if (view instanceof ListView) {
			Context context = view.getContext();
			fillLeft(context);
			ListView listView = (ListView) view;
			listView.setSelector(R.drawable.list_selector);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			((Vars)getActivity().getApplication()).leftAdapter = new LeftArrayAdapter(context, 0, ((Vars)getActivity().getApplication()).leftList, mListener);
//			model.leftAdapter = new LeftArrayAdapter(context, 0, Objects.requireNonNull(model.getLeftList().getValue()), mListener);
			listView.setAdapter(((Vars)getActivity().getApplication()).leftAdapter);
//			listView.setAdapter(model.leftAdapter);
		}
		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnLeftListFragmentInteractionListener) {
			mListener = (OnLeftListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnLeftListFragmentInteractionListener extends LeftArrayAdapter.OnListener {
		void onListFragmentInteraction(ListItem item);

		@Override
		void SelectedLeftItemId(int id);
	}


	private void fillLeft(Context context) {
		int leftListDefaultLeftPadding = Tools.dpToPx(16, context);
		int leftListDefaultTopPadding = Tools.dpToPx(16, context);
		int leftListDefaultBottomPadding = Tools.dpToPx(18, context);
		int leftListDefaultBetweenPadding = Tools.dpToPx(8, context);
		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false,
				fade_hosts = false;
		((Vars)getActivity().getApplication()).leftList.add(new ListItem(
				0, Vars.MY_PHOTOS_ID, context.getString(R.string.photos),null,
//		model.addLeftListItem(new ListItem(0, Vars.MY_PHOTOS_ID, context.getString(R.string.photos),null,
				Vars.myPhotos_icon2(context), leftListDefaultLeftPadding, leftListDefaultTopPadding,
				leftListDefaultBottomPadding, true, leftListDefaultBetweenPadding, fade_photos));
		((Vars)getActivity().getApplication()).leftList.add(new ListItem(0, Vars.MY_MUSIC_ID, context.getString(R.string.music),null,
//		model.addLeftListItem(new ListItem(0, Vars.MY_MUSIC_ID, context.getString(R.string.music),null,
				Vars.myMusic_icon(context), leftListDefaultLeftPadding, leftListDefaultTopPadding,
				leftListDefaultBottomPadding, true, leftListDefaultBetweenPadding, fade_music));
		((Vars)getActivity().getApplication()).leftList.add(new ListItem(0,Vars.MY_CONTACTS_ID, context.getString(R.string.contacts),null,
//		model.addLeftListItem(new ListItem(0,Vars.MY_CONTACTS_ID, context.getString(R.string.contacts),null,
				Vars.myContacts_icon(context), leftListDefaultLeftPadding, leftListDefaultTopPadding,
				leftListDefaultBottomPadding, true, leftListDefaultBetweenPadding, fade_contacts));
		((Vars)getActivity().getApplication()).leftList.add(new ListItem(0, Vars.MY_FILES_ID, context.getString(R.string.files),null,
//		model.addLeftListItem(new ListItem(0, Vars.MY_FILES_ID, context.getString(R.string.files),null,
				Vars.myFiles_icon(context), leftListDefaultLeftPadding, leftListDefaultTopPadding,
				leftListDefaultBottomPadding, true, leftListDefaultBetweenPadding, fade_files));
		((Vars)getActivity().getApplication()).leftList.add(new ListItem(0, Vars.MY_LOCAL_HOSTS,
				context.getString(R.string.my_network),null,
//		model.addLeftListItem(new ListItem(0, Vars.MY_FILES_ID, context.getString(R.string.files),null,
				Vars.myLocalHosts_icon(context), leftListDefaultLeftPadding, leftListDefaultTopPadding,
				leftListDefaultBottomPadding, true, leftListDefaultBetweenPadding, fade_hosts));
	}
/*
	public void openLeftPanel(Context context) {
		FrameLayout leftPanel = context.getV
	}
*/
}
