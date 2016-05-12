package fr.djoutsop.service;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.djoutsop.entity.Link;

public class WebCrawlerService {

	public void parseHtmlContent(String htmlContent) {
		Document doc = Jsoup.parse(htmlContent);

		Elements trs = doc.select("tr");

		for (Element tr : trs) {
			Elements tds = tr.children().select("td");
			if(tds.size()==5) {
				System.out.println(tds.get(1).select("a").attr("href"));
				
			}
		}

	}

	public List<Link> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

}
