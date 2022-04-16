package com.unity3d.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollectActivity extends AppCompatActivity {

    private TextView balanceTV;
    private Button exitBtn;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        balanceTV = (TextView) findViewById(R.id.balanceTV);
        exitBtn = findViewById(R.id.exitBtn);

        SharedPreferences sharedPref = this.getSharedPreferences("com.unity3d.player.v2.playerprefs", Context.MODE_PRIVATE);
        int balance = sharedPref.getInt("balance", 0);
        String mobile = sharedPref.getString("mobile", "");

        Bundle bundle = getIntent().getExtras();
        int amount = bundle.getInt("amount");

        reff = FirebaseDatabase.getInstance("https://ar-atm-otp-default-rtdb.firebaseio.com/").getReference().child(mobile);
        reff.child("balance").setValue(balance - amount);

        balanceTV.setText(String.valueOf(balance - amount));

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}