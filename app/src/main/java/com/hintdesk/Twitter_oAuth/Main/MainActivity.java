package com.hintdesk.Twitter_oAuth.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.hintdesk.Twitter_oAuth.Constants.ConstantValues;
import com.hintdesk.Twitter_oAuth.R;
import com.hintdesk.Twitter_oAuth.Twitter.OAuthActivity;
import com.hintdesk.Twitter_oAuth.Twitter.TwitterActivity;
import com.hintdesk.Twitter_oAuth.Twitter.TwitterUtil;
import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;
import twitter4j.auth.RequestToken;

/**
 * Created by Christopher Stokes on 2014-07-18.
 */

public class MainActivity extends Activity {

    private Button buttonLogin;
    private boolean isUseStoredTokenKey = false;
    private boolean isUseWebViewForAuthentication = false;
    private boolean loggedIn;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (!OSUtil.IsNetworkAvailable(getApplicationContext())) {
            AlertMessageBox.Show(MainActivity.this, "Internet connection", "A valid internet connection can't be established", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }

        if (StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_KEY) || StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_SECRET)) {
            AlertMessageBox.Show(MainActivity.this, "Twitter oAuth infos", "Please set your twitter consumer key and consumer secret", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }

        initializeComponent();
        initializeLogIn();
        if (loggedIn)
            logIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLogIn();
    }

    private void initializeComponent() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(buttonLoginOnClickListener);
    }

    private void initializeLogIn(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(sharedPreferences.getBoolean("logged in", false));
        loggedIn = sharedPreferences.getBoolean("logged in", false);
        if(loggedIn){
            logIn();
        }
    }

    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            logIn();

        }
    };

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new TwitterAuthenticateTask().execute();
        }
        else
        {
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                if (!isUseWebViewForAuthentication)
                {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
                    intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL,requestToken.getAuthenticationURL());
                    startActivity(intent);
                }
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }
}
