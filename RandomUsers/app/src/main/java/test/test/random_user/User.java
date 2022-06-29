package test.test.random_user;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * користувач
 */
class User {

    /**
     * JSON Який представляє користувача
     */
    private JSONObject json;
    /**
     * Для завантаження зображень в ImageView
     */
    private ImageViewLoader imageViewLoader;



    public User(Activity act,JSONObject json){
        this.json=json;

        imageViewLoader=new ImageViewLoader(act);

    }


    /**
     * Встановити зображення користувача в IMageView, в залежності від розміру
     * @param imageView
     * @param sizeName large,medium,thumbnail
     */
    public void setInImageViewBySizeName(ImageView imageView,String sizeName){

        try {
            JSONObject picture=json.getJSONObject("picture");
            if(picture!=null){
                String url=picture.getString(sizeName);
                setInImageView(imageView,url);

            }
        } catch (Exception e) {
        }


    }


    /**
     * Завантажити зображення в imageView
     * @param imageView
     * @param url Адреса зображення
     */
    private void setInImageView(ImageView imageView,String url){

        imageViewLoader.load(imageView,url);


    }


    /**
     * Повернути пошту коритувача
     * @return
     */
    public String getEmail(){
        if(json!=null){

            try {
                String email=json.getString("email");

                if(email!=null) {
                    return email;
                }

            } catch (Exception e) {
            }

        }
        return "";

    }




    private String getUserParam2(String param1,String param2){
        if(json!=null){

            try {
                JSONObject jsonObj=json.getJSONObject(param1);
                if(jsonObj!=null){
                    String param=jsonObj.getString(param2);

                    return param;

                }
            } catch (Exception e) {
            }

        }
        return "";
    }


    public String getLogin(){

        return getUserParam2("login","uuid");

    }

    public String getLocation(){

        return getUserParam2("location","city");


    }

    public String getRegistered(){

        return getUserParam2("registered","date");


    }
    /**
     * Повернути назву користувача
     * @return
     */
    public String getName(){
        if(json!=null){

            try {
                JSONObject name=json.getJSONObject("name");
                if(name!=null){
                    String nameReturn=name.getString("title")+" "+name.getString("first")+" "+name.getString("last");

                    return nameReturn;

                }
            } catch (Exception e) {
            }

        }
        return "";

    }





}
