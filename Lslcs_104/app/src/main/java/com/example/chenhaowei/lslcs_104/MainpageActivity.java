package com.example.chenhaowei.lslcs_104;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by chenhaowei on 16/4/30.
 */
public class MainpageActivity extends AppCompatActivity {
    private ListView List_needfixList;
    private String[] drawerTitle=new String[]{"路燈開啟","輸入位置","刪除位置","是否關閉通知"};
    private int[] drawerImage=new int[]{R.mipmap.ic_openlight,R.mipmap.ic_inputlocation,R.mipmap.ic_delete,R.mipmap.ic_main_used};
    private ArrayList<HashMap<String,String>> lightlist=new ArrayList<HashMap<String,String>>();
    private Timer timer;
    private boolean userback = false;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        List_needfixList = (ListView) findViewById(R.id.mainpage_list);
        List_needfixList.setOnItemClickListener(new OnListClick());
        new needfixthread().execute();
        showEditDialog();
        Drawerset();
        Timerset();
        checkGPS();
        toolbarSet();
    }
    public void toolbarSet(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainpage_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(63, 81, 181));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.ic_leftdrawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_reset:
                new needfixthread().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
    set a timer
     */
    private void Timerset(){
        timer = new Timer(true);
        //the first notation is in 10min and next is duration 10min
        timer.schedule(new breakTimer(), 600000, 600000);
    }
    private void Drawerset(){
        ArrayList<Map<String,Object>> drawerlist=new ArrayList<Map<String,Object>>();
        for(int i=0;i<drawerTitle.length;i++){
            Map<String,Object> item=new HashMap<String,Object>();
            item.put("drawer_item_image", drawerImage[i]);
            item.put("drawer_item_text", drawerTitle[i]);
            drawerlist.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,drawerlist,
                R.layout.list_drawer_resourse
                ,new String[]{"drawer_item_image","drawer_item_text"}
                ,new int[]{R.id.drawer_image,R.id.drawer_text});
        ListView list_drawer = (ListView) findViewById(R.id.mainpage_right_drawer);
        list_drawer.setAdapter(adapter);
        list_drawer.setOnItemClickListener(new OndrawerListClick());
    }
    public void showEditDialog()
    {
        LoginActivity editNameDialog = new LoginActivity();
        editNameDialog.setCancelable(false);
        editNameDialog.show(getFragmentManager(), "EditNameDialog");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(userback==false) {
                Toast.makeText(this, "再按一次離開", Toast.LENGTH_SHORT).show();

                userback=true;
                Timer backTimer = new Timer(true);
                backTimer.schedule(new backTimer(), 2000);
            }
            else if(userback == true){
                finish();
            }
        }
        return false;
    }
    class needfixthread extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(Void... arg0) {

            String ans=new getSQLdata("select * from test where broken=1").getServerConnect();
            //Log.e("input",String.valueOf(input.equals("")));
            return ans;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("Warning")){
                //Log.v("get","get an error from server");
            }
            else{
                lightlist=new itemSeperate(result).seperate();
            }
            ListAdapter adapter = new SimpleAdapter(
                    MainpageActivity.this, lightlist,
                    R.layout.list_resourse, new String[] { "ID" }
                    , new int[] { R.id.list_resourse_text });
            List_needfixList.setAdapter(adapter);
        }

    }

    class OnListClick implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            String tem = "經度："+lightlist.get(position).get("Longitude")+"\n緯度："+lightlist.get(position).get("Latitude");
            mainpage_dialog editNameDialog = mainpage_dialog.newInstance
                    ("出發？",tem, "等等","出發",position);
            editNameDialog.show(getFragmentManager(), "EditNameDialog");
        }
    }
    public void onStartClick(int position){
        Intent intent = new Intent(MainpageActivity.this, NavigationActivity.class);
        intent.putExtra("latitude", lightlist.get(position).get("Latitude"));
        intent.putExtra("longitude", lightlist.get(position).get("Longitude"));
        startActivity(intent);
    }
    class OndrawerListClick implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            closeDrawer();
            Intent intent = new Intent();
            // TODO Auto-generated method stub
            switch (position){
                case 0:

                    intent.setClass(MainpageActivity.this, OpenLightActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent.setClass(MainpageActivity.this, InputlocationActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent.setClass(MainpageActivity.this,DeleteActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    if(drawerImage[3]==R.mipmap.ic_main_used){
                        drawerImage[3] = R.mipmap.ic_main_nonuse;
                        Drawerset();
                        timer.cancel();
                    }
                    else{
                        drawerImage[3] = R.mipmap.ic_main_used;
                        Drawerset();
                        timer = new Timer(true);
                        timer.schedule(new breakTimer(), 600000, 600000);
                    }
                    break;
            }

        }

    }
    public void closeDrawer(){
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.mainpage_drawer) ;
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void openDrawer(){
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.mainpage_drawer) ;
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public class backTimer extends TimerTask
    {
        public void run()
        {
            userback = false;
        }
    }

    public class breakTimer extends TimerTask
    {
        public void run()
        {
            String ans=new getSQLdata("select * from test where broken=1").getServerConnect();

            String[] tem =ans.split("@@@@@");
            if(tem.length>1){
                final int notifyID = 1; // 通知的識別號碼
                int D=0;

                D |= Notification.DEFAULT_VIBRATE;
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(D).setContentTitle("糟糕！").setContentText("又有工作囉！").build(); // 建立通知
                notificationManager.notify(notifyID, notification);
            }
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
    protected void onResume(){
        super.onResume();
        new needfixthread().execute();
        //載入重新更新
    }

    protected void onPause(){
        super.onPause();
    }
}
