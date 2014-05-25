package com.CNAM.GeoRouting;

import android.os.StrictMode;

import android.util.Base64;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;




/**
 * Created by fwoelffel on 25/05/14.
 */
public class NetworkManager {

    // ---------------------

    private static NetworkManager m_instance = null;
    private static HttpClient m_client = null;
    private static String m_credential = "";

    private static final String SERVER_IP = "10.1.2.75";
    private static final String PUBLIC_ROOT = "http://" + SERVER_IP + "/api/rest";
    private static final String ROOT_PATH = "https://" + SERVER_IP + "/api/rest/1.0";
    private static final String USER_COOKIE_NAME = "AlcUserId";

    // ---------------------

    private NetworkManager() {

        m_client = getHttpClient();

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

        HttpGet get = new HttpGet();
        HttpResponse response = null;
        String responseBody = "";
        JSONObject responseBodyJSON = null;

        get.setURI(URI.create("https://"+SERVER_IP+"/api/rest/authenticate?version=1.0"));
        response = null;
        responseBody = "";
        responseBodyJSON = null;
        String credential = "";
        String authEncode = "Basic " + Base64.encodeToString(new String(_login + ":" + _password).getBytes(), Base64.NO_WRAP);
        get.setHeader("Authorization", authEncode);
        try {
            response = m_client.execute(get);
            responseBody = EntityUtils.toString(response.getEntity()).replaceAll("[\n]", "");
            responseBodyJSON = new JSONObject(responseBody);
            credential = responseBodyJSON.getString("credential");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == 200) {
            m_credential = credential;
            return true;
        }
        return false;

    }

    // ---------------------

    public String getCredential() {
        return m_credential;
    }

    // ---------------------

    private boolean openSession() {

        HttpPost post = new HttpPost(ROOT_PATH + "/sessions");
        HttpResponse response = null;
        post.setHeader("Cookie", USER_COOKIE_NAME + "=" + m_credential);
        post.setHeader("Content-type", "application/json");
        post.setHeader("Accept", "application/json");

        try {
            // Setting POST's data
            JSONObject data = new JSONObject();
            data.put("applicationName", "GeoRouting");
            post.setEntity(new StringEntity(data.toString()));
            response = m_client.execute(post);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        return false;

    }

    // ---------------------

    private boolean closeSession() {

        // FIXME : Seems to freeze the application... Don't know why

        HttpDelete delete = new HttpDelete(ROOT_PATH + "/sessions");
        HttpResponse response = null;
        delete.setHeader("Cookie", USER_COOKIE_NAME + "=" + m_credential);

        try {
            response = m_client.execute(delete);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == 204) {
            return true;
        }
        return false;
    }

    // ---------------------

    public JSONArray getProfiles() {

        HttpGet get = new HttpGet(ROOT_PATH + "/routing/profiles");
        HttpResponse response = null;
        String responseBody = "";
        JSONObject responseBodyJSON = null;
        get.setHeader("Cookie", USER_COOKIE_NAME + "=" + m_credential);

        try {
            openSession();
            response = m_client.execute(get);
//            closeSession();
            responseBody = EntityUtils.toString(response.getEntity()).replaceAll("[\n]", "");
            responseBodyJSON = new JSONObject(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                return responseBodyJSON.getJSONArray("profiles");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONArray();
    }

    // ---------------------

    private HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
}

    // ---------------------

    private class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}