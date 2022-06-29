package test.test.random_user;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Для асинхронного завантаження зображень в ImageView
 * Просто визвіть load
 */
class ImageViewLoader implements ImageUrl.IImageUrl {



    private ImageUrl imageUrl;
    private ArrayList<TaskImageLoad> tasks=new ArrayList<TaskImageLoad>();

    public ImageViewLoader(Activity act){
        imageUrl=new ImageUrl(act,this);
    }

    /**
     * Завантажити зображення в ImageView
     * @param img
     * @param url
     */
    public void load(ImageView img, String url){

        //Створимо задачу завантаження
        TaskImageLoad task=new TaskImageLoad(img,url);
        //Додамо но задач
        tasks.add(task);

        //Почнемо завантажувати зображення
        imageUrl.loadImage(url);


    }

    /**
     * Завантиажити зображення з задачі
     * @param task
     */
    public void loadByTask(TaskImageLoad task){

        if(task!=null){
            imageUrl.loadImage(task.url);
        }




    }


    /**
     * повернути задачу по адресі завантаження
     * @param url
     * @return
     */
    private TaskImageLoad getTaskByUrl(String url){
        for (TaskImageLoad task:tasks
             ) {
            if(task!=null){
                if(task.url.equals(url)){
                    return task;

                }
            }
        }
        return null;

    }

    /**
     * Видалити задачу
     * @param task
     */
    private void removeTask(TaskImageLoad task){
        if(tasks!=null){
            tasks.remove(task);

        }

    }

    /**
     * Визветься при завершенні завантаження зображення
     * @param bitmap
     * @param url
     */
    @Override
    public void onImageLoaded(Bitmap bitmap, String url) {

        TaskImageLoad task=getTaskByUrl(url);
        if(task!=null){

            //Встановимо зображення в ImageView
            task.img.setImageBitmap(bitmap);

            removeTask(task);

        }

    }

    /**
     * Помилка завантаження зображення
     * @param url
     */
    @Override
    public void onImageError(String url) {

        TaskImageLoad task=getTaskByUrl(url);
        if(task!=null){
            //Почнемо ще завантажувати
            loadByTask(task);
        }


    }

    /**
     * Задача для завантаження зображення в IMageView
     */
    class TaskImageLoad{

        /**
         * В яке завантажити
         */
        public ImageView img;
        /**
         * Адреса зображення
         */
        public String url;

        public TaskImageLoad(ImageView img, String url){

            this.img=img;
            this.url=url;

        }

    }
}
