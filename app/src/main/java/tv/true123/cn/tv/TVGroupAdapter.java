package tv.true123.cn.tv;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by junbo on 25/8/2016.
 */
public class TVGroupAdapter extends BaseAdapter {
    Context context;
    String[] list;
    int mPosition;

    public TVGroupAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(String[] up) {
        this.list = up;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (mPosition == i) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setBackgroundColor(context.getColor(R.color.group_item_pressed_bg));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.group_item_pressed_bg));
            }
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.list_item_text_pressed_bg));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setBackgroundColor(context.getColor(R.color.group_item_bg));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.group_item_bg));
            }
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.tab_text_color));
        }
        String tv =  list[i];
        viewHolder.textView.setText(tv);
        return view;
    }

    public void setSelection(int position) {
        mPosition = position;
    }

    class ViewHolder {
        TextView textView;
    }
}
