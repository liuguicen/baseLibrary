package com.lgc.baselibrary.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 主要用于获取图片文件夹下的所有图片的路径，图片大小支取5k-6000k的见方法注解
 *
 * @author acm_lgc
 * @version jdk 1.8, sdk 21
 */
public class FileTool {
    private static long lastTempTime = System.currentTimeMillis();

    /**
     * @return 包括点
     */
    @Nullable
    public static String getSuffix(String path) {
        if (path == null) return null;
        int dotId = path.lastIndexOf(".");
        if (0 <= dotId && dotId < path.length() - 1) { // 有后缀
            return path.substring(dotId);
        }
        return null;
    }

    public enum UrlType {
        FILE_PATH,
        URL
    }

    public static String getFileNameInPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    public static String getApplicationDir(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    /**
     * @param context
     * @return 后缀是PNG，表示希望PNG格式存储
     */
    public static String createTempPicPath(Context context) {
        String appPath = getApplicationDir(context);
        String tempDir = appPath + "/tempPic";
        File file = new File(tempDir);
        if (!file.exists()) {
            {
                if (!file.mkdirs()) {
                    Log.e("FileTool", "createTempPicPath: 创建文件失败");
                    return null;
                }
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date curDate = new Date(lastTempTime++);//获取当前时间
        String time = formatter.format(curDate);
        time += "_" + lastTempTime % 1000;
        String tempPath = tempDir + "/ptu" + time + ".png";
        return tempPath;
    }

    /**
     * 处理路径不存在的情况
     *
     * @param file 文件
     * @return 是否创建成功
     */
    public static boolean createNewFile(File file) {
        //处理目录
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs())
                return false;
        }
        //文件
        if (!file.exists())
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            //递归删除目录中的子目录下
            for (File file : children) {
                boolean success = deleteDir(file);
                if (!success) {
                    return false;
                }
            }
        }
        // 若是目录，此时为空，可以删除
        return dir.delete();
    }

    public static long getFileSize(File dir) {
        long size = 0;
        try {
            if (dir.isDirectory()) {
                File[] children = dir.listFiles();
                //递归删除目录中的子目录下
                for (File file : children) {
                    size += getFileSize(file);
                }
            } else
                size += dir.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除文件夹中所有存在的子文件
     */
    public static boolean deleteAllChileFile(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            //递归删除目录中的子目录下
            for (File file : children) {
                boolean success = deleteDir(file);
                if (!success) return false;
            }
        }
        return true;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */

    public static @Nullable
    String getImagePathFromUri(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getParentPath(String path) {
        return path.substring(0, path.lastIndexOf('/'));
    }

    /**
     * 获取所有的子文件,不存在时创建
     */
    public static File[] getAllChildFiles(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.listFiles();
    }

    public static String replaceSuffix(String path, String suffix) {
        if (path != null && suffix != null) {
            int dotId = path.lastIndexOf('.');
            if (dotId != -1) {
                path = path.substring(0, dotId);
            }
            if (suffix.charAt(0) == '.') {
                path += suffix;
            } else {
                path += '.' + suffix;
            }
        }
        return path;
    }


    /**
     * 判断Url的类型，目前只支持文件路径和一般url
     *
     * @param url
     * @return
     */
    public static UrlType urlType(@NonNull String url) {
        if (url.contains("://")) {
            return UrlType.URL;
        } else {
            return UrlType.FILE_PATH;
        }
    }


    /**
     * 获取SD卡缓存目录
     *
     * @param context 上下文
     * @param type    文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     *                {@link Environment#DIRECTORY_MUSIC},
     *                {@link Environment#DIRECTORY_PODCASTS},
     *                {@link Environment#DIRECTORY_RINGTONES},
     *                {@link Environment#DIRECTORY_ALARMS},
     *                {@link Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link Environment#DIRECTORY_PICTURES}, or
     *                {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache/" + type);
            }

            if (appCacheDir == null) {
                Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }
}
