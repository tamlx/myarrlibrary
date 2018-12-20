package b.laixuantam.myaarlibrary.widgets.imagecrop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.imagecrop.util.BitmapLoadUtils;
import b.laixuantam.myaarlibrary.widgets.imagecrop.view.ImageCropView;

/**
 * private void startCrop(Uri imageUri) {
 * Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);
 * intent.setData(imageUri);
 * if (imageUri != null) {
 * startActivity(intent);
 * } else {
 * startActivityForResult(intent, ACTION_REQUEST_IMAGE_CROP);
 * }
 * }
 *
 * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * if (resultCode == RESULT_OK) {
 * switch (requestCode) {
 * <p>
 * case ACTION_REQUEST_IMAGE_CROP:
 * String filePath2 = data.getStringExtra("result");
 * Uri filePathUri2 = Uri.parse(filePath2);
 * loadAsync(filePathUri2);
 * break;
 * }
 * }
 * }
 */


public class ImageCropActivity extends Activity {
    public static final String TAG = "CropActivity";

    private ImageCropView imageCropView;

    private Button btn_1_1, btn_3_4, btn_4_3, btn_16_9, btn_9_16, btn_crop;

    private View rlEmptyImage, btnSelectImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        imageCropView = findViewById(R.id.image);
        btn_1_1 = (Button) findViewById(R.id.ratio11btn);
        btn_3_4 = (Button) findViewById(R.id.ratio34btn);
        btn_4_3 = (Button) findViewById(R.id.ratio43btn);
        btn_16_9 = (Button) findViewById(R.id.ratio169btn);
        btn_9_16 = (Button) findViewById(R.id.ratio916btn);
        btn_crop = (Button) findViewById(R.id.crop_btn);

