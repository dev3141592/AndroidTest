package test.test.random_user;
import android.content.Context;

import test.test.random_user.R;


public class ManagerUrl {

	
	
	public static final String getUrlSite(Context act){
		return act.getResources().getString(R.string.url_site);
	}
	public static final String getUrlPath(Context act){
		return act.getResources().getString(R.string.url_path);
	}
	public static final String getUrlProtocol(Context act){
		return act.getResources().getString(R.string.url_protocol);
	}
	
	public static final String getUrlDo(Context act){
		return getUrlProtocol(act)+ManagerUrl.getUrlSite(act)+ManagerUrl.getUrlPath(act);
	}

	//===================================

	public static final String getUrlGetUser(Context act){
		return getUrlDo(act)+act.getResources().getString(R.string.url_get_user);
	}


}
