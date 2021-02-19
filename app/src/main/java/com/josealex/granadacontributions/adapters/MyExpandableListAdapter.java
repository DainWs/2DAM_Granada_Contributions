package com.josealex.granadacontributions.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.josealex.granadacontributions.R;

import java.util.HashMap;
import java.util.List;

public abstract class MyExpandableListAdapter<T> extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<T>> expandableListDetail;

    public MyExpandableListAdapter(Context context, List<String> expandableListTitle,
                                   HashMap<String, List<T>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    public void update(String title, List<T> newModels) {
        expandableListDetail.put(title, newModels);
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail
                .get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_user, null);
        }

        T model = (T) getChild(listPosition, expandedListPosition);

        TextView mNameView = (TextView) convertView.findViewById(R.id.item_user_name);
        TextView mMailView = (TextView) convertView.findViewById(R.id.item_user_correo);
        ImageView mImageView = (ImageView)convertView.findViewById(R.id.item_user_image);

        initValues(model, convertView);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.simple_expandable_list_group, null);
        }

        String listTitle = (String) getGroup(listPosition);

        TextView listTitleTV = (TextView) convertView.findViewById(R.id.list_title);

        listTitleTV.setTypeface(null, Typeface.BOLD);
        listTitleTV.setText(listTitle);

        View finalConvertView = convertView;
        convertView.findViewById(R.id.expandable_list_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleClick(finalConvertView);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public abstract void initValues(T model, View convertView);

    public abstract void onTitleClick(View convertView);
}
