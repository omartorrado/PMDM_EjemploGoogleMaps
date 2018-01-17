package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Result lastResult;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if(rawResult!=lastResult) {
            Log.i("QRCode", rawResult.getText());
            lastResult=rawResult;
            //Toast.makeText(this, lastResult.getText(), Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);
            Intent winActivity=new Intent(getApplicationContext(),WinActivity.class);
            winActivity.putExtra("qrText",lastResult.getText());
            startActivity(winActivity);
        }
    }
}