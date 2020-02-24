package edu.dartmouth.stayfocus.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.room.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    class TodoListViewHolder extends RecyclerView.ViewHolder{
        private final TextView todoItemView;

        private TodoListViewHolder(View itemView){
            super(itemView);
            todoItemView = itemView.findViewById(R.id.textViewRecycle);
        }
    }

    private final LayoutInflater mInflater;

    private List<Todo> mTodoList; //Cached a copy of todoList

    TodoListAdapter(Context context){ mInflater = LayoutInflater.from(context); }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TodoListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoListViewHolder holder, int position){
        if(mTodoList != null){
            Todo current = mTodoList.get(position);
            holder.todoItemView.setText(current.getTitle());
        }else{
            holder.todoItemView.setText("No TodoItem");
        }
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
