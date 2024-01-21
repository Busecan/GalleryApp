package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddLabelFragment extends Fragment {

    private EditText editTextLabelName;
    private EditText editTextLabelDescription;
    private Button buttonAddLabel;
    private ListView listViewLabels;

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_label, container, false);

        editTextLabelName = view.findViewById(R.id.editTextLabelName);
        editTextLabelDescription = view.findViewById(R.id.editTextLabelDescription);
        buttonAddLabel = view.findViewById(R.id.buttonAddLabel);
        listViewLabels = view.findViewById(R.id.listViewLabels);

        databaseReference = FirebaseDatabase.getInstance().getReference("labels");

        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLabel();
            }
        });

        loadLabels();

        return view;
    }

    private void addLabel() {
        String labelName = editTextLabelName.getText().toString().trim();
        String labelDescription = editTextLabelDescription.getText().toString().trim();

        if (!labelName.isEmpty()) {
            String labelId = databaseReference.push().getKey();
            Label label = new Label(labelId, labelName, labelDescription);
            databaseReference.child(labelId).setValue(label);

            // Temizle
            editTextLabelName.setText("");
            editTextLabelDescription.setText("");
        }
    }

    private void loadLabels() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Label> labels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    labels.add(label);
                }

                // ListView'e etiketleri yükle
                ArrayAdapter<Label> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, labels);
                listViewLabels.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumu
            }
        });
    }
}