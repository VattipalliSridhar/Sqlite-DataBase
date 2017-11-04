package com.katamapps.sqlitedatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    Toolbar toolbar;
    public static int height,width;
    RelativeLayout title_layout,main_layout,sub_layout;
    ListView employeelistview;

    Uri mImageCaptureUri;
    String text;
    Bitmap bm;

    FloatingActionButton actionButton;


    public static EditText eid,ename,ephone,eaddress;
    public static Button ok,cancel,gallery;
    public static ImageView viewimg;
    public static Dialog dialog ;

    public static ListView listviews;
    public static MyDataBase mydatabase;
    public static ArrayList<String> EID_ArrayList = new ArrayList<String>();
    public static ArrayList<String> ENAME_ArrayList = new ArrayList<String>();
    public static ArrayList<String> EPHONE_NUMBER_ArrayList = new ArrayList<String>();
    public static ArrayList<String> EAddress_ArrayList = new ArrayList<String>();
    public static ArrayList<String> EPhoto_Arraylist = new ArrayList<String>();
    public static SQLiteListAdapter ListAdapter;
    public static SQLiteDatabase SQLITEDATABASE;
    public static Cursor cursor;
    public static String ephoto;
    public static Context context;

    SearchView searchView1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        context=MainActivity.this;

        mydatabase = new MyDataBase(this, "mydb");

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        searchView1=(SearchView)findViewById(R.id.searchView1);
        searchView1.setQueryHint("Search");


        searchView1.setOnQueryTextListener(new OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query)
            {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                return false;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        actionButton=(FloatingActionButton)findViewById(R.id.fab);
        actionButton.setOnClickListener(this);



        listviews = (ListView) findViewById(R.id.employeelistview);


        ShowSQLiteDBdata();
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fab:



                dialog= new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.employeeeditor);
                dialog.getWindow().setLayout(width - width / 8,height-height/4);

                eid=(EditText)dialog.findViewById(R.id.eid);
                eid.setText(null);

                ename=(EditText)dialog.findViewById(R.id.ename);
                ename.setText(null);

                ephone=(EditText)dialog.findViewById(R.id.ephone);
                ephone.setText(null);

                eaddress=(EditText)dialog.findViewById(R.id.eaddress);
                eaddress.setText(null);

                viewimg=(ImageView)dialog.findViewById(R.id.viewimg);
                viewimg.getLayoutParams().height=width/7;
                viewimg.getLayoutParams().width=width/7;


                gallery=(Button)dialog.findViewById(R.id.gallery);
                gallery.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        Intent galeryintent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galeryintent, 200);
                    }
                });


                ok=(Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String empid = eid.getText().toString();
                        String empname = ename.getText().toString();
                        String empcellno = ephone.getText().toString();
                        String empaddres=eaddress.getText().toString();
                        Calendar cc = Calendar.getInstance();
                        String times=cc.getTime().toString();
                        eid.setText(null);
                        ename.setText(null);
                        ephone.setText(null);
                        eaddress.setText(null);
                        String empphoto=ephoto;
                        if(empphoto!=null)
                        {

                        }
                        else
                        {
                            empphoto=BitMapToString(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                        }
                        if (empid.length() <= 0 || empname.length() <= 0 || empcellno.length() <= 0 || empaddres.length() <=0 || empphoto.length() <= 0)
                        {
                            Toast.makeText(getApplicationContext(), "do not insert empty field ", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            mydatabase.insertRecord(empid, empname, empcellno,empaddres,empphoto,times);
                        }

                        ShowSQLiteDBdata();
                        ephoto=null;
                        dialog.dismiss();
                    }
                });
                cancel=(Button)dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });


                dialog.show();

                break;

            default:
                break;
        }

    }

    public static void ShowSQLiteDBdata()
    {
        SQLITEDATABASE = mydatabase.getWritableDatabase();
        //cursor = SQLITEDATABASE.rawQuery("SELECT * FROM employeedetails", null);
        cursor=	mydatabase.fetchRecords();
        EID_ArrayList.clear();
        ENAME_ArrayList.clear();
        EPHONE_NUMBER_ArrayList.clear();
        EAddress_ArrayList.clear();
        EPhoto_Arraylist.clear();
        if (cursor.moveToFirst())
        {
            do
            {
                EID_ArrayList.add(cursor.getString(cursor.getColumnIndex(MyDataBase.eid)));
                ENAME_ArrayList.add(cursor.getString(cursor.getColumnIndex(MyDataBase.ename)));
                EPHONE_NUMBER_ArrayList.add(cursor.getString(cursor.getColumnIndex(MyDataBase.ephone)));
                EAddress_ArrayList.add(cursor.getString(cursor.getColumnIndex(MyDataBase.eaddress)));
                EPhoto_Arraylist.add(cursor.getString(cursor.getColumnIndex(MyDataBase.eimage)));
            } while (cursor.moveToNext());
        }
        ListAdapter = new SQLiteListAdapter(context, EID_ArrayList, ENAME_ArrayList, EPHONE_NUMBER_ArrayList,EAddress_ArrayList,EPhoto_Arraylist);
        listviews.setAdapter(ListAdapter);
        cursor.close();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(requestCode==200 && resultCode==RESULT_OK)
            {
                mImageCaptureUri = data.getData();
                text = "galery";
                doCrop();

            }
            else if (requestCode == 300 )
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        bm = extras.getParcelable("data");
                        if(bm!=null)
                        {
                            viewimg.setImageBitmap(bm);
                        }
                        ephoto=BitMapToString(bm);

                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    public void doCrop()
    {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        if (text.equals("gallery")) {
            // here it pass the uri to that intent
            cropIntent.setDataAndType(mImageCaptureUri, "image/*");
        } else {
            cropIntent.setDataAndType(mImageCaptureUri, "image/*");
        }
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("noFaceDetection", true);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 300);

    }




}

