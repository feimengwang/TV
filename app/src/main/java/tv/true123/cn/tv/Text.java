package tv.true123.cn.tv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by junbo on 28/8/2016.
 */
public class Text {
    private static String FILE = "c:/c2.txt";
    private static Map<String, T> map = new TreeMap<>(new Comparator<String>() {

        @Override
        public int compare(String s, String t1) {
            return s.compareTo(t1);
        }
    });

    public static void main(String[] args) {
//        Text text = new Text();
//         text.read();
//         text.calculate(map);
        List<String> l = new ArrayList();
        l.add("d222222d");
        l.add("ddd");
        l.add("jjjjjjj");
       List<String> l2 = new ArrayList<>(l);
        Collections.copy(l2,l);
        System.out.println(l2);
        for(String s:l2) {
            l.remove(s);
        }
        System.out.println(l);
    }


    private void calculate(Map m) {
        System.out.println(m);
    }

    private void read() {
        File file = new File(FILE);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] ss = line.split(",");
                if (ss.length < 2) continue;

                T l = map.get(ss[0]);
                if (l == null) {
                    l = new T();
                    l.setName(ss[0]);
                    l.addUrl(ss[1]);

                    map.put(ss[0], l);
                } else {
                    map.get(ss[0]).addUrl(ss[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class T {
        private String name;
        private List url;

        public List getUrl() {
            return url;
        }

        public void addUrl(String surl) {
            if (this.url == null) this.url = new ArrayList();
            this.url.add(surl);
        }

        public void setUrl(List url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return "T{" +
                    "name='" + name + '\'' +
                    ", url=" + url +
                    '}';
        }
    }
}
