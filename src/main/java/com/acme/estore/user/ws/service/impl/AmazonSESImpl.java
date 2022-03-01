package com.acme.estore.user.ws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.service.AmazonSES;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

@Service
public class AmazonSESImpl implements AmazonSES {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * @Autowired private MessageSource messageSource;
	 */
	
    private ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();	
	
	@Override
	public void verifyEmail(UserDTO userDTO) {	
		
		LOGGER.debug("Verifying email address");
				
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.build();
 
		String htmlBodyWithToken = messageSource.getMessage("email.body.registeration.html", new Object[0], null); // HTMLBODY.replace("$tokenValue", userDTO.getEmailVerificationToken());
		String textBodyWithToken = messageSource.getMessage("email.body.registeration.text", new Object[0], null); //TEXTBODY.replace("$tokenValue", userDTO.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new com.amazonaws.services.simpleemail.model.Destination().withToAddresses(userDTO.getEmail()))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
				.withBody(new Body().withHtml(new com.amazonaws.services.simpleemail.model.Content().withCharset("UTF-8").withData(htmlBodyWithToken))
				.withText(new com.amazonaws.services.simpleemail.model.Content().withCharset("UTF-8").withData(textBodyWithToken)))
				.withSubject(new com.amazonaws.services.simpleemail.model.Content().withCharset("UTF-8")
				.withData(messageSource.getMessage("email.subject.registration", new Object[0], null))))
				.withSource(messageSource.getMessage("email.from.registeration", new Object[0], null));
		
		client.sendEmail(request);

		LOGGER.debug("Successfully verified email address and sent to recipient");
	}

	@Override
	public boolean sendPasswordResetRequest(String firstName, String email, String token) {
	      boolean returnValue = false;
	      
	      messageSource.setBasename("messages");
	 	 
	      AmazonSimpleEmailService client = 
	          AmazonSimpleEmailServiceClientBuilder.standard()
	            .withRegion(Regions.US_EAST_1).build();
	      
	      
	      String htmlBodyWithToken = messageSource.getMessage("email.body.passwordreset.html", new Object[0], null); //PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
	             //htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
	        
	      String textBodyWithToken = messageSource.getMessage("email.body.passwordreset.text", new Object[0], null); //PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
	             //textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
	      
	      
	      SendEmailRequest request = new SendEmailRequest()
	          .withDestination(new com.amazonaws.services.simpleemail.model.Destination().withToAddresses( email ) )
	          .withMessage(new com.amazonaws.services.simpleemail.model.Message()
	          .withBody(new Body()
	          .withHtml(new com.amazonaws.services.simpleemail.model.Content()
	          .withCharset("UTF-8").withData(htmlBodyWithToken))
	          .withText(new com.amazonaws.services.simpleemail.model.Content()
	          .withCharset("UTF-8").withData(textBodyWithToken)))
	          .withSubject(new com.amazonaws.services.simpleemail.model.Content()
	          .withCharset("UTF-8")
	          .withData(messageSource.getMessage("email.subject.passwordreset", new Object[0], null))))
	          .withSource("email.from.passwordreset");

	      SendEmailResult result = client.sendEmail(request); 
	      if(result != null && (result.getMessageId()!=null && !result.getMessageId().isEmpty()))
	      {
	          returnValue = true;
	      }
	      
	      return returnValue;
	}

}
