package com.CNAM.GeoRouting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<Profile> implements Preferences{

    private SharedPreferences m_sharedPrefs;
    private boolean m_active;

    public ProfileAdapter(Context context, ArrayList<Profile> objects) {
        super(context, R.layout.profilelayout, objects);
        m_sharedPrefs = context.getSharedPreferences(Preferences.APPNAME, 0);
        refreshList();
        loadPreferences();
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

    public ArrayList<Profile> getProfiles () {
        ArrayList<Profile> arr = new ArrayList<Profile>();
        for(int i = 0; i < getCount(); i++) {
            arr.add(getItem(i));
        }
        return arr;
    }

    public void refreshList() {
        for(Profile p : getProfiles())
        {
            p.setActivated(false);
            remove(p);
        }
        populate();
        int id = NetworkManager.getInstance().getAppliedProfileID();
        for(Profile p : getProfiles())
        {
            if (p.getId() == id)
            {
                p.setActivated(true);
            }
        }

    }

    public Profile getActivated () {
        for(Profile p : getProfiles()) {
            if (p.isActivated()) {
                return p;
            }
        }
        return null;
    }

    public void setActivated (int _index) {
        setActivated(getItem(_index));
    }

    public void setActivated (Profile _profile) {
        if (_profile != null && NetworkManager.getInstance().setAppliedProfile(_profile.getId())) {
            for(Profile p : getProfiles()) {
                p.setActivated(false);
            }
            _profile.setActivated(true);
            savePreferences();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.profilelayout, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(getItem(position).getName());

        ImageView state = (ImageView) rowView.findViewById(R.id.state);
        if (getItem(position).isActivated())
            state.setVisibility(View.VISIBLE);
        else
            state.setVisibility(View.INVISIBLE);
        return rowView;
    }

    private void populate () {
        JSONArray profiles = NetworkManager.getInstance(m_sharedPrefs.getString(Preferences.CREDENTIAL, "")).getProfiles();
        if (profiles != null)
            for (int i = 0; i < profiles.length(); i++) {
                int profileID = -1;
                String profileName = "";
                try {
                    profileID = profiles.getJSONObject(i).getInt("id");
                    profileName = profiles.getJSONObject(i).getString("name");
                    addProfile(new Profile(profileID, profileName));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ProfileException e) {
                    e.printStackTrace();
                }

            }
    }

    @Override
    public void loadPreferences() {
        m_active = !m_sharedPrefs.getBoolean(Preferences.AUTO, true);
    }

    @Override
    public void savePreferences() {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.commit();
    }

    public void setActive(boolean _active) {
        m_active = _active;
    }

    public boolean isEnabled(int _position)
    {
        return m_active;
    }

}
