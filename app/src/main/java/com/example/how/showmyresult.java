package com.example.how;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

public class showmyresult extends AppCompatActivity {

    ArrayList<String> arrayList=new ArrayList<String>();
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmyresult);
        recyclerView=findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String savedvideopath= getApplicationContext().getExternalCacheDir().getPath()+"/"+"savedvideo"+"/";
        File file=new File(savedvideopath);
           File[]files=file.listFiles();
           for(int i=0;i<files.length;i++)
           {
               arrayList.add(files[i].getName());
           }



        mAdapter=new myadapter(arrayList,this);
        recyclerView.setAdapter(mAdapter);


    }
    void  show(String e)
    {
        AlertDialog.Builder a =new AlertDialog.Builder(this);
        a.setMessage(e);
        a.setCancelable(true);
        a.show();

    }
}
