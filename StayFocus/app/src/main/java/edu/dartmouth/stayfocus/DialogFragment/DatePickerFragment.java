package edu.dartmouth.stayfocus.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import edu.dartmouth.stayfocus.R;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText editText = getActivity().findViewById(R.id.editDate);
        editText.setText(year+"/"+(month+1)+"/"+dayOfMonth);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
}
