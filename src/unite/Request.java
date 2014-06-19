package unite;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
	
	private String errorMsg;
	
	private HttpClient client;
	private HttpUriRequest request;
	private List<NameValuePair> params;
	
	private OnResponseListener listener;
	
	public Request(HttpClient client, HttpUriRequest request) {
		this.client = client;
		this.request = request;
		this.listener = null;
		this.errorMsg = null;
		
		params = new ArrayList<NameValuePair>();
	}
	
	public Request(HttpClient client, HttpUriRequest request, OnResponseListener listener) {
		this.client = client;
		this.request = request;
		this.listener = listener;
		this.errorMsg = null;
		
		params = new ArrayList<NameValuePair>();
	}
	
	public Request setOnResponseListener(OnResponseListener listener) {
		this.listener = listener;
		return this;
	}
	
	public Header[] getHeaders() {
		return request.getAllHeaders();
	}
	
	public Header[] getHeaders(String name) {
		return request.getHeaders(name);
	}

	public Request addHeader(String name, String value) {
		request.addHeader(name, value);		
		return this;
	}
	
	public Request setHeader(String name, String value) {
		request.setHeader(name, value);		
		return this;
	}
	
	public Request removeHeaders(String name) {
		request.removeHeaders(name);
		return this;
	}
	
	public String getMethod() {
		return request.getMethod();
	}
	
	public List<NameValuePair> getParams() {
		return params;
	}
	
	public Request addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
		return this;
	}
	
	public Request setParams(List<NameValuePair> params) {
		this.params = params;
		return this;
	}
	
	public Request setUri(String uri) {
		try {
			((HttpRequestBase) request).setURI(new URI(uri));
		} catch (URISyntaxException e) {
			setErrorMsg(e.getMessage());
		}
		return this;
	}
	
	public String getErrorMsg() {
		return errorMsg != null ? errorMsg : "Chill, everything okay.";
	}
	
	public Response send() {
		try {
			bindParams();
		} catch (NullPointerException e) {
			setErrorMsg(e.getMessage());
		}
		return new Response(client, request, errorMsg, listener);
	}
	
	public Response send(OnResponseListener listener) {
		try {
			bindParams();
		} catch (NullPointerException e) {
			setErrorMsg(e.getMessage());
		}
		return new Response(client, request, errorMsg, listener);
	}
	
	private void bindParams() {
		String method = getMethod();
		
		try {
			if (method.equals("POST")) {
				if (contentTypeIsJson()) {
					((HttpPost) request).setEntity(new StringEntity(paramsToJsonString()));
				} else {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(params));
				}
			} else if (method.equals("PUT")) {
				((HttpPut) request).setEntity(new UrlEncodedFormEntity(params));
			}
		} catch (UnsupportedEncodingException e) {
			setErrorMsg(e.getMessage());
		}
	}
	
	private boolean contentTypeIsJson() {
		Header[] contentTypes = getHeaders(HTTP.CONTENT_TYPE);
		for (Header contentType : contentTypes) {
			if (contentType.getValue().equals("application/json")) {
				return true;
			}
		}
		return false;
	}
	
	private String paramsToJsonString() {
		JSONObject jsonParams = new JSONObject();
		for (NameValuePair param : params) {
			try {
				jsonParams.accumulate(param.getName(), param.getValue());
			} catch (JSONException e) {
				setErrorMsg(e.getMessage());
			}
		}
		return jsonParams.toString();
	}
	
	private void setErrorMsg(String msg) {
		errorMsg = msg;
	}
}
