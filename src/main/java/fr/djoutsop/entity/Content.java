package fr.djoutsop.entity;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class Content implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((meta == null) ? 0 : meta.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Content other = (Content) obj;
		if (meta == null) {
			if (other.meta != null)
				return false;
		} else if (!meta.equals(other.meta))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public String getTitle() {
		return title;
	}

	public String getMeta() {
		return meta;
	}

	public List<URL> getUrls() {
		return urls;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String meta;
	private List<URL> urls;

	public Content(String title, String meta, List<URL> urls) {
		super();
		this.title = title;
		this.meta = meta;
		this.urls = urls;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Content [title=");
		builder.append(title);
		builder.append(", meta=");
		builder.append(meta);
		builder.append(", urls=");
		builder.append(urls);
		builder.append("]");
		return builder.toString();
	}

}
