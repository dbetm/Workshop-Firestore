package com.example.david.app_alumnos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Atributos
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    public void setup() {
        db = FirebaseFirestore.getInstance();
    }

    public void agregarDatos(View v) {
        // Creamos un nuevo alumno con la estructura de datos Map
        Map<String, Object> alumno = new HashMap<>();
        alumno.put("nombre", "David");
        alumno.put("apellido_s", "Betancourt Montellano");
        alumno.put("nombre_usuario", "dbetm");
        alumno.put("nacimiento", 1998);

        // Referencia a la colección de alumnos
        CollectionReference alumnosCollectionRef = db.collection("alumnos");

        // Se agrega el documento con un ID generado automáticamente por Cloud Firestore.
        alumnosCollectionRef
                .add(alumno)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "Documento agregado con ID: " + documentReference.getId(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error al tratar de agregar documento: " + e,
                                Toast.LENGTH_LONG).show();
                    }
        });
    }
}
