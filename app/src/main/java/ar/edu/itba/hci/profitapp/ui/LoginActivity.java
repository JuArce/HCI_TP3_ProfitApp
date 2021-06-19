package ar.edu.itba.hci.profitapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.ActivityLoginBinding;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.api.model.Credentials;
import ar.edu.itba.hci.profitapp.api.model.Error;

public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "ar.edu.itba.hci.profitapp.message";

    public static final String TAG = "UI";
    private App app;
    private ActivityLoginBinding activityLoginBinding;
    private boolean didLoginFromLink = false;
    private int routineId = -1;
    private boolean validCredentials = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = ((App) getApplication());

        Intent linkIntent = getIntent();

        if (linkIntent != null && linkIntent.getData() != null) {
            didLoginFromLink = true;
            Uri linkData = linkIntent.getData();
            routineId = Integer.parseInt(linkData.getLastPathSegment()); //se inicia por un link
        }

        if (app.getPreferences().getAuthToken() != null) {
            if (didLoginFromLink) {
                startDetailActivity();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(activityLoginBinding.getRoot());

        activityLoginBinding.usernameInput.setOnClickListener(v -> {
            activityLoginBinding.usernameContainer.setError(null);
            activityLoginBinding.passwordContainer.setError(null);
            activityLoginBinding.errorText.setText("");
            validCredentials = true;
        });

        activityLoginBinding.passwordInput.setOnClickListener(v -> {
            activityLoginBinding.usernameContainer.setError(null);
            activityLoginBinding.passwordContainer.setError(null);
            activityLoginBinding.errorText.setText("");
            validCredentials = true;
        });

        activityLoginBinding.loginButton.setOnClickListener(v -> {
            if (activityLoginBinding.usernameContainer.getEditText().getText().toString().length() < 3) {
                activityLoginBinding.usernameContainer.setError(getResources().getString(R.string.invalid_user));
                validCredentials = false;
            }
            if (activityLoginBinding.passwordContainer.getEditText().getText().toString().length() < 8) {
                activityLoginBinding.passwordContainer.setError(getResources().getString(R.string.invalid_password));
                validCredentials = false;
            }

            if (validCredentials) {
                Credentials credentials = new Credentials(activityLoginBinding.usernameContainer.getEditText().getText().toString(), activityLoginBinding.passwordContainer.getEditText().getText().toString());
                app.getUserRepository().login(credentials).observe(this, r -> {
                    if (r.getStatus() == Status.SUCCESS) {
                        app.getPreferences().setAuthToken(r.getData().getToken());

                        if (didLoginFromLink) {
                            startDetailActivity();
                        } else {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        defaultResourceHandler(r);
                    }
                });
            }

        });
    }

    private void startDetailActivity() {
        Intent detailIntent = new Intent(this, RoutineActivity.class);
        detailIntent.putExtra(EXTRA_MESSAGE, routineId);
        startActivity(detailIntent);
    }

    private void defaultResourceHandler(Resource<?> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                Log.d(TAG, "Loading");
                break;
            case ERROR:
                Error error = resource.getError();
                String message = "Error";
                Log.d(TAG, message);
                activityLoginBinding.errorText.setText(resource.getError().getDescription());
                break;
        }
    }
}