package com.rigelci.tool;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestHttps {

	private String httpsURL;
	 private URL destURL;
	 private HttpsURLConnection urlConn;
	 private String request;

	 public void connectURL() {
	  try {
	   destURL = new URL(httpsURL);
	   urlConn = (HttpsURLConnection) destURL.openConnection();
	  } catch (Exception e) {
	   System.out.println("Connect   URL   Error   :" + "\n" + e);
	  }
	 }

	 public void doResponse() {
	  try {
	   DataInputStream inStream = new DataInputStream(urlConn
	     .getInputStream());
	   System.out.println("Print   HeaderFile:");
	   int i = 0;
	   while (urlConn.getHeaderField(i) != null) {
	    System.out.println(urlConn.getHeaderFieldKey(i) + "   "
	      + urlConn.getHeaderField(i));
	    i++;
	   }
	   System.out.println("method=" + urlConn.getRequestMethod());
	   System.out.println("" + urlConn.getResponseCode());

	   int ch;
	   while ((ch = inStream.read()) >= 0) {
	    System.out.print((char) ch);
	   }
	   inStream.close();
	  } catch (Exception e) {
	   System.out.println("Do   Response   Error" + "\n" + e);
	  }
	 }

	 /**
	  * @param args
	  */
	 public static void main(String args[]) {

	  TestHttps th = new TestHttps();
	  // th.httpsURL =
	th.httpsURL = " https://xxxxx";

	  // HttpClient httpclient = new HttpClient();
	  // httpclient.getHostConfiguration().setProxy("myproxyhost", 8080);
	  // httpclient.getState().setProxyCredentials(
	  // "my-proxy-realm",
	  // " myproxyhost",
	  // new UsernamePasswordCredentials("my-proxy-username",
	  // "my-proxy-password"));
	// GetMethod httpget = new GetMethod(" https://1xxxx81");
	// GetMethod httpget = new GetMethod(" https://19xxxxx");
	  // try {
	  // httpclient.executeMethod(httpget);
	  // System.out.println(httpget.getStatusLine());
	  // } catch (Exception e) {
	  // // TODO: handle exception
	  // e.printStackTrace();
	  // } finally {
	  // httpget.releaseConnection();
	  // }
	 }
}
