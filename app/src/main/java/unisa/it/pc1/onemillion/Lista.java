package unisa.it.pc1.onemillion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class Lista extends AppCompatActivity {
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lista = findViewById(R.id.comunicazioniSalvate);

        Intent i = getIntent();

        String contenuto = i.getStringExtra("messaggio");
        Date data = (Date)i.getSerializableExtra("Data");

        Task task = new Task(contenuto,data);

        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.list_element, new ArrayList<Task>());

        lista.setAdapter(customAdapter);


        customAdapter.add(task);


    }
}
