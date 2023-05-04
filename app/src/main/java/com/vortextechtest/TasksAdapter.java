package com.vortextechtest;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

    private Context mContext;
    private List<Task> tasksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTaskTitle;
        public RelativeLayout rlItem;

        public MyViewHolder(View view) {
            super(view);
            rlItem = (RelativeLayout) view.findViewById(R.id.rlItem);
            tvTaskTitle = (TextView) view.findViewById(R.id.tvTaskTitle);
        }
    }


    public TasksAdapter(Context mContext, List<Task> tasksList) {
        this.mContext = mContext;
        this.tasksList = tasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Task task = tasksList.get(position);

        holder.tvTaskTitle.setText(task.title);

        if(task.marked)
            holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() |   Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.tvTaskTitle.setPaintFlags(0);

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateMark(task, !task.marked);

                if(task.marked)
                    holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() |   Paint.STRIKE_THRU_TEXT_FLAG);
                else
                    holder.tvTaskTitle.setPaintFlags(0);

                // update remain text
                String remainStr = mContext.getResources().getString(R.string.main_screen_tv_remaining_tasks);
                remainStr = remainStr.replace("*1*", 0+"");
                remainStr = remainStr.replace("*2*", getItemCount()+"");

                MainActivity.tvRemainingTasks.setText(remainStr);
            }
        });

    }

    private void updateMark(Task task, boolean mark) {
        try {
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            task.marked = mark;
            realm.insertOrUpdate(task);
            realm.commitTransaction();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}