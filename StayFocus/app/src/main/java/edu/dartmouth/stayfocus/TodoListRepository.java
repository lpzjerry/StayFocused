package edu.dartmouth.stayfocus;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.dartmouth.stayfocus.room.TodoListDao;
import edu.dartmouth.stayfocus.room.TodoList;
import edu.dartmouth.stayfocus.room.TodoListRoomDatabase;

public class TodoListRepository {
    private TodoListDao mTodoListDao;
    private LiveData<List<TodoList>> mALLTodoListEntry;

    public TodoListRepository(Application application){
        TodoListRoomDatabase db = TodoListRoomDatabase.getDatabase(application);
        mTodoListDao = db.todoListDao();
        mALLTodoListEntry = mTodoListDao.getAllTodoList();
    }

    public LiveData<List<TodoList>> getAllTodoList(){
        return mALLTodoListEntry;
    }

    public void insert(TodoList todoList){
        TodoListRoomDatabase.databaseWriteExecutor.execute(() ->{
            mTodoListDao.insert(todoList);
        });
    }
}
