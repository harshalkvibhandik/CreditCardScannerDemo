package com.hsbc.creditcardscannerdemo;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOConstants;
import io.card.payment.CardIOFragment;
import io.card.payment.CreditCard;
import io.card.payment.interfaces.CardScanListener;

public class MainActivity extends AppCompatActivity implements CardScanListener {
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

    /*private void setupCreditCardScanner() {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        //arguments setup for Card.io
        Bundle args = new Bundle();
        args.putBoolean(CardIOConstants.PORTRAIT_ORIENTATION_LOCK, true);
        args.putInt(CardIOConstants.CARD_IO_VIEW, container.getId());
        args.putInt(CardIOConstants.CARD_IO_OVERLAY_COLOUR, Color.GREEN);
        args.putString(CardIOConstants.EXTRA_SCAN_INSTRUCTIONS, "Hold your card here.\nIt will scan automatically.");
        args.putBoolean(CardIOConstants.CARD_EXPIRY, true); //not working

        //adding Card.io as fragment
        CardIOFragment cardFragment = new CardIOFragment();
        cardFragment.setArguments(args);

        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.add(container.getId(), cardFragment);
        fragTransaction.commit();
    }*/

    private void setupCreditCardScanner() {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        //arguments setup for Card.io
        Bundle bundle = new Bundle();
        bundle.putInt(CardIOConstants.CARD_IO_VIEW, container.getId());
        bundle.putBoolean(CardIOConstants.PORTRAIT_ORIENTATION_LOCK, true);
        bundle.putBoolean(CardIOConstants.CARD_EXPIRY, false);
        bundle.putInt(CardIOConstants.CARD_IO_OVERLAY_COLOUR, Color.GREEN);
        bundle.putBoolean(CardIOConstants.EXTRA_HIDE_CARDIO_LOGO, true);
        bundle.putString(CardIOConstants.EXTRA_SCAN_INSTRUCTIONS, "Hold your card here.\nIt will scan automatically.");
        //bundle.putBoolean(CardIOConstants.EXTRA_USE_CARDIO_LOGO, false);

        CardIOFragment fragment = new CardIOFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(container.getId(), fragment, "CARDIO").commit();
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
    public void onCardScanSuccess(CreditCard cc, Bitmap bitmap) {
        textViewResult.setText("Detected Card Number\n\n"+cc.getFormattedCardNumber());
    }

    @Override
    public void onCardScanFail() {

    }

    @Override
    public void onPictureTaken(byte[] data) {

    }
}
