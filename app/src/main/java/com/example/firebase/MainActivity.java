package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.Model.Note;

public class MainActivity extends AppCompatActivity {
    private FirebaseService firebaseService;
    ArrayAdapter adapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebaseService.addNote("hi from android");
        //firebaseService.add2Note("hi from android, with feedback");

        //ArrayAdapter<Note> notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        firebaseService = new FirebaseService(adapter);

        //ListView notesListView = findViewById(R.id.notes_list_view);

        createList(adapter);
        //adapter.add(firebaseService.list);
        /*listView.setOnItemClickListener((listView,linearLayout,pos,id) ->{
            TextView tv = linearLayout.findViewById(android.R.id.text1); //cast row to TextView
            System.out.println("you pressed " + pos);
            System.out.println("you pressed " + tv.getText());
        });*/
        firebaseService.startListener();
    }

    private void createList(ArrayAdapter<Note> adapter) {
        listView = findViewById(R.id.notes_list_view);


        listView.setOnItemClickListener((listView,linearLayout,pos,id) ->{
            TextView tv = linearLayout.findViewById(android.R.id.text1); //cast row to TextView
            System.out.println("you pressed " + tv.getText());

            Intent intent = new Intent(this, PhotoRollViewer.class);
            intent.putExtra("text",tv.getText());
            intent.putExtra("id", adapter.getItem((int) id).getId());
            startActivity(intent);

        });

        listView.setAdapter(adapter);
    }

    public void photoRollPagePressed(View view){
        startActivity(new Intent(MainActivity.this,PhotoRollViewer.class));
    }
}