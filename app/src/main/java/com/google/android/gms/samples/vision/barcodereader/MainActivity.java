/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.vision.barcodereader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private CompoundButton autoCapture;
    private TextView statusMessage;
    private TextView barcodeValue;
    private RadioGroup scanMode;
    private TextView assetID;
    public static String ScanModeValue;
    //public static final String ScanMode = "Barcode".


    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        scanMode = (RadioGroup) findViewById(R.id.scan_mode);
        assetID = (TextView)findViewById(R.id.asset_id);
        autoCapture = (CompoundButton) findViewById(R.id.use_auto);

        findViewById(R.id.read_barcode).setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
            //intent.putExtra(BarcodeCaptureActivity.ScanMode, scanMode.getCheckedRadioButtonId());
            intent.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    public String onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_login:
                if (checked)
                    ScanModeValue = "login";
                    //data.putExtra(ScanModeHeader, ScanMode);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.110.13.2/Login-form.php"));
                    startActivity(browserIntent);
                    return ScanModeValue;
            case R.id.radio_logout:
                if (checked)
                    ScanModeValue = "logout";
                    Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.110.13.2/Logout.php"));
                    startActivity(browserIntent2);
                    return ScanModeValue;
                    //data.putExtra(ScanModeHeader, ScanMode);
            case R.id.radio_checkin:
                if (checked)
                    ScanModeValue = "in";
                    //data.putExtra(ScanModeHeader, ScanMode);
                    return ScanModeValue;
            case R.id.radio_checkout:
                if (checked)
                    ScanModeValue = "out";
                    //data.putExtra(ScanModeHeader, ScanMode);
                    return ScanModeValue;
            case R.id.radio_report:
                if (checked)
                    ScanModeValue = "report";
                    //data.putExtra(ScanModeHeader, ScanMode);
                    return ScanModeValue;
            case R.id.radio_cart:
                if (checked)
                    ScanModeValue = "cart";
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.110.13.2/view-cart.php"));
                    startActivity(browserIntent3);
                    return ScanModeValue;
        }
        return ScanModeValue;
    }



    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    String AssetID = ((barcode).displayValue).replace("http://10.110.13.2/report.php?assetid=", "");

                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText((barcode).displayValue + ScanModeValue);
                    assetID.setText(AssetID);

                    if (ScanModeValue == "report")
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.110.13.2/report.php?assetid=" + AssetID));
                        startActivity(browserIntent);
                    } else if (ScanModeValue == "out" || ScanModeValue == "in") {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.110.13.2/cartoutin.php?assetid=" + AssetID + "&status=" + ScanModeValue));
                        startActivity(browserIntent);
                    }

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
