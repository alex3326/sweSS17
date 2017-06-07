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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.sw2017.todo4u.adapter.CategorySpinnerAdapter;
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
    private Spinner spinnerCategory;
    private SeekBar seekProgress;
    private TasksDataSource tds;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.task_add_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long categoryId = getIntent().getLongExtra("id", 0);
        long taskId = getIntent().getLongExtra("task", 0);
        tds = new TasksDataSource(this);

        calDueDate = Calendar.getInstance();
        calDueDate.clear();
        calReminderDate = Calendar.getInstance();
        calReminderDate.clear();

        tfTitle = (EditText) findViewById(R.id.task_add_tfTitle);
        tfDescription = (EditText) findViewById(R.id.task_add_tfDescription);
        btDueDate = (Button) findViewById(R.id.task_add_btDueDate);
        btReminderDate = (Button) findViewById(R.id.task_add_btReminderDate);
        spinnerCategory = (Spinner) findViewById(R.id.task_add_spinnerCategory);
        seekProgress = (SeekBar) findViewById(R.id.task_add_seekProgress);


        TaskCategoriesDataSource tcDs = new TaskCategoriesDataSource(this);
        tcDs.open();
        final List<TaskCategory> categories = tcDs.getAll();
        tcDs.close();

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        setTitle(R.string.task_add_title);

        if (taskId > 0) {
            tds.openReadonly();
            task = tds.getById(taskId);
            tds.close();

            tfTitle.setText(task.getTitle());
            tfDescription.setText(task.getDescription());
            if (task.getDueDate() != null) {
                calDueDate.setTimeInMillis(task.getDueDateAsNumber());
                String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calDueDate.getTime());
                btDueDate.setText(formattedDate);
            }
            if (task.getReminderDate() != null) {
                calReminderDate.setTimeInMillis(task.getReminderDateAsNumber());
                String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calReminderDate.getTime());
                btReminderDate.setText(formattedDate);
            }
            spinnerCategory.setSelection(categories.indexOf(task.getCategory()));
            categoryId = task.getCategory().getId();
            seekProgress.setProgress(task.getProgress());

            setTitle(R.string.task_edit_title);

        } else if (categoryId > 0) {
            TaskCategory myCat = new TaskCategory();
            myCat.setId(categoryId);
            spinnerCategory.setSelection(categories.indexOf(myCat));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.task_add_btSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields())
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
        if (task == null) {
            task = new Task();
            task.setCreationDate(new Date());
        }

        task.setTitle(tfTitle.getText().toString());
        task.setDescription(tfDescription.getText().toString());
        if (calDueDate.isSet(Calendar.DAY_OF_MONTH)) {
            task.setDueDate(calDueDate.getTimeInMillis());
        } else {
            task.setDueDate(null);
        }
        if (calReminderDate.isSet(Calendar.DAY_OF_MONTH)) {
            task.setReminderDate(calReminderDate.getTimeInMillis());
        } else {
            task.setReminderDate(null);
        }
        task.setDescription(tfDescription.getText().toString());

        Object selectedCat = spinnerCategory.getSelectedItem();
        if (selectedCat instanceof TaskCategory) {
            task.setCategory((TaskCategory) selectedCat);
        }

        task.setProgress(seekProgress.getProgress());

        tds.insertOrUpdate(task);
        tds.close();

        finish();
    }

    private boolean validateFields() {
        if (tfTitle.getText().toString().isEmpty() || tfDescription.getText().toString().isEmpty()
                || !calDueDate.isSet(Calendar.DAY_OF_MONTH)) {
            Toast.makeText(getApplicationContext(), R.string.task_add_error_required_fields, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (calReminderDate.isSet(Calendar.DAY_OF_MONTH) && calDueDate.isSet(Calendar.DAY_OF_MONTH) && calReminderDate.compareTo(calDueDate) > 0) {
            Toast.makeText(getApplicationContext(), R.string.task_add_error_dates_reminderAfterDue, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onDueDateSelect(View view) {
        pickDate(calDueDate, btDueDate, R.string.task_add_setDueDate);
    }

    public void onReminderDateSelect(View view) {
        pickDate(calReminderDate, btReminderDate, R.string.task_add_setReminderDate);
    }

    private void pickDate(final Calendar cal, final Button button, int defaultButtonText) {
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        cal.clear();
        button.setText(defaultButtonText);
    }
}
