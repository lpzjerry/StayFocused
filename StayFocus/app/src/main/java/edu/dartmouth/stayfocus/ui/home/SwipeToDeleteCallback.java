package edu.dartmouth.stayfocus.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private TodoListAdapter mAdapter;

    public SwipeToDeleteCallback(TodoListAdapter adapter){
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);

    }
}
