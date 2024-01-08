package com.example.docapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordEmailActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnNext;
    private String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_forget_password);

        inputEmail = findViewById(R.id.inputemail);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                generatedOTP = generateOTP();

                sendOTPEmail(email, generatedOTP);

                Intent intent = new Intent(ForgotPasswordEmailActivity.this, ForgotPasswordOTPActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("otp", generatedOTP);
                startActivity(intent);
            }
        });
    }

    private String generateOTP() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOTPEmail(String email, String otp) {
        // TODO: Replace with your email and password
        final String username = "awstested@gmail.com";
        final String password = "fnti mhno ihet hzti";

        // Set properties for sending email using Gmail SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with an authenticator
        final Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Execute the AsyncTask to send the email in the background
        new SendEmailTask(email, otp, username, password, session).execute();
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private String email;
        private String otp;
        private String username;
        private String password;
        private Session session;

        SendEmailTask(String email, String otp, String username, String password, Session session) {
            this.email = email;
            this.otp = otp;
            this.username = username;
            this.password = password;
            this.session = session;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Create a MimeMessage object
                Message message = new MimeMessage(session);
                // Set the sender address
                message.setFrom(new InternetAddress(username));
                // Set the recipient address
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                // Set the subject
                message.setSubject("Password Reset OTP");
                // Set the message body
                message.setText("Your OTP for password reset is: " + otp);

                // Send the email
                Transport.send(message);

                return true;
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("EmailSending", "Error sending email", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Display success message
                Toast.makeText(ForgotPasswordEmailActivity.this, "OTP sent to your email", Toast.LENGTH_SHORT).show();
                // Log success
                handleEmailResult(true);
            } else {
                // Display error message
                Toast.makeText(ForgotPasswordEmailActivity.this, "Error sending OTP. Check your email configuration.", Toast.LENGTH_SHORT).show();
                // Log failure
                handleEmailResult(false);
            }
        }

        private void handleEmailResult(boolean success) {
            // You can add additional logic here based on the success or failure of email sending
            if (success) {
                // Email sent successfully, log success
                Log.d("EmailSending", "Email sent successfully");
                // You can perform any additional actions here
                // For example, log the success, update UI, etc.
            } else {
                // Email sending failed, log failure
                Log.e("EmailSending", "Error sending email");
                // You can perform any error handling here
                // For example, log the failure, show an error message, etc.
            }
        }
    }
}
