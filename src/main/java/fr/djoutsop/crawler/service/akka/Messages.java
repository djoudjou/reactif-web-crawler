package fr.djoutsop.crawler.service.akka;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

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
		public final Content content;

		public Index(URL url, Content content) {
			super();
			this.url = url;
			this.content = content;
		}
	}

	public static class Content implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String title;
		public final String meta;
		public final List<URL> urls;

		public Content(String title, String meta, List<URL> urls) {
			super();
			this.title = title;
			this.meta = meta;
			this.urls = urls;
		}

		@Override
		public String toString() {
			return String.format("Content [title=%s, meta=%s, urls=%s]", title,
					meta, urls);
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

		public IndexFinished(URL url,List<URL> urls) {
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
