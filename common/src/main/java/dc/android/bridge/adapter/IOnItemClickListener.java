package dc.android.bridge.adapter;

import android.view.View;

public interface IOnItemClickListener<T> {
    void onClick(T t, View v);
}
