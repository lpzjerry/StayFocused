package edu.dartmouth.stayfocus;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.dartmouth.stayfocus.Entry;


public class FirebaseHelper {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;

    private static DatabaseReference database;
    private static String userId;

    public FirebaseHelper(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();
        userId = firebaseUser.getUid();
    }

    public void addEntry(Entry entry){
        database.child("users").child(userId).child("items").push().setValue(entry);
//        database.child("users").child(userId).child("entries").child(String.valueOf(entry.getId())).child("id").setValue(entry.getId());
//        database.child("users").child(userId).child("entries").child(String.valueOf(entry.getId())).child("title").setValue(entry.getTitle());

    }

    public void deleteEntry(int position){
        database.child("users").child(userId).child("items")
                .orderByChild("title")
                .equalTo((String) test_list1.listView.getItemAtPosition(position))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                            firstChild.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void editData(int position, final Entry entry){
        database.child("users").child(userId).child("items")
                .orderByChild("title")
                .equalTo((String)test_list1.listView.getItemAtPosition(position))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot edtData: dataSnapshot.getChildren()){
                            edtData.getRef().setValue(entry);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });
    }


    public void deleteAllEntry(){
        database.child("users").child(userId).child("items").removeValue();
    }

}
