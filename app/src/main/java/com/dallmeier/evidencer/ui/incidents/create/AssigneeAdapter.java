package com.dallmeier.evidencer.ui.incidents.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.model.AssigneeEntity;

import java.util.List;


public class AssigneeAdapter extends ArrayAdapter<AssigneeEntity> {
    private List<AssigneeEntity> assignees;

    public AssigneeAdapter(Context context, int textViewResourceId, List<AssigneeEntity> assignees) {
        super(context, textViewResourceId, assignees);
        this.assignees = assignees;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        final TextView label = (TextView) row.findViewById(R.id.tv_spinner);
        label.setText(assignees.get(position).getUsername());
        return row;
    }

    public List<AssigneeEntity> getAssignees() {
        return assignees;
    }

}