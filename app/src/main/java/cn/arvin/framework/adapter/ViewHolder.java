package cn.arvin.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.arvin.framework.core.image.ImageLoader;


/**
 * @author 23glh  2015-9-21 下午4:58:03
 * @version 1.0
 */
public class ViewHolder {

    private SparseArray<View> mViews;

    private View mConvertView;

    private int mPosition;


    /**
     * 创建与给定的convertView对象相关联的ViewHolder对象
     *
     * @param context      上下文对象
     * @param parent       item布局对象的父容器
     * @param itemLayoutId item布局id
     * @param position     当前item所在的位置
     */

    public ViewHolder(Context context, ViewGroup parent, int itemLayoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(itemLayoutId, parent, false);
        // 给convertView设置tag
        mConvertView.setTag(this);
    }

    /**
     * 获取与给定的convertView对象相关联的ViewHolder对象，如果convertView为null，则创建convertView对象，
     * 并把当前的ViewHolder对象与之绑定
     *
     * @param context      上下文对象
     * @param convertView  listView或者GridView中缓存的item布局对象
     * @param parent       item布局对象的父容器
     * @param itemLayoutId item布局id
     * @param position     当前item所在的位置
     * @return convertView所绑定的ViewHolder对象
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int itemLayoutId, int position) {
        if (convertView == null) {
            ViewHolder viewHolder = new ViewHolder(context, parent, itemLayoutId, position);
            return viewHolder;
        }
        //更新viewHolder中的position
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.setPosition(position);
        return viewHolder;
    }

    /**
     * 获取当前ViewHolder对象绑定的ConvertView对象
     *
     * @return ConvertView对象
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 获取viewHolder绑定的ConvertView所在的位置
     *
     * @return ConvertView的位置，同时也就是ViewHolder设置的控件所在的item位置
     */
    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 在ViewHolder对应的convertView上的控件集合中，通过控件的Id获取对应的控件
     * 如果没有则通过findViewById()在convertView上获取对应view对象并加入mViews
     *
     * @param viewId 控件的id号
     * @return 给定的id号对应的view对象
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**

     * 给convertView上的TextView设置字符串
     *
     * @param viewId TextView的id号
     * @param text   要设置的文本内容
     * @return ViewHolder对象，用于支持对象链的形式调用方法
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm

     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片，可根据mOptions对象相关设置决定加载后图片的显示样式
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByUrl(int viewId, String url) {
        if(!url.startsWith("http")&&!url.startsWith("file")){
            return this;
        }
        ImageView imageView = getView(viewId);
        imageView.setTag(url);
        ImageLoader.showImage(url, imageView);
        return this;
    }

    /**
     * 给指定的控件设置单击监听
     *
     * @param viewId
     * @param listener
     * @return ViewHolder
     * 可以在代码中通过 viewHolder.getView(R.id.btn_login).setOnClickListener(listener)设置
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    /**
     * 设置相应控件是否可见
     *
     * @param viewId 要设置的控件id号
     * @param statu  控件是否可见的状态值：View.VISIBLE = 0,INVISIBLE = 4,GONE = 8
     * @return ViewHolder
     */

    public ViewHolder setVisible(int viewId, int statu) {
        View view = getView(viewId);
        view.setVisibility(statu);
        return this;
    }

    /**
     * 以下方法可以根据我们的需要进行扩展
     * */

	/*
	•setText() Calls setText(String) on any TextView.
	•setAlpha() Calls setAlpha(float) on any View.
	•setVisible() Calls setVisibility(int) on any View.
	•linkify() Calls Linkify.addLinks(view, ALL) on any TextView.
	•setTypeface() Calls setTypeface(Typeface) on any TextView.
	•setProgress() Calls setProgress(int) on any ProgressBar.
	•setMax() Calls setMax(int) on any ProgressBar.
	•setRating() Calls setRating(int) on any RatingBar.
	•setImageResource() Calls setImageResource(int) on any ImageView.
	•setImageDrawable() Calls setImageDrawable(Drawable) on any ImageView.
	•setImageBitmap() Calls setImageBitmap(Bitmap) on any ImageView.
	•setImageUrl() Uses Square's Picasso to download the image and put it in an ImageView.
	•setImageBuilder() Associates a Square's Picasso RequestBuilder to an ImageView.
	•setOnClickListener()
	•setOnTouchListener()
	•setOnLongClickListener()
	•setTag()
	•setChecked()
	•setAdapter()
	*/

}
