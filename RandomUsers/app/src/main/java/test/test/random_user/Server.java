package test.test.random_user;

class Server extends test.test.random_user.net.Server {

    public static final int SERVER_WHO_USERS=1;

    public Server(MainActivity context, IServerListener isl) {
        super(context, isl);


    }

    /**
     * Завантажити користувачів
     */
    public void loadUsers(){
        this.load(ManagerUrl.getUrlGetUser(act),SERVER_WHO_USERS);

    }

}
