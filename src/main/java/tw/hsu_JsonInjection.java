package tw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tw.hsu.parser.ListParser;
import tw.hsu.parser.Parser;
import tw.hsu_jsoninjection.R;


/**
 *
 *  JSON版
 * 
 * @author Weiyen
 *
 */
public class hsu_JsonInjection extends hsu_Injector<JSONObject> {

	static Parser Parser0 = new Parser<JSONObject>() {
		@Override
		public boolean isTarget(View child) {
			return (child instanceof TextView);
		}

		@Override
		public void exec(View child, String[] tags, int index, JSONObject data) {
			if(!(child instanceof hsu_JsonInjection) && data.has(tags[index])) {
				try {
					String jData = data.getString(tags[index]);
					if(index < tags.length - 1) {
						if(tags[index + 1].equals("Html")) {
							jData = jData.replaceAll("<p>", "");
							jData = jData.replaceAll("</p>", "\r\n");
						}
					}
					if(child instanceof EditText){
						((EditText)child).setHint(jData);
					}
					else{
						((TextView) child).setText(jData);
					}


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	static Parser Parser1 = new Parser<JSONObject>() {
		@Override
		public boolean isTarget(View child) {
			return (child instanceof ImageView);
		}

		@Override
		public void exec(View child, String[] tags, int index, JSONObject data) {
			if(data.has(tags[index])) {
				try {
					Glide.with(child.getContext()).load(data.getString(tags[index])).into((ImageView)child);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	static Parser Parser2 = new ListParser();


	public hsu_JsonInjection(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		parsers = new ArrayList<>();

		parsers.add(Parser0);

		parsers.add(Parser1);

		parsers.add(Parser2);

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
			return;
		}

		checkHolder();

		ViewGroup parent = (ViewGroup) getParent();
		search(parent, js);

		setHolder();

	}


	@Override
	public int hasData(JSONObject data, String[] tags) {
		for(int i = 0 ; i < tags.length ; i++){
			if(data.has(tags[i]))
				return i;
		}
		return super.hasData(data, tags);
	}

}
