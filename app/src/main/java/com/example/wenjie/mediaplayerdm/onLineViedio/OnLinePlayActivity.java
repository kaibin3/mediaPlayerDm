package com.example.wenjie.mediaplayerdm.onLineViedio;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wenjie.mediaplayerdm.R;

public class OnLinePlayActivity extends AppCompatActivity {

    OnLinePlayFragment onLinePlayFragment = new OnLinePlayFragment();

    VideoFragment videoFragment = new VideoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_line_play);

       // addFragment(onLinePlayFragment);
        addFragment(new VideoFragment());
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.play_fragment, fragment);
        transaction.commit();
    }


}
