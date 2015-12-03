package com.njk.bean;

import java.io.Serializable;

/**
 * Created by a on 2015/11/4.
 */
public class Emums implements Serializable{
    public enum OrderType{order_type1("1","距离最近"),order_type2("2","人气最高"),order_type3("3","点评最多"),order_type4("4","人均最低"),order_type5("5","人均最高");

        public String  id;
        public String name;
        OrderType(String id,String name) {
            this.id = id;
            this.name = name;
        }
    }

    public enum NjiaType{
        njia_type0("0","全部"),njia_type1("1","农家园"), njia_type2("2","采摘园"),njia_type3("3","垂钓园"),njia_type4("4","祈福寺院");

        public String  id;
        public String name;
        NjiaType(String id,String name) {
            this.id = id;
            this.name = name;
        }
    }
}
