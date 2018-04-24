package at.tte.emgtec.tte;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Florian Leitner on 05.12.2014.
 */
public class TTEMeth {
    private static String TAG ="TTEMeth";
    public static void WriteMPStatistics() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy");
        String date = df.format(c.getTime());

        try {
            File myFile = new File(Variables.SavePath, "MaximumPowerStatistics.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(date + "_" + Variables.MaximumPower + "_");
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
        }
    }
    public static String ReadSDFile(String Name){
        File file = new File(Variables.SavePath,Name);
        String strainsRawNP = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                strainsRawNP += line;
                strainsRawNP += '\n';
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return strainsRawNP;
    }
    public static void ReadStatistics() {
        File file = new File(Variables.SavePath, "Save.txt");

        StringBuilder text = new StringBuilder();
        ArrayList<String> statistics = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                statistics.add(line);
            }
        } catch (IOException e) {
        }
        Variables.TimeHeldSummedUp = statistics.get(1).substring(20, statistics.get(1).length() - 1);
        Variables.NumberOfStrains = Integer.parseInt(statistics.get(2).substring(20, statistics.get(2).length() - 1));
        Variables.longest = Integer.parseInt(statistics.get(3).substring(20, statistics.get(3).length() - 1));
    }

    public static void WriteStatistics() {
        try {
            //Allgemeine Statistiken
            //String Savetext = "\n[TimeHeldSummedUp__]" + TimeHeldSummedUp + "\n[NumberOfStrains___]" + NumberOfStrains + "\n[Longest___________]" + longest;
            WriteClearFile("Save.txt", "\n[TimeHeldSummedUp__]" + Variables.TimeHeldSummedUp + "\n[NumberOfStrains___]" + Variables.NumberOfStrains + "\n[Longest___________]" + Variables.longest);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy");
            String date = df.format(c.getTime());

            WriteAppendFile("NumberOfStrains.txt", date + "_" + Variables.NumberOfStrainsThisSession + "_");


            // System.IO.File.AppendAllText(AppDomain.CurrentDomain.BaseDirectory+"\\NumberOfStrains.txt",date  + " " + NumberOfStrainsThisSession + "\r\n");
            Variables.NumberOfStrainsThisSession = 0;
            WriteAppendFile("TimePerSession.txt", date + "_" + Variables.TimeHeldThisSession + "_");
        } catch (Exception e) {
            //MessageBox.Show("Dateipfad falsch");
        }
    }

    public static void WriteClearFile(String Name, String Text) {
        try {
            File myFile = new File(Variables.SavePath, Name);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(Text);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Log.d(TAG, "write clear file failed");
        }
    }

    private static void WriteAppendFile(String Name, String Text) {
        try {
            File myFile = new File(Variables.SavePath, Name);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(Text);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
        }
    }
}

