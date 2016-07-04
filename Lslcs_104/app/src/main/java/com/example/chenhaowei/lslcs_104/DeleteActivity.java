package com.example.chenhaowei.lslcs_104;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by chenhaowei on 16/5/12.
 */
public class DeleteActivity extends AppCompatActivity {
    final private String deleteUrl = "http://dmcl.twbbs.org/104_Project/haowei_delete.php";
    final private String[] TypeSpinner = {
            "選擇時間 ▼","一小時內","四小時內","一天內"
    };
    private String FormRegerster;
    ArrayList<HashMap<String, String>> Timelist = new ArrayList<HashMap<String, String>>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        toolbarSet();
        spinnerSet();
    }
    /*
    set spinner contain and when spinner be click
     */
    public void spinnerSet(){
        final Spinner spi_type = (Spinner)findViewById(R.id.delete_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this , R.layout.spinner_resourse , TypeSpinner);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdownview_resourse);
        spi_type.setAdapter(spinnerAdapter);
        spi_type.setSelection(0);
        spi_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Calendar today = Calendar.getInstance();
                String todayForm;
                switch (position) {
                    case 1:
                        today.add(Calendar.HOUR, -1);
                        /*
                        get the time that current time hour-1
                         */
                        todayForm = dateForm(today);
                        FormRegerster = todayForm;
                        new catchthread().execute(todayForm);
                        break;
                    case 2:
                        today.add(Calendar.HOUR, -4);
                        todayForm = dateForm(today);
                        FormRegerster = todayForm;
                        new catchthread().execute(todayForm);
                        break;
                    case 3:
                        today.add(Calendar.DATE, -1);
                        todayForm = dateForm(today);
                        FormRegerster = todayForm;
                        new catchthread().execute(todayForm);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    public void toolbarSet(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.delete_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(63, 81, 181));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.ic_home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /*
    same form with database and compare in php
     */
    private String dateForm(Calendar today){
        String Y = String.valueOf(today.get(Calendar.YEAR));
        String M = String.valueOf(today.get(Calendar.MONTH) + 1);//0 is 1month 11 is December(?)
        String D = String.valueOf(today.get(Calendar.DATE));
        String H = String.valueOf(today.get(Calendar.HOUR_OF_DAY));
        String Mi = String.valueOf(today.get(Calendar.MINUTE));
        String tem = Y+"-"+M+"-"+D+" "+H+":"+Mi+":00";
        //Log.e("time", tem);
        return tem;
    }
    public void onDeleteClick(int position){
        String ID = Timelist.get(position).get("ID");
        new deletethread().execute(ID);
        new catchthread().execute(FormRegerster);
    }
    class OnListClick implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            String tem = "ID："+Timelist.get(position).get("ID")+
                    "\nType："+Timelist.get(position).get("TYPE")+
                    "\nIP："+Timelist.get(position).get("IP")+
                    "\n經度："+Timelist.get(position).get("Longitude")+
                    "\n緯度："+Timelist.get(position).get("Latitude")+
                    "\nDistID："+Timelist.get(position).get("DistID")+
                    "\nZoneID："+Timelist.get(position).get("ZoneID")+
                    "\nSegmentID："+Timelist.get(position).get("SegmentID")+
                    "\nNodeID："+Timelist.get(position).get("NodeID");
            delete_dialog editNameDialog = delete_dialog.newInstance
                    ("刪除？",tem, "等等","刪除！",position);
            editNameDialog.show(getFragmentManager(), "EditNameDialog");
        }
    }
    class catchthread extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... arg0) {
            String tem = "select * from `test` where `update` >= '"+arg0[0]+"'";
            String ans=
                    new getSQLdata(tem).getServerConnect();
            //Log.e("catch",ans);

            return ans;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("Warning")){
                //Log.v("get","get an error from server");
            }
            else{
                Timelist = new ArrayList<HashMap<String, String>>();
                Timelist =new itemSeperate(result).seperate();
            }
            ListView list_delete = (ListView) findViewById(R.id.delete_listView);
            ListAdapter adapter = new SimpleAdapter(
                    DeleteActivity.this, Timelist,
                    R.layout.list_resourse, new String[] { "ID" }
                    , new int[] { R.id.list_resourse_text });
            list_delete.setAdapter(adapter);
            list_delete.setOnItemClickListener(new OnListClick());
        }


    }
    class deletethread extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("ID", arg0[0]));

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(deleteUrl);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();
               // Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                //Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }
            return arg0[0];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //ArrayListToJSONArray();
            Toast.makeText(getApplicationContext(), "已刪除！ID="+result,
                    Toast.LENGTH_SHORT).show();
        }


    }
}
