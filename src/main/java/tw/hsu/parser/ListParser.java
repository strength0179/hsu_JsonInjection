package tw.hsu.parser;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tw.hsu.android.adapter.RecyclerInjectionAdapter;
import tw.hsu_jsoninjection.R;

/**
 * Created by HsuWeiyen on 2021/1/20.
 */

public class ListParser implements Parser<JSONObject> {
    @Override
    public boolean isTarget(View child) {
        return (child instanceof ListView) || (child instanceof RecyclerView);
    }


    @Override
    public void exec(View child, String[] tags, int index, JSONObject data) {
        try {

            if(data.has(tags[index]) && (data.get(tags[index]) instanceof JSONArray)) {

                String tag1 = null, tag2 = null;

                if(child.getTag(R.id.list_layout) != null && child.getTag(R.id.list_text) != null){
                    tag1 = (String) child.getTag(R.id.list_layout);
                    tag2 = (String) child.getTag(R.id.list_text);

                }
                else if(tags.length >= index + 3){
                    tag1 = tags[index+ 1];
                    tag2 = tags[index+ 2];

                }

                Context context =child.getContext();

                int layoutRes = context.getResources().
                        getIdentifier(tag1, "layout", context.getPackageName());
                if(layoutRes <= 0){
                    Log.w("JsonInjector", "Resource[" + tag1 + "] can't found. Use the correct nane.");
                    return;
                }

                int textRes = context.getResources().
                        getIdentifier(tag2, "id", context.getPackageName());
                if(textRes <= 0){
                    Log.w("JsonInjector", "Resource[" + tag2 + "] can't found. Use the correct nane.");
                    return;
                }


                ArrayList<String> list = new ArrayList<>();
                JSONArray array = data.getJSONArray(tags[index]);
                for(int i = 0 ; i < array.length() ; i++){
                    list.add(array.getString(i));
                }

                if(child instanceof ListView){
                    ((ListView)child).setAdapter(new ArrayAdapter<String>(child.getContext(), layoutRes, textRes, list));
                }
                else {
                    ((RecyclerView)child).setAdapter(new RecyclerInjectionAdapter(child.getContext(), layoutRes, textRes, list));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
