package com.kwl360.palestinestickers;
import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.view.*;import android.widget.*;import java.io.*;
public class MainActivity extends Activity {
 static final String ID="palestine_boy",AUTH="com.kwl360.palestinestickers.stickercontentprovider";
 @Override public void onCreate(Bundle b){super.onCreate(b);
 LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(28,40,28,25);box.setBackgroundColor(Color.rgb(248,250,252));
 TextView title=new TextView(this);title.setText("🇵🇸 ملصقات ابن فلسطين");title.setTextSize(26);title.setGravity(Gravity.CENTER);box.addView(title);
 TextView sub=new TextView(this);sub.setText("12 ملصقًا متحركًا");sub.setTextSize(17);sub.setGravity(Gravity.CENTER);sub.setPadding(0,12,0,28);box.addView(sub);
 GridLayout grid=new GridLayout(this);grid.setColumnCount(3);
 for(int i=1;i<=12;i++){ImageView v=new ImageView(this);GridLayout.LayoutParams p=new GridLayout.LayoutParams();p.width=getResources().getDisplayMetrics().widthPixels/3-28;p.height=p.width;p.setMargins(3,3,3,3);v.setLayoutParams(p);
 try{InputStream s=getAssets().open("stickers/"+ID+"/sticker_"+String.format(java.util.Locale.US,"%02d",i)+".webp");byte[] data=new byte[s.available()];s.read(data);s.close();android.graphics.ImageDecoder.Source src=android.graphics.ImageDecoder.createSource(java.nio.ByteBuffer.wrap(data));v.setImageDrawable(android.graphics.ImageDecoder.decodeDrawable(src));}catch(Exception e){}
 grid.addView(v);}
 ScrollView scroll=new ScrollView(this);scroll.addView(grid);box.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
 Button add=new Button(this);add.setText("إضافة الحزمة إلى واتساب");add.setTextSize(18);box.addView(add);add.setOnClickListener(v->addPack());
 TextView foot=new TextView(this);foot.setText("يلزم تثبيت واتساب.");foot.setGravity(Gravity.CENTER);box.addView(foot);setContentView(box);
 }
 void addPack(){Intent intent=new Intent("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
 intent.putExtra("sticker_pack_id",ID);intent.putExtra("sticker_pack_authority",AUTH);intent.putExtra("sticker_pack_name","ضحكات ابن فلسطين");
 intent.setPackage("com.whatsapp");
 try{startActivityForResult(intent,200);}catch(Exception e){new AlertDialog.Builder(this).setMessage("تعذر فتح واتساب.").setPositiveButton("حسنًا",null).show();}
 }
}