package com.example.how;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> Inputarray=new ArrayList<String>();
    String tosplit;
    String[] splitter;
    Button takeinput,playexo,save,showsavedvideo;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;

    HashMap<String,String> readyforvideo=new HashMap<>();
boolean i=false;
    String path;
    ArrayList<String>paths=new ArrayList<String>();

    ConcatenatingMediaSource concatenatedSource;
    DataSource.Factory datasourcefactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persmission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        persmission(Manifest.permission.READ_EXTERNAL_STORAGE);
        takeinput=findViewById(R.id.takeinput);
        playexo=findViewById(R.id.playexo);
        playerView=findViewById(R.id.player_view);
        save=findViewById(R.id.save);
        showsavedvideo=findViewById(R.id.showsavedvideo);
        showsavedvideo.setOnClickListener(this);


        takeinput.setOnClickListener(this);
        playexo.setOnClickListener(this);
        save.setOnClickListener(this);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
         datasourcefactory=new DefaultDataSourceFactory(this, Util.getUserAgent(this, "completeproject"));
          path=getApplicationContext().getExternalCacheDir().getPath()+"/"+"videotorun"+"/";
         File fileforstrorage=new File(path);
         if(!fileforstrorage.exists())
         {
             fileforstrorage.mkdirs();

         }
         copy(path,"v.mp4");
         copy(path,"b.mp4");



    }
    void persmission (String  permissi)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,permissi
        )
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissi)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{permissi},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    public void getspeechInput ()
    {
        Locale locale = new Locale("bn");

        Intent intent=new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH) ;
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,locale.getLanguage());
        if (intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,2);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 2: {
                if (resultCode == RESULT_OK && data!=null)
                {

                    // a=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Inputarray  =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tosplit=Inputarray.get(0);
                      splitter=tosplit.split(" ");
                    i=true;
                      //show(""+splitter[1]);
                    paths.clear();







                }



            }
        }
    }
   void readythehasmap()
    {
        readyforvideo.put("কে","v.mp4");
        readyforvideo.put("আমার","b.mp4");


    }
    void readythepath()

    {



        for (int k=0;k<splitter.length;k++) {

      //  show("before"+splitter[k]);
            if (readyforvideo.containsKey(splitter[k])) {


                paths.add(path + "/" + readyforvideo.get(splitter[k]) + "/");
               // show("after"+paths.get(k));

            }


        }


    }
    void checkforami()
    {
        show("splitter return"+splitter[0]);
        show("return 2"+splitter[1]);

    }


    void readymediasorce()
    {
       // String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"b.mp4.mp4"+"/";

      //  File pat=new File(path);


            MediaSource[] mediaSources;

            mediaSources = new MediaSource[paths.size()];


            for (int j = 0; j < paths.size(); j++) {

                MediaSource media = new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(paths.get(j)));

                mediaSources[j] = media;
            }
            concatenatedSource = new ConcatenatingMediaSource(mediaSources);




    }


  void  show(String e)
  {
      AlertDialog.Builder a =new AlertDialog.Builder(this);
      a.setMessage(e);
      a.setCancelable(true);
      a.show();

  }
    void exoplayer()
    {
        simpleExoPlayer.prepare(concatenatedSource);
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);
    }
    void merger ()  {
        int count=paths.size();
        Movie[] movie=new Movie[count];
        for(int i=0;i<count;i++)
        {
            File file =new File (paths.get(i));
            if(file.exists())
            {
                try {
                    movie[i]= MovieCreator.build(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LinkedList<Track> videotrack=new LinkedList<>();
        for(Movie m:movie) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("vide")) {
                    videotrack.add(t);

                }
            }
        }
        Movie result=new Movie();
        if(videotrack.size()>0)
        {

            try {
                result.addTrack(new AppendTrack(videotrack.toArray(new Track[videotrack.size()])));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        Container out=new DefaultMp4Builder().build(result);
        String pat= getApplicationContext().getExternalCacheDir().getPath()+"/"+"savedvideo"+"/";
        File fileforsavevideo=new File(pat);
        if(!fileforsavevideo.exists())
        {
            fileforsavevideo.mkdirs();

        }
        long timestamp=new Date().getTime();
        String b="sonamoni"+timestamp+".mp4";

        File storage=new File(pat,b);
        FileChannel filechannel =null;

        try {
            filechannel =new RandomAccessFile(storage,"rw").getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.writeContainer(filechannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            filechannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.takeinput)
        {

            getspeechInput ();




        }
        if(v.getId()==R.id.playexo)
        {
            if(i==true) {
                 show(""+i);
                readythehasmap();
                readythepath();
                if(!paths.isEmpty()) {

                   //show("i am here");
                    readymediasorce();
                    exoplayer();

                   // i = true;
                }
              else {
                    Toast.makeText(this,"these videos will be added in our database soon...",Toast.LENGTH_SHORT).show();
                }
            }
            {
                Toast.makeText(this,"please give speech input first",Toast.LENGTH_SHORT).show();

            }


           //simpleExoPlayer= simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
          //  readymediasorce();
          //  exoplayer();





        }
        if(v.getId()==R.id.save)
        {
            try {
                merger();
            }catch(Exception e)
            {
                show(""+e);
            }
        }
        if(v.getId()==R.id.showsavedvideo)
        {
            Intent i=new Intent(this,showmyresult.class);
            startActivity(i);

        }

    }
    void copy(String path,String fileforstorage) {
        InputStream in = null;
        OutputStream out = null;
        try {

            in = getResources().openRawResource(R.raw.b);

            File fi = new File(path, fileforstorage);
            out = new FileOutputStream(fi);
            byte[] a = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(a)) != -1) {
                    out.write(a, 0, read);
                }

            } catch (Exception e) {
                show("exception come from fucking  stream write from read " + e);

            }
        } catch (FileNotFoundException ex) {
            show("fucked by output stream" + ex);
            ex.printStackTrace();
        }

    }



}
