package at.sw2017.todo4u.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import at.sw2017.todo4u.R;
import at.sw2017.todo4u.model.Task;


public class TaskAdapter extends ArrayAdapter<Task> {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<Task> items;

    public TaskAdapter(Activity activity, int textViewResourceId, List<Task> items) {
        super(activity, textViewResourceId, items);
        try {
            this.activity = activity;
            this.items = items;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return items.size();
    }

    public Task getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.task_list_item, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.tasklistitem_name);
                holder.display_days = (TextView) vi.findViewById(R.id.tasklistitem_days);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            Task task = items.get(position);
            long daysRemaining = 0;

            if (task.getDueDate() != null) {
                daysRemaining = TimeUnit.MILLISECONDS.toDays(
                        task.getDueDate().getTime() - new Date().getTime()
                );
            }

            holder.display_name.setText(task.getTitle());
            holder.display_days.setText(String.format(Locale.getDefault(), "%d", daysRemaining));


        } catch (Exception e) {

        }
        return vi;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_days;

    }
}
