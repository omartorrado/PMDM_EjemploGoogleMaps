package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ChooseLevelActivity extends AppCompatActivity {

    /*
    Esta clase la vamos a usar para escoger que dificultad a  buscar de siguiente
     */
    RadioButton selectedButton;

    RadioGroup rg;

    Button botonChoose;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);

        rg=(RadioGroup)findViewById(R.id.radioGroup);

        botonChoose=(Button)findViewById(R.id.buttonChoose);

        intent=getIntent();


        botonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedButton=(RadioButton)findViewById(rg.getCheckedRadioButtonId());
                //Toast.makeText(view.getContext(), selectedButton.getText(), Toast.LENGTH_SHORT).show();
                intent.putExtra("result",selectedButton.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }
}
