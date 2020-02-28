package edu.dartmouth.stayfocus.Focus;

import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import edu.dartmouth.stayfocus.R;

public class SetTimerDialog extends DialogFragment implements DialogInterface.OnClickListener {
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog ret = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_set_timer, null);
        builder.setView(view);
        builder.setTitle("Set Focusing Time");
        builder.setPositiveButton("Start", this);
        ret = builder.create();
        // Window window = ret.getWindow();
        // window.setGravity(Gravity.BOTTOM);
        return ret;
    }

    public void onClick(DialogInterface dialog, int item) {
        if (item == DialogInterface.BUTTON_POSITIVE) {
            Intent intent = new Intent(getActivity(), FocusingActivity.class);
            Bundle bundle = new Bundle();
            int hour = 0, minute = 1, second = 0;

            EditText hourText = (EditText) view.findViewById(R.id.et_hour);
            String hourStr = hourText.getText().toString();
            if (!hourStr.isEmpty())
                hour = Integer.parseInt(hourStr);
            // Minute
            EditText minuteText = (EditText) view.findViewById(R.id.et_minute);
            String minuteStr = minuteText.getText().toString();
            if (!minuteStr.isEmpty())
                minute = Integer.parseInt(minuteStr);

            bundle.putInt("hour", hour);
            bundle.putInt("minute", minute);
            bundle.putInt("second", second);
            intent.putExtras(bundle);
            // Log.d("pengze", "Dialog: " + hour + " " + minute);
            if (hour > 0 || minute > 0)
                startActivity(intent);
        }
    }
}
