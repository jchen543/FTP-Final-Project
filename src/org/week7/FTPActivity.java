package org.week7;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FTPActivity extends Activity {
    protected FTPClient mFTPClient;
	protected TextView ftpfile;
	private EditText ftpAddressStr;
	private EditText userid;
	private EditText pword;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
              
        ftpfile = (TextView) findViewById(R.id.ftpfile);
        ftpAddressStr = (EditText) findViewById(R.id.ftpip);
        userid = (EditText) findViewById(R.id.id);
        pword = (EditText) findViewById(R.id.password);
        
        final Button button = (Button) findViewById(R.id.connect);
        button.setOnClickListener(new View.OnClickListener() {
            private FTPClient mFTPClient;

			public void onClick(View v) {

    			mFTPClient = new FTPClient();
				
    			String ftpip = ftpAddressStr.getText().toString();
    			String id = userid.getText().toString();
    			String password = pword.getText().toString();
    			
            	if (ftpConnect(mFTPClient, ftpip, id, password, 21)){
                	Toast.makeText(getApplicationContext(), "Login successful ", 4).show();

                	String[] files = ftpGetCurrentWorkingDirectory(mFTPClient);
        			if (files.length > 1) {
        				for (int i = 0; i < files.length; i++) {
        					ftpfile.append(files[i]);
        					ftpfile.append("\n");
        				}

        			} else {

        				ftpfile.append("cannot display files");
        			}
            	} 
                }

                public boolean ftpConnect(FTPClient mFTPClient, String host, String username, String password, int port) {
            		try {
            			// connecting to the host
            			mFTPClient.connect(host, port);

            			// now check the reply code, if positive mean connection success
            				// login using username & password
            				boolean status = mFTPClient.login(username, password);

            				/*
            				 * Set File Transfer Mode
            				 * 
            				 * To avoid corruption issue you must specified a correct
            				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
            				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
            				 * transferring text, image, and compressed files.
            				 */
            				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            				mFTPClient.enterLocalPassiveMode();

            				return status;
            		} catch (Exception e) {
            			Toast.makeText(getApplicationContext(), "Login Unsuccessful", 4).show();
            			// You can display a dialog showing connection failure
            		}
            		return false;
            	}
                public String[] ftpGetCurrentWorkingDirectory(FTPClient mFTPClient) {
            		try {
            			mFTPClient.changeWorkingDirectory("Radio/MP3");
            			String[] workingDir = mFTPClient.listNames();
            			// display current directory
            			Toast.makeText(getApplicationContext(),
            					"You are at: " + mFTPClient.printWorkingDirectory(), 4)
            					.show();
            			
            			return workingDir;
            		} catch (Exception e) {
            			Toast.makeText(getApplicationContext(), "Cannot get current dir", 4)
            					.show();
            			return null;
            		}
            	}

                         });
        final Button disconnect = (Button) findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ftpDisconnect(mFTPClient));
			}
				public boolean ftpDisconnect(FTPClient mFTPClient) {
					try {
						mFTPClient.logout();
						mFTPClient.disconnect();
						return true;
					} catch (Exception e) {
            			Toast.makeText(getApplicationContext(), "Loged Out Successfull", 4).show();				
					}

					return false;
				}

			});
}
}

