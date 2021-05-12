package com.example.uniro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class UniversityListActivity extends AppCompatActivity {

    private Button resendCode;
    private TextView verifyMsg;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    RecyclerView recyclerView;
    DatabaseReference database;
    UniversityAdapter universityAdapter;
    ArrayList<University> list;
    SearchView searchView;

    private TextView userNameView;
    private TextView userEmailView;
    private ConstraintLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(200);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        ///setting the recycler view

        userNameView = findViewById(R.id.userNameView);
        userEmailView = findViewById(R.id.userEmailView);
        drawer = findViewById(R.id.drawer);

        recyclerView = findViewById(R.id.universityList);
        searchView = findViewById(R.id.searchView);
        database = FirebaseDatabase.getInstance().getReference("university");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        universityAdapter = new UniversityAdapter(this, list);
        recyclerView.setAdapter(universityAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    University university = dataSnapshot.getValue(University.class);
                    list.add(university);
                }
                universityAdapter.notifyDataSetChanged();
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





        resendCode = findViewById(R.id.verifyNowButton);
        verifyMsg = findViewById(R.id.emailNotVerified);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(UniversityListActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error!=null){
                        Log.d("tag","Error:"+error.getMessage());
                }else {
                        Toast.makeText(getApplicationContext(), "Hello, " + value.getString("name"),Toast.LENGTH_SHORT).show();
                }

                if(value != null)
                {
                    userNameView.setText(Objects.requireNonNull(value.get("name")).toString());
                    userEmailView.setText(Objects.requireNonNull(value.get("email")).toString());
                }


                
            }
        });

        final FirebaseUser user = fAuth.getCurrentUser();
            if(!user.isEmailVerified()){
                resendCode.setVisibility(View.VISIBLE);
                verifyMsg.setVisibility(View.VISIBLE);

                resendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                    }
                });
            }

    }

    public void logout(View view) {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        startActivity(new Intent(UniversityListActivity.this, MainActivity.class));
        finish();
    }

    private void search(String str){
        ArrayList<University> myList = new ArrayList<>();
        for(University object : list){
            if(object.getName().toLowerCase().contains(str.toLowerCase())){
               myList.add(object);
            }
        }
        UniversityAdapter myAdapter = new UniversityAdapter(this,myList);
        recyclerView.setAdapter(myAdapter);
    }


    public void openDrawer(View view) {

        if(drawer.getVisibility() == View.VISIBLE){

            drawer.setVisibility(View.GONE);

        }else {

            drawer.setVisibility(View.VISIBLE);

        }

    }

    public void openInfo(View view) {
        startActivity(new Intent(UniversityListActivity.this, AppInfoActivity.class));
    }
}