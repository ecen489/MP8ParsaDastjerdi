package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PullActivity extends AppCompatActivity {
    DatabaseReference dbrf;
    FirebaseAuth mAuth;
    FirebaseUser user = null;
    Map<Integer, String> names;
    private ArrayAdapter<String> adapter;
    private ListView list;
    private ArrayList<String> array = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_activity);

        names = new HashMap<Integer, String>();
        names.put(123, "Bart");
        names.put(404, "Ralph");
        names.put(456, "Milhouse");
        names.put(888, "Lisa");

        dbrf = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        list.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();
        user = mAuth.getCurrentUser();
    }


    /*
        Query type 1: Upon entering a student ID, each course and grade of that student
        are displayed in the RecyclerView, eg., if you enter “123” all of Bart’s courses
        and grades should be shown.
    */
    public void query1(View view){

        final String studentID = ((EditText) findViewById(R.id.edittext)).getText().toString();

        if (user == null){
            Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Grade grade = snapshot.getValue(Grade.class);

                        if (grade.student_id == Integer.parseInt(studentID)){
                            array.add(grade.course_name + ": " + grade.grade);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Data snapshot doesn't exist", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // maybe send a log statement to the console
            }
        };

        (dbrf.child("simpsons/grades")).addValueEventListener(valueEventListener);
    }


    /*
        Query type 2. Upon entering a subject ID, all courses and grades of each student with ID greater than
        or equal to the entered ID are pulled along with their names and displayed in the RecyclerView, eg.
        if you enter “456,” the output should be “Milhouse, Math, B+”, “Lisa, Physics, A+” etc.
        Note that the name associated with an ID, and the grades of that ID are in two different children in the json file.
     */
    public void query2(View view){

        final String studentID = ((EditText) findViewById(R.id.edittext)).getText().toString();

        if (user == null){
            Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_SHORT).show();
            return;
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    array.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Grade grade = snapshot.getValue(Grade.class);

                        if (grade.student_id >= Integer.parseInt(studentID)){
                            array.add(names.get(grade.student_id) + ", " + grade.course_name + ", " + grade.grade);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Data snapshot doesn't exist", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // maybe send a log statement to the console
            }
        };

        (dbrf.child("simpsons/grades")).addValueEventListener(valueEventListener);
    }



    /*
        Go to push activity
     */
    public void push(View view){
        Intent intent = new Intent(this, PushActivity.class);
        startActivity(intent);
    }


    /*
        Sign out of current account
     */
    public void signOut(View view){

        // sign out of account in this function
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();

        // Return back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

