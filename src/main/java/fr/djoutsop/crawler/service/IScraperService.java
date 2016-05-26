package fr.djoutsop.crawler.service;

import java.io.IOException;
import java.net.URL;

import fr.djoutsop.crawler.entity.Content;

public interface IScraperService {
	public Content scrap(URL url) throws IOException;

	public Content scrap(String urlToScrap) throws IOException;

	public Content scrap(String urlToScrap, String referer) throws IOException;

	public Content scrapHtmlContent(String htmlContent);
}
