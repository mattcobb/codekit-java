package com.att.api.notification;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationChannel {
	private String channelId;
	private int maxEvents;
	
	public NotificationChannel (String channelId, int maxEvents) {
		this.setChannelId(channelId);
		this.setMaxEvents(maxEvents);
	}
	
    public static NotificationChannel valueOf(JSONObject jobj) throws JSONException {
    	JSONObject jsonChannel = jobj.getJSONObject("channel");

        return new NotificationChannel(
        	jsonChannel.getString("channelId"),
            jsonChannel.optInt("maxEventsPerNotification", 0));
    }

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getMaxEvents() {
		return maxEvents;
	}
	public void setMaxEvents(Integer maxEvents) {
		this.maxEvents = maxEvents;
	}
}
