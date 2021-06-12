package ar.edu.itba.hci.profitapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ar.edu.itba.hci.profitapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(activityLoginBinding.getRoot());
//        VideoView videoview = (VideoView) findViewById(R.id.videoView);
//        Uri uri = Uri.parse("https://assets.mixkit.co/videos/preview/mixkit-man-training-by-the-sea-14065-large.mp4");
//        videoview.setVideoURI(uri);
//        videoview.start();

        activityLoginBinding.loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        
    }


}