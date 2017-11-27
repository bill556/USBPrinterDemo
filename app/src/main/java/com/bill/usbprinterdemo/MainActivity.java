package com.bill.usbprinterdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void printReceiptClicked(View view) {
        Intent i = new Intent(this, PrinterService.class);
        startService(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
