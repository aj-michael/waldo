package net.ajmichael.waldo;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 11/24/14.
 */
public class UserListViewFragment extends ListFragment {

    private List<UserItem> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("I want to be here");
        items = new ArrayList<UserItem>();
        final Resources resources = getResources();
        Firebase ref = new Firebase("https://wheremyboysat.firebaseio.com/");
        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                System.out.println("Made it all the way here");
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    String email = (String) childSnap.child("email").getValue();
                    String img_url = (String) childSnap.child("img_url").getValue();
                    System.out.println(email);
                    System.out.println(img_url);
                    //items.add(new UserItem(resources.getDrawable(R.drawable.ic_launcher),email));
                    try {
                        items.add(new UserItem(drawableFromUrl(img_url),email));
                    } catch (IOException e) {
                        items.add(new UserItem(resources.getDrawable(R.drawable.ic_launcher),email));
                        e.printStackTrace();
                    }
                }
                setListAdapter(new UserListViewAdapter(getActivity(), items));
                getView().setBackground(resources.getDrawable(R.drawable.background));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.connect();
        InputStream input = conn.getInputStream();
        Bitmap bmp = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(bmp);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        UserItem item = items.get(position);
        Toast.makeText(getActivity(), item.email, Toast.LENGTH_SHORT).show();
    }

    public static Fragment newInstance() {
        System.out.println("I am here");
        return new UserListViewFragment();
    }
}
