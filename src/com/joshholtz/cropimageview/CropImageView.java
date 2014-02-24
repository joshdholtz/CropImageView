package com.joshholtz.cropimageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
	
	private  Drawable mDraggerDrawable;
	private  Drawable mCropDrawable;
	
	private boolean mKeepSquare;
	
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
	
	public void setCornerDrawable(int color, int width, int height) {
		mDraggerDrawable = this.getCircleDrawable(color, width, height);
		
		if (mTopLeftDragger != null) {
			mTopLeftDragger.setImageDrawable(mDraggerDrawable);
		}
		
		if (mBottomRightDragger != null) {
			mBottomRightDragger.setImageDrawable(mDraggerDrawable);
		}
	}
	
	public void setKeepSquare(boolean keepSquare) {
		mKeepSquare = keepSquare;
		
		if (mTopLeftDragger != null && mBottomRightDragger != null) {
			Log.d(TAG, "Draggers exist");
		} else {
			Log.d(TAG, "Draggers don't exist");
		}
	}
	
	public void setCropAreaDrawable(int fillColor, int fillAlpha, int strokeColor, int strokeAlpha, int strokeWidth) {
		mCropDrawable = this.getCropDrawable(fillColor, fillAlpha, strokeColor, strokeAlpha, strokeWidth);
		
		if (mDaBox != null) {
			if (android.os.Build.VERSION.SDK_INT < 16) {
				mDaBox.setBackgroundDrawable(mCropDrawable);
			} else {
				mDaBox.setBackground(mCropDrawable);
			}
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

		// Magic math
		if (bitmapRatio < thisRatio) {
			int bitmapWidth = (int) (bitmapRatio * this.getHeight());
			weirdSidePadding = (int) ((this.getWidth() - bitmapWidth) / 2.0); 
		} else {
			int bitmapHeight = (int) (bitmapRatio * this.getWidth());
			weirdVerticalPadding = (int) ((this.getHeight() - bitmapHeight) / 2.0);
		}
		
		FrameLayout.LayoutParams params = (LayoutParams) mDaBox.getLayoutParams();
		
		// Getting crop dimensions
		float d = context.getResources().getDisplayMetrics().density;
		int x = (int)((params.leftMargin - weirdSidePadding) * d);
		int y = (int)((params.topMargin - weirdVerticalPadding) * d);
		int width = (int)((this.getWidth() - params.leftMargin - params.rightMargin) * d);
		int height = (int)((this.getHeight() - params.topMargin - params.bottomMargin) * d);
		
		Bitmap crooopppppppppppppppeed = Bitmap.createBitmap(mBitmap, x, y, width, height);
		
		return crooopppppppppppppppeed;
	}
	
	private void init() {
		mDraggerDrawable = this.getCircleDrawable(Color.rgb(255, 200, 0), HEIGHT_OF_DRAGGER, HEIGHT_OF_DRAGGER);
		mCropDrawable = this.getCropDrawable(Color.LTGRAY, 150, Color.LTGRAY, 255, 8);
		
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
			mTopLeftDragger.setImageDrawable(mDraggerDrawable);
			mTopLeftDragger.setOnTouchListener(this);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(INITIAL_MARGIN_OF_DRAGGER, INITIAL_MARGIN_OF_DRAGGER, 0, 0);
			this.addView(mTopLeftDragger, params);
		}
		
		if (mBottomRightDragger == null) {
			mBottomRightDragger = new ImageView(this.getContext());
			mBottomRightDragger.setImageDrawable(mDraggerDrawable);
			mBottomRightDragger.setOnTouchListener(this);
			
			int width = this.getWidth();
			int height = this.getHeight();
			
			int leftMargin = width - INITIAL_MARGIN_OF_DRAGGER - HEIGHT_OF_DRAGGER;
			int topMargin = height - INITIAL_MARGIN_OF_DRAGGER - HEIGHT_OF_DRAGGER;
			int rightMargin = 0;
			int bottomMargin = 0;
			
			if (mKeepSquare) {
				int smallestMargin = Math.min(leftMargin, topMargin);
				leftMargin = smallestMargin;
				topMargin = smallestMargin;
			}
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
			this.addView(mBottomRightDragger, params);
		}
		
		if (mDaBox == null) {
			mDaBox = new ImageView(this.getContext());
//			mDaBox.setImageDrawable(this.getCropDrawable());
			if (android.os.Build.VERSION.SDK_INT < 16) {
				mDaBox.setBackgroundDrawable(mCropDrawable);
			} else {
				mDaBox.setBackground(mCropDrawable);
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
	            
	            int leftMargin = (int) (x-dx);
	            int topMargin = (int) (y - dy);
	    		if (mKeepSquare) {
	    			int smallestMargin = Math.min(leftMargin, topMargin);
	    			leftMargin = smallestMargin;
	    			topMargin = smallestMargin;
	    		}
	            
	            parms.leftMargin = leftMargin;
	            parms.topMargin = topMargin;
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
		
		int widthOfCorner = mDraggerDrawable.getIntrinsicWidth();
		int heightOfCorner = mDraggerDrawable.getIntrinsicHeight();
		
		int leftMargin = (int) (paramsTopLeft.leftMargin + (widthOfCorner/2.0f));
		int topMargin = (int) (paramsTopLeft.topMargin + (heightOfCorner/2.0f));
		int rightMargin = (int) (width - paramsBottomRight.leftMargin - (widthOfCorner/2.0f));
		int bottomMargin = (int) (height - paramsBottomRight.topMargin - (heightOfCorner/2.0f));
		
		FrameLayout.LayoutParams params = (LayoutParams) mDaBox.getLayoutParams();;
		params.leftMargin = leftMargin;
		params.topMargin = topMargin;
		params.rightMargin = rightMargin;
		params.bottomMargin = bottomMargin;
		mDaBox.setLayoutParams(params);

	}
	
	private Drawable getCircleDrawable(int color, int width, int height) {
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicWidth( width);
        biggerCircle.setIntrinsicHeight( height );
        biggerCircle.setBounds(new Rect(0, 0, width, height));
        biggerCircle.getPaint().setColor(color);
        
        return biggerCircle;
	}
	
	private Drawable getCropDrawable(int fillColor, int fillAlpha, int strokeColor, int strokeAlpha, int strokeWidth) {
		ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
		sd1.getPaint().setColor(strokeColor);
		sd1.getPaint().setStyle(Style.STROKE);
		sd1.getPaint().setStrokeWidth(strokeWidth);
		sd1.setAlpha(255);
		 
		ShapeDrawable sd2 = new ShapeDrawable(new RectShape());
		sd2.getPaint().setColor(fillColor);
		sd2.getPaint().setStyle(Style.FILL);
		sd2.setAlpha(fillAlpha);
		 
		Drawable[] layers = new Drawable[2];
		layers[0] = sd1;
		layers[1] = sd2;
		LayerDrawable composite = new LayerDrawable(layers);
		
		return composite;
	}
	
}
