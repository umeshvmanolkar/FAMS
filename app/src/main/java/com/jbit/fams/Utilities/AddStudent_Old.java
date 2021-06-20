package com.jbit.fams.Utilities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.legacy.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbit.fams.DB.AppDatabase;
import com.jbit.fams.EditStudents;
import com.jbit.fams.ImportStudentsByExcel;
import com.jbit.fams.R;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.AddPersistedFaceResult;
import com.microsoft.projectoxford.face.contract.CreatePersonResult;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AddStudent_Old extends AppCompatActivity {

    ImagePicker_old picker;

    String studentName;
    String regNo;

    ImageView takenImageForStudent;
    private static final int PICK_IMAGE_ID = 200;
    Bitmap bitmap;

    boolean imageTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        createFolder();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarSubtitle = toolbar.findViewById(R.id.toolbar_subtitle);
        toolbarSubtitle.setText((AppDatabase.getAppDatabase(this)).courseDao().getCourseNameById(Prefs.getString("courseId", "")));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(100);

        takenImageForStudent = findViewById(R.id.takenImageForStudent);

        takenImageForStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext(), getString(R.string.pick_image_intent_text_student));
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
*/

                 picker = new ImagePicker_old();


                Intent chooseImageIntent = picker.captureImage(AddStudent_Old.this);//ImagePicker.getPickImageIntent(getApplicationContext(), getString(R.string.pick_image_intent_text_student));
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

            }
        });

        (findViewById(R.id.rotate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = ImagePicker.rotate(bitmap, 90);
                takenImageForStudent.setImageBitmap(bitmap);
            }
        });

        final Button addStudent = findViewById(R.id.addStudent);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (alreadyHasPermission()) {
                    studentName = ((EditText) findViewById(R.id.studentName)).getText().toString();
                    regNo = ((EditText) findViewById(R.id.regNo)).getText().toString();

                    AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

                    if (!imageTaken) {
                        Toast.makeText(AddStudent_Old.this, "Select an image for the student", Toast.LENGTH_SHORT).show();
                    } else if (studentName.equals("")) {
                        ((EditText) findViewById(R.id.studentName)).setError("Please enter a Student Name");
                        (findViewById(R.id.studentName)).requestFocus();
                    } else if (regNo.equals("")) {
                        ((EditText) findViewById(R.id.regNo)).setError("Please enter a Registration Number");
                        (findViewById(R.id.regNo)).requestFocus();
                    } else if (database.studentDao().checkRegNoUnique(regNo) == 1) {
                        ((EditText) findViewById(R.id.regNo)).setError("Registration number should be unique");
                        (findViewById(R.id.regNo)).requestFocus();
                    } else {
                        new AddPersonTask().execute(Prefs.getString("courseId", ""), studentName, regNo);
                    }
                } else {
                    ActivityCompat.requestPermissions(AddStudent_Old.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

            }
        });

        (findViewById(R.id.importByExcel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ImportStudentsByExcel.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddStudent_Old.this, EditStudents.class));
    }

    private boolean alreadyHasPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStart();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied to write your External storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
*/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICK_IMAGE_ID:
                //bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                bitmap = ImagePicker.getImageFromResult1(this,resultCode,data);
                takenImageForStudent.setImageBitmap(bitmap);

                imageTaken = true;

                if (bitmap == null){
                    takenImageForStudent.setImageDrawable(getDrawable(R.drawable.circle_icon));
                    imageTaken = false;
                }
                else{
                    (findViewById(R.id.rotate)).setVisibility(View.VISIBLE);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            //*** Rename File
            String strNewName = "MyPicture.jpg";
            String NewPath = strSDCardPathName + strNewName;




            //*** Resize Images
            try {
                ResizeImages(picker.mCurrentPhotoPath, NewPath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

             bitmap = BitmapFactory.decodeFile(NewPath);
            takenImageForStudent.setImageBitmap(bitmap);

            imageTaken = true;

            if (bitmap == null){
                takenImageForStudent.setImageDrawable(getDrawable(R.drawable.circle_icon));
                imageTaken = false;
            }
        }
    }

    public static void ResizeImages(String sPath, String sTo) throws IOException {

        Bitmap photo = BitmapFactory.decodeFile(sPath);
        photo = Bitmap.createScaledBitmap(photo, 300, 300, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(sTo);
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        File file = new File(sPath);
        file.delete();

    }

    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";

    public void createFolder() {
        File folder = new File(strSDCardPathName);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

    }


























    // Background task of adding a person to person group.
    class AddPersonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // Get an instance of face service client.
            FaceServiceClient faceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));
            try{
                publishProgress("Syncing with server to add person...");
                Log.v("","Request: Creating Person in person group" + params[0]);

                // Start the request to creating person.
                CreatePersonResult createPersonResult = faceServiceClient.createPersonInLargePersonGroup(
                        params[0], //personGroupID
                        params[1], //name
                        params[2]); //userData or regNo

                return createPersonResult.personId.toString();


            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.v("",e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String personId) {

            if (personId != null) {
                Log.v("","Response: Success. Person " + personId + " created.");

                //Toast.makeText(AddStudent.this, "Person with personId "+personId+" successfully created", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddStudent_Old.this, "Student was successfully created", Toast.LENGTH_SHORT).show();


                new AddFaceTask().execute(personId);
            }
        }
    }

    class AddFaceTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));
            try{
                Log.v("", "Adding face...");
                UUID personId = UUID.fromString(params[0]);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                InputStream imageInputStream = new ByteArrayInputStream(stream.toByteArray());

                AddPersistedFaceResult result = faceServiceClient.addPersonFaceInLargePersonGroup(
                        Prefs.getString("courseId", ""),
                        personId,
                        imageInputStream,
                        "",
                        null);


                File folder = new File(Environment.getExternalStorageDirectory(), "/Faces/");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                File photo = new File(Environment.getExternalStorageDirectory(), "/Faces/"+result.persistedFaceId.toString()+".jpg");
                if (photo.exists()) {
                    photo.delete();
                }

                try {



                    FileOutputStream fos= new FileOutputStream(photo.getPath());

                    fos.write(stream.toByteArray());
                    fos.close();

                    Log.v("Store face in storage", "Face stored with name "+photo.getName()+" and path "+photo.getAbsolutePath());
                }
                catch (IOException e) {
                    Log.e("Store face in storage", "Exception in photoCallback", e);
                }

                return result.persistedFaceId.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String persistedFaceId) {
            Log.v("", "Successfully added face with persistence id "+persistedFaceId);

            Toast.makeText(AddStudent_Old.this, "Face was successfully added to the student", Toast.LENGTH_SHORT).show();

            //Toast.makeText(AddStudent.this, "Face with persistedFaceId "+persistedFaceId+" successfully created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStudent_Old.this, EditStudents.class));
        }
    }
}
