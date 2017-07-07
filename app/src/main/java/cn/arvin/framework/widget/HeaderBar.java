package cn.arvin.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.arvin.framework.R;


public class HeaderBar extends RelativeLayout {

    private Context mContext;

    private View mLeft;

    private View mRight;

    private View mCenterTitle;

    private View mCenterLogo;

    private View mDivider;

    private int mBackgroundRes = R.color.bg_color_white;

    private int mDividerRes = R.color.title_divider;

    private String mLeftText;
    private float mLeftTextSize;
    private int mLeftTextColor;

    private int mLeftDrawableRes;

    private int mLogoRes;

    private String mCenterText;
    private float mCenterTextSize;
    private int mCenterTextColor;

    public HeaderBar(Context context) {
        this(context, null);
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (attrs != null) {
            initAttrs(attrs, defStyleAttr);
        }

        initView();
    }


    private void initAttrs(AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.HeaderBar, defStyleAttr, 0);

        mBackgroundRes = typedArray.getResourceId(R.styleable.HeaderBar_headerBackground, 0);
        mDividerRes = typedArray.getResourceId(R.styleable.HeaderBar_headerDivider, 0);

        mLeftText = typedArray.getString(R.styleable.HeaderBar_headerLeftText);
        mLeftTextSize = typedArray.getDimension(R.styleable.HeaderBar_headerLeftTextSize, 0);
        mLeftTextColor = typedArray.getColor(R.styleable.HeaderBar_headerLeftTextColor, 0);

        mLeftDrawableRes = typedArray.getResourceId(R.styleable.HeaderBar_headerLeftDrawable, 0);

        mCenterText = typedArray.getString(R.styleable.HeaderBar_headerCenterText);
        mCenterTextSize = typedArray.getDimension(R.styleable.HeaderBar_headerCenterTextSize, 0);
        mCenterTextColor = typedArray.getColor(R.styleable.HeaderBar_headerCenterTextColor, 0);
        mLogoRes = typedArray.getResourceId(R.styleable.HeaderBar_headerLogo, 0);

