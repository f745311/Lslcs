package com.example.chenhaowei.lslcs_104;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by chenhaowei on 16/5/17.
 */
public class Info_dialog extends DialogFragment {
    public static Info_dialog newInstance(String title,String message){
        Info_dialog frag = new Info_dialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message",message);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        final String message = getArguments().getString("message");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message);


        //.show(); // show cann't be use here

        return builder.create();
    }
}
