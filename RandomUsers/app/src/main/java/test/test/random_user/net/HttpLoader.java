package test.test.random_user.net;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;


public class HttpLoader implements Runnable {


	/**
	 * Затруднительное Исключение генерируется, если процесс не может связать локальный
	 * адрес / порт, либо потому, что он уже связан или зарезервирован ОС.
	 */
	public static final int ERORR_CLASS_BindException=1;
	/**
	 * ConnectException генерируется, если соединение не может
	 * быть установлено на удаленный хост по определенному порту.
	 */
	public static final int ERORR_CLASS_ConnectException=2;
	/**
	 * Если запрос HTTP должен быть повторен, это будет сгенерировано исключение,
	 * если запрос не может быть повторена автоматически.
	 */
	public static final int ERORR_CLASS_HttpRetryException=3;
	/**
	 * Это исключение, когда программа пытается создать URL из неправильного описания.
	 */
	public static final int ERORR_CLASS_MalformedURLException=4;
	/**
	 * NoRouteToHostException будет выброшен при попытке подключения к удаленному узлу,
	 *  но хозяин не может быть достигнуто, например, из-за плохо настроенный маршрутизатор
	 *  или брандмауэр блокирует.
	 */
	public static final int ERORR_CLASS_NoRouteToHostException=5;
	/**
	 * Это PortUnreachableException будет выброшено, если сообщение ICMP Port Unreachable было получено.
	 */
	public static final int ERORR_CLASS_PortUnreachableException=6;
	/**
	 * Сигналы, либо попытка подключения к разъему неправильного типа, произошла применение неподдерживаемой
	 *  операции или общей ошибки в базовом протоколе.
	 */
	public static final int ERORR_CLASS_ProtocolException=7;
	/**
	 * Это SocketException может быть выброшен при создании сокета или настройки опций, а также
	 * является суперклассом всех других исключений, связанных с сокетов.
	 */
	public static final int ERORR_CLASS_SocketException=8;
	/**
	 * Это исключение, когда тайм-аут истек сокет чтения или принять операции.
	 */
	public static final int ERORR_CLASS_SocketTimeoutException=9;
	/**
	 * Брошенный, когда имя хоста не может быть решена.
	 */
	public static final int ERORR_CLASS_UnknownHostException=10;
	/**
	 * Отбрасывается, если нет необходимости ContentHandler не может быть найдено
	 * для конкретной услуги, запрашиваемой связи URL.
	 */
	public static final int ERORR_CLASS_UnknownServiceException=11;
	/**
	 * URISyntaxException будет выброшено, если некоторая информация
	 * не может быть разобран при создании URI.
	 */
	public static final int ERORR_CLASS_URISyntaxException=12;

	/**
	 * события загрузки
	 */
	private IHttpLoaderListener listener;
	/**
	 * Адрес загрузки
	 */
	private String url;

	/**
	 * Загруженные данные
	 */
	private byte[] loadedByte;


	/**
	 * метод запроса, например GET, HEAD, POST
	 */
	protected String requestMethod="GET";
	/**
	 * Какой контент загружен, может быть null
	 */
	protected String fieldContentType;

	/**
	 * Сколько байтов контент, модет быть null, или неточно
	 */
	private String fieldContentLength;

	/**
	 * может быть null
	 */
	private String fieldContentEncoding;

	/**
	 * Последнее время измененния обьекта
	 * Tue, 15 Nov 1994 12:45:26 GMT
	 */
	private String fieldLastModified;

	/**
	 * Смотрим описание в http
	 */
	private String fieldVia;

	/**
	 * Хранить via
	 */
	private boolean saveVia;


	private String fieldServer;

	/**
	 * методы которые принимает сервер для ресурса
	 * Allow: GET, HEAD, PUT
	 */
	private String fieldAllow;

	/**
	 * Коддировка текста если тип контента текстовый
	 */
	private String charset;
	/**
	 * параметры агента
	 */
	private UserAgent userAgent;

	/**
	 * Загружать только по https
	 */
	public boolean onlyHttps=false;



	private boolean useCookie=true;



	private Logger log;

	private int errorClass=0;

	private String referer;


	/**
	 * текущий поток загрузки
	 */
	private Thread th;

	/**
	 * Айдизагрузчика если понадобится устанавливаем сами
	 */
	public int id;

	/**
	 * Скольок загржено данных с адреса, можно при этом и contentLenght отображать ход загрузки
	 */
	private long thisReaded=0;


	private boolean load=false;


	/**
	 * Данне если они не null, то их нужно будет отправить в в заданный адрес и метод включить Post
	 */
	private PostData postData;

	private int maxBytes=-1;


	public int who;
	/**
	 * Осуществлели был вход
	 */
	public boolean login;

	private Context context;

	private SSLContext sslcontext;

	private HttpURLConnection uc;

	private OutputStream os;
	private InputStream in;

	public boolean isStop;

	/**
	 * Текущий процент загрузки поста
	 */
	public int thisPercentUpload=0;

	public HttpLoader(UserAgent userAgent, IHttpLoaderListener listener, Context context) {

		this.context=context;

		this.listener = listener;
		this.userAgent=userAgent;
		log=new Logger(listener);



	}

	/**
	 * Устновить максимальное количестов байтов для загрузки. Если необходимо протестировать
	 * чтото напиример. когда не нужен весь ответ полностью, файл и т.д.
	 * !!!!!!!!!!!Нужно устанавливать при каждой закачке.
	 * @param maxBytesLoad -1 - всё качать, или сколько указано
	 */
	public void setMaxBytes(int maxBytesLoad){
		maxBytes=maxBytesLoad;
	}

	public int getMaxBytes(){
		return this.maxBytes;
	}

	/**
	 * получить какие данные отправятся при запросе к серверу
	 * Данне если они не null, то их нужно будет отправить в в заданный адрес и метод включить Post
	 * @return
	 */
	public PostData getPostData() {
		return postData;
	}


	/**
	 * Установить данные, которых нужно отправить на сервер, после отправки. данные зануляются
	 *
	 * @param postData
	 */
	public void setPostData(PostData postData) {
		this.postData = postData;
	}

	/**
	 * Установить какеим методом посылать запрос
	 * @param method GET, HEAD, POST
	 */
	public void setRequestMethod(String method){
		this.requestMethod=method;
	}

	/**
	 * Получить какой на текущий момент установлен метод запроса
	 * @return
	 */
	public String getRequestMethod(){
		return this.requestMethod;
	}

	/**
	 * было ли начато загрузку когда либо в этом отекте. напримепр если нужноначать хзагружать список адресов
	 * в неопределённом месте, и неизвестно, впервые запустится загрузка или нет
	 * @return
	 */
	public boolean isLoading(){
		return load;
	}


	/**
	 * Использовать ли кукиЮ принимать ли
	 */
	public void setUseCookie(boolean use){
		this.useCookie=use;
	}

	/**
	 * Установить агента
	 * @param userAgent
	 */
	public void setUserAgent(UserAgent userAgent){

		this.userAgent=userAgent;
	}

	/**
	 * Начать загружать, оповещение будет через интерфейс
	 *
	 * @param url
	 */
	public void load(String url) {

		this.load=true;

		this.url = url;

		this.stop();


		th = new Thread(this);
		th.start();



	}

	/**
	 * Остановить текущую загрузку, не все колбачные вызовы будут получены
	 */
	public void stop(){
		if(th!=null){

			try{
				uc.disconnect();

			}catch(Exception e){

			}


			try{
				os.close();



			}catch(Exception e){

			}
			try{
				in.close();

			}catch(Exception e){

			}



			try{

				//th.interrupt();
			}catch(Exception e){

			}



		}

		isStop=true;
	}
	/**
	 * Начать загружать, оповещение будет через интерфейс
	 *
	 * @param url
	 */
	public void load(String url, String referer) {

		this.stop();

		this.load=true;
		this.referer=referer;

		this.url = url;

		th = new Thread(this);
		th.start();

	}

	/**
	 * получить загруженные байты
	 *
	 * @return
	 */
	public byte[] getLoadedBytes() {
		return this.loadedByte;
	}

	/**
	 * длинна контента
	 * @return
	 */
	public String getFieldContentLength(){
		return this.fieldContentLength;
	}

	/**
	 * Получить тип загруженого
	 *
	 * @return
	 */
	public String getFieldContentType() {
		return this.fieldContentType;
	}

	/**
	 * Скольок загружено контента в текущем адресе
	 * @return
	 */
	public long getThisReaded() {
		return thisReaded;
	}

	/**
	 * Какие методы пдерживат сервер на ресурс
	 * @return
	 */
	public String getFieldAllow() {
		return this.fieldAllow;
	}

	public String getFieldServer(){
		return this.fieldServer;
	}

