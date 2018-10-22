package com.example.david.app_alumnos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void recuperarDocumento(View v) {
        // Declaramos e inicializamos la referencia del documento
        DocumentReference docRef = db.collection("alumnos").document("Tuj7ozP3n6MmVHRPE4DG");
        // Se hace una llamada al método get() del objeto referencia del documento.
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    // Se recupera el documento con los datos, en este caso es el resultado de task
                    DocumentSnapshot alumno = task.getResult();
                    if(alumno.exists()) { // Pregunta existencial (literal)
                        Intent intent = new Intent(getApplicationContext(), DatosAlumno.class);
                        // alumno.getData(); Retorna un Map<String, Object>
                        /*
                         * Al intent le agregamos un parámetro llamado datosAlumno
                         * con el string de los datos del alumno
                         */
                        intent.putExtra("datosAlumno", alumno.getData().toString());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "¡Alumno no encontrado!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "get failed with " + task.getException(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
