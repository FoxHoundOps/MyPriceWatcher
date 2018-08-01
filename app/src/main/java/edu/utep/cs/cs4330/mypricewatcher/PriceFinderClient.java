package edu.utep.cs.cs4330.mypricewatcher;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  A subclass of PriceFinder that overrides fetchPrice(). This PriceFinder subclass will actually
 *  act as a client to a Web service that, given a Web URL, returns the price of the item found
 *  at the specified Web URL.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class PriceFinderClient  extends PriceFinder{
    private final String USER = "dgnajera";
    private final String PIN = "cs.utep.edu";
    private final String WS_URL = "http://142.93.17.10/query";
    private final int RESPONSE_OK = 200;
    private String response;

    /**
     * Override in order to actually query a Web service for the item price information.
     *
     * @param url                       The Web url containing an item
     * @return                          The price of the item at 'url'
     * @throws PriceNotFoundException   Throw in case price is not found
     */
    @Override
    public double fetchPrice(String url) throws PriceNotFoundException{
        try {
            return Double.parseDouble(query(USER, PIN, url));
        }
        catch (java.lang.NumberFormatException e) {
            throw new PriceNotFoundException();
        }
    }

    /**
     * Build and send the query to the web server based on the parameters passed in, and
     * return the response.
     *
     * @param user  Web service user that is generating the request
     * @param pin   Web service user pin
     * @param site  The site to send to the Web service, to have it return the price
     *
     * @return      Web service response (either "error" or the price of the item on the 'site'
     */
    private String query(String user, String pin, String site) {
        try {
            String query = WS_URL + "/" + user + "/" + pin + "/" + site;
            URL url = new URL(query);
            Log.d("PriceFinderClient.query", "Making query: " + url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == RESPONSE_OK) {
                response = readAll(con.getInputStream());
                con.disconnect();
                Log.d("PriceFinderClient.query", "Success! Response = " + response);
                return response;
            }
            else {
                con.disconnect();
                return "Error";
            }
        }
        catch (Exception e) {
            return "Error";
        }
    }

    /**
     * Given an InputStream, read, build, and return the response.
     *
     * @param stream        The InputStream from which to read from
     * @return              The response read from the stream
     * @throws Exception    Throw Exception
     */
    private String readAll(InputStream stream) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        return response.toString();
    }
}
