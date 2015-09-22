package com.njk.bean;

import java.io.Serializable;

/**
 * Created by a on 2015/9/17.
 */
public class DetailInfoBean implements Serializable {
    String title;
    String content;
    int iconId;
    InfoType type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public InfoType getType() {
        return type;
    }

    public void setType(InfoType type) {
        this.type = type;
    }

    public enum InfoType{
        INTRO,FEATURE,STAY,RECREATION,ROUTE,SPECIAL
    }
}
