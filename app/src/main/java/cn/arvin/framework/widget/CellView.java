package cn.arvin.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.arvin.framework.R;

public class CellView extends RelativeLayout {

  private View mTopDivider;

  //icon和name不共用一个TextView是因为drawableLeft无法调整icon的位置
  private ImageView mIcon;

  private TextView mName;

  private TextView mValue;

  private ImageView mNext;

  private View mBottomDivider;

  private int mResIcon;
  private int mResNext;
  private float mMarginLeft;
  private float mMarginRight;

  private float mContentMarginLeft;
  private float mContentMarginRight;

  private CharSequence mNameText;
  private CharSequence mValueText;

  private int mNameTextColor;
  private float mNameTextSize;
  private int mNameBackground;

  private int mValueTextColor;
  private float mValueTextSize;
  private int mValueBackground;

  private int mResDividerTop;
  private int mResDividerBottom;

  private float mDividerHeight = 1f;
  private float mDividerTopHeight;
  private float mDividerBottomHeight;

  public CellView(Context context) {
    this(context, null);
  }

  public CellView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    if (attrs != null) {
      initAttrs(context, attrs, defStyleAttr);
    }

    setup(context);

    init();
  }

  private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
    TypedArray typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.CellView, defStyleAttr, 0);

    mResIcon = typedArray.getResourceId(R.styleable.CellView_icon, 0);
    mResNext = typedArray.getResourceId(R.styleable.CellView_next, 0);
    mMarginLeft = typedArray.getDimension(R.styleable.CellView_marginLeft, 0);
    mMarginRight = typedArray.getDimension(R.styleable.CellView_marginRight, 0);

    mContentMarginLeft = typedArray.getDimension(R.styleable.CellView_contentMarginLeft, 0);
    mContentMarginRight = typedArray.getDimension(R.styleable.CellView_contentMarginRight, 0);

    mNameText = typedArray.getString(R.styleable.CellView_nameText);
    mValueText = typedArray.getString(R.styleable.CellView_valueText);

    mNameTextColor = typedArray.getColor(R.styleable.CellView_nameTextColor, 0);
    mNameTextSize = typedArray.getDimension(R.styleable.CellView_nameTextSize, 0);
    mNameBackground = typedArray.getResourceId(R.styleable.CellView_nameBackground, 0);

    mValueTextColor = typedArray.getColor(R.styleable.CellView_valueTextColor, 0);
    mValueTextSize = typedArray.getDimension(R.styleable.CellView_valueTextSize, 0);
    mValueBackground = typedArray.getResourceId(R.styleable.CellView_valueBackground, 0);

    mResDividerTop = typedArray.getResourceId(R.styleable.CellView_dividerTop, 0);
    mResDividerBottom = typedArray.getResourceId(R.styleable.CellView_dividerBottom, 0);

    mDividerHeight *= context.getResources().getDisplayMetrics().density;
    mDividerHeight = typedArray.getDimension(R.styleable.CellView_dividerHeight, mDividerHeight);
    mDividerTopHeight =
        typedArray.getDimension(R.styleable.CellView_dividerTopHeight, mDividerHeight);
    mDividerBottomHeight =
        typedArray.getDimension(R.styleable.CellView_dividerBottomHeight, mDividerHeight);

    typedArray.recycle();
  }

  private void setup(Context context) {
    LayoutInflater mInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    mInflater.inflate(R.layout.common_layout_cell, this, true);

    mIcon = (ImageView) findViewById(R.id.cell_icon);
    mName = (TextView) findViewById(R.id.cell_name);
    mValue = (TextView) findViewById(R.id.cell_value);
    mNext = (ImageView) findViewById(R.id.cell_next);
    mTopDivider = findViewById(R.id.cell_divider_top);
    mBottomDivider = findViewById(R.id.cell_divider_bottom);
  }

  private void init() {
    mIcon.setImageResource(mResIcon);
    LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();
    iconParams.setMargins((int) mMarginLeft, 0, 0, 0);
    mIcon.setLayoutParams(iconParams);

    mNext.setImageResource(mResNext);
    LayoutParams nextParams = (LayoutParams) mNext.getLayoutParams();
    nextParams.setMargins(0, 0, (int) mMarginRight, 0);
    mNext.setLayoutParams(nextParams);

    mName.setText(mNameText);
    LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams) mName.getLayoutParams();
    nameParams.setMargins((int) mContentMarginLeft, 0, 0, 0);
    mName.setLayoutParams(nameParams);
    mName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNameTextSize);
    mName.setTextColor(mNameTextColor);
    mName.setBackgroundResource(mNameBackground);

    mValue.setText(mValueText);
    LinearLayout.LayoutParams valueParams = (LinearLayout.LayoutParams) mValue.getLayoutParams();
    valueParams.setMargins(0, 0, (int) mContentMarginRight, 0);
    mValue.setLayoutParams(valueParams);
    mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mValueTextSize);
    mValue.setTextColor(mValueTextColor);
    mValue.setBackgroundResource(mValueBackground);

    mTopDivider.setBackgroundResource(mResDividerTop);
    ViewGroup.LayoutParams topDividerParams = mTopDivider.getLayoutParams();
    topDividerParams.height = (int) mDividerTopHeight;
    mTopDivider.setLayoutParams(topDividerParams);
    mBottomDivider.setBackgroundResource(mResDividerBottom);
    ViewGroup.LayoutParams bottomDividerParams = mBottomDivider.getLayoutParams();
    bottomDividerParams.height = (int) mDividerBottomHeight;
    mBottomDivider.setLayoutParams(bottomDividerParams);
  }

  /**
   * 设置CellView的icon
   */
  public void setIcon(int resIcon) {
    mResIcon = resIcon;
    mIcon.setImageResource(resIcon);
  }

  /**
   * 获取CellView的name
   */
  public String getName() {
    return (String) mNameText;
  }

  /**
   * 设置CellView的name
   */
  public void setName(CharSequence name) {
    mNameText = name;
    mName.setText(name);
  }

  /**
   * 获取CellView的value
   */
  public String getValue() {
    return (String) mValueText;
  }

  /**
   * 设置CellView的value
   */
  public void setValue(CharSequence value) {
    mValueText = value;
    mValue.setText(value);
  }

  public void setValueTextColor(int color) {
    mValue.setTextColor(color);
  }

  /**
   * 设置CellView的next
   */
  public void setNext(int resNext) {
    mResNext = resNext;
    mNext.setImageResource(resNext);
  }
}
