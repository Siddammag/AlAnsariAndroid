package app.alansari.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Parveen Dala on 12 March, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class FileUtils {

    private final static String directoryName = "Al_Ansari";


    // Checks if external storage is available for read and write
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Checks if external storage is available to at least read
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getExternalStorageDir(String directoryName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), directoryName);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    LogUtils.e("ok", "Al Ansari External Directory not created");
                    return null;
                }
            }
            return file;
        } catch (NoSuchFieldError ex) {
            return null;
        }
    }

    public static File getInternalStorageDir(Context context, String directoryName) {
        // Get the directory for the app's private pictures directory.
        try {
            File file = new File(context.getExternalFilesDir(null), directoryName);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    LogUtils.e("ok", "Al_Ansari Internal Directory not created");
                    return null;
                }
            }
            return file;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void saveScreenShot(final Context context, Bitmap bitmap) {
        try {
//            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "AL_Ansari_" + System.currentTimeMillis() + ".png", "Al Ansari Transaction");
            File folder;
            if (isExternalStorageWritable()) {
                folder = getExternalStorageDir(directoryName);
                if (folder == null) {
                    folder = getInternalStorageDir(context, directoryName);
                }
            } else {
                folder = getInternalStorageDir(context, directoryName);
            }

            if (folder == null) {
                Toast.makeText(context, "Unable to read mobile directory.", Toast.LENGTH_SHORT).show();
                return;
            }

            File imagePath = new File(folder, "Al_Ansari_Txn_" + System.currentTimeMillis() + ".png");

            if (imagePath != null) {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(imagePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imagePath)));
                    MediaScannerConnection.scanFile(
                            context,
                            new String[]{imagePath.getAbsolutePath()},
                            null,
                            null);
                    Toast.makeText(context, "Saved to Gallery.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    LogUtils.e("GREC", e.getMessage(), e);
                } catch (IOException e) {
                    Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    LogUtils.e("GREC", e.getMessage(), e);
                }
            } else {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }
}
