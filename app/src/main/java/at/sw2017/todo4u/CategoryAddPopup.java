package at.sw2017.todo4u;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.model.TaskCategory;

public class CategoryAddPopup extends AppCompatActivity {

    private TaskCategoriesDataSource tcds;
    private RadioButton radio_none;
    private RadioButton radio_red;
    private RadioButton radio_green;
    private RadioButton radio_yellow;
    private RadioButton radio_blue;
    private RadioButton radio_cyan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tcds = new TaskCategoriesDataSource(this);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_category_add_popup);
        this.setFinishOnTouchOutside(false);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int Width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (Width * .8), (int) (height * .6));

        radio_none = (RadioButton) findViewById(R.id.rbtnNone);
        radio_red = (RadioButton) findViewById(R.id.rbtnRed);
        radio_green = (RadioButton) findViewById(R.id.rbtnGreen);
        radio_yellow = (RadioButton) findViewById(R.id.rbtnYellow);
        radio_blue = (RadioButton) findViewById(R.id.rbtnBlue);
        radio_cyan = (RadioButton) findViewById(R.id.rbtnCyan);

        radio_red.setBackgroundColor(Color.argb(150, 200, 20, 30));
        radio_green.setBackgroundColor(Color.argb(150, 30, 200, 20));
        radio_yellow.setBackgroundColor(Color.argb(150, 220, 255, 0));
        radio_blue.setBackgroundColor(Color.argb(150, 20, 30, 200));
        radio_cyan.setBackgroundColor(Color.argb(150, 0, 183, 235));
    }


    public void saveNewCategory(View v) {

        EditText tx_new_category = (EditText) findViewById(R.id.tx_new_category);

        String categoryName = tx_new_category.getText().toString();

        if (categoryName.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.category_add_error_name_empty,
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            tcds.open();
            TaskCategory taskCategory = new TaskCategory(categoryName);
            taskCategory.setColor(TaskCategory.CategoryColor.NONE);
            if (radio_red.isChecked())
                taskCategory.setColor(TaskCategory.CategoryColor.RED);
            if (radio_green.isChecked())
                taskCategory.setColor(TaskCategory.CategoryColor.GREEN);
            if (radio_yellow.isChecked())
                taskCategory.setColor(TaskCategory.CategoryColor.YELLOW);
            if (radio_blue.isChecked())
                taskCategory.setColor(TaskCategory.CategoryColor.BLUE);
            if (radio_cyan.isChecked())
                taskCategory.setColor(TaskCategory.CategoryColor.CYAN);

            if (tcds.insertOrUpdate(taskCategory)) {
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.category_add_success, categoryName),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.category_add_error_database, categoryName),
                        Toast.LENGTH_SHORT
                ).show();
            }
            tcds.close();
            finish();
        }
    }


    public void cancelPopup(View v) {
        finish();
    }

}
