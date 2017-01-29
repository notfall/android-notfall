package com.mayassin.android.notfall.android;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mayassin.android.notfall.R;

public class LoginActivity extends AppCompatActivity {
    private View  registerLayoutView;
    private EditText usernameEditText;
    private SessionManager sess;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sess = new SessionManager(getApplicationContext());

        registerLayoutView = findViewById(R.id.register_Button);
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);

        registerLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                if(username.isEmpty()) {
                    Snackbar.make(view, "You need to specify a username and password!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                sess.createSession(username);

            }
        });

    }

    @Override
    protected void onResume() {
        if(sess.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onResume();

    }
}
