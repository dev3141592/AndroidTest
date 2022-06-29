package test.test.random_user;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.test.random_user.net.Server;


/**
 * Екран користувача
 */
class DisplayUser extends Display {


    /**
     * Зображення користувача
     */
    private ImageView imageViewUser;
    /**
     * Назва користувача
     */
    private TextView textViewName;
    /**
     * Пошта коритувача
     */
    private TextView textViewMail;

    private TextView textViewLogin;
    private TextView textViewLocation;
    private TextView textViewRegisstered;
    /**
     * Кнопка повернутися назад
     */
    private Button buttonBack;





    public DisplayUser(MainActivity act, int resId) {
        super(act, resId);


        imageViewUser=this.rootView.findViewById(R.id.ImageView_user);
        textViewName=this.rootView.findViewById(R.id.textView_name);
        textViewMail=this.rootView.findViewById(R.id.textView_mail);
        buttonBack=this.rootView.findViewById(R.id.button_back);

        textViewLogin=this.rootView.findViewById(R.id.textView_login);
        textViewRegisstered=this.rootView.findViewById(R.id.textView_registered);
        textViewLocation=this.rootView.findViewById(R.id.textView_location);


        buttonBack.setOnClickListener(this);





    }

    /**
     * Встановити користувача на екран
     * @param user
     */
    public void setUser(User user){

        if(user!=null){

            //Встановлюемо, зображення завантаження
            imageViewUser.setImageResource(R.drawable.loading);


            //Завантажемо зображення в ImageView
            user.setInImageViewBySizeName(imageViewUser,"large");


            textViewName.setText(""+user.getName());
            textViewMail.setText(""+user.getEmail());

            textViewLogin.setText(""+user.getLogin());
            textViewRegisstered.setText(""+user.getRegistered());
            textViewLocation.setText(""+user.getLocation());


        }


    }



    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view!=null && view==buttonBack){
            act.setDisplayMain();
        }


    }



}
