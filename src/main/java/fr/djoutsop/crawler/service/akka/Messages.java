package fr.djoutsop.crawler.service.akka;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.djoutsop.crawler.entity.Content;

public class Messages {

	public static class Start implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;

		public Start(URL url) {
			super();
			this.url = url;
		}
	}

	public static class Scrap implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;

		public Scrap(URL url) {
			super();
			this.url = url;
		}
	}

	public static class Index implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;
		public final ContentMessage content;

		public Index(URL url, ContentMessage content) {
			super();
			this.url = url;
			this.content = content;
		}
	}

	public static class ContentMessage implements Serializable {
		static Logger logger = LoggerFactory.getLogger(ContentMessage.class);
		static final long serialVersionUID = 1L;
		public final String title;
		public final String meta;
		public final List<URL> urls;

		public ContentMessage(String title, String meta, List<URL> urls) {
			super();
			this.title = title;
			this.meta = meta;
			this.urls = urls;
		}

		public ContentMessage(Content content) {
			super();
			this.title = content.getTitle();
			this.meta = content.getDescription();
			this.urls = content.getUrls().stream().map(url -> {
				try {
					return new URL(url);
				} catch (Exception ex) {
					logger.error("invalid url > {}", url);
					return null;
				}
			}).filter(url -> url != null).collect(Collectors.toList());
		}

		@Override
		public String toString() {
			return String.format("Content [title=%s, meta=%s, urls=%s]", title, meta, urls);
		}
	}

	public static class ScrapFinished implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;

		public ScrapFinished(URL url) {
			super();
			this.url = url;
		}
	}

	public static class IndexFinished implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;
		public final List<URL> urls;

		public IndexFinished(URL url, List<URL> urls) {
			super();
			this.url = url;
			this.urls = urls;
		}
	}

	public static class ScrapFailure implements Serializable {
		private static final long serialVersionUID = 1L;
		public final URL url;
		public final Throwable reason;

		public ScrapFailure(URL url, Throwable reason) {
			super();
			this.url = url;
			this.reason = reason;
		}
	}

}
