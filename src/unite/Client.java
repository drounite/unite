package unite;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class Client {
	
	private HttpClient client;
	
	public Client() {
		client = new DefaultHttpClient();
	}
	
	public Request head() {
		return getRequest(new HttpHead());
	}
	
	public Request head(String uri) {
		return getRequest(new HttpHead(uri));
	}
	
	public Request get() {
		return getRequest(new HttpGet());
	}
	
	public Request get(String uri) {
		return getRequest(new HttpGet(uri));
	}
	
	public Request post() {
		return getRequest(new HttpPost());
	}
	
	public Request post(String uri) {
		return getRequest(new HttpPost(uri));
	}
	
	public Request put() {
		return getRequest(new HttpPut());
	}
	
	public Request put(String uri) {
		return getRequest(new HttpPut(uri));
	}
	
	public Request delete() {
		return getRequest(new HttpDelete());
	}
	
	public Request delete(String uri) {
		return getRequest(new HttpDelete(uri));
	}
	
	private Request getRequest(HttpUriRequest httpUriRequest) {
		return new Request(client, httpUriRequest);
	}
}
