package test.test.random_user.net;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class PostCreator {



	public static final PostData getData4(ArrayList<PostItem> arr){

		PostData pd=PostCreator.getData2(arr);

		return pd;
	}

	public static final PostData getData3(PostItem[] pi){

		ArrayList<PostItem> arr=new ArrayList<PostItem>();
		for(int i=0;i<pi.length;i++){
			arr.add(pi[i]);
		}
		PostData pd=PostCreator.getData2(arr);

		return pd;
	}

	public static final PostData getData2(ArrayList<PostItem> fields){

		if(fields!=null){
			try{

				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				DataOutputStream dos=new DataOutputStream(baos);

				String boundary = "1B" + System.currentTimeMillis();
				String crlf = "\r\n";
				String bound="--" + boundary + crlf;

				//StringBuffer sb = new StringBuffer();




				for(int i=0;i<fields.size();i++){

					PostItem pi=fields.get(i);


					if(pi.type==PostItem.ITEM_TYPE_STRING){

						dos.write(bound.getBytes());

						dos.write(("Content-Disposition: form-data; name="+pi.name + crlf+crlf).getBytes());
						dos.write((new String(pi.data) + crlf).getBytes());

					}

					if(pi.type==PostItem.ITEM_TYPE_FILE){

						dos.write(bound.getBytes());
						dos.write(("Content-Disposition: form-data; name=\""+pi.name+"\"" + "; filename=\""+pi.fileName+"\""+ crlf).getBytes());
						dos.write(("Content-Type: "+pi.contentType+ crlf+crlf).getBytes());
						dos.write(pi.data);
						dos.write(crlf.getBytes());

					}



				}

				dos.write((bound+"--").getBytes());




				try {
					PostData pd=new PostData();
					pd.boundary=boundary;
					pd.data=baos.toByteArray();

					try{
						dos.close();

					}catch(Exception e){

					}
					try{
						baos.close();

					}catch(Exception e){

					}

					return pd;



				} catch (Exception e) {

				};
			}catch(Exception e){

			}
		}
		return null;
	}

	public static final PostData getData(ArrayList<PostItem> fields){

		if(fields!=null){


			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			DataOutputStream dos=new DataOutputStream(baos);

			String boundary = "1B" + System.currentTimeMillis();
			String crlf = "\r\n";
			String bound="--" + boundary + crlf;

			StringBuffer sb = new StringBuffer();




			for(int i=0;i<fields.size();i++){

				PostItem pi=fields.get(i);


				if(pi.type==PostItem.ITEM_TYPE_STRING){

					sb.append(bound);
					sb.append("Content-Disposition: form-data; name="+pi.name + crlf+crlf);
					sb.append(new String(pi.data) + crlf);

				}

				if(pi.type==PostItem.ITEM_TYPE_FILE){

					sb.append(bound);
					sb.append("Content-Disposition: form-data; name=\""+pi.name+"\"" + "; filename=\""+pi.fileName+"\""+ crlf);
					sb.append("Content-Type: "+pi.contentType+ crlf+crlf);
					sb.append(new String(pi.data) + crlf);

				}



			}

			sb.append(bound+"--");


			String outpp=sb.toString();

			try {
				PostData pd=new PostData();
				pd.boundary=boundary;
				pd.data=outpp.getBytes("UTF-8");

				return pd;



			} catch (Exception e) {

			};

		}
		return null;
	}


	public static final PostData getData(String[] fields){

		if(fields!=null){


			String boundary = "1B" + System.currentTimeMillis();
			String crlf = "\r\n";
			StringBuffer sb = new StringBuffer();



			for(int i=0;i<fields.length-1;i+=2){

				sb.append("--" + boundary + crlf);
				sb.append("Content-Disposition: form-data; name="+fields[i] + crlf+crlf);
				sb.append(fields[i+1] + crlf);

			}

			sb.append("--" + boundary + crlf);


			String outpp=sb.toString();
			outpp+=new String( crlf+"--" + boundary + "--" + crlf);


			try {
				PostData pd=new PostData();
				pd.boundary=boundary;
				pd.data=outpp.getBytes("UTF-8");

				return pd;



			} catch (Exception e) {

			};

		}
		return null;
	}
}
