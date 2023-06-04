package com.example.proyecto2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Vistas
    EditText et_nombre;
    TextView tv_bestScore;
    ImageView iv_personaje;
    Button btn_jugar;

    //Sonido
    MediaPlayer mp;

    int imageId;

    @SuppressLint({"DiscouragedApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Para el toolbar
        setSupportActionBar(findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Vistas
        et_nombre=findViewById(R.id.nombre);
        tv_bestScore=findViewById(R.id.bestScore);
        iv_personaje=findViewById(R.id.personaje);
        btn_jugar=findViewById(R.id.jugar);
        mp= MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper( this,"db",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        @SuppressLint("Recycle") Cursor consulta = db.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record:"+ temp_score + "de "+temp_nombre);
            db.close();
        }
    }
    public void jugar(View view){
        String nombre = et_nombre.getText().toString();
        if(!nombre.equals("")){
            mp.stop();
            mp.release();
            Intent intent = new Intent(this,Nivel1.class);
            intent.putExtra("jugador",nombre);
            intent.putExtra("selectedImageId", imageId);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"Debe escribir su nombre",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);

        }
    }
    @Override
    public void onBackPressed(){

    }
    private final int[] imageIds = {
            R.drawable.mango,
            R.drawable.fresa,
            R.drawable.sandia,
            R.drawable.naranja,
            R.drawable.manzana,
            R.drawable.uva
    };

    private int currentImageIndex = 0;

    public void onCharacterImageClicked(View view) {
        imageId= imageIds[currentImageIndex];

        ((ImageView) view).setImageResource(imageId);

        currentImageIndex++;

        if (currentImageIndex >= imageIds.length) {
            currentImageIndex = 0;
        }

    }



}