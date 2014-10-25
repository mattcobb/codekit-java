package com.att.api.speech.service;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.att.api.oauth.OAuthToken;
import com.att.api.rest.APIResponse;
import com.att.api.rest.RESTClient;
import com.att.api.rest.RESTException;
import com.att.api.service.APIService;
import com.att.api.speech.model.SpeechResponse;

/**
 * Class that handles communication with the speech server.
 *
 */
public class SpeechCustomService extends APIService {

    /**
     * Setup a service for handling custom speech requests
     *
     * @param fqdn The server processing the request
     * @param token The token being used to authenticate
     */
    public SpeechCustomService(String fqdn, OAuthToken token) {
        super(fqdn, token);
    }

    /**
     * Request the API to convert audio to text
     *
     * @param audio audio to convert to to text.
     * @param grammar grammar file 
     * @param dictionary dictionary file
     *
     * @return response in the form of a SpeechResponse object
     * @throws RESTException
     */
    public SpeechResponse speechToText(File audio, File grammar, 
            File dictionary) throws RESTException {
        return speechToText(audio, grammar, dictionary, null);
    }

    /**
     * Request the API to convert audio to text
     *
     * @param audio audio to convert to to text.
     * @param grammar grammar file 
     * @param dictionary dictionary file
     * @param speechContext modify the speech context of the request
     *
     * @return response in the form of a SpeechResponse object
     * @throws RESTException
     */
    public SpeechResponse speechToText(File audio, File grammar,
            File dictionary, String speechContext) throws RESTException {
        return speechToText(audio, grammar, dictionary, speechContext, null);
    }

    /**
     * Request the API to convert audio to text
     *
     * @param attachments audio files to convert to to text.
     * @param grammar grammar file 
     * @param xArg add additional xarg values
     *
     * @return response in the form of a SpeechResponse object
     * @throws RESTException
     */
    public SpeechResponse sendRequest(String [] attachments, 
            String speechContext, String xArg) throws Exception {
        return parseSuccess(sendRequestAndReturnRawJson(attachments, speechContext, xArg, "en-US"));
    }

    public String sendRequestAndReturnRawJson(String [] attachments, 
            String speechContext, String xArg, String isoLanguage) throws Exception {
        final String endpoint = getFQDN() + "/speech/v3/speechToTextCustom";

        RESTClient restClient = new RESTClient(endpoint)
            .addAuthorizationHeader(getToken())
            .addHeader("Accept", "application/json")
            .addHeader("X-SpeechContext", speechContext)
            .addHeader("Content-Language", isoLanguage);

        if (xArg != null && !xArg.equals("")) {
            restClient.setHeader("X-Arg", xArg);
        }

        String subType = "x-srgs-audio";
        String[] bodyNameAttribute = { 
            "x-dictionary", 
            "x-grammar", 
            "x-voice" 
        };

        APIResponse apiResponse = restClient.httpPost(attachments, subType, bodyNameAttribute);
        return apiResponse.getResponseBody();
    }
}
