package test.test.random_user;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.test.random_user.net.Server;

/**
 * Головний єкран, на якому відображаються ві користувачі
 */
class DisplayMain extends Display implements ViewUserLine.IViewUserLine {


    /**
     * В цьому контейнері відображаютсья всі користувачі
     */
    private LinearLayout llUserLines;

    /**
     * Для роботи з сервером
     */
    private test.test.random_user.Server server;

    /**
     * Завантажений обект JSONObject
     */
    private JSONObject json;

    /**
     * Завантежені користувачі
     */
    private ArrayList<User> users=new ArrayList<User>();

    /**
     * Для дозавантаження коистувачів
     */
    private LoadedPage loadedPage;


    public DisplayMain(MainActivity act, int resId) {
        super(act, resId);



        llUserLines=rootView.findViewById(R.id.ll_user_lines);

        ScrollView ScollView_users=rootView.findViewById(R.id.ScollView_users);


        loadedPage=new LoadedPage(llUserLines);


        block();


        server=new test.test.random_user.Server(act,this);

        //РОзпочнемо завантажувати json файл
        loadUsers();





    }

    /**
     * Завантажити всих користувачів
     */
    public void loadUsers(){
        server.loadUsers();
    }




    @Override
    public void onClick(View view) {
        super.onClick(view);




    }



    /**
     * Створити користувачів по завантаженому файлі
     */
    private void refreshUsersByJson(){
        if(json!=null){


            this.users.clear();

            try {
                JSONArray usersJson=json.getJSONArray("results");
                if(usersJson!=null){
                    for (int i=0;i<usersJson.length();i++){
                        JSONObject userJson=usersJson.getJSONObject(i);
                        if(userJson!=null){
                            try{

                                User user=new User(act,userJson);
                                //додаємо створеного користувавча
                                this.users.add(user);

                            } catch (Exception e) {

                            }


                        }
                    }


                }

            } catch (JSONException e) {


            }


        }

    }

    /**
     * Оновити сторінку по всіх користувачів
     */
    private void refreshPageUsers(){

        for (User user:users
             ) {
            if(user!=null){
                ViewUserLine vul=new ViewUserLine(act,R.layout.view_user_line,this);
                this.llUserLines.addView(vul.rootView);
                vul.setUser(user);


            }
        }

    }

    /**
     * Визивається після завантаження файлу json користувачів
     */
    public void onLoadUser(){


        //Оновляємо користувачів
        refreshUsersByJson();
        //Заповняємо ними сторінку
        refreshPageUsers();

        //Розблокуємо інтерфейс, прогресс, що крутиться
        unBlock();


    }

    /**
     * Визивається, після завантаження данних з сервера
     * @param s
     * @param data
     * @param who що завантажено, визначається при старті завантаження
     */
    @Override
    public void onServerLoad(Server s, String data, int who) {
        super.onServerLoad(s, data, who);



        if(data!=null){

            if(who== test.test.random_user.Server.SERVER_WHO_USERS){
                //Завантажено користувачів
                try {
                    json=new JSONObject(data);

                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onLoadUser();
                        }
                    });

                } catch (JSONException e) {
                    //помилка, спробуємо ще
                    loadUsers();


                }
            }

        }




    }

    @Override
    public void onServerError(Server s, int who) {
        super.onServerError(s, who);

        //спробуємо ще
        loadUsers();


    }


    /**
     * Викликається при кліку на користувача
     * @param vul
     */
    @Override
    public void onClickUser(ViewUserLine vul) {

        if(vul!=null){
            if(vul.user!=null){
                //Встановлюємо екран користувача
                DisplayUser displayUser=act.setDisplayUser();
                if(displayUser!=null){
                    //Встановлюємо на екран створеного раніше користувача
                    displayUser.setUser(vul.user);

                }

            }

        }


    }
}