	/**
	 * Кодировка контента
	 * @return
	 */
	public String getFieldContentEncoding(){
		return this.fieldContentEncoding;
	}

	/**
	 * Установиь вести ли лог
	 * @param logging
	 */
	public void setLogging(boolean logging){
		this.log.logging=logging;
	}

	/**
	 * Ведутся ли логи
	 * @return
	 */
	public boolean isLogging(){
		return this.log.logging;
	}

	/**
	 * Кодировка текста, или null
	 * @return Кодировка текста, или null
	 */
	public String getCharset(){

		if(this.charset==null){

			String c=this.fieldContentType;
			if(c!=null){

				int idc=c.indexOf("charset");
				if(idc!=-1){

					int iddo=c.indexOf("=", idc);
					if(iddo!=-1){

						int idfin=c.indexOf(";", iddo);

						if(idfin==-1){
							idfin=c.length();
						}

						c=c.substring(iddo+1, idfin);
					}

				}
			}

			this.charset=c;
		}
		return this.charset;
	}

	/**
	 * Вернуть строковое представление даты послдней модификации загруженноого ресурса
	 * @return
	 */
	public String getFieldLastmodified(){
		return this.fieldLastModified;
	}

	public String getFieldVia(){
		return this.fieldVia;
	}

	/**
	 * Хранить ли via поле
	 */
	public void setSaveVia(boolean savevia){
		this.saveVia=savevia;
	}

	/**
	 * по какому адресу сейчас загружается или загрузилось
	 * @return
	 */
	public String getUrl(){
		return this.url;
	}

	/**
	 * Получить имя класса ошибки, сравнить с константами этого класса
	 * @return
	 */
	public int getErrorClass(){
		return this.errorClass;
	}

	/**
	 * Лог загрузки
	 * @return Лог загрузки
	 */
	public String getLogGlobal(){
		return this.log.logGlobal;
	}

	public String getLogLocal(){
		return this.log.logLocal;
	}
	public String getLogThis(){
		return this.log.logThis;
	}




