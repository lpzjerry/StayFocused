package edu.dartmouth.stayfocus.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Date;

import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.TodoEditActivity;
import edu.dartmouth.stayfocus.room.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {
    private HomeFragment context;

    class TodoListViewHolder extends RecyclerView.ViewHolder{
        private final TextView todoItemView1;

        private final TextView todoItemView2;

        private TodoListViewHolder(View itemView){
            super(itemView);
            todoItemView1 = itemView.findViewById(R.id.textViewRecycle1);
            todoItemView2 = itemView.findViewById(R.id.textViewRecycle2);
        }
    }

    private final LayoutInflater mInflater;

    private List<Todo> mTodoList; //Cached a copy of todoList

    TodoListAdapter(HomeFragment context){
        this.context = context;
        mInflater = LayoutInflater.from(context.getContext());
    }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TodoListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoListViewHolder holder, int position){
        if(mTodoList != null){
            Todo current = mTodoList.get(position);
            holder.todoItemView1.setText(current.getTitle());
            if(current.isCompleted()){
                holder.todoItemView1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if(current.getDueDate() != null) {
                holder.todoItemView2.setText(current.getDueDate().toString());
            }else{
                holder.todoItemView2.setText("No Duedate Set");
            }
        }else{
            holder.todoItemView1.setText("No TodoItem");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo = mTodoList.get(position);
                Intent intent = new Intent(context.getContext(), TodoEditActivity.class);
                intent.putExtra("todo", todo);
                ((HomeFragment)context).startActivityForResult(intent, 2);
            }
        });
    }

    void setTodoLists(List<Todo> todos){
        mTodoList = todos;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTodoList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTodoList != null) {
            return mTodoList.size();
        }
        else return 0;
    }
}
