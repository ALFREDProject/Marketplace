package com.tempos21.market.client.harvest;

import com.tempos21.market.client.http.T21HttpClientWithSSL;
import com.tempos21.market.util.TLog;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * This class is responsible for obtaining a JSON result from an url
 * <p>
 * <p>
 * Usage:
 * </p>
 * <ol>
 * <li>Instantiate the class with the right HarvetsPetition</li>
 * <li>Add the corresponding parameters with the {@link #addParameter
 * addParameter method}</li>
 * <li>Add the corresponding header values with the {@link #addHeader addHeader
 * method}</li>
 * <li>Execute the request using {@link #executeGetRequest()} or
 * {@link #executePostRequest()}. You can also execute the
 * {@link #executePreferredRequest()} and it will run the method stablished with
 * the {@link #setPreferred(petitionType)} method</li>
 * </ol>
 *
 * @author Sergi Martinez
 */
public class JsonHarvester {

    private HarvestPetition petition;

    /**
     * Instantiates a new json harvester.
     *
     * @param petition a HarvesPetition object containing the url to execute and the
     *                 id of the petition
     */
    public JsonHarvester(HarvestPetition petition) {
        this.petition = petition;

    }

    /**
     * return the parameters collection.
     *
     * @param httpRequest the get/post object to get initial Parameters
     * @return the httpParams collection with all parameters filled up
     */
    public HttpParams generateParams(HttpRequest httpRequest) {
        Iterator<Map.Entry<String, String>> it = petition.getParamsMap()
                .entrySet().iterator();
        HttpParams params = httpRequest.getParams();
        while (it.hasNext()) {
            Map.Entry<String, String> e = (Entry<String, String>) it.next();
            String key = e.getKey();// URLEncoder.encode(e.getKey());
            String value = e.getValue();// URLEncoder.encode(e.getValue());
            params.setParameter(key, value);
        }
        return params;
    }

    /**
     * return the parameters collection.
     *
     * @param httpRequest the get/post object to get initial Parameters
     * @return the httpParams collection with all parameters filled up
     */
    public String generateQueryParams() {
        if (petition.getParamsMap().entrySet().size() == 0)
            return "";
        else {
            boolean first = true;
            StringBuffer queryParams = new StringBuffer("?");
            Iterator<Map.Entry<String, String>> it = petition.getParamsMap()
                    .entrySet().iterator();
            // just iterate the map and fill the httpParams with its content
            while (it.hasNext()) {
                if (!first)
                    queryParams.append("&");
                else
                    first = false;
                Map.Entry<String, String> e = (Entry<String, String>) it.next();
                String key = URLEncoder.encode(e.getKey());
                String value = URLEncoder.encode(e.getValue());
                queryParams.append(String.format("%s=%s", key, value));

            }
            return queryParams.toString();
        }
    }

    /**
     * return the headers variables array.
     *
     * @param httpRequest the get/post object to get initial headers
     * @return an array of Header objects
     */
    public Header[] generateHeaders(HttpMessage httpRequest) {
        Iterator<Map.Entry<String, String>> it = petition.getHeadersMap()
                .entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = (Entry<String, String>) it.next();
            String key = e.getKey();// URLEncoder.encode(e.getKey());
            String value = e.getValue();// URLEncoder.encode(e.getValue());
            httpRequest.addHeader(key, value);
        }
        return httpRequest.getAllHeaders();

    }

    /**
     * return the entities array.
     *
     * @param httpRequest the get/post object to get initial headers
     * @return an Entitiy object with all the parameters
     */
    public HttpEntity generateEntity(HttpEntityEnclosingRequestBase httpRequest) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = petition.getEntityMap()
                .entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = (Entry<String, String>) it.next();
            String key = URLEncoder.encode(e.getKey());
            String value;
            value = e.getValue();
            params.add(new BasicNameValuePair(key, value));
        }
        UrlEncodedFormEntity ent = null;
        try {
            ent = new UrlEncodedFormEntity(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ent;

    }

    /**
     * Extracts the JSON object from a post or get execution result.
     *
     * @param httpResult http result obtained from a set or post call
     * @return the json element from request. Null if not valid.
     * @throws IOException
     * @throws ParseException
     */
    public String getJsonFromRequest(HttpResponse httpResult)
            throws ParseException, IOException {
        String stringResult = null;
        stringResult = EntityUtils.toString(httpResult.getEntity(), "UTF-8");
        return stringResult;
    }

    /**
     * Executes the request as a get and returns the result including a JSON
     * object
     *
     * @return the HarvesResult of the petition
     * @throws IOException
     * @throws ParseException
     */
    public HarvestResult executeGetRequest() throws ParseException, IOException {
        final HttpGet get = new HttpGet(petition.getUrl()
                + generateQueryParams());

        HttpResponse httpResult = null;
        T21HttpClientWithSSL httpClient = petition.getHttpClient();

        get.setHeaders(generateHeaders(get));
        int statusCode = HttpStatus.SC_NOT_FOUND;
        logPetition(get);
        httpResult = httpClient.execute(get);
        statusCode = httpResult.getStatusLine().getStatusCode();
        TLog.i("Status code = " + statusCode);
        String answer = getJsonFromRequest(httpResult);
        HarvestResult result = generateResult(answer, statusCode, httpClient);
        return result;
    }

    /**
     * Executes the request as a post and returns the result in the form of a
     * JSON object
     *
     * @return the HarvesResult of the petition
     * @throws IOException
     * @throws ParseException
     */
    public HarvestResult executePostRequest() throws ParseException,
            IOException {
        HttpPost post = new HttpPost(petition.getUrl() + generateQueryParams());

        HttpResponse httpResult = null;
        T21HttpClientWithSSL httpClient = petition.getHttpClient();

        post.setEntity(generateEntity(post));
        post.setHeaders(generateHeaders(post));


        int statusCode = HttpStatus.SC_NOT_FOUND;
        logPetition(post);

        httpResult = httpClient.execute(post);
        statusCode = httpResult.getStatusLine().getStatusCode();

        String answer = getJsonFromRequest(httpResult);
        HarvestResult result = generateResult(answer, statusCode, httpClient);
        return result;

    }

    /**
     * Executes the request using the preferred method stablished with
     * {@link #setPreferred(petitionType)}
     *
     * @return the json element
     * @throws IOException
     * @throws ParseException
     */
    public HarvestResult executePreferredRequest() {
        HarvestResult result = null;
        try {
            if (petition.getPreferred() == HarvestPetition.PetitionType.GET)
                result = executeGetRequest();
            if (petition.getPreferred() == HarvestPetition.PetitionType.POST)
                result = executePostRequest();
        } catch (ParseException e) {
            result = new HarvestResult(null);
            result.setReason(HarvestResult.REASON_PARSEERROR);
        } catch (IOException e) {
            result = new HarvestResult(null);
            result.setReason(HarvestResult.REASON_IOERROR);
        }

        return result;

    }

    /**
     * Generates a HarvesResult object with the provided JSON result and the
     * HttpStatusCode.
     *
     * @param jsonAnswer the JSON Element obtained
     * @param statusCode the HTTP status code of the petition
     * @param httpClient
     * @return the HarvesResult of the petition with the Id from the
     * HarvesPetition
     */
    public HarvestResult generateResult(String jsonAnswer, int statusCode, T21HttpClientWithSSL httpClient) {
        HarvestResult result = new HarvestResult(jsonAnswer);
        result.setHttpStatus(statusCode);
        result.setHttpClient(httpClient);

        TLog.d("[HARVESTER:RESULT] " + this.toString());
        TLog.d("[HARVESTER:RESULT] Status Code=" + statusCode);
        TLog.d("[HARVESTER:RESULT] jsonAnswer=" + result.getJsonAnswer().toString());
        TLog.d("[HARVESTER:RESULT] HttpStatus=" + result.getHttpStatus());
        TLog.d("[HARVESTER:RESULT] Cookies="
                + result.getHttpClient().getCookieStore().toString());

        return result;
    }


    private void logPetition(HttpRequestBase httpPetition) {
        TLog.d("[HARVESTER:PETITION] " + this.toString());
        TLog.d("[HARVESTER:PETITION] Type= " + httpPetition.getMethod());
        TLog.d("[HARVESTER:PETITION] Url=" + httpPetition.getURI().toString());
        if (httpPetition instanceof HttpPost) {
            TLog.d("[HARVESTER:PETITION] Body= " + petition.getEntityMap().toString());
        }
        TLog.d("[HARVESTER:PETITION] Header=" + petition.getHeadersMap().toString());
        TLog.d("[HARVESTER:PETITION] Cookies=" + petition.getHttpClient().getCookieStore().toString());
    }

}
