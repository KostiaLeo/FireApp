package com.example.fireapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.item_of_user, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_of_user, null);
        }
        ((TextView) convertView.findViewById(R.id.nick))
                .setText(user.getNick());
        ((TextView) convertView.findViewById(R.id.mail))
                .setText(user.getMail());
        ((TextView) convertView.findViewById(R.id.pwd))
                .setText(user.getPwd());
        return convertView;
    }
}
