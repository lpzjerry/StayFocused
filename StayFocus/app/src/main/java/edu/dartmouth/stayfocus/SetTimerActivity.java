package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);
    }

    public void onClickSetFocusStart(View view) {
        Intent intent = new Intent(this, FocusingActivity.class);
        Bundle bundle = new Bundle();
        int hour = 0, minute = 0, second = 0;

        EditText hourText = (EditText) findViewById(R.id.et_hour);
        String hourStr = hourText.getText().toString();
        if (!hourStr.isEmpty())
            hour = Integer.parseInt(hourStr);
        // Minute
        EditText minuteText = (EditText) findViewById(R.id.et_minute);
        String minuteStr = minuteText.getText().toString();
        if (!minuteStr.isEmpty())
            minute = Integer.parseInt(minuteStr);
        // Second
        EditText secondText = (EditText) findViewById(R.id.et_second);
        String secondStr = secondText.getText().toString();
        if (!secondStr.isEmpty())
            second = Integer.parseInt(secondStr);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);
        bundle.putInt("second", second);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    public void onClickSetFocusCancel(View view) {
        this.finish();
    }
}
