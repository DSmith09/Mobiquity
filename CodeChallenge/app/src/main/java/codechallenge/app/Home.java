package codechallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by David on 4/10/14.
 */

public class Home extends Activity {

    //Dropbox Variables
    private DbxAccount dbxAccount;
    private DbxAccountManager dbxAccountManager;
    private DbxFileSystem dbxFileSystem;
    public static DbxPath dbxPath;
    public static File mediaFileDir;

    //Button Variables
    private Button photoButton;
    private Button directoryButton;

    //Intent Variable to switch between Activities
    private Intent i;

    //logger
    Logger logger = Logger.getLogger(Home.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), "xhn3ix9acv5juwr", "hpfl6iup6rqer1h");
        dbxAccount = dbxAccountManager.getLinkedAccount();

        //Setup the view
        setupView();
    }

    private void setupView() {
        if(dbxAccount.isLinked()) {
            photoButton = (Button)findViewById(R.id.photoButton);
            directoryButton = (Button)findViewById(R.id.directoryButton);
            createDirectory();
            createLocalDirectory();
            //Button onClick Listeners
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(Home.this, Camera.class);
                    Home.this.startActivity(i);
                    Home.this.finish();
                }
            });

            directoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(Home.this, Directory.class);
                    Home.this.startActivity(i);
                    Home.this.finish();
                }
            });
        }

        else {
            //If the User somehow accessed this page without logging in, return them to the title page
            i = new Intent(this, MainActivity.class);
            this.startActivity(i);
            this.finish();
        }
    }

    //Create the directory on Dropbox if it hasn't already been created
    private void createDirectory() {
        try {
            dbxFileSystem = DbxFileSystem.forAccount(dbxAccount);
            dbxPath = new DbxPath("Code Challenge");
            if (!dbxFileSystem.exists(dbxPath)) {
                dbxFileSystem.createFolder(dbxPath);
            }
        }
        catch (DbxException e) {
            logger.log(Level.WARNING, "UNABLE TO CREATE DROPBOX DIRECTORY");
        }
    }

    //Create local directory for image files
    private void createLocalDirectory() {
        mediaFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "CodeChallenge");

        if(!mediaFileDir.exists()) {
            if(!mediaFileDir.mkdirs()) {
                logger.log(Level.WARNING, "DIRECTORY COULD NOT BE CREATED; ABORTING");
                return;
            }
        }
    }
}
