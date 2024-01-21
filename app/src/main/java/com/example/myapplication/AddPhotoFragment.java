package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPhotoFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button buttonCapturePhoto;
    private LinearLayout linearLayoutLabels;

    private Uri photoUri;
    private List<CheckBox> checkBoxes;
    private Map<String, Boolean> selectedLabels;

    private DatabaseReference photosReference;
    private DatabaseReference labelsReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        buttonCapturePhoto = view.findViewById(R.id.buttonTakePhoto);
        linearLayoutLabels = view.findViewById(R.id.linearLayoutLabels);

        photosReference = FirebaseDatabase.getInstance().getReference("photos");
        labelsReference = FirebaseDatabase.getInstance().getReference("labels");

        checkBoxes = new ArrayList<>();
        selectedLabels = new HashMap<>();

        buttonCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });

        loadLabels();

        return view;
    }

    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(getContext(), "Kamera uygulaması bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        photoUri = data.getData();
                        savePhotoToDatabase(photoUri.toString());
                        showPhoto();
                    }
                }
            }
    );

    private void showPhoto() {
        // Çekilen fotoğrafı ImageView içinde göster
        if (photoUri != null) {
            ImageView imageViewPhoto = getView().findViewById(R.id.imageViewPhoto);
            imageViewPhoto.setImageURI(photoUri);
        }
    }

    private void savePhotoToDatabase(String photoUrl) {
        String photoId = photosReference.push().getKey();
        Photo photo = new Photo(photoId, photoUrl, selectedLabels.toString());
        photosReference.child(photoId).setValue(photo);

        Toast.makeText(getContext(), "Fotoğraf başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
    }

    private void loadLabels() {
        labelsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    if (label != null) {
                        createCheckBoxForLabel(label);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumu
            }
        });
    }

    private void createCheckBoxForLabel(Label label) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(label.getName());
        checkBox.setTag(label.getId());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedLabels.put((String) buttonView.getTag(), isChecked);
        });

        linearLayoutLabels.addView(checkBox);
        checkBoxes.add(checkBox);
    }
}