package com.glzlaohuai.permissionhandler;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by glzlaohuai on 16/7/1 上午10:54.
 */
public class PermissionHandler {
   private Map<Integer, PermissionRequestCallback> callbackMap = new HashMap<>();


   /**
    * 将未授权的权限过滤出来
    *
    * @param context
    * @param permissions
    * @return
    */
   private static String[] filterUngrantedPermission(Context context, String... permissions) {
      List<String> permissionList = new ArrayList<>();
      for (String permission :
              permissions) {
         if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission);
         }
      }
      String[] result = new String[permissionList.size()];
      return permissionList.toArray(result);
   }


   /**
    * 对activityCompat的requestPermission的包装，只是permissions变为可变参数而不是字符串数组，更加好用
    *
    * @param activity
    * @param permissions
    */
   private static String[] requestPermission(Activity activity, int requestCode, String... permissions) {
      String[] needRequestPermissions = filterUngrantedPermission(activity, permissions);
      if (needRequestPermissions.length > 0) {
         ActivityCompat.requestPermissions(activity, needRequestPermissions, requestCode);
      }
      return needRequestPermissions;
   }


   public static int getGrantResult(int[] grantResults) {
      int result = 0;
      for (int temp :
              grantResults) {
         if (temp == PackageManager.PERMISSION_GRANTED) {
            result++;
         }
      }
      return result;
   }


   /**
    * 申请权限，注意，该方法内部会自动过滤已经授权完成的权限，如果全部权限都已经授权，那么会直接回调成功
    *
    * @param activity
    * @param requestCode
    * @param callback
    * @param permissions
    */
   public boolean requestPermission(Activity activity, int requestCode, PermissionRequestCallback callback, String... permissions) {
      boolean shouldRequestPermission = true;
      if (callback == null) {
         throw new RuntimeException("PermissionRequestCallback can not be null");
      }
      callbackMap.put(requestCode, callback);
      if (requestPermission(activity, requestCode, permissions).length == 0) {
         callback.success();
         shouldRequestPermission = false;
      }
      return shouldRequestPermission;
   }

   /**
    * 在{@link Activity#onRequestPermissionsResult(int, String[], int[])}中添加该方法，用以将申请结果通知到该类
    *
    * @param requestCode
    * @param permissions
    * @param grantResults
    */
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      if (callbackMap.keySet().contains(requestCode)) {
         PermissionRequestCallback callback = callbackMap.get(requestCode);
         if (callback == null) {
            throw new RuntimeException("find no PermissionRequestCallback for requestCode: " + requestCode);
         }
         if (permissions.length == getGrantResult(grantResults) && permissions.length > 0) {
            callback.success();
         } else {
            callback.failed();
         }
      }
   }


   /**
    * 权限授权结果回调
    */
   public interface PermissionRequestCallback {
      void success();

      void failed();
   }


}
