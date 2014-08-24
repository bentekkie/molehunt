package com.dragonfruit.codinghouse.molehunt;

import com.dragonfruit.codinghouse.molehunt.GameHelper.GameHelperListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.R.drawable;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.*;

public class MainActivity
extends Activity
implements View.OnClickListener {


	public RelativeLayout layout;
	private AdView adView;
    FileInputStream inputStream;
    ImageView preview;
	AlertDialog dialog;
	Boolean Signedin;
	GameHelper mHelper;
    FontFitButton changeImage;
    FontFitButton resetImage;
	Timer T=new Timer();
	SoundPool soundPool0;
	SoundPool soundPool1;
	SoundPool soundPool2;
	SoundPool soundPool3;
	SoundPool soundPoolback;
	int sound0;
	int sound1;
	int sound2;
	int sound3;
	int soundback;
	int streamid;
	Boolean isPaused;
	int progress;
	int ticks;
	String startButtonText;
	String start2ButtonText;
	String resetButtonText;
	String timeTitleText;
	String redButtonTag;
	String startButtonTag;
	String start2ButtonTag;
	String restartButtonTag;
	String shareButtonTag;
	String clearButtonText;
	String leaderButtonText;
	String clearButtonTag;
	String leaderButtonTag;
    String changeImageTag;
    String resetImageTag;
	Bitmap bm;
	ShapeDrawable sd;
	float stext;
	float ltext;
	float eltext;
	int twidth;
	int tlength;
	int wincriteria;
    Button start;
    Button start2;
    FrameLayout startButtonsLayout;
	DisplayMetrics metrics;
	LayoutParams startButtonLayout;
	LayoutParams start2ButtonLayout;
	LayoutParams startButtonWrapperLayout;
    LayoutParams resetButtonLayout;
	LayoutParams timeTextLayout;
    LayoutParams endTitleLayout;
    LayoutParams shareButtonLayout;
    LayoutParams clearScoreLayout;
    LayoutParams leaderButtonLayout;
	FrameLayout frame;
    ImageView backround;
    ImageView backroundend;
	FrameLayout end;
	static FrameLayout shareFrame;
    TextView timed;
    FontFitButton reset;
    Button share;
    TextView stitle;
    Typeface type;
    Button leaderButton;
    static File imagePath;
    static File molePath;
    Bitmap bitmap;
    int[] generalButtonDimention;
    int[] generalTextDimention;
    int[] shareButtonDimention;
    int[] clearScoreButtonDimention;
    String hightscoreString;
	String hightscoreStringe;
	String hightscoreStrings;
	ImageButton button;
    String time;
	String timee;
	String times;
	SharedPreferences prefs;
    Button clearScore;
    TextView shareScore;
    TextView shareTitle;
    TextView shareText;
    TextView stopWatch;
    TextView moleCount;
    LayoutParams shareScoreLayout;
    LayoutParams shareTitleLayout;
    LayoutParams shareTextLayout;
    ImageView shareName;
    int soundfile;
	BitmapDrawable bitmapDrawable;
    Boolean customImage;
	int gameMode;
    final Handler   myHandler   = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
        	time = Integer.toString(ticks);
    		if(time.length() <= 3){
    			times = "0";
    			timee = time;
    		}else{
    			timee = time.substring((time.length()-3));
    			times = time.substring(0, (time.length()-3));
    		}
    		stopWatch.setText(times+"s  "+timee+"ms");
        }
    };
    final Handler   stop2Handler   = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
        	setupEndScreen();
        	if(Signedin == false){
    			leaderButton.setText("Sign in");
    		}else{
    			leaderButton.setText(leaderButtonText);
    		}
        	int highscore = prefs.getInt("many", -1);
        	if(highscore<0){
        		timed.setText(progress+" Moles hit!");
        		Editor editor = prefs.edit();
    			editor.putInt("many", progress);
    			editor.commit();
        	}else if(progress <= highscore){
        		timed.setText(progress+" Moles hit!\n"+"Highscore is "+highscore+" Moles");
        	}else if(progress > highscore){
        		timed.setText(progress+" Moles hit!\n"+(progress-highscore)+"more than Highscore!");
        		timed.setText(progress+" Moles hit!");
        		Editor editor = prefs.edit();
    			editor.putInt("many", progress);
    			editor.commit();
        	}
        	if(Signedin == true){
    			Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_many), progress);
    			Games.Achievements.unlock(mHelper.getApiClient(), getString(R.string.achievement_beginner));
    			Games.Achievements.increment(mHelper.getApiClient(), getString(R.string.achievement_marathon), 1);
    			Games.Achievements.increment(mHelper.getApiClient(), getString(R.string.achievement_addict), 1);
        	}
        	setEnd();
        	
        }
    };
    RelativeLayout.LayoutParams lp;
    RelativeLayout.LayoutParams stopWatchlp;
    RelativeLayout.LayoutParams moleCountlp;
    
    
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupVars();
    Signedin = false;
    setupGameServices();
    setupStartScreen();
    setupDialogBox();
	
    super.setContentView(frame);
} // ()
protected void onStart() {
    super.onStart();
    mHelper.onStart(this);
	}
protected void onStop() {
	super.onStop();
    mHelper.onStop();
}


	@Override
protected void onActivityResult(int request, int response, Intent data) {
	super.onActivityResult(request, response, data);
    mHelper.onActivityResult(request, response, data);


        if (request == 1) {
            final Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                FileOutputStream fos;
                try {
                    molePath.createNewFile();
                    Bitmap bitmap =photo;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                    fos = new FileOutputStream(molePath);
                    fos.write(bitmapdata);
                } catch (FileNotFoundException e) {
                    Log.e("GREC", e.getMessage(), e);
                } catch (IOException e) {
                    Log.e("GREC", e.getMessage(), e);
                }

                Bitmap temp;
                File file = new File(getApplicationContext().getFilesDir(), "mole");
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                temp =BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/tamcustomimage.png",options);
                preview.setImageBitmap(photo);
                Editor editor = prefs.edit();
                editor.putBoolean("customImage", true);
                editor.commit();
                customImage = true;
            }
        }
}
public void setupVars(){
    preview = new ImageView(this);
	adView = new AdView(this);
	sd = new ShapeDrawable();
	sd.setShape(new RoundRectShape(new float[]{2,2,2,2,2,2,2,2}, null, null));
	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.raw.brown016);
	bitmapDrawable = new BitmapDrawable(getResources(),bmp);
	bitmapDrawable.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT);
    changeImage = new FontFitButton(this);
    resetImage = new FontFitButton(this);
	button = new ImageButton (this);
	mHelper = new GameHelper(this, GameHelper.CLIENT_ALL);
	shareName = new ImageView(this);
	type = Typeface.createFromAsset(getAssets(),"fonts/PlayfairDisplay-Black.ttf"); 
	layout = new RelativeLayout (this);
	T=new Timer();
	lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	soundPool0 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	sound0 = soundPool0.load(this, R.raw.hitone, 1);
	soundPool1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	sound1 = soundPool1.load(this, R.raw.hittwo, 1);
	soundPool2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	sound2 = soundPool2.load(this, R.raw.hitthree, 1);
	soundPool3 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	sound3 = soundPool3.load(this, R.raw.hitfour, 1);
	soundPoolback = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	soundback = soundPoolback.load(this, R.raw.backmusic, 1);
	isPaused = false;
	startButtonText = "FAST";
	start2ButtonText = "MANY";
	resetButtonText = "Restart";
	timeTitleText = "Time";
	clearButtonText = "Clear";
	leaderButtonText = "Leader-\nBoard";
	redButtonTag = "1";
	startButtonTag = "2";
	restartButtonTag = "3";
	shareButtonTag = "4";
	clearButtonTag = "5";
	leaderButtonTag = "6";
	start2ButtonTag = "7";
    resetImageTag = "8";
    changeImageTag = "9";
	stext = 30.0f;
	ltext = 60.0f;
	eltext = 100.0f;
	twidth = 4;
	tlength = 5;
	wincriteria = 20;
    start = new Button(this);
    start2 = new Button(this);
	metrics = this.getResources().getDisplayMetrics();
	leaderButton = new Button(this);
    generalButtonDimention = new int[]{(metrics.widthPixels/twidth),((metrics.heightPixels)/(tlength+1))};
    generalTextDimention = new int[]{(metrics.widthPixels),((metrics.heightPixels)/(tlength+1))};
    shareButtonDimention = new int[]{(metrics.widthPixels/10),((metrics.widthPixels)/10)};
    clearScoreButtonDimention = new int[]{(metrics.widthPixels/twidth),(metrics.heightPixels)/(2*(tlength+1))};
	startButtonLayout = new LayoutParams(metrics.widthPixels,generalButtonDimention[1], Gravity.TOP);
	start2ButtonLayout = new LayoutParams(metrics.widthPixels,generalButtonDimention[1], Gravity.BOTTOM);
	startButtonWrapperLayout = new LayoutParams(metrics.widthPixels,2*generalButtonDimention[1], Gravity.CENTER);
	lp = new RelativeLayout.LayoutParams(generalButtonDimention[0],generalButtonDimention[1]);
	stopWatchlp = new RelativeLayout.LayoutParams(generalTextDimention[0],(metrics.heightPixels/20));
	moleCountlp = new RelativeLayout.LayoutParams(generalTextDimention[0],(metrics.heightPixels/20));
    resetButtonLayout = new LayoutParams(generalButtonDimention[0],generalButtonDimention[1], Gravity.CENTER | Gravity.BOTTOM);
	timeTextLayout = new LayoutParams(generalTextDimention[0],generalTextDimention[1], Gravity.CENTER);
	endTitleLayout = new LayoutParams(generalTextDimention[0],generalTextDimention[1], Gravity.CENTER | Gravity.TOP);
    shareButtonLayout = new LayoutParams(shareButtonDimention[0],shareButtonDimention[1], Gravity.RIGHT | Gravity.TOP);
    clearScoreLayout = new LayoutParams(clearScoreButtonDimention[0],clearScoreButtonDimention[1], Gravity.BOTTOM |Gravity.LEFT);
    shareScoreLayout = new LayoutParams(504,168, Gravity.CENTER);
    shareTitleLayout =  new LayoutParams(504,168, Gravity.TOP);
    shareTextLayout =  new LayoutParams(504,100, Gravity.BOTTOM);
    leaderButtonLayout = new LayoutParams(clearScoreButtonDimention[0],clearScoreButtonDimention[1], Gravity.BOTTOM |Gravity.RIGHT);
    frame = new FrameLayout(this);
    startButtonsLayout = new FrameLayout(this);
    backround = new ImageView(this);
    backroundend = new ImageView(this);
    shareFrame = new FrameLayout(this);
	end = new FrameLayout(this);
    timed = new TextView(this);
    reset = new FontFitButton(this);
    stitle = new TextView(this);
    share = new Button(this);
    clearScore = new Button(this);
    shareScore = new TextView(this);
    shareTitle = new TextView(this);
    shareText = new TextView(this);
    moleCount = new TextView(this);
    imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
    molePath = new File(Environment.getExternalStorageDirectory() + "/tamcustomimage.png");
    prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    stopWatch = new TextView(this);
}
	
