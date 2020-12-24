package dc.android.bridge.adapter;

import android.view.View;

public interface IOnItemLongClickListener<T> {
    /**
     *
     * @param t 点击位置的数据类
     * @param v
     */
    void onLongClick(T t, View v);
}
