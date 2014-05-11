package unite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;

public class Response {
	
	HttpResponse response;

	public Response(HttpClient client, HttpUriRequest request) {
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}
	
	public Header[] getHeaders() {
		return response.getAllHeaders();
	}
	
	public Header[] getHeaders(String name) {
		return response.getHeaders(name);
	}
	
	public HttpParams getParams() {
		return response.getParams();
	}
	
	public String getContent() {
		String content = "";
		
		try {
			InputStream instream = response.getEntity().getContent();
			content = streamToString(instream);
            instream.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public long getContentLength() {
		return response.getEntity().getContentLength();
	}
	
	private String streamToString(InputStream is) {
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    
	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}
