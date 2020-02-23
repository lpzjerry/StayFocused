package edu.dartmouth.stayfocus.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "todoList_table")
public class TodoList {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    public TodoList(@NonNull String title){this.mTitle = title;}

    public String getTitle(){return this.mTitle;}
}
