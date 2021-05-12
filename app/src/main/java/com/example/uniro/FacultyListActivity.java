package com.example.uniro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FacultyListActivity extends AppCompatActivity {

     DatabaseReference database;
     RecyclerView recyclerView;
     FacultyAdapter facultyAdapter;
     ArrayList<Faculty> list;
     SearchView searchView;
     Button loadMapActivity;
     University university;
     ImageView universityImage;
     TextView universityName;
    TextView universityEmail;
    TextView universitySite;
    TextView universityPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(200);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        University university = UniversityAdapter.univ;
        database = FirebaseDatabase.getInstance().getReference(university.getName().toString());

        recyclerView = findViewById(R.id.facultyList);
        searchView = findViewById(R.id.searchView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMapActivity = findViewById(R.id.loadMap);
        universityImage = findViewById(R.id.universityImage);
        universityName = findViewById(R.id.universityName);
        universityEmail = findViewById(R.id.universityEmail);
        universitySite = findViewById(R.id.universitySite);
        universityPhone = findViewById(R.id.universityPhone);
        university = UniversityAdapter.univ;

        if (university.getMainimg() != null) {
            String url = university.getMainimg();
            Glide.with(this)
                    .load(university.getMainimg().toString())
                    .into(universityImage);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(this).clear(universityImage);
            // remove the placeholder (optional); read comments below
            universityImage.setImageDrawable(null);
        }

        universityName.setText(university.getName());
        universityEmail.setText("Email: " + university.getEmail());
        universitySite.setText("Site: " + university.getSite());
        universityPhone.setText("Phone: " + university.getPhone());

        loadMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacultyListActivity.this, MapActivity.class));
            }
        });

        list = new ArrayList<>();
        facultyAdapter = new FacultyAdapter(this, list);
        recyclerView.setAdapter(facultyAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Faculty faculty = dataSnapshot.getValue(Faculty.class);
                    list.add(faculty);
                }
                facultyAdapter.notifyDataSetChanged();
                if(searchView != null){
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            search(newText);
                            return false;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    private void search(String str){
        ArrayList<Faculty> myList = new ArrayList<>();
        for(Faculty object : list){
            if(object.getName().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        FacultyAdapter myAdapter = new FacultyAdapter(this,myList);
        recyclerView.setAdapter(myAdapter);
    }
}