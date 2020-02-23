package edu.dartmouth.stayfocus.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoListDao {

    // allowing the insert of the same task multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TodoList title);  //insert one task

    @Query("DELETE FROM todoList_table")
    void deleteAll();

    @Query("SELECT * from todoList_table ORDER BY title ASC")
    LiveData<List<TodoList>> getAllTodoList();
}
