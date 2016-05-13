package fr.djoutsop.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Link {
	final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private String path;
	private Date lastModified;

	public Link(String path, Date lastModified) {
		this.path = path;
		this.lastModified = lastModified;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Link [path=");
		builder.append(path);
		builder.append(", lastModified=");
		builder.append(lastModified);
		builder.append("]");
		return builder.toString();
	}

	public Link(String path, String lastModifiedStr) throws ParseException {
		this.path = path;
		this.lastModified = DATE_FORMAT.parse(lastModifiedStr);
	}

	public String getPath() {
		return path;
	}

	public Date getLastModified() {
		return lastModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		Link other = (Link) obj;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

}
