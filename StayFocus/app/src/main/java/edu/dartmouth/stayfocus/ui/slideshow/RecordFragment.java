package edu.dartmouth.stayfocus.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.dartmouth.stayfocus.Entry;
import edu.dartmouth.stayfocus.FirebaseHelper;
import edu.dartmouth.stayfocus.R;

public class RecordFragment extends Fragment {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {

        } else {
            mUserId = mFirebaseUser.getUid();

            // Set up ListView
            final ListView listView = (ListView) view.findViewById(R.id.listView);
            final ArrayList<Entry> adapterData = new ArrayList<>();
            final CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), R.layout.adapter_view, adapterData);
            //Set up listView adapter
            listView.setAdapter(adapter);


            final EditText text = (EditText)  view.findViewById(R.id.todoText);
            final Button button = (Button)  view.findViewById(R.id.addButton);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Entry item = new Entry(text.getText().toString(), text.getText().toString(), "30min", "failed");
                    FirebaseHelper helper = new FirebaseHelper();
                    helper.addEntry(item);
                    text.setText("");
                }
            });

            // Use Firebase to populate the list.
            mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String startTime = (String)dataSnapshot.child("startTime").getValue();
                    String endTime = (String)dataSnapshot.child("endTime").getValue();
                    String duration = (String)dataSnapshot.child("duration").getValue();
                    String success = (String)dataSnapshot.child("success").getValue();
                    adapter.add(new Entry(startTime, endTime, duration, success));
                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {



                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // Delete items when clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirebaseHelper helper = new FirebaseHelper();
                    helper.deleteEntry(position, listView);
                    adapterData.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    //Customize record adapter
    public class CustomAdapter extends ArrayAdapter<Entry> {
        private ArrayList<Entry> objects;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Entry> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.adapter_view, null);
            }


            Entry entry = objects.get(position);

            if (entry != null) {
                TextView durationView = (TextView) v.findViewById(R.id.name);
                TextView timeView = (TextView) v.findViewById(R.id.record);

                if (durationView != null){
                    durationView.setText(entry.getDuration());
                }
                if (timeView != null){
                    timeView.setText(entry.getStartTime());
                }

            }

            return v;
        }
    }

}