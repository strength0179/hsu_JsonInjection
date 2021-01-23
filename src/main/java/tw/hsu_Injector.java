package tw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import tw.hsu.parser.Parser;


/**
 *
 * 基礎功能
 * 
 * @author Weiyen
 *
 */
public class hsu_Injector<T> extends TextView {

	public hsu_Injector(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		Log.d("HsuDemo", getClass().getName() + ":onAttachedToWindow");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
//		Log.d("HsuDemo", getClass().getName() + ":onDetachedFromWindow");
	}

	public ArrayList<ViewHolder> tmp;
	public Object[] holders;
	public ArrayList<Parser> parsers;
	

	String[] tags = null;
	int tagIndex = -1;
	boolean jsonHas = false;

	public int hasData(T data, String[] tags){
		return -1;
	}

	public void checkHolder(){
		if(getTag() == null){
			tmp = new ArrayList<>();
		}
		else{
			holders = (Object[]) getTag();
		}
	}

	public void setHolder(){
		if(tmp != null && tmp.size() > 0){
			holders = new Object[tmp.size()];
			tmp.toArray(holders);
			tmp.clear();
			tmp = null;
			setTag(holders);
		}
	}

	void search(ViewGroup group, T js) {

		int count = (holders!= null)?holders.length:group.getChildCount();

//		System.out.print("s" + count+"s");
		for(int i = 0 ; i < count ; i++) {
//			System.out.print("!");
			View child = ((holders != null)?((ViewHolder)holders[i]).lead:group.getChildAt(i));
			
			if(child instanceof hsu_Injector) {
				continue;
			}
			
			tagIndex = -1;
			jsonHas = false;

			if(child.getTag() instanceof String) {
				tags = ((String)child.getTag()).split("\\|");

				for(int x = 0 ; x < tags.length ; x++) {
					tagIndex = hasData(js, tags);
					if(tagIndex != -1){
						jsonHas = true;
						x += tags.length;
					}

				}
				
			}

			if(jsonHas) {
				if(holders == null){
					tmp.add(new ViewHolder(child));
				}

				for(Parser parser : parsers){

					if(parser.isTarget(child)){
						parser.exec(child, tags, tagIndex, js);
					}

				}

			}

			if(child instanceof ViewGroup && holders == null) {
				child.setVisibility(View.VISIBLE);
				search((ViewGroup) child, js);
			}
			
		}
		
	}


	public class ViewHolder{
		View lead;
		View team;

		public ViewHolder(View lead){
			this.lead = lead;
		}
	}

}
