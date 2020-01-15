package app.alansari.fragments.my_emirates;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.ProfessionalLoadList;
import app.alansari.models.profiledetails.ProfileDetails;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.ImageCaptureActivity;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static android.app.Activity.RESULT_OK;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.UPDATE_MY_ID_DETAILS;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyEmiratesIdFragment extends Fragment implements View.OnClickListener, OnWebServiceResult {
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE_FRONT = 3;
    private static final int REQUEST_IMAGE_CAPTURE_BACK = 4;
    public static String base64All;
    int mValueClick = 0;
    ImageView capturedPhotoFrontSide, capturedPhotoBackSide;
    Uri image, imageFront, imageBack;
    String mCameraFileName, mCameraFileNameFront, mCameraFileNameBack;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Dialog imagePickDialog;
    private String base64Front = "";
    private String base64Back = "";
    private JSONObject jsonObject;
    private String sessionTime;
    private Context context;
    private Dialog dynamicDataAlert;
    private LinearLayout accountExpandLayout;
    private ProfileDetails.TEMPLATELISTItem dataObject;
    private TextInputLayout dynamicLayout1, dynamicLayout2;
    private EditText etName1, etName2;
    String fieldId;
    private JSONObject jsonObjectDynamicData;
    boolean hideDialog;

    public MyEmiratesIdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    if (mValueClick == 1) {

                        if (data != null) {
                            image = data.getData();
                        }
                        if (image == null && mCameraFileName != null) {
                            image = Uri.fromFile(new File(mCameraFileName));
                        }
                        File file = new File(mCameraFileName);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        capturedPhotoFrontSide.setImageURI(image);
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(image);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        base64Front = CommonUtils.encodeTobase64(selectedImage);

                        Log.e("shjsgbs", "" + base64Front);

                    } else if (mValueClick == 2) {
                        if (data != null) {
                            image = data.getData();

                        }
                        if (image == null && mCameraFileName != null) {
                            image = Uri.fromFile(new File(mCameraFileName));

                        }
                        File file = new File(mCameraFileName);
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        capturedPhotoBackSide.setImageURI(image);
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(image);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        base64Back = CommonUtils.encodeTobase64(selectedImage);
                        Log.e("shjsgbs", "" + base64Back);

                    }


                } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;

                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream, null, options);
                        Uri selectedImageUri = data.getData();
                        String picturePath = getPath(getActivity().getApplicationContext(), selectedImageUri);
                        // Log.d("Picture Path", picturePath);
                        Bitmap newBitmap = CommonUtils.modifyOrientation2(selectedImage, picturePath);

                        if (mValueClick == 1) {
                            capturedPhotoFrontSide.setImageBitmap(newBitmap);
                            base64Front = CommonUtils.encodeTobase64(newBitmap);
                            base64All = base64Front;


                            editor.putString("baseFront", base64Front);
                            editor.commit();

                        } else if (mValueClick == 2) {
                            capturedPhotoBackSide.setImageBitmap(newBitmap);
                            base64Back = CommonUtils.encodeTobase64(newBitmap);
                            base64All = base64Back;

                            editor.putString("baseBack", base64Back);
                            editor.commit();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//--------------------------------------------------------------------------------------------------
                } else if (requestCode == REQUEST_IMAGE_CAPTURE_FRONT && resultCode == RESULT_OK) {
                    if (mValueClick == 1) {
                        if (data != null) {
                            imageFront = data.getData();
                        }
                        if (imageFront == null && mCameraFileNameFront != null) {
                            imageFront = Uri.fromFile(new File(mCameraFileNameFront));
                        }
                        File file = new File(mCameraFileNameFront);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        // capturedPhotoFrontSide.setImageURI(imageFront);
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageFront);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        base64Front = CommonUtils.encodeTobase64(selectedImage);
                        base64All = base64Front;
                        Log.e("shjsgbs", "REQUEST_IMAGE_CAPTURE_FRONT" + base64Front);


                        byte[] decodeString = Base64.decode(base64Front, Base64.DEFAULT);
                        Bitmap decode = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        capturedPhotoFrontSide.setImageBitmap(decode);

                        editor.putString("baseFront", base64Front);
                        editor.commit();


                    }
                } else if (requestCode == REQUEST_IMAGE_CAPTURE_BACK && resultCode == RESULT_OK) {

                    if (mValueClick == 2) {
                        if (data != null) {
                            imageBack = data.getData();

                        }
                        if (imageBack == null && mCameraFileNameBack != null) {
                            imageBack = Uri.fromFile(new File(mCameraFileNameBack));

                        }
                        File file = new File(mCameraFileNameBack);
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        //capturedPhotoBackSide.setImageURI(imageBack);
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageBack);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        base64Back = CommonUtils.encodeTobase64(selectedImage);
                        base64All = base64Back;
                        Log.e("shjsgbs", "REQUEST_IMAGE_CAPTURE_BACK" + base64Back);

                        editor.putString("baseBack", base64Back);
                        editor.commit();

                        byte[] decodeString = Base64.decode(base64Back, Base64.DEFAULT);
                        Bitmap decode = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        capturedPhotoBackSide.setImageBitmap(decode);

                    }


