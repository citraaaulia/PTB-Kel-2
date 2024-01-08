package com.example.docapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.docapp.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    private FirebaseFirestore firestore;

    private static final String CHANNEL_ID = "my_channel";

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

        ImageButton deleteAllButton = findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteAllNotifications();
            }
        });

        firestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            NotificationItem notificationItem = document.toObject(NotificationItem.class);
                            notificationList.add(notificationItem);

                            // Trigger local notification
                            showNotification(notificationItem.getJudul(), notificationItem.getSubjudul());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("NotificationActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for my notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.notification);

        notificationManager.notify(1, builder.build());
    }

    public static class NotificationItem {

        private String judul;
        private String subjudul;

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
    }

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
        }

        @Override
        public int getItemCount() {
            return notificationItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textJudulNotifikasi;
            private TextView subjudulNotifikasi;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textJudulNotifikasi = itemView.findViewById(R.id.textjudulnotifikasi);
                subjudulNotifikasi = itemView.findViewById(R.id.subjudulnotifikasi);
            }
        }

        private void deleteAllNotifications() {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            firestore.collection("users")
                    .document(userId)
                    .collection("notifications")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            notificationList.clear();
                            adapter.notifyDataSetChanged();
                            Log.d("NotificationActivity", "All notifications deleted successfully");
                        } else {
                            Log.e("NotificationActivity", "Error deleting notifications", task.getException());
                        }
                    });
        }
    }
}
