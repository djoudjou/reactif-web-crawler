package fr.djoutsop.crawler.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
	
	@RequestMapping("/index")
    public String index(Model model) {
        return "home";
    }
	
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
