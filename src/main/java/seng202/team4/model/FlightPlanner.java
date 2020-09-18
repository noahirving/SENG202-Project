package seng202.team4.model;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class FlightPlanner {

    public int getID(String src, String dest) {
        // api key EXQlo0QmCxshuiHbjg5cFe2Yl3oZN44kP7HyM3H2
        System.out.println("getting id");
        int id = 0;

        try {
            URI uriSearch = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.flightplandatabase.com")
                    .setPath("/search/plans")
                    .setParameter("fromICAO", src)
                    .setParameter("toICAO", dest)
                    .setParameter("limit", "1")
                    .build();

            URL obj = uriSearch.toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("search url: " + uriSearch.toString());
            System.out.println("code: " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject myResponse = new JSONObject(response.toString()
                    .replace("[", "").replace("]", ""));
            id = (int) myResponse.get("id");
            System.out.println("id: "+ id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public String getCSV(int ID) {
        System.out.println("getting csv");
        String csv = null;
        try {
            URI uriSearch = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.flightplandatabase.com")
                    .setPath("/plan/" + ID)
                    .build();
            URL obj = uriSearch.toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "text/csv");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine+"\n");
            }
            in.close();
            csv = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csv;
    }

    public void searchFlight() {
        System.out.println("trying to search");
        String src = "YARD";
        String dest = "YARG";
        int id = getID(src, dest);
        System.out.println(getCSV(id));
    }
}
