package com.example.software_patterns;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPassword;
    private ProgressDialog loadingBar;
    DatabaseReference databaseInfo;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputEmail = (EditText) findViewById(R.id.register_email);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }


    private void registerUser() {
        final String email = InputEmail.getText().toString().trim();
        final String firstName = InputName.getText().toString().trim();
        String password = InputPassword.getText().toString().trim();
        if (email.isEmpty()) {
            InputEmail.setError("Email is required");
            InputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            InputEmail.setError("Please enter a valid email");
            InputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            InputPassword.setError("Password is required");
            InputPassword.requestFocus();
            return;
        }

        if (password.length() < 6 || password.length() > 15) {
            InputPassword.setError("Password should be of 6-15 characters");
            InputPassword.requestFocus();
            return;
        }

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            //editTextPassword.setError("Password should contain atleast one upper case alphabet");
            InputPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            InputPassword.requestFocus();
            return;
        }

        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            InputPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            InputPassword.requestFocus();
            return;
        }

        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            InputPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            InputPassword.requestFocus();
            return;
        }

        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars)) {
            InputPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            InputPassword.requestFocus();
            return;
        }

        //  progressBar.setVisibility(View.VISIBLE);
        insertData(firstName, email, password);


    }


    private void insertData(final String name, final String email, final String pass) {
        // final DatabaseReference RootRef;
        //  fAuth = FirebaseAuth.getInstance();
        //  FirebaseUser rUser = fAuth.getCurrentUser();
        //  assert rUser != null;
        //  final String userId = rUser.getUid();
//        databaseInfo = FirebaseDatabase.getInstance().getReference("users");
        //    RootRef = FirebaseDatabase.getInstance("https://electronicsapp-b7c4e-default-rtdb.firebaseio.com/users").getReference("users");


        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser rUser = mAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseInfo = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("first", name);
                    //  hashMap.put("last", lastName);
                    hashMap.put("email", email);

                    databaseInfo.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {

                                Toast.makeText(RegisterActivity.this, "error!!",
                                        Toast.LENGTH_SHORT).show();
                                // progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}







