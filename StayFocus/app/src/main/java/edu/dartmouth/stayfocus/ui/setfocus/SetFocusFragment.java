package edu.dartmouth.stayfocus.ui.setfocus;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dartmouth.stayfocus.CountdownTimerActivity;
import edu.dartmouth.stayfocus.R;

public class SetFocusFragment extends Fragment {

    private SetFocusViewModel mViewModel;

    public static SetFocusFragment newInstance() {
        return new SetFocusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_focus_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetFocusViewModel.class);
        // TODO: Use the ViewModel
    }

    public void onClickSetFocusStart(View view) {
    }

    public void onClickSetFocusCancel(View view) {
    }

}
