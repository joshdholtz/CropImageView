package com.joshholtz.cropimageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CropImageView extends FrameLayout implements View.OnTouchListener {
	
	private final static String TAG = "CropImageView";
	
	private final static int HEIGHT_OF_DRAGGER_INSIDE = 30;
	private final static int HEIGHT_OF_DRAGGER = 45;
	private final static int INITIAL_MARGIN_OF_DRAGGER = 60;
	
	private ImageView mImageView;
	private ImageView mTopLeftDragger;
	private ImageView mBottomRightDragger;
	private ImageView mDaBox;
	
	private Bitmap mBitmap;
	
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
	
	public void setImageBitmap(Bitmap bm) { 
		if (mImageView != null) {
			mBitmap = bm;
			mImageView.setImageBitmap(bm);
		}
	}
	
	public void setImageResource(Resources resources, int resId) {
		this.setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
	}
	
	public Bitmap crop(Context context) {
		// Weird padding cause image size
		int weirdSidePadding = 0;
		int weirdVerticalPadding = 0;
		
		// Image width / height ration
		double bitmapRatio = ((double)mBitmap.getWidth() / (double)mBitmap.getHeight());
		
		// Image width / height ration
		double thisRatio = ((double)this.getWidth() / (double)this.getHeight());
		
		Log.d(TAG, "This width and height - " + this.getWidth() + ", " + this.getHeight());
		Log.d(TAG, "Bitmap ratio - " + bitmapRatio);
		Log.d(TAG, "This ratio - " + thisRatio);
		
		if (bitmapRatio < thisRatio) {
			int bitmapWidth = (int) (bitmapRatio * this.getHeight());
			Log.d(TAG, "Bitmap is taller - width is = " + bitmapWidth);
			weirdSidePadding = (int) ((this.getWidth() - bitmapWidth) / 2.0); 
		} else {
			
		}
		
		FrameLayout.LayoutParams params = (LayoutParams) mDaBox.getLayoutParams();
		
		// Getting crop dimensions
		float d = context.getResources().getDisplayMetrics().density;
		int x = (int)((params.leftMargin - weirdSidePadding) * d);
		int y = (int)((params.topMargin + weirdVerticalPadding) * d);
		int width = (int)((this.getWidth() - params.leftMargin - params.rightMargin + (weirdSidePadding * 0)) * d);
		int height = (int)((this.getHeight() - params.topMargin - params.bottomMargin - weirdVerticalPadding) * d);
		
		Bitmap crooopppppppppppppppeed = Bitmap.createBitmap(mBitmap, x, y, width, height);
		
		return crooopppppppppppppppeed;
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
			
			int width = this.getWidth();
			int height = this.getHeight();
			
			int leftMargin = width - INITIAL_MARGIN_OF_DRAGGER - HEIGHT_OF_DRAGGER;
			int topMargin = height - INITIAL_MARGIN_OF_DRAGGER - HEIGHT_OF_DRAGGER;
			int rightMargin = 0;
			int bottomMargin = 0;
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			this.addView(mBottomRightDragger, params);
		}
		
		if (mDaBox == null) {
			mDaBox = new ImageView(this.getContext());
//			mDaBox.setImageDrawable(this.getCropDrawable());
			if (android.os.Build.VERSION.SDK_INT < 16) {
				mDaBox.setBackgroundDrawable(this.getCropDrawable());
			} else {
				mDaBox.setBackground(this.getCropDrawable());
			}
			mDaBox.setScaleType(ScaleType.MATRIX);
			mDaBox.setAdjustViewBounds(true);
//			mDaBox.setBackgroundColor(Color.WHITE);
			
//			FrameLayout.LayoutParams paramsTopLeft = (LayoutParams) mTopLeftDragger.getLayoutParams();
//			FrameLayout.LayoutParams paramsBottomRight = (LayoutParams) mBottomRightDragger.getLayoutParams();
//			
//			int leftMargin = (int) (paramsTopLeft.leftMargin + (HEIGHT_OF_DRAGGER/2.0));
//			int topMargin = (int) (paramsTopLeft.topMargin + (HEIGHT_OF_DRAGGER/2.0));
//			int rightMargin = (int) (paramsBottomRight.rightMargin + HEIGHT_OF_DRAGGER + INITIAL_MARGIN_OF_DRAGGER);
//			int bottomMargin = (int) (paramsBottomRight.bottomMargin + HEIGHT_OF_DRAGGER + INITIAL_MARGIN_OF_DRAGGER);
//			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			this.addView(mDaBox, 1, params);
			
			moveCrop();
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
	            
	            moveCrop();
	        }
	        break;
	        case MotionEvent.ACTION_UP :
	        {

	        }
	        break;
	    }
	    return true;
    }

	private void moveCrop() {
		FrameLayout.LayoutParams paramsTopLeft = (LayoutParams) mTopLeftDragger.getLayoutParams();
		FrameLayout.LayoutParams paramsBottomRight = (LayoutParams) mBottomRightDragger.getLayoutParams();
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		int leftMargin = (int) (paramsTopLeft.leftMargin + (HEIGHT_OF_DRAGGER/2.0f));
		int topMargin = (int) (paramsTopLeft.topMargin + (HEIGHT_OF_DRAGGER/2.0f));
		int rightMargin = (int) (width - paramsBottomRight.leftMargin - (HEIGHT_OF_DRAGGER/2.0f));
		int bottomMargin = (int) (height - paramsBottomRight.topMargin - (HEIGHT_OF_DRAGGER/2.0f));
		
		Log.d(TAG, "Trying to move crop - " + leftMargin + ", " + topMargin + ", " + rightMargin + ", " + bottomMargin);
		
		FrameLayout.LayoutParams params = (LayoutParams) mDaBox.getLayoutParams();;
		params.leftMargin = leftMargin;
		params.topMargin = topMargin;
		params.rightMargin = rightMargin;
		params.bottomMargin = bottomMargin;
		mDaBox.setLayoutParams(params);

	}
	
	private Drawable getCircleDrawable() {
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( HEIGHT_OF_DRAGGER );
        biggerCircle.setIntrinsicWidth( HEIGHT_OF_DRAGGER);
        biggerCircle.setBounds(new Rect(0, 0, HEIGHT_OF_DRAGGER, HEIGHT_OF_DRAGGER));
        biggerCircle.getPaint().setColor(Color.BLUE);
        
        return biggerCircle;
	}
	
	private Drawable getCropDrawable() {
		ShapeDrawable greenShape = new ShapeDrawable(new RectShape());
	    greenShape.getPaint().setStrokeWidth(3);
	    greenShape.getPaint().setColor(Color.WHITE);
	    greenShape.setAlpha(150);
	    greenShape.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
	    
	    // Its not really green
	    return greenShape;
	}
	
}
