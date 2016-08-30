package tv.true123.cn.tv;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by junbo on 25/8/2016.
 */
public class Util {
    static String TAG="Util";
    static SharedPreferences sharedPreferences;

    public static List getTV(Context context) {
        List<TV> list = new ArrayList<TV>();
        try {
            InputStream inputStream = context.getAssets().open("tv.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] s = line.split(",");
                if (s != null && s.length < 2) {
                    continue;
                }

                if (s[0] == null || "".equals(s[0].trim())) continue;
                TV tv = new TV(s[0].trim());
                int indexOf = list.indexOf(tv);
                if (indexOf >= 0) {
                    tv = list.get(indexOf);
                    if("".equals(getInvalidUrl(context,s[1]))){
                        tv.addUrl(s[1]);
                    }

                    //tv.setIndex(getIndex(context,s[0]));
                } else {
                    if("".equals(getInvalidUrl(context,s[1]))){
                        tv.addUrl(s[1]);
                    }
                   // tv.setIndex(getIndex(context,s[0]));
                    list.add(tv);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<TV> copy=new ArrayList<TV>(list);
        Collections.copy(copy,list);
        for(TV tv:copy){
            if(tv.getUrls()==null || tv.getUrls().size()==0){
                list.remove(tv);
            }
        }

        Collections.sort(list, new Comparator<TV>() {
            @Override
            public int compare(TV tv, TV t1) {
                if (tv == null || t1 == null) return 0;
                if (tv.getIndex() == t1.getIndex()) {
                    return tv.getName().compareTo(t1.getName());
                }
                return tv.getIndex() - t1.getIndex();
            }
        });

        return list;
    }

    public static String getInvalidUrl(Context context, String url) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("tv", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(MD5(url.trim()), "");

    }


    public static void updateInvalidUrl(Context context,String url){
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("tv", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(MD5(url),"invalid").commit();
    }
    public static void clear(Context context){
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("tv", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().clear().commit();
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
