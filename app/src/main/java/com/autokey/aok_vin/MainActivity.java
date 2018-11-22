package com.autokey.aok_vin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.DialogInterface;

import static com.autokey.aok_vin.R.id.cmdInsert;
import static com.autokey.aok_vin.R.id.cmdScanOperator;
import static com.autokey.aok_vin.R.id.lblWifiConnName;
import static com.autokey.aok_vin.WebService.invokeInsertTelaio4Android;
import static java.math.RoundingMode.UP;

public class MainActivity extends AppCompatActivity
{
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private TextView txtOperator;
    private TextView txtVinBarcode;
    private TextView txtModel;
    private TextView txtVIN;
    private Button cmdInsert;
    private Button cmdScanOperator;
    private Button cmdScanVIN;
    public String astr = "";
    private TextView lblWiFiConnName;
    private Boolean isConnectionValid = false;
    private Boolean IsDebuggingMode = false;

    String displayText = "";

    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOperator = (TextView) findViewById(R.id.txtOperator);
        txtVinBarcode = (TextView) findViewById(R.id.txtVinBarcode);
        txtModel = (TextView) findViewById(R.id.txtModel);
        txtVIN = (TextView) findViewById(R.id.txtVIN);
        txtResult = (TextView) findViewById(R.id.txtResult);
        //txtVIN.setEnabled(false);
        //txtModel.setEnabled(false);
        lblWiFiConnName = (TextView) findViewById(lblWifiConnName);
        cmdScanOperator = (Button)findViewById(R.id.cmdScanOperator);
        cmdScanVIN = (Button)findViewById(R.id.cmdScanVIN);
        // Rendo cliccabile il lblWiFiConnName per andare a gestore WIFI
        if(!IsDebuggingMode) {
            lblWiFiConnName.setOnClickListener (new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    isConnectionValid = VerifyWIFI ();
                    if (isConnectionValid) {

                        cmdInsert = (Button) findViewById (R.id.cmdInsert);
                        cmdInsert.setOnClickListener (new View.OnClickListener ()
                        {
                            //@Override
                            public void onClick(View view)
                            {
                                txtResult.getText ().toString ();
                                AsyncCallWS task = new AsyncCallWS ();
                                task.execute ();
                            }
                        });
                    }
                }
            });
        }
        else
        {
            lblWiFiConnName.setOnClickListener (new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    isConnectionValid = true;
                    if (isConnectionValid) {

                        cmdInsert = (Button) findViewById (R.id.cmdInsert);
                        cmdInsert.setOnClickListener (new View.OnClickListener ()
                        {
                            //@Override
                            public void onClick(View view)
                            {
                                txtResult.getText ().toString ();
                                AsyncCallWS task = new AsyncCallWS ();
                                task.execute ();
                            }
                        });
                    }
                }
            });
        }
        //lblWiFiConnName.performClick();

        txtResult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                txtVinBarcode.setText("");
                txtVIN.setText("");
                txtModel.setText("");
                txtResult.setText("");
            }
        });

        isConnectionValid = VerifyWIFI();
        isConnectionValid = true;

        if (isConnectionValid) {

            cmdInsert = (Button) findViewById(R.id.cmdInsert);
            cmdInsert.setOnClickListener(new View.OnClickListener()
            {
                //@Override
                public void onClick(View view)
                {
                    txtResult.getText().toString();
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            });
        }
    }


    public boolean VerifyWIFI()
    {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(MainActivity.CONNECTIVITY_SERVICE);

//        cmdScanOperator.setEnabled(false);
//        cmdScanVIN.setEnabled(false);
//        cmdInsert.setEnabled(false);

        Boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (isWifi) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d("wifiInfo", wifiInfo.toString());
            Log.d("SSID", wifiInfo.getSSID());
            if (wifiInfo.getSSID().toString().toUpperCase().contains("AUTOK")) {
                lblWiFiConnName.setText(wifiInfo.getSSID().toString() + "\r\nConnection is OK !");
                lblWiFiConnName.setTextColor(Color.BLUE);
                isConnectionValid = true;
                return true;
//                cmdScanOperator.setEnabled(true);
//                cmdScanVIN.setEnabled(true);
//                cmdInsert.setEnabled(true);
            } else {
                lblWiFiConnName.setText("Not Autokey WIFI\nClick to Recheck/Change !");
                lblWiFiConnName.setTextColor(Color.RED);
//                cmdScanOperator.setEnabled(false);
//                cmdScanVIN.setEnabled(false);
//                cmdInsert.setEnabled(false);
                OpenWifiCenter();
                isConnectionValid = true;
                //VerifyWIFI();
            }

        } else {
            //textView.setText("nothing");
            // Activity transfer to wifi settings

            lblWiFiConnName.setText("WIFI is OFF LINE !\r\nClick to Recheck/Change !");
            lblWiFiConnName.setTextColor(Color.RED);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Open WIFI settings...");
            builder.setMessage("Press OK to go to WIFI settings !");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {

                public void onClick(DialogInterface dialog, int which)
                {
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),0);
                    dialog.dismiss();
                    //lblWiFiConnName.performClick();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
            {


                public void onClick(DialogInterface dialog, int which)
                {
                    // I do not need any action here you might
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            isConnectionValid = false;

        }
        return isConnectionValid;
    }

    public void OpenWifiCenter()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Open WIFI settings...");
        builder.setMessage("Press OK to go to WIFI settings !");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int which)
            {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),0);
                dialog.dismiss();
                //lblWiFiConnName.performClick();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {


            public void onClick(DialogInterface dialog, int which)
            {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void scanOperator(View v)
    {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SCAN_FORMATS", "CODE_39");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException exc) {
            showDialog(MainActivity.this, "No scanner found", "Download a scanner now ? ", "Yes", "No").show();
        }
    }

    public void scanVIN(View v)
    {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SCAN_FORMATS", "CODE_39");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException exc) {
            showDialog(MainActivity.this, "No scanner found", "Download a scanner now ? ", "Yes", "No").show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                //Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                //toast.show();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Il barcode è : " + contents + "\nIl formato è : " + format);
                dlgAlert.setTitle("Risultato lettura");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                // Togliere commento per il msgbox
                //dlgAlert.create().show();
                txtOperator.setText(contents);

            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                //Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                //toast.show();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Il barcode è : " + contents + "\nIl formato è : " + format);
                dlgAlert.setTitle("Risultato lettura");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                // Togliere commento per il msgbox
                //dlgAlert.create().show();
                txtVinBarcode.setText(contents);
                String aModel = contents.substring(0, 3);
                String aVIN = contents.substring(3, 10);
                txtModel.setText(aModel);
                txtVIN.setText(aVIN);
            }
        }

    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo)
    {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        return downloadDialog.show();
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            displayText = WebService.invokeCercaTelaio4Android(txtVIN.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            txtResult.setText(displayText);
            String aText = txtResult.getText().toString();
            if (aText.equals("true")) {
                txtResult.setText("VIN already inserted");
                txtResult.setTextColor(Color.RED);
            }
            else
            {

                AsyncCallWSInsert task = new AsyncCallWSInsert();
                task.execute();
            }
        }
    }

    private class AsyncCallWSInsert extends AsyncTask<String, Void, Void>
    {


        protected Void doInBackground(String... params)
        {

            displayText = WebService.invokeInsertTelaio4Android(txtVIN.getText().toString(),txtModel.getText().toString(),txtOperator.getText().toString().toUpperCase());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            txtResult.setText(displayText);
            String aText = txtResult.getText().toString().toUpperCase();
            if (!aText.equals("ERROR OCCURED")) {
                txtResult.setText("VIN inserted ! \r\nClick here to insert a new VIN  !");
                txtResult.setTextColor(Color.BLUE);
            } else {
                txtResult.setText("VIN NOT inserted ! \n\rClick here to try insert a new VIN  !");
                txtResult.setTextColor(Color.RED);
            }
        }
    }
}