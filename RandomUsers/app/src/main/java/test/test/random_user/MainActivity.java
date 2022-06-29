package test.test.random_user;

import androidx.annotation.NonNull;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity{


    public DisplayMain displayMain;
    public DisplayUser displayUser;

    /**
     * Пікселів на dp
     */
    public float pixdp=0;

    /**
     * Розмір екрана
     */
    public int wid,hei;


    /**
     * Поточний диспей
     */
    public Display thisDisplay=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fullScreen();


        this.setContentView(R.layout.size);
        View v=this.findViewById(R.id.size);
        pixdp=((float)v.getLayoutParams().width/100f);


        this.setContentView(R.layout.display_size);
        LinearLayout llds=(LinearLayout) this.findViewById(R.id.ll_size_display);

        Rect size = new Rect();
        llds.getWindowVisibleDisplayFrame(size);
        wid=size.width();
        hei=size.height();



        setDisplayMain();



    }


    /**
     * Встановити головний екран
     */
    public void setDisplayMain(){

        if(displayMain==null){
            displayMain=new DisplayMain(this,R.layout.display_main);
        }
        setDisplay(displayMain);
    }


    /**
     * Встановити екран користувача
     */
    public DisplayUser setDisplayUser(){

        if(displayUser==null){
            displayUser=new DisplayUser(this,R.layout.display_user);
        }
        setDisplay(displayUser);

        return displayUser;
    }



    /**
     * Встановити екрана
     * @param display
     */
    private void setDisplay(Display display){
        if(display!=null) {
            this.thisDisplay = display;
            setContentView(display.rootView);

            display.onSet();
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionManager.onRequestPermissionsResult(this,requestCode, permissions, grantResults);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult( requestCode,  resultCode,  data);


        if(this.thisDisplay!=null){
            this.thisDisplay.onActivityResult(requestCode,  resultCode,  data);

        }

    }


    private void fullScreen(){

        try{

            try{

                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                requestWindowFeature(Window.FEATURE_NO_TITLE);

            }catch(Exception e){

            }

            if (Build.VERSION.SDK_INT < 16) {

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }else{

                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                // Remember that you should never show the action bar if the
                // status bar is hidden, so hide that too if necessary.
                ActionBar actionBar = getActionBar();
                actionBar.hide();

            }

            hideSystemUI();

        }catch(Exception e){

        }

    }

    private void hideSystemUI() {
        try{
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }catch(Exception e){

        }
    }

}