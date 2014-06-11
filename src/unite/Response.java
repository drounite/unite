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

	public Response(HttpClient client, HttpUriRequest request, String errorMsg, OnResponseListener listener) {
		this.client = client;
		this.request = request;
		this.errorMsg = errorMsg;
		
		makeRequest = new MakeRequest(listener).execute();
	}
	
	/**
	 * Waits if necessary for the computation to complete, and then retrieves its result.
	 * 
	 * @return String The response content
	 */
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
	
	public long getContentLength() {
		return response.getEntity().getContentLength();
	}
	
	public String getErrorMsg() {
		return errorMsg;
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
		
		private OnResponseListener listener;
		
		public MakeRequest(OnResponseListener listener) {
			this.listener = listener;
		}
		
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
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (listener != null) {
				listener.onResponseReceived(Response.this);
			}
		}
	}
	
	private void setErrorMsg(String msg) {
		errorMsg = msg;
	}
}
