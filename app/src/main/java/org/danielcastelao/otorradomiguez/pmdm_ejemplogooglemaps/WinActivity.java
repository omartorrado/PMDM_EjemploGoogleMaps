package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class WinActivity extends AppCompatActivity {

    TextView winMessage;
    String qrMessage;
    Button botonVolver;
    Button botonSalir;
    GifImageView imageWin;

    String tesoro1Code;
    String tesoro2Code;
    String tesoro3Code;

    Intent intent;
    private Intent winIntent;

    private boolean tesoro1;
    private boolean tesoro2;
    private boolean tesoro3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        winMessage=(TextView)findViewById(R.id.textViewWin);
        botonVolver=(Button)findViewById(R.id.buttonVolver);
        botonSalir=(Button)findViewById(R.id.buttonSalir);
        qrMessage=getIntent().getExtras().getString("qrText");
        imageWin= (GifImageView) findViewById(R.id.gifView);

        //cargo el intent para meterle los extras
        intent=new Intent(getApplicationContext(),MapsActivity.class);
        winIntent=this.getIntent();
        intent.putExtra("t1",winIntent.getExtras().getBoolean("t1"));
        intent.putExtra("t2",winIntent.getExtras().getBoolean("t2"));
        intent.putExtra("t3",winIntent.getExtras().getBoolean("t3"));

        tesoro1=winIntent.getExtras().getBoolean("t1");
        tesoro2=winIntent.getExtras().getBoolean("t2");
        tesoro3=winIntent.getExtras().getBoolean("t3");

        //comprobamos que ha encontrado el qr correcto todo esta puesto uno de ejemplo
        if(qrMessage.matches("es.omar.tm.Tesoro1")){
            winMessage.setText("Has encontrado el primer tesoro");
            intent.putExtra("t1",true);
            if(tesoro2==true&&tesoro3==true){
                //Si ha encontrado todos oculta el boton volver, muestra un boton salir, cambia el gif y cambia el mensaje
                imageWin.setImageResource(R.drawable.fin);
                winMessage.setText("Bien hecho");
                botonVolver.setVisibility(View.GONE);
                botonSalir.setVisibility(View.VISIBLE);
            }
            imageWin.setVisibility(View.VISIBLE);
        }else if(qrMessage.matches("es.omar.tm.Tesoro2")){
            winMessage.setText("Has encontrado el segundo tesoro");
            intent.putExtra("t2",true);
            if(tesoro1==true&&tesoro3==true){
                //Si ha encontrado todos oculta el boton volver, muestra un boton salir, cambia el gif y cambia el mensaje
                imageWin.setImageResource(R.drawable.fin);
                winMessage.setText("Bien hecho");
                botonVolver.setVisibility(View.GONE);
                botonSalir.setVisibility(View.VISIBLE);
            }
            imageWin.setVisibility(View.VISIBLE);
        }else if(qrMessage.matches("es.omar.tm.Tesoro3")){
            winMessage.setText("Has encontrado el tercer tesoro");
            intent.putExtra("t3",true);
            if(tesoro2==true&&tesoro1==true){
                //Si ha encontrado todos oculta el boton volver, muestra un boton salir, cambia el gif y cambia el mensaje
                imageWin.setImageResource(R.drawable.fin);
                winMessage.setText("Bien hecho");
                botonVolver.setVisibility(View.GONE);
                botonSalir.setVisibility(View.VISIBLE);
            }
            imageWin.setVisibility(View.VISIBLE);
        }else{
            winMessage.setText("No es el QR correcto, sigue buscando");
            imageWin.setVisibility(View.GONE);
        }



        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                //Iniciamos la activity de escoger
                Intent intent=new Intent(getApplicationContext(),ChooseLevelActivity.class);
                startActivityForResult(intent,77);
                */

                startActivity(intent);
            }
        });
    }

    //Esto ccorresponde al startActivityForResult de la ChooseActivity (que no uso de momento)
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