//--------------------------------------------------------------------------------------------------


                } else if (requestCode == 6 && resultCode == RESULT_OK) {
                    if (data.getParcelableExtra(Constants.OBJECT) != null) {
                        Object object = data.getParcelableExtra(Constants.OBJECT);
                        Log.e("chjsbchsc", "" + object);
                        Log.e("skcnsjbch", "" + requestCode + " " + data);
                        switch (requestCode) {
                            case 6:
                                etName1.setText(((ProfessionalLoadList) object).getDisplayValue());
                                //setDataToEditText(position, ((ProfessionalLoadList) object).getDisplayValue(), ((ProfessionalLoadList) object).getDisplayKey());
                                break;

                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        hideDialog = getArguments().getBoolean("HideDialog");
       return inflater.inflate(R.layout.fragment_my_emirates_id, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        ((Button) view.findViewById(R.id.btn_click_submit)).setOnClickListener(this);
        capturedPhotoFrontSide = view.findViewById(R.id.captured_photo_front_side);
        capturedPhotoBackSide = view.findViewById(R.id.captured_photo_back_side);
        context = getActivity();

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.save), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

       /* if (base64Front == "" ) {
            editor.putString("baseFront", "");
            editor.commit();
        }
        if (base64Back == "") {
            editor.putString("baseBack", "");
            editor.commit();

        }*/
        if (base64Front == "" || base64Back == "") {
            editor.clear().commit();

        }


//--------------------------------------------------------------------------------------------------


        ((LinearLayout) view.findViewById(R.id.tv_front_side)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ImageCaptureActivity.class);
                i.putExtra("name", "1");
                getActivity().startActivity(i);


            }
        });

        ((LinearLayout) view.findViewById(R.id.tv_back_side)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ImageCaptureActivity.class);
                i.putExtra("name", "2");
                getActivity().startActivity(i);


            }
        });


