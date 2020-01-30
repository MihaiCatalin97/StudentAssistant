package com.lonn.studentassistant.firebaselayer.services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.lonn.studentassistant.firebaselayer.R;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static javax.mail.Message.RecipientType.TO;

@Accessors(chain = true)
public class EmailService {
	private static EmailService instance;
	private String emailUsername, emailPassword;
	@Getter
	@Setter
	private String host;
	@Getter
	@Setter
	private Integer port;
	@Getter
	@Setter
	private boolean startTls = true;
	@Getter
	@Setter
	private boolean enableSelfSigned = true;
	private javax.mail.Authenticator auth;
	private InternetAddress from;
	@Getter
	@Setter
	private InternetAddress to = null;
	@Getter
	@Setter
	private String subject = null;
	@Getter
	@Setter
	private String txtBody = null;

	private EmailService(Context context) {
		emailUsername = context.getResources().getString(R.string.email_username);
		emailPassword = context.getResources().getString(R.string.email_password);
		host = context.getResources().getString(R.string.email_host);
		port = context.getResources().getInteger(R.integer.email_port);
		from = parseAddress(emailUsername);

		auth = new EmailService.Authenticator(emailUsername, emailPassword);
	}

	public static EmailService getInstance(Context context) {
		if (instance == null) {
			instance = new EmailService(context);
		}

		return instance;
	}

	public static InternetAddress parseAddress(final String address) {
		try {
			return new InternetAddress(address);
		}
		catch (AddressException exception) {
			return null;
		}
	}

	public void send(boolean async, @Nullable Callback callback) {
		if (async) {
			AsyncTask.execute(() -> send(callback));
		}
		else {
			send(callback);
		}
	}

	public void send(@Nullable Callback callback) {
		try {
			Properties props = new Properties();
			Session session;

			props.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(props, auth);

			Message msg = new MimeMessage(session);

			msg.setFrom(from);
			msg.setSentDate(new Date());
			msg.setRecipient(TO, getTo());

			msg.addHeader("Precedence", "bulk");
			msg.setSubject(getSubject());

			Multipart mp = new MimeMultipart();

			MimeBodyPart bodyMsg = new MimeBodyPart();
			bodyMsg.setText(getTxtBody(), "iso-8859-1");

			mp.addBodyPart(bodyMsg);

			msg.setContent(mp);

			props.put("mail.smtp.host", getHost());
			props.put("mail.smtp.port", getPort());

			props.put("mail.user", from);

			if (isStartTls()) {
				props.put("mail.smtp.starttls.enable", "true");
			}

			if (isEnableSelfSigned()) {
				props.put("mail.smtp.ssl.trust", getHost());
			}

			props.put("mail.mime.charset", "UTF-8");

			Transport.send(msg);

			Callback.handle(callback, null);
		}
		catch (Exception e) {
			Callback.handle(callback, e);
		}
	}

	public EmailService reset() {
		this.to = null;
		this.subject = null;
		this.txtBody = null;

		return this;
	}

	public interface Callback {
		static void handle(@Nullable Callback c, Exception e) {
			if (c != null) {
				new Handler(Looper.getMainLooper()).post(() -> c.done(e));
			}
		}

		void done(@Nullable Exception e);
	}

	public static class Authenticator extends javax.mail.Authenticator {
		private final String username;
		private final String password;

		public Authenticator(final String username, final String password) {
			this.username = username;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}
}
