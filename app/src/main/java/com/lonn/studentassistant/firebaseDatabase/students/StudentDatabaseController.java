package com.lonn.studentassistant.firebaseDatabase.students;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.interfaces.IDatabaseController;
import com.lonn.studentassistant.common.interfaces.IRepository;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;

import java.util.ArrayList;
import java.util.List;

public class StudentDatabaseController implements IDatabaseController<Student>
{
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public StudentDatabaseController()
    {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("students");
    }

    public void setAll(final List<Student> list)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Student student = dataSnapshot.getValue(Student.class);

                    if (student != null)
                    {
                        student.numarMatricol = dataSnapshot.getKey();
                        list.add(student);
                    }

                    Log.e("Got students",Integer.toString(list.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void add(Student student)
    {
        databaseReference.child(student.numarMatricol).setValue(student);
    }

    public void remove(Student student)
    {
        databaseReference.child(student.numarMatricol).removeValue();
    }

    public void update(Student student)
    {
        databaseReference.child(student.numarMatricol).setValue(student);
    }
}
