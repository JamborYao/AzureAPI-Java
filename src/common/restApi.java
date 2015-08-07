package common;
import java.io.IOException;
import java.net.*;
import java.security.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Base64;

public class restApi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
            String subscriptionId = "5c99153f-be77-49ec-bbaa-107014e5dc69";
            String keyStorePath = "D:\\OpenSource\\java\\bin\\AzureKeyStore.jks";
            String keyStorePassword = "test123";
            String url = "";
    ///subscriptions/{subscription}/providers/Microsoft.Web/sites?api-version=2014-11-01
            //List locations
            String space="eastasiawebspace";
           String websiteconfigure="https://management.core.windows.net:8443/5c99153f-be77-49ec-bbaa-107014e5dc69/services/WebSpaces/"+space+"/sites/JK123/config";
           String webspaceString= "https://management.core.windows.net/5c99153f-be77-49ec-bbaa-107014e5dc69/services/WebSpaces/";
           
           String automationString="https://management.core.windows.net/5c99153f-be77-49ec-bbaa-107014e5dc69/cloudServices/"
           		+ "OaaSCSBBWCYXKOAZOJCWOIRTR6W4JCQOEHLF5OAM4B37RP75UCHQGSDNXA-East-US"
           		+ "/resources/automation/AutomationAccount/test127?resourceType=AutomationAccount&detailLevel=Full&resourceProviderNamespace=automation";
           
           String listContainerString="https://management.core.windows.net/5c99153f-be77-49ec-bbaa-107014e5dc69/cloudServices/"
           		+ "OaaSCSBBWCYXKOAZOJCWOIRTR6W4JCQOEHLF5OAM4B37RP75UCHQGSDNXA-East-US?"
           		+ "resourceType=AutomationAccount&detailLevel=Full&resourceProviderNamespace=automation";
           		
            url = String.format("https://management.core.windows.net/%s/services/WebSpaces/sites", subscriptionId);
           // String response = processGetRequest(new URL(listContainerString), keyStorePath, keyStorePassword);
            String contentString="<Resource xmlns=\"http://schemas.microsoft.com/windowsazure\"> "+
								  "<CloudServiceSettings> "+
								   " <GeoRegion>East US 2</GeoRegion>"+ 
								 " </CloudServiceSettings> "+
								 " <SchemaVersion>1.0</SchemaVersion> "+
								  "<Plan>Free</Plan> "+
							    "</Resource>";
				byte[] data=contentString.getBytes();				            
           int postresponse = processPostRequest(new URL(automationString),data,"application/xml", keyStorePath, keyStorePassword);
           // System.out.println(response);
           System.out.println(postresponse);
             
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		
	}
	
	 private static int processPostRequest(URL url, byte[] data, String contentType, String keyStore, String keyStorePassword) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
	        SSLSocketFactory sslFactory = getSSLSocketFactory(keyStore, keyStorePassword);
	        HttpsURLConnection con = null;
	        con = (HttpsURLConnection) url.openConnection();
	        con.setSSLSocketFactory(sslFactory);
	        con.setDoOutput(true);
	        con.setRequestMethod("PUT");
	        con.addRequestProperty("x-ms-version", "2013-08-01");
	        con.setRequestProperty("Content-Length", String.valueOf(data.length));
	        con.setRequestProperty("Content-Type", contentType);
	         
	        DataOutputStream  requestStream = new DataOutputStream (con.getOutputStream());
	        requestStream.write(data);
	        requestStream.flush();
	        requestStream.close();
	        return con.getResponseCode();
	    }

	
	private static String processGetRequest(URL url, String keyStore, String keyStorePassword) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
        SSLSocketFactory sslFactory = getSSLSocketFactory(keyStore, keyStorePassword);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(sslFactory);
        con.setRequestMethod("GET");
    /*    final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());*/
        
        con.addRequestProperty("x-ms-version", "2013-06-01");
       // con.addRequestProperty("x-ms-date",  utcTime);
        //con.addRequestProperty("x-ms-version", "2014-02-01");
        InputStream responseStream = (InputStream) con.getContent();
        String response = getStringFromInputStream(responseStream);
        responseStream.close();
        return response;
    }

	 private static KeyStore getKeyStore(String keyStoreName, String password) throws IOException
	    {
	        KeyStore ks = null;
	        FileInputStream fis = null;
	        try {
	            ks = KeyStore.getInstance("JKS");
	            char[] passwordArray = password.toCharArray();
	            fis = new java.io.FileInputStream(keyStoreName);
	            ks.load(fis, passwordArray);
	            fis.close();
	             
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        finally {
	            if (fis != null) {
	                fis.close();
	            }
	        }
	        return ks;
	    }
	     
	    private static SSLSocketFactory getSSLSocketFactory(String keyStoreName, String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
	        KeyStore ks = getKeyStore(keyStoreName, password);
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
	        keyManagerFactory.init(ks, password.toCharArray());
	 
	          SSLContext context = SSLContext.getInstance("TLS");
	          context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
	 
	          return context.getSocketFactory();
	    }
	     
	    // Source - http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
	    private static String getStringFromInputStream(InputStream is) {
	          
	        BufferedReader br = null;
	        StringBuilder sb = new StringBuilder();
	  
	        String line;
	        try {
	  
	            br = new BufferedReader(new InputStreamReader(is));
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	  
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	  
	        return sb.toString();
	  
	    }

}
