package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

class WebSiteCompare {

    private static final String username = "user";
    private static final String password = "passwd";

    static void iterateThroughWebsitesAndCompare(List<CustomPair<String, Status>> subpages1, List<CustomPair<String, Status>> subpages2) throws IOException {
        for (CustomPair<String, Status> pair1 : subpages1) {
            for (CustomPair<String, Status> pair2 : subpages2) {
                if (pair1.getKey().equals(pair2.getKey())) {
                    Boolean equal = compareWebSite(pair1.getKey(), pair2.getKey());
                    if (equal != null && equal) {
                        pair1.setValue(Status.EQUAL);
                        pair2.setValue(Status.EQUAL);
                    } else {
                        pair1.setValue(Status.NOT_EQUAL);
                        pair2.setValue(Status.NOT_EQUAL);
                    }
                }
            }
        }
    }

    public static void printResult(List<CustomPair<String, Status>> subpages1, List<CustomPair<String, Status>> subpages2) {

        boolean allEqual = true;
        for (CustomPair<String, Status> pair1 : subpages1) {
            if (pair1.getValue().equals(Status.NOT_EQUAL) || pair1.getValue().equals(Status.NOT_FOUND)) {
                System.out.println(pair1.getValue() + " " + pair1.getKey());
                allEqual = false;
            }
        }
        for (CustomPair<String, Status> pair2 : subpages2) {
            if (pair2.getValue().equals(Status.NOT_FOUND)) {
                System.out.println(pair2.getValue() + " " + pair2.getKey());
                allEqual = false;
            }
        }

        if (allEqual) {
            System.out.println("All Files were Equal");
        }
    }

    private static Boolean compareWebSite(String url1, String url2) throws IOException {

        BufferedReader in = createBufferedReader(url1, username, password);
        BufferedReader in2 = createBufferedReader(url2, username, password);

        Boolean equal = compareBufferReader(in, in2);

        /*if (equal != null && equal) {
            System.out.println(url1 + " and " + url2 + " are Equal");
        } else if (equal != null) {
            System.out.println(url1 + " and " + url2 + "are Not Equal");
        }*/

        return equal;
    }

    private static BufferedReader createBufferedReader(String url, String username, String password) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        String encoded = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));  //Java 8
        connection.setRequestProperty("Authorization", "Basic " + encoded);
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private static Boolean compareBufferReader(BufferedReader in, BufferedReader in2) throws IOException {
        Boolean equal = true;
        String inputLine;
        String inputLine2;
        try {

            while ((inputLine = in.readLine()) != null && (inputLine2 = in2.readLine()) != null)
                if (inputLine.compareTo(inputLine2) != 0) {
                    equal = false;
                    break;
                }
        } catch (Exception e) {
            System.out.println(e);
            equal = null;
        } finally {
            in.close();
            in2.close();
        }

        return equal;
    }
}
