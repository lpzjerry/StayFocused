package edu.dartmouth.stayfocus.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import edu.dartmouth.stayfocus.DialogFragment.DatePickerFragment;
import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.TodoActivity;
import edu.dartmouth.stayfocus.TodoEditActivity;
import edu.dartmouth.stayfocus.room.Todo;

import static android.app.Activity.RESULT_OK;
import static edu.dartmouth.stayfocus.R.*;
import static edu.dartmouth.stayfocus.R.color.colorPrimary;
import static edu.dartmouth.stayfocus.TodoActivity.EXTRA_REPLY;

public class HomeFragment extends Fragment {

    public HomeFragment(){

    }

    private HomeViewModel homeViewModel;
    FloatingActionButton fab;
    public Date datePicker;

    public static final int TODOACTIVITY_REQUEST_CODE = 1;
    public static final int TODOEDITACTIVITY_REQUEST_CODE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(layout.fragment_todo, container, false);

        RecyclerView recyclerView = root.findViewById(id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllTodoList().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(@NonNull List<Todo> todos) {
                Log.d("debug", ""+ todos);
                adapter.setTodoLists(todos);
            }
        });
        adapter.setHomeViewModel(homeViewModel);

        fab = root.findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog("Todo");
            }
        });
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Add new todoitem was done by new activity at first, the first if condition holds this situation
        //Now add todoitem is done through showInputDialog, the first if condition is useless here but the code remains
        if(requestCode == TODOACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Todo todo = (Todo)data.getSerializableExtra(EXTRA_REPLY);
            homeViewModel.insert(todo);
        }else if(requestCode == TODOEDITACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Boolean isDelete = data.getBooleanExtra("isDelete", false);
            if(!isDelete) {
                Todo todo = (Todo) data.getSerializableExtra("todoEdit");
                Log.d("debug555", "onActivitySaveCalled" + todo.isCompleted() + todo.getTitle());
                homeViewModel.update(todo);
            }else{
                Todo todo = (Todo)data.getSerializableExtra("todoDelete");
                homeViewModel.delete(todo);
            }
        }else{
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }

    }
    public void showInputDialog(String title){
        datePicker = null;
        final View customLayout = getLayoutInflater().inflate(layout.activity_todo, null);

        customLayout.findViewById(id.editDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setHomeFragment(HomeFragment.this);
                datePickerFragment.setCustomLayout(customLayout);
                datePickerFragment.show(getParentFragmentManager(), "datePicker");
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(),R.style.DialogTheme)
                .setView(customLayout)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


        TextView okButton = customLayout.findViewById(id.bnTodoOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Todo mTodo = new Todo();
                    EditText editTextTitle = (EditText)customLayout.findViewById(id.title);
                    if(editTextTitle != null && !editTextTitle.getText().toString().isEmpty()){
                        mTodo.setTitle(editTextTitle.getText().toString());
                        mTodo.setDueDate(datePicker);
                        Date createTime = new Date();
                        mTodo.setCreateTime(createTime);
                        homeViewModel.insert(mTodo);
                    }else{
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                string.empty_not_saved,
                                Toast.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();

            }
        });
        //AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle(title)




    }
    public Activity getActivity(Fragment fragment) {
        if (fragment == null) {
            return null;
        }
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment.getActivity();
    }
}