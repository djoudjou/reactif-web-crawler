package fr.djoutsop.crawler.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Content implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private Set<String> urls;

	public Content(String title, String description, List<String> urls) {
		super();
		this.title = title;
		this.description = description;
		if (urls != null) {
			this.urls = new HashSet<>(urls);
		} else {
			this.urls = null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((urls == null) ? 0 : urls.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Content other = (Content) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (urls == null) {
			if (other.urls != null) {
				return false;
			}
		} else if (other.urls == null) {
			return false;
		} else if (!urls.containsAll(other.urls)) {
			return false;
		} else if (urls.size() != other.urls.size()) {
			return false;
		}
		return true;
	}

	public String getTitle() {
		return title;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Content(title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", urls=");
		builder.append(urls);
		builder.append(")");
		return builder.toString();
	}

}
