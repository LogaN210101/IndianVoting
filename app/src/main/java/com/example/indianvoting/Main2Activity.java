package com.example.indianvoting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private Button vte;
    private RadioGroup rg;
    private RadioButton rb;
    private DatabaseReference db;
    private List<String> party;
    private List<Integer> counter;
    private int c=100;
    private String voter="";

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        vte=findViewById(R.id.vote);
        rg=findViewById(R.id.ballot);

        db= FirebaseDatabase.getInstance().getReference();
        party=new ArrayList<>();
        counter=new ArrayList<>();
        voter=getIntent().getStringExtra("Data");
        prepareBallot();
        vte.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v == vte)
        {
            int checkedId = rg.getCheckedRadioButtonId();
            if(checkedId==-1)
            {
                Toast.makeText(getApplicationContext(),"Please select a candidate.",Toast.LENGTH_SHORT).show();
                return;
            }
            rb=findViewById(checkedId);
            int x = counter.get(checkedId - 100) + 1;
            ResetData rd = new ResetData(x);
            VoterReset vr =new VoterReset(voter.substring(voter.indexOf('$')+1, voter.lastIndexOf('$')), 1);
            db.child("EVM").child(party.get(checkedId-100)).setValue(rd);
            db.child("voterList").child(voter.substring(0,voter.indexOf('$'))).setValue(vr);
            Toast.makeText(getApplicationContext(),"Successfully Voted",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /*private void castVote() {
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    rb=findViewById(checkedId);
                    x=counter.get(checkedId-100)+1;
                    Toast.makeText(getApplicationContext(),"XYZ",Toast.LENGTH_SHORT).show();
                }
            });

    }*/

    private void prepareBallot() {
        db.child("EVM").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        party.add(ds.getKey());
                        ResetData rd = ds.getValue(ResetData.class);
                        assert rd != null;
                        counter.add(rd.count);
                    }
                    for(String b: party)
                    {
                        rb=new RadioButton(getApplicationContext());
                        rb.setText(b);
                        rb.setId(c);
                        rg.addView(rb);
                        c++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
