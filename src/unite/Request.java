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
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class Request {
	
	private HttpClient client;
	private HttpUriRequest request;
	private List<NameValuePair> params;
	
	public Request(HttpClient client, HttpUriRequest request) {
		this.client = client;
		this.request = request;
		params = new ArrayList<NameValuePair>();
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
	
	public String getUri() {
		return request.getURI().toString();
	}
	
	public Request setUri(String uri) {
		try {
			((HttpRequestBase) request).setURI(new URI(uri));
		} catch (URISyntaxException e) {
			Log.e("Unite-Request", e.getMessage());
		}
		return this;
	}
	
	public String getScheme() {
		return request.getURI().getScheme();
	}
	
	public Request setScheme(String scheme) {
		String uri = buildUri(scheme, getUserInfo(), getHost(), getPort(),
				getPath(), getQuery());
//		setUri(uri);
		return this;
	}
	
	public String getAuthority() {
		return request.getURI().getAuthority();
	}
	
	public String getUserInfo() {
		return request.getURI().getUserInfo();
	}
	
	public Request setUserInfo(String username, String password) {
		setUri(buildUri(getScheme(), username + ":" + password, getHost(),
				getPort(), getPath(), getQuery()));
		return this;
	}
	
	public String getHost() {
		return request.getURI().getHost();
	}
	
	public Request setHost(String host) {
		setUri(buildUri(getScheme(), getUserInfo(), host, getPort(), getPath(),
				getQuery()));
		return this;
	}
	
	public int getPort() {
		return request.getURI().getPort();
	}
	
	public Request setPort(int port) {
		setUri(buildUri(getScheme(), getUserInfo(), getHost(), port, getPath(),
				getQuery()));
		return this;
	}
	
	public String getPath() {
		return request.getURI().getPath();
	}
	
	public Request setPath(String path) {
		setUri(buildUri(getScheme(), getUserInfo(), getHost(), getPort(), path,
				getQuery()));
		return this;
	}
	
	public String getQuery() {
		return request.getURI().getQuery();
	}
	
	public Request setQuery(String query) {
		setUri(buildUri(getScheme(), getUserInfo(), getHost(), getPort(), getPath(),
				query));
		return this;
	}
	
	public Response send() throws UnsupportedEncodingException {
		bindParams();
		return new Response(client, request);
	}
	
	private void bindParams() throws UnsupportedEncodingException {
		String method = getMethod();

		if (method.equals("POST")) {
			((HttpPost) request).setEntity(new UrlEncodedFormEntity(params));
		} else if (method.equals("PUT")) {
			((HttpPut) request).setEntity(new UrlEncodedFormEntity(params));
		}
	}
	
	private String buildUri(
			String scheme,
			String userInfo,
			String host,
			int port,
			String path,
			String query
			) {
		return new UriBuilder()
		.scheme(scheme)
		.userInfo(userInfo)
		.host(host)
		.port(port)
		.path(path)
		.query(query)
		.build();
	}
}
