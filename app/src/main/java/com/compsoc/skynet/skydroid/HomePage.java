package com.compsoc.skynet.skydroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class HomePage extends AppCompatActivity {
    static String user=null;
    static String pass=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        System.out.println("Fetching Extras");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (String) extras.getString("user");
            pass = (String) extras.getString("pass");
        }

        if(user!=null&&pass!=null) {
            try {
                System.out.println("Starting SSH");
                String result = new sshconnection().execute().get();
                if(result.equals(null)){
                    Intent error = new Intent(this, SkynetLogin.class);
                    startActivity(error);
                }
                System.out.println("Finished SSH");
                final TextView t = (TextView) findViewById(R.id.textView7);
                t.setText(result);
            } catch (Exception exception) {

            }
        }
    }
    private class sshconnection extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            try {
                JSch jsch = new JSch();
                System.out.println("Username: "+user);
                System.out.println("Password: "+pass);

                Session session = jsch.getSession(user, "skynet.ie", 22);
                session.setPassword(pass);
                System.out.println("Session Setup");
                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                System.out.println("Connecting to Session");
                session.connect();
                System.out.println("Sending Command");
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand("ls");
                channel.connect();

                InputStream input = channel.getInputStream();

                Scanner s1 = new Scanner(input);
                String next = "";
                while(s1.hasNext()) {
                    String current = s1.nextLine();
                    next = next+"\n" + current;
                    System.out.println(next);
                    if(current.equals("Auth Fail")){
                        return null;
                    }
                }

                channel.disconnect();
                return next;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;


        }
    }
}
