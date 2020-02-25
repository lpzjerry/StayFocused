package edu.dartmouth.stayfocus.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    // allowing the insert of the same task multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);  //insert one task

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * from todo_table ORDER BY due_date ASC")//ASC
    LiveData<List<Todo>> getAllTodoList();

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);
}
