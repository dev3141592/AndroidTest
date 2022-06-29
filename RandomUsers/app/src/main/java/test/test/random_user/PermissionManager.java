package test.test.random_user;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionManager {


	/**
	 * Разрешения, которые должны быть установлены по очереди
	 */
	private static String[] setPermissions;
	/**
	 * текущее разрешение, которое нужн опроверить
	 */
	private static int thisSetPermission=-1;
	
	/**
	 * Активность, в которой проверяются разрешения
	 */
	private static Activity activity;
	/**
	 * 
	 * @param act
	 * @param permissions  Manifest.permission.*
	 */
	@SuppressLint("NewApi")
	public static void check(Activity act, String permissions) {

		activity=act;
		
		
			if (Build.VERSION.SDK_INT > 22) {

				try {
					int permissionCheck = act
							.checkSelfPermission(permissions);

					if (PackageManager.PERMISSION_DENIED == permissionCheck) {
						// нету прав, нужно запросить, но проверить ранние
						// запросы
						
						
						// разрешение есть
						boolean ranhe = act
								.shouldShowRequestPermissionRationale(permissions);
						if (ranhe) {
							// раньше было отклонено разрешение
							

						} else {

						}
						
						
						act.requestPermissions(
								new String[] { permissions},2346346);



					} else {
						
						checkNextPermission(act);
					}
				} catch (Exception e) {
					checkNextPermission(act);
				}

			} else {
				
				
				checkNextPermission(act);
				
			}

		

	}
	
	
	
	/**
	 * Есть ли данное разрешение
	 * @param permission Manifest.permission.*
	 * @return
	 */
	@SuppressLint("NewApi")
	public static final boolean isPermission(Activity act, String permission){
		
		if (Build.VERSION.SDK_INT <=22) {
			return true;
		}else{
		     
			int permissionCheck = act
					.checkSelfPermission(permission);

			if (PackageManager.PERMISSION_DENIED == permissionCheck) {
				return false;
			}else{
				return true;
			}
		}


	}
	
	/**
	 * Есть ли данное разрешение
	 * @param permission Manifest.permission.*
	 * @return
	 */
	@SuppressLint("NewApi")
	public static final boolean isPermission(MainActivity act, String[] permission){
		
		if(permission!=null){
		for(int i=0;i<permission.length;i++){
			
		if (Build.VERSION.SDK_INT <=22) {
			return true;
		}else{
		     
			int permissionCheck = act
					.checkSelfPermission(permission[i]);

			if (PackageManager.PERMISSION_DENIED == permissionCheck) {
				return false;
			}
		}
		}
		}
		
		return true;
		
	}
	
	
	/**
	 * 
	 * @param act
	 * @param permissions
	 */
	@SuppressLint("NewApi")
	public static void check(Activity act, String[] permissions){
		
		
	       
		   setPermissions=permissions;
		   thisSetPermission=-1;
		   activity=act;
		   checkNextPermission(act);


		   
	}
	
	private static void checkNextPermission(Activity act){
		
		if(setPermissions!=null){
			 thisSetPermission++;
			 if(thisSetPermission<setPermissions.length){
			 	if(!PermissionManager.isPermission(act,setPermissions[thisSetPermission])) {
					PermissionManager.check(activity, setPermissions[thisSetPermission]);
				}else{
					PermissionManager.checkNextPermission(act);
				}

			 }
		}
	}
	
	/**
	 * Ставить в таком же методе активности
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	public static void onRequestPermissionsResult(MainActivity act, int requestCode,
                                                  String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		

			if(permissions!=null && grantResults!=null){
				if(permissions.length==grantResults.length){
					
					for(int i=0;i<permissions.length;i++){
						//каждое разрешение на которое получен ответ
						
							if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
								
							}
						
					}
					
					PermissionManager.checkNextPermission(act);
					
				}
				
			}

		checkNextPermission(act);
		
	}
	
	
	
	
}
