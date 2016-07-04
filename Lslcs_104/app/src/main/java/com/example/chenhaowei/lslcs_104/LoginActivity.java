package com.example.chenhaowei.lslcs_104;


import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity  extends DialogFragment {

    private TextView tv_errortext;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.login_fragment, container);
        final EditText ed_account = (EditText) view.findViewById(R.id.login_account);
        final EditText ed_password = (EditText) view.findViewById(R.id.login_password);
        final Button bt_summit = (Button) view.findViewById(R.id.login_summit);
        tv_errortext = (TextView) view.findViewById(R.id.login_errortext);
        bt_summit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String password = ed_password.getText().toString();
                String account = ed_account.getText().toString();
                String command ="select * from account where account='"+account+"' and password='"+password+"'";
                new checkthread().execute(command);
            }
        });
        return view;
    }

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
            if(tem.length>=2){
                return true;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result==true){
                LoginActivity.this.dismiss();
            }
            else {
                tv_errortext.setText("帳號密碼錯誤");
            }
        }

    }

}

