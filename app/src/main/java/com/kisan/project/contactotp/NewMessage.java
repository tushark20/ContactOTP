package com.kisan.project.contactotp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class NewMessage extends AppCompatActivity {

    static  String NO_INTERNET_CONNECTION_ERROR = "No internet connection.";
    static  String SUCCESS = "The message is successfully accepted for delivery by Nexmo.";//0
    static  String MSG_TEXT = "Hi. Your OTP is: ";
    static  String INTERNAL_ERROR = "There was an error processing your request in the Platform.";//5
    static  String INVALID_MESSAGE = "unrecognized text or phone number.";//6
    static  String NUMBER_NOT_APPROVED = "The number needs to be pre-approved.";//29
    EditText editOTP;
    String msg="";
    String name ="";
    String number ="";
    String time ="";
    String otp="";
    JSONObject jObj;
    String status;
    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        try
        {
            setTitle("New Message");
            getActionBar().setHomeButtonEnabled(true);
        }catch (Exception e)
        {

        }

        Intent intent = getIntent();
        final String jsonString = intent.getStringExtra("json_contact");

        try
        {
            jObj = new JSONObject(jsonString);
            name = jObj.getString("fname")+" "+jObj.getString("lname");
            number = jObj.getString("number").replace("+", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        editOTP = (EditText)findViewById(R.id.editTextOTP);

        //generates 6-digit random number
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        otp = String.valueOf(n);

        editOTP.setText(MSG_TEXT+otp);
        msg = editOTP.getText().toString().replaceAll(" ", "+");
    }


    public void sendOTP(View v)
    {
        // Instantiate the RequestQueue.
        time =  DateFormat.getDateTimeInstance().format(new Date());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://rest.nexmo.com/sms/json?api_key=f709759b&api_secret=a6c22c4c5dcb0b14&from=TUSHAR&to="+number+"&text="+msg;
        //"https://rest.nexmo.com/sms/json?api_key=f709759b&api_secret=a6c22c4c5dcb0b14&from=NEXMO&to=919970257396&text=Welcome+to+Nexmo";


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject myObject = new JSONObject(response);
                            status = myObject.getJSONArray("messages").getJSONObject(0).get("status").toString();
                            if(status.equals("0"))
                            {
                                status = "Success";
                                error = SUCCESS;
                                DBHandler db = new DBHandler(getApplicationContext());
                                db.addMsg(name, number, time, otp);
                            }
                            else if(status.equals("29"))
                            {
                                status = "Error";
                                error = NUMBER_NOT_APPROVED;
                            }
                            else if(status.equals("5"))
                            {
                                status = "Error";
                                error = INTERNAL_ERROR;
                            }
                            else if(status.equals("6"))
                            {
                                status = "Error";
                                error = INVALID_MESSAGE;
                            }
                            else
                            {
                                status = "Error";
                                error = myObject.getJSONArray("messages").getJSONObject(0).get("error-text").toString();
                            }

                            DialogBox(status,error);
                            editOTP.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogBox("Error", "No Internet Connection");
                editOTP.setText(error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    //Dialog box to show notification
    public void DialogBox(String _status, String _message)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setTitle(_status);
        dlgAlert.setMessage(_message);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dlgAlert.create().show();
    }

}

