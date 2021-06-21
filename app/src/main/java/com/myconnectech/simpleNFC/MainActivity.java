package com.myconnectech.simpleNFC;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    // MENU:
    // https://developer.android.com/guide/topics/ui/menus
    // NFC:
    // https://developer.android.com/guide/topics/connectivity/nfc/nfc

    TextView tw, twID;
    NfcAdapter nfcAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tw = findViewById(R.id.textView);
        twID = findViewById(R.id.twID);
        tw.setText("Read NFC");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
    // Helper
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableReaderMode(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_settings) {
            Log.d("MENU", "Settings");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d("NFC", "onTagDiscovered");
        twID.setText(bytesToHex(tag.getId()));
        tw.setText("discover "+tag.toString());
    }
}