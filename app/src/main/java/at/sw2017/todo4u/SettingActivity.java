package at.sw2017.todo4u;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import at.sw2017.todo4u.database.SettingsDataSource;
import at.sw2017.todo4u.model.Setting;

public class SettingActivity extends AppCompatActivity {

    private SettingsDataSource sds;
    private Spinner spinnerColor;
    private Setting settingCategoryColorOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sds = new SettingsDataSource(this);
        sds.open();
        settingCategoryColorOption = sds.getSettingByKey(Setting.KEY_CATEGORY_COLOR_OPTION);
        sds.close();

        spinnerColor = (Spinner) findViewById(R.id.settings_spinnerCategoryColor);
        spinnerColor.setSelection(settingCategoryColorOption.getValue());


        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sds.open();
                settingCategoryColorOption.setValue(position);
                sds.insertOrUpdate(settingCategoryColorOption);
                sds.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

}
