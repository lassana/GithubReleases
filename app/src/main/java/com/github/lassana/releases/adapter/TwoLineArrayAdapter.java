package com.github.lassana.releases.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * @author lassana
 * @since 1/14/14
 */
public abstract class TwoLineArrayAdapter<T> extends ArrayAdapter<T> {

    private final LayoutInflater mLayoutInflater;
    private final int mListItemLayoutResId;

    public TwoLineArrayAdapter(Context context, T[] ts) {
        this(context, android.R.layout.two_line_list_item, ts);
    }

    public TwoLineArrayAdapter(Context context, int listItemLayoutResourceId, T[] ts) {
        this(context, listItemLayoutResourceId, Arrays.asList(ts));
    }

    public TwoLineArrayAdapter(Context context, List<T> ts) {
        this(context, android.R.layout.two_line_list_item, ts);
    }

    public TwoLineArrayAdapter(Context context, int listItemLayoutResourceId, List<T> ts) {
        super(context, listItemLayoutResourceId, ts);
        mLayoutInflater = LayoutInflater.from(context);
        mListItemLayoutResId = listItemLayoutResourceId;
    }


    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = mLayoutInflater.inflate(mListItemLayoutResId, parent, false);
        }
        TextView lineOneView = (TextView) listItemView.findViewById(android.R.id.text1);
        TextView lineTwoView = (TextView) listItemView.findViewById(android.R.id.text2);
        T t = getItem(position);
        lineOneView.setText(lineOneText(t));
        lineTwoView.setText(lineTwoText(t));
        return listItemView;
    }

    public abstract String lineOneText(T t);

    public abstract String lineTwoText(T t);
}