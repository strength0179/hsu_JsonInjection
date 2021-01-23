package tw.hsu.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tw.hsu_ObjectInjection;

/**
 * Created by HsuWeiyen on 2021/1/12.
 */

public class RecyclerInjectionAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    Context context;
    int layoutid, id;
    ArrayList<T> list;
    public RecyclerInjectionAdapter(Context context, int layoutid, int id, ArrayList<T> list){
        this.context = context;
        this.layoutid = layoutid;
        this.id = id;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(context).inflate(layoutid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.setData(list.get(position), id, new ItemClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public T getItem(int position){
        return list.get(position);
    }

    OnItemClickListener itemClick;
    public void setOnItemClickListener(OnItemClickListener itemClick){
        this.itemClick = itemClick;
    }

    class ItemClick implements View.OnClickListener{
        int position;

        ItemClick(int i){
            position = i;
        }


        @Override
        public void onClick(View view) {
            if(itemClick != null){
                itemClick.onItemClick(RecyclerInjectionAdapter.this, view, position);
            }
        }
    }

    public static interface OnItemClickListener{
        /**
         *
         * @param parent 可以從這裡直接呼叫getItem取得資料
         * @param view
         * @param position
         */
        public void onItemClick(RecyclerInjectionAdapter parent, View view, int position);
    }

}


class RecyclerHolder<T> extends RecyclerView.ViewHolder{


    public RecyclerHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setData(T data, int id, View.OnClickListener click){
        View injector = itemView.findViewById(id);

        if(injector instanceof hsu_ObjectInjection){
            ((hsu_ObjectInjection)injector).setObject(data);
        }
        else{
            ((TextView)injector).setText(data.toString());
        }

        itemView.setOnClickListener(click);
    }

}
