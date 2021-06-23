package com.myconnectech.simpleNFC;

import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
    private static final int BLACK = 0xFF000000;
    // https://stackoverflow.com/questions/13247701/how-to-add-a-logo-to-qr-code-in-android
    // https://skrymerdev.wordpress.com/2012/09/22/qr-code-generation-with-zxing/

    public void addQR(String content) {
        BitMatrix matrix = null;
        QRCodeWriter qrWriter; // = new QRCodeWriter();
        qrWriter = new  QRCodeWriter();

        try {
            matrix = qrWriter.encode(content, // "https://myConnecTech.fr",
                    BarcodeFormat.QR_CODE,
                    300,
                    300 ,
                    null);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[offset + x] = BLACK;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            ImageView iv = findViewById(R.id.imageView);
            iv.setImageBitmap(bitmap);
            //return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //image = MatrixToImageWriter.toBufferedImage(matrix);
        //Bitmap image = toBitmap(matrix);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, null);
        Log.d("RUN", "onResume-1");
        addQR("https://myConnecTech.fr");
        Log.d("RUN", "onResume-2");
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

        addQR(bytesToHex(tag.getId()));
    }
}