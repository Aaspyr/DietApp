package com.example.godiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Stetho*/
        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        /* Database*/
        DBAdapter db = new DBAdapter(this);
        db.open();

        /* Setup for food */
        // Count rows in food
        int numberRows = db.count("food");

        if(numberRows < 1){
            // Run setup
            Toast.makeText(this, "Loading setup", Toast.LENGTH_LONG).show();
            DBSetupInsert setupInsert = new DBSetupInsert(this);
            setupInsert.insertAllCategories();
            setupInsert.insertAllFood();
            Toast.makeText(this, "Setup completed!", Toast.LENGTH_LONG).show();
        }

        /* Check if user table exist */

        numberRows = db.count("users");
        if(numberRows < 1){
            // Sign up
            Toast.makeText(this, "You are only few fields away from signing up...", Toast.LENGTH_LONG).show();
           numberRows = db.count("users");
           if(numberRows<1){
               Toast.makeText(this, "You have to sign up...", Toast.LENGTH_LONG).show();
               Intent i = new Intent(MainActivity.this, SignUp.class);
               startActivity(i);
           }

        }
        /*test update*/
        long id = 1;
        String value = "test@cydziala.pl";
        db.update("users", "user_id", id, "user_email", value);

        /*Close DB*/
        db.close();

       //Toast.makeText(this, "Dziala", Toast.LENGTH_SHORT).show();
    }
}