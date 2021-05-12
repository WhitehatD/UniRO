package com.example.uniro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FacultyDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(200);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Faculty faculty = FacultyAdapter.fac;

        TextView facultyName = findViewById(R.id.facultyName);
        TextView facultyDesc= findViewById(R.id.facDepartments);
        ImageView facultyImage = findViewById(R.id.facultyImage);
        facultyDesc.setText(faculty.getDepartments());
        facultyName.setText(faculty.getName());

        if (faculty.getMainimg() != null) {
            String url = faculty.getMainimg();
            Glide.with(this)
                    .load(faculty.getMainimg().toString())
                    .into(facultyImage);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(this).clear(facultyImage);
            // remove the placeholder (optional); read comments below
            facultyImage.setImageDrawable(null);
        }

    }
}