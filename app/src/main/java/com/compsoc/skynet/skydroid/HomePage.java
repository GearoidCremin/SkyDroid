package com.compsoc.skynet.skydroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class HomePage extends AppCompatActivity {
    static String user = null;
    static String pass = null;
    static String next = "";
    static boolean list = false;
    static boolean run = true;
    static ChannelShell channel;
    static InputStream input;
    static String output;
    static Scanner s1;
    static JSch jsch;
    static Session session;
    static ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        System.out.println("Fetching Extras");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (String) extras.getString("user");
            pass = (String) extras.getString("pass");
        }

        if (user != null && pass != null) {
            try {
                mHandler = new Handler();
                System.out.println("Starting SSH");
                startRepeatingTask();
                new sshconnection().execute();
                System.out.println("Finished SSH");
            } catch (Exception exception) {

            }
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
   //         System.out.println("task is running");
            //         updateStatus(); //this function can change value of mInterval.
          if(!run) {
              try {
                  next = baos.toString("UTF-8");
              } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
              }
          }
            final TextView t = (TextView) findViewById(R.id.textView7);
            t.setText(next);
        //    System.out.println(next);
    //        System.out.println("task has finished an iteration");
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


    private static OutputStream convertStringtoStream(String string) {
        byte[] stringByte = string.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(string.length());
        try {
            bos.write(stringByte);
        } catch (Exception e) {
            System.out.println(e);
        }
        return bos;
    }

    public void list(View view) {
//        System.out.println("Listing contents");
        EditText commandEdit = (EditText) findViewById(R.id.editText3);
        output = commandEdit.getText().toString();
        output = output +"\n";
        commandEdit.setText("");
        new sshsend().execute();
    }
    private class sshsend extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            try {
                session = jsch.getSession(user, "skynet.ie", 22);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            session.setPassword(pass);
            System.out.println("Session Setup");
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            System.out.println("Connecting to Session");
            try {
                session.connect(30000);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            System.out.println("Sending Command");
            try {
                channel = (ChannelShell) session.openChannel("shell");
                ((ChannelShell)channel).setPty(false);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            System.out.println("Channel Open");
            OutputStream stream = new OutputStream() {
                @Override
                public void write(int oneByte) throws IOException {

                }
            };
            //             System.out.println("Sending output stream");
            //              channel.setOutputStream(convertStringtoStream(output));
            System.out.println("Connecting");
            ByteArrayInputStream bais= new ByteArrayInputStream(output.getBytes());
            channel.setInputStream(bais);
            channel.setOutputStream(baos, true);
            try {
                channel.connect(30000);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class sshconnection extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

                try {
                    jsch = new JSch();
                    System.out.println("Username: " + user);
                    System.out.println("Password: " + pass);

                    session = jsch.getSession(user, "skynet.ie", 22);
                    session.setPassword(pass);
                    System.out.println("Session Setup");
                    // Avoid asking for key confirmation
                    Properties prop = new Properties();
                    prop.put("StrictHostKeyChecking", "no");
                    session.setConfig(prop);
                    System.out.println("Connecting to Session");
                    session.connect(30000);
                    System.out.println("Sending Command");
                    channel = (ChannelShell) session.openChannel("shell");
                    ((ChannelShell)channel).setPty(false);
                    System.out.println("Channel Open");
                    OutputStream stream = new OutputStream() {
                        @Override
                        public void write(int oneByte) throws IOException {

                        }
                    };
                      output = "finger gearoid\nls\n";
                    //             System.out.println("Sending output stream");
                    //              channel.setOutputStream(convertStringtoStream(output));
                    System.out.println("Connecting");
                    ByteArrayInputStream bais= new ByteArrayInputStream(output.getBytes());
                    channel.setOutputStream(baos,true);
                    channel.setInputStream(bais);
                    channel.connect(30000);

            //        input = channel.getInputStream();
          //          System.out.println("Processing input stream");
            //        s1 = new Scanner(input);
                    run = false;
                //    channel.setOutputStream(convertStringtoStream(output));
             //       System.out.write(output.getBytes(),0,output.length());
           //         input = channel.getInputStream();

//                    System.out.println("Disconnecting");

                    Thread.sleep(2000);
                    //System.setIn(new ByteArrayInputStream(output.getBytes()));
   /*               System.out.println("Sending...");
                    InputStream stream1 = new ByteArrayInputStream(output.getBytes());
                    System.setIn(stream1);*/
                    Thread.sleep(2000);
                    channel.disconnect();
                    return next;

                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }

                return null;
                }
            }
        }
