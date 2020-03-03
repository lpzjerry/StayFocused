package edu.dartmouth.stayfocus;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

import java.util.Date;

import edu.dartmouth.stayfocus.room.Todo;

public class TodoActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.todolistsql.REPLY";

   // public Date datePicked = null;

    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        todo = new Todo();

        final EditText editTextTitle = (EditText)findViewById(R.id.title);



    /*    findViewById(R.id.notes).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showInputDialog("Notes", EditorInfo.TYPE_CLASS_TEXT, (EditText)findViewById(R.id.notes));
            }
        });

        EditText editTextNotes = (EditText) findViewById(R.id.notes);

 /*       findViewById(R.id.BNRecordSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editTextTitle.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = editTextTitle.getText().toString();
                    todo.setTitle(title);
                    String notes = editTextNotes.getText().toString();
                    todo.setNotes(notes);
                    todo.setDueDate(datePicked);
                    Date createDate = new Date();
                    todo.setCreateTime(createDate);
                    replyIntent.putExtra(EXTRA_REPLY, todo);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        }); */
    }

  /*  public void showDatePickerDialog(View v){
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
