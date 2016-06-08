package fr.djoutsop.crawler.entity;

import java.io.Serializable;

public class Source implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final long id;
	private final String name;
	private final String url;

	public Source(long id,String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
}
