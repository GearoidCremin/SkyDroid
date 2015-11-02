package com.compsoc.skynet.skydroid;

import com.jcraft.jsch.*;
//import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Scanner;
//import java.io.PrintStream;
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
public class SkynetLogin extends AppCompatActivity {
    //    static PrintStream commander;
//    static ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    static Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skynet_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Welcome to the Computers Society! Current news will appear here.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skynet_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void joinSkynet(View view) {
        Intent joinSkynet = new Intent(this, JoinSkynet.class);
        startActivity(joinSkynet);
    }

    public void login(View view) {

        EditText usernameEdit = (EditText) findViewById(R.id.editText2);
        String user = usernameEdit.getText().toString();
        EditText passwordEdit = (EditText) findViewById(R.id.editText);
        String pass = passwordEdit.getText().toString();
        System.out.println("Username: "+user);
        System.out.println("Password: "+pass);
        if(user!=null && pass!= null) {
            System.out.println("Creating HomePage");
            Intent homePage = new Intent(this, HomePage.class);
            homePage.putExtra("pass", pass);
            homePage.putExtra("user", user);
            System.out.println("Sending to HomePage");
            startActivity(homePage);
        }
    }
}
