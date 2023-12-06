package com.example.todoapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddAndUpdateTask;
import com.example.todoapp.HomeActivity;
import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.Connectivity;
import com.example.todoapp.Utils.JavaMailAPI;
import com.example.todoapp.Utils.MyDatabase;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>
{
    private List<ToDoModel> todos_list;
    private HomeActivity homeActivity;
    private MyDatabase db;

    public ToDoAdapter(MyDatabase db, HomeActivity homeActivity) {
        this.db = db;
        this.homeActivity = homeActivity;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        final ToDoModel item = todos_list.get(position);
        holder.checkBox.setText(item.getTitle());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));

        if (item.getStatus() == 1)
        {
            SpannableString spannableString = new SpannableString(item.getTitle());
            spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkBox.setText(spannableString);
        }
        else
        {
            holder.checkBox.setText(item.getTitle());
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                db.updateTaskStatus(item.getId(), 1);
                SpannableString spannableString = new SpannableString(todos_list.get(position).getTitle());
                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.checkBox.setText(spannableString);
                db.updateTaskTitle(item.getId(), spannableString.toString());
            }
            else
            {
                db.updateTaskStatus(item.getId(), 0);
                holder.checkBox.setText(item.getTitle());

            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public Context getContext() {
        return homeActivity;
    }

    public void setTasks(List<ToDoModel> todos_list) {
        this.todos_list = todos_list;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = todos_list.get(position);
        db.deleteTask(item.getId());

        String email = db.getUserEmail(item.getUser_id());
        String subject = "⛔ " + homeActivity.getResources().getString(R.string.task_deleted_email_subject);
        String body = homeActivity.getResources().getString(R.string.task_deleted_email_body) + ": \n- " + item.getTitle();
        if (Connectivity.isNetworkConnected(homeActivity))
        {
            JavaMailAPI.sendMail(homeActivity, email, subject, body);
        } else {
            Connectivity.showNoInternetMessage(homeActivity);
        }

        todos_list.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position, ToDoAdapter adapter) {
        ToDoModel item = todos_list.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("title", item.getTitle());
        bundle.putInt("status", item.getStatus());
        bundle.putInt("user_id", item.getUser_id());

        String editTaskHint = homeActivity.getResources().getString(R.string.edit_task_hint);
        String editButtonText = homeActivity.getResources().getString(R.string.edit_task_button_text);
        AddAndUpdateTask fragment = new AddAndUpdateTask(editTaskHint, editButtonText, adapter);
        fragment.setArguments(bundle);
        fragment.show(homeActivity.getSupportFragmentManager(), fragment.getTag());
    }

    @Override
    public int getItemCount() {
        return todos_list.size();
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        public ToDoViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoCheckbox);
        }
    }
}
