package net.ajmichael.waldo;

import android.graphics.drawable.Drawable;

/**
 * Created by adam on 11/24/14.
 */
public class UserItem {

    public final Drawable icon;
    public final String email;

    public UserItem(Drawable icon, String email) {
        this.icon = icon;
        this.email = email;
    }

}
