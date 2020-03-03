package edu.dartmouth.stayfocus.ui.record;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.dartmouth.stayfocus.Entry;
import edu.dartmouth.stayfocus.FirebaseHelper;
import edu.dartmouth.stayfocus.R;

public class RecordFragment extends Fragment {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseHelper helper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper();

        if (mFirebaseUser == null) {

        } else {
            mUserId = mFirebaseUser.getUid();

            // Set up ListView
            final ListView listView = (ListView) view.findViewById(R.id.listView);
            final ArrayList<Entry> adapterData = new ArrayList<>();
            final CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), R.layout.adapter_view, adapterData);
            //Set up listView adapter
            listView.setAdapter(adapter);


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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("DELETE");
                    alertDialog.setMessage("Do you want to delete this record?");
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE,"delete",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            helper.deleteEntry(position, listView);
                            adapterData.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.show();
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
                ImageView resImg = (ImageView) v.findViewById(R.id.icon_res);

                if (durationView != null){
                    durationView.setText(entry.getDuration());
                }
                if (timeView != null){
                    timeView.setText(entry.getStartTime());
                }
                if(entry.getSuccess().equals("failed")){
                    resImg.setImageResource(R.drawable.failed);

                }else{
                    resImg.setImageResource(R.drawable.successful);

                }

            }

            return v;
        }
    }



}