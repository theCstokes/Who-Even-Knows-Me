package com.hintdesk.Twitter_oAuth.Twitter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hintdesk.Twitter_oAuth.Encoding.CipherUtils;
import com.hintdesk.Twitter_oAuth.Constants.ConstantValues;
import com.hintdesk.Twitter_oAuth.Encoding.Data;
import com.hintdesk.Twitter_oAuth.Main.MainActivity;
import com.hintdesk.Twitter_oAuth.Service.MyService;
import com.hintdesk.Twitter_oAuth.R;
import com.hintdesk.core.util.StringUtil;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Christopher Stokes on 2014-07-18.
 */
public class TwitterActivity extends Activity {

    Button buttonUpdateStatus;
    EditText editTextStatus;
    TextView textViewStatus;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);
        initializeComponent();
        initControl();
        initList();
        getActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initList() {
        final ListView listview = (ListView) findViewById(R.id.listview);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        //listview.setBackgroundColor(Color.rgb(158,158,158));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                                view.setClickable(true);
                            }
                        });
            }

        });
    }


    private void initControl() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged in", true);
        editor.commit();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
        } else
            new TwitterGetAccessTokenTask().execute("");
    }

    private void initializeComponent() {
        buttonUpdateStatus = (Button) findViewById(R.id.buttonUpdateStatus);
        editTextStatus = (EditText) findViewById(R.id.editTextStatus);

        buttonUpdateStatus.setOnClickListener(buttonUpdateStatusOnClickListener);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.logout:
                onLogout();
                Toast.makeText(getBaseContext(), "Loging out", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    private void onLogout(){
        stopService(new Intent(this, MyService.class));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
        editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
        editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false);
        editor.putBoolean("logged in", false);
        editor.commit();
        TwitterUtil.getInstance().reset();
        Intent intent = new Intent(TwitterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener buttonUpdateStatusOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String status = editTextStatus.getText().toString();
            System.out.println("updates");
            if (!StringUtil.isNullOrWhitespace(status)) {
                new TwitterUpdateStatusTask().execute(status);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a status", Toast.LENGTH_SHORT).show();
            }
            showNotification();
            editTextStatus.setText("");
        }
    };

    private ListView.OnItemClickListener listOnItemClickListener = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
    private void showNotification(){
        System.out.println("showing notifactation from TwitterActivity");
        final String subject = "New Ecrypted Confession";
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(TwitterActivity.this, TwitterActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(TwitterActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle(subject)
                .setContentText("Here's an awesome update for you!")
                .setSmallIcon(R.drawable.send)
                .setContentIntent(pIntent)
                .setSound(soundUri)

                .addAction(R.drawable.tweet, "View", pIntent)
                .addAction(0, "Remind", pIntent)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    public void cancelNotification(int notificationId){

        if (Context.NOTIFICATION_SERVICE!=null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

    private View.OnClickListener buttonGetStatusOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            new TwitterGetFreinds().execute();
        }
    };

    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {
        Drawable d, profile;
        @Override
        protected void onPostExecute(String userName) {
            getActionBar().setTitle(userName);
            getActionBar().setIcon(profile);
        }

        @Override
        protected String doInBackground(String... params) {

            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            if (!StringUtil.isNullOrWhitespace(params[0])) {
                try {

                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
                    editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);

                    User user = twitter.showUser(twitter.getId());
                    InputStream in = (InputStream) new URL(user.getBiggerProfileImageURL()).getContent();
                    d = Drawable.createFromStream(in, "Profile Picture");
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    profile = new BitmapDrawable(getResources(), resized);

                    InputStream is = (InputStream) new URL(user.getBiggerProfileImageURL()).getContent();
                    Bitmap realImage = BitmapFactory.decodeStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] pictureBytes = baos.toByteArray();
                    System.out.println("bytes : " + Arrays.toString(pictureBytes));
                    String encodedImage = Base64.encodeToString(pictureBytes, Base64.DEFAULT);
                    editor.putString("image_data", encodedImage);
                    editor.commit();
                    return twitter.showUser(accessToken.getUserId()).getName();
                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                String previouslyEncodedImage = sharedPreferences.getString("image_data", "");
                System.out.println("in decode");
                byte[] pictureBytes = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                System.out.println("bytes : " + Arrays.toString(pictureBytes));
                Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                profile = new BitmapDrawable(getResources(), resized);

                try {
                    TwitterUtil.getInstance().setTwitterFactory(accessToken);
                    return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), "Tweet successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Tweet failed, could be to long", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String out = "TheWhoKnowsMeApp : ";
            out += CipherUtils.encode(params[0]);
            System.out.println(out);
            if(out.length() > 140){
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            } else {
                try {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                    String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");

                    if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
                        AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                        twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(out);
                        return true;
                    }

                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return false;
            }
        }
    }

    class TwitterGetFreinds extends AsyncTask<String, String, Boolean> {
        String lookatthis;
        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(getApplicationContext(),
                    lookatthis, Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected Boolean doInBackground(String... params){
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            User u1 = null ;
            long userID = 0, cursor = -1;
            try {
                userID = twitter.getId();
            } catch (Exception e) {
            }
            IDs ids = null;
            List<twitter4j.Status> statuses;

            try {
                ids = twitter.getFriendsIDs(userID, cursor);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            if(ids != null){
                ArrayList<Long> users = new ArrayList<Long>();
                for(Long id : ids.getIDs()){
                    System.out.println(id);
                    users.add(id);
                }
                int u = (int) Math.round((Math.random() * (users.size() - 1)));;
                System.out.println("Sizs : " + users.size());
                System.out.println("U : " + u);
                try {
                    statuses = twitter.getUserTimeline(users.get(u));
                    if(statuses != null){
                        for(twitter4j.Status s : statuses){
                            System.out.println(s.getText());
                            //if(Data.checkIfNewStatus(s)){
                                Data.addStatus(s);
                                if(Data.getLastStatusAdded().getText().contains("TheWhoKnowsMeApp : ")){
                                    String tweet = Data.getLastStatusAdded().getText().substring(19);
                                    System.out.println("Pulled tweet data : " + tweet);
                                    //System.out.println(CipherUtils.decode(tweet));
                                    lookatthis = CipherUtils.decode(tweet);
                                }

                                //break;
                            //}
                        }

                    }

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return  true;
            }
        return false;
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}