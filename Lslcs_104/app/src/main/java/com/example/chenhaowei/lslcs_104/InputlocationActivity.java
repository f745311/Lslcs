package com.example.chenhaowei.lslcs_104;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * Created by chenhaowei on 16/5/5.
 */
public class InputlocationActivity extends AppCompatActivity implements LocationListener{
    private String[] TypeSpinner = {
            "選擇種類 ▼","end node"
    };
    private String sqlurl = "http://dmcl.twbbs.org/104_Project/haowei_insert.php";
    private LocationManager locationManager;
    private Location bestProviderLocation;
    private String Longitude;
    private String Latitude;
    private EditText et_ID;
    private EditText et_IP;
    private EditText et_DistID;
    private EditText et_ZoneID;
    private EditText et_SegmentID;
    private EditText et_NodeID;
    private Spinner spi_type;
    private TextView tv_Longi;
    private TextView tv_Lati;
    private String ID;
    private String IP;
    private String DistID;
    private String ZoneID;
    private String SegmentID;
    private String NodeID;
    private String type;
    private String gps = LocationManager.GPS_PROVIDER;
    private String network = LocationManager.NETWORK_PROVIDER;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initial();
        toolbarSet();
    }
    public void initial(){
        et_ID = (EditText) findViewById(R.id.input_id);
        et_IP = (EditText) findViewById(R.id.input_ip);
        et_DistID = (EditText) findViewById(R.id.input_distid);
        et_ZoneID = (EditText) findViewById(R.id.input_zoneid);
        et_SegmentID = (EditText) findViewById(R.id.input_segmentid);
        et_NodeID = (EditText) findViewById(R.id.input_nodeid);
        spi_type = (Spinner)findViewById(R.id.input_spinner);
        tv_Longi =(TextView) findViewById(R.id.input_showLogi);
        tv_Lati = (TextView) findViewById(R.id.input_showLati);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this , R.layout.spinner_resourse , TypeSpinner);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdownview_resourse);
        spi_type.setAdapter(spinnerAdapter);
        spi_type.setSelection(0);

    }
    public void toolbarSet(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.input_toolbar);
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
    public void OnClearClick(View view){
        et_ID.setText("");
        et_IP.setText("");
        et_DistID.setText("");
        et_ZoneID.setText("");
        et_SegmentID.setText("");
        et_NodeID.setText("");
        spi_type.setSelection(0);
    }
    public void OnUploadClick(View view){
        locationServiceInitial();
    }
    public void OnInputClick(View view){
        if (et_ID.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input ID",
                    Toast.LENGTH_SHORT).show();
        }
        else if (et_IP.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input IP",
                    Toast.LENGTH_SHORT).show();
        }
        else if (spi_type.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), "Select TYPE",
                    Toast.LENGTH_SHORT).show();
        }
        else if (et_DistID.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input DistID",
                    Toast.LENGTH_SHORT).show();
        }
        else if (et_ZoneID.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input ZoneID",
                    Toast.LENGTH_SHORT).show();
        }
        else if (et_SegmentID.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input SegmentID",
                    Toast.LENGTH_SHORT).show();
        }
        else if (et_NodeID.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Input NodeID",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            ID = et_ID.getText().toString();
            IP = et_IP.getText().toString();
            type = TypeSpinner[spi_type.getSelectedItemPosition()];
            DistID = et_DistID.getText().toString();
            ZoneID = et_ZoneID.getText().toString();
            SegmentID = et_SegmentID.getText().toString();
            NodeID = et_NodeID.getText().toString();
            Longitude = tv_Longi.getText().toString();
            Latitude = tv_Lati.getText().toString();
            tv_Longi.setText("");
            tv_Lati.setText("");
            new inserttread().execute();
        }
    }


    class inserttread extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            //new connect();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("ID", ID));
            nameValuePairs.add(new BasicNameValuePair("TYPE",type ));
            nameValuePairs.add(new BasicNameValuePair("IP", IP));
            nameValuePairs.add(new BasicNameValuePair("DistID", DistID));
            nameValuePairs.add(new BasicNameValuePair("ZoneID", ZoneID));
            nameValuePairs.add(new BasicNameValuePair("SegmentID", SegmentID));
            nameValuePairs.add(new BasicNameValuePair("NodeID", NodeID));
            nameValuePairs.add(new BasicNameValuePair("Longitude", Longitude));
            nameValuePairs.add(new BasicNameValuePair("Latitude", Latitude));


            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(sqlurl);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();

                //Log.e("pass 1", "connection success ");



            }
            catch(Exception e)
            {
                //Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //ArrayListToJSONArray();
            Toast.makeText(getApplicationContext(), "Insert successfully",
                    Toast.LENGTH_SHORT).show();
        }


    }
    /*
    get the best way to get location and if best way not work we get network way
     */
    private void locationServiceInitial() {
        checkGPS();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
        Criteria criteria = new Criteria();
        // 获得最好的定位效果
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        // 使用省电模式
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String bestProvider= locationManager.getBestProvider(criteria, true);
        //Log.e("bestprovider", bestProvider);
        locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        bestProviderLocation = locationManager.getLastKnownLocation(network);
        //Log.i("best",String.valueOf(locationManager.isProviderEnabled(bestProvider)));
        //Log.i("gps",String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        //Log.i("network",String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
        if(bestProviderLocation!=null) {
            //Log.e("getLocationWay",bestProvider);
            this.onLocationChanged(bestProviderLocation);
        }
        else {
            locationManager.removeUpdates(this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            bestProviderLocation = locationManager.getLastKnownLocation(bestProvider);
            if(bestProviderLocation!=null){
               // Log.e("getLocationWay","GPS");
                this.onLocationChanged(bestProviderLocation);
            }
            else{
                locationManager.removeUpdates(this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                bestProviderLocation = locationManager.getLastKnownLocation(gps);
                if(bestProviderLocation!=null){
                   // Log.e("getLocationWay","NETWORK");
                    this.onLocationChanged(bestProviderLocation);
                }
                else {
                    Toast.makeText(this, "無法定位座標", Toast.LENGTH_SHORT).show();
                    locationManager.removeUpdates(this);
                    locationServiceInitial();
                }
            }
        }


    }


    @Override
    public void onLocationChanged(Location location) {  //當地點改變時
        // TODO 自動產生的方法 Stub
        if(location != null) {
            double currentLongitude = location.getLongitude();   //取得經度
            double currentLatitude = location.getLatitude();     //取得緯度
           // Log.e("getlocation","");
           // Log.e("catchxy",String.valueOf(currentLongitude));
            tv_Longi.setText(String.valueOf(currentLongitude));
            tv_Lati.setText(String.valueOf(currentLatitude));
        }
        else {
            //Log.e("getlocation","2");
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onProviderDisabled(String arg0) {//當GPS或網路定位功能關閉時
        // TODO 自動產生的方法 Stub
        Toast.makeText(this, "請開啟gps或3G網路", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderEnabled(String arg0) { //當GPS或網路定位功能開啟
        // TODO 自動產生的方法 Stub
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) { //定位狀態改變
        // TODO 自動產生的方法 Stub
        switch (arg1) {
            case LocationProvider.AVAILABLE:
                InputlocationActivity.this.setTitle("AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                InputlocationActivity.this.setTitle("OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                InputlocationActivity.this.setTitle("TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        checkGPS();
        locationServiceInitial();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        checkGPS();
        locationManager.removeUpdates(this);   //離開頁面時停止更新

    }
    public void checkGPS(){
        /*
        check gps nad network are work
         */
        final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
        final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }
}