	/**
	 * Загрузка с интернета в отдельном потоке
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub


		//=====================ресет раннее загруженного
		this.log.logLocal="";

		this.loadedByte = null;

		this.fieldContentEncoding=null;
		this.fieldContentType=null;
		this.fieldContentLength=null;

		this.charset=null;
		this.thisReaded=0;
		this.errorClass=0;


		boolean loaded=false;

		//===========================начало загрузки
		uc = null;
		//HttpURLConnection uc2 = null;
		in = null;
		try {



			this.log.log("==========\nurl="+url);


			URL url = new URL(this.url);
			if(this.onlyHttps){
				//только защищенное
				if(this.url.indexOf("http://")==0){
					String urlb=this.url.substring(4);
					url= new URL("https"+urlb);
				}
			}

			this.log.log("protocol="+url.getProtocol());


			if(url.getProtocol().equals("http")){
				uc = (HttpURLConnection) url.openConnection();
			}else{
				uc = (HttpsURLConnection) url.openConnection();
			}





			//установка метода запроса GET HEAD POST
			if(this.postData==null){


				uc.setRequestMethod(this.requestMethod);

			}else{

				//нужно отправить данные
				uc.setRequestMethod("POST");

			}

			this.log.log("open connection");


			//=========================================запрос


			this.log.log("Установка параметров");

			//===агент
			uc.setRequestProperty("User-Agent", this.userAgent.agentName);



			//====реферер
			if(this.referer!=null){

				//uc.setRequestProperty("Referer", this.referer);
				this.referer=null;
			}


			//=====================================если нужно чтото загрузить
			this.log.log("Загрузка...");

			if(this.postData!=null){

				thisPercentUpload=0;


				uc.setDoOutput(true);

				this.log.log("передача данных...");


				uc.setRequestProperty("Content-Length", String.valueOf(this.postData.data.length));



				if(postData.contentType==null){
					uc.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.postData.boundary);

				}else{
					uc.setRequestProperty("Content-Type", postData.contentType+"; boundary=" + this.postData.boundary);

				}



				try{

					this.log.log("Открытие выходгного потока");

					os = uc.getOutputStream();


					this.log.log("Запись  впоток..");

					if(postData.data.length>10000){
						//загрузим кусками
						int chank=(int) ((double)postData.data.length/100d);
						int offset=0;
						while(true){
							if(offset<postData.data.length){

								if(offset+chank>postData.data.length){
									chank=postData.data.length-offset;
								}
								os.write(postData.data, offset, chank);

								offset+=chank;



								thisPercentUpload=(int) (100f*((double)offset/(double)postData.data.length));
								try{
									//os.flush();
									Thread.sleep(1);
									Thread.currentThread().notifyAll();
								}catch(Exception e){

								}

								dispathPercent();

							}else{

								break;
							}

						}


					}else{
						//загружаем всё сразу
						os.write(postData.data);
						thisPercentUpload=100;

					}

					dispathPercent();




				}catch(IOException e){
					this.log.log("ошибка передачи данных\n"+e.toString());

				}

				this.postData=null;

			}else{
				uc.setDoOutput(false);
			}



			//==========================================ответ

			int responceCode=uc.getResponseCode();



			this.log.log("Ответ сервера "+responceCode);


			String fields="";
			for(int i=0;i<1000;i++){
				String fi=uc.getHeaderField(i);
				if(fi==null){
					break;
				}else{

					fields+="\n"+fi;
				}

			}

			this.log.log("Поля ответа=====\n"+fields);


			String location = uc.getHeaderField("Location");
			String locationContent = uc.getHeaderField("Content-Location");

			this.fieldContentType = uc.getContentType();
			this.fieldContentLength=uc.getHeaderField("Content-Length");
			this.fieldContentEncoding=uc.getHeaderField("Content-Encoding");
			this.fieldLastModified=uc.getHeaderField("Last-Modified");
			this.fieldVia=uc.getHeaderField("Via");
			this.fieldServer=uc.getHeaderField("Server");
			this.fieldAllow=uc.getHeaderField("Allow");

			if(responceCode== HttpURLConnection.HTTP_OK){



				if (location != null) {
					// перенаправление

					this.load(location);

					return;

				} else {


					this.openInputStream(in, uc);

					//ставим что данные загружены, если вверху будет исключение то это не выполнится
					loaded=true;

				}
			}else{


			}



			if(responceCode== HttpURLConnection.HTTP_CREATED ||
					responceCode== HttpURLConnection.HTTP_MOVED_TEMP ||
					responceCode== HttpURLConnection.HTTP_MOVED_PERM

			){


			}




			if (location != null) {
				// перенаправление

				this.log.log("пренаправление");


				this.load(location);


			}else{

				if(responceCode==405){

					if(this.fieldAllow!=null){

						//выбрать нужный метод


						this.listener.onEmptyHttpLoader(this);

					}else{
						//ошибка, сервер должен был дать список методов

						this.log.log("Ошибка, сервер не дал методов");

						if(loaded){
							//было загружено чтото

							this.listener.onLoadHttpLoader(this);


						}else{
							//ничего не загружено

							this.listener.onEmptyHttpLoader(this);


						}
					}

				}else{

					//перенаправления нету
					//загрузка завершена

					if(locationContent!=null && loaded==false){
						//ничего не загрузили. и есть вариант контент в другом месте
						//загрузим его


						this.load(locationContent);

					}else{

						if(loaded){
							//было загружено чтото

							this.listener.onLoadHttpLoader(this);



						}else{
							//ничего не загружено


							this.listener.onEmptyHttpLoader(this);


						}
					}
				}
			}

		} catch (Exception e) {


			//=====================разбор какая ошибка
			if(e instanceof UnknownHostException){
				this.errorClass=this.ERORR_CLASS_UnknownHostException;
			}
			if(e instanceof BindException){
				this.errorClass=this.ERORR_CLASS_BindException;
			}
			if(e instanceof ConnectException){
				this.errorClass=this.ERORR_CLASS_ConnectException;
			}

			if(e instanceof HttpRetryException){
				this.errorClass=this.ERORR_CLASS_HttpRetryException;
			}
			if(e instanceof MalformedURLException){
				this.errorClass=this.ERORR_CLASS_MalformedURLException;
			}
			if(e instanceof NoRouteToHostException){
				this.errorClass=this.ERORR_CLASS_NoRouteToHostException;
			}
			if(e instanceof PortUnreachableException){
				this.errorClass=this.ERORR_CLASS_PortUnreachableException;
			}
			if(e instanceof ProtocolException){
				this.errorClass=this.ERORR_CLASS_ProtocolException;
			}
			if(e instanceof SocketException){
				this.errorClass=this.ERORR_CLASS_SocketException;
			}
			if(e instanceof SocketTimeoutException){
				this.errorClass=this.ERORR_CLASS_SocketTimeoutException;
			}
			if(e instanceof UnknownHostException){
				this.errorClass=this.ERORR_CLASS_UnknownHostException;
			}
			if(e instanceof UnknownServiceException){
				this.errorClass=this.ERORR_CLASS_UnknownServiceException;
			}
			if(e instanceof URISyntaxException){
				this.errorClass=this.ERORR_CLASS_URISyntaxException;
			}



			//==============================лог
			this.log.log("\nОшибка\n"+e.toString());


			//===========================вывод калбака


			if (this.listener != null) {

				this.listener.onErrorHttpLoader(this);

			}

		} finally {
			if (in != null) {

				try {
					if (in != null) {
						in.close();
					}
					if (uc != null) {
						uc.disconnect();
					}
				} catch (IOException e) {

				}

			}



		}



		this.load=false;

	}

	private void dispathPercent(){
		if(listener!=null){

			this.listener.onPercentUploadHttpLoadr(this);

		}
	}

	/**
	 * для пулучения данных реализовать метод
	 * @param in
	 * @param uc
	 * @throws IOException
	 */
	protected void openInputStream(InputStream in, HttpURLConnection uc) throws IOException {

		this.log.log("Получены данные "+fieldContentType);

		if(maxBytes!=-2){
			//==========открытие входного потока
			in = new BufferedInputStream(uc.getInputStream());

			if(this.maxBytes==-1){
				this.readInputStream(in);
			}else{
				this.readInputStream(in,maxBytes);
			}

			maxBytes=-1;
		}

		maxBytes=-1;

		if(loadedByte!=null){
			this.log.log("\nЗагружено "+loadedByte.length+" б");
		}
	}

