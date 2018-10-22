package com.example.david.app_alumnos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatosAlumno extends AppCompatActivity {
    // Atributos
    FirebaseFirestore db;

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

    private void setup() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void lanzarAgente(View v) {
        // Referencia al botón
        Button btnLanzarAgente = (Button) findViewById(R.id.btnLanzarAgente);
        // Se deshabilita el botón
        btnLanzarAgente.setEnabled(false);

        // Para inicializar "bd" en esta actividad.
        setup();

        // Se obtiene el objeto referencia de la colección de interés
        CollectionReference collRef = db.collection("alumnos");

        // Se manda llamar el método que agrega el agente de escucha en tiempo real
        collRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) { // Si error es diferente de nulo, el agente da error
                    Toast.makeText(getApplicationContext(), "Error: " + e,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // Se recorren las instancias que presentan cambios
                for(DocumentChange dc : snapshots.getDocumentChanges()) {
                    TextView txtAccion = findViewById(R.id.txtAccion);
                    switch (dc.getType()) {
                        case ADDED:     // un alumno se agregó
                            txtAccion.setText(String.valueOf(
                                dc.getDocument().getData().get("nombre") +
                                    " se ha agregado."
                            ));
                            Log.d("Agrega", "New city: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:  // un alumno se modificó en alguno de sus campos
                            txtAccion.setText(String.valueOf(
                                dc.getDocument().getData() +
                                    " se ha modificado."));
                            Log.d("Modifica", "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:   // un alumno se eliminó
                            txtAccion.setText(String.valueOf(
                                dc.getDocument().getData() +
                                    " se ha eliminado."));
                            Log.d("Elimina", "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });

    }
}
