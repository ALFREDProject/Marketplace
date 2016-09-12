package com.tempos21.market.client.harvest;

import com.tempos21.market.client.http.T21HttpClientWithSSL;

import org.apache.http.impl.client.BasicCookieStore;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/***
 * HarvestPetition is used to store all the needed data to perform a harvest.
 * The JsonHarvester class will take all the information to execute the petition
 * from here.
 *
 * @author Sergi Martinez
 */
public class HarvestPetition {
    private String url = "";

    ;
    private T21HttpClientWithSSL httpClient;
    private HashMap<String, String> paramsMap; // a map containing the
    // parameters
    private HashMap<String, String> headersMap; // a map containing the headers
    private HashMap<String, String> entityMap;
    private HashMap<String, String> restMap;
    private PetitionType preferred = PetitionType.GET;
    private BasicCookieStore cookieStore;
    /**
     * Constructor, initializes the petition
     *
     * @param url        The url to be called for the harvesting
     * @param httpClient The httpClient to be used
     */
    public HarvestPetition(String url, T21HttpClientWithSSL httpClient) {

        //Store values
        this.setUrl(url);
        this.setHttpClient(httpClient);

        //Initializing Maps
        paramsMap = new HashMap<String, String>();
        headersMap = new HashMap<String, String>();
        entityMap = new HashMap<String, String>();
        restMap = new LinkedHashMap<String, String>();
    }

    /**
     * This method gets the full url, including the REST and QueryParams values
     *
     * @return the full url
     */
    public String getUrl() {
        StringBuffer newUrl = new StringBuffer(url);
        if (!restMap.isEmpty()) {
            if (!url.substring(url.length()).equals("/"))
                newUrl.append("/");
            for (Entry<String, String> param : restMap.entrySet()) {
                String encoded = URLEncoder.encode(param.getValue());
                encoded = encoded.replace("+", "%20");
                newUrl.append(encoded).append("/");
            }
        }
        if (newUrl.length() > 0)
            return newUrl.toString();
        else
            return "";
    }

    /**
     * Establishes de base Url
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public T21HttpClientWithSSL getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(T21HttpClientWithSSL httpClient2) {
        this.httpClient = httpClient2;
        if (cookieStore != null)
            this.httpClient.setCookieStore(cookieStore);
    }

    /**
     * Adds a new parameter for the http request.
     *
     * @param name  name of the parameter
     * @param value value for the parameter
     */
    public void addParameter(String name, String value) {
        paramsMap.put(name, value);
    }

    /**
     * Adds a new header variable for the http request.
     *
     * @param name  name of the header variable
     * @param value value for the header variable
     */
    public void addHeader(String name, String value) {
        headersMap.put(name, value);
    }

    /**
     * Adds a new header variable for the http request.
     *
     * @param name  name of the header variable
     * @param value value for the header variable
     */
    public void addEntity(String name, String value) {
        entityMap.put(name, value);
    }

    public void addRest(String name, String value) {
        restMap.put(name, value);
    }

    public HashMap<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(HashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public HashMap<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(HashMap<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public HashMap<String, String> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(HashMap<String, String> entityMap) {
        this.entityMap = entityMap;
    }

    public HashMap<String, String> getRestMap() {
        return restMap;
    }

    public void setRestMap(HashMap<String, String> restMap) {
        this.restMap = restMap;
    }

    /**
     * Gets the preferred execution method.
     *
     * @return the preferred execution method
     */
    public PetitionType getPreferred() {
        return preferred;
    }

    /**
     * Sets the preferred execution method.
     *
     * @param preferred The execution method to prefer
     */
    public void setPreferred(PetitionType preferred) {
        this.preferred = preferred;
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
        if (this.httpClient != null)
            httpClient.setCookieStore(cookieStore);
    }

    // Type of petition
    public static enum PetitionType {
        GET, POST
    }

}
