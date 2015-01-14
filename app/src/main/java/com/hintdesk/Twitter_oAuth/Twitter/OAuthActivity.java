package com.hintdesk.Twitter_oAuth.Twitter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hintdesk.Twitter_oAuth.Constants.ConstantValues;

/**
 * Created by Christopher Stokes on 2014-07-18.
 */
public class OAuthActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String authenticationUrl = getIntent().getStringExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OAuthWebViewFragment oAuthWebViewFragment = new OAuthWebViewFragment(authenticationUrl);
        fragmentTransaction.add(android.R.id.content,oAuthWebViewFragment);
        fragmentTransaction.commit();
    }
}