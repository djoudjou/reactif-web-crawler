package fr.djoutsop.crawler.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.djoutsop.crawler.entity.Method;
import fr.djoutsop.crawler.service.akka.impl.AkkaWebCrawlerService;
import fr.djoutsop.crawler.service.procedural.impl.WebCrawlerService;
import fr.djoutsop.crawler.utils.Loggable;

@Controller
public class WebCrawlerController {
	@Loggable
	Logger logger;

	@Autowired
	WebCrawlerService webCrawlerService;

	@Autowired
	AkkaWebCrawlerService akkaWebCrawlerService;

	@RequestMapping(value = "/crawl", method = RequestMethod.GET)
	public @ResponseBody List<String> crawl(
			@RequestParam("url") String urlToCrawl,
			@RequestParam(value = "method", defaultValue = "STANDARD") Method method,
			@RequestParam(value = "depth", defaultValue = "0") Integer depth,
			@RequestParam(value = "filter", defaultValue = "") String[] extensions)
			throws IOException {
		logger.debug("start crawl {} depth={} filter={}", urlToCrawl, depth,
				extensions);

		List<String> result = null;

		switch (method) {
		case STANDARD:
			result = webCrawlerService.crawl(urlToCrawl, depth, extensions)
					.collect(Collectors.toList());
			break;
		case AKKA:
			result = akkaWebCrawlerService.crawl(urlToCrawl, depth, extensions)
					.collect(Collectors.toList());
			break;
		}

		logger.debug("crawl result > {}", result);
		return result;

	}
}
