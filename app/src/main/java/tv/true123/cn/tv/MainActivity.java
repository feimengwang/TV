package tv.true123.cn.tv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";
    @BindView(R.id.list1)
    ListView listView1;

    @BindView(R.id.list2)
    ListView listView2;
    TVAdapter tvAdapter;
    TVGroupAdapter groupAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Map map;
    String[] group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setListener();
        readData();
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: ");

                readData();

            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        swipeRefreshLayout.setRefreshing(true);
        groupAdapter = new TVGroupAdapter(this, new String[]{});
        listView1.setAdapter(groupAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List list = (List) map.get(group[i]);
                tvAdapter.updateList(list);
                tvAdapter.notifyDataSetChanged();
                groupAdapter.setSelection(i);
                groupAdapter.notifyDataSetChanged();

            }
        });
        final List list = new ArrayList();//Util.getTV(this);
        tvAdapter = new TVAdapter(this, list);
        listView2.setAdapter(tvAdapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TV tv = (TV) list.get(i);
                Intent intent = new Intent(MainActivity.this, TVPlayView.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("url", (ArrayList<String>) tv.getUrls());
                bundle.getString("name", tv.getName());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View firstView = absListView.getChildAt(firstVisibleItem);

                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }

            }
        });
    }

    public void readData() {
        Observable.create(new Observable.OnSubscribe<Map>() {

            @Override
            public void call(Subscriber<? super Map> subscriber) {
                List list = Util.getTV(MainActivity.this);
                Map map = Util.getMapTV(list);
                subscriber.onNext(map);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<Map>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(Map list) {
                        map = list;
                        update();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void update() {
        Log.i(TAG, "update: " + map);
        group = new String[map.keySet().size()];
        Iterator<String> it = map.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            String key = it.next();
            group[i] = key;
            i++;
        }
        Log.i(TAG, "update: " + group);
        groupAdapter.updateList(group);
        groupAdapter.notifyDataSetChanged();
        groupAdapter.setSelection(0);
        tvAdapter.updateList((List) map.get(group[0]));
        tvAdapter.notifyDataSetChanged();

    }
}
