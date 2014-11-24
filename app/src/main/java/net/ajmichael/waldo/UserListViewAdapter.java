package net.ajmichael.waldo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adam on 11/24/14.
 */
public class UserListViewAdapter extends ArrayAdapter<UserItem> {

    public UserListViewAdapter(Context context, List<UserItem> users) {
        super(context, R.layout.listview_user, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserItem user = getItem(position);
        viewHolder.ivIcon.setImageDrawable(user.icon);
        viewHolder.tvTitle.setText(user.email);
        viewHolder.tvDescription.setText(user.email);
        return convertView;
    }

    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }

}
