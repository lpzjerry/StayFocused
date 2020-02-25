package edu.dartmouth.stayfocus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.Date;

import edu.dartmouth.stayfocus.DialogFragment.DatePickerFragment;
import edu.dartmouth.stayfocus.DialogFragment.TimePickerFragment;

public class TodoActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.todolistsql.REPLY";

    public Date datePicked = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        final EditText editTextTitle = (EditText)findViewById(R.id.title);



        findViewById(R.id.notes).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showInputDialog("Notes", EditorInfo.TYPE_CLASS_TEXT, (EditText)findViewById(R.id.notes));
            }
        });

        EditText editTextNotes = (EditText) findViewById(R.id.notes);

        findViewById(R.id.BNRecordSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editTextTitle.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = editTextTitle.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, title);
                    String notes = editTextNotes.getText().toString();
                    replyIntent.putExtra("notes", notes);
                    replyIntent.putExtra("duedate", datePicked);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    public void showDatePickerDialog(View v){
        DialogFragment dialogFragmentDate = new DatePickerFragment();
        dialogFragmentDate.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v){
        DialogFragment dialogFragmentTime= new TimePickerFragment();
        dialogFragmentTime.show(getSupportFragmentManager(), "timePicker");
    }

   /* public void onSaveButtonClicked(View v){
        finish();//temporary
    }*/

    public void onCancelButtonClicked(View v){
        finish();
    }

    public void showInputDialog(String title, int type, final EditText view){
        final EditText editText = new EditText(this);
        editText.setInputType(type);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(editText.getText());
                        view.setText(task);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create();
        alertDialog.show();
    }
}
