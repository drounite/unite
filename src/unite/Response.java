package unite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

public class Response {
	
	private String errorMsg;
	
	private HttpClient client;
	private HttpUriRequest request;
	private HttpResponse response;
	
	private AsyncTask<Void, Void, String> makeRequest;

	public Response(HttpClient client, HttpUriRequest request, String errorMsg) {
		this.client = client;
		this.request = request;
		this.errorMsg = errorMsg;
		
		makeRequest = new MakeRequest().execute();
	}
	
	public String getContent() {
		try {
			return makeRequest.get();
		} catch (InterruptedException e) {
			setErrorMsg(e.getMessage());
		} catch (ExecutionException e) {
			setErrorMsg(e.getMessage());
		}
		
		return errorMsg;
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
	
	private String getResponseContent() {
		String content = "";
		
		try {
			InputStream instream = response.getEntity().getContent();
			content = streamToString(instream);
            instream.close();
		} catch (IllegalStateException e) {
			setErrorMsg(e.getMessage());
		} catch (IOException e) {
			setErrorMsg(e.getMessage());
		}
		
		return content;
	}
	
	public long getContentLength() {
		return response.getEntity().getContentLength();
	}
	
	public String getErrorMsg() {
		return errorMsg;
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
	    	setErrorMsg(e.getMessage());
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	        	setErrorMsg(e.getMessage());
	        }
	    }
	    return sb.toString();
	}
	
	private class MakeRequest extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... params) {

			String responseContent = "";
			
			try {
				response = client.execute(request);
				responseContent = getResponseContent();
			} catch (ClientProtocolException e) {
				setErrorMsg(e.getMessage());
			} catch (IOException e) {
				setErrorMsg(e.getMessage());
			}
			
			return responseContent;
		}
	}
	
	private void setErrorMsg(String msg) {
		errorMsg = msg;
	}
}
