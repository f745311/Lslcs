package com.example.chenhaowei.lslcs_104;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenhaowei on 16/5/1.
 */
public class itemSeperate {
    String input;
   public itemSeperate(String input){
    this.input = input;
   }
    public ArrayList<HashMap<String, String>> seperate() {
       // Log.e("input",input);
        if (input.equals("")||input.equals("wrong")) {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ID","暫時沒有");
            list.add(map);
            return list;
        } else {
            String[] items = input.split("@@@@@");
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < items.length; i++) {
                String[] content = items[i].split("###");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", content[0]);
                map.put("TYPE", content[1]);
                map.put("IP", content[2]);
                map.put("Longitude", content[3]);
                map.put("Latitude", content[4]);
                map.put("On_Off", content[5]);
                map.put("broken", content[6]);
                map.put("DistID", content[7]);
                map.put("ZoneID", content[8]);
                map.put("SegmentID", content[9]);
                map.put("NodeID", content[10]);

                list.add(map);
            }
            return list;
        }
    }
}
