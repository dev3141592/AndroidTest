package test.test.random_user.net;
public class PostItem {

	
	public static final int ITEM_TYPE_STRING=1;
	public static final int ITEM_TYPE_FILE=2;
	
	
	public int type;
	public String name;
	public byte[] data;
	public String fileName;
	public String contentType;
	
	/**
	 * 
	 *
	 * @param data
	 */
	public PostItem(String name, byte[] data){
		
		this.type=ITEM_TYPE_STRING;
		this.data=data;
		this.name=name;
		
	}
	
    public PostItem(String name, String filename, String contentType, byte[] data){
		
		this.type=ITEM_TYPE_FILE;
		this.fileName=filename;
		this.contentType=contentType;
		this.data=data;
		this.name=name;
		
	} 
	
}
