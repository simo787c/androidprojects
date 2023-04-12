package com.example.firebase;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.firebase.Model.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //static instance

    private ArrayAdapter adapter;

    public FirebaseService() {
    }

    public FirebaseService(ArrayAdapter adapter) {
        this.adapter = adapter;
    }

    public void addNote(String text){
        //new document with new ids in firestore
        DocumentReference ref = db.collection("notes2").document();
        Map<String, String> map = new HashMap<>();
        map.put("text",text);
        ref.set(map);
    }

    public void editImageNote(String documentId, String fileName){
        //get document with id
        DocumentReference ref = db.collection("notes2").document(documentId);
        Map<String, Object> map = new HashMap<>();

        //only save new image
        map.put("fileName",fileName);
        ref.update(map);
    }

    //method with listener
    public void add2Note(String text){
        //new document with new ids in firestore
        DocumentReference ref = db.collection("notes2").document();
        Map<String, String> map = new HashMap<>();
        map.put("text",text);
        //replaced longer ref.set with lambda
        ref.set(map)
                .addOnSuccessListener(unused -> System.out.println("document saved, " + text))
                .addOnFailureListener(e -> System.out.println("document Not saved, " + text));

//        ref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                System.out.println("document saved, " + text);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("document Not saved, " + text);
//            }
//        });

    }

    private String col = "notes2";
    //public not good, is temp
    public List<Note> list = new ArrayList<>();
    /*public void startListener(ArrayAdapter<Note> notesAdapter) {
        db.collection(col).addSnapshotListener((snap,error) ->{
            if (error == null){
                list.clear();

                for (DocumentSnapshot s : snap.getDocuments()){
                    String noteText = s.getString("text");
                    Note note = new Note(noteText, s.getId());
                    list.add(note);
                }

                notesAdapter.clear();
                notesAdapter.addAll(list);
                notesAdapter.notifyDataSetChanged(); // will update the GUI
            }
        });
    }*/
    public void startListener() {
        db.collection(col).addSnapshotListener((snap,error) ->{
            if (error == null){
                list.clear();

                for (DocumentSnapshot s : snap.getDocuments()){
                    String noteText = s.getString("text");
                    Note note = new Note(noteText, s.getId());
                    list.add(note);
                }
                adapter.clear();
                adapter.addAll(list);
                /*notesAdapter.clear();
                notesAdapter.addAll(list);*/
                adapter.notifyDataSetChanged(); // will update the GUI

            }
        });
    }


}
