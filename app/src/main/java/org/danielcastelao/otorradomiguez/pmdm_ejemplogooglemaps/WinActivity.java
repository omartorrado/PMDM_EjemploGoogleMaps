package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WinActivity extends AppCompatActivity {

    TextView winMessage;
    String qrMessage;
    Button botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        winMessage=(TextView)findViewById(R.id.textViewWin);
        botonVolver=(Button)findViewById(R.id.buttonVolver);
        qrMessage=getIntent().getExtras().getString("qrText");
        winMessage.setText(qrMessage);

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciamos la activity de escoger
                Intent intent=new Intent(getApplicationContext(),ChooseLevelActivity.class);
                startActivityForResult(intent,77);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 77) {
            if(resultCode == Activity.RESULT_OK){
                //Esta data creo que es la que devuelve los datos
                String result=data.getStringExtra("result");
                Toast.makeText(this, "Has elegido "+result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
