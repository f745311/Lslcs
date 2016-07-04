package com.example.chenhaowei.lslcs_104;

import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by chenhaowei on 16/5/1.
 * it's pop the dialog directly , because we not locate the point actually
 */
public class NavigationActivity extends AppCompatActivity implements LocationListener{

    private LocationManager locationManager;
    private Location bestProviderLocation;
    private WebView wv_map;
    private String EndLat;
    private String EndLon;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getEnd();
        toolbarSet();
        webViewSetting();
        showEditDialog();
    }
    private void webViewSetting(){
        final String mapUrl = "file:///android_asset/GoogleMap.html";
        wv_map = (WebView)findViewById(R.id.navigation_map);
        wv_map.getSettings().setJavaScriptEnabled(true);
        wv_map.getSettings().setDomStorageEnabled(true);
        wv_map.addJavascriptInterface(this, "Android");
        wv_map.setWebViewClient(new WebViewClientDemo());
        wv_map.loadUrl(mapUrl);
    }
    /*
    define a webviewclient
     */
    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //在这里执行你想调用的js函数
            locationServiceInitial();

        }

    }


    public void toolbarSet(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.navigation_toolbar);
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
    private void getEnd(){
        Intent intent = getIntent();
        EndLat = intent.getExtras().getString("latitude");
        //Log.e("endLat", EndLat);
        EndLon = intent.getExtras().getString("longitude");
    }
    public void showEditDialog()
    {
        controlLight_dialog editNameDialog = new controlLight_dialog();
        editNameDialog.setCancelable(false);
        editNameDialog.show(getFragmentManager(), "EditNameDialog");

    }
    public void showInfoEditDialog(final String tem)
    {

        Info_dialog editNameDialog = Info_dialog.newInstance("資訊",tem);
        editNameDialog.show(getFragmentManager(), "EditNameDialog");

    }
    private void locationServiceInitial() {
        checkGPS();
        final String gps = LocationManager.GPS_PROVIDER;
        final String network = LocationManager.NETWORK_PROVIDER;
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
        try{
        locationManager.requestLocationUpdates(bestProvider, 5000, 10, this);
        bestProviderLocation = locationManager.getLastKnownLocation(network);}
        catch (SecurityException e){

        }
       // Log.i("best",String.valueOf(locationManager.isProviderEnabled(bestProvider)));
        //Log.i("gps",String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        //Log.i("network",String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
        if(bestProviderLocation!=null) {
           // Log.e("getLocationWay",bestProvider);
            this.onLocationChanged(bestProviderLocation);
        }
        else {
            try {
                locationManager.removeUpdates(this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
                bestProviderLocation = locationManager.getLastKnownLocation(bestProvider);
            }
            catch (SecurityException e){

            }
            if(bestProviderLocation!=null){
                //Log.e("getLocationWay","GPS");
                this.onLocationChanged(bestProviderLocation);
            }
            else{
                try {
                    locationManager.removeUpdates(this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
                    bestProviderLocation = locationManager.getLastKnownLocation(gps);
                }
                catch (SecurityException e){

                }
                if(bestProviderLocation!=null){
                   // Log.e("getLocationWay","NETWORK");
                    this.onLocationChanged(bestProviderLocation);
                }
                else {
                    Toast.makeText(this, "無法定位座標", Toast.LENGTH_SHORT).show();
                    try {
                        locationManager.removeUpdates(this);
                        locationServiceInitial();
                    }
                    catch (SecurityException e){

                    }
                }
            }
        }

    }
    public void onLocationChanged(Location location) {
        if(location != null) {
            double d_lati = location.getLatitude();
            double d_lngi = location.getLongitude();
            String latitude = String.valueOf(d_lati);
            String longitude = String.valueOf(d_lngi);
            if (Math.abs(d_lati - Double.valueOf(EndLat)) <= 0.000003 && Math.abs(d_lngi - Double.valueOf(EndLon)) <= 0.000003) {
                showEditDialog();
            }
            /*
            it is a bad way to check locate or not,but i'm lazy
             */
            Log.e("lati",latitude);
            Log.e("lngi", longitude);
            wv_map.loadUrl("javascript:direct(\"" + latitude + "\",\"" + longitude + "\")");
            //wv_map.loadUrl("javascript:direct(" + "24.937190"+ "," + "121.361814" + ")");
        }
        else{
           // Log.e("getlocation","2");
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_SHORT).show();
        }
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                NavigationActivity.this.setTitle("AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                NavigationActivity.this.setTitle("OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                NavigationActivity.this.setTitle("TEMPORARILY_UNAVAILABLE");
                break;
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        checkGPS();
        locationServiceInitial();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            checkGPS();
            locationManager.removeUpdates(this);
        }//離開頁面時停止更新
        catch (SecurityException e){

        }

    }

    public void checkGPS(){
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
    public String getLng(){

        return EndLon;
    }
    public String getLat(){
        return EndLat;
    }
/*
@JavascriptInterface is a function to connect with javascrip
 */
    @JavascriptInterface
    public String GetEndLon()
    {
        Log.e("Elon",EndLon);
        return EndLon;
    }
    @JavascriptInterface
    public String GetEndLat()
    {
        Log.e("Elat",EndLat);
        return EndLat;
    }
    @JavascriptInterface
    public void getDistDura(final String dura,final String dist){

        new textchangeThread().execute(dura,dist);
    }




    class textchangeThread extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... navigationInfo) {
            // TODO Auto-generated method stub
            String text =
            "距離："+navigationInfo[1]+"        時間："+navigationInfo[0];
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //ArrayListToJSONArray();
            TextView tv_info = (TextView) findViewById(R.id.navigation_info);
            try {
                tv_info.setText(result);
            }
            catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

}
