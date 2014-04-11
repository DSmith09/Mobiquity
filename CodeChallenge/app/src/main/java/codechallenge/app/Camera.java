package codechallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by David on 4/10/14.
 */
public class Camera extends Activity {

    //Variable to get file path
    private File mediaFile;

    //Dropbox Variables
    private DbxAccountManager mDbxAcctMgr;
    private DbxAccount dbxAccount;
    private DbxFileSystem dbxFileSystem;
    private DbxFile file;

    //Logger
    Logger logger = Logger.getLogger(Camera.class.getName());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), "xhn3ix9acv5juwr", "hpfl6iup6rqer1h");
        dbxAccount = mDbxAcctMgr.getLinkedAccount();

        //Create intent to start image capture
        final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri();
        i.putExtra("CodeChallenge", fileUri);

        //Create new file object in Dropbox
        writeNewFile();

        //Start image capture intent
        startActivityForResult(i, 0);
    }

    //Generates the Correct IMG Format with TimeStamp
    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());

        mediaFile = new File(Home.mediaFileDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                File upload = new File(mediaFile.getPath());

                try {
                    file.writeFromExistingFile(upload, true);
                    file.close();
                }
                catch(IOException e) {
                    logger.log(Level.WARNING, "File not found!");
                }
                Toast.makeText(this, "Image saved to Dropbox User: " +
                      dbxAccount.getUserId(), Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Camera Stopped", Toast.LENGTH_LONG).show();

            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "IMAGE CAPTURE FAILED", Toast.LENGTH_LONG).show();
            }
        goHome();
    }

    //Return back to Home Activity
    private void goHome() {
        Intent finish = new Intent(this, Home.class);
        this.startActivity(finish);
        this.finish();
    }

    //Method to create new File Object in Dropbox for image capture file
    private void writeNewFile() {
        try {
            dbxFileSystem = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        }
        catch(DbxException.Unauthorized e) {
            logger.log(Level.WARNING, "User Unauthorized with app");
            goHome();
        }

        try {
            file = dbxFileSystem.create(new DbxPath(Home.dbxPath + mediaFile.getPath()));
        }
        catch(DbxException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}