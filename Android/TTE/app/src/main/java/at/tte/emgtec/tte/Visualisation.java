package at.tte.emgtec.tte;

import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Florian Leitner on 16.10.2014.
 */
public class Visualisation {
    static int framecount = 80;
    public static ArrayList<Integer> pictures_pers = new ArrayList<Integer>();
    public static ArrayList<Integer> pictures_top = new ArrayList<Integer>();
    public static ArrayList<Integer> pictures_view = new ArrayList<Integer>();

    static int aktuellerFrame=0;
    static double Border;
    static String Type = "Pers";
    public static Resources res;
    static int bufferA, bufferB;
    public static void SetData(int Framecount)
    {

        // MessageBox.Show("Fucking error fuck. Please try to fucking reopen this fuck, little fucker.\n" + "For you fucking geeks: Here are some fucking numbers...  EX:17986465785489\n"+"Hope you´re happy with this shit. I´m fucking outa here.", "U FAILED AT FAILING"); } //

    }
    public static void Update(int value2, int value1, String type) {
        Type = type;
        Border = Variables.MaximumPower * 0.7;
        if (Variables.S_FrameSwitchMethod.equals("Threasholdvalue")) {
            /*
            if (value1 > Border) {
                if (aktuellerFrame < framecount - 1)
                    aktuellerFrame++;
            }

            if (value2 > Border) {
                if (aktuellerFrame > 0)
                    aktuellerFrame--;
            }
            */
            int bord1 = (int)(Variables.MaximumPower * 0.7);
            int bord2 = (int)(Variables.MaximumPower * 0.7);
            if (value1 < bord1 && value2 > bord2) {
                if (aktuellerFrame < framecount - 1)
                    aktuellerFrame+=5;
            }

            if (value2 < bord2 && value1 > bord1) {
                if (aktuellerFrame > 5)
                    aktuellerFrame-=5;
            }
            Log.d("Threas",aktuellerFrame + " " + value1 + " " + 650 + " " +value2);
        } else if (Variables.S_FrameSwitchMethod.equals("LinearSwitching")) {
            bufferA = aktuellerFrame;

            if (aktuellerFrame < framecount && aktuellerFrame >= 0)
                aktuellerFrame = (framecount * value1) / Variables.MaximumPower;

            bufferB = aktuellerFrame;
            Log.d("Linear",aktuellerFrame + "");

            //Smoothing
            if (bufferA - bufferB < 1)
                aktuellerFrame += (bufferA - bufferB) - (bufferA - bufferB + 1);
            if (bufferA - bufferB > 1)
                aktuellerFrame -= (bufferA - bufferB) - (bufferA - bufferB - 1);
        }




        //Limitation
        if (aktuellerFrame <= 1)
            aktuellerFrame = 1;
        if (aktuellerFrame > 78)
            aktuellerFrame = 79;

        Log.d("Frame",aktuellerFrame + "");
    }

    public static int getPic()
    {
        if (Type.equals("Pers")) {
            return pictures_pers.get(aktuellerFrame);
            //Log.d("lol", "wtf");
        }
        if (Type.equals("Top"))
            return pictures_top.get(aktuellerFrame);
        if (Type.equals("Real"))
            return pictures_view.get(aktuellerFrame);
        return 0;
    }
}
