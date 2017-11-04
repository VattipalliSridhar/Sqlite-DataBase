package com.katamapps.sqlitedatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SQLiteListAdapter extends BaseAdapter {
    ArrayList<String> UserName;
    ArrayList<String> User_PhoneNumber;
    Context context;
    ArrayList<String> userID;
    ArrayList<String> User_Address;
    ArrayList<String> User_Image;
    
   String eidss;

    public class Holder
    {
        TextView textviewid;
        TextView textviewname;
        TextView textviewphone_number;
        TextView textViewaddress;
        Button optionbutton;
        ImageView empimgview;
    }

    public SQLiteListAdapter(Context context2, ArrayList<String> id, ArrayList<String> name, ArrayList<String> phone,
    								ArrayList<String> eAddress_ArrayList, ArrayList<String> ePhoto_Arraylist)
    {
        this.context = context2;
        this.userID = id;
        this.UserName = name;
        this.User_PhoneNumber = phone;
        this.User_Address=eAddress_ArrayList;
        this.User_Image=ePhoto_Arraylist;
    }

    public int getCount()
    {
        return this.userID.size();
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(final int position, View child, ViewGroup parent)
    {
        Holder holder;
        if (child == null)
        {
            child = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.listviewdatalayout, null);
            holder = new Holder();
            holder.textviewid = (TextView) child.findViewById(R.id.textViewID);
            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);
            holder.textviewphone_number = (TextView) child.findViewById(R.id.textViewPHONE_NUMBER);
            holder.textViewaddress = (TextView) child.findViewById(R.id.textViewaddress);
            holder.empimgview=(ImageView)child.findViewById(R.id.empimgview);
            holder.empimgview.getLayoutParams().height=MainActivity.width/7;
            holder.empimgview.getLayoutParams().width=MainActivity.width/7;
            holder.optionbutton=(Button)child.findViewById(R.id.optionbutton);
            holder.optionbutton.getLayoutParams().height=MainActivity.width/10;
            holder.optionbutton.getLayoutParams().width=MainActivity.width/10;
            holder.optionbutton.setOnClickListener(new OnClickListener()
            {
				
				@Override
				public void onClick(View v)
				{
					final CharSequence[] options = { "Update", "Delete","Cancel" };
					AlertDialog.Builder builder = new AlertDialog.Builder(context);

			        builder.setTitle("Modify....!");
			        builder.setItems(options, new DialogInterface.OnClickListener()
			        {

			            @Override

			            public void onClick(final DialogInterface dialog, int item)
			            {

			                if (options[item].equals("Update"))

			                {
			                	MainActivity.dialog= new Dialog(context);
			                	MainActivity.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			                	MainActivity.dialog.setContentView(R.layout.employeeeditor);
			                	MainActivity.dialog.getWindow().setLayout(MainActivity.width - MainActivity.width/ 8,MainActivity.height-MainActivity.height/4);
			                	
			                	MainActivity.eid=(EditText)MainActivity.dialog.findViewById(R.id.eid);
			                	MainActivity.eid.setText((CharSequence)userID.get(position));
			        			
			                	MainActivity.ename=(EditText)MainActivity.dialog.findViewById(R.id.ename);
			                	MainActivity.ename.setText((CharSequence)UserName.get(position));
			        			
			                	MainActivity.ephone=(EditText)MainActivity.dialog.findViewById(R.id.ephone);
			                	MainActivity.ephone.setText((CharSequence)User_PhoneNumber.get(position));
			        			
			                	MainActivity.eaddress=(EditText)MainActivity.dialog.findViewById(R.id.eaddress);
			                	MainActivity.eaddress.setText((CharSequence)User_Address.get(position));
			        			
			        			MainActivity.viewimg=(ImageView)MainActivity.dialog.findViewById(R.id.viewimg);
			        			MainActivity.viewimg.getLayoutParams().height=MainActivity.width/7;
			        			MainActivity.viewimg.getLayoutParams().width=MainActivity.width/7;
			        			MainActivity.viewimg.setImageBitmap(StringToBitMap(User_Image.get(position)));
			        			
			        			MainActivity.gallery=(Button)MainActivity.dialog.findViewById(R.id.gallery);
			        			MainActivity.gallery.setOnClickListener(new OnClickListener()
			        			{
			        				
			        				@Override
			        				public void onClick(View v)
			        				{
			        					Intent galeryintent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			        	        		((Activity) context).startActivityForResult(galeryintent, 200);
			        				}

									
			        			});
			                	
			                	MainActivity.ok=(Button)MainActivity.dialog.findViewById(R.id.ok);
			                	MainActivity.ok.setOnClickListener(new OnClickListener()
			        			{
			        				
			        				@Override
			        				public void onClick(View v)
			        				{
			        					String empid = MainActivity.eid.getText().toString();
			        		            String empname =MainActivity.ename.getText().toString();
			        		            String empcellno = MainActivity.ephone.getText().toString();
			        		            String empaddres=MainActivity.eaddress.getText().toString();
			        		            String empphoto=MainActivity.ephoto;
			        		            String empphotos=User_Image.get(position);
			        		            if(empphoto!=null)
			        		            {
			        		            	
			        		            }
			        		            else if(empphotos!=null)
			        		            {
			        		            	empphoto=User_Image.get(position);
			        		            }
			        		            else
			        		            {
			        		            	empphoto=BitMapToString(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
			        		            	//empphoto=MainActivity.ephoto;
			        		            }
			        		            if (empid.length() <= 0 || empname.length() <= 0 || empcellno.length() <= 0 || empaddres.length() <=0 || empphoto.length() <=0 )
			        		            {
			        		                Toast.makeText(context, "do not insert empty field ", 0).show();
			        		            }
			        		            else
			        		            {
			        		            	 MainActivity.mydatabase.updateRecord(empid, empname, empcellno,empaddres,empphoto);
			        		            }
			        		            
			        		           
			        		            MainActivity.ShowSQLiteDBdata();
			        		            MainActivity.ephoto=null;
			        					
			        					MainActivity.dialog.dismiss();
			        				}

									

									
			        			});
			                	MainActivity.cancel=(Button)MainActivity.dialog.findViewById(R.id.cancel);
			                	MainActivity.cancel.setOnClickListener(new OnClickListener()
			        			{
			        				
			        				@Override
			        				public void onClick(View v)
			        				{
			        					
			        					MainActivity.dialog.dismiss();
			        				}
			        			});
			                	
			                	
			                	MainActivity.dialog.show();
			                	dialog.dismiss();
			                }

			                else if (options[item].equals("Delete"))
			                {
			                	eidss = userID.get(position);
			                	
			                	int id = Integer.parseInt((String)userID.get(position));
			                	AlertDialog.Builder builder = new AlertDialog.Builder(context);
			                	builder.setTitle("Confirm......!");
			                	builder.setMessage("Do you want delete employee id: "+id+" record...?");
			                	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			            		{

			            			@Override
			            			public void onClick(DialogInterface dialog, int which)
			            			{
			            				 if (eidss.length() > 0)
				        		            {
				        		            	MainActivity.mydatabase.deleteRecord(eidss);
				        		            }
				        		            else
				        		            {
				        		                Toast.makeText(context, "do not delete empty field ", 0).show();
				        		            }
				        		            MainActivity.ShowSQLiteDBdata();
			            				dialog.dismiss();
			            			}
			            		});
			            		builder.setNegativeButton("No",
			            				new DialogInterface.OnClickListener()
			            		{

			            					@Override
			            					public void onClick(DialogInterface dialog, int which)
			            					{
			            						dialog.dismiss();

			            					}
			            		});
			            		builder.show();
			            	
	        		           
			                	dialog.dismiss();
			                }
			                else if (options[item].equals("Cancel"))
			                {
			                	dialog.dismiss();
			                }
			            }

			        });

			        builder.show();
					
				}
			});
            child.setTag(holder);
        }
        else
        {
            holder = (Holder) child.getTag();
        }
        holder.textviewid.setText((CharSequence) this.userID.get(position));
        holder.textviewname.setText((CharSequence) this.UserName.get(position));
        holder.textviewphone_number.setText((CharSequence) this.User_PhoneNumber.get(position));
        holder.textViewaddress.setText((CharSequence) this.User_Address.get(position));
        holder.empimgview.setImageBitmap(StringToBitMap(this.User_Image.get(position)));
        
        
        return child;
    }

	public Bitmap StringToBitMap(String encodedString)
	{
		try
		   {
		      byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
		      Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		      return bitmap;
		   }
		   catch(Exception e)
		   {
		      e.getMessage();
		      return null;
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
	
    
	
	
	
	
	
	
	
	
	
}
