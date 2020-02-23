package edu.dartmouth.stayfocus.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.dartmouth.stayfocus.TodoListRepository;
import edu.dartmouth.stayfocus.room.TodoList;

public class HomeViewModel extends AndroidViewModel{
    private TodoListRepository mRepository;

    private LiveData<List<TodoList>> mAllTodoList;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new TodoListRepository(application);
        mAllTodoList = mRepository.getAllTodoList();
    }

    LiveData<List<TodoList>> getAllTodoList(){
        return mAllTodoList;
    }

    public void insert(TodoList todoList){
        mRepository.insert(todoList);
    }

}