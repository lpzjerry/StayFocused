package edu.dartmouth.stayfocus.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TodoList.class}, version =  1, exportSchema = false)
public abstract class TodoListRoomDatabase extends RoomDatabase {

    public abstract TodoListDao todoListDao();

    private static volatile TodoListRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
   public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TodoListRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TodoListRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoListRoomDatabase.class, "todolist_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
