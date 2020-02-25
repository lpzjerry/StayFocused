package edu.dartmouth.stayfocus.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import edu.dartmouth.stayfocus.R;

public class TimePickerFragment extends DialogFragment
       implements TimePickerDialog.OnTimeSetListener {
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText editText = getActivity().findViewById(R.id.editTime);
        editText.setText(hourOfDay+":"+minute);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
