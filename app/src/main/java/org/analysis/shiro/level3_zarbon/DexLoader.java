package org.analysis.shiro.level3_zarbon;

import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DexLoader {

    private static final String key = "mysecretaeskey12";  // 16바이트 AES 키
    private static final String iv = "initialvector123";  // 16바이트 IV
    private static final String TAG = "DexLoader";

    // 암호화된 DEX 파일을 복호화하고 로드하는 메서드
    public static String loadEncryptedDex(Context context) throws Exception {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            // AES 키 및 IV 설정
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            // Cipher 초기화 (AES/CBC/PKCS5Padding 모드 사용)
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            // assets에서 암호화된 DEX 파일 읽기
            inputStream = context.getAssets().open("encrypted_classes.dex");
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0, len);
            }
            byte[] encryptedDex = byteStream.toByteArray();
            Log.d(TAG, "Encrypted DEX size: " + encryptedDex.length);

            // 복호화
            byte[] decryptedDex = cipher.doFinal(encryptedDex);
            Log.d(TAG, "Decrypted DEX size: " + decryptedDex.length);

            // 복호화된 DEX 파일을 임시로 저장
            File cacheDir = context.getDir("dex", Context.MODE_PRIVATE);
            File tempDexFile = new File(cacheDir, "temp.dex");
            fos = new FileOutputStream(tempDexFile);
            fos.write(decryptedDex);
            Log.d(TAG, "Decrypted DEX saved to: " + tempDexFile.getAbsolutePath());

            // 임시 DEX 파일이 존재하는지 확인
            if (tempDexFile.exists()) {
                Log.d(TAG, "Temp DEX file exists: " + tempDexFile.getAbsolutePath());
            } else {
                Log.e(TAG, "Temp DEX file does not exist.");
                return "n";  // 파일이 없으면 예외 대신 기본 값 반환
            }

            // DexClassLoader로 복호화된 DEX 파일 로드
            DexClassLoader dexClassLoader = new DexClassLoader(
                    tempDexFile.getAbsolutePath(),
                    cacheDir.getAbsolutePath(),
                    null,
                    context.getClassLoader()
            );

            // 로드한 z 클래스를 가져와서 d() 메서드 실행
            try {
                Class<?> loadedClass = dexClassLoader.loadClass("org.analysis.shiro.level3_zarbon.z");
                Log.d(TAG, "Class z loaded successfully");

                Object result = loadedClass.getMethod("d").invoke(null);
                Log.d(TAG, "Root detection result: " + result.toString());

                return result.toString();

            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Class z not found", e);
                throw e;
            } catch (Exception e) {
                Log.e(TAG, "Error invoking d() method", e);
                throw e;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in loadEncryptedDex", e);
            throw e;
        } finally {
            // 리소스 해제
            if (inputStream != null) {
                inputStream.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
