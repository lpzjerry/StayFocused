package edu.dartmouth.stayfocus.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.TodoActivity;
import edu.dartmouth.stayfocus.TodoEditActivity;
import edu.dartmouth.stayfocus.ui.home.HomeFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    Calendar calendar = Calendar.getInstance();
    private HomeFragment homeFragment;
    private View customLayout;


    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public void setCustomLayout(View customLayout){
        this.customLayout = customLayout;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(dayOfMonth);
        homeFragment.datePicker = date;


        EditText editText = customLayout.findViewById(R.id.editDate);
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
