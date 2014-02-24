package com.joshholtz.cropimageview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CropImageView extends FrameLayout implements View.OnTouchListener {
	
	private final static int HEIGHT_OF_DRAGGER_INSIDE = 30;
	private final static int HEIGHT_OF_DRAGGER = 32;
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
	
	public void setImageResource(int resId) {
		if (mImageView != null) {
			mImageView.setImageResource(resId);
		}
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
			mTopLeftDragger.setOnTouchListener(this);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(INITIAL_MARGIN_OF_DRAGGER, INITIAL_MARGIN_OF_DRAGGER, 0, 0);
			this.addView(mTopLeftDragger, params);
		}
		
		if (mBottomRightDragger == null) {
			mBottomRightDragger = new ImageView(this.getContext());
			mBottomRightDragger.setImageDrawable(this.getCircleDrawable());
			mBottomRightDragger.setOnTouchListener(this);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(this.getWidth() - HEIGHT_OF_DRAGGER - INITIAL_MARGIN_OF_DRAGGER, this.getHeight() - HEIGHT_OF_DRAGGER - INITIAL_MARGIN_OF_DRAGGER, 0, 0);
			this.addView(mBottomRightDragger, params);
		}
	}
	
	FrameLayout.LayoutParams parms;
//	LinearLayout.LayoutParams par;
	float dx=0,dy=0,x=0,y=0;
	
	@Override
    public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
	    {
	        case MotionEvent.ACTION_DOWN :
	        {
	            parms = (LayoutParams) v.getLayoutParams();
//	            par = (LinearLayout.LayoutParams) getWindow().findViewById(Window.ID_ANDROID_CONTENT).getLayoutParams();
	            dx = event.getRawX() - parms.leftMargin;
	            dy = event.getRawY() - parms.topMargin;
	        }
	        break;
	        case MotionEvent.ACTION_MOVE :
	        {
	            x = event.getRawX();
	            y = event.getRawY();
	            parms.leftMargin = (int) (x-dx);
	            parms.topMargin = (int) (y - dy);
	            v.setLayoutParams(parms);
	        }
	        break;
	        case MotionEvent.ACTION_UP :
	        {

	        }
	        break;
	    }
	    return true;
    }

	private Drawable getCircleDrawable() {
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( HEIGHT_OF_DRAGGER_INSIDE );
        biggerCircle.setIntrinsicWidth( HEIGHT_OF_DRAGGER_INSIDE);
        biggerCircle.setBounds(new Rect(0, 0, HEIGHT_OF_DRAGGER_INSIDE, HEIGHT_OF_DRAGGER_INSIDE));
        biggerCircle.getPaint().setColor(Color.BLUE);
        
        return biggerCircle;
	}
	
}