public void setupStartScreen(){

//    adView.setAdSize(AdSize.BANNER);
//    adView.setAdUnitId("ca-app-pub-1669366584619509/6317058674");
//    AdRequest adRequest = new AdRequest.Builder()
  //  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
 //   .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
//    .build();
    LayoutParams ad = new LayoutParams(metrics.widthPixels,100,Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//    adView.loadAd(adRequest);
//    adView.setId(5);
    backround.setBackground(bitmapDrawable);
    frame.addView(backround);
//    frame.addView(adView, ad);
    if(molePath.exists()){
        Bitmap temp;

            File file = new File(getApplicationContext().getFilesDir(), "mole");
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            temp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/tamcustomimage.png",options);
            preview.setImageBitmap(temp);

    }else{

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bf = BitmapFactory.decodeResource(getResources(), R.raw.mole2, options);
        preview.setImageBitmap(bf);
    }
    LayoutParams previewLayout = new FrameLayout.LayoutParams(generalButtonDimention[0],generalButtonDimention[1], Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    LayoutParams changeImageLayout = new FrameLayout.LayoutParams(generalButtonDimention[0],generalButtonDimention[1], Gravity.BOTTOM | Gravity.LEFT);
    LayoutParams resetImageLayout = new FrameLayout.LayoutParams(generalButtonDimention[0],generalButtonDimention[1], Gravity.BOTTOM | Gravity.RIGHT);
    changeImage.setText("Change\nImage");
    changeImage.setBackgroundResource(R.drawable.customimage_button);
    changeImage.setGravity(Gravity.CENTER);
    changeImage.setTextColor(Color.WHITE);
    changeImage.setTypeface(type);
    changeImage.setTag(changeImageTag);
    changeImage.setOnClickListener(this);
    resetImage.setText("Reset\nImage");
    resetImage.setBackgroundResource(R.drawable.customimage_button);
    resetImage.setGravity(Gravity.CENTER);
    resetImage.setTextColor(Color.WHITE);
    resetImage.setTypeface(type);
    resetImage.setTag(resetImageTag);
    resetImage.setOnClickListener(this);
    start.setTag(startButtonTag);
    start.setTypeface(type);
    start.setText(startButtonText);
    start.setTextColor(Color.WHITE);
    start.setBackgroundColor(Color.BLUE);
    start.setBackgroundResource(R.drawable.start_button);
    start.setTextSize(TypedValue.COMPLEX_UNIT_PX, generalButtonDimention[1] / 3);
    start.setOnClickListener(this);
    start.setGravity(Gravity.CENTER);
    start2.setTag(start2ButtonTag);
    start2.setTypeface(type);
    start2.setText(start2ButtonText);
    start2.setTextColor(Color.WHITE);
    start2.setBackgroundColor(Color.BLUE);
    start2.setBackgroundResource(R.drawable.start_button);
    start2.setTextSize(TypedValue.COMPLEX_UNIT_PX, generalButtonDimention[1] / 3);
    start2.setOnClickListener(this);
    start2.setGravity(Gravity.CENTER);
    startButtonsLayout.addView(start, startButtonLayout);
    startButtonsLayout.addView(start2, start2ButtonLayout);
    frame.addView(preview, previewLayout);
    frame.addView(changeImage, changeImageLayout);
    frame.addView(resetImage, resetImageLayout);
    frame.addView(startButtonsLayout, startButtonWrapperLayout);
}
public void destroyGameBoard(){
	soundPoolback.stop(streamid);
	layout.removeAllViews();
}

public void setupGameBoard(){

	
    layout.setPadding(1,1,1,1);
    layout.setBackground(bitmapDrawable);
	stopWatchlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	moleCountlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.ALIGN_PARENT_TOP) ;
    stopWatch.setTypeface(type);
    stopWatch.setText("0s000ms");
    stopWatch.setTextColor(Color.WHITE);
    stopWatch.setGravity(Gravity.CENTER);
    stopWatch.setTextSize(TypedValue.COMPLEX_UNIT_PX,((metrics.heightPixels/25)-2));
    stopWatch.setTypeface(type);
    stopWatch.setText("0s");
    stopWatch.setTextColor(Color.WHITE);
    stopWatch.setGravity(Gravity.CENTER);
    stopWatch.setTextSize(TypedValue.COMPLEX_UNIT_PX,((metrics.heightPixels/25)-2));
    lp.setMargins(0, 0, 0, 0);
    int x = randomInt(metrics.widthPixels-generalButtonDimention[0]);
	while(x > lp.leftMargin && x < (lp.leftMargin-generalButtonDimention[0])){
		x = randomInt(metrics.widthPixels-generalButtonDimention[0]);
	}
	int y = (randomInt(metrics.heightPixels-generalButtonDimention[1]-(metrics.heightPixels/20)))+(metrics.heightPixels/20);
	while(y > lp.topMargin && y < (lp.topMargin-generalButtonDimention[1])){
		y = randomInt(metrics.heightPixels-generalButtonDimention[1]);
	}
	lp.setMargins(x, y, (metrics.widthPixels-x-generalButtonDimention[0]), (metrics.heightPixels-y-generalButtonDimention[1]));
	layout.requestLayout();
	LayoutTransition lt = new LayoutTransition();
	lt.setDuration(LayoutTransition.CHANGING, 100);
	layout.setLayoutTransition(lt);
	layout.addView(stopWatch,stopWatchlp);
    layout.addView(generateButton(), lp);
    
}

public ImageButton generateButton(){
	
	button = new ImageButton (this);
	final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 8;
	Bitmap bf = BitmapFactory.decodeResource(getResources(), R.raw.mole2, options);

	bm = Bitmap.createScaledBitmap( bf , lp.height-10, lp.width, true);
	drawTextToBitmap(this,bm,"20");
	button.setImageBitmap(drawTextToBitmap(this,bm,"20"));
	button.setBackgroundColor(Color.TRANSPARENT); 
	button.setTag(redButtonTag);
	button.setOnClickListener(this);
    return button;
}

public int randomInt(int size){
	Random r = new Random();
	int b = r.nextInt(size);
	return b;
}
public void destroyEndScreen(){
	end.removeAllViews();
}
public void setupEndScreen(){

    end.setBackground(bitmapDrawable);
    isPaused = true;
    timed.setTypeface(type);
    timed.setText(0+"ms");
    timed.setTextColor(Color.WHITE);
    timed.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalTextDimention[1]/3);
    timed.setGravity(Gravity.CENTER);
    timed.setShadowLayer(6,2,2,Color.BLACK);
    reset.setTypeface(type);
    reset.setText(resetButtonText);
    reset.setTextColor(Color.WHITE);
    reset.setTag(restartButtonTag);
    reset.setOnClickListener(this);
    reset.setBackgroundColor(Color.BLUE);
    reset.setBackgroundResource(R.drawable.reset_button);
   // reset.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalButtonDimention[1]/5);
    stitle.setTypeface(type);
    stitle.setText(timeTitleText);
    stitle.setTextColor(Color.WHITE);
    stitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalTextDimention[1]-10);
    stitle.setGravity(Gravity.CENTER);
    stitle.setShadowLayer(6,2,2,Color.BLACK);
    share.setGravity(Gravity.CENTER);
    share.setBackgroundColor(Color.GREEN);
    share.setTag(shareButtonTag);
    share.setOnClickListener(this);
    share.setBackgroundResource(drawable.ic_menu_share);
    clearScore.setGravity(Gravity.CENTER);
    clearScore.setText(clearButtonText);
    clearScore.setTypeface(type);
    clearScore.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalButtonDimention[1]/5);
    clearScore.setBackgroundColor(Color.BLACK);
    clearScore.setTextColor(Color.WHITE);
    clearScore.setTag(clearButtonTag);
    clearScore.setOnClickListener(this);
    leaderButton.setGravity(Gravity.CENTER);
    leaderButton.setTypeface(type);
    leaderButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalButtonDimention[1]/7);
    leaderButton.setBackgroundColor(Color.RED);
    leaderButton.setText(leaderButtonText);
    leaderButton.setTextColor(Color.WHITE);
    leaderButton.setOnClickListener(this);
    leaderButton.setTag(leaderButtonTag);
    end.addView(leaderButton,leaderButtonLayout);
    end.addView(clearScore,clearScoreLayout);
    end.addView(share,shareButtonLayout);
    end.addView(timed, timeTextLayout);
    end.addView(reset, resetButtonLayout);
    end.addView(stitle, endTitleLayout);
    isPaused = true;
    
}

