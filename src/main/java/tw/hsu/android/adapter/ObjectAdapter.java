package tw.hsu.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by HsuWeiyen on 2021/1/21.
 */

public class ObjectAdapter extends ArrayAdapter {

    int textViewResourceId;
    class ViewHolder{
        public TextView injector;
    }

    public ObjectAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

        ViewHolder holder = null;
        if(convertView.getTag() != null){
            holder = (ViewHolder) convertView.getTag();
        }
        else{
            holder = new ViewHolder();
            holder.injector = convertView.findViewById(textViewResourceId);
        }

        Object item = getItem(position);
        ((ObjectInjector)holder.injector).setObject(item);

        return convertView;
    }
}
