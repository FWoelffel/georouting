package com.CNAM.GeoRouting;

import android.os.StrictMode;

import android.util.Base64;

import org.apache.http.client.ClientProtocolException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;

import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class NetworkManager {

    // ---------------------

    private static NetworkManager m_instance = null;
    private static String m_credential = "";

    private static final String SERVER_IP = "ot2.sqanet.fr";
    private static final String PUBLIC_ROOT = "http://" + SERVER_IP + "/api/rest";
    private static final String ROOT_PATH = "https://" + SERVER_IP + "/api/rest/1.0";
    private static final String USER_COOKIE_NAME = "AlcUserId";

    // ---------------------

    private NetworkManager() {

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]
            {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                }
            }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            HttpsURLConnection.setFollowRedirects(true);
            CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // TODO : Remove the two lines above and do all the networking stuff in another thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    // ---------------------

    public static NetworkManager getInstance() {
        if ( m_instance == null){
            m_instance = new NetworkManager();
        }
        return m_instance;
    }

    // ---------------------

    public static NetworkManager getInstance(String _credential) {
        NetworkManager networkManager = getInstance();
        networkManager.m_credential = _credential;
        return networkManager;
    }

    // ---------------------

    public boolean authenticate(String _login, String _password) {

        try {

            URL url = new URL("https://"+SERVER_IP+"/api/rest/authenticate?version=1.0");
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            String authEncode = "Basic " + Base64.encodeToString(new String(_login + ":" + _password).getBytes(), Base64.NO_WRAP);
            httpURLConnection.setRequestProperty("Authorization", authEncode);
            httpURLConnection.setInstanceFollowRedirects(true);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String responseBody = getResponseBody(httpURLConnection);
                JSONObject responseBodyJSON = new JSONObject(responseBody);

                m_credential = responseBodyJSON.getString("credential");

                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    // ---------------------

    public String getCredential() {
        return m_credential;
    }

    // ---------------------

    private boolean openSession() {

        try {

            URL url = new URL(ROOT_PATH + "/sessions");
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Cookie",USER_COOKIE_NAME + "=" + m_credential);
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            JSONObject data = new JSONObject();
            data.put("applicationName", "GeoRouting");

            byte[] outputInBytes = data.toString().getBytes("UTF-8");
            OutputStream os = httpURLConnection.getOutputStream();
            os.write( outputInBytes );
            os.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String responseBody = getResponseBody(httpURLConnection);
                JSONObject responseBodyJSON = new JSONObject(responseBody);

                //TODO : test licenses

                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    // ---------------------

    private boolean closeSession()
    {
        try
        {
            URL url = new URL(ROOT_PATH + "/sessions");
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Cookie",USER_COOKIE_NAME + "=" + m_credential);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------

    public JSONArray getProfiles() {

        try {

            openSession();

            URL url = new URL(ROOT_PATH + "/routing/profiles");
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Cookie",USER_COOKIE_NAME + "=" + m_credential);
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String responseBody = getResponseBody(httpURLConnection);
                JSONObject responseBodyJSON = new JSONObject(responseBody);
                return responseBodyJSON.getJSONArray("profiles");
            }

            closeSession();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------------

    public JSONObject getRoutingState(){

        try {

            openSession();

            URL url = new URL(ROOT_PATH + "/routing/state");
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Cookie",USER_COOKIE_NAME + "=" + m_credential);
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String responseBody = getResponseBody(httpURLConnection);
                JSONObject responseBodyJSON = new JSONObject(responseBody);
                return responseBodyJSON;
            }

            closeSession();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    // ---------------------

    public int getAppliedProfileID() {

        JSONObject routingState = getRoutingState();
        if(routingState != null)
        {
            try {
                return routingState.getJSONObject("appliedProfile").getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    // ---------------------

    public boolean setAppliedProfile(int _id) {

        try
        {
            URL url = new URL(ROOT_PATH + "/routing/profiles/" + _id);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Cookie", USER_COOKIE_NAME + "=" + m_credential);
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            JSONObject data = new JSONObject();
            data.put("requestId", "");

            byte[] outputInBytes = data.toString().getBytes("UTF-8");
            OutputStream os = httpURLConnection.getOutputStream();
            os.write( outputInBytes );
            os.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    private String getResponseBody (HttpsURLConnection _httpURLConnection) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(_httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}