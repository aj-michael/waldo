package net.ajmichael.waldo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by adam on 11/22/14.
 */
public class UserMapFragment extends MapFragment {

    private String id;

    public static MapFragment newInstance(int position) {
        GoogleMapOptions options = new GoogleMapOptions();
        return MapFragment.newInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SecureRandom random = new SecureRandom();
        this.id = (new BigInteger(130,random).toString(32));
        return super.onCreateView(inflater,container,savedInstanceState);
    }

}