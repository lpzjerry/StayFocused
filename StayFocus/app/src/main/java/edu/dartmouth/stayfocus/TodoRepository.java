package edu.dartmouth.stayfocus;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.dartmouth.stayfocus.room.TodoDao;
import edu.dartmouth.stayfocus.room.Todo;
import edu.dartmouth.stayfocus.room.TodoRoomDatabase;

public class TodoRepository {
    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodoList;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TodoRepository(Application application){
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodoList = mTodoDao.getAllTodoList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Todo>> getAllTodoList(){
        return mAllTodoList;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Todo todo){
        TodoRoomDatabase.databaseWriteExecutor.execute(() ->{
            mTodoDao.insert(todo);
        });
        Log.d("debug555", "repository called");
    }

    public void delete(Todo todo){
        TodoRoomDatabase.databaseWriteExecutor.execute(() ->{
            mTodoDao.delete(todo);
        });
    }

    public void update(Todo todo){
        TodoRoomDatabase.databaseWriteExecutor.execute(() ->{
            mTodoDao.update(todo);
        });
    }
}
