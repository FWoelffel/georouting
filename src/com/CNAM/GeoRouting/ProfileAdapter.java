package com.CNAM.GeoRouting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<Profile> {

    public ProfileAdapter(Context context, ArrayList<Profile> objects) {
        super(context, R.layout.rowlayout, objects);
    }

    public boolean addProfile (Profile _profile) {
        if (_profile == null)
            return false;
        for(int i = 0; i < getCount(); i++)
            if(getItem(i) == null || getItem(i).equals(_profile))
                return false;
        add(_profile);
        return true;
    }

    public Profile getActivated () {
        for(int i = 0; i < getCount(); i++)
            if(getItem(i).isActivated())
                return getItem(i);
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(getItem(position).getName());

        TextView description = (TextView) rowView.findViewById(R.id.description);
        description.setText(getItem(position).getDescription());

        ImageView state = (ImageView) rowView.findViewById(R.id.state);
        if (getItem(position).isActivated())
            state.setVisibility(View.VISIBLE);
        else
            state.setVisibility(View.INVISIBLE);
        return rowView;
    }

}
