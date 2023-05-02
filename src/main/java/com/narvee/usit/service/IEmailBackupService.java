package com.narvee.usit.service;

import java.util.List;
import com.narvee.usit.entity.Email;

public interface IEmailBackupService {

	public void saveBackup(Email email);

	public void saveBackup(String host, String port, String username, String password);

	public List<Email> getAllEmails();

	public Email getEmail(long id);
	
	public List<Email> findEmailByFilter(String keyword);
	
	//public  void check(String host, String user, String password) ;
}
