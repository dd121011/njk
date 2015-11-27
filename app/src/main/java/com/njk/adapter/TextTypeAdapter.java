package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njk.R;
import com.njk.view.ViewHolder;

import java.util.List;

/**
 * Created by a on 2015/11/27.
 */
public class TextTypeAdapter extends BaseAdapter {
    List<String> texts = null;
    Activity context = null;
    public TextTypeAdapter(Activity context, List<String> textArra) {
        texts = textArra;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return texts.size();
    }

    @Override
    public Object getItem(int position) {

        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.near_item_text_layout, null);
        }
        TextView t = ViewHolder.get(convertView, R.id.item_text);
        t.setText(texts.get(position));


        return convertView;
    }

}