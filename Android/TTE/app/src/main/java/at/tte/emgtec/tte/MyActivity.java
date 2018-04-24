package at.tte.emgtec.tte;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MyActivity extends Activity {
    ScrollView mainbtn_lay;
    ScrollView mainlayr_scroll;
    public static Resources res;
    public static String pkgname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final ImageView mainbtn1 = (ImageView) findViewById(R.id.imageBtn1);
        final ImageView mainbtn2 = (ImageView) findViewById(R.id.imageBtn2);
        final ImageView mainbtn3 = (ImageView) findViewById(R.id.imageBtn3);
        final ImageView mainbtn4 = (ImageView) findViewById(R.id.imageBtn4);
        mainlayr_scroll = (ScrollView) findViewById(R.id.main_scrolllay);
        Visualisation.res = getResources();
        res = getResources();
        pkgname = getApplicationContext().getPackageName();
        Visupics(80);
        StateListDrawable statesbtn1 = new StateListDrawable();
        statesbtn1.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.btn_exerc1_click));
        statesbtn1.addState(new int[] {}, getResources().getDrawable(R.drawable.btn_exerc1));
        mainbtn1.setImageDrawable(statesbtn1);
        mainbtn1.setClickable(true);

        StateListDrawable statesbtn2 = new StateListDrawable();
        statesbtn2.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.btn_exerc2_click));
        statesbtn2.addState(new int[] {}, getResources().getDrawable(R.drawable.btn_exerc2));
        mainbtn2.setImageDrawable(statesbtn2);
        mainbtn2.setClickable(true);

        StateListDrawable statesbtn3 = new StateListDrawable();
        statesbtn3.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.btn_breakout_click));
        statesbtn3.addState(new int[] {}, getResources().getDrawable(R.drawable.btn_breakout));
        mainbtn3.setImageDrawable(statesbtn3);
        mainbtn3.setClickable(true);

        StateListDrawable statesbtn4 = new StateListDrawable();
        statesbtn4.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.btn_statistics));
        statesbtn4.addState(new int[] {}, getResources().getDrawable(R.drawable.btn_statistics_click));
        mainbtn4.setImageDrawable(statesbtn4);
        mainbtn4.setClickable(true);

        mainbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), exercise1_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mainbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), exercise2_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        mainbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), breakout_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        mainbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), statistics_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //TTEMeth.ReadStatistics();
        //TODO: wieder reinklammern

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        BLEcs.BTDisconnect();
    }
    private void Visupics(int framecount)
    {
        Visualisation.pictures_pers.clear();
        Visualisation.pictures_top.clear();
        Visualisation.pictures_view.clear();
        for (int i = 0; i < framecount ; i++)
        {
            if (i < 10)
            {
                Visualisation.pictures_pers.add(getResources().getIdentifier("hand000" + i, "drawable", getPackageName()));
                //Visualisation.pictures_pers.add(res.getIdentifier(MyActivity.pkgname + ":drawable/hand000" + i + ".jpg", null, null));
                Visualisation.pictures_top.add(getResources().getIdentifier("handt000" + i, "drawable", getPackageName()));
                Visualisation.pictures_view.add(getResources().getIdentifier("handp000" + i, "drawable", getPackageName()));
                Log.d("blaaa", Visualisation.pictures_pers.get(Visualisation.pictures_pers.size()-1)+"");
            }

            else
            {
                Visualisation.pictures_pers.add(getResources().getIdentifier("hand00" + i, "drawable", getPackageName()));
                //Visualisation.pictures_pers.add(res.getIdentifier(MyActivity.pkgname + ":drawable/hand00" + i + ".jpg", null, null));
                Visualisation.pictures_top.add(getResources().getIdentifier("handt00" + i, "drawable", getPackageName()));
                Visualisation.pictures_view.add(getResources().getIdentifier("handp00" + i, "drawable", getPackageName()));
            }
        }
        Visualisation.Type = "Pers";

    }
}
