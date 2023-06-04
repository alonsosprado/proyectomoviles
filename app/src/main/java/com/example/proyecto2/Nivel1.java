package com.example.proyecto2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class Nivel1 extends AppCompatActivity {

   TextView tv_nombre,tv_score;
   ImageView iv_Auno,iv_Ados,iv_Vidas,iv_Personaje;
   EditText et_Respuesta;
   MediaPlayer mp,mpGreat,mpBad;

   int score,numAleatorio_uno,numAleatorio_dos,resultado,vidas=3;

   String nombre_jugador,string_score,string_vidas;

   String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel1);

        Toast.makeText(this,"Nivel 1- Sumas basicas",Toast.LENGTH_SHORT).show();
        tv_nombre = findViewById(R.id.textView_Nombre);
        tv_score = findViewById(R.id.textView_Score);
        iv_Vidas = findViewById(R.id.imageView_Manzanas);
        iv_Auno= findViewById(R.id.imageView_NumeroUno);
        iv_Ados = findViewById(R.id.imageView_NumeroDos);
        iv_Personaje = findViewById(R.id.imageView2);
        et_Respuesta = findViewById(R.id.et_Resultado);

        nombre_jugador= getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: "+nombre_jugador);

        int selectedImageId = getIntent().getIntExtra("selectedImageId", -1);

        if (selectedImageId != -1) {

            iv_Personaje.setImageResource(selectedImageId);
        }

        setSupportActionBar(findViewById(R.id.myToolbar2));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        mp= MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);


        mpGreat= MediaPlayer.create(this,R.raw.wonderful);
        mpBad= MediaPlayer.create(this,R.raw.bad);


        numeroAleatorio();

    }
    public void comparar(View vista){
        String respuesta = et_Respuesta.getText().toString();
        if(!respuesta.equals("")){
            int respuestaJugador = Integer.parseInt(respuesta);
            if(resultado == respuestaJugador){
                mpGreat.start();
                score++;
                tv_score.setText("Score:"+score);
            }else{
                mpBad.start();
                vidas--;
                switch (vidas){
                    case 3:
                        iv_Vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        iv_Vidas.setImageResource(R.drawable.dosvidas);
                        Toast.makeText(this,"Quedan 2 manzanas",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        iv_Vidas.setImageResource(R.drawable.unavida);
                        Toast.makeText(this,"Queda 1 manzana",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(this,"Has perdido todas tus manzanas",Toast.LENGTH_SHORT).show();
                        mp.stop();
                        mp.release();
                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
            baseDeDatos();
            et_Respuesta.setText("");
            numeroAleatorio();
        }else{
            Toast.makeText(this,"Debes dar una respuesta",Toast.LENGTH_SHORT).show();
        }
    }
    private void numeroAleatorio() {
        if(score <= 9){
            numAleatorio_uno =(int)(Math.random() *10);
            numAleatorio_dos =(int)(Math.random() *10);

            resultado = numAleatorio_uno + numAleatorio_dos;

            if(resultado <= 10){
                for(int i = 0;i < numero.length;i++){
                    int id = getResources().getIdentifier(numero[i],"drawable",getPackageName());
                    if(numAleatorio_uno == i){
                        iv_Auno.setImageResource(id);
                    }
                    if(numAleatorio_dos == i){
                        iv_Ados.setImageResource(id);
                    }
                }
            }else{
                numeroAleatorio();
            }
        }else{
            Intent intent = new Intent(this,MainActivity2_Nivel2.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);

            intent.putExtra("jugador",nombre_jugador);
            intent.putExtra("score",string_score);
            intent.putExtra("vidas",string_vidas);

            mp.stop();
            mp.release();
            startActivity(intent);
            finish();
        }
    }
    public void baseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"db",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor consulta = db.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null
        );
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            int bestScore = Integer.parseInt(temp_score);

            if(score > bestScore) {
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);
                db.update("puntaje", modificacion, "score=" + bestScore, null);
            }else{
                ContentValues insertar = new ContentValues();
                insertar.put("nombre", nombre_jugador);
                insertar.put("score", score);
                db.insert("puntaje",null,insertar);
            }
            db.close();
        }
    }
}