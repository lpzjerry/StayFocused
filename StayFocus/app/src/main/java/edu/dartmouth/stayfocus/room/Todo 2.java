package edu.dartmouth.stayfocus.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "todo_table")
public class Todo implements Serializable {

    public Todo(){

    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    @ColumnInfo(name = "notes")
    private String mNotes;

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    @ColumnInfo(name = "creation_time")
    @TypeConverters({TimestampConverter.class})
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ColumnInfo(name = "update_time")
    @TypeConverters({TimestampConverter.class})
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ColumnInfo(name = "due_date")
    @TypeConverters({TimestampConverter.class})
    private Date dueDate;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @ColumnInfo(name = "is_completed")
    private boolean completed;

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    @ColumnInfo(name = "is_important")
    private boolean important;

    public boolean isImportant(){
        return important;
    }

    public void setImportant(boolean important){
        this.important = important;
    }
}
