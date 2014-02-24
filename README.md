# CropImageView - Android

Super easy component for Android to crop an image

![](https://raw.github.com/joshdholtz/CropImageView/master/screenshots/screenshot_1.png)

### Installation

- Download JAR from releases [crop-image-view-0.0.1.jar](https://github.com/joshdholtz/CropImageView/releases/tag/crop-image-view-0.0.1)
- Download and use as an Android library project

### Example

````xml

<com.joshholtz.cropimageview.CropImageView
        android:id="@+id/crop_image_view"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:background="#666666"
        android:src="@drawable/josh_hood"
        />

````

````java

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
//		cropImageView.setKeepSquare(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_crop) {
			try {
				DisplayActivity.imageToShow = cropImageView.crop(this);
				
				Intent intent = new Intent(this, DisplayActivity.class);
				this.startActivity(intent);
			} catch (IllegalArgumentException e) {
				// Crop section is out of bounds of image
			}
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
}

````

## Author

Josh Holtz, me@joshholtz.com, [@joshdholtz](https://twitter.com/joshdholtz)

## License

CropImageView is available under the MIT license. See the LICENSE file for more info.
