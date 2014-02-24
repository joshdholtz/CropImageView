package com.joshholtz.cropimageview.test;

import com.joshholtz.cropimageview.CropImageView;
import com.joshholtz.cropimageview.R;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	CropImageView cropImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cropImageView = (CropImageView) this.findViewById(R.id.crop_image_view);
		cropImageView.setImageResource(R.drawable.josh_hood);
	}

}
