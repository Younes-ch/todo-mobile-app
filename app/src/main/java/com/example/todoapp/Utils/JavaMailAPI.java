package com.example.todoapp.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.todoapp.EmailCredentials;
import com.example.todoapp.LoadingDialog;
import com.example.todoapp.R;

import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void,Void,Void>  {


    private final Context context;

    private final String email;
    private final String subject;
    private final String body;

    private LoadingDialog loadingDialog;

    public JavaMailAPI(Context context, String email, String subject, String body) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.body = body;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new LoadingDialog(context);
        loadingDialog.startLoadingDialog();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loadingDialog.dismissDialog();
        String toastMessage = "✅ " + context.getResources().getString(R.string.email_sent_confirmation);
        Toast.makeText(context, toastMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EmailCredentials.EMAIL, EmailCredentials.PASSWORD);
                    }
                });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(EmailCredentials.EMAIL));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
            Transport.send(mimeMessage);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendMail(Context context, String email, String subject, String body) {
        JavaMailAPI javaMailAPI = new JavaMailAPI(context, email, subject, body);
        javaMailAPI.execute();
    }
}
