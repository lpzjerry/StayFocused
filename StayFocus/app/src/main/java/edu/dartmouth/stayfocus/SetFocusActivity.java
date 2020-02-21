package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.dartmouth.stayfocus.ui.setfocus.SetFocusFragment;

public class SetFocusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_focus_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SetFocusFragment.newInstance())
                    .commitNow();
        }
    }

    public void onClickSetFocusStart(View view) {
        Intent intent = new Intent(this, CountdownTimerActivity.class);
        Bundle bundle = new Bundle();
        int hour = 0, minute = 0, second = 0;
        EditText hourText = (EditText) findViewById(R.id.et_hour);
        hour = Integer.parseInt(hourText.getText().toString());
        EditText minuteText = (EditText) findViewById(R.id.et_minute);
        minute = Integer.parseInt(minuteText.getText().toString());
        EditText secondText = (EditText) findViewById(R.id.et_second);
        second = Integer.parseInt(secondText.getText().toString());
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
