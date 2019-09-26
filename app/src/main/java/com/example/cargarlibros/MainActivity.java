package com.example.cargarlibros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listadatos;

    RecyclerView ejem;
    EditText buscar;
    Button btn_buscar;
    JSONArray libros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ejem = findViewById(R.id.ejemplo);
        ejem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listadatos = new ArrayList<String>();


        buscar = findViewById(R.id.edt_libro);
        btn_buscar = findViewById(R.id.btn_buscar);


        ConnectivityManager c = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//ve si tenemos  coneccion a internet

        NetworkInfo info = c.getActiveNetworkInfo();//getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(info == null) {
            Toast.makeText(this, "Active su Wifi", Toast.LENGTH_LONG).show();
        }else if(!info.isConnected()) {
            Toast.makeText(this, "Error, no tiene internet", Toast.LENGTH_LONG).show();
        }

        final String BASE_URL ="https://www.googleapis.com/books/v1/volumes?";//concatenanto

        final String QUERY_PARAM = "q";

        final String MAX_RESULTS = "maxResults";

        final String PRINT_TYPE = "printType";

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, buscar.getText().toString())
                        .appendQueryParameter(MAX_RESULTS, "10")
                        .appendQueryParameter(PRINT_TYPE, "books")
                        .build();

                try {
                    URL requestURL = new URL(builtURI.toString());
                    TaskCargarLibros task = new TaskCargarLibros(MainActivity.this);//porque el constructor requeria el context
                    task.execute(requestURL);//ejecutar
                } catch (Exception e) {
                    Log.e("ErrorInternet", e.toString());
                    e.printStackTrace();
                }
            }
        });

    }
    public void  llenarlibros (JSONArray l){
        libros = l;

        listadatos.clear();
        try {


            for (int i = 0; i< libros.length();i++) {
                listadatos.add(libros.getJSONObject(i).getJSONObject("volumeInfo").getString("title"));
                adapter adaptador = new adapter(listadatos);
                ejem.setAdapter(adaptador);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}