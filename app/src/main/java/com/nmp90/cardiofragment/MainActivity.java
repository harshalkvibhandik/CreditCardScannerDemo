package com.nmp90.cardiofragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOFragment;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity implements CardIOFragment.CardDetectionListener {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        checkForCameraPermission();
    }

    private void checkForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            setupCreditCardScanner();
        }
    }

    private void setupCreditCardScanner() {
        CardIOFragment fragment = new CardIOFragment();
        fragment.setCardDetectionListener(this);
        Bundle args = new Bundle();
        args.putBoolean(CardIOFragment.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        args.putBoolean(CardIOFragment.EXTRA_HIDE_CARDIO_LOGO, true);
        args.putBoolean(CardIOFragment.EXTRA_SUPPRESS_CONFIRMATION, true);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().add(R.id.container, fragment, "CARDIO").commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCreditCardScanner();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }//end onRequestPermissionsResult

    @Override
    public void onEdgeUpdate() {
        //textViewPlaceYourCard.setVisibility(View.GONE);
    }

    @Override
    public void onCardDetected(CreditCard detectedCard) {
        StringBuilder builder = new StringBuilder();
        builder.append("Card Number: ").append(detectedCard.cardNumber);
        textViewResult.setText(builder.toString());
    }
}
