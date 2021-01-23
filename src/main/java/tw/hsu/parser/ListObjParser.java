package tw.hsu.parser;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import tw.GetField;
import tw.hsu.android.adapter.ObjectAdapter;
import tw.hsu.android.adapter.RecyclerInjectionAdapter;
import tw.hsu_jsoninjection.R;

/**
 * Created by HsuWeiyen on 2021/1/20.
 */

public class ListObjParser implements Parser<Object> {

    GetField getFielder;
    public ListObjParser(GetField getFielder){
        this.getFielder = getFielder;
    }

    @Override
    public boolean isTarget(View child) {
        return (child instanceof ListView) || (child instanceof RecyclerView);
    }


    @Override
    public void exec(View child, String[] tags, int index, Object data) {
        try {


            Field field = getFielder.getField(tags);

            if(field != null && field.getType().isArray()) {

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
                    Log.w("ObjectInjector", "Resource[" + tag1 + "] can't found. Use the correct nane.");
                    return;
                }

                int textRes = context.getResources().
                        getIdentifier(tag2, "id", context.getPackageName());
                if(textRes <= 0){
                    Log.w("ObjectInjector", "Resource[" + tag2 + "] can't found. Use the correct nane.");
                    return;
                }


                ArrayList<Object> list = new ArrayList<>();

                Object array = field.get(data);
                int length = Array.getLength(array);
                for(int i = 0 ; i < length ; i++){
                    Object unit = Array.get(array, i);
                    list.add(unit);
                }

                if(child instanceof ListView){
                    ((ListView)child).setAdapter(new ObjectAdapter(child.getContext(), layoutRes, textRes, list));
                }
                else {
                    ((RecyclerView)child).setAdapter(new RecyclerInjectionAdapter(child.getContext(), layoutRes, textRes, list));
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
