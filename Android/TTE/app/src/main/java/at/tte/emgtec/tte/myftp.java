package at.tte.emgtec.tte;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
    * Created by Florian Leitner on 28.09.2014.
            */
    public class myftp {
    private String host = null;
    private String user = null;
    private String pass = null;
    FTPClient mFTPClient;
    String TAG = "FTP";

    //Android-Spezifisch
    Context context;//Context!!
    int port = 21;
    public myftp(String hostIP, String userName, String password) { host = hostIP; user = userName; pass = password;}

    boolean status1 = false;
    boolean finished1 = false;
    public boolean download(final String srcFilePath, final String desFilePath){
        status1 = false;
        finished1 = false;
        new Thread() {
            @Override
            public void run() {
                Connect(host, user, pass);
                ChangeDirectory(srcFilePath);
                try {
                    FileOutputStream desFileStream = new FileOutputStream(desFilePath);
                    status1 = mFTPClient.retrieveFile(srcFilePath, desFileStream);
                    desFileStream.close();
                } catch (Exception e) {
                    Log.d(TAG, "download failed");
                }
                Disconnect();
                finished1 = true;
            }
        }.start();
        while(!finished1) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status1;
    }
    boolean status2 = false;
    boolean finished2 = false;
    public boolean upload(final String srcFilePath,final String desDirectory){
        status2 = false;
        finished2 = false;
        new Thread() {
            @Override
            public void run() {
                Connect(host, user, pass);
                ChangeDirectory(desDirectory);

                try {
                    String[] path = srcFilePath.split("/");
                    //FileInputStream srcFileStream = context.openFileInput(srcFilePath);

                    File firstLocalFile = new File(srcFilePath);


                    InputStream inputStream = new FileInputStream(firstLocalFile);

                    // change working directory to the destination directory
                    //if (ftpChangeDirectory(desDirectory)) {
                    status2 = mFTPClient.storeFile(path[path.length - 1], inputStream);
                    //}
                    inputStream.close();

                } catch (Exception e) {
                    Log.d(TAG, "upload failed: " + e);
                }
                Disconnect();
                finished2 = true;
            }
        }.start();
        while(!finished2) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status2;
    }
    boolean status3 = false;
    boolean finished3 = false;
    public boolean delete(final String filePath)
    {
        status3 = false;
        finished3 = false;
        new Thread() {
            @Override
            public void run() {
                Connect(host, user, pass);
                try {
                    status3 = mFTPClient.deleteFile(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Disconnect();
                finished3 = true;
            }
        }.start();
        while(!finished3) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status3;
    }
    boolean status4 = false;
    boolean finished4 = false;
    public boolean deleteDirectory(final String dir_path)
    {
        status4 = false;
        finished4 = false;
        new Thread() {
            @Override
            public void run() {
                Connect(host, user, pass);
                try {
                    status4 = mFTPClient.removeDirectory(dir_path);
                } catch (Exception e) {
                    Log.d(TAG, "Error: could not remove directory named " + dir_path);
                }
                Disconnect();
                finished4 = true;
            }
        }.start();
        while(!finished4)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return status4;
    }
    public void deleteDirectory(String deleteFolder, Boolean deleteAll)
    {
        if (!deleteAll)
            deleteDirectory(deleteFolder);
        else
        {
            ArrayList<String> deleteRoot = new ArrayList<String>(Arrays.asList(directoryListSimple(deleteFolder)));
            deleteRoot.remove(""); deleteRoot.remove("."); deleteRoot.remove("..");
            for(String elem : deleteRoot)
            {
                if (!elem.contains("."))
                    deleteDirectory(deleteFolder + "/" + elem, true);
                else
                    delete(deleteFolder + "/" + elem);
            }

            deleteRoot = new ArrayList<String>(Arrays.asList(directoryListSimple(deleteFolder)));
            deleteRoot.remove(""); deleteRoot.remove("."); deleteRoot.remove("..");
            if (deleteRoot.size() == 0)
                deleteDirectory(deleteFolder);
        }
    }
    boolean status5 = false;
    boolean finished5 = false;
    public boolean createDirectory(final String new_dir_path)
    {
        status5 = false;
        finished5 = false;
        new Thread() {
            @Override
            public void run() {
                Connect(host, user, pass);
                try {
                    status5 = mFTPClient.makeDirectory(new_dir_path);
                } catch (Exception e) {
                    Log.d(TAG, "Error: could not create new directory named " + new_dir_path);
                }
                Disconnect();
                finished5 = true;
            }
        }.start();
        while (!finished5)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status5;
    }
    boolean finished6 = false;
    public String[] directoryListSimple(final String directory)
    {
        finished6 = false;
        final ArrayList<String> filelist = new ArrayList<String>();
        new Thread() {
            @Override
            public void run() {

                Connect(host, user, pass);
                try {
                    FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
                    int length = ftpFiles.length;

                    for (int i = 0; i < length; i++) {
                        String name = ftpFiles[i].getName();
                        boolean isFile = ftpFiles[i].isFile();

                        if (isFile) {
                            filelist.add(name);
                        } else {
                            filelist.add(name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Disconnect();
                finished6 = true;
            }
        }.start();
        while(!finished6)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return filelist.toArray(new String[filelist.size()]);
    }
    //Method to connect to FTP server:
    private boolean Connect(String host, String username, String password)
    {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);

            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(username, password);

	            /* Set File Transfer Mode
	             *
	             * To avoid corruption issue you must specified a correct
	             * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
	             * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
	             * for transferring text, image, and compressed files.
	             */
                mFTPClient.setFileType(FTP.ASCII_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                return status;
            }
        } catch(Exception e) {
            Log.d("connection","Error: could not connect to host " + host + " reason: " + e);
        }

        return false;
    }

    private boolean Disconnect()
    {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }

        return false;
    }
    private boolean ChangeDirectory(String directory_path)
    {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch(Exception e) {
            Log.d(TAG, "Error: could not change directory to " + directory_path);
        }
        return false;
    }
}