//--------------------------------------------------------------------------------------------------

        if (getArguments() != null) {
            String eidNumber = getArguments().getString(Constants.EID_NUMBER);
            String cardNumber = getArguments().getString(Constants.CARD_NUMBER);
            String expiryDate = getArguments().getString(Constants.EXPIRY_DATE);

        }

        view.findViewById(app.alansari.R.id.btn_click_front_side).setOnClickListener(this);
        view.findViewById(R.id.btn_click_back_side).setOnClickListener(this);
        view.findViewById(app.alansari.R.id.btn_click_submit).setOnClickListener(this);


        imagePickDialog = new Dialog(getActivity(), app.alansari.R.style.CustomDialogThemeLightBg);
        imagePickDialog.setCanceledOnTouchOutside(true);
        imagePickDialog.setContentView(R.layout.image_pick_dialog);
        imagePickDialog.findViewById(app.alansari.R.id.camera).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkCameraPermission();
            }
        });


        imagePickDialog.findViewById(app.alansari.R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkGalleryPermission();
            }
        });

        imagePickDialog.findViewById(app.alansari.R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(),
                        android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), android.Manifest.permission.CAMERA)) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{android.Manifest.permission
                                                .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{android.Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            //pickCamera();
            //cameraIntent();
            if (mValueClick == 1) {
                cameraIntentFront();

            } else if (mValueClick == 2) {
                cameraIntentback();


            }


        }


    }

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    private void cameraIntentFront() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileNameFront = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_FRONT);


    }

    private void cameraIntentback() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileNameBack = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_BACK);


    }

    public void pickPhotos() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_IMAGE_PICK);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void pickCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void cameraIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case UPDATE_MY_ID_DETAILS:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            LogUtils.e("adcfjnasdcfjbsdResult", "" + response.getString("RESULT").toString());
                            LogUtils.e("adcfjnasdcfjbsdResult", "" + response.getString("MESSAGE").toString());
                            MyEmiratesResultPage resultPage = new MyEmiratesResultPage();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.MESSAGE, response.getString("MESSAGE").toString());
                            resultPage.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_layout, resultPage).addToBackStack(null).commit();

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            MyEmiratesResultPage resultPage = new MyEmiratesResultPage();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.MESSAGE, "Emirates ID was not uploaded successfully. Kindly send copy of Emirates ID to hello.app@alansari.ae to update your profile or try again after some time.");
                            resultPage.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_layout, resultPage).addToBackStack(null).commit();

                        }
                    } else {
                        Toast.makeText(getContext(), getString(app.alansari.R.string.empty_result), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case VALIDATE_BENEFICIARY_CE:
            case ADD_BENEFICIARY_CE:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (sType == CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE) {
                                //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                //finish();
                                //snackbar(response.getString(Constants.MESSAGE));
                                submitData();
                                return;
                            }
                            submitData2();
                            //onError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            if(!response.getString(Constants.MESSAGE).equalsIgnoreCase("null"))
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_LONG).show();

                        } else {
                            onError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onError();
                    }
                } else {
                    onError();
                }
                break;
        }
    }

    private void onError() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click_submit:
                if (validationCheck()) {
                    if(hideDialog){
                        dynanicDataAlertBox();
                    }else{
                        submitData();
                    }
                } else {
                    Toast.makeText(getContext(), "Please upload Pictures", Toast.LENGTH_SHORT).show();
                }
                //getFragmentManager().beginTransaction().replace(R.id.fragment_layout, new MyEmiratesResultPage()).addToBackStack(null).commit();
                break;
            case R.id.btn_click_front_side:
                mValueClick = 1;
                imagePickDialog.show();
                break;
            case R.id.btn_click_back_side:
                mValueClick = 2;
                imagePickDialog.show();
                break;
            case R.id.dynamic_layout1:
            case R.id.edit_text1:
                Intent i = new Intent(context, SelectItemActivity.class);
                i.putExtra(Constants.ITEM_TYPE, Constants.SELECT_PROFESIONAL_AND_DESIGANATION);
                i.putExtra(Constants.ID, fieldId);
                i.putExtra(Constants.TITLE, Constants.SERVICE_TYPE);
                startActivityForResult(i, 6);
                break;
        }


    }


    public String compressImage(String imageUri, Context context) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private boolean validationCheck() {
        if (base64Front != null && !TextUtils.isEmpty(base64Front))
            if (base64Back != null && !TextUtils.isEmpty(base64Back))
                return true;
        return false;


    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private void submitData() {
        if (NetworkStatus.getInstance(getContext()).isOnline2(getContext())) {
            if (jsonObject == null)
                addDataJsonFormat();
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.UPDATE_MY_ID_DETAILS,
                    UPDATE_MY_ID_DETAILS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(UPDATE_MY_ID_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_MY_ID_DETAILS.toString());
            CommonUtils.showLoading(getContext(), getString(R.string.please_wait), UPDATE_MY_ID_DETAILS.toString(), false);
        } else {
            Toast.makeText(getContext(), getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitDataNew() {
        if (NetworkStatus.getInstance(getContext()).isOnline2(getContext())) {
            if (jsonObject == null) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.UPDATE_MY_ID_DETAILS,
                        UPDATE_MY_ID_DETAILS, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(UPDATE_MY_ID_DETAILS.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_MY_ID_DETAILS.toString());
                CommonUtils.showLoading(getContext(), getString(R.string.please_wait), UPDATE_MY_ID_DETAILS.toString(), false);

            }
        } else {
            Toast.makeText(getContext(), getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void submitDataHideDialog() {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (jsonObject != null) {
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.SUBMIT_MY_PROFILE_PROFILE_URL, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE, Request.Method.PUT, this);
                    AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString(), false);
                }
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private JSONObject addDataJsonFormat() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.AREX_MEM_FK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.CE_MEM_FK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(getContext()));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            if(hideDialog){
                if (getArguments() != null) {
                    jsonObject.put(Constants.EID_NUMBER, changeEidFormat(getArguments().getString(Constants.EID_NUMBER)));
                    jsonObject.put(Constants.EID_CARD_NUMBER, getArguments().getString(Constants.CARD_NUMBER));
                    jsonObject.put(Constants.ID_EXPIRTY_DATE, changingDateFormat(getArguments().getString(Constants.EXPIRY_DATE)));
                } else {
                    jsonObject.put(Constants.EID_NUMBER, "");
                    jsonObject.put(Constants.EID_CARD_NUMBER, "");
                    jsonObject.put(Constants.ID_EXPIRTY_DATE, "");
                }
            }

            if (base64Front != null && !TextUtils.isEmpty(base64Front)) {
                jsonObject.put(Constants.ID_UPLOAD_FRONT, base64Front);
            } else {
                Toast.makeText(getContext(), "Please upload Front Side of Emirates Id", Toast.LENGTH_SHORT).show();

            }
            if (base64Back != null && !TextUtils.isEmpty(base64Back)) {
                jsonObject.put(Constants.ID_UPLOAD_BACK, base64Back);
            } else {
                Toast.makeText(getContext(), "Please upload Back Side of Emirates Id", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiary :-  " + jsonObject.toString());
        return jsonObject;
    }


    private String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private String changeEidFormat(String string) {
        StringBuffer str = new StringBuffer(string);
        System.out.println("string = " + str);
        // insert boolean value at offset 8
        str.deleteCharAt(3);
        str.deleteCharAt(7);
        str.deleteCharAt(14);
        return str.toString();
    }

    private String changingDateFormat(String string) {
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        Date date = null;
        try {
            date = sourceDateFormat.parse(string);
            SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(targetDateFormat.format(date));
            return String.valueOf(targetDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void dynanicDataAlertBox() {
        dynamicDataAlert = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        dynamicDataAlert.setCanceledOnTouchOutside(false);
        dynamicDataAlert.setCancelable(false);
        dynamicDataAlert.setContentView(R.layout.dynamic_alert_data);
        accountExpandLayout = dynamicDataAlert.findViewById(R.id.account_info_layout);
        Button btnSubmit = dynamicDataAlert.findViewById(R.id.btn_submit);
        Button btn_cancel = dynamicDataAlert.findViewById(R.id.dialog_btn_cancel);
        ((TextView) dynamicDataAlert.findViewById(R.id.dialog_title)).setText(getString(R.string.dynamic_dialog_title));
        dynamicLayout1 = dynamicDataAlert.findViewById(R.id.dynamic_layout1);
        dynamicLayout2 = dynamicDataAlert.findViewById(R.id.dynamic_layout2);
        etName1 = dynamicDataAlert.findViewById(R.id.edit_text1);
        etName2 = dynamicDataAlert.findViewById(R.id.edit_text2);
        onDataSet();
        etName1.setOnClickListener(this);
        dynamicLayout1.setOnClickListener(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateData();
                dynamicDataAlert.dismiss();


                //tEmail.setError(mContext.getString(R.string.error_enter_valid_email));
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicDataAlert.dismiss();
            }
        });

        dynamicDataAlert.show();

    }


    private void onDataSet() {
        if (getArguments().getParcelable("data") != null) {
            dataObject = getArguments().getParcelable("data");
            dynamicLayout1.setHint(dataObject.getTEMPALTEDETAILS().get(0).getFIELD());
            dynamicLayout2.setHint(dataObject.getTEMPALTEDETAILS().get(1).getFIELD());
            etName1.setText(dataObject.getTEMPALTEDETAILS().get(0).getFIELDVALUE());
            etName2.setText(dataObject.getTEMPALTEDETAILS().get(1).getFIELDVALUE());
            fieldId = (dataObject.getTEMPALTEDETAILS().get(0).getFIELDID());
            /*for (int i = 0; i < dataObject.getTEMPALTEDETAILS().size(); i++) {
                Log.e("cnjscbshdbc", "" + dataObject.getTEMPALTEDETAILS().get(i).getFIELDVALUE());

            }*/

        }
    }
    private void validateData() {
        try {
                jsonObjectDynamicData = new JSONObject();
                jsonObjectDynamicData.put(dataObject.getTEMPALTEDETAILS().get(0).getFIELDID(), etName1.getText().toString().trim());
                jsonObjectDynamicData.put(dataObject.getTEMPALTEDETAILS().get(1).getFIELDID(), etName2.getText().toString().trim());
                jsonObjectDynamicData.put(dataObject.getTEMPALTEDETAILS().get(2).getFIELDID(), dataObject.getTEMPALTEDETAILS().get(2).getFIELDVALUE());

            Log.e("fvbfshvcjmnv ", "" + jsonObjectDynamicData);
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                try {
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    jsonObjectDynamicData.put(Constants.USER_PK_ID, CommonUtils.getUserId());
                    jsonObjectDynamicData.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
                    jsonObjectDynamicData.put(Constants.SESSION_ID, sessionTime);
                    jsonObjectDynamicData.put(Constants.CATEGORY_ID,dataObject.getCATEGORYID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObjectDynamicData, Constants.VALIDATE_MY_PROFILE_PROFILE_URL, CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void submitData2() {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (jsonObjectDynamicData!= null) {
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObjectDynamicData, Constants.SUBMIT_MY_PROFILE_PROFILE_URL, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE, Request.Method.PUT, this);
                    AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString(), false);
                }
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}