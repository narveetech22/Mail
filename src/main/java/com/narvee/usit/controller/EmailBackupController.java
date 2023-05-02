package com.narvee.usit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.service.IEmailBackupService;

@RestController
@RequestMapping("/usit/mail")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailBackupController {

	@Autowired
	private IEmailBackupService service;

	@GetMapping("/mail3")
	public void save() {
		System.out.println("hello kiran");
//		String host = "smtp.narveetech.com";
//        String port = "995";
//        final String username= "saikiran@narveetech.com";  
//		final String password= "Narvee123$";//change accordingly  
//		
//		String host = "smtp.narvee.com";
//        String port = " 25";//995 110
//        final String username= "employement@narvee.com";  
//		final String password= "Narvee123$";//change accordingly  
		
		String host = "smtp.gmail.com";
      String port = " 25";//995 110
      final String username= "bsaikiran612@gmail.com";  
		final String password= "sanasree";//change accordingly 
		service.saveBackup(host, port, username, password);
	}
	
//	@RequestMapping(value = "/mail", method = RequestMethod.GET, produces = "multipart/mixed")
//	public ResponseEntity<RestAPIResponse> savemsd() {
//		
//		System.out.println("hello kiran");
//		String host = "smtp.narveetech.com";
//        String port = "995";
//        final String username= "saikiran@narveetech.com";  
//		final String password= "Narvee123$";//change accordingly  
//		//service.saveBackup(host, port, username, password);
//		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", "jjjj"),HttpStatus.OK);
//	}
	
	
	
/*	
	@RequestMapping(value = "/getMails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllMails() {
		
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", service.getAllEmails()),HttpStatus.OK);
	}
	

	@RequestMapping(value = "/getMails/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMail(@PathVariable long id)  {
		
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fethed Emails By ID",service.getEmail(id)),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getMailsK/{keyword}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMailByKeyord(@PathVariable String keyword) {
		
		return new ResponseEntity<>(new RestAPIResponse("Success", "Successfully Fetched By Keyword", service.findEmailByFilter(keyword)),HttpStatus.OK);
	}
	
	*/
}
