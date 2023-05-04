package com.vortextechtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAddTask;
    static TextView tvRemainingTasks;

    private RecyclerView m_recyclerView;
    private TasksAdapter m_adapter;
    private List<Task> m_tasksList;
    private RecyclerView.LayoutManager m_layoutManager;

    private int m_tasksCount;
    private int m_tasksRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiate Realm database
        Realm.init(this);

        // init view
        m_recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        etTask = (EditText) findViewById(R.id.etTask);
        btnAddTask = (Button) findViewById(R.id.btnAddTask);
        tvRemainingTasks = (TextView) findViewById(R.id.tvRemainingTasks);

        updateRemainText();

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = etTask.getText().toString();

                int ind = m_tasksCount + 1;
                Task newTask = new Task(ind, taskTitle, false);
                addTaskToSavedTasksList(newTask);
            }
        });

        m_layoutManager = new LinearLayoutManager(this);
        m_recyclerView.setLayoutManager(m_layoutManager);

        getSavedTasksList();
    }

    private void updateRemainText() {

        // update remain text
        String remainStr = getResources().getString(R.string.main_screen_tv_remaining_tasks);
        remainStr = remainStr.replace("*1*", m_tasksRemain+"");
        remainStr = remainStr.replace("*2*", m_tasksCount+"");

        tvRemainingTasks.setText(remainStr);
    }

    /**
     * Get saved tasks from realm local database
     */
    public void getSavedTasksList() {

        m_tasksList = new ArrayList<>();

        try {
            Realm realm=Realm.getDefaultInstance();
            RealmResults<Task> realmModels=realm.where(Task.class).findAll();

            int markedTasks = 0;

            for ( Task task : realmModels ) {

                if(task.marked)
                    markedTasks++;

                Task a = new Task(task.id, task.title, task.marked);
                m_tasksList.add(a);
            }

            m_tasksRemain = m_tasksList.size() - markedTasks;
            m_tasksCount = m_tasksList.size();

            // Initiate recyclerView

            m_adapter = new TasksAdapter(this, m_tasksList);

            m_recyclerView.setItemAnimator(new DefaultItemAnimator());
            m_recyclerView.setAdapter(m_adapter);
        } catch (Exception e){
            e.printStackTrace();
        }

        updateRemainText();
    }

    private void addTaskToSavedTasksList(Task task) {
        try {
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(task);
            realm.commitTransaction();
        } catch (Exception e){
            e.printStackTrace();
        }

        // clean edit text
        etTask.setText("");

        // update tasks list
        getSavedTasksList();
    }
}
