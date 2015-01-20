/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4 */

/*
 * Copyright 2015 AT&T
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.att.api.notification;

import org.json.JSONException;
import org.json.JSONObject;

import com.att.api.oauth.OAuthToken;
import com.att.api.service.APIService;
import com.att.api.rest.*;

/**
 * @author mattcobb
 *
 */
public class NotificationService extends APIService
{
	public NotificationService(String fqdn, OAuthToken token) {
		super(fqdn, token);
	}
	
	private final String endpointBase = getFQDN() + "/notification/v1/channels";
	
	public NotificationChannel createNotificationChannel(
    		String serviceName) throws RESTException, JSONException{
		return this.createNotificationChannel(serviceName, "application/json", 1.0);
	}
	
    public NotificationChannel createNotificationChannel(
    		String serviceName,
    		String ncType,
    		double version) throws RESTException, JSONException {
    	
    	JSONObject jsonChannel = new JSONObject();
    	jsonChannel.put("serviceName", serviceName);
    	jsonChannel.put("notificationContentType", ncType);
    	jsonChannel.put("notificationVersion", version);
    	
    	JSONObject jsonPostBody = new JSONObject();
    	jsonPostBody.put("channel", jsonChannel);
    	
        final APIResponse response = new RESTClient(endpointBase)
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .addAuthorizationHeader(getToken())
            .httpPost(jsonPostBody.toString());

        try {
            JSONObject jobj = new JSONObject(response.getResponseBody());

            return NotificationChannel.valueOf(jobj);
        } catch (JSONException pe) {
            throw new RESTException(pe);
        }
    }
}
