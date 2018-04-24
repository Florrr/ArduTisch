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
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class statistics_Activity extends Activity {

    private static String TAG = "statistics";
    ImageView PaintScreen1;
    ImageView PaintScreen2;
    ArrayList<Integer> strainsInt32 = new ArrayList<Integer>();
    ArrayList<String> strainsDatesString = new ArrayList<String>();
    ArrayList<Integer> HeldTime = new ArrayList<Integer>();
    ArrayList<String> HeldDatesString = new ArrayList<String>();
    private Handler MP_Timer = new Handler();
    private boolean heightnot0 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_);
        PaintScreen1 = (ImageView) findViewById(R.id.imgStatistics1);
        PaintScreen2 = (ImageView) findViewById(R.id.imgStatistics2);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DrawStrains();
                DrawSummedUpTime();
            }
        };

        MP_Timer.postDelayed(runnable, 100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistics_, menu);
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
    int offsetLeft = 50;
    int groundpaint = 120;
    int PlotHeight = 200;
    int multyplier;
    int Plotwidth;
    int listsize;
    String value;
    int lowerscalevalue;
    int upperscalevalue;
    int listsizeTime;
    int upperscalevalueTime;
    int lowerscalevalueTime;
    int TimeBiggest = 0;
    int TimeSmallest = 10000000;
    int strainsBiggest = 0;
    int strainsSmallest = 10000000;
    public void DrawStrains()
    {
        Log.d(TAG,PaintScreen1.getWidth() + "/" + PaintScreen1.getHeight());
        Bitmap mp = Bitmap.createBitmap(PaintScreen1.getWidth(), PaintScreen1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas pictureBox = new Canvas(mp);
        Paint penwhite = new Paint();
        penwhite.setColor(Color.WHITE);
        Paint penblack = new Paint();
        penwhite.setColor(Color.BLACK);
        Paint pengray = new Paint();
        penwhite.setColor(Color.GRAY);
        pictureBox.drawRect(0, 0, PaintScreen1.getWidth(), PaintScreen1.getHeight(), penwhite);
        Paint pen1 = new Paint();
        penwhite.setColor(Color.RED);
        GetboundsOfDatas(strainsInt32);

        try
        {
            multyplier = Plotwidth / listsize;
        }
        catch(Exception e) { multyplier = 1; }


        for (int i = 1; i < strainsInt32.size(); i++)
        {
            //First List<>
            GetboundsOfDatas(strainsInt32);
            int y1 = CalcValueToDraw((double)strainsInt32.get(i - 1));
            int y2 = CalcValueToDraw((double)strainsInt32.get(i));
            //if (CalcValueToDraw(upperscalevalue) > y1 && CalcValueToDraw(upperscalevalue) > y2) //only draws data in the screen. otherwise it would be drawn outside of the rectangle
            pictureBox.drawLine(offsetLeft + (i - 1) * multyplier, ((y1) * -1) + PaintScreen1.getHeight() - groundpaint, offsetLeft + i * multyplier, ((y2) * -1) + PaintScreen1.getHeight() - groundpaint, pen1);
            pictureBox.drawText(strainsDatesString.get(i).substring(0, 10), offsetLeft + i * multyplier, ((groundpaint) * -1) + PaintScreen1.getHeight() + 10, penblack);
        }


        pictureBox.drawRect(offsetLeft, ((PlotHeight + groundpaint) * -1) + PaintScreen1.getHeight(), offsetLeft + (listsize * multyplier), PlotHeight + ((PlotHeight + groundpaint) * -1) + PaintScreen1.getHeight(), penblack);
        pictureBox.drawRect(offsetLeft - 1, (((PlotHeight + groundpaint) * -1) + PaintScreen1.getHeight() - 1), (offsetLeft - 1)+(listsize * multyplier), (PlotHeight + 2)+(((PlotHeight + groundpaint) * -1) + PaintScreen1.getHeight() - 1),penblack);



        //Things
        value = lowerscalevalue + ""; /*___________________________________________________________*/pictureBox.drawText(value, offsetLeft - value.length() * 9, PaintScreen1.getHeight() - groundpaint - 6 - 000,penblack);
        value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 1) + ""; pictureBox.drawText(value, offsetLeft - value.length() * 9, PaintScreen1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 1),penblack); pictureBox.drawLine(offsetLeft, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 1), offsetLeft + listsize * multyplier, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 1), pengray);
        value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 2) + ""; pictureBox.drawText(value, offsetLeft - value.length() * 9, PaintScreen1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 2), penblack); pictureBox.drawLine(offsetLeft, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 2), offsetLeft + listsize * multyplier, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 2),pengray);
        value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 3) + ""; pictureBox.drawText(value, offsetLeft - value.length() * 9, PaintScreen1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 3), penblack); pictureBox.drawLine(offsetLeft, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 3), offsetLeft + listsize * multyplier, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 3), pengray);
        value = lowerscalevalue + (((upperscalevalue - 000000000000000) / 4) * 4) + ""; pictureBox.drawText(value, offsetLeft - value.length() * 9, PaintScreen1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 4), penblack); pictureBox.drawLine(offsetLeft, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4, offsetLeft + listsize, PaintScreen1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4, pengray);
        value = "Strains over time"; pictureBox.drawText(value, offsetLeft, 10, penblack);

        PaintScreen1.setImageBitmap(mp);
    }
    public void DrawSummedUpTime()
    {
        Bitmap mp = Bitmap.createBitmap(PaintScreen2.getWidth(), PaintScreen2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas offScreenDC2 = new Canvas(mp);
        Paint penwhite = new Paint();
        penwhite.setColor(Color.WHITE);
        Paint penblack = new Paint();
        penwhite.setColor(Color.BLACK);
        Paint pengray = new Paint();
        penwhite.setColor(Color.GRAY);
        Paint pen1 = new Paint();
        penwhite.setColor(Color.RED);
        offScreenDC2.drawRect(0, 0, PaintScreen1.getWidth(), PaintScreen1.getHeight(), penwhite);

        multyplier = Plotwidth / listsizeTime;

        for (int i = 1; i < HeldTime.size(); i++)
        {
            //First List<>
            GetboundsOfDatas2(HeldTime);
            int y1 = CalcValueToDraw2((double)HeldTime.get(i - 1));
            int y2 = CalcValueToDraw2((double)HeldTime.get(i));
            //if (CalcValueToDraw(upperscalevalue) > y1 && CalcValueToDraw(upperscalevalue) > y2) //only draws data in the screen. otherwise it would be drawn outside of the rectangle
            offScreenDC2.drawLine(offsetLeft + (i - 1) * multyplier, ((y1) * -1) + PaintScreen2.getHeight() - groundpaint, offsetLeft + i * multyplier, ((y2) * -1) + PaintScreen2.getHeight() - groundpaint, pen1);
            offScreenDC2.drawText(HeldDatesString.get(i).substring(0, 10), offsetLeft + i * multyplier, ((groundpaint) * -1) + PaintScreen2.getHeight() + 10, penblack);
        }




        offScreenDC2.drawRect(offsetLeft, ((PlotHeight + groundpaint) * -1) + PaintScreen2.getHeight(), offsetLeft + (listsizeTime * multyplier), PlotHeight + ((PlotHeight + groundpaint) * -1) + PaintScreen2.getHeight(),penblack);
        offScreenDC2.drawRect(offsetLeft - 1, (((PlotHeight + groundpaint) * -1) + PaintScreen2.getHeight() - 1), (offsetLeft - 1) + (listsizeTime * multyplier), (PlotHeight + 2)+(((PlotHeight + groundpaint) * -1) + PaintScreen2.getHeight() - 1),penblack);



        //Things
        value = lowerscalevalue + ""; /*_____________________________________________________*/offScreenDC2.drawText(value,offsetLeft - value.length() * 9, PaintScreen2.getHeight() - groundpaint - 6 - 000,penblack);
        value = lowerscalevalueTime + (((upperscalevalueTime - lowerscalevalueTime) / 4) * 1) + ""; offScreenDC2.drawText(value, offsetLeft - value.length() * 9, PaintScreen2.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 1),penblack); offScreenDC2.drawLine(offsetLeft, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 1), offsetLeft + listsizeTime * multyplier, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 1),pengray);
        value = lowerscalevalueTime + (((upperscalevalueTime - lowerscalevalueTime) / 4) * 2) + ""; offScreenDC2.drawText(value, offsetLeft - value.length() * 9, PaintScreen2.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 2),penblack); offScreenDC2.drawLine(offsetLeft, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 2), offsetLeft + listsizeTime * multyplier, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 2),pengray);
        value = lowerscalevalueTime + (((upperscalevalueTime - lowerscalevalueTime) / 4) * 3) + ""; offScreenDC2.drawText(value, offsetLeft - value.length() * 9, PaintScreen2.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 3),pengray); offScreenDC2.drawLine(offsetLeft, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 3), offsetLeft + listsizeTime * multyplier, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 3),pengray);
        value = lowerscalevalueTime + (((upperscalevalueTime - 0000000000000000000) / 4) * 4) + ""; offScreenDC2.drawText(value, offsetLeft - value.length() * 9, PaintScreen2.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 4),pengray); offScreenDC2.drawLine( offsetLeft, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4, offsetLeft + listsize, PaintScreen2.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4,pengray);
        value = "Held time"; offScreenDC2.drawText(value, offsetLeft, 10,penblack);


        PaintScreen2.setImageBitmap(mp);
    }
    public void ReadData()
    {
        try
        {
            String strainsRawNP = TTEMeth.ReadSDFile("NumberofStrains.txt");

            //Dataset: 01.01.1999 value
            //strainsRaw = System.IO.File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "\\NumberOfStrains.txt");

            String[] buffer1 = strainsRawNP.split("_");

            for (int i = 0; i < buffer1.length - 1; i += 2)
            {
                strainsDatesString.add(buffer1[i]);
                strainsInt32.add(Integer.parseInt(buffer1[i + 1]));
            }

            for (int i = 0; i < strainsDatesString.size(); i++)
            {
                if (i + 1 < strainsDatesString.size())
                {
                    if (strainsDatesString.get(i).equals(strainsDatesString.get(i + 1)))
                    {
                        strainsInt32.set(i,strainsInt32.get(i) + strainsInt32.get(i + 1));
                        strainsInt32.remove(i + 1);
                        strainsDatesString.remove(i + 1);
                        i--;
                    }
                }
            }

            listsize = strainsInt32.size();

            //HeldTimeRaw = System.IO.File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "\\TimePerSession.txt");
            String buffer = TTEMeth.ReadSDFile("TimePerSession.txt");
            buffer1 = buffer.split("_");

            for (int i = 0; i < buffer1.length - 1; i += 2)
            {
                HeldDatesString.add(buffer1[i]);
                HeldTime.add(Integer.parseInt(buffer1[i + 1]));
            }

            //for (int i = 0; i < HeldTimeRaw.Length; i++)
            //{
            //    HeldTime.Add(Convert.ToInt32(new TimeSpan(Convert.ToInt32(HeldTimeRaw[i].Substring(11, 2)), Convert.ToInt32(HeldTimeRaw[i].Substring(14, 2)), Convert.ToInt32(HeldTimeRaw[i].Substring(17, 2))).TotalSeconds));
            //    HeldDates.Add(new DateTime(Convert.ToInt32(HeldTimeRaw[i].Substring(0, 4)), Convert.ToInt32(HeldTimeRaw[i].Substring(5, 2)), Convert.ToInt32(HeldTimeRaw[i].Substring(8, 2))));
            //}

            for (int i = 0; i < HeldDatesString.size(); i++)
            {
                if (i + 1 < HeldDatesString.size())
                {
                    if (HeldDatesString.get(i).equals(HeldDatesString.get(i + 1)))
                    {
                        HeldTime.set(i,HeldTime.get(i) + HeldTime.get(i + 1));
                        HeldTime.remove(i + 1);
                        HeldDatesString.remove(i + 1);
                        i--;
                    }
                }
            }
            listsizeTime = HeldTime.size();
        }
        catch (Exception e){ }
    }
    public int CalcValueToDraw(Double value)
    { try { return (int)((PlotHeight * (value)) / (upperscalevalue)); } catch(Exception e) { return 1; } }
    public int CalcValueToDraw2(Double value)
    { try { return (int)(((PlotHeight * (value)) / (upperscalevalueTime))); } catch (Exception e) { return 1; } }
    public void GetboundsOfDatas(ArrayList<Integer> datas1)
    {
        try
        {
            for (int i = 0; i < strainsInt32.size(); i++)
            {
                if (datas1.get(i) < strainsSmallest) strainsSmallest = datas1.get(i);
                if (datas1.get(i) > strainsBiggest) strainsBiggest = datas1.get(i);
            }
            upperscalevalue = strainsBiggest + (int)(strainsBiggest * 0.2);
            lowerscalevalue = 0;
        }
        catch (Exception e){ }
    }
    public void GetboundsOfDatas2(ArrayList<Integer> datas1)
    {
        try
        {

            for (int i = 0; i < datas1.size(); i++)
            {
                if (datas1.get(i) < TimeSmallest) TimeSmallest = datas1.get(i);
                if (datas1.get(i) > TimeBiggest)  TimeBiggest = datas1.get(i);
            }
            upperscalevalueTime = TimeBiggest + (int)(TimeBiggest * 0.2);
            lowerscalevalueTime = 0;
        }
        catch(Exception e) { }
    }
}
