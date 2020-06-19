package program.location;

import program.Confidential;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Finds the country that a given place is in.
 */

public class CountryFromPlace {

    /** the API key for the Google Geocaching API **/
    private static final String API_KEY = Confidential.CountryFromPlace_API_KEY;


    /** the main method for the class **/
    public static String getCountryCode(String place) {
        if ((place == null) || isCode(place)) {
            return place;
        }
        if (isCountryName(place)) {
            return nameToCode(place);
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + place.replace(" ", "+") + "&key=" + API_KEY;
        String fullResponse = readWebpage(url);
        if ((fullResponse == null) || (fullResponse.contains("\"status\" : \"ZERO_RESULTS\""))) {
            return null;
        }
        return codeFromResponse(fullResponse);
    }

    /** reads a webpage from a given url to a string **/
    private static String readWebpage(String url) {
        String output = "";
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                output += inputLine  + "\n";
            in.close();
        } catch (Exception e) {
            System.err.println("Was unable to read Geocaching data from " + url);
            return null;
        }
        return output;
    }

    /** finds and returns the country code from the API response **/
    private static String codeFromResponse(String fullResponse) {
        String relevantLine = findRelevantLine(fullResponse);
        final String before = "\"short_name\" : \"";
        final String after = "\",";
        if (!(relevantLine.contains(before) && relevantLine.contains(after))) {
            throw new IllegalArgumentException("Was unable to get the country code from this response:\n" + relevantLine);
        }

        String output = relevantLine.substring(relevantLine.indexOf(before) + before.length());
        output = output.substring(0, output.indexOf(after));

        assert isCode(output);
        return output;
    }

    /** finds and returns the line containing the country code **/
    private static String findRelevantLine(String fullResponse) {
        String[] responseArray = fullResponse.split("\n");
        for (int i=0; i<responseArray.length; i++) {
            if (responseArray[i].contains("\"country\"")) {
                return responseArray[i-1];
            }
        }
        throw new IllegalArgumentException("Was unable to find the target line in the response:\n" + fullResponse);
    }

    /** an list of all countries in upper case **/
    private static ArrayList<String> countriesUC = mkCountries();
    /** used to construct the countriesUC list **/
    private static ArrayList<String> mkCountries() {
        ArrayList<String> output = new ArrayList<String>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            output.add(l.getDisplayCountry().toUpperCase());
        }
        return output;
    }

    /** checks if the given place is a country code **/
    private static boolean isCode(String place) {
        Locale l = new Locale("", place);
        return !l.getDisplayCountry().equalsIgnoreCase(place);
    }

    /** checks if a given place is a country **/
    private static boolean isCountryName(String place) {
        return countriesUC.contains(place.toUpperCase());
    }

    /** converts a country name to a code **/
    private static String nameToCode(String country) {
        assert isCountryName(country);

        int index = countriesUC.indexOf(country.toUpperCase());
        return Locale.getISOCountries()[index]; // if out of bounds error thrown, isCountryName doesn't work
    }
}
