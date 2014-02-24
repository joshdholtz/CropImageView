package com.joshholtz.cropimageview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CropImageView extends FrameLayout {
	
	private final static int HEIGHT_OF_DRAGGER = 30;
	private final static int INITIAL_MARGIN_OF_DRAGGER = 60;
	
	private ImageView mImageView;
	private ImageView mTopLeftDragger;
	private ImageView mBottomRightDragger;
	
	public CropImageView(Context context) {
		super(context);
		init();
	}
	
	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		if (mImageView == null) {
			mImageView = new ImageView(this.getContext());
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			this.addView(mImageView, params);
		}
		
	}
	
	@Override
	protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
		super.onLayout(changed, left, top, right, bottom);
		
		if (mTopLeftDragger == null) {
			mTopLeftDragger = new ImageView(this.getContext());
			mTopLeftDragger.setImageDrawable(this.getCircleDrawable());
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(INITIAL_MARGIN_OF_DRAGGER, INITIAL_MARGIN_OF_DRAGGER, 0, 0);
			this.addView(mTopLeftDragger, params);
		}
		
		if (mBottomRightDragger == null) {
			mBottomRightDragger = new ImageView(this.getContext());
			mBottomRightDragger.setImageDrawable(this.getCircleDrawable());
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(this.getWidth() - HEIGHT_OF_DRAGGER - INITIAL_MARGIN_OF_DRAGGER, this.getHeight() - HEIGHT_OF_DRAGGER - INITIAL_MARGIN_OF_DRAGGER, 0, 0);
			this.addView(mBottomRightDragger, params);
		}
	}
	
	public void setImageResource(int resId) {
		if (mImageView != null) {
			mImageView.setImageResource(resId);
		}
	}

	private Drawable getCircleDrawable() {
		ShapeDrawable circle= new ShapeDrawable( new OvalShape());
		circle.setIntrinsicHeight( HEIGHT_OF_DRAGGER );
        circle.setIntrinsicWidth(HEIGHT_OF_DRAGGER);
        circle.setBounds(new Rect(0, 0, HEIGHT_OF_DRAGGER, HEIGHT_OF_DRAGGER));
        circle.getPaint().setColor(Color.BLUE);
        return circle;
	}
	
}
