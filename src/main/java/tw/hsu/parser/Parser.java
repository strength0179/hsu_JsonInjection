package tw.hsu.parser;

import android.view.View;

/**
 * Created by HsuWeiyen on 2021/1/20.
 */

public interface Parser<T> {
    public boolean isTarget(View child);

    public void exec(View child, String[] tags, int index, T data) ;
}
