package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private Button buttonGetOTP;
    private EditText inputMobile;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpactivity3);

        inputMobile= findViewById(R.id.inputMobile);
        buttonGetOTP = findViewById(R.id.buttonGetOTP);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOTP.setOnClickListener(v -> {
            if (inputMobile != null) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SendOTPActivity.this, "Enter Registered Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.INVISIBLE);

                reff = FirebaseDatabase.getInstance("https://ar-atm-otp-default-rtdb.firebaseio.com/").getReference().child(inputMobile.getText().toString());
                reff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long maxid = snapshot.getChildrenCount();
                        if (maxid > 0) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + inputMobile.getText().toString(),
                                0,
                                TimeUnit.SECONDS,
                                SendOTPActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NotNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                        Toast.makeText(SendOTPActivity.this,e.getMessage() ,Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NotNull String verificationId, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(SendOTPActivity.this, VerifyOTPActivity.class);
                                        intent.putExtra("mobile", inputMobile.getText().toString());
                                        intent.putExtra("verificationId", verificationId);
                                        startActivity(intent);

                                    }
                                }
                            );
                        }
                        else {
                            Toast.makeText(SendOTPActivity.this, "Mobile number is not registered...", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}