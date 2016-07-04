package com.example.chenhaowei.lslcs_104;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenhaowei on 16/5/10.
 */
public class controlLight_dialog extends DialogFragment {
    private ImageView imageView;
    private String lng;
    private String lat;
    private String updateURL ="http://dmcl.twbbs.org/104_Project/haowei_update";
    private String completeURL ="http://dmcl.twbbs.org/104_Project/haowei_complete";
    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.activity_controllight, container);
        imageView = (ImageView) view.findViewById(R.id.control_img);
        lng = ((NavigationActivity)getActivity()).getLng();
        lat = ((NavigationActivity)getActivity()).getLat();
        final Button bt_left = (Button)view.findViewById(R.id.control_left);
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlLight_dialog.this.dismiss();
            }
        });
        final Button bt_complete = (Button) view.findViewById(R.id.control_complete);
        bt_complete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new completethread().execute(completeURL);
            }
        });

        final String command ="select * from test where Longitude='"+lng+"' and Latitude='"+lat+"'";
        new checkthread().execute(command);
        final Button bt_Info = (Button) view.findViewById(R.id.controllight_info);
        bt_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new catchthread().execute(command);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new clickthread().execute(command);
            }
        });

        return view;
    }
/*
* get info from database */
    class checkthread extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(String... arg0) {

            String ans=
                    new getSQLdata(arg0[0]).getServerConnect();
            String[] tem =ans.split("###");
            if(tem[5].equals("1")){
                return true;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result==true){
                imageView.setBackgroundResource(R.drawable.bright);
            }
            else {
                imageView.setBackgroundResource(R.drawable.dark);
            }

        }

    }
/*
* when click image write info to database and get info from database*/
    class clickthread extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(String... arg0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            String ans=
                    new getSQLdata(arg0[0]).getServerConnect();
            String[] tem =ans.split("###");
            if(tem[5].equals("1")){
                try
                {
                    nameValuePairs.add(new BasicNameValuePair("On_Off", "0"));
                    nameValuePairs.add(new BasicNameValuePair("Longitude", lng));
                    nameValuePairs.add(new BasicNameValuePair("Latitude", lat));


                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(updateURL);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();

                    //Log.e("pass 1", "connection success ");

                }
                catch(Exception e)
                {
                   // Log.e("Fail 1", e.toString());
                }
                return true;
            }

            else {
                try {
                    nameValuePairs.add(new BasicNameValuePair("On_Off", "1"));
                    nameValuePairs.add(new BasicNameValuePair("Longitude", lng));
                    nameValuePairs.add(new BasicNameValuePair("Latitude", lat));


                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(updateURL);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();

                    //Log.e("pass 1", "connection success ");

                } catch (Exception e) {
                    //Log.e("Fail 1", e.toString());
                }
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result==true){
                imageView.setBackgroundResource(R.drawable.dark);
            }
            else {
                imageView.setBackgroundResource(R.drawable.bright);
            }

        }

    }

/*
* when complete button be click ,dialog finish and set database 'broken' = 0*/
    class completethread extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... arg0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                try
                {
                    nameValuePairs.add(new BasicNameValuePair("Longitude", lng));
                    nameValuePairs.add(new BasicNameValuePair("Latitude", lat));


                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(arg0[0]);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();

                    //Log.e("pass 1", "connection success ");

                }
                catch(Exception e)
                {
                    //Log.e("Fail 1", e.toString());
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            controlLight_dialog.this.dismiss();
        }

    }
    class catchthread extends AsyncTask<String,Void,String> {
        
        @Override
        protected String doInBackground(String... arg0) {
            String ans=
                    new getSQLdata(arg0[0]).getServerConnect();
            String[] oneArray = ans.split("@@@@@");
            String[] twoArray =oneArray[0].split("###");
            String tem = "ID："+twoArray[0]+
                    "\nType："+twoArray[1]+
                    "\nIP："+twoArray[2]+
                    "\n經度："+twoArray[3]+
                    "\n緯度："+twoArray[4]+
                    "\nDistID："+twoArray[7]+
                    "\nZoneID："+twoArray[8]+
                    "\nSegmentID："+twoArray[9]+
                    "\nNodeID："+twoArray[10];
            return tem;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ((NavigationActivity)getActivity()).showInfoEditDialog(result);
        }


    }
}
