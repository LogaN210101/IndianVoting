package com.example.indianvoting;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button n, n2;
    EditText e1,e2;
    DatabaseReference db;
    List<String> people;
    int t=0;
    String s="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        n=findViewById(R.id.next);
        n2=findViewById(R.id.newsb);
        e1=findViewById(R.id.name);
        e2=findViewById(R.id.uid);
        n.setOnClickListener(this);
        n2.setOnClickListener(this);
        db = FirebaseDatabase.getInstance().getReference();
        people =new ArrayList<>();



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v==n)
        {
            String name=e1.getText().toString();
            String id=e2.getText().toString();
            if(name.length()==0 || id.length()==0)
            {
                Toast.makeText(getApplicationContext(),"field cannot be empty",Toast.LENGTH_SHORT).show();
                return;
            }
            validity(name, id);
        }
        else if(v==n2)
        {
            news();
        }

    }

    private void news() {
        Intent i = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validity( final String nm, final String id) {
        db.child("voterList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VoterReset vr = ds.getValue(VoterReset.class);
                    people.add(ds.getKey() + "$" + vr.name + "$" + vr.flag);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        for (String p : people) {
            int fl = Integer.parseInt(p.substring(p.lastIndexOf('$') + 1));
            if (id.equals(p.substring(0, p.indexOf('$'))) && nm.equals(p.substring(p.indexOf('$') + 1, p.lastIndexOf('$'))) && fl == 0) {
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                i.putExtra("Data",p);
                Toast.makeText(getApplicationContext(), "Vote Here", Toast.LENGTH_SHORT).show();
                startActivity(i);
                return;


            }
        }
        Toast.makeText(getApplicationContext(), "name does not match with id or you have already voted", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {
        finishAffinity();


    }
}
