package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import edu.dartmouth.stayfocus.DialogFragment.DatePickerFragment;
import edu.dartmouth.stayfocus.DialogFragment.DatePickerFragmentEdit;
import edu.dartmouth.stayfocus.room.Todo;

public class TodoEditActivity extends AppCompatActivity {

    private Todo todo;
    public Date datePicked2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);

        Intent intent = getIntent();
        todo = (Todo)intent.getSerializableExtra("todo");

        String title = todo.getTitle();
        TextView textViewTitle = findViewById(R.id.todoName);
        textViewTitle.setText(title);

        Date dueDate = todo.getDueDate();
        if(dueDate!=null){
            EditText editTextDuedate = (EditText) findViewById(R.id.dueDateEdit);
            editTextDuedate.setText(dueDate.toString());
        }


        Boolean isCompleted = todo.isCompleted();
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        if(isCompleted){
            checkBox1.setChecked(true);
        }

        Boolean isImportant = todo.isImportant();
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        if(isImportant){
            checkBox2.setChecked(true);
        }

        String notes = todo.getNotes();
        EditText editTextNotes = findViewById(R.id.notesEdit);
        if(notes != null){
            editTextNotes.setText(notes);
        }

    }

    public void onSave(View view){
        Intent replyIntent = new Intent();
        if(datePicked2 !=null) {
            todo.setDueDate(datePicked2);
        }
        EditText editTextNotes = findViewById(R.id.notesEdit);
        todo.setNotes(editTextNotes.getText().toString());
        Log.d("debug555", editTextNotes.getText().toString()+"");

        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        Boolean isCompleted = checkBox1.isChecked();
        if(isCompleted){
            todo.setCompleted(true);
        }
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        Boolean isImportant = checkBox2.isChecked();
        if(isImportant){
            todo.setImportant(true);
        }
        Log.d("debug555", ""+todo.isCompleted());
        replyIntent.putExtra("todoEdit", todo);

        replyIntent.putExtra("isDelete", false);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void showDatePickerDialogEdit(View v){
        DialogFragment dialogFragmentDateEdit = new DatePickerFragmentEdit();
        dialogFragmentDateEdit.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDelete(View view){
        Intent replyIntent = new Intent();
        replyIntent.putExtra("isDelete", true);
        replyIntent.putExtra("todoDelete", todo);
        setResult(RESULT_OK, replyIntent);
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
