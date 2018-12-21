package com.gmail.lidteam.checkers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.gmail.lidteam.checkers.R;

// Add in your root build.gradle at the end of repositories:
// maven { url 'https://jitpack.io' }
public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(BaseSlide.newInstance(R.layout.slide1));
        addSlide(BaseSlide.newInstance(R.layout.slide2));
        addSlide(BaseSlide.newInstance(R.layout.slide3));
        showStatusBar(false);
        showSkipButton(true);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }



    
}


