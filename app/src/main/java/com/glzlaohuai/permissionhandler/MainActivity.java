package com.glzlaohuai.permissionhandler;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

   private PermissionHandler permissionHandler = new PermissionHandler();


   private static final int REQUEST_CODE_CAMERA = 0;
   private static final int REQUEST_CODE_LOCATION = 1;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);


      View.OnClickListener clickListener = new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            switch (v.getId()) {
               case R.id.camera:
                  requestCameraPermission();
                  break;
               case R.id.location:
                  requestLocationPermission();
                  break;
            }
         }
      };


      findViewById(R.id.camera).setOnClickListener(clickListener);
      findViewById(R.id.location).setOnClickListener(clickListener);
   }


   private void requestCameraPermission() {
      permissionHandler.requestPermission(this, REQUEST_CODE_CAMERA, new PermissionHandler.PermissionRequestCallback() {
         @Override
         public void success() {

         }

         @Override
         public void failed() {

         }
      }, Manifest.permission.CAMERA);
   }


   private void requestLocationPermission() {
      permissionHandler.requestPermission(this, REQUEST_CODE_CAMERA, new PermissionHandler.PermissionRequestCallback() {
         @Override
         public void success() {

         }

         @Override
         public void failed() {

         }
      }, Manifest.permission.ACCESS_FINE_LOCATION);

   }


   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
   }
}
