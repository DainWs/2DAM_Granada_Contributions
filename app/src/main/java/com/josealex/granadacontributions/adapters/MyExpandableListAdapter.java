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
import com.josealex.granadacontributions.modules.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MyExpandableListAdapter<T> extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle = new ArrayList<>();
    private HashMap<String, List<T>> expandableListDetail = new HashMap<>();

    public MyExpandableListAdapter(Context context, List<String> expandableListTitle,
                                   HashMap<String, List<T>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    public MyExpandableListAdapter(Context context, String title,
                                   List<T> list) {
        this.context = context;
        expandableListTitle.add(title);
        expandableListDetail.put(title, list);
    }

    public void update(String title, List<T> newModels) {
        expandableListDetail.put(title, newModels);
        notifyDataSetChanged();
    }

    @Override
    public T getChild(int listPosition, int expandedListPosition) {
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
        initValues((T) getChild(listPosition, expandedListPosition), convertView);

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

        final ImageView expandableState = ((ImageView) convertView.findViewById(R.id.expandable_state_image));

        convertView.setOnClickListener(v -> {
            if(isExpanded) expandableState.setImageResource(R.mipmap.ic_expanded);
            else expandableState.setImageResource(R.mipmap.ic_collapsed);
        });

        listTitleTV.setTypeface(null, Typeface.BOLD);
        listTitleTV.setText(listTitle);

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
}
