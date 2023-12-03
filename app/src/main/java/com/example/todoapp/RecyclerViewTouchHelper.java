package com.example.todoapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapters.ToDoAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;
    public RecyclerViewTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT)
        {
            alertDeleteDialog(position);
        }
        else
        {
            adapter.editTask(position);
        }
    }

    private void alertDeleteDialog(int position)
    {
        new AlertDialog.Builder(new ContextThemeWrapper(adapter.getContext(), R.style.CustomAlertDialogTheme))
                .setTitle(R.string.delete_task_dialog_title)
                .setMessage(R.string.confirm_delete_task_message)
                .setPositiveButton(R.string.positive_choice, (dialog, which) -> {
                    adapter.deleteTask(position);
                    Toast.makeText(adapter.getContext(), "✅ " + adapter.getContext().getResources().getString(R.string.confirm_task_deletion), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.negative_choice, (dialog, which) -> {
                    adapter.notifyItemChanged(position);
                })
                .show();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.cozy_red))
                .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.light_blue))
                .addSwipeRightActionIcon(R.drawable.baseline_edit_24)
                .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 20)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
