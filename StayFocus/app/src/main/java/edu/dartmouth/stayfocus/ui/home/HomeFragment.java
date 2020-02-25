package edu.dartmouth.stayfocus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.TodoActivity;
import edu.dartmouth.stayfocus.TodoEditActivity;
import edu.dartmouth.stayfocus.room.Todo;

import static android.app.Activity.RESULT_OK;
import static edu.dartmouth.stayfocus.TodoActivity.EXTRA_REPLY;

public class HomeFragment extends Fragment {

    public HomeFragment(){

    }

    private HomeViewModel homeViewModel;
    FloatingActionButton fab;

    public static final int TODOACTIVITY_REQUEST_CODE = 1;
    public static final int TODOEDITACTIVITY_REQUEST_CODE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllTodoList().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(@NonNull List<Todo> todos) {
                Log.d("debug", ""+ todos);
                adapter.setTodoLists(todos);
            }
        });

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TodoActivity.class);
                startActivityForResult(intent, TODOACTIVITY_REQUEST_CODE);
            }
        });
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TODOACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Todo todo = new Todo();
            String title = data.getStringExtra(EXTRA_REPLY);
            todo.setTitle(title);
            String notes = data.getStringExtra("notes");
            todo.setNotes(notes);
            Date dueDate = (Date)data.getSerializableExtra("duedate");
            Date createDate = new Date();
            todo.setDueDate(dueDate);
            todo.setCreateTime(createDate);
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
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }

    }
}