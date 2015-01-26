package com.att.api.notification;

import org.junit.Test;

import com.att.api.oauth.OAuthToken;
import com.att.api.rest.APIRequestError;
import com.att.api.rest.RESTConfig;
import com.att.api.rest.RESTException;

import static org.junit.Assert.assertTrue;
//import com.att.api.notification.*;

public class NotificationServiceTest {

    // set these settings to use this integration test
    // TODO: move to config file
    private final String fqdn = "https://api-uat.mars.bf.sl.attcompute.com";
    private final String accessToken = "BF-ACSI~1~20150126202708~k3oCWi6aLHHsBWu8eS8M9mbPPPQ79iJ8";
    //private final String consentToken = "";

    @Test
    public void send() throws RESTException {
    	try {
	        RESTConfig.setDefaultProxy("one.proxy.att.com", 8080);
	    	RESTConfig.setDefaultTrustAllCerts(true);
	
	        if (accessToken == null || accessToken.equals("")) {
	            final String msg = "Notification integration test settings not set; skipping.";
	            System.out.println(msg);
	            return; 
	        }
	
	        final long noExpiry = OAuthToken.NO_EXPIRATION;
	        OAuthToken token = new OAuthToken(accessToken, noExpiry, "");
	
	        NotificationService notificationSrvc = new NotificationService(fqdn, token);
	
	        NotificationChannel channel=null;
	        try {
	            channel = notificationSrvc.createNotificationChannel("MIM");
	        } catch (RESTException createChanEx) {
	        	// If a channel already exists for this key, delete it and continue the test
	        	APIRequestError reqErr = new APIRequestError(createChanEx.getErrorMessage());
	        	if(reqErr.isNotificationChannelAlreadyExistsError() && reqErr.getNotificationChannelId()!=null) {
	        		notificationSrvc.deleteNotificationChannel(reqErr.getNotificationChannelId());
	        		channel = notificationSrvc.createNotificationChannel("MIM");
	        	} else {
	        		throw createChanEx;
	        	}
	        }
        
	        System.out.println(channel.toString());
	        assertTrue(channel.getChannelId() != null);
	        
	        NotificationChannel channelDetails = notificationSrvc.getNotificationChannel(channel.getChannelId());
	        assert(channelDetails.getMaxEvents() > 0);
	        assert(channelDetails.getChannelId() == channel.getChannelId());
	        assert(channelDetails.getChannelType().equalsIgnoreCase("http_callback"));
	        assert(channelDetails.getServiceName() == "MIM");
	        
	        // Clean up the channel so the test runs successfully next time
	        notificationSrvc.deleteNotificationChannel(channel.getChannelId());
    	} catch (Exception testEx) {
    		System.out.println(testEx.toString());
    		assertTrue(testEx == null);
    	}
    }
}
