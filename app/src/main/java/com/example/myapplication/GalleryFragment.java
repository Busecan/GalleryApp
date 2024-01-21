package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private TextView titleTextViewGallery;
    private Spinner spinnerLabels;
    private RecyclerView recyclerViewPhotos;

    private List<Label> labels = new ArrayList<>();  // labels listesi initialize edildi
    private List<Photo> photos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        titleTextViewGallery = view.findViewById(R.id.titleTextViewGallery);
        spinnerLabels = view.findViewById(R.id.spinnerLabels);
        recyclerViewPhotos = view.findViewById(R.id.recyclerViewPhotos);

        titleTextViewGallery.setText("Galeri");

        spinnerLabels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Seçilen öğeyi logla
                Label selectedLabel = labels.get(position);
                Log.d("SpinnerSelection", "Selected Label: " + selectedLabel.getName());

                // Burada başka bir işlem yapabilirsiniz, örneğin seçilen etiketle ilgili fotoğrafları güncelleyebilirsiniz.
                // updatePhotos(selectedLabel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Bir şey seçilmediğinde logla
                Log.d("SpinnerSelection", "No Label Selected");
            }
        });

        return view;
    }
    // Veri geldiğinde etiketleri ve fotoğrafları ayarlamak için bir yöntem ekleyin
    public void setLabelsAndPhotos(List<Label> labels, List<Photo> photos) {
        this.labels = labels;
        this.photos = photos;
        updateUI();
    }

    private void updateUI() {
        // Spinner'ı etiketlerle ayarlayın
        ArrayAdapter<Label> labelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, labels);
        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabels.setAdapter(labelAdapter);

        // RecyclerView'ı fotoğraflarla ayarlayın
        PhotoAdapter photoAdapter = new PhotoAdapter(getContext(), photos);
        recyclerViewPhotos.setAdapter(photoAdapter);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter güncellemesi
        labelAdapter.notifyDataSetChanged();

        // Veriyi logla
        Log.d("FirebaseData", "Labels size: " + labels.size());
        for (Label label : labels) {
            Log.d("FirebaseData", "Label: " + label.getName());
        }
    }
}

