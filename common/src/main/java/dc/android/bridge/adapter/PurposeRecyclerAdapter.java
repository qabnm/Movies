package dc.android.bridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PurposeRecyclerAdapter<T> extends RecyclerView.Adapter<PurposeRecyclerAdapter.PurposeViewHolder<T>> {
    protected Context context;
    /**
     * 布局资源
     */
    protected int layoutId;
    /**
     * 子类
     */
    private Class<? extends PurposeViewHolder<T>> viewHolderClass;

    /**
     * 填充数据
     */
    private List<T> data = new ArrayList<>();

    private IOnItemClickListener<T> listenerClick;
    private IOnItemLongClickListener<T> longClick;

    /**
     * 这个构造方法配合下面的set使用
     */
    public PurposeRecyclerAdapter() {
    }

    public PurposeRecyclerAdapter(Class<? extends PurposeViewHolder<T>> viewHolderClass) {
        setViewHolderClass(viewHolderClass);
    }

    public PurposeRecyclerAdapter(Class<? extends PurposeViewHolder<T>> viewHolderClass, int layoutId) {
        setViewHolderClass(viewHolderClass, layoutId);
    }

    /**
     * 初始化时候集合有数据可以使用这个
     *
     * @param viewHolderClass
     * @param data
     */
    public PurposeRecyclerAdapter(Class<? extends PurposeViewHolder<T>> viewHolderClass, List<T> data) {
        this.viewHolderClass = viewHolderClass;
        this.data = data;
    }

    public void setViewHolderClass(Class<? extends PurposeViewHolder<T>> viewHolderClass) {
        this.viewHolderClass = viewHolderClass;
        this.layoutId = viewHolderClass.getAnnotation(RecyclerItemViewId.class).value();
    }

    /**
     * 去annotation
     *
     * @param viewHolderClass
     * @param layoutId
     */
    public void setViewHolderClass(Class<? extends PurposeViewHolder<T>> viewHolderClass, int layoutId) {
        this.viewHolderClass = viewHolderClass;
        this.layoutId = layoutId;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public IOnItemClickListener<T> getListenerClick() {
        return listenerClick;
    }

    public IOnItemLongClickListener<T> getLongClick() {
        return longClick;
    }

    public void setListenerClick(IOnItemClickListener<T> listenerClick) {
        this.listenerClick = listenerClick;
    }

    public void setLongClick(IOnItemLongClickListener<T> longClick) {
        this.longClick = longClick;
    }

    public void add(T data, int position) {
        this.data.add(position, data);
        notifyItemInserted(position);
    }

    public void add(T data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    public void add(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void update(int position, T data) {
        this.data.set(position, data);
        notifyItemChanged(position);
    }

    public void update(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public T getDetailBean(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public PurposeViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PurposeViewHolder<T> viewHolder = null;
        if (null == context) context = parent.getContext();
        try {
            View convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            viewHolder = viewHolderClass.getConstructor(View.class).newInstance(convertView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurposeViewHolder<T> holder, int position) {
        holder.convert(data.get(position), this, context, position);
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public abstract static class PurposeViewHolder<T> extends RecyclerView.ViewHolder {
        public PurposeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void convert(T bean, PurposeRecyclerAdapter<T> adapter, Context context, int position);
    }
}
