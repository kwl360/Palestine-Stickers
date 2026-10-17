package com.kwl360.stickermaker;
import android.content.*;import android.net.Uri;import java.io.*;import java.util.*;import java.util.zip.*;
public final class ZipImporter {
 public static class Result {public int imported,skipped,packs;public String warning="";}
 public static Result run(Context c,Uri uri)throws Exception{
 Result r=new Result();Map<String,String> active=new HashMap<>();int entries=0;long total=0;
 File tmp=new File(c.getCacheDir(),"kwl-"+System.nanoTime());if(!tmp.mkdirs())throw new IOException("تعذر إنشاء مجلد مؤقت");
 try(InputStream in=c.getContentResolver().openInputStream(uri);ZipInputStream zip=new ZipInputStream(new BufferedInputStream(in))){
 ZipEntry e;while((e=zip.getNextEntry())!=null){if(e.isDirectory()){zip.closeEntry();continue;}if(++entries>500)throw new IOException("الحد الأقصى 500 ملف");
 String path=e.getName().replace('\\','/');String name=path.substring(path.lastIndexOf('/')+1);String lower=name.toLowerCase(Locale.ROOT);
 if(!(lower.endsWith(".png")||lower.endsWith(".jpg")||lower.endsWith(".jpeg")||lower.endsWith(".webp"))){r.skipped++;zip.closeEntry();continue;}
 String group=path.contains("/")?path.substring(0,path.indexOf('/')):"ملصقاتي";
 File f=new File(tmp,"entry"+entries);long size=0;
 try(FileOutputStream out=new FileOutputStream(f)){byte[] b=new byte[8192];int n;while((n=zip.read(b))!=-1){size+=n;total+=n;if(size>8*1024*1024||total>120L*1024*1024)throw new IOException("حجم ZIP كبير جدًا");out.write(b,0,n);}}
 boolean animated=lower.endsWith(".webp")&&animated(f);String key=group+(animated?"-a":"-s");String id=active.get(key);
 if(id==null||PackStore.count(c,id)>=30){id=PackStore.create(c,group+(animated?" متحركة":" ثابتة")+" "+(r.packs+1),animated);active.put(key,id);r.packs++;}
 try{PackStore.add(c,id,Uri.fromFile(f));r.imported++;}catch(Exception ex){r.skipped++;if(r.warning.isEmpty())r.warning=ex.getMessage();}finally{f.delete();zip.closeEntry();}
 }}finally{File[] fs=tmp.listFiles();if(fs!=null)for(File f:fs)f.delete();tmp.delete();}return r;
 }
 static boolean animated(File f){try(FileInputStream in=new FileInputStream(f)){byte[] b=new byte[(int)Math.min(f.length(),500*1024)];int n=in.read(b);if(n<16)return false;for(int i=12;i<n-3;i++)if(b[i]=='A'&&b[i+1]=='N'&&b[i+2]=='I'&&b[i+3]=='M')return true;}catch(Exception ignored){}return false;}
}