package com.narvee.usit.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.narvee.usit.entity.Email;
import com.narvee.usit.repository.IEmailRepository;
import com.narvee.usit.service.IEmailBackupService;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//commented for mail error
@Service
public class EmailBackupServiceimpl implements IEmailBackupService {

	@Autowired
	private IEmailRepository repo;

	// @Autowired(required = true)
	//MimeMessage mm;

	private String saveDirectory = "E:/stores";

	@Override
	public void saveBackup(Email email) {

	}

	@Override
	public void saveBackup(String host, String port, String userName, String password) {
		Properties properties = new Properties();
		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);
		Email e = null;
		// SSL setting
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("pop3");
			store.connect(userName, password);

			// opens the INBOX folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				e = new Email();
				Message message = arrayMessages[i];

				// Getting Emailed_From
				Address[] fromAdd = message.getFrom();
				String from = fromAdd[0].toString();
				if (from.contains("<")) {
					String value = from.substring(from.indexOf("<") + 1, from.indexOf(">", from.indexOf(">")));
					// System.out.println("From======>" + value);
					e.setFrom(value);

				} else {
					e.setFrom(from);
				}
				// Getting Emailed_To
				Address[] to = message.getRecipients(Message.RecipientType.TO);
				String to1 = to[0].toString();
				if (to1.contains("<")) {
					String value = to1.substring(to1.indexOf("<") + 1, to1.indexOf(">", to1.indexOf(">")));
					// System.out.println("To=======> " + value);
					e.setTo(value);
				} else {
					// System.out.println("To=======> " + to1);
					e.setTo(to1);
				}
				// Getting Emailed_CC
				Address[] cc = message.getRecipients(Message.RecipientType.CC);
				String finalCC = "";
				try {
					for (int j = 0; j < cc.length; j++) {
						String fromCC = cc[j].toString();
						if (fromCC.contains("<")) {
							String value = fromCC.substring(fromCC.indexOf("<") + 1,
									fromCC.indexOf(">", fromCC.indexOf(">")));
							value = value.concat(", ");
							finalCC = finalCC.concat(value);
						} else {
							fromCC = fromCC.concat(",");
							finalCC = finalCC.concat(fromCC);
						}
					}
					finalCC = finalCC.substring(0, finalCC.length() - 1);
					// System.out.println("CC=====>" + finalCC);
				} catch (NullPointerException e1) {
				}
				String subject = message.getSubject();
				
				String s = "I can has cheezeburger?";
				String s2 = "I can has cheezeburger?";
				String s3 = "I can has cheezeburger?";
				String s4 = "I can has cheezeburger?";
				boolean hasCheese = s.contains("cheeze");
				
				if(subject!=null) {
				if(subject.contains("Java")) {
					e.setSubjectcategory("Java Profiles");
				}
				else if(subject.contains("Dot net")) {
					e.setSubjectcategory("Dot net Profiles");
				}
				else if(subject.contains("resume email")) {
					e.setSubjectcategory("resume email");
				}
				else {
					e.setSubjectcategory("Others");
				}
				}
				else {
					e.setSubjectcategory("Others==");

				}
				
				String sentDate = message.getSentDate().toString();
				String contentType = message.getContentType();
				String messageContent = "";
				//System.out.println(subject + "=============================================" + contentType+" ============================== "+messageContent);
				// store attachment file name, separated by comma
				String attachFiles = "";
				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						
						if (part.isMimeType("text/plain")) {
							  //System.out.println("plain text");
							} else if (part.isMimeType("multipart/*")) {
								MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
								 messageContent = getTextFromMimeMultipart(mimeMultipart);
								 System.out.println("message content after else if=====>"+messageContent);
							} else if (part.isMimeType("message/rfc822")) {
								//System.out.println("message/rfc822");
							} else {
							//	System.out.println("text/html content");
							}
						
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							part.saveFile(saveDirectory + File.separator + fileName);
						} 
						else if (part.isMimeType("multipart/*")) {
							MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
							 messageContent = getTextFromMimeMultipart(mimeMultipart);
							 System.out.println("message content after else if=====>"+messageContent);
						}
						else 
						{
							// this part may be the message content
							messageContent = part.getContent().toString();
							// System.out.println("message content before else if====>"+messageContent);
						}
					}
					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				} 
//				else if (contentType.contains("text/plain") || contentType.contains("text/html")
//						|| contentType.contains("multipart/mixed") || contentType.contains("application/octet-stream") ) {
//					Object content = message.getContent();
//					if (content != null) {
//						messageContent = content.toString();
//						
//						 System.out.println("message content after else if=====>"+messageContent);
//					        
//					}
//				}
				e.setSubject(subject);
				e.setCc(finalCC);
				// e.setBcc(ebcc);
				// e.setBody2(messageContent);
				e.setAttachment(attachFiles);
				e.setBody(messageContent);
				repo.save(e);
				arrayMessages[i].setFlag(Flags.Flag.DELETED, true);
			}
			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			// System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			// System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
	
	@Override
	public List<Email> getAllEmails() {

		return repo.findAll();
	}

	@Override
	public Email getEmail(long id) {

		return repo.findById(id).get();
	}

	@Override
	public List<Email> findEmailByFilter(String keyword) {
		if (keyword != null) {
			return repo.getAllEmailBasedOnFilter(keyword);
		}
		return repo.findAll();
	}

	

}
