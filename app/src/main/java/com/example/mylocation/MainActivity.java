package com.example.mylocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.LocationServices;
import android.hardware.FusedLocationProviderClient;
import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;




public class GPSPermissionActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;    // 현재 위치를 가져오기 위한 변수
    int REQUEST_CODE = 1;
    TextView location_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s_permission);

        //FusedLocationProviderClient의 인스턴스를 생성.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위도 경도를 보여줄 뷰를 inflate.
        location_view = findViewById(R.id.location);

        // 위치를 가져오기 위한 버튼.
        Button btn_get_location = findViewById(R.id.btn_get_location);
        btn_get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GPS 권한 요청.
                ActivityCompat.requestPermissions(GPSPermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {   // 권한요청이 허용된 경우

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();    // 권한요청이 거절된 경우
                    return;
                }

                // 위도와 경도를 가져온다.
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // 파라미터로 받은 location을 통해 위도, 경도 정보를 텍스트뷰에 set.
                                    location_view.setText("위도: " + location.getLatitude() + " / 경도: " + location.getLongitude());
                                }
                            }
                        });
            }
            else{
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();    // 권한요청이 거절된 경우
            }
        }
    }
}