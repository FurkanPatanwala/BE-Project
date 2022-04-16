package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unity3d.player.UnityPlayerActivity;


public class MainActivity extends Activity {

    private Button button;
    private Button button_exit;
    private TextView nameTV, contactTV, balanceTV;

    DatabaseReference reff;

    private String mobile;
    private String name, accountType;
    private int balance, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.camera_button);
        button_exit = findViewById(R.id.exit_button);
        nameTV = (TextView) findViewById(R.id.nameTV);
        contactTV = (TextView) findViewById(R.id.contactTV);
        balanceTV = (TextView) findViewById(R.id.balanceTV);

        mobile = getIntent().getStringExtra("mobile");

        reff = FirebaseDatabase.getInstance("https://ar-atm-otp-default-rtdb.firebaseio.com/").getReference().child(mobile);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue().toString();
                accountType = snapshot.child("accountType").getValue().toString();
                balance = Integer.parseInt(snapshot.child("balance").getValue().toString());
                password = Integer.parseInt(snapshot.child("pin").getValue().toString());

                nameTV.setText(name);
                contactTV.setText(mobile);
                balanceTV.setText(String.valueOf(balance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(v -> {
            SharedPreferences sharedPref = this.getSharedPreferences("com.unity3d.player.v2.playerprefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("balance", balance);
            editor.putString("mobile", mobile);
            editor.putString("name", name);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
            intent.putExtra("password", password);
            intent.putExtra("accountType", accountType);
            intent.putExtra("balance", balance);
            startActivity(intent);
        });

        button_exit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        });

    }
}
