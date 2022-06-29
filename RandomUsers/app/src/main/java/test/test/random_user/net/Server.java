package test.test.random_user.net;

import java.util.ArrayList;
import java.util.Locale;

import test.test.random_user.MainActivity;


public class Server implements HttpLoader.IHttpLoaderListener {




    private IServerListener isl;

    protected MainActivity act;

    /**
     * Текущий загрузчик
     */
    private HttpLoader httpL;

    public String log = "";


    public Server(MainActivity context, IServerListener isl) {

        this.isl = isl;
        this.act = context;

    }


    public void load(String url, int who, String android, String pass, ArrayList<PostItem> fields) {

        PostItem[] post = null;
        if (fields != null && fields.size() > 0) {
            int i = 0;
            post = new PostItem[fields.size()];
            for (PostItem posti : fields
            ) {
                post[i] = posti;
                i++;
            }

        }

        load(url, who, android, pass, post);
    }


    public void load(String url, int who, String android, String pass, PostItem[] fields) {

        String locale = Locale.getDefault().getLanguage();

        ArrayList<PostItem> arr = new ArrayList<PostItem>();
        arr.add(new PostItem("android", ("" + android).getBytes()));
        arr.add(new PostItem("pass", ("" + pass).getBytes()));

        arr.add(new PostItem("locale", locale.getBytes()));

        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                arr.add(fields[i]);
            }
        }

        PostData pd = PostCreator.getData2(arr);
        httpL = new HttpLoader(new UserAgent(null), this, act);
        httpL.setPostData(pd);
        httpL.who = who;
        httpL.load(url);
    }


    public void load(String url, int who) {


        httpL = new HttpLoader(new UserAgent(null), this, act);
        httpL.who = who;
        httpL.load(url);
    }


    /**
     * Остановить текущую загрузку, все колбачные вызовы не будут получены
     */
    public void stop() {
        if (httpL != null) {
            this.httpL.stop();
        }
    }


    public interface IServerListener {

        public void onServerLoad(Server s, String data, int who);

        public void onServerError(Server s, int who);

        public void onServerUploadPercent(Server s, int who, int percent);


    }


    @Override
    public void onLoadHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {


            try {

                String data = null;
                if (l.getLoadedBytes() != null) {
                    try {

                        data = new String(l.getLoadedBytes(), "utf-8");

                    } catch (Exception e) {

                    }
                }


                if (data != null) {

                    isl.onServerLoad(this, data, l.who);


                } else {


                    isl.onServerError(this, l.who);

                }

            } catch (Error e) {
                isl.onServerError(this, l.who);
            }

        }

    }


    @Override
    public void onErrorHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {
            isl.onServerError(this, l.who);
        }
    }


    @Override
    public void onLogHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        String addLog = l.getLogThis();
        log += "\n" + addLog;

        //act.showDebugMessage(addLog);

    }


    @Override
    public void onNotAllowedHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {
            isl.onServerError(this, l.who);
        }
    }


    @Override
    public void onNotMethodHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {
            isl.onServerError(this, l.who);
        }
    }


    @Override
    public void onEmptyHttpLoader(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {
            isl.onServerError(this, l.who);
        }
    }


    @Override
    public void onPercentUploadHttpLoadr(HttpLoader l) {
        // TODO Auto-generated method stub
        if (isl != null) {
            isl.onServerUploadPercent(this, l.who, l.thisPercentUpload);
        }
    }
}
