//package gov.pppl.game.game.networking.server;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
///**
// * A fragment representing a list of Items.
// * <p/>
// * Large screen devices (such as tablets) are supported by replacing the ListView
// * with a GridView.
// * <p/>
// * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
// * interface.
// */
//public class BluetoothDevices extends Fragment implements AbsListView.OnItemClickListener {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBERs
//    private static final String ARG_DEVICES = "devices";
//    private OnFragmentInteractionListener listener;
//
//    /**
//     * The fragment's ListView/GridView.
//     */
//    private AbsListView absListView;
//    private ArrayList<String> deviceNameList;
//
//    /**
//     * The Adapter which will be used to populate the ListView/GridView with
//     * Views.
//     */
//    private ListAdapter adapter;
//
//
//    public static BluetoothDevices newInstance() {
//        BluetoothDevices fragment = new BluetoothDevices();
//        Bundle args = new Bundle();
//        args.putStringArrayList(ARG_DEVICES, Bluetooth.getDeviceNames());
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public BluetoothDevices() {}
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        deviceNameList = getArguments().getStringArrayList(ARG_DEVICES);
//        deviceNameList.add("Test");
//        // TODO: Change Adapter to display your content
//        adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_select_device_list, deviceNameList);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
//
//        // Set the adapter
//        absListView = (AbsListView) view.findViewById(android.R.id.list);
//        absListView.setAdapter(adapter);
//
//        // Set OnItemClickListener so we can be notified on item clicks
//        absListView.setOnItemClickListener(this);
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            listener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
//
//    /**
//     * The default content for this Fragment has a TextView that is shown when
//     * the list is empty. If you would like to change the text, call this method
//     * to supply the text it should use.
//     */
//    public void setEmptyText(CharSequence emptyText) {
//        View emptyView = absListView.getEmptyView();
//
//        if (emptyView instanceof TextView) {
//            ((TextView) emptyView).setText(emptyText);
//        }
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(String id);
//    }
//
//}