        typedArray.recycle();
    }

    private void initView() {
        View.inflate(mContext, R.layout.common_layout_header, this);

        mLeft = findViewById(R.id.header_leftview);
        mCenterLogo = findViewById(R.id.header_center_logo);
        mCenterTitle = findViewById(R.id.header_center_title);
        mRight = findViewById(R.id.header_rightview);
        mDivider = findViewById(R.id.header_divider);

        this.setBackgroundResource(mBackgroundRes);
        if (mDividerRes == 0) {
            mDivider.setVisibility(View.GONE);
        } else {
            mDivider.setVisibility(View.VISIBLE);
            mDivider.setBackgroundResource(mDividerRes);
        }
        setBackBtnEnable(true);

        ((TextView) mLeft).setText(mLeftText);
        if (mLeftTextSize != 0) {
            ((TextView) mLeft).setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        }
        if (mLeftTextColor != 0) {
            ((TextView) mLeft).setTextColor(mLeftTextColor);
        }
        if (mLeftDrawableRes != 0) {
            Drawable leftDrawable = getResources().getDrawable(mLeftDrawableRes);
            ((TextView) mLeft).setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }
        ((TextView) mCenterTitle).setText(mCenterText);
        if (mCenterTextSize != 0) {
            ((TextView) mCenterTitle).setTextSize(TypedValue.COMPLEX_UNIT_PX, mCenterTextSize);
        }
        if (mCenterTextColor != 0) {
            ((TextView) mCenterTitle).setTextColor(mCenterTextColor);
        }
        if (mLeftDrawableRes != 0) {
            ((ImageView) mCenterLogo).setImageResource(mLeftDrawableRes);
        }
    }


    /**
     * 获取导航标题栏左侧的view
     *
     * @return View对象
     */
    public View getLeftView() {
        //当顶部导航布局文件中没有添加对应id的控件，获取时默认返回下面的ImageButton，箭头图标
        if (mLeft == null) {
            mLeft = new ImageButton(getContext());
            //mLeft.setBackgroundResource(R.drawable.header_back_btn_selector);
            addView(mLeft, getLeftViewParams());
        }
        return mLeft;
    }

    /**
     * 设置导航标题栏左侧的view
     *
     * @param v 要设置的View对象
     */
    public void setLeftView(View v) {
        if (v == null) {
            return;
        }
        if (mLeft != null) {
            removeView(mLeft);
        }

        mLeft = v;
        addView(v, getLeftViewParams());
    }


    /**
     * 获取导航标题栏中间Logo位置的view
     *
     * @return View对象
     */
    public View getCenterLogoView() {
        //当顶部导航布局文件中没有添加对应id的控件，获取中间Logo时默认返回下面的ImageView
        if (mCenterLogo == null) {
            mCenterLogo = new ImageView(getContext());
//            ((ImageView) mCenterLogo).setImageResource(R.mipmap.ic_launcher);
            addView(mCenterLogo, getCenterLogoViewParams());
        }
        return mCenterLogo;
    }

    /**
     * 设置导航标题栏中间Logo位置的view
     *
     * @param v 要设置的View对象
     */
    public void setCenterLogoView(View v) {
        if (v == null) {
            return;
        }
        if (mCenterLogo != null) {
            removeView(mCenterLogo);
        }
        mCenterLogo = v;
        addView(v, getCenterLogoViewParams());
    }


    /**
     * 获取导航标题栏中间title位置的view
     *
     * @return View对象
     */
    public View getCenterTitleView() {
        //当顶部导航布局文件中没有添加对应id的控件，获取中间标题时默认返回下面的TextView
        if (mCenterTitle == null) {
            mCenterTitle = new TextView(getContext());
            addView(mCenterTitle, getCenterTitleViewParams());
        }
        return mCenterTitle;
    }

    /**
     * 设置导航标题栏中间title位置的view
     *
     * @param v 要设置的View对象
     */
    public void setCenterTitleView(View v) {
        if (v == null) {
            return;
        }
        if (mCenterTitle != null) {
            removeView(mCenterTitle);
        }

        mCenterTitle = v;
        addView(v, getCenterTitleViewParams());
    }

    /**
     * 获取导航标题栏右侧位置的view
     *
     * @return View对象
     */
    public View getRightView() {
        //当顶部导航布局文件中没有添加对应id的控件，获取是默认返回下面的button
        if (mRight == null) {
            mRight = new TextView(getContext());
            addView(mRight, getRightViewParams());
        }
        return mRight;
    }

    /**
     * 设置导航标题栏右侧位置的view
     *
     * @param v 要设置的View对象
     */
    public void setRightView(View v) {
        if (v == null) {
            return;
        }

        if (mRight != null) {
            removeView(mRight);
        }

        mRight = v;
        addView(v, getRightViewParams());
    }


    /**
     * 设置导航标题栏的标题内容
     *
     * @param title
     */
    public void setTitle(String title) {
        ((TextView) mCenterTitle).setText(title);
    }

    /**
     * 设置导航标题栏的标题内容
     *
     * @param resId
     */
    public void setTitle(int resId) {
        ((TextView) mCenterTitle).setText(resId);
    }

    /**
     * 设置标题栏中标题左侧的LOGO图标是否显示
     *
     * @param visible
     */
    public void setLogoVisible(boolean visible) {
        mCenterLogo.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 更换LOGO图标
     *
     * @param resId 要更换的图标id
     */
    public void setLogoImage(int resId) {
        ImageView image = (ImageView) mCenterLogo;
        image.setImageResource(resId);
    }

    public void setLogoView(ImageView view) {
        setCenterLogoView(view);
    }

    /**
     * 设置左侧回退按钮是否可用
     *
     * @param enable true 显示并可用 ，false隐藏不可用
     */
    public void setBackBtnEnable(boolean enable) {
        mLeft.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        mLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scanForActivity(getContext()).finish();
            }
        });
    }

    /**
     * 根据context获取activity
     *
     * @param context
     * @return
     */
    private static Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }

    /**
     * 设置左侧回退按钮上显示的文字
     *
     * @param text
     */
    public void setBackBtnText(String text) {
        if (mLeft instanceof TextView) {
            ((TextView) mLeft).setText(text);
        }
    }

    /**
     * 设置右侧按钮是否可用，默认为设置菜单按钮
     *
     * @param enable true 显示并可用 ，false隐藏不可用
     */
    public void setRightViewEnable(boolean enable) {
        if (mRight != null) {
            View menuButton = mRight;
            menuButton.setEnabled(enable);
            menuButton.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 设置导航头布局背景图片
     *
     * @param resid 图片资源id
     */
    protected void setHeaderBarBackground(int resid) {
        setBackgroundResource(resid);
    }

    /**
     * 设置导航头布局背景颜色
     *
     * @param color 背景颜色值
     */
    protected void setHeaderBarBackgroundColor(int color) {
        setBackgroundColor(color);
    }


    private LayoutParams getLeftViewParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(10, 0, 0, 0);
        return params;
    }

    private LayoutParams getCenterTitleViewParams() {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        return params;
    }

    private LayoutParams getCenterLogoViewParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.LEFT_OF, R.id.header_center_title);
        return params;
    }

    private LayoutParams getRightViewParams() {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(0, 0, 10, 0);
        return params;
    }

    public void setDividerVisible(boolean visible) {
        mDivider.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
