package common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;











import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;



public class SendmessageToServiceBus {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
/*
		String a_Url = "https://jamborsb.servicebus.windows.net/mytest/" ;

		URL url = new URL (a_Url);
		//String encoding = Base64Encoder.encode ("test:test");
		
		
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
	
		
		 HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setDoInput(true); 
		connection.setDoOutput(true);
		
		 
		connection.setRequestMethod("POST"); 
		
		
		
		

		//HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		
		connection.setDoInput(true); 
		connection.setDoOutput(true);
	
		

		connection.setRequestMethod( "POST" );
		
		String tokenString=generateSasToken("https://jamborsb.servicebus.windows.net/mytest/","test","Ksr2l2nOmj1aU0QNW4+0vzBjWxwJ0zQ4vTjsGGIHuA8=");
		connection.setRequestProperty  ("Authorization", tokenString);
		
		JSONObject data = new JSONObject();
		data.put("message", "hello java");
		OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
		wr.write(data.toString());
		wr.flush();
		connection.connect();
		/*
		InputStream content = (InputStream)connection.getInputStream();
		BufferedReader in   = 
		    new BufferedReader (new InputStreamReader (content));
		String line;
		while ((line = in.readLine()) != null) {
		    System.out.println(line);
		}
		*/
		String tokenString=generateSasToken("https://jamborsb.servicebus.windows.net/mytest/","test","Ksr2l2nOmj1aU0QNW4+0vzBjWxwJ0zQ4vTjsGGIHuA8=");
		String authString="Authorization:"+tokenString;
		
		System.out.println(authString);

	}
	
	
	
	private static String generateSasToken(String uri, String keyName, String key){
	    String ret = "";

	   // long tokenExpirationTime = (System.currentTimeMillis() / 1000) + (10 * 365 * 24 * 60 * 60);
	    
	    Date now = new Date();
	    Date previousDate=new Date(1970);
	    long tokenExpirationTime = ((now.getTime() - previousDate.getTime()) / 1000 )+3600;
	   
	    try {
	        String stringToSign = URLEncoder.encode(new URL(uri).toString(),java.nio.charset.StandardCharsets.UTF_8.toString()) + "\n" + tokenExpirationTime;
	        
	        System.out.println(stringToSign);
	        SecretKey secretKey = null;

	        byte[] keyBytes = key.getBytes("UTF-8");

	        Mac mac = Mac.getInstance("HMACSHA256");

	        secretKey = new SecretKeySpec(keyBytes, mac.getAlgorithm());

	        mac.init(secretKey);

	        byte[] digest = mac.doFinal(stringToSign.getBytes());
			//We then use the composite signing key to create an oauth_signature from the signature base string
	        String signature = Base64.encodeBase64String(digest);
	        System.out.println( URLEncoder.encode(signature, java.nio.charset.StandardCharsets.UTF_8.toString()));
	       // String signature = Base64.encodeBase64String(mac.doFinal(stringToSign.getBytes("UTF-8")));
	        ret = String.format("SharedAccessSignature sr=%s&sig=%s&se=%s&skn=%s",
	                URLEncoder.encode(uri, java.nio.charset.StandardCharsets.UTF_8.toString()),
	                URLEncoder.encode(signature, java.nio.charset.StandardCharsets.UTF_8.toString()),
	                String.valueOf(tokenExpirationTime),
	                keyName);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    return ret;
	}


}
