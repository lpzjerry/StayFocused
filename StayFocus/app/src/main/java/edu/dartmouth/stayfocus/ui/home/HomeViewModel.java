package edu.dartmouth.stayfocus.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.dartmouth.stayfocus.TodoRepository;
import edu.dartmouth.stayfocus.room.Todo;

public class HomeViewModel extends AndroidViewModel{
    private TodoRepository mRepository;

    private LiveData<List<Todo>> mAllTodoList;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodoList = mRepository.getAllTodoList();
    }

    LiveData<List<Todo>> getAllTodoList(){
        return mAllTodoList;
    }

    public void insert(Todo todo){
        mRepository.insert(todo);
        Log.d("debug555", "homeviewmodel insert called");
    }

    public void delete(Todo todo){
        mRepository.delete(todo);
        Log.d("debug555", "homeviewmodel delete called");
    }

    public void update(Todo todo){
        mRepository.update(todo);
    }

}