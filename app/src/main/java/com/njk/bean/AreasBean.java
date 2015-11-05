package com.njk.bean;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by a on 2015/10/30.
 */
public class AreasBean {
    public TreeMap<String,Items> province;


    public class Items{
        public List<Item> items;

        @Override
        public String toString() {
            return "Items{" +
                    "items=" + items +
                    '}';
        }
    }

    public class Item{
        public String id;
        public String name;
        public List<City> citys;

        @Override
        public String toString() {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", citys=" + citys +
                    '}';
        }
    }

    public class City{
        public String id;
        public String name;
        public List<ScenciImp> scenic;

        @Override
        public String toString() {
            return "City{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", scenic=" + scenic +
                    '}';
        }
    }

    public class ScenciImp{
        public String id;
        public String title;
        public String province;
        public String city;

        @Override
        public String toString() {
            return "ScenciImp{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AreasBean{" +
                "province=" + province +
                '}';
    }
}
