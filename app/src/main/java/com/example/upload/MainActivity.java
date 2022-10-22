package com.example.upload;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.upload.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FirebaseStorage storage;
    ActivityMainBinding binding;
    Uri fileUri;
    StorageReference sR;
    ProgressDialog pD;
    ActivityResultLauncher<String> sPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        fileUri = result;
                    }
                });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
//        pD = new ProgressDialog(this);
//        pD.setTitle("Uploading");
//        pD.show();
        sPhoto.launch("image/*");
        SimpleDateFormat fN = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String fname = fN.format(now);
        sR = FirebaseStorage.getInstance().getReference("images"+fname);
        sR.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        binding.imageView.setImageURI(fileUri);
                        Toast.makeText(MainActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
//                        if(pD.isShowing()){
//                            pD.dismiss();
//                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        if(pD.isShowing()){
//                            pD.dismiss();
//                        }
                        Toast.makeText(MainActivity.this,"Uploaded failed because of"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

}