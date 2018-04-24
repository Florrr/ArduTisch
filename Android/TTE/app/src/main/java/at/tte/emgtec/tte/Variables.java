package at.tte.emgtec.tte;

import java.util.ArrayList;

/**
 * Created by Florian Leitner on 15.10.2014.
 */
public class Variables {

    public static String userTherapist = "";
    public static String userID = "";
    public static String DataSave = "";
    public static Boolean loggedOn = false;
    public static int MaximumPower = 0;
    public static String S_FrameSwitchMethod = "Threasholdvalue";
    public static String received;
    public static boolean checkconnection = false;
    public static ArrayList<Integer> EMGblau = new ArrayList<Integer>();
    public static ArrayList<Integer> EMGrot = new ArrayList<Integer>();
    //MEINS!!
    public static String SavePath = "";
    public static String TimeHeldSummedUp;
    public static int NumberOfStrains;
    public static long longest;
    public static int NumberOfStrainsThisSession;
    /**
     * TotalSecounds
     */
    public static int TimeHeldThisSession;

}
