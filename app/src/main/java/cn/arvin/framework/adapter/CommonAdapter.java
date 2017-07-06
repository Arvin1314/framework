package cn.arvin.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @param <T>
 * @author 23glh  2015-9-21 下午4:57:12
 * @version 1.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> data, int itemLayoutId) {
        mContext = context;
        mData = data;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mData==null?0:mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建convertView对应的viewHolder,并绑定到一起
        final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
        //到这里，viewHolder里面已经保存了与之绑定的convertView上面的控件，
        //这个方法就是讲getItem()返回的对象中数据设置到viewHolder中的控件上,由使用者自己重写
        convert(viewHolder, getItem(position));

        return viewHolder.getConvertView();
    }

    /**
     * 留给子类去重写，通过ViewHolder把View找到，通过Item设置值
     *
     * @param viewHolder
     * @param item
     */
    public abstract void convert(ViewHolder viewHolder, T item);
}
