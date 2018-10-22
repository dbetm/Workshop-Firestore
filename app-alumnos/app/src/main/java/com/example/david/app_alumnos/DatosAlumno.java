package com.example.david.app_alumnos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatosAlumno extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_alumno);

        // 1) Referencia a los TextView's
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtApellido_s = findViewById(R.id.txtApellido);
        TextView txtUsername = findViewById(R.id.txtUsername);
        TextView txtNacimiento = findViewById(R.id.txtNacimiento);
        // 2) Recuperar el parámetro
        String datosAlumno = getIntent().getExtras().getString("datosAlumno");
        datosAlumno = datosAlumno.replace("{", "");
        datosAlumno = datosAlumno.replace("}", "");
        // Quitar los espacios en blanco
        datosAlumno = datosAlumno.replace(" ", "");

        HashMap<String, Object> datos = new HashMap<>();
        String []pares = datosAlumno.split(",");

        // 3) Los agregamos a la estructura de datos
        for (int i = 0 ;i < pares.length;i++) {
            String pair = pares[i];
            String[] keyValue = pair.split("=");
            datos.put(keyValue[0], keyValue[1]);
        }

        // 4) Se setean a los TextView correspondientes
        txtNombre.setText(String.valueOf(datos.get("nombre")));
        txtApellido_s.setText(String.valueOf(datos.get("apellido_s")));
        txtUsername.setText(String.valueOf(datos.get("nombre_usuario")));
        txtNacimiento.setText(String.valueOf(datos.get("nacimiento")));
    }
}
