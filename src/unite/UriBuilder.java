package unite;

public class UriBuilder {
	
	private String scheme = null;
	private String userInfo = null;
	private String host = null;
	private String port = null;
	private String path = null;
	private String query = null;
	
	public UriBuilder scheme(String scheme) {
		this.scheme = scheme;
		return this;
	}
	
	public UriBuilder userInfo(String userInfo) {
		this.userInfo = userInfo;
		return this;
	}
	
	public UriBuilder host(String host) {
		this.host = host;
		return this;
	}
	
	public UriBuilder port(int port) {
		if (port > 0) {
			this.port = Integer.toString(port);	
		}
		return this;
	}
	
	public UriBuilder path(String path) {
		this.path = path;
		return this;
	}
	
	public UriBuilder appendPath(String path) {
		if (this.path == null || this.path.isEmpty()) {
			this.path = path;	
		} else {
			this.path += "/" + path;
		}		
		return this;
	}
	
	public UriBuilder query(String query) {
		this.query = query;
		return this;
	}
	
	public UriBuilder appendQueryParam(String name, String value) {
		if (this.path == null || this.path.isEmpty()) {
			this.query = name + "=" + value;	
		} else {
			this.query += "&" + name + "=" + value;
		}
		
		return this;
	}
	
	public String build() {
		String uri = "";
		if (scheme != null && !scheme.isEmpty()) {
			uri += scheme + "://";
		}
		if (userInfo != null && !userInfo.isEmpty()) {
			uri += userInfo + "@";
		}
		if (host != null && !host.isEmpty()) {
			uri += host;
		}
		if (port != null && !port.isEmpty()) {
			uri += ":" + port;
		}
		if (path != null && !path.isEmpty()) {
			uri += "/" + path;
		}
		if (query != null && !query.isEmpty()) {
			uri += "?" + query;
		}
		return uri;
	}
}
