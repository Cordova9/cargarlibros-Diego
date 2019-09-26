package com.example.cargarlibros;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class TaskCargarLibros extends AsyncTask<URL,Integer, String> {

    MainActivity actividad;

    Context context;

    public TaskCargarLibros(Context context) {
        this.context = context;
        actividad = (MainActivity) context;
    }

    @Override
    protected String doInBackground(URL... urls) {//genera en el hilo secundario
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setReadTimeout(20000 /* milliseconds */);
            conn.setConnectTimeout(20000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            String text = new Scanner(is).useDelimiter("\\A").next();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) { // apesar de estar en otra clase se ejecuta en la rincipal
        super.onPostExecute(s);
        try {
            JSONObject objeto = new JSONObject(s);
            JSONArray libros = objeto.getJSONArray("items");

            actividad.llenarlibros(libros);

            JSONObject libro = libros.getJSONObject(0);
            JSONObject infolibro = libro.getJSONObject("volumeInfo");
            //Toast.makeText(context,infolibro.getString("title"),Toast.LENGTH_LONG).show();
            // Toast.makeText(context,libros.getJSONObject(0).getString("id"),Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}