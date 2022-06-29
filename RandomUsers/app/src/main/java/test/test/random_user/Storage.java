package test.test.random_user;

import android.content.Context;
import android.content.SharedPreferences;


public class Storage {

	private Context context;
	SharedPreferences settings=null;
	SharedPreferences.Editor prefEditor=null;
	
	
	
	public Storage(Context context){
		this.context=context;
		
		settings = context.getSharedPreferences("opened", Context.MODE_PRIVATE);
        prefEditor = settings.edit();
	}

	
	
	
	/**
	 * Проверить есть ли ячейка с такми имененм
	 * @param name
	 * @return
	 */
	public boolean isEmpty(String name){
		
		return settings.contains(name);
	}
	
	/**
	 * ЯПолучить стороку с ячейки
	 * @param name имя ячейки
	 * @return возвратит строку или null
	 */
	public String getString(String name){
		
		String str=settings.getString(name, null);
		
				prefEditor.commit();
		return str;
		
	}
	
	/**
	 * Получить float с ячейкеи, если нету то вернёт 0
	 * @param name имя ячейки
	 * @return число или 0 если нету, но вернёт 0 если там 0. Определить есть ли такая ячейка нельзя точно
	 */
	public float getFloat(String name){
		
	        float fl=settings.getFloat(name, 0);
	        prefEditor.commit();
	        return fl;
		
	}
	
	/**
	 * 
	 * @param name имя ячейки для храненния
	 * @param value значение
	 */ 
	public void setFloat(String name, float value){
		prefEditor.putFloat(name, value);
		prefEditor.commit();
		
		
	}
	
	public void setLong(String name, long value){
		prefEditor.putLong(name, value);
		prefEditor.commit();
		
		
	}
	
	public void setDouble(String name, double value){
		
		
		prefEditor.putString(name, ""+value);
		prefEditor.commit();
		
		
	}
    public double getDouble(String name){
		
    	double lon=0;
    	try{
		   lon= Double.parseDouble(settings.getString(name, "0"));
    	}catch(Exception e){
    		
    	}
        prefEditor.commit();
        return (double)lon;
		
		
	}
	
	public long getLong(String name){
		
		long lon=settings.getLong(name, 0);
        prefEditor.commit();
        return lon;
		
		
	}
	
	public void setBoolean(String name, boolean value){
		prefEditor.putBoolean(name, value);
		prefEditor.commit();
		
		
		
		
	}
	
	/**
	 * Возвратит значение с ячейки под имене, если нету ячейки то возвратит false
	 * @param name
	 * @return
	 */
	public boolean getBoolean(String name){
		
		boolean boo=settings.getBoolean(name, false);
        prefEditor.commit();
        return boo;
		
		
	}
	
	
	public void setInteger(String name, int value){
		prefEditor.putInt(name, value);
		prefEditor.commit();
		
	}
	
	public int getInteger(String name){
		
        int fl=settings.getInt(name, 0);
        prefEditor.commit();
        return fl;
	
    }
	
	public void remove(String key){
		
		prefEditor.remove(key);
		prefEditor.commit();
	}
	/**
	 * 
	 * @param name имя ячейки для храненния
	 * @param value значение
	 */ 
	public void setString(String name, String value){
		
		prefEditor.putString(name, value);
		prefEditor.commit();
		
	}


	/**
	 * Записать массив
	 * @param arr
	 * @param key
	 */
	public void setArrayString(String[] arr, String key){

		if(arr!=null) {
			this.setInteger(key + "_count", arr.length);

			for (int i = 0; i < arr.length; i++) {
				this.setString(key + "_value" + i, arr[i]);
			}
		}


	}
	/**
	 * Вернуть массив
	 * @param key
	 * @return
	 */
	public String[] getArrayString(String key){

		int count=this.getInteger(key+"_count");
		String[] retu=null;
		if(count>0){
			retu=new String[count];
			for(int i=0;i<count;i++){
				retu[i]=this.getString(key+"_value"+i);
			}
		}

		return retu;
	}
	
	/**
	 * Записать массив
	 * @param arr
	 * @param key
	 */
	public void setArrayInt(int[] arr, String key){

	    if(arr!=null) {
            this.setInteger(key + "_count", arr.length);

            for (int i = 0; i < arr.length; i++) {
                this.setInteger(key + "_value" + i, arr[i]);
            }
        }
		
		
	}



	/**
	 * Вернуть массив
	 * @param key
	 * @return
	 */
    public int[] getArrayInt(String key){
		
		int count=this.getInteger(key+"_count");
		int[] retu=null;
		if(count>0){
		    retu=new int[count];
		for(int i=0;i<count;i++){
			retu[i]=this.getInteger(key+"_value"+i);
		}
		}
		
		return retu;
	}
	
	
}
