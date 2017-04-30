package at.sw2017.swess17;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sw2017.swess17.application.Task;

public class TaskViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        Date date = new Date();
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Task1", "Only a task", date));
        taskList.add(new Task("Task2", "Only a task", date));
        taskList.add(new Task("Task3", "Only a task", date));
        taskList.add(new Task("Task4", "Only a task", date));
        taskList.add(new Task("Task5", "Only a task", date));
        taskList.add(new Task("Task6", "Only a task", date));

        String[] taskNames = taskList.stream().map(x -> x.getName()).toArray(size -> new String[size]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_task_view, taskNames);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
