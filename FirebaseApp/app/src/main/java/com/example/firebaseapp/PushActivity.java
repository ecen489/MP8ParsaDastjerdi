package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class PushActivity extends AppCompatActivity {

    private DatabaseReference dbrf;
    private FirebaseAuth mAuth;
    private FirebaseUser user = null;

    private int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_activity);

        dbrf = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }

    public void radioClick(View view){

        switch (view.getId()){
            case (R.id.bart):
                studentID = 123;
                break;

            case (R.id.ralph):
                studentID = 404;
                break;

            case (R.id.milhouse):
                studentID = 456;
                break;

            case (R.id.lisa):
                studentID = 888;
                break;

            default:
                studentID = 123;
        }
    }


    public void pushGrade(View view){
        if (user == null){
            Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }

        String courseId = ((EditText) findViewById(R.id.course_id)).getText().toString();
        String courseName = ((EditText) findViewById(R.id.course_name)).getText().toString();
        String grade = ((EditText) findViewById(R.id.grade)).getText().toString();

        Random rand = new Random();
        String grade_id = String.valueOf(rand.nextInt(10000));

        DatabaseReference ref = dbrf.child("simpsons/grades/" + grade_id);
        ref.child("course_id").setValue(Integer.parseInt(courseId));
        ref.child("course_name").setValue(courseName);
        ref.child("grade").setValue(grade);
        ref.child("student_id").setValue(studentID);


        Toast.makeText(getApplicationContext(), "Grade: " + grade + " pushed to database", Toast.LENGTH_SHORT).show();

        // Go back to pull activity
        Intent intent = new Intent(this, PullActivity.class);
        startActivity(intent);
    }
}
