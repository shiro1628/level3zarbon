package org.analysis.shiro.level3_zarbon;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private void showAlert(String message) {
        AlertDialog v0 = new AlertDialog.Builder(this).create();
        v0.setTitle(message);
        v0.setMessage("bypass and then kill a zarbon");
        v0.setButton(AlertDialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {
            System.exit(0);
        });
        v0.setCancelable(false);
        v0.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 암호화된 DEX 파일을 복호화 및 로드하여 루팅 감지
        try {
            String result = DexLoader.loadEncryptedDex(this);  // 루팅 결과 받아오기
            if ("y".equals(result)) {
                showAlert("Root detected!");  // 루팅 탐지 시 경고창 표시
            }else{
                Log.e(TAG, "result : "+ result);
            }
            // 루팅이 감지되지 않으면 아무 작업도 하지 않음
        } catch (Exception e) {
            Log.e(TAG, "Error in root detection", e);
        }
    }
}
