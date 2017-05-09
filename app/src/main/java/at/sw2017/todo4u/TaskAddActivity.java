package at.sw2017.todo4u;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.database.TasksDataSource;
import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;

public class TaskAddActivity extends AppCompatActivity {

    private EditText tfTitle;
    private EditText tfDescription;
    private Button btDueDate;
    private Calendar calDueDate;
    private Button btReminderDate;
    private Calendar calReminderDate;
    private TasksDataSource tds;
    private long categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.task_add_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        categoryId = getIntent().getLongExtra("id", 0);
        tds = new TasksDataSource(this);

        calDueDate = Calendar.getInstance();
        calDueDate.clear();
        calReminderDate = Calendar.getInstance();
        calReminderDate.clear();

        tfTitle = (EditText) findViewById(R.id.task_add_tfTitle);
        tfDescription = (EditText) findViewById(R.id.task_add_tfDescription);
        btDueDate = (Button) findViewById(R.id.task_add_btDueDate);
        btReminderDate = (Button) findViewById(R.id.task_add_btReminderDate);

        setTitle(R.string.task_add_title);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.task_add_btSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmptyFields())
                    updateData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData() {
        tds.open();
        TaskCategory cat = null;
        if (categoryId != 0) {
            TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(this);
            tcDs.openReadonly();
            cat = tcDs.getById(categoryId);
            tcDs.close();
        }
        Task task = new Task(tfTitle.getText().toString());
        task.setDescription(tfDescription.getText().toString());
        if (calDueDate.isSet(Calendar.DAY_OF_MONTH)) {
            task.setDueDate(calDueDate.getTimeInMillis());
        }
        if(calReminderDate.isSet(Calendar.DAY_OF_MONTH)) {
            task.setReminderDate(calReminderDate.getTimeInMillis());
        }
        task.setDescription(tfDescription.getText().toString());
        task.setCategory(cat);
        task.setState(Task.State.OPEN);

        tds.insertOrUpdate(task);
        tds.close();

        finish();
    }

    private boolean checkEmptyFields() {
        if (tfTitle.getText().toString().isEmpty() || tfDescription.getText().toString().isEmpty()
                || !calDueDate.isSet(Calendar.DAY_OF_MONTH)) {
            Toast.makeText(getApplicationContext(), R.string.task_add_error_required_fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onDueDateSelect(View view) {
        pickDate(calDueDate, btDueDate);
    }

    public void onReminderDateSelect(View view) {
        pickDate(calReminderDate, btReminderDate);
    }

    private void pickDate(final Calendar cal, final Button button) {
        if (!cal.isSet(Calendar.DAY_OF_MONTH)) {
            cal.setTimeInMillis(System.currentTimeMillis());
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime());
                        button.setText(formattedDate);

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.clear();
        datePickerDialog.show();
    }
}
