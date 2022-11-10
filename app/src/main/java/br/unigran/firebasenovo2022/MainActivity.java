package br.unigran.firebasenovo2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText nome;
    EditText nomePesquisa;
    Spinner cidade;
    List dados;
    ListView listView;
    ArrayAdapter listaAdapter;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        nome= findViewById(R.id.idNome);
        nomePesquisa= findViewById(R.id.idpesquisa);
        dados= new LinkedList();

        listView= findViewById(R.id.idlista);
        listaAdapter= new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dados);
        listView.setAdapter(listaAdapter);
        cidade();
        ler();
        iniciaDate();
    }

    private void cidade() {
        cidade= findViewById(R.id.idCidade);
        List cidades = new LinkedList();
        cidades.add("Dourados");
        cidades.add("Fatima do sul");
        DatabaseReference veiculos = databaseReference.child("veiculos");
        veiculos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:
                snapshot.getChildren()) {
                    cidades.add(data.child("nome").getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,cidades);
        cidade.setAdapter(adapter);
    }

    public void salvar(View view){
        Pessoa p = new Pessoa();
        p.nome=nome.getText().toString();
        p.cidade=cidade.getSelectedItem().toString();
        DatabaseReference pessoas = databaseReference.child("pessoas");
        pessoas.push().setValue(p);
        Toast.makeText(getApplicationContext(),"Salvo",Toast.LENGTH_SHORT).show();

    }
    public void pesquisar(View view){
        ler();
    }

    public void ler(){
        DatabaseReference pessoas = databaseReference.child("pessoas");
        DatabaseReference veiculos = databaseReference.child("veiculos");
        Query query = pessoas.orderByChild("nome").startAt(nomePesquisa.getText().toString())
                ;


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dados.clear();
                Log.i("Firebase",snapshot.getValue().toString());
                for (DataSnapshot item:snapshot.getChildren()
                     ) {
                    Pessoa pessoa = item.getValue(Pessoa.class);
                    dados.add(pessoa);
                    listaAdapter.notifyDataSetChanged();
                    Log.i("Firebase",pessoa.nome);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebase",error.toString());
                Toast.makeText(getApplicationContext(),"erro de conexão",Toast.LENGTH_SHORT).show();

            }
        });
    }
//exemplo data
    EditText dataEdit;
    DatePickerDialog datePickerDialog;
    public void iniciaDate() {
        dataEdit = findViewById(R.id.idData);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                dataEdit.setText(dia+"/"+(mes+1)+"/"+ano);
            }
        };
        Calendar c =Calendar.getInstance();
        int dia,mes,ano;
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH);
        ano=c.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this,DatePickerDialog.THEME_HOLO_DARK,dateSetListener,ano,mes,dia);
    }
    public void showDatePick(View view) {
        datePickerDialog.show();
          }
}