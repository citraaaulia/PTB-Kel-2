package com.example.docapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.rv_notifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                NotificationItem notificationItem = document.toObject(NotificationItem.class);
                                notificationItem.setId(document.getId()); // Set document ID
                                notificationList.add(notificationItem);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("NotificationActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // ...

    // Buat kelas model untuk item notifikasi
    public static class NotificationItem {

        private String judul;
        private String subjudul;

        private String id;

        // Buat konstruktor kosong untuk Firebase
        public NotificationItem() {
        }

        public NotificationItem(String judul, String subjudul) {
            this.judul = judul;
            this.subjudul = subjudul;
        }

        public String getJudul() {
            return judul;
        }

        public String getSubjudul() {
            return subjudul;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    // Buat kelas adapter untuk RecyclerView
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        private List<NotificationItem> notificationItems;

        public NotificationAdapter(List<NotificationItem> notificationItems) {
            this.notificationItems = notificationItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NotificationItem notificationItem = notificationItems.get(position);
            holder.textJudulNotifikasi.setText(notificationItem.getJudul());
            holder.subjudulNotifikasi.setText(notificationItem.getSubjudul());

            // Handle delete button click
            holder.deleteButton.setOnClickListener(v -> {
                // Get the notification ID
                String notificationId = notificationItem.getId();

                // Delete the notification from Firestore
                deleteNotificationFromFirestore(notificationId);

                // Remove the item from the list
                notificationItems.remove(position);
                // Notify the adapter about the removal
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return notificationItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textJudulNotifikasi;
            private TextView subjudulNotifikasi;
            private ImageButton deleteButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textJudulNotifikasi = itemView.findViewById(R.id.textjudulnotifikasi);
                subjudulNotifikasi = itemView.findViewById(R.id.subjudulnotifikasi);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }

        private void deleteNotificationFromFirestore(String notificationId) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            firestore.collection("users")
                    .document(userId)
                    .collection("notifications")
                    .document(notificationId)  // Specify the document ID to delete
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("NotificationActivity", "Notification deleted successfully");
                        } else {
                            Log.e("NotificationActivity", "Error deleting notification", task.getException());
                        }
                    });
        }
    }
}