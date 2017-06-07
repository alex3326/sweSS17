package at.sw2017.todo4u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.sw2017.todo4u.adapter.TaskAdapter;
import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.database.TasksDataSource;
import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;


public class TaskListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private ListView task_list_view;
    private ArrayAdapter<Task> adapter;
    private TasksDataSource tds;
    private SearchView searchView;
    private boolean showFinishedList = false;
    private TaskCategory category = null;
    private SortOption sorted_by = SortOption.DUE_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);


        long categoryId = getIntent().getLongExtra("id", 0);

        tds = new TasksDataSource(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_task);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(this);
        tcDs.openReadonly();
        category = tcDs.getById(categoryId);
        String categoryName = "";
        if (category != null) {
            categoryName = category.getName();
        }

        setTitle(getString(R.string.task_list_title, categoryName));

        adapter = new TaskAdapter(this, android.R.layout.simple_list_item_1);
        task_list_view = (ListView) findViewById(R.id.task_list_view);
        task_list_view.setAdapter(adapter);

        task_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                if (o instanceof Task) {
                    Task t = (Task) o;
                    Intent intent = new Intent(TaskListActivity.this, TaskAddActivity.class);
                    intent.putExtra("task", t.getId());
                    startActivity(intent);
                }
            }
        });

        updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasklist, menu);

        searchView = (SearchView) menu.findItem(R.id.bt_search_task).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bt_add_task) {
            Intent intent = new Intent(TaskListActivity.this, TaskAddActivity.class);
            intent.putExtra("id", category.getId());
            startActivity(intent);
        } else if (id == R.id.bt_search_task) {
            return true;
        } else if (id == R.id.bt_finished) {
            showFinishedList = true;
            updateData();
        } else if (id == R.id.bt_sort) {
            if (sorted_by == SortOption.DUE_DATE) {
                sorted_by = SortOption.PROGRESS;
                adapter.sort(new TaskProgressComparator());
            } else if (sorted_by == SortOption.PROGRESS) {
                sorted_by = SortOption.DUE_DATE;
                adapter.sort(new TaskDueDateComparator());
            }
        } else if (id == android.R.id.home) {
            if (showFinishedList) {
                showFinishedList = false;
                updateData();
            } else {
                Intent intent = new Intent(TaskListActivity.this, CategoryListActivity.class);
                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (showFinishedList) {
            showFinishedList = false;
            updateData();
        } else {
            Intent intent = new Intent(TaskListActivity.this, CategoryListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        tds.open();
        adapter.clear();
        List<Task> tasks;
        if (showFinishedList) {
            setTitle(R.string.task_list_title_finished);
            tasks = tds.getFinishedTasksInCategory(category);
        } else {
            setTitle(getString(R.string.task_list_title, category.getName()));
            tasks = tds.getNotFinishedTasksInCategory(category);
        }

        Comparator<Task> comparator =
                (sorted_by == SortOption.PROGRESS) ? new TaskProgressComparator()
                        : new TaskDueDateComparator();
        Collections.sort(tasks, comparator);

        adapter.addAll(tasks);
        tds.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.trim().isEmpty()) {
            updateData();
        } else {
            adapter.clear();
            tds.open();
            adapter.addAll(tds.getTasksInCategoryWithTitle(category.getId(), newText));
            tds.close();
            adapter.notifyDataSetChanged();
        }

        return false;
    }

    private enum SortOption {DUE_DATE, PROGRESS}
}

class TaskDueDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getDueDate() == null) {
            return 1;
        } else if (task2.getDueDate() == null) {
            return -1;
        } else {
            return task1.getDueDateAsNumber().compareTo(task2.getDueDateAsNumber());
        }
    }
}

class TaskProgressComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.getProgress() - task2.getProgress();
    }
}