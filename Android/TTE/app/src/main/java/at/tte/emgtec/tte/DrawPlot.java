package at.tte.emgtec.tte;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Florian Leitner on 15.10.2014.
 */
public class DrawPlot {
    String TAG ="DrawPlot";
    //Variables for Plotter
    Double ToleranzOfNextValue = 0.03;
    ImageView Plot_PictureBox1;
    //DrawData
    Bitmap offScreenBmp;
    Canvas offScreenDC;
    Paint pen1 = new Paint();
    Paint pen2 = new Paint();

    //Datathings
    int listsize = 400;
    int startvalue = 2000;

    //Graphical variables
    Paint pen = new Paint();
    int lowestvalue = 70;
    int biggest = 0;
    int biggest1 = 0; //Biggest value of datas1<>
    int biggest2 = 0; //Biggest value of datas2<>
    int smallest1 = 100000; //Smallest value of datas1<>
    int smallest2 = 100000; //Smallest value of datas2<>
    int groundpaint = 25;
    int y1, y2;
    //Font schrift = new Font("Arial", 10, FontStyle.Bold);
    //Font MaximumPowerSchrift = new Font("Arial", 20, FontStyle.Bold);
    int offsetLeft = 50;
    int PlotHeight = 260;
    Boolean DrawLowerBorder = false;
    Boolean DrawUpperBorder = false;

    //settings
    Boolean show_borders = false;
    Boolean autoscale_up = true;
    int lowerscalevalue = 0;
    int upperscalevalue;
    String MuscleToCheck = "Blue";


    public String DP_exercise = "";

    //Exercise 1
    int lowerborder = 200;
    int upperborder = 800;
    int lowerborderEx2 = 200;
    int upperborderEx2 = 800;// currentPos = 2

    //MaximumPowertest
    int MP_TimeLeft = 10;


    public void DP_SetPictureBox(ImageView pictureBox)
    {
        Plot_PictureBox1 = pictureBox;

        offScreenBmp = Bitmap.createBitmap(pictureBox.getWidth(), pictureBox.getHeight(), Bitmap.Config.ARGB_8888);
        offScreenDC = new Canvas(offScreenBmp);
        pen1.setColor(Color.BLUE);
        pen1.setStrokeWidth(4);
        pen2.setColor(Color.RED);
        pen2.setStrokeWidth(4);
        pen.setColor(Color.BLACK);
    }
    public void DP_SetoffsetLeft(int newoffset)
    { offsetLeft = newoffset; }
    public void DP_SetgroundPaint(int newgroundpaint)
    { groundpaint = newgroundpaint; }
    public void DP_SetPlotHeight(int newPlotHeight)
    { PlotHeight = newPlotHeight; }
    public void DP_SetDP_exercise(String newexercise)
    { DP_exercise = newexercise; }
    public void DP_SetMuscleToCheck(String newMuscleToCheck)
    { MuscleToCheck = newMuscleToCheck; }
    public void DP_SetPen1_Color(int color)
    { pen1.setColor(color);}
    public void DP_SetPen2_Color(int color)
    { pen2.setColor(color); }
    public void DP_Setlistsize(int newlistsize)
    { listsize = newlistsize; }
    public void DP_SetMP_TimeLeft(int newTime)
    { MP_TimeLeft = newTime; }
    public void DP_SetBorders(int newupperborder, int newLowerBorderEx2, int newUpperBorderEx2)
    {
        upperborder = newupperborder;
        lowerborderEx2 = newLowerBorderEx2;
        upperborderEx2 = newUpperBorderEx2;
    }

    public int DP_GetBiggest1()
    { return biggest1; }

