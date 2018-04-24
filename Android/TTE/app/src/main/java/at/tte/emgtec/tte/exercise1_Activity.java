package at.tte.emgtec.tte;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class exercise1_Activity extends Activity {
    TextView textdebug;
    int prog_r = 0;
    int prog_g = 0;
    int prog_b = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise1_);
        Button btnconnect = (Button) findViewById(R.id.connect);
        btnconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BLE","connecting");
                BLEcs.contxt = getApplicationContext();
                BLEcs.BTConnect();
            }
        });
        SeekBar seek_r = (SeekBar) findViewById(R.id.seek_r);
        seek_r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                prog_r = progress;
                Send_RGB();
            }

        });

        Button btnSend = (Button) findViewById(R.id.btnSend);
        final EditText bleSendText = (EditText) findViewById(R.id.editText2);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLEcs.BLEsendString("mode," + bleSendText.getText());
            }
        });

        SeekBar seek_b = (SeekBar) findViewById(R.id.seek_b);
        seek_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                prog_b = progress;
                Send_RGB();
            }

        });
        SeekBar seek_g = (SeekBar) findViewById(R.id.seek_g);
        seek_g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                prog_g = progress;
                Send_RGB();
             }

        });
    }

    private void Send_RGB(){
        int RGB = getIntFromColor((float)(prog_r/100.0),(float)(prog_g/100.0),(float)(prog_b/100.0));
        Log.d("RBG","val:"+prog_r+"-"+prog_g+"-"+prog_b+"rgb-"+RGB);
        BLEcs.BLEsendString("rgb," + RGB);
    }
    public int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0xFF0000;
        G = (G << 8) & 0x00FF00;
        B = B & 0x0000FF;

        return 0x000000 | R | G | B;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise1_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BLEcs.BTDisconnect();
        Variables.checkconnection = true;
    }
    @Override
    protected  void onPause(){
        super.onPause();
        BLEcs.BTDisconnect();
        Variables.checkconnection = true;
    }
    @Override
     protected  void onStop(){
        super.onStop();
        BLEcs.BTDisconnect();
        Variables.checkconnection = true;
    }
}
