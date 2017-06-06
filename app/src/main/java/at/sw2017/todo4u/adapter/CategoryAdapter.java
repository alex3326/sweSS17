package at.sw2017.todo4u.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.sw2017.todo4u.R;
import at.sw2017.todo4u.database.SettingsDataSource;
import at.sw2017.todo4u.database.TasksDataSource;
import at.sw2017.todo4u.model.Setting;
import at.sw2017.todo4u.model.TaskCategory;


public class CategoryAdapter extends ArrayAdapter<TaskCategory> {
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Activity activity, int textViewResourceId, List<TaskCategory> items) {
        super(activity, textViewResourceId, items);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.category_list_item, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.categorylistitem_name);
                holder.display_count = (TextView) vi.findViewById(R.id.categorylistitem_count);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            TaskCategory cat = getItem(position);
            TasksDataSource tds = new TasksDataSource(getContext());
            tds.openReadonly();
            int count = tds.getNotFinishedTasksInCategory(cat).size();
            tds.close();


            holder.display_name.setText(cat.getName());
            holder.display_count.setText(String.format("%d", count));

            SettingsDataSource sds = new SettingsDataSource(getContext());
            sds.open();
            Integer catColorOption = sds.getSettingByKey(Setting.KEY_CATEGORY_COLOR_OPTION).getValue();

            if (catColorOption == 0) {
                // None
                vi.setBackgroundColor(Color.WHITE);
            } else if (catColorOption == 1) {
                // Colorful
                int color;
                switch (cat.getColor()) {
                    default:
                    case NONE:
                        color = Color.WHITE;
                        break;
                    case RED:
                        color = Color.argb(30, 200, 20, 30);
                        break;
                    case GREEN:
                        color = Color.argb(30, 30, 200, 20);
                        break;
                    case YELLOW:
                        color = Color.argb(30, 220, 255, 0);
                        break;
                    case BLUE:
                        color = Color.argb(30, 20, 30, 200);
                        break;
                    case CYAN:
                        color = Color.argb(30, 0, 183, 235);
                        break;
                }
                vi.setBackgroundColor(color);
            } else if (catColorOption == 2) {
                //Gray and White
                if (position % 2 == 0) {
                    vi.setBackgroundColor(Color.argb(30, 0, 0, 0));
                } else {
                    vi.setBackgroundColor(Color.WHITE);
                }
            }

        } catch (Exception e) {
        }

        return vi;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_count;

    }
}