        rlEmptyImage = findViewById(R.id.rlEmptyImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        if (getIntent() != null && getIntent().getData() != null) {

            Intent i = getIntent();
            Uri uriImageSelected = i.getData();
            imageCropView.setImageFilePath(uriImageSelected.toString());

            imageCropView.setVisibility(View.VISIBLE);
            rlEmptyImage.setVisibility(View.GONE);

            btn_1_1.setEnabled(true);
            btn_3_4.setEnabled(true);
            btn_4_3.setEnabled(true);
            btn_16_9.setEnabled(true);
            btn_9_16.setEnabled(true);
            btn_crop.setEnabled(true);
        } else {
            imageCropView.setVisibility(View.GONE);
            rlEmptyImage.setVisibility(View.VISIBLE);
            btn_1_1.setEnabled(false);
            btn_3_4.setEnabled(false);
            btn_4_3.setEnabled(false);
            btn_16_9.setEnabled(false);
            btn_9_16.setEnabled(false);
            btn_crop.setEnabled(false);
        }
        imageCropView.setAspectRatio(1, 1);

        btn_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPossibleCrop(1, 1)) {
                    imageCropView.setAspectRatio(1, 1);
                } else {
                    Toast.makeText(ImageCropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click 3 : 4");
                if (isPossibleCrop(3, 4)) {
                    imageCropView.setAspectRatio(3, 4);
                } else {
                    Toast.makeText(ImageCropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_4_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click 4 : 3");
                if (isPossibleCrop(4, 3)) {
                    imageCropView.setAspectRatio(4, 3);
                } else {
                    Toast.makeText(ImageCropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_16_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click 16 : 9");
                if (isPossibleCrop(16, 9)) {
                    imageCropView.setAspectRatio(16, 9);
                } else {
                    Toast.makeText(ImageCropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_9_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click 9 : 16");
                if (isPossibleCrop(9, 16)) {
                    imageCropView.setAspectRatio(9, 16);
                } else {
                    Toast.makeText(ImageCropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imageCropView.isChangingScale()) {
                    Bitmap b = imageCropView.getCroppedImage();

                    if (b != null) {
                        bitmapConvertToFile(b);
                    } else {
                        Toast.makeText(ImageCropActivity.this, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
    }

    private static final int ACTION_REQUEST_GALLERY = 99;

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    String filePath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        filePath = getRealPathFromURI_API19(this, data.getData());
                    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                        filePath = getRealPathFromURI_API11to18(this, data.getData());
                    } else {
                        filePath = getRealPathFromURI_BelowAPI11(this, data.getData());
                    }

                    if (filePath.isEmpty()) {
                        return;
                    }
                    Uri filePathUri = Uri.parse(filePath);
                    loadAsync(filePathUri);
                    break;
            }
        }
    }

    private boolean isPossibleCrop(int widthRatio, int heightRatio) {
        Bitmap bitmap = imageCropView.getViewBitmap();
        if (bitmap == null) {
            return false;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth < widthRatio && bitmapHeight < heightRatio) {
            return false;
        } else {
            return true;
        }
    }

    File bitmapFile = null;

    public File bitmapConvertToFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;

        try {
            File file = null;

            String fileStoreName = getString(R.string.project_image_resource);
            if (checkExternalStorageAvailable()) {
                file = new File(Environment.getExternalStoragePublicDirectory(fileStoreName), "");
            } else {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir(fileStoreName, Context.MODE_PRIVATE);
                // Create imageDir
                file = new File(directory, "");
            }
            if (!file.exists()) {
                file.mkdir();
            }

            bitmapFile = new File(file, "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg");
            fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            MediaScannerConnection.scanFile(this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(ImageCropActivity.this, "file saved", Toast.LENGTH_LONG).show();
                            returnResult(bitmapFile.getAbsolutePath());
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return bitmapFile;
    }

    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

    private boolean checkExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return (mExternalStorageAvailable && mExternalStorageWriteable) ? true : false;
    }

    public void onClickSaveButton(View v) {
        imageCropView.saveState();
        View restoreButton = findViewById(R.id.restore_btn);
        if (!restoreButton.isEnabled()) {
            restoreButton.setEnabled(true);
        }
    }

    public void onClickRestoreButton(View v) {
        imageCropView.restoreState();
    }

    private void loadAsync(final Uri uri) {
        Log.i(TAG, "loadAsync: " + uri);

        Drawable toRecycle = imageCropView.getDrawable();
        if (toRecycle instanceof BitmapDrawable) {
            if (((BitmapDrawable) imageCropView.getDrawable()).getBitmap() != null)
                ((BitmapDrawable) imageCropView.getDrawable()).getBitmap().recycle();
        }
        imageCropView.setImageDrawable(null);

        imageWidth = 1000;
        imageHeight = 1000;

        DownloadAsync task = new DownloadAsync();
        task.execute(uri);
    }

    int imageWidth, imageHeight;

    class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements DialogInterface.OnCancelListener {

        ProgressDialog mProgress;
        private Uri mUri;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            mProgress = new ProgressDialog(ImageCropActivity.this);
            mProgress.setIndeterminate(true);
            mProgress.setCancelable(true);
            mProgress.setMessage("Loading image...");
            mProgress.setOnCancelListener(this);
            mProgress.show();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            mUri = params[0];

            Bitmap bitmap;

            bitmap = BitmapLoadUtils.decode(mUri.toString(), imageWidth, imageHeight, true);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (mProgress.getWindow() != null) {
                mProgress.dismiss();
            }

            if (result != null) {
                setImageURI(mUri, result);
            } else {
                Toast.makeText(ImageCropActivity.this, "Failed to load image " + mUri, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            Log.i(TAG, "onProgressCancel");
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled");
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        if (wholeID.split(":").length < 2) {
            return filePath;
        }
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int columnIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(columnIndex);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int columnIndex
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private boolean setImageURI(final Uri uri, final Bitmap bitmap) {

        Log.d(TAG, "image size: " + bitmap.getWidth() + "x" + bitmap.getHeight());
        imageCropView.setImageBitmap(bitmap);

        imageCropView.setVisibility(View.VISIBLE);
        rlEmptyImage.setVisibility(View.GONE);

        btn_1_1.setEnabled(true);
        btn_3_4.setEnabled(true);
        btn_4_3.setEnabled(true);
        btn_16_9.setEnabled(true);
        btn_9_16.setEnabled(true);
        btn_crop.setEnabled(true);

        return true;
    }

    private void returnResult(String resultCropFilePath) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", resultCropFilePath);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}