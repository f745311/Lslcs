package com.example.chenhaowei.lslcs_104;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by chenhaowei on 16/5/10.
 */
public class mainpage_dialog extends DialogFragment {
    public static mainpage_dialog newInstance(String title,String message,String positiveButton,String negativeButton,int position){
        mainpage_dialog frag = new mainpage_dialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message",message);
        args.putString("positiveButton",positiveButton);
        args.putString("negativeButton",negativeButton);
        args.putInt("position",position);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        final String message = getArguments().getString("message");
        final String positiveButton = getArguments().getString("positiveButton");
        final String negativeButton = getArguments().getString("negativeButton");
        final int position = getArguments().getInt("position");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton,null)
                .setNegativeButton(negativeButton,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MainpageActivity)getActivity()).onStartClick(position);
                    }
                } )
                .setCancelable(false);

        //.show(); // show cann't be use here

        return builder.create();
    }
}
