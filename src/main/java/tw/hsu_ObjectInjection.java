package tw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import tw.hsu.android.adapter.ObjectInjector;
import tw.hsu.parser.ListObjParser;
import tw.hsu.parser.Parser;


/**
 *
 * Object版
 * 
 * @author Weiyen
 *
 */
public class hsu_ObjectInjection extends hsu_Injector<Object> implements ObjectInjector, GetField {


	public hsu_ObjectInjection(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		parsers = new ArrayList<>();


		parsers.add(new Parser<Object>() {
			@Override
			public boolean isTarget(View child) {
				return (child instanceof TextView);
			}

			@Override
			public void exec(View child, String[] tags, int index, Object data) {
				Field f = getField(tags);


				if (f != null ) {


					try {
//						Log.d("HsuDemo", "TextView got Field " + f.getType().toString());
						String jData = "";
						if (f.getType() == String.class) {
							jData = (String) f.get(data);
							if (index < tags.length - 1) {
								if (tags[index + 1].equals("Html")) {
									jData = jData.replaceAll("<p>", "");
									jData = jData.replaceAll("</p>", "\r\n");
								}
							}
						}
						else if (f.getType().toString().equals("long")) {
							jData = Long.toString(f.getLong(data));
						}
						else if (f.getType().toString().startsWith("int")) {
							jData = Integer.toString(f.getInt(data));
						}
						else if (f.getType().toString().startsWith("float")) {
							jData = Float.toString(f.getFloat(data));
						}
						else if (f.getType().toString().startsWith("double")) {
							jData = Double.toString(f.getDouble(data));
						}
						else if (f.getType() == Long.class) {
							jData = Long.toString((long)f.get(data));
						}
						else if (f.getType() == Integer.class) {
							jData = Integer.toString((int)f.get(data));
						}
						else if (f.getType() == Float.class) {
							jData = Float.toString((float)f.get(data));
						}
						else if (f.getType() == Double.class) {
							jData = Double.toString((double)f.get(data));
						}


						if (child instanceof EditText) {
							((EditText) child).setHint(jData);
						} else {
							((TextView) child).setText(jData);
						}


					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});


		parsers.add(new Parser<Object>() {
			@Override
			public boolean isTarget(View child) {
				return (child instanceof ImageView);
			}

			@Override
			public void exec(View child, String[] tags, int index, Object data) {
				Field f = getField(tags);

				if(f != null && f.getType() == String.class) {
					try {
						Glide.with(getContext()).load(f.get(data)).into((ImageView)child);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});

		parsers.add(new ListObjParser(this));

	}


	@Override
	public void setObject(Object js) {
		// TODO Auto-generated method stub

		checkHolder();
		ViewGroup parent = (ViewGroup) getParent();

		search(parent, js);

		setHolder();

	}


	Field[] fields;


	@Override
	public Field getField(String[] tag){
		for(int i = 0 ; i < tags.length ; i++){
			for(int j = 0 ; j < fields.length ; j++){
				if(fields[j].getName().equals(tags[i]))
					return fields[j];
			}
		}

		return null;
	}

	@Override
	public int hasData(Object data, String[] tags) {

		if(fields == null) {
			fields = data.getClass().getDeclaredFields();

		}

		for(int i = 0 ; i < tags.length ; i++){
			for(int j = 0 ; j < fields.length ; j++){
				if(fields[j].getName().equals(tags[i])) {
					return i;
				}
			}
		}
		return super.hasData(data, tags);
	}

}
