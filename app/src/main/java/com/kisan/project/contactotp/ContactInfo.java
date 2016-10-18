package com.kisan.project.contactotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ContactInfo extends AppCompatActivity {

    JSONObject jObj;
    TextView fname,lname,contactno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        try {setTitle("Contact Info");
        }catch (Exception e) {e.printStackTrace();}

       //receive intent from MainActivity
        Intent intent = getIntent();
        final String jsonString = intent.getStringExtra("json_contact");




        fname = (TextView)findViewById(R.id.textfname);
        contactno = (TextView)findViewById(R.id.textcnumber);

        //set name and contact number
        try
        {
            jObj = new JSONObject(jsonString);
            fname.setText(jObj.getString("fname")+" "+jObj.getString("lname"));
            contactno.setText(jObj.getString("number"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //create new message button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(getApplicationContext(), NewMessage.class);//send intent to NewMessage Activity
                sendIntent.putExtra("json_contact", jsonString);
                startActivity(sendIntent);

            }
        });

    }


}
