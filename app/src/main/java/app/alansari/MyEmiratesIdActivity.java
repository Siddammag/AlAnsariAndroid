package app.alansari;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.fragments.my_emirates.MyEmiratesIdFragment;
import app.alansari.fragments.my_emirates.MyEmiratesPersonalInfo;
import app.alansari.models.profiledetails.ProfileDetails;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class MyEmiratesIdActivity extends NavigationBaseActivity implements View.OnClickListener, LogOutTimerUtil.LogOutListener {
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    int mValueClick = 0;
    int fragmentValue;
    private Context context;
    private Dialog imagePickDialog;
    private ProfileDetails.TEMPLATELISTItem dataObject;
    boolean hideDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    finish();
                } else if (getSupportFragmentManager().getBackStackEntryCount() == 2 && fragmentValue == 2) {
                    finish();
                } else if (getSupportFragmentManager().getBackStackEntryCount() == 3) {
                    finish();

                } else {
                    super.onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_emirates_id);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("My Emirates ID");
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);

        if(getIntent().getExtras()!=null){
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
        }
        boolean hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
         hideDialog = getIntent().getBooleanExtra("HideDialog", true);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
            addFragment(2);
            fragmentValue = 2;
        } else {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.VISIBLE);
            findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
            addFragment(1);
            fragmentValue = 1;
        }

        init();

    }

    private void addFragment(int type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle=null;
        try{
            bundle=new Bundle();
            bundle.putParcelable("data",dataObject);
            bundle.putBoolean("HideDialog", hideDialog);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if (type == 1) {
            MyEmiratesPersonalInfo myEmiratesPersonalInfo=new MyEmiratesPersonalInfo();
            myEmiratesPersonalInfo.setArguments(bundle);
            fragmentTransaction.add(R.id.fragment_layout, myEmiratesPersonalInfo).addToBackStack(null);
        }else {
            mFirebaseAnalytics.logEvent("AML_Pending", null);
            Log.i("AML_Pending", "Success in MyEmiratesId Acitivity");

            MyEmiratesIdFragment myEmiratesIdFragment=new MyEmiratesIdFragment();
            myEmiratesIdFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.fragment_layout,myEmiratesIdFragment).addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    private void init() {
        /*findViewById(app.alansari.R.id.btn_click_front_side).setOnClickListener(this);
        findViewById(R.id.btn_click_back_side).setOnClickListener(this);
        findViewById(app.alansari.R.id.btn_click_submit).setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);


        imagePickDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
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
        });*/


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 3) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_menu:
                openMenuDrawer();
                break;
          /*  case R.id.btn_click_front_side:
                mValueClick=1;
                imagePickDialog.show();
                break;
            case R.id.btn_click_back_side:
                mValueClick=2;
                imagePickDialog.show();
                break;
            case R.id.btn_click_submit:
                break;*/

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(MyEmiratesIdActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(MyEmiratesIdActivity.this,
                        android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (MyEmiratesIdActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (MyEmiratesIdActivity.this, android.Manifest.permission.CAMERA)) {

                Snackbar.make(MyEmiratesIdActivity.this.findViewById(android.R.id.content),
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
            pickCamera();
        }
    }

    private void pickCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(MyEmiratesIdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyEmiratesIdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    public void pickPhotos() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedImage = (Bitmap) extras.get("data");


                    // Bitmap newBitmap = Bitmap.createScaledBitmap(selectedImage, 400, 400, false);



                    /*Uri imageUri = CommonUtils.getImageUri(getApplicationContext(), selectedImage);
                    Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getRealPathFromURI(context, imageUri));*/
                    //profilePic.setImageBitmap(newBitmap);
                    // base64 = CommonUtils.encodeTobase64(newBitmap);


                    String timeStamp =
                            new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                    String imageFileName = "IMG_" + timeStamp + "_";
                    File storageDir =
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );

                    //imageFilePath = image.getAbsolutePath();


                   /*  Uri imageUri = CommonUtils.getImageUri(getApplicationContext(), selectedImage);
                     Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getRealPathFromURI(context, imageUri));*/

                    // base64 = CommonUtils.encodeTobase64(newBitmap);
                    //  Log.e("shnjksb",""+CommonUtils.encodeTobase64(newBitmap));

                    Bitmap newBitmap = CommonUtils.modifyOrientation2(selectedImage, image.getAbsolutePath());
                    if (mValueClick == 1) {
                        ((ImageView) findViewById(R.id.captured_photo_front_side)).setImageBitmap(Bitmap.createScaledBitmap(newBitmap, 500, 500, false));
                    } else if (mValueClick == 2) {
                        ((ImageView) findViewById(R.id.captured_photo_back_side)).setImageBitmap(Bitmap.createScaledBitmap(newBitmap, 500, 500, false));
                    }


                } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream, null, options);
                        // Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getPath(imageUri, context));

                        Bitmap newBitmap = CommonUtils.modifyOrientation2(selectedImage, CommonUtils.getPath(imageUri, context));
                        // profilePic.setImageBitmap(newBitmap);
                        //base64 = CommonUtils.encodeTobase64(newBitmap);
                        //Bitmap newBitmap = CommonUtils.modifyOrientation2(selectedImage, image.getAbsolutePath());
                        if (mValueClick == 1) {
                            ((ImageView) findViewById(R.id.captured_photo_front_side)).setImageBitmap(newBitmap);
                        } else if (mValueClick == 2) {
                            ((ImageView) findViewById(R.id.captured_photo_back_side)).setImageBitmap(newBitmap);
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }
}
