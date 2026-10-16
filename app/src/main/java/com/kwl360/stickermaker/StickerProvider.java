package com.kwl360.stickermaker;
import android.content.*;import android.database.*;import android.database.MatrixCursor;import android.net.Uri;import android.os.*;import android.content.res.AssetFileDescriptor;import org.json.*;import java.io.*;import java.util.*;
public class StickerProvider extends ContentProvider {
 static final String[] META={"identifier","name","publisher","tray_image_file","image_data_version","avoid_cache","publisher_email","publisher_website","privacy_policy_website","license_agreement_website","animated_sticker_pack"};
 static final String[] STICKERS={"sticker_file_name","sticker_emoji"};
 @Override public boolean onCreate(){return true;}
 @Override public String getType(Uri uri){return "image/webp";}
 @Override public Cursor query(Uri uri,String[] proj,String sel,String[] args,String order){List<String>s=uri.getPathSegments();if(s.size()==0)return null;
 if("metadata".equals(s.get(0))){MatrixCursor c=new MatrixCursor(META);JSONArray a=PackStore.packs(getContext());for(int i=0;i<a.length();i++){JSONObject p=a.optJSONObject(i);if(p==null)continue;String id=p.optString("id");if(s.size()==2&&!id.equals(s.get(1)))continue;if(PackStore.count(getContext(),id)<3)continue;c.addRow(new Object[]{id,p.optString("name"),"KWL360","tray.png",String.valueOf(PackStore.count(getContext(),id)),0,"","","","",0});}return c;}
 if(s.size()==2&&"stickers".equals(s.get(0))){JSONObject p=PackStore.get(getContext(),s.get(1));if(p==null)return null;MatrixCursor c=new MatrixCursor(STICKERS);JSONArray f=p.optJSONArray("files");for(int i=0;i<f.length();i++)c.addRow(new Object[]{f.optString(i),"😀"});return c;}
 return null;}
 @Override public AssetFileDescriptor openAssetFile(Uri uri,String mode)throws FileNotFoundException{List<String>s=uri.getPathSegments();if(s.size()!=3||!("stickers_asset".equals(s.get(0))||"stickers".equals(s.get(0))))throw new FileNotFoundException();String id=s.get(1),file=s.get(2);JSONObject p=PackStore.get(getContext(),id);if(p==null||!file.matches("sticker_[0-9]{3}\\.webp|tray\\.png"))throw new FileNotFoundException();File f=new File(PackStore.dir(getContext(),id),file);if(!f.isFile())throw new FileNotFoundException();return new AssetFileDescriptor(ParcelFileDescriptor.open(f,ParcelFileDescriptor.MODE_READ_ONLY),0,f.length());}
 @Override public int delete(Uri u,String s,String[] a){return 0;}@Override public Uri insert(Uri u,ContentValues v){return null;}@Override public int update(Uri u,ContentValues v,String s,String[] a){return 0;}
}