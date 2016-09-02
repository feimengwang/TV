package tv.true123.cn.tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by junbo on 25/8/2016.
 */
public class TVAdapter extends BaseAdapter {
    Context context;
    List list;


    public TVAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List up) {
        this.list.clear();
        this.list.addAll(up);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.list_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TV tv = (TV) list.get(i);
        viewHolder.textView.setText(tv.getName());
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}