    public void DrawData(ArrayList<Integer> datas1, ArrayList<Integer> datas2)
    {
        try
        {
            //Canvas pictureBox = new Canvas(((BitmapDrawable)Plot_PictureBox1.getDrawable()).getBitmap());
            Paint penwhite = new Paint();
            penwhite.setColor(Color.WHITE);
            offScreenDC.drawRect(0,0,Plot_PictureBox1.getWidth(),Plot_PictureBox1.getHeight(),penwhite);

            //Draw actual Data
            for (int i = 1; i < datas1.size() - 1; i++)
            {
                //First List<>
                GetboundsOfDatas(datas1, datas2);
                y1 = CalcValueToDraw((double)datas1.get(i - 1));
                y2 = CalcValueToDraw((double)datas1.get(i));
                //if (CalcValueToDraw(upperscalevalue) > y1 && CalcValueToDraw(upperscalevalue) > y2) //only draws data in the screen. otherwise it would be drawn outside of the rectangle
                offScreenDC.drawLine(offsetLeft + i*8 - 8, ((y1) * -1) + Plot_PictureBox1.getHeight() - groundpaint, offsetLeft + i*8, ((y2) * -1) + Plot_PictureBox1.getHeight() - groundpaint,pen1);
                //Second List<>
                //GetboundsofDatas(datas1,datas2) not needed
                y1 = CalcValueToDraw((double)datas2.get(i-1));
                y2 = CalcValueToDraw((double)datas2.get(i));
                //if (CalcValueToDraw(upperscalevalue) > y1 && CalcValueToDraw(upperscalevalue) > y2) //only draws data in the screen. otherwise it would be drawn outside of the rectangle
                offScreenDC.drawLine(offsetLeft + i*8 - 8, ((y1) * -1) + Plot_PictureBox1.getHeight() - groundpaint, offsetLeft + i*8, ((y2) * -1) + Plot_PictureBox1.getHeight() - groundpaint,pen2);
            }

            //Draw Everything around the acutal Data
            //Black Rectangle
            Paint paintblack = new Paint();
            paintblack.setColor(Color.BLACK);
            paintblack.setStrokeWidth(2);
            Paint paintblackunfilled = new Paint();

            paintblackunfilled.setColor(Color.BLACK);
            paintblackunfilled.setStrokeWidth(5);
            paintblackunfilled.setStyle(Paint.Style.STROKE);
            offScreenDC.drawRect(offsetLeft, ((PlotHeight + 50) * -1) + Plot_PictureBox1.getHeight() + groundpaint,  offsetLeft+listsize, (((PlotHeight + 50) * -1) + Plot_PictureBox1.getHeight() + groundpaint)+PlotHeight,paintblackunfilled);

            //Draw and scale lines in x - direction
            paintblack.setStrokeWidth(1);
            Paint paintgray = new Paint();
            paintgray.setTextSize(48f);
            paintblack.setTextSize(48f);
            paintgray.setColor(Color.GRAY);
            String value;
            value = lowerscalevalue+""; /*______________________________________________*/offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 -  000000000000000000 ,paintblack);
            value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 1)+""; offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 1),paintblack); offScreenDC.drawLine(offsetLeft, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 1), offsetLeft + listsize, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 1),paintgray);
            value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 2)+""; offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 2),paintblack); offScreenDC.drawLine(offsetLeft, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 2), offsetLeft + listsize, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 2),paintgray);
            value = lowerscalevalue + (((upperscalevalue - lowerscalevalue) / 4) * 3)+""; offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 3),paintblack); offScreenDC.drawLine(offsetLeft, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 3), offsetLeft + listsize, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 3),paintgray);
            value = lowerscalevalue + (((upperscalevalue - 000000000000000) / 4) * 4)+""; offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 4),paintblack); offScreenDC.drawLine(offsetLeft, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4, offsetLeft + listsize, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4,paintgray);
            value = lowerscalevalue + (((upperscalevalue - 000000000000000) / 4) * 4)+""; offScreenDC.drawText(value, offsetLeft - value.length() * 30, Plot_PictureBox1.getHeight() - groundpaint - 6 - (PlotHeight / 4 * 4),paintblack); offScreenDC.drawLine(offsetLeft, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4, offsetLeft + listsize, Plot_PictureBox1.getHeight() - groundpaint - (PlotHeight / 4 * 4) - PlotHeight % 4,paintgray);

            Paint penred = new Paint();
            penred.setStrokeWidth(2);
            Paint penthreas = new Paint();
            penthreas.setColor(Color.BLACK);
            penthreas.setTextSize(32f);
            pen.setColor(Color.RED);
            //idea: Add an arrow uppwards, if a border is now displayed yet
            if (DP_exercise.equals("Exercise 1"))
            {
                if (upperborder < biggest1) //DrawUpper ThreasHold Line
                {
                    offScreenDC.drawLine(offsetLeft, ((CalcValueToDraw((double)upperborder)) * -1) + Plot_PictureBox1.getHeight() - groundpaint, offsetLeft + listsize, ((CalcValueToDraw((double)upperborder)) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penred);
                    offScreenDC.drawText("upper threashold:" + upperborder, offsetLeft+5, ((CalcValueToDraw((double)upperborder)+5) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penthreas);
                }
                else //Draw Arrow Up
                {
                    offScreenDC.drawText("↑ Threasholds" , offsetLeft + 15, ((PlotHeight + 15) * -1) + Plot_PictureBox1.getHeight() - groundpaint,paintblack);
                    //offScreenDC.DrawRectangle(new Pen(Color.Black, 1), offsetLeft, ((PlotHeight + 50) * -1) + Plot_PictureBox1.Height + groundpaint, listsize, PlotHeight);
                }
            }

            if (DP_exercise.equals("Exercise 2"))
            {
                if (upperborderEx2 < biggest1)
                {
                    offScreenDC.drawLine(offsetLeft, ((CalcValueToDraw((double)upperborderEx2)) * -1) + Plot_PictureBox1.getHeight() - groundpaint, offsetLeft + listsize, ((CalcValueToDraw((double)upperborderEx2)) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penred);
                    offScreenDC.drawText("upper threashold:" + upperborderEx2, offsetLeft+5, ((CalcValueToDraw((double)upperborder)+5) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penthreas);
                }
                if (lowerborderEx2 < biggest1)
                {
                    offScreenDC.drawLine(offsetLeft, ((CalcValueToDraw((double)lowerborderEx2)) * -1) + Plot_PictureBox1.getHeight() - groundpaint, offsetLeft + listsize, ((CalcValueToDraw((double)lowerborderEx2)) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penred);
                    offScreenDC.drawText("lower threashold:" + lowerborderEx2, offsetLeft+5, ((CalcValueToDraw((double)lowerborder)+5) * -1) + Plot_PictureBox1.getHeight() - groundpaint,penthreas);
                }
            }
            if (DP_exercise.equals("MaximumPower"))
            {
                Variables.MaximumPower = biggest1;
                offScreenDC.drawText("Your maximum power is: " + biggest1 + " mV!", Plot_PictureBox1.getWidth() / 2 - (biggest+"Your maximum power is:").length() * 2 - 300, 110,paintblack);
                offScreenDC.drawText("Time left: " + (10 - MP_TimeLeft), 20, 45,paintblack);
            }

            try
            {
                //pictureBox.drawImage(offScreenBmp, 0, 0);
                Plot_PictureBox1.setImageBitmap(offScreenBmp);
            }
            catch (Exception ex) {  }
        }
        catch(Exception e) { Log.d(TAG, "Problem in DrawData"); }
    }
    public int CalcValueToDraw(Double value)
    { try { return (int)(PlotHeight * (value)) / (upperscalevalue); } catch(Exception e) { return 1; } }
    public void GetboundsOfDatas(ArrayList<Integer> datas1, ArrayList<Integer> datas2)
    {
        try
        {
            biggest1 = 0; //Could be set. If biggest1 = 0; gets commended away, then the autoscale only work in the up direction.
            //First List<> datas1<>
            if (autoscale_up)
            {
                for (int i = 0; i < datas1.size(); i++)
                {
                    if (datas1.get(i) < smallest1) smallest1 = datas1.get(i);
                    if (datas1.get(i) > biggest1) biggest1 = datas1.get(i);
                }
            }

            //Second List<> datas2<>
            if (autoscale_up)
            {
                upperscalevalue = 0;
                for (int i = 0; i < datas2.size(); i++)
                {
                    if (datas2.get(i) < smallest1) smallest1 = datas2.get(i);
                    if (datas2.get(i) > biggest1) biggest1 = datas2.get(i);
                }

                upperscalevalue = biggest1 + 30;
            }

            lowestvalue = (int)(biggest1 * 0.01);
        }
        catch(Exception e) { Log.d(TAG,"Problem in GetBoundsOfData()"); }
    }
}
