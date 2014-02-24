package com.joshholtz.cropimageview.test;

import com.joshholtz.cropimageview.CropImageView;
import com.joshholtz.cropimageview.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

public class MainActivity extends Activity {

	CropImageView cropImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cropImageView = (CropImageView) this.findViewById(R.id.crop_image_view);
		cropImageView.setImageResource(getResources(), R.drawable.josh_hood);
		
		// OPTIONAL - set corner color size and crop area color
//		cropImageView.setCornerDrawable(Color.rgb(255, 200, 0), 100, 100);
//		cropImageView.setCropAreaDrawable(Color.LTGRAY, 150, Color.CYAN, 200, 8);
		
		// OPTIONAL - keep crop square
		cropImageView.setKeepSquare(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_crop) {
			DisplayActivity.imageToShow = cropImageView.crop(this);
			
			Intent intent = new Intent(this, DisplayActivity.class);
			this.startActivity(intent);
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
}