public void setupTimer(){
	T=new Timer();
    T.scheduleAtFixedRate(new TimerTask() {         
        @Override
        public void run() {
        	if(gameMode == 0){
                ticks++;
                }else{
                	if(ticks== 0){

                		stop2Handler.sendEmptyMessage(0);

                		
                	}
                	ticks--;
                }
        	
                myHandler.sendEmptyMessage(0);
        }   
    }, 1, 1);
}
public void setEnd(){
	super.setContentView(end);
	destroyGameBoard();
}
public void onClick(View view) {
	if(view == button){
		if(progress == 0){
			isPaused = false;
			setupTimer();
			streamid = soundPoolback.play(soundback, 1.0f, 1.0f, 0, -1, 1.0f);
		}
	int lastsound = soundfile;
	soundfile = randomInt(4);
	while(soundfile == lastsound){
		soundfile = randomInt(4);
	}
	switch(soundfile){
	case 0:
		soundPool0.play(sound0, 1.0f, 1.0f, 0, 0, 1.0f);
		break;
	case 1:
		soundPool1.play(sound1, 1.0f, 1.0f, 0, 0, 1.0f);
		break;
	case 2:
		soundPool2.play(sound2, 1.0f, 1.0f, 0, 0, 1.0f);
		break;
	case 3:
		soundPool3.play(sound3, 1.0f, 1.0f, 0, 0, 1.0f);
		break;
	}
	//	int tone = wincriteria-progress;
	
	//ToneGenerator td = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
	//td.startTone(tone, 100);
	progress++;
	moleCount.setText(progress+"moles");
	if(gameMode ==0){
	if(progress == wincriteria){
		setupEndScreen();
		int tickstemp = ticks;
		int highscore = prefs.getInt("key", 0);
		time = Integer.toString(tickstemp);
		if(Signedin == false){
			leaderButton.setText("Sign in");
		}else{
			leaderButton.setText(leaderButtonText);
		}
		if(time.length() <= 3){
			times = "0";
			timee = time;
		}else{
			timee = time.substring((time.length()-3));
			times = time.substring(0, (time.length()-3));
		}
		if((highscore-tickstemp) <= 0){
			if(highscore == 0){
				timed.setText(times+getString(R.string.second)+" "+timee+getString(R.string.millisecond),TextView.BufferType.SPANNABLE);
			}else{

				hightscoreString = Integer.toString(highscore);
				if(hightscoreString.length() <= 3){
					hightscoreStrings = "0";
					hightscoreStringe = hightscoreString;
				}else{
					hightscoreStringe = hightscoreString.substring((hightscoreString.length()-3));
					hightscoreStrings = hightscoreString.substring(0, (hightscoreString.length()-3));
				}
				timed.setText(times+getString(R.string.second)+" "+timee+getString(R.string.millisecond)+"\nHighscore: "+hightscoreStrings+getString(R.string.second)+" "+hightscoreStringe+getString(R.string.millisecond),TextView.BufferType.SPANNABLE);
			
			}
		}else{
			String fasters;
			String fastere;
			String faster = Integer.toString((highscore-tickstemp));
			if(faster.length() <= 3){
				fasters = "0";
				fastere = faster;
			}else{
				fastere = faster.substring((faster.length()-3));
				fasters = faster.substring(0, (faster.length()-3));
			}
			timed.setText(times+getString(R.string.second)+" "+timee+getString(R.string.millisecond)+"\n"+fasters+getString(R.string.second)+" "+fastere+getString(R.string.millisecond)+" faster!",TextView.BufferType.SPANNABLE);
		}
		if(tickstemp <= prefs.getInt("key", 0)){
			Editor editor = prefs.edit();
			editor.putInt("key", tickstemp);
			editor.commit();
			if(Signedin == true){
			Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_fast), tickstemp);
			}
		}
		if(prefs.getInt("key", 0) == 0){
			Editor editor = prefs.edit();
			editor.putInt("key", tickstemp);
			editor.commit();
			if(Signedin == true){
			Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_fast), tickstemp);
			}
		}
		if(Signedin == true){
			Games.Achievements.unlock(mHelper.getApiClient(), getString(R.string.achievement_beginner));
			Games.Achievements.increment(mHelper.getApiClient(), getString(R.string.achievement_marathon), 1);
			Games.Achievements.increment(mHelper.getApiClient(), getString(R.string.achievement_addict), 1);
			if(tickstemp < 10000){
				Games.Achievements.unlock(mHelper.getApiClient(), getString(R.string.achievement_racer));
			}
		}
		setEnd();
	}
	}
	int x = randomInt(metrics.widthPixels-generalButtonDimention[0]);
	while(x > lp.leftMargin && x < (lp.leftMargin-generalButtonDimention[0])){
		x = randomInt(metrics.widthPixels-generalButtonDimention[0]);
	}
	int y = (randomInt(metrics.heightPixels-generalButtonDimention[1]-(metrics.heightPixels/20)))+(metrics.heightPixels/20);
	while(y > lp.topMargin && y < (lp.topMargin-generalButtonDimention[1])){
		y = randomInt(metrics.heightPixels-generalButtonDimention[1]);
	}
	lp.setMargins(x, y, (metrics.widthPixels-x-generalButtonDimention[0]), (metrics.heightPixels-y-generalButtonDimention[1]));
	layout.requestLayout();
	if(gameMode == 0){
	if(wincriteria-progress == 0){
		button.setImageBitmap(drawTextToBitmap(this,bm,Integer.toString((wincriteria))));
	}else{
		button.setImageBitmap(drawTextToBitmap(this,bm,Integer.toString((wincriteria-progress))));
	}
	}else{
		button.setImageBitmap(drawTextToBitmap(this,bm,Integer.toString(progress)));
	}
	//setToGrey(view);
	}else if(view.getTag() ==startButtonTag){

	    setupGameBoard();
		gameMode = 0;
		ticks = 0;
		progress = 0; 


		stitle.setText("FAST");
	    super.setContentView(layout);
                                                                                                                                
	    
	}else if(view.getTag() ==start2ButtonTag){

	    setupGameBoard();
		gameMode = 1;
		ticks = 20000;
		progress = 0;
		stitle.setText("MANY");
		button.setImageBitmap(drawTextToBitmap(this,bm,Integer.toString(progress)));
	    super.setContentView(layout);
	    
                                                                                                                                
	    
	}else if(view.getTag() == restartButtonTag){
		T.cancel();
		gameMode = 3;
		ticks = 0;
		progress = 0;
		myHandler.sendEmptyMessage(0);
	    super.setContentView(frame);	
	    destroyEndScreen();
	}else if(view.getTag() == shareButtonTag){
		setupShare();
		while(generateShare(timed.getText()) == false){	
		};
		destroyShare();
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
	    sharingIntent.setType("image/jpeg");
	    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My Score is "+times+"."+timee+" seconds!");
	    sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE, "Share your score!");
	    sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(imagePath));
	    startActivity(Intent.createChooser(sharingIntent, "Share via"));
		if(Signedin == true){
			Games.Achievements.unlock(mHelper.getApiClient(), getString(R.string.achievement_socialite));
		}
		end.setBackground(bitmapDrawable);
	}else if(view.getTag() == clearButtonTag){
		dialog.show();
	}else if(view.getTag() == leaderButtonTag){
		if(Signedin == true){
			if(gameMode == 0){
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_fast)), 5);
			}else if(gameMode ==1){
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_many)), 5);
			}
		}else{
		mHelper.beginUserInitiatedSignIn();
		}
	}else if(view.getTag() == resetImageTag){

        Editor editor = prefs.edit();
        editor.putBoolean("customImage", false);
        editor.commit();
        getApplicationContext().deleteFile("mole");
        customImage = false;
    }else if(view.getTag() == changeImageTag){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);



    }
}
public void destroyShare(){
	shareFrame.removeAllViews();
	shareFrame.destroyDrawingCache();
}
public void setupShare(){
	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.raw.brown016);
	BitmapDrawable bd = new BitmapDrawable(getResources(),bmp);
	bd.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT);
	shareTitle.setText(stitle.getText());
	shareTitle.setTypeface(type);
	shareTitle.setTextColor(Color.WHITE);
	shareTitle.setGravity(Gravity.CENTER);
	shareTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,120);
	shareTitle.setDrawingCacheEnabled(true);
	shareTitle.layout(0, 0, 504, 128); 
	shareScore.setText("No Score Recorded");
	shareScore.setTypeface(type);
	shareScore.setTextColor(Color.WHITE);
	shareScore.setGravity(Gravity.CENTER);
	shareScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
	shareScore.setDrawingCacheEnabled(true);
	shareScore.layout(0, 222, 504, 292);
	shareFrame.setBackground(bd);
	shareFrame.layout(0, 0, 504, 504);
	shareName.setImageDrawable(getResources().getDrawable(R.raw.name));
	shareName.layout(0, 404, 504, 504);

	shareText.setDrawingCacheEnabled(true);
	shareFrame.addView(shareName,shareTextLayout);
	shareFrame.addView(shareTitle,shareTitleLayout);
	shareFrame.addView(shareScore,shareScoreLayout);
	
}
public Boolean generateShare(CharSequence score){
	shareScore.setText(score);
	shareTitle.setText(stitle.getText());
	shareTitle.buildDrawingCache();
	shareScore.buildDrawingCache();
    shareFrame.setDrawingCacheEnabled(true);
    shareFrame.buildDrawingCache();
    Bitmap bm = shareFrame.getDrawingCache();
    while(bm == null){
    	bm = shareFrame.getDrawingCache();
    }
	FileOutputStream fos;
    try {
        fos = new FileOutputStream(imagePath);
        bm.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    } catch (FileNotFoundException e) {
        Log.e("GREC", e.getMessage(), e);
        return false;
    } catch (IOException e) {
        Log.e("GREC", e.getMessage(), e);
        return false;
    }
	return true;
}
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
       // setContentView();

    } else {
      //  setContentView(R.layout.portraitView);
    }
}
public void setupGameServices(){
	 GameHelperListener listener = new GameHelper.GameHelperListener() {
	        @Override
	        public void onSignInSucceeded() {
	            Signedin = true;
	        }
	        @Override
	        public void onSignInFailed() {
	            Signedin = false;
	        }

	    };
	    mHelper.setup(listener);
}
public Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap,  String gText) {
		  Resources resources = gContext.getResources();
		  @SuppressWarnings("unused")
		float scale = resources.getDisplayMetrics().density;
		  //Bitmap bitmap = 
		  //    BitmapFactory.decodeResource(resources, gResId);
		 
		  android.graphics.Bitmap.Config bitmapConfig =
		      bitmap.getConfig();
		  // set default bitmap config if none
		  if(bitmapConfig == null) {
		    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		  }
		  // resource bitmaps are imutable, 
		  // so we need to convert it to mutable one
		  bitmap = bitmap.copy(bitmapConfig, true);
		 
		  Canvas canvas = new Canvas(bitmap);
		  // new antialised Paint
		  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		  // text color - #3D3D3D
		  paint.setColor(Color.WHITE);
		  // text size in pixels
		  TextView dummy = new TextView(this);
		  dummy.setTextSize(TypedValue.COMPLEX_UNIT_PX,generalButtonDimention[1]/2);
		  paint.setTextSize(dummy.getTextSize());
		  paint.setTypeface(type);
		  // text shadow
		 // paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
		 
		  // draw text to the Canvas center
		  Rect bounds = new Rect();
		  paint.getTextBounds(gText, 0, gText.length(), bounds);
		  int x = (bitmap.getWidth() - bounds.width())/2;
		  int y = (bitmap.getHeight() + bounds.height())/2;
		 
		  canvas.drawText(gText, x, y, paint);
		 
		  return bitmap;
		}

public void setupDialogBox(){
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// Add the buttons
	builder.setMessage("Are you sure you want to clear highscores?");
	builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	       		Editor editor = prefs.edit();
	       		if(gameMode == 0){
	       			editor.putInt("key", 0);
	       		}else if(gameMode == 1){
	       			editor.putInt("many", 0);
	       		}
	    		editor.commit();
	    		timed.setText("Scores Cleared!");
	           }
	       });
	builder.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

	dialog = builder.create();
}
}