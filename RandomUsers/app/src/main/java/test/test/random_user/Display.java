package test.test.random_user;

import android.content.Intent;
import android.widget.Toast;

import test.test.random_user.net.Server;


/**
 * Головний класс для всіх єкранів
 */
class Display extends SimpleView implements Server.IServerListener {




    public Display(MainActivity act, int resId){
        super(act, resId);



    }

    /**
     * Вызыввается после установки экрана
     */
    public void onSet(){

    }


    protected void run(){

    }


    /**
     * показати тост
     * @param text
     */
    protected void toast(String text){

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(act, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){


    }



    @Override
    public void onServerLoad(Server s, String data, int who) {

    }

    @Override
    public void onServerError(Server s, int who) {

    }

    @Override
    public void onServerUploadPercent(Server s, int who, int percent) {

    }
}
