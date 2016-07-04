package com.example.chenhaowei.lslcs_104;


import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by chenhaowei on 16/5/6.
 */
public class OpenLightActivity extends AppCompatActivity {
    private String mapUrl = "file:///android_asset/testSegment.html";
    private String serverUrl = "http://dmcl.twbbs.org/104_Project/haowei_testSegment.html";
    private WebView webView;
    private String gps = LocationManager.GPS_PROVIDER;
    private String network = LocationManager.NETWORK_PROVIDER;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openlight);
        toolbarSet();
        webView = (WebView) findViewById(R.id.openlight_webView);
        webView.addJavascriptInterface(this, "Android");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(serverUrl);
    }
    public void toolbarSet(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.openlihgt_toolbar);
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
    public void OnDeleteClick (View view){
        //it's a way to let javascrip exec its function
        webView.loadUrl("javascript:deleteAllShapes()");
    }
    public void OnOnClick (View view){
        webView.loadUrl("javascript:turnOnlight()");
    }
    public void OnOffClick (View view){
        webView.loadUrl("javascript:turnOfflight()");
    }
    @JavascriptInterface
    public void haveNotChoose()
    {
        Toast.makeText(this, "你還沒選擇範圍呢!", Toast.LENGTH_SHORT).show();
    }


}