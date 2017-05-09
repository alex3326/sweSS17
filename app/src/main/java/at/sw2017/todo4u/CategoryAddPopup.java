package at.sw2017.todo4u;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.model.TaskCategory;

public class CategoryAddPopup extends AppCompatActivity {

    private TaskCategoriesDataSource tcds;

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

        getWindow().setLayout((int) (Width * .8), (int) (height * .50));
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
