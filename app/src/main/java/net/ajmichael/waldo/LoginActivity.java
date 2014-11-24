package net.ajmichael.waldo;

import android.app.Activity;
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
                            System.out.println("User successfully created");
                            ref.child("taken_emails").push().setValue(email);
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            System.out.println("This error");
                            System.out.println(firebaseError.toString());
                        }
                    });
                }
                ref.authWithPassword(email,password,new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        ref.child("users").child(authData.getUid()).child("email").setValue(email);
                        System.out.println("Whaddup");
                        Intent intent = new Intent(that, MainActivity.class);

                        startActivity(intent);
                        // onNavigationDrawerItemSelected(1);
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        System.out.println("All the way to this error");
                        System.out.println(firebaseError.toString());
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("No this error");
                System.out.println(firebaseError.toString());
            }
        });
    }

}
