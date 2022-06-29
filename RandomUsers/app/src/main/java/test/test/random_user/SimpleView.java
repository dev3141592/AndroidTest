package test.test.random_user;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SimpleView implements View.OnClickListener {

	public RelativeLayout rootView;
	public RelativeLayout viewBlock;
	
	protected MainActivity act;

	
	public SimpleView(MainActivity act, int resId){
		
		this.act=act;
		
		
		this.rootView= (RelativeLayout) LayoutInflater.from(act).inflate(resId, null);
		this.viewBlock= (RelativeLayout) LayoutInflater.from(act).inflate(R.layout.view_block, null);


		viewBlock.setOnClickListener(this);


	}

	public SimpleView() {
	}


	public void setFrontView(View v){
		if(v.getParent()==null){
			int countChildren=this.rootView.getChildCount();
			this.rootView.addView(v,countChildren);
		}
	}
	
	public void removeFrontView(View v){
		try{
		    this.rootView.removeView(v);
		}catch(Exception e){
			
		}
	}


	/**
	 * Заблокувати це відображення прогрессом
	 */
	public void block(){

		setFrontView(viewBlock);
	}

	/**
	 * Розблокувати це відображення від прогресса
	 */
	public void unBlock(){

		removeFrontView(viewBlock);
	}
	
	public void blockOtherThread(){
		this.act.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				block();
			}});
		
	}
	
	public void unBlockOtherThread(){
		this.act.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				unBlock();
			}});
		
	}

	@Override
	public void onClick(View view) {

	}


	interface ISimpleView{
		/**
		 * Вызывается когда вьюшка создана
		 * @param sv
		 */
		public void onCreateSimpleView(SimpleView sv);
	}

	

	

	
	
}
