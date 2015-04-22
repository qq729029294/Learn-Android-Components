package com.sky;

import android.net.Uri;

public class ServiceDef {
    public static final String DBNAME = "serviedb";  
    public static final int VERSION = 1;
    public static final String AUTOHORITY = "com.sky.login"; 
    
    public static final String VOTETNAME = "vote";
    public static final String VOTEID = "voteid";
    public static final String VOTENAME = "votename";
    public static final int VOTE_ITEM = 1;
    public static final int VOTE_ITEM_ID = 2;
    public static final String VOTE_ITEM_TYPE = "vnd.android.cursor.dir/com.multiapk_service.login/vote"; 
    public static final String VOTE_ITEM_ID_TYPE = "vnd.android.cursor.item/com.multiapk_service.login/vote";
    public static final Uri VOTE_URI = Uri.parse("content://" + AUTOHORITY + "/vote"); 
}