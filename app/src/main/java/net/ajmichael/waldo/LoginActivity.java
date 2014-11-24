package net.ajmichael.waldo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by adam on 11/23/14.
 */
public class LoginActivity extends Activity {

    Firebase ref;

    private EditText emailET = null;
    private EditText passwordET = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://wheremyboysat.firebaseio.com");
        emailET = (EditText) findViewById(R.id.editText2);
        passwordET = (EditText) findViewById(R.id.editText1);
    }

    public void register(View view) {
        final LoginActivity that = this;
        System.out.println("IN THE NEW ONE");
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();
        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_LONG).show();

        ref.child("taken_emails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false;
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    String taken_email = (String) childSnap.getValue();
                    if (taken_email.equals(email)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    // user was not found, register a new one
                    ref.createUser(email, password, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            ref.child("taken_emails").push().setValue(email);
                            System.out.println("User successfully created");
                            DialogFragment dialog = new ErrorDialog();
                            Bundle args = new Bundle();
                            args.putString("title", "User successfully created!");
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "tag");
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            System.out.println(30);
                            DialogFragment dialog = new ErrorDialog();
                            Bundle args = new Bundle();
                            args.putString("title", "Unable to create new user");
                            args.putString("message", firebaseError.toString());
                            dialog.setArguments(args);
                            dialog.show(getFragmentManager(), "tag");
                        }
                    });
                }
                ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        int num = (int) (Math.random()*95);
                        String gender = Math.random() > .5 ? "men" : "women";
                        String img_url = "http://api.randomuser.me/portraits/"+gender+"/"+String.valueOf(num)+".jpg";
                        ref.child("users").child(authData.getUid()).child("email").setValue(email);
                        ref.child("users").child(authData.getUid()).child("img_url").setValue(img_url);
                        System.out.println("Whaddup");
                        Intent intent = new Intent(that, MainActivity.class);

                        startActivity(intent);
                        // onNavigationDrawerItemSelected(1);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        System.out.println(31);
                        DialogFragment dialog = new ErrorDialog();
                        Bundle args = new Bundle();
                        args.putString("title", "Invalid login credentials");
                        args.putString("message", firebaseError.toString());
                        dialog.setArguments(args);
                        dialog.show(getFragmentManager(), "tag");
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println(32);
                DialogFragment dialog = new ErrorDialog();
                Bundle args = new Bundle();
                args.putString("title", "Database Problem");
                args.putString("message", firebaseError.toString());
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "tag");
            }
        });
    }

}
