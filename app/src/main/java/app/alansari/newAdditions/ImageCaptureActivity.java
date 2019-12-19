package app.alansari.newAdditions;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import app.alansari.R;

public class ImageCaptureActivity extends AppCompatActivity {


   /* private static final String TAG = "ImageActivity";
    FrameLayout mFrameLayout;
    SurfaceView mSurfaceView;
    Button mButtonCapture;
    Camera mCamera;
    Preview mPreview;

    String mImage_str, mCheckInOut;
    private ProgressDialog mDialog;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        SharedPreferences sharedPreferences=getSharedPreferences(getString(R.string.save), Context.MODE_PRIVATE);

        ImageView imageView=findViewById(R.id.iv_show_image);
        String selectView=getIntent().getStringExtra("name");

        if(selectView.equalsIgnoreCase("1")){

            String img=sharedPreferences.getString("baseFront","");
            if(img.equalsIgnoreCase(""))
                finish();


            byte[] decodeString= Base64.decode(img,Base64.DEFAULT);
            Bitmap decode=BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            imageView.setImageBitmap(decode);
            Log.e("scvsvbshb",""+img);

        }else if(selectView.equalsIgnoreCase("2")){
            String img=sharedPreferences.getString("baseBack","");

            if(img.equalsIgnoreCase(""))
                finish();

            byte[] decodeString= Base64.decode(img,Base64.DEFAULT);
            Bitmap decode=BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            imageView.setImageBitmap(decode);
            Log.e("scvsvbshb",""+img);

        }









    }

    /*private void initView() {
        mContext = this;

        mFrameLayout = (FrameLayout) findViewById(R.id.xFrameLayout);
        mSurfaceView = (SurfaceView) findViewById(R.id.xSurfaceView);
        mButtonCapture = (Button) findViewById(R.id.xButtonCapture);

        mPreview = new Preview(this, mSurfaceView);
        mPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(mPreview);
        mPreview.setKeepScreenOn(true);


        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Please Wait...");




        mButtonCapture.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                mCamera = Camera.open(1);
                mCamera.startPreview();
                Camera.CameraInfo info = new Camera.CameraInfo();
                mCamera.setDisplayOrientation(0);
                mCamera.setDisplayOrientation(getCorrectDisplayOrientation(info, mCamera));

                mPreview.setCamera(mCamera);
            } catch (RuntimeException e) {
                Toast.makeText(this, "Camera Not Found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int getCorrectDisplayOrientation(Camera.CameraInfo info, Camera mCamera) {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 270;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    @Override
    protected void onPause() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] bytes, Camera camera) {
            *//*AlertDialog.Builder builder = new AlertDialog.Builder(ImageCaptureAttendanceActivity.this);
            builder.setTitle("Are you sure you want to save this picture");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // new SaveImageTask().execute(data);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();*//*


            new SaveImageTask().execute(bytes);
            resetCam();

            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };


    private void resetCam() {
        mCamera.startPreview();
        mPreview.setCamera(mCamera);
    }

    @Override
    public void onClick(View view) {
        if (view == mRelLayBack) {
            finish();
        }
        if (view == mButtonCapture) {
            if (mNetworkPermission.IsNetworkAvailable()) {
                //mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
                mCamera.takePicture(shutterCallback, rawCallback, new Camera.PictureCallback() {
                    private File imageFile;

                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {

                        try {
                            Bitmap loadedImage = null;
                            Bitmap rotatedBitmap = null;
                            loadedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            int width = loadedImage.getWidth();
                            int height = loadedImage.getHeight();
                            int newWidth = 300;
                            int newHeight = 300;

                            float scaleWidth = ((float) newWidth) / width;
                            float scaleHeight = ((float) newHeight) / height;

                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);
                            matrix.postRotate(270);

                            rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, true);

                            File sdCard = Environment.getExternalStorageDirectory();
                            File dir = new File(sdCard.getAbsolutePath() + "/Media");

                            if (dir.exists() == false) {
                                dir.mkdirs();
                            }
                            boolean success = true;
                            if (success) {
                                String filname = String.format("DKG " + "%d.jpg", currentTimeMillis());
                                imageFile = new File(dir, filname);

                            } else {
                                Toast.makeText(mContext, "Image Not Saved", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            FileOutputStream fout = new FileOutputStream(imageFile);
                            fout.write(outputStream.toByteArray());
                            fout.close();


                        *//*    ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DATE_TAKEN,
                                    System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                            values.put(MediaStore.MediaColumns.DATA,
                                    imageFile.getAbsolutePath());
                            ImageCaptureAttendanceActivity.this.getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*//*


                            refreshGallery(imageFile);

                            if (mDDPref.sGetCheckInOutValue().equalsIgnoreCase("0")) {
                                mDDPref.sSetCheckInOutValue("" + 1);
                                mCheckInOut = "IN";
                            } else if (mDDPref.sGetCheckInOutValue().equalsIgnoreCase("1")) {
                                mDDPref.sSetCheckInOutValue("" + 0);
                                mCheckInOut = "OUT";
                            } else {
                                mDDPref.sSetCheckInOutValue("" + 1);
                                mCheckInOut = "IN";
                            }

                            Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            mImage_str = Base64.encodeToString(b, Base64.DEFAULT);


                            mSettingParamerers();
                            //remove Image from gallery
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(dir, children[i]).delete();
                                }
                            }




                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });


            } else {
                OnlyDialogBox onlyDialogBox = new OnlyDialogBox(this, (OnDialogListnersOnly) this);
                onlyDialogBox.onShowDialogBox();

            }

        }
    }






    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {


        @Override
        protected Void doInBackground(byte[]... bytes) {
            FileOutputStream outputStream = null;
            try {
                File sdCard = Environment.getExternalStorageDirectory();


                File dir = new File(sdCard.getAbsolutePath() + "/ABSCrm/Media");
                if (dir.exists() == false) {
                    dir.mkdirs();
                }


                String fileName = String.format("DKG " + "%d.jpg", currentTimeMillis());

                //String FileNmae=("DKG "+ System.currentTimeMillis()  + ".jpg ");
                File outFile = new File(dir, fileName);
                outputStream = new FileOutputStream(outFile);
                outputStream.write(bytes[0]);
                outputStream.flush();
                outputStream.close();
                Log.e(TAG, "onPictureTaken - wrote bytes: " + bytes.length + " to " + outFile.getAbsolutePath());
                //mCheckInOutValue=0;
                refreshGallery(outFile);




                Bitmap bm = BitmapFactory.decodeFile(outFile.getAbsolutePath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                mImage_str = Base64.encodeToString(b, Base64.DEFAULT);
                Log.e("checkInOut2", "" +mImage_str );

                mSettingParamerers();


                //finish();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
            }
            return null;
        }

    }

    private void mSettingParamerers() {
        try {
            mCamera.stopPreview();

            //jsonObjectCalling.put("image_str", mImage_str);


            Log.e("testsnfvjs", "" + mCheckInOut + " " +mImage_str);



            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    //new mCallingWebservices().execute();

                }
            });
        } catch (JSONException e) {

        }
    }

    private void refreshGallery(File outFile) {
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(outFile));
        sendBroadcast(i);

    }


}



*/
}
