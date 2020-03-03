package edu.dartmouth.stayfocus.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Date;

import edu.dartmouth.stayfocus.R;
import edu.dartmouth.stayfocus.TodoEditActivity;
import edu.dartmouth.stayfocus.room.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {
    private HomeFragment context;

    private HomeViewModel homeViewModel;

    class TodoListViewHolder extends RecyclerView.ViewHolder{
        private final TextView todoItemView1;

        private final TextView todoItemView2;

        private final CheckBox checkBox;

        private TodoListViewHolder(View itemView){
            super(itemView);
            todoItemView1 = itemView.findViewById(R.id.textViewRecycle1);
            todoItemView2 = itemView.findViewById(R.id.textViewRecycle2);
            checkBox = itemView.findViewById(R.id.checkBoxComplete);
        }
    }

    private final LayoutInflater mInflater;

    private List<Todo> mTodoList; //Cached a copy of todoList

    TodoListAdapter(HomeFragment context){
        this.context = context;
        mInflater = LayoutInflater.from(context.getContext());
    }

    public void setHomeViewModel(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
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
            }else{
                holder.todoItemView1.setPaintFlags(0);
            }
            if(current.getDueDate() != null) {
                holder.todoItemView2.setText(current.getDueDate().toString());
            }else{
                holder.todoItemView2.setText("No Duedate Set");
            }
            holder.checkBox.setChecked(current.isCompleted());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    current.setCompleted(isChecked);
                    homeViewModel.update(current);
                }
            });
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

    public void deleteItem(int position){
        Todo mTodo = mTodoList.get(position);
        homeViewModel.delete(mTodo);
        notifyItemRemoved(position);
        Log.d("debug555", "deleteItem called");
    }
}
