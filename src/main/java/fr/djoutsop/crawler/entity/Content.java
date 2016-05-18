package fr.djoutsop.crawler.entity;

import java.io.Serializable;
import java.util.List;

public class Content implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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


	public List<String> getUrls() {
		return urls;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private List<String> urls;

	public Content(String title, String description, List<String> urls) {
		super();
		this.title = title;
		this.description = description;
		this.urls = urls;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Content [title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", urls=");
		builder.append(urls);
		builder.append("]");
		return builder.toString();
	}

	public String getDescription() {
		return description;
	}

}
