package com.joshholtz.cropimageview.test;

import com.joshholtz.cropimageview.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;

public class DisplayActivity extends Activity {
	
	public static Bitmap imageToShow;

	ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		imageView = (ImageView) this.findViewById(R.id.image_view);
		imageView.setImageBitmap(imageToShow);
	
	}

}
