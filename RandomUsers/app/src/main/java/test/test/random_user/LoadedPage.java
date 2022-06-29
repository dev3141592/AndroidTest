package test.test.random_user;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Класс, для підзавантаженні сторінки, не встиг допрацювати, цей класс не використовуеться
 */
class LoadedPage implements View.OnScrollChangeListener, View.OnTouchListener {

    public int startElement=0;
    public int endElement=0;

    private LinearLayout container;

    public LoadedPage(LinearLayout container){
        this.container=container;



        container.setOnTouchListener(this);



    }



    public void pushBack(ArrayList<LoadedPageElement> elements){

    }

    public void pushFront(ArrayList<LoadedPageElement> elements){

    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {



    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        return false;
    }


    public interface ILoadedPage{
        public void onNeedLoad(int startElement,int endElement);
    }


}
