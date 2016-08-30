package tv.true123.cn.tv;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by junbo on 25/8/2016.
 */
public class TV implements Comparator, Parcelable {
    private String name;
    private int index;
    private List<String> urls;

    protected TV(Parcel in) {
        name = in.readString();
        index = in.readInt();
        urls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(urls);
        dest.writeInt(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TV> CREATOR = new Creator<TV>() {
        @Override
        public TV createFromParcel(Parcel in) {
            return new TV(in);
        }

        @Override
        public TV[] newArray(int size) {
            return new TV[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public TV(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addUrl(String surl){
        if(this.urls==null) this.urls=new ArrayList();
        this.urls.add(surl);
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0 + index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (((TV) obj).getName() == null) return false;
        return ((TV) obj).getName().equals(getName());
    }

    @Override
    public int compare(Object o, Object t1) {
        if (o == null || t1 == null) return 0;

        return ((TV) o).getIndex() - ((TV) t1).getIndex();
    }
}