	/**
	 * Для стения данных  с потока реализовать метод
	 * @param is
	 * @throws IOException
	 */
	protected void readInputStream(InputStream is) throws IOException {

		ByteArrayOutputStream bb = new ByteArrayOutputStream();

		boolean re = true;
		while (re) {
			int by = is.read();
			if (by != -1) {
				this.thisReaded++;
				bb.write(by);
			} else {
				re = false;
			}

		}

		this.loadedByte = bb.toByteArray();
	}

	/**
	 * Для стения данных  с потока реализовать метод
	 * @param is
	 * @throws IOException
	 */
	protected void readInputStream(InputStream is, int maxBytes) throws IOException {

		ByteArrayOutputStream bb = new ByteArrayOutputStream();

		boolean re = true;
		int readed=0;
		while (re && maxBytes>readed) {
			int by = is.read();
			readed++;
			if (by != -1) {
				this.thisReaded++;
				bb.write(by);
			} else {
				re = false;
			}

		}

		this.loadedByte = bb.toByteArray();
	}


	public class Logger{


		/**
		 * Лог за всё время
		 */
		public String logGlobal="";
		/**
		 * Лог текущего круга
		 */
		public String logLocal="";
		/**
		 * ЧТо делается в данные момент
		 */
		public String logThis="";

		private IHttpLoaderListener listener;

		public boolean logging=true;

		public Logger(IHttpLoaderListener listener){


			this.listener=listener;
		}

		public void log(String str){


			if(this.logging){


				this.logGlobal+="\n"+str;
				this.logLocal+="\n"+str;
				this.logThis=str;

				//=======обрезка глобального лога
				if(this.logGlobal.length()>50000){
					this.logGlobal=this.logGlobal.substring(0, 50000);
				}


				listener.onLogHttpLoader(HttpLoader.this);

			}

		}



	}

	/**
	 * Интерфейс для оповещания состояния
	 *
	 * @author 1
	 *
	 */
	public interface IHttpLoaderListener {



		public void onLoadHttpLoader(HttpLoader l);

		public void onErrorHttpLoader(HttpLoader l);

		public void onLogHttpLoader(HttpLoader l);

		/**
		 * метод недопускается для запроса к серверу, смотрите какие допускаются
		 * @param l
		 */
		public void onNotAllowedHttpLoader(HttpLoader l);
		/**
		 * сервер не дал список методов, таких как POST HEAD
		 */
		public void onNotMethodHttpLoader(HttpLoader l);

		/**
		 * Ничего не загрузили
		 * @param l
		 */
		public void onEmptyHttpLoader(HttpLoader l);

		/**
		 * Когда меняется процент загрузки
		 */
		public void onPercentUploadHttpLoadr(HttpLoader l);

	}
}

