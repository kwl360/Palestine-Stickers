package com.kwl360.palestinestickers;
import android.content.*;import android.content.res.AssetFileDescriptor;import android.database.*;import android.database.MatrixCursor;import android.net.Uri;import android.os.*;import java.io.*;import java.util.*;
public class StickerProvider extends ContentProvider {
 static final String ID="palestine_boy", ROOT="stickers/"+ID+"/";
 static final String[] META={"identifier","name","publisher","tray_image_file","image_data_version","avoid_cache","publisher_email","publisher_website","privacy_policy_website","license_agreement_website","animated_sticker_pack"};
 static final String[] STICKERS={"sticker_file_name","sticker_emoji"};
 @Override public boolean onCreate(){return true;}
 @Override public String getType(Uri uri){return "vnd.android.cursor.item/vnd.com.whatsapp.sticker";}
 @Override public Cursor query(Uri uri,String[] projection,String selection,String[] selectionArgs,String sortOrder){
 List<String> s=uri.getPathSegments();MatrixCursor c;
 if(s.size()>0 && s.get(0).equals("metadata")){c=new MatrixCursor(META);if(s.size()==1 || (s.size()==2&&s.get(1).equals(ID)))
 c.addRow(new Object[]{ID,"ضحكات ابن فلسطين","KWL360","tray.png","1",0,"","","","","1"});return c;}
 if(s.size()==2&&s.get(0).equals("stickers")&&s.get(1).equals(ID)){
 c=new MatrixCursor(STICKERS);String[] emojis={"🌹","👍","😂","❤️","😱","😅","🇵🇸","🙏","🤫","🌙","🤷","👋"};
 for(int i=1;i<=12;i++)c.addRow(new Object[]{String.format(Locale.US,"sticker_%02d.webp",i),emojis[i-1]});return c;}
 return null;
 }
 @Override public AssetFileDescriptor openAssetFile(Uri uri,String mode)throws FileNotFoundException {
 List<String>s=uri.getPathSegments();String filename=null;
 if(s.size()==3&&(s.get(0).equals("stickers_asset")||s.get(0).equals("stickers"))&&s.get(1).equals(ID))filename=s.get(2);
 if(filename==null || !filename.matches("sticker_[0-9]{2}\\.webp|tray\\.png"))throw new FileNotFoundException();
 try {InputStream in=getContext().getAssets().open(ROOT+filename);
 File cache=new File(getContext().getCacheDir(),filename);
 try(FileOutputStream out=new FileOutputStream(cache)){byte[] buf=new byte[8192];int n;while((n=in.read(buf))!=-1)out.write(buf,0,n);}in.close();
 return new AssetFileDescriptor(ParcelFileDescriptor.open(cache,ParcelFileDescriptor.MODE_READ_ONLY),0,cache.length());
 }catch(IOException e){throw new FileNotFoundException(e.getMessage());}
 }
 @Override public int delete(Uri u,String s,String[] a){return 0;}
 @Override public Uri insert(Uri u,ContentValues v){return null;}
 @Override public int update(Uri u,ContentValues v,String s,String[] a){return 0;}
}