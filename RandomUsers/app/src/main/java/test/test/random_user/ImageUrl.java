package test.test.random_user;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Для всинхронного завантаження зображень з інтернета
 * Почніть завантажувати, коли зображення завантажиться - визветься метод інтерфесу
 */
class ImageUrl {

    private Activity activity;

    private Storage storage;

    private IImageUrl iImageUrl;


    public ImageUrl(Activity activity, IImageUrl iImageUrl) {


        this.activity = activity;
        this.iImageUrl = iImageUrl;
        storage = new Storage(activity);
    }


    /**
     * Зберегти зображення на диску, щоб не завантажувати повторно
     * @param bitmap
     * @param url
     * @return
     */
    private boolean saveBitmap(Bitmap bitmap, String url) {

        if (bitmap != null) {

        }

        return false;

    }

    /**
     * Завантажити зображення з диску, щб не завантажувати з інтернета
     * @param url
     * @return
     */
    private Bitmap loadBitmap(String url) {




        return null;

    }


    public void loadImage(final String url) {

        try {
            URL urlLoadImage = new URL(url);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //перевірка в кеші
                        Bitmap bmImage = loadBitmap(url);
                        if (bmImage == null) {
                            //В кеші немає, загрузимо з інтернета
                            bmImage = BitmapFactory.decodeStream(urlLoadImage.openConnection().getInputStream());

                            //Зберегти в кеш
                            saveBitmap(bmImage, url);
                        }

                        final Bitmap bmLoaded = bmImage;


                        if (bmLoaded != null) {
                            dispatchLoadImage(bmLoaded, url);

                        } else {
                            //Помилка завантаження
                            dispatchErrorLoadImage(url);
                        }


                    } catch (Exception e) {

                        //Якась помилка
                        dispatchErrorLoadImage(url);

                    }
                }
            }).start();

        } catch (Exception e) {

            dispatchErrorLoadImage(url);
        }

    }

    /**
     * повідомити, що зображення завантажено
     * @param bmLoaded
     * @param url
     */
    public void dispatchLoadImage(final Bitmap bmLoaded, final String url) {
        if (iImageUrl != null && activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iImageUrl.onImageLoaded(bmLoaded, url);
                }
            });

        }
    }

    /**
     * Повідомити про помилку завантаження
     * @param url
     */
    public void dispatchErrorLoadImage(String url) {
        if (iImageUrl != null && activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iImageUrl.onImageError(url);
                }
            });

        }
    }


    /**
     * Інтерфейс для колбеків
     */
    public interface IImageUrl {
        /**
         * Викликаєтсья при завантаженні зображення
         * @param bitmap
         * @param url
         */
        public void onImageLoaded(Bitmap bitmap, String url);

        /**
         * Викликаєтсья при помилці завантаження
         * @param url
         */
        public void onImageError(String url);
    }
}
