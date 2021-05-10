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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class UniversityListActivity extends AppCompatActivity {

    private Button resendCode;
    private TextView verifyMsg;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private TextView name, email;
    private String userID;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(200);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);

        recyclerView = findViewById(R.id.recycle);

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
                        name.setText(value.getString("name"));
                        email.setText(value.getString("email"));
                }

            }
        });

        Query query = fStore.collection("uni");

        FirestoreRecyclerOptions<Universitate> options = new FirestoreRecyclerOptions.Builder<Universitate>()
                                .setQuery(query, Universitate.class)
                                .build();

        adapter = new FirestoreRecyclerAdapter<Universitate, UniversitateView>(options) {
            @NonNull
            @NotNull
            @Override
            public UniversitateView onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_uni, parent, false);
                return new UniversitateView(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull UniversitateView holder, int position, @NonNull @NotNull Universitate model) {
                holder.uni_name.setText(model.getName());
                holder.uni_desc.setText(model.getDesc());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void logout(View view) {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        startActivity(new Intent(UniversityListActivity.this, MainActivity.class));
        finish();
    }
}