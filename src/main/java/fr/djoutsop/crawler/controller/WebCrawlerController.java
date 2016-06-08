package fr.djoutsop.crawler.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.djoutsop.crawler.entity.Method;
import fr.djoutsop.crawler.entity.Source;
import fr.djoutsop.crawler.service.akka.impl.AkkaWebCrawlerService;
import fr.djoutsop.crawler.service.procedural.impl.StandardWebCrawlerService;
import fr.djoutsop.crawler.utils.Loggable;

@Controller
public class WebCrawlerController {
	@Loggable
	Logger logger;

	@Autowired
	StandardWebCrawlerService webCrawlerService;

	@Autowired
	AkkaWebCrawlerService akkaWebCrawlerService;

	@RequestMapping(value = { "/", "/index" })
	public String index(Model model) {

		List<Source> sources = Arrays.asList(
				new Source(1L, "one punchman", "http://wallagain.cc/content/comics/one_punchman_56161ed820296/"),
				new Source(2L, "fairy tail", "http://www.wallagain.cc/content/comics/fairy_tail_5146fe64d7cb0/"));

		model.addAttribute("sources", sources);

		return "home";
	}
	
	@RequestMapping(value = "/crawlScan", method = RequestMethod.GET)
	public String crawl(@RequestParam("url") String urlToCrawl,
			@RequestParam(value = "method", defaultValue = "STANDARD") Method method,
			@RequestParam(value = "depth", defaultValue = "0") Integer depth,
			@RequestParam(value = "filter", defaultValue = "") String[] extensions,
			Model model) throws IOException {
		
		logger.debug("start crawl {} depth={} filter={}", urlToCrawl, depth, extensions);

		List<String> result = null;

		switch (method) {
		case STANDARD:
			result = webCrawlerService.crawl(urlToCrawl, depth, extensions).collect(Collectors.toList());
			break;
		case AKKA:
			result = akkaWebCrawlerService.crawl(urlToCrawl, depth, extensions).collect(Collectors.toList());
			break;
		}

		logger.debug("crawl result > {}", result);
		
	    model.addAttribute("scans", result);
	    
	    return "fragments/crawl :: resultsList";
	}

	@RequestMapping(value = "/crawl", method = RequestMethod.GET)
	public @ResponseBody List<String> crawl(@RequestParam("url") String urlToCrawl,
			@RequestParam(value = "method", defaultValue = "STANDARD") Method method,
			@RequestParam(value = "depth", defaultValue = "0") Integer depth,
			@RequestParam(value = "filter", defaultValue = "") String[] extensions) throws IOException {
		logger.debug("start crawl {} depth={} filter={}", urlToCrawl, depth, extensions);

		List<String> result = null;

		switch (method) {
		case STANDARD:
			result = webCrawlerService.crawl(urlToCrawl, depth, extensions).collect(Collectors.toList());
			break;
		case AKKA:
			result = akkaWebCrawlerService.crawl(urlToCrawl, depth, extensions).collect(Collectors.toList());
			break;
		}

		logger.debug("crawl result > {}", result);
		return result;
	}
}
