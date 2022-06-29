package test.test.random_user;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Рядок користувача на первому екрані
 */
class ViewUserLine extends LoadedPageElement {


    private ImageView ImageViewUserPhoto;
    private TextView textViewUserName;


    /**
     * Встановлений користувач
     */
    public User user;

    private IViewUserLine vul;


    public ViewUserLine(MainActivity act, int resId,IViewUserLine vul){
        super(act,resId);


        this.vul=vul;

        ImageViewUserPhoto=rootView.findViewById(R.id.imagView_user_photo);
        textViewUserName=rootView.findViewById(R.id.textView_user_name);

        ImageViewUserPhoto.setImageResource(R.drawable.loading);

        this.rootView.setOnClickListener(this);



    }

    public void dispatchClickUser(){
        if(vul!=null){
            vul.onClickUser(this);

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view==rootView){

            dispatchClickUser();

        }
    }

    /**
     * Встановти користувача в рядок
     * @param user
     */
    public void setUser(User user){


        this.user=user;

        //Завантаження зображення з середнім розміром
        user.setInImageViewBySizeName(ImageViewUserPhoto,"medium");
        //Встановимо назву
        textViewUserName.setText(user.getName());


    }



    public interface IViewUserLine{
        public void onClickUser(ViewUserLine vul);
    }


}
