package edu.dartmouth.stayfocus.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.dartmouth.stayfocus.Entry;
import edu.dartmouth.stayfocus.FirebaseHelper;
import edu.dartmouth.stayfocus.R;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;
    Calendar rightNow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Button button = (Button)root.findViewById(R.id.test_button);
        Button feedbackButton = (Button)root.findViewById(R.id.feedback_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rightNow = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(rightNow.getTime());
                Entry item = new Entry(formattedDate,formattedDate, "30min", "failed");
                FirebaseHelper helper = new FirebaseHelper();
                helper.addEntry(item);

            }
        });
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        return root;
    }


}