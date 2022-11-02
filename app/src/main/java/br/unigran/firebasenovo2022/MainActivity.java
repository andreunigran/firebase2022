package br.unigran.firebasenovo2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        ler();
        salvar();

    }
    public void salvar(){
        Pessoa p = new Pessoa();
        p.nome="Teste";
        p.cidade="dourados";
        DatabaseReference pessoas = databaseReference.child("pessoas");
        pessoas.child("006").setValue(p);

    }

List lista = new LinkedList();
    public void ler(){
        DatabaseReference pessoas = databaseReference.child("pessoas");
        DatabaseReference veiculos = databaseReference.child("veiculos");
     /*   pessoas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        pessoas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                Log.i("Firebase",snapshot.getValue().toString());
                for (DataSnapshot dados:snapshot.getChildren()
                     ) {
                    Pessoa pessoa = dados.getValue(Pessoa.class);
                    lista.add(pessoa);
                    Log.i("Firebase",pessoa.nome);
                  //  Log.i("Firebase",snapshot.child("nome").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase",error.toString());
            }
        });
    }
}