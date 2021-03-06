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

    private Intent qrintent;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        qrintent=this.getIntent();

        System.out.println("Tiempo qr: "+qrintent.getExtras().getLong("tiempo"));
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
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra("t1",qrintent.getExtras().getBoolean("t1"));
        intent.putExtra("t2",qrintent.getExtras().getBoolean("t2"));
        intent.putExtra("t3",qrintent.getExtras().getBoolean("t3"));
        intent.putExtra("tiempo",qrintent.getExtras().getLong("tiempo"));
        startActivity(intent);
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
            winActivity.putExtra("t1",qrintent.getExtras().getBoolean("t1"));
            winActivity.putExtra("t2",qrintent.getExtras().getBoolean("t2"));
            winActivity.putExtra("t3",qrintent.getExtras().getBoolean("t3"));
            winActivity.putExtra("tiempo",qrintent.getExtras().getLong("tiempo"));
            startActivity(winActivity);
        }
    }
}