package com.dukeai.manageloads.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.apiUtils.AppHelper;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

public class Utilities {

    public static final int CONTACT_PICKER_REQUEST = 177;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7;
    private static final String TAG = "UTILITIES";
    static String REPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    static String REPORT_DATE = "yyyy-MM-dd";
    private static boolean isFileNameAvailable = false;
    private static String currentFileName = "";

    public static String getStrings(Context context, int id) {
        String value = null;
        if (context != null && id != -1) {
            value = context.getResources().getString(id);
        }
        return value;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Drawable getDrawable(Context context, int drawableId) {
        Drawable value = null;
        if (context != null && drawableId != -1) {
            value = ContextCompat.getDrawable(context, drawableId);
        }
        return value;
    }


    private static String getCurrentYearFirstDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String date = year + "-01-01";
        return date;
    }

    private static String getCustomDate() {

        return "";

    }


    public static String getEndDateReport(int i) {
        if(i==1){
            return getLastDateOfLastMonth();
        }else if(i==2){
           return getReportLastQuarterEndDate();
       }else{
           return getReportCurrentDate();
       }
//        return getReportCurrentDate();

    }

    public static String getStartDate(int i) {
        String toDate = "";
        switch (i) {
            case 0:
                /**Last Week**/
                return getReportLastWeekDate();
            case 1:
                /**Last Month**/
//                return getReportLastMonthDate();
                return  getFirstDateOfLastMonth();
            case 2:
                /**Last Quarter**/
                return getReportLastQuarterDate();
            case 3:
                /**Year To Date**/
                return getCurrentYearFirstDate();
            case 4:
                return getCustomDate();
        }
        return toDate;
    }



    public static String getFileName(int index) {
        return "IMG_" + getReportCurrentDateAndTime() + "_" + index + ".jpg";
    }

    public static String getReportCurrentDateAndTime() {
        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        try {
            return URLEncoder.encode(df.format(new Date()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    public static String getReportCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        df.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        try {
            return URLEncoder.encode(df.format(new Date()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void navigateToProfilePage(FragmentActivity fragmentActivity) {
//        NavigationFlowManager.openFragments(new ProfileFragment(), null, fragmentActivity, R.id.dashboard_wrapper);
    }

    public static String getReportLastWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        df.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        try {
            return URLEncoder.encode(df.format(cal.getTime()), "utf-8");
        } catch (Exception ignored) {
            return null;
        }
    }


    public static String getFirstDateOfLastMonth(){
        SimpleDateFormat shortSdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH);
        c.add(Calendar.MONTH,-1);

        Log.d("Current Month ", " " + c.getTime());

        Date fromDateQuater = null;

        try {
            c.set(Calendar.DATE, 1);
            fromDateQuater = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);

        try {
            return  URLEncoder.encode(df.format(fromDateQuater), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLastDateOfLastMonth(){
        SimpleDateFormat shortSdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();

        Date toDateQuater = null;
        try {
            c1.add(Calendar.MONTH,-1);
            c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
            toDateQuater = longSdf.parse(shortSdf.format(c1.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        df.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        try {
            return  URLEncoder.encode(df.format(toDateQuater), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getFormattedDate(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return URLEncoder.encode(df.format(calendar.getTime()), "utf-8");

        } catch (Exception ignored) {
            return null;
        }
    }

    public static String getReportLastQuarterDate() {

        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        SimpleDateFormat longSdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH);

        if (currentMonth == 1 || currentMonth == 2 || currentMonth == 3) {
            currentMonth = 1;
        } else if (currentMonth == 4 || currentMonth == 5 || currentMonth == 6) {
            currentMonth = 4;
        } else if (currentMonth == 7 || currentMonth == 8 || currentMonth == 9) {
            currentMonth = 7;
        } else if (currentMonth == 10 || currentMonth == 11 || currentMonth == 12) {
            currentMonth = 10;
        }

        currentMonth = currentMonth - 3;
        Log.d("Current Month ", " " + currentMonth);
        Date fromDateQuater = null;/*w  w  w  .  j a va 2 s . c  om*/
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
//            fromDateQuater = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
            return URLEncoder.encode(df.format(c.getTime()), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static String getReportLastQuarterEndDate() {

        SimpleDateFormat df = new SimpleDateFormat(REPORT_DATE);
        SimpleDateFormat shortSdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH);

        if (currentMonth == 1 || currentMonth == 2 || currentMonth == 3) {
            currentMonth = 1;
        } else if (currentMonth == 4 || currentMonth == 5 || currentMonth == 6) {
            currentMonth = 4;
        } else if (currentMonth == 7 || currentMonth == 8 || currentMonth == 9) {
            currentMonth = 7;
        } else if (currentMonth == 10 || currentMonth == 11 || currentMonth == 12) {
            currentMonth = 10;
        }

        currentMonth = currentMonth - 3;
        Log.d("Current Month ", " " + currentMonth);
        Date fromDateQuater = null;/*w  w  w  .  j a va 2 s . c  om*/
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            fromDateQuater = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Last Month Before ", currentMonth + "");

        Date toDateQuater = null;/*w  w  w  .  j a va 2 s . c  om*/
        currentMonth = currentMonth + 2;
        Log.d("Last Month After", currentMonth + "");
        try {
            c1.set(Calendar.MONTH, currentMonth - 1);
            c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
//            toDateQuater = longSdf.parse(shortSdf.format(c1.getTime()) + " 00:00:00");
            return URLEncoder.encode(df.format(c1.getTime()), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static String getReportType(String str) {
        if (str == null) {
            return "";
        }
        switch (str) {
            case AppConstants.ReportConstants.BALANCESHEET:
                return AppConstants.ReportConstants.BALANCE_SHEET;
            case AppConstants.ReportConstants.EXPENSES:
                return AppConstants.ReportConstants.EXPENSE;
            case AppConstants.ReportConstants.YTD:
                return AppConstants.ReportConstants.YTD_TEXT;
            case AppConstants.ReportConstants.PL:
                return AppConstants.ReportConstants.PL_TEXT;
            case AppConstants.ReportConstants.LIABILITIES:
                return AppConstants.ReportConstants.LIABILITIES_TEXT;
            case AppConstants.ReportConstants.IFTA:
                return AppConstants.ReportConstants.IFTA;
            case AppConstants.ReportConstants.POD:
                return AppConstants.ReportConstants.POD;
            case AppConstants.ReportConstants.SELFTAX:
                return AppConstants.ReportConstants.selftax;
            case AppConstants.ReportConstants.FEDTAX:
                return AppConstants.ReportConstants.fedtax;
        }
        return "";
    }

    public static void resetGlobalData() {
        Duke.isWithOutToken = false;
//        Duke.fileStatusModel = new FileStatusModel();
        Duke.FileStoragePath = "";
        Duke.imageStoragePath = "";
        Duke.ProfileImage = null;
        Duke.userName = null;
        Duke.deviceToken = null;
        Duke.referralId = null;
        resetFileData();
    }


    public static void resetFileData() {
        Duke.uploadingImagesList = new ArrayList<>();
        Duke.uploadingImageStoragePaths = new ArrayList<>();
        Duke.sortedUploadingImageStoragePaths = new ArrayList<>();
        Duke.sortedImagesList = new ArrayList<>();
    }

    public static MultipartBody.Part[] getMultipartBody(ArrayList<String> bitmaps, Boolean isProfileUpload, int width, int height) {

        MultipartBody.Part[] list = new MultipartBody.Part[bitmaps.size()];
        for (int i = 0; i < bitmaps.size(); i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            options.inDither = true;
            if (isProfileUpload)
                options.inSampleSize = calculateInSampleSize(options, width, height);
            try {
                Bitmap bm = BitmapFactory.decodeFile(bitmaps.get(i), options);
                if (isProfileUpload) {
                    bm = getPortraitResizedBitmap(bm, 480, 320);
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
                } else {
                    bm = getPortraitResizedBitmap(bm, 2160, 1440);
                    bm.compress(Bitmap.CompressFormat.JPEG, 72, baos);
                }
                byte[] byteArrayImage = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), encodedImage);

                String fileName = "";
                if (isProfileUpload) {
                    fileName = getProfileImageName();
                } else {
                    fileName = getFileName(i);
                }
                MultipartBody.Part body = MultipartBody.Part.createFormData(fileName, fileName, requestFile);

                list[i] = body;
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }
        return list;
    }

    public static MultipartBody.Part[] getPDFMultipartBody(Activity activity, ArrayList<String> pdfURIList, ArrayList<Uri> uris, int width, int height) {

        String resultBase64Encoded = "";

        MultipartBody.Part[] list = new MultipartBody.Part[uris.size()];
        for (int i = 0; i < uris.size(); i++) {

        try {
            InputStream in = activity.getContentResolver().openInputStream(uris.get(i));

            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), resultBase64Encoded);
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            String fileName = getReportCurrentDateAndTime() + "_" + i + ".pdf";

            Duke.PDFDocFilenames.add(fileName);
//
            MultipartBody.Part body = MultipartBody.Part.createFormData(fileName, fileName, requestFile);
            list[i] = body;
//                }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        }
        return list;
    }

    public static MultipartBody.Part getFileCountArray(ArrayList<Integer> bitmapCount) {
        if (bitmapCount != null && bitmapCount.size() > 0) {
            String str = Arrays.toString(bitmapCount.toArray());
            MultipartBody.Part body = MultipartBody.Part.createFormData(ApiConstants.UploadDocuments.FILE_COUNT, str);
            return body;
        }
        return null;
    }

    public static String getProfileImageName() {
        return AppHelper.getCurrUser() + "_profile.jpg";
    }




    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                Uri contentUri = null;
                try {
                    String id = DocumentsContract.getDocumentId(uri);
                    contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } catch (Exception ignored) {

                }


                return getDataColumn(context, contentUri, null, null);
            } else if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else if ("document".equals(type)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                    }
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }else if(isExternalStorageDocument(uri)){
            Log.d("PM -> ","isExternalStorageDocument");
        }
        // MediaStore (and general)
        else if (uri.getScheme()!=null) {

            if("content".equalsIgnoreCase(uri.getScheme())){
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        if (uri != null) {
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } catch (Exception e) {
                String error = e.getLocalizedMessage();
                Log.d("Error fetching doc", error);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private static String bodyAsString(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readString(body.contentType().charset());
        } catch (IOException ignored) {
            return "";
        }
    }

//    public static boolean writeResponse(Activity activity, ResponseBody body) {
//        File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Download/");
//        if (!testDirectory.exists()) {
//            testDirectory.mkdir();
//        }
//
//        String folderName = "DUKE.AI";
//        String sub_folder = "POD Documents";
//
//        File myDir0 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), folderName);
//        if (!myDir0.exists()) {
//            myDir0.mkdirs();
//        }
//
//        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName, sub_folder);
//        if (!myDir.exists()) {
//            myDir.mkdirs();
//        }
//        File extStore = Environment.getExternalStorageDirectory();
//
//
//        FileOutputStream fos = null;
//        try {
//            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
//            Duke.PODDocumentsPath = timeStamp;
//
//            File myDir1 = new File(extStore + "/" + folderName + "/" + sub_folder, timeStamp);
//            if (!myDir1.exists()) {
//                myDir1.mkdirs();
//            }
//
//            File file;
//            InputStream in = null;
//
//            if(isFileNameAvailable && currentFileName.length()>0) {
//                currentFileName = currentFileName.substring(0, currentFileName.length()-4);
//                Duke.FileStoragePath = extStore + "/" + folderName + "/" + sub_folder + "/" + timeStamp + "/" + currentFileName + ".pdf";
//                file = new File(myDir1, currentFileName + ".pdf");
//                Duke.podDocsStoragePaths.add(Duke.FileStoragePath);
//            } else {
//                Duke.FileStoragePath = extStore + "/" + folderName + "/" + sub_folder + "/" + timeStamp + "/" + timeStamp + ".pdf";
//                file = new File(myDir1, timeStamp + ".pdf");
//            }
//
//            in = body.byteStream();
//            fos = new FileOutputStream(file);
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = in.read(buffer)) > 0) {
//                try {
//                    fos.write(buffer, 0, len1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            fos.close();
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }

    public static boolean writeResponse(Activity activity, ResponseBody body) {
//        File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Download/");
//        if (!testDirectory.exists()) {
//            testDirectory.mkdir();
//        }

        String folderName = "DUKE.AI";
        String sub_folder = "POD Documents";

//        File myDir0 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), folderName);
//        if (!myDir0.exists()) {
//            myDir0.mkdirs();
//        }
//
//        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName, sub_folder);
//        if (!myDir.exists()) {
//            myDir.mkdirs();
//        }
        File extStore = Environment.getExternalStorageDirectory();


        FileOutputStream fos = null;
        OutputStream outputStream = null;
        FileOutputStream fos2 = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
//            Duke.PODDocumentsPath = timeStamp;
//
            File myDir1 = new File(extStore + "/" + folderName);
            if (!myDir1.exists()) {
                myDir1.mkdirs();
            }
//
            File file;
            InputStream in = null;
//
//            if(isFileNameAvailable && currentFileName.length()>0) {
//                currentFileName = currentFileName.substring(0, currentFileName.length()-4);
//                Duke.FileStoragePath = extStore + "/" + folderName + "/" + sub_folder + "/" + timeStamp + "/" + currentFileName + ".pdf";
//                file = new File(myDir1, currentFileName + ".pdf");
//                Duke.podDocsStoragePaths.add(Duke.FileStoragePath);
//            } else {
//                Duke.FileStoragePath = extStore + "/" + folderName + "/" + sub_folder + "/" + timeStamp + "/" + timeStamp + ".pdf";
//                file = new File(myDir1, timeStamp + ".pdf");
//            }

            ContentResolver contentResolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
// save to a folder
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + folderName + "/" + timeStamp + ".pdf");
            Uri uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);

// You can use this outputStream to write whatever file you want:
//            fos = (FileOutputStream) contentResolver.openOutputStream(uri);
            outputStream = contentResolver.openOutputStream(uri);
//            Duke.FileStoragePath = contentValues.getAsString(MediaStore.MediaColumns.RELATIVE_PATH);
            Duke.FileStoragePath = Environment.DIRECTORY_DOWNLOADS + "/" + folderName + "/" + timeStamp + ".pdf";
            Duke.FileStoragePathURI = uri;

            in = body.byteStream();
//            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                try {
//                    fos.write(buffer, 0, len1);
                    outputStream.write(buffer, 0, len1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            fos.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean writeResponse(Activity activity, ResponseBody body, String fileName) {
        fileName = fileName.substring(0, fileName.length() - 4);
        File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Download/");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }

        String folderName = "DUKE.AI";
        String sub_folder = "POD Documents";

        File myDir0 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), folderName);
        if (!myDir0.exists()) {
            myDir0.mkdirs();
        }

        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName, sub_folder);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File extStore = Environment.getExternalStorageDirectory();


        FileOutputStream fos = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            Duke.PODDocumentsPath = timeStamp;

            File myDir1 = new File(extStore + "/" + folderName + "/" + sub_folder, fileName);
            if (!myDir1.exists()) {
                myDir1.mkdirs();
            }

            Duke.FileStoragePath = extStore + "/" + folderName + "/" + sub_folder + "/" + timeStamp + "/" + fileName + "/" + fileName + ".pdf";

            File file = new File(myDir1, fileName + ".pdf");
            fos = new FileOutputStream(file);
            Duke.podDocsStoragePaths.add(Duke.FileStoragePath);
            InputStream in = body.byteStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                try {
                    fos.write(buffer, 0, len1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean writeResponseBodyToDisk(Activity activity, ResponseBody body) {
        Boolean isWritten = false;
        isWritten = writeResponse(activity, body);
        return isWritten;
    }

    public static boolean writeResponseBodyToDisk(Activity activity, ResponseBody body, String fileName) {
        Boolean isWritten = false;
        if (fileName.length() > 0) {
            isFileNameAvailable = true;
            currentFileName = fileName;
        }
        isWritten = writeResponse(activity, body);
        return isWritten;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        if (image == null) {
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getPortraitResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        try {

            int width = bm.getWidth();
            int height = bm.getHeight();
            float deg = 0;

//        if (width > height) {
////            width = bm.getHeight();
////            height = bm.getWidth();
//            deg = 90;
//        }
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // create a matrix for the manipulation
//        Matrix matrix = new Matrix();
//        // resize the bit map
//
//        if(width>height){
//            matrix.postRotate(deg,scaleWidth, scaleHeight);
//
//        }else {
//            matrix.postScale(scaleWidth, scaleHeight);
//
//        }


            if (width > height) {
//            width = bm.getHeight();
//            height = bm.getWidth();
            }

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map

            if (width > height) {

                matrix.postScale(scaleHeight, scaleHeight);
                matrix.postRotate(90);
            } else {
                matrix.postScale(scaleWidth, scaleHeight);

            }



            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        } catch (Exception e) {
            Log.d("Bitmap error", e.getLocalizedMessage());
            return null;
        }
    }

    public static int getScreenWidthInPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
//        float scaleFactor = dm.scaledDensity;
//        Log.d("Scaled factor", "sdf");
        return width;
    }

    public static int getScreenHeightInPixels(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    public static Bitmap decodeSampledBitmapFromResource(InputStream inputStream, int reqWidth, int reqHeight) {       // First decode with
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    /*public static void openContactsPickerAndShare(Activity activity, Context context, String message) {
        new MultiContactPicker.Builder(activity) //Activity/fragment context
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(context, R.color.blue_212537)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(context, R.color.blue_212537)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setMessage(message)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(Utilities.CONTACT_PICKER_REQUEST);
    }*/

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    /*public static boolean isRequestingLocationUpdates(Context context) {
        return context.getSharedPreferences(AppConstants.Ifta.LOCATION_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getBoolean(HomeFragment.LOCATION_SETTINGS_KEY, false);
    }*/

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param isRequesting The location updates state.
     */
/*    public static void setRequestingLocationUpdates(Context context, boolean isRequesting) {
        context.getSharedPreferences(AppConstants.Ifta.LOCATION_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(HomeFragment.LOCATION_SETTINGS_KEY, isRequesting)
                .apply();
    }*/

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public static boolean serviceIsRunningInForeground(Context context, Class cls) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (cls.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getLocalAddress(Context context, Location location) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());
        String code = "";
        String completeAddress = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() > 0) {
                Log.d("Address", " " + addresses.get(0));
                String city = addresses.get(0).getLocality() == null ? "" : addresses.get(0).getLocality();
                String locality = addresses.get(0).getSubLocality() == null ? "" : addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                Duke.stateName = state;
                if(shortenStateName(Duke.stateName).length()>0) {
                    Duke.stateName = shortenStateName(Duke.stateName);
                } else {
                    Duke.stateName = state;
                }
                completeAddress = addresses.get(0).getAddressLine(0);
                completeAddress = StringUtils.substringBeforeLast(completeAddress, ",");
                completeAddress = StringUtils.substringBeforeLast(completeAddress, ",") + ", " + Duke.stateName;
                code = city.equals("") ? (state) : (locality + ", " + state);
            }
            return completeAddress;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Is Google Play services available, It is important to have google play services updated
     */
    /*public static boolean isPlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return status == ConnectionResult.SUCCESS;
    }*/


    public static void saveBitmap(String path, Bitmap bitmap) {
        File file = new File(path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String shortenStateName(String state) {

        Map<String, String> states = new HashMap<String, String>();
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("Alberta", "AB");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "QC");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");

        if(states.containsKey(state)) {
            return states.get(state);
        }

        return "";
    }

}
