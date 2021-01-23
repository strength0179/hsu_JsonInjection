package tw.hsu.observe;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;

/**
 * Created by HsuWeiyen on 2021/1/21.
 */

public class Observe {

    static Observe Obs;
    public static Observe GetObserve(){
        if(Obs == null)
            Obs = new Observe();

        return Obs;
    }




    public LifecycleObserver getLifecycler(Context context){

        for(LifecyclerHolder holder : holderArrayList){
            if(holder.context == context){
                return holder.lifecycleObserver;
            }
        }

        LifecyclerHolder holder = new LifecyclerHolder();
        holder.context = context;
        holder.lifecycleObserver = new LifecycleObserver() {


        };

        holderArrayList.add(holder);
        return  holder.lifecycleObserver;
    }

    ArrayList<LifecyclerHolder> holderArrayList = new ArrayList<>();
    LifecycleObserver lifecycler;

    class LifecyclerHolder{
        Context context;
        LifecycleObserver lifecycleObserver;
    }

    static int ID = 0;
    class InjectorLifeCycler implements LifecycleObserver {

        int id = ID++;

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            Log.d("HsuDemo", id + ".onResume called");
        }
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            Log.d("HsuDemo", id + ".onPause called");
        }
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            Log.d("HsuDemo", id + ".onDestroy called");
        }
    }


}
