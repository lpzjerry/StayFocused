package edu.dartmouth.stayfocus.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "todo_table")
public class Todo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    public Todo(@NonNull String title){this.mTitle = title;}

    public String getTitle(){return this.mTitle;}
}
