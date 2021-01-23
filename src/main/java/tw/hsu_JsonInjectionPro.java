package tw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 
 * TextView上設置Tag，則會讀取Tag對應的欄位內的值，然後呼叫setText並傳入值
 * 
 * ImageView如果要讀取JSon中傳入的檔案路徑，請先設置一個View然後在View中設Tag
 * (XML檔中，View在ImageView之前/之上。ImageView本身反而不要設定Tag，會導致Glide元件錯誤。)
 * 
 * ViewGroup/Layout如果有tag，則是進行Visibility的Gone/Visible的切換。
 *
 * 
 * @201807261543
 * ViewGroup的規則判斷改為使用Key:Value的方式
 * [isNull][isEmpty][isNullOrEmpty][isTrue][isFalse]
 * 如果為True，就將ViewGroup隱藏
 * 
 * @201808131011
 * 因為這個功能只需要Ta所ㄧg中的一段所以不需要連續完整掃描整段Tag
 * 所以改成精準找出Tag內容即可
 * 
 * @201808281100
 * 新增功能：個別TextView還有自己解析JSon的方法，所以把整個JSON輸入
 * 
 * @author Weiyen
 *
 */
public class hsu_JsonInjectionPro extends TextView {

	Display mDisplay;
	DisplayMetrics metrics;

	public hsu_JsonInjectionPro(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
//		super.setText(text, type);
		
		JSONObject js = null;
		try {
			js = new JSONObject(text.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			System.out.println("ErrorJS |" + text.length() + "|" + text.toString());
//			e.printStackTrace();
		}
		if(js == null) {
//			System.out.println("parse Json Fail. " + text.length() + "|" + text.toString());
			return;
		}
		
		
		ViewGroup parent = (ViewGroup) getParent();
		search(parent, js);
//		System.out.println(text.toString());
	}
	
	final String TagNam = "isNullisEmptyisTrueisFalseIsNullIsEmptyIsTrueIsFalsefullJson";

	String[] tmpData = null;
	String[] tags = null;
	int tagIndex = -1;
	boolean jsonHas = false;
	boolean jsonVisible = true;
	boolean fullJson = false;

	void search(ViewGroup group, JSONObject js) {

		int count = group.getChildCount();
//		System.out.print("s" + count+"s");
		for(int i = 0 ; i < count ; i++) {
//			System.out.print("!");
			View child = group.getChildAt(i);
			
			if(child instanceof hsu_JsonInjectionPro && child != this) {
				return;
			}
			
			tagIndex = -1;
			jsonHas = false;
			jsonVisible = true;
			
			//@201808131011
			if(child.getTag() instanceof String) {
				tags = ((String)child.getTag()).split("\\|");
				
				for(int x = 0 ; x < tags.length ; x++) {
					
					if(js.has(tags[x])) {
						jsonHas = true;
						tagIndex = x;
						x += tags.length;
					}
					else if(TagNam.contains(tags[x])) {
						
						//判斷是否要隱藏這組View
						int state = View.VISIBLE;
						
						try {
							if(tags[x].equals("fullJson")) {
								fullJson = true;
								jsonHas = true;
								tagIndex = x;
								x += tags.length;
							}
							else if(!js.has(tags[x+1])) {
								state = View.GONE;
							}
							else {
								switch (tags[x]) {
								case "isNull":
									if(js.isNull(tags[x+1]))
										state = View.GONE;
									break;
								case "isEmpty":
									if(js.getString(tags[x+1]).isEmpty())
										state = View.GONE;
									break;
								case "isTrue":
									if(js.getBoolean(tags[x+1]))
										state = View.GONE;
									break;
								case "isFalse":
									if(!js.getBoolean(tags[x+1]))
										state = View.GONE;
									break;
								default:
									break;
								}
							}
							
//							System.out.println("TagName contain " + tags[x] + " " + ((state == View.GONE)?"GONE":"VISIBLE"));
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(state  == View.GONE)
							jsonVisible = false;
						
						child.setVisibility(state);
					}
					
				}
				
			}//@201808131011 End
			
			
			if(jsonHas) {
				String tag = tags[tagIndex];
				if(!(child instanceof ViewGroup)) {
//						System.out.print(" "+tag + " ");
					try {
						boolean isNull = js.isNull((String) tag);
						String jData = fullJson?"":js.getString((String) tag);

						if(jData.equals("false") || jData.equals("true")){
							boolean b = Boolean.parseBoolean(jData);
						}
						else if(child instanceof EditText) {
							
							if(child != this) {
								((TextView) child).setText(isNull?"":jData);
								if(!isNull)
									((EditText)child).setHint(jData);
							}
						}
						else if(child instanceof TextView) {
							if(fullJson) {
								((TextView) child).setText(js.toString());
							}
							else {
								if(tagIndex < tags.length - 1) {
									if(tags[tagIndex + 1].equals("Html")) {
										jData = jData.replaceAll("<p>", "");
										jData = jData.replaceAll("</p>", "\r\n");
									}
								}
								((TextView) child).setText(jData);
							}
							
								
						}
						else if(child instanceof View) {
							if(tagIndex < tags.length - 1 && (
									tags[tagIndex+1].equals("Circle")||
									tags[tagIndex+1].equals("CircleCrop"))) {
								tmpData = new String[2];
								tmpData[0] = tag;
								tmpData[1] = tags[tagIndex+1];
							}
							else if(tagIndex < tags.length - 2 && (
									tags[tagIndex+1].equals("Round") || 
									tags[tagIndex+1].equals("RoundW") || 
									tags[tagIndex+1].equals("RoundH"))) {
								tmpData = new String[3];
								tmpData[0] = tag;
								tmpData[1] = tags[tagIndex+1];
								tmpData[2] = tags[tagIndex+2];
							}
							else {
								tmpData = new String[1];
								tmpData[0] = tag;
							}
							
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if(child instanceof ViewGroup && jsonVisible){
					child.setVisibility(View.VISIBLE);
					search((ViewGroup) child, js);
				}
				
			}
			else if(child instanceof ImageView) {
				if(tmpData != null) {
					
					final String[] ts = tmpData;
					tmpData = null;
					try {


						String jData = null;
						for(String t : ts){
							if(js.has(t)){
								jData = js.getString(t);
							}
						}

						Glide.with(getContext()).load(jData).into((ImageView)child);
						tmpData = null;
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				child.setVisibility(View.VISIBLE);
			}
			else if(child instanceof ViewGroup && jsonVisible) {
				child.setVisibility(View.VISIBLE);
				search((ViewGroup) child, js);
			}
			
		}
		
	}


	

}
