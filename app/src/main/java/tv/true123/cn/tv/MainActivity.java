package tv.true123.cn.tv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TVAdapter tvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Util.clear(this);
        listView = (ListView) findViewById(R.id.list);
        final List list = new ArrayList();//Util.getTV(this);
        tvAdapter = new TVAdapter(this, list);
        listView.setAdapter(tvAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        readData();
    }

    public void readData() {
        Observable.create(new Observable.OnSubscribe<List>() {

            @Override
            public void call(Subscriber<? super List> subscriber) {
                List list = Util.getTV(MainActivity.this);
                subscriber.onNext(list);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List list) {
                        tvAdapter.updateList(list);
                        tvAdapter.notifyDataSetChanged();
                    }
                });
    }
}
