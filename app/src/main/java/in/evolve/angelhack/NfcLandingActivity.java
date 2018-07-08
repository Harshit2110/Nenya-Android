package in.evolve.angelhack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class NfcLandingActivity extends AppCompatActivity {

    private Socket socket;
    private String TAG = "NfcLandingActivity";
    private int RC_NFC = 574;
    private SharedPrefUtil sharedPrefUtil;  // no need of shared pref....

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_landing);
        Toast.makeText(this, "Testy", Toast.LENGTH_SHORT).show();
        sharedPrefUtil = new SharedPrefUtil(NfcLandingActivity.this);
        initialiseHospitalSocket();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"Connected to Socket Server");
                socket.emit("newp","started");
            }
        });


        socket.on("your_call", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"here is a call");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        yourTurnAlert();
                    }
                });

            }
        });

        socket.on("new_order", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"io works");
            }
        });

        socket.on("finally", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];

                try {
                    final String to_print = jsonObject.getString("pres");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(NfcLandingActivity.this)
                                    .setTitle("Doctor Presciption...")
                                    .setMessage(to_print)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NfcLandingActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NfcLandingActivity.this.finish();
                                        }
                                    })
                                    .create().show();
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"It May be a tag;");

        if (socket==null){
            initialiseHospitalSocket();
        }

        if (socket!=null && !socket.connected()){
            socket.connect();
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())  && Constants.MODE==0){
            Constants.MODE =1;
            processNfcTag(getIntent());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_NFC){
            showNfcStatus();
        }
    }

    void processNfcTag(Intent intent){
        Log.d("TTTTTTT","ttttttttttt");
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(tag == null){
            Log.d("AAAAAAAAAAAAAAAAAAAAAAA","NULL");
        }else {
            byte[] arr = tag.getId();
            for(byte b: arr){
                Log.d("AAA",String.valueOf(b));
            }
        }

        for (String techUsed : tag.getTechList()){
            Log.d(TAG,""+techUsed);
        }
/*
        NfcTagUtils nfcTagUtils = new NfcTagUtils();
        tag = nfcTagUtils.patchTag(tag);*/
        String data = NfcTagUtils.readTag(tag);

        Log.d(TAG,data+"and the daata is");

        if(socket==null) {
            initialiseHospitalSocket();
            Log.d("SOOOOOOOOOOOCKET","Socket null hai");
        }

        if (socket!=null && !socket.connected()){
            socket.connect();
            Log.d("SOOOOOOOOOOOCKET","Socket null nhi hai");
        }


    }

    void initialiseHospitalSocket(){
        try{
            Log.d("FINAL DESTINATION","A raha h");
            socket = IO.socket(Constants.SOCKETS_SERVER);
            Log.d("FINAL DESTINATION AAA","A raha h");
            socket.connect();
            Log.d(TAG,"Connecting to Menu Socket Server");
        }catch (URISyntaxException e){
            e.printStackTrace();
        }

        if (!socket.connected()){
            socket.connect();
        }

    }

    void showNfcStatus(){

        Boolean nfcState = UtilFunction.checkNFCStatus(NfcLandingActivity.this);

        if (nfcState == null){
            // nfcInfo.setText("NFC NOT AVAILABLE");
            Intent intent=new Intent(NfcLandingActivity.this,MainActivity.class);
            startActivity(intent);
        }

        else if (nfcState){
            //nfcInfo.setText("NFC AVAILABLE ... USE ANY ACTIVE OR PASSIVE DEVICE TO USE WITH IT...");
        }
        else {
            showSwitchOnNfcDialog();
        }
    }

    void showSwitchOnNfcDialog(){

        new AlertDialog.Builder(NfcLandingActivity.this)
                .setMessage("NFC Is Not Enabled. In Order To Enjoy Our Services Enable Your Wifi..")
                .setCancelable(false)
                .setPositiveButton("ENABLE NFC", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            startActivityForResult(intent,RC_NFC);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivityForResult(intent,RC_NFC);
                        }
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NfcLandingActivity.this.finish();
                    }
                })
                .create().show();
    }

    void yourTurnAlert(){

        new AlertDialog.Builder(NfcLandingActivity.this)
                .setMessage("Doctor is Calling You.....")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*NfcLandingActivity.this.finish();*/
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*NfcLandingActivity.this.finish();*/
                        /*Intent intent = new Intent(NfcLandingActivity.this,NfcLandingActivity.class);*/

                    }
                })
                .create().show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"here we destroyed");
        socket.close();
    }
}
