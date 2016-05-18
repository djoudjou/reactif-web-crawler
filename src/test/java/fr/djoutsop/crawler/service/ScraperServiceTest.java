package fr.djoutsop.crawler.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import fr.djoutsop.crawler.entity.Content;

@RunWith(MockitoJUnitRunner.class)
public class ScraperServiceTest {

	ScraperService subject;

	@Before
	public void setup() {
		subject = new ScraperService();
	}

	@Test
	public void defaultScraperService_ShouldSetEmptyStringAsAllowedExtension() {
		// Given

		// When
		subject = new ScraperService();

		// Then
		assertThat(subject.allowedExtensions, hasSize(1));
		assertThat(subject.allowedExtensions, contains(""));
	}

	@Test
	public void newScraperService_ShouldSetGivenAllowedExtensions() {
		// Given

		// When
		subject = new ScraperService("zip", "png");

		// Then
		assertThat(subject.allowedExtensions, hasSize(3));
		assertThat(subject.allowedExtensions, containsInAnyOrder("zip", "png", ""));
	}

	@Test
	public void newScraperService_ShouldSetGivenAllowedExtensions_WithoutDoublon() {
		// Given

		// When
		subject = new ScraperService("zip", "zip", "png");

		// Then
		assertThat(subject.allowedExtensions, hasSize(3));
		assertThat(subject.allowedExtensions, containsInAnyOrder("zip", "png", ""));
	}

	@Test
	public void isSubUrl_ShouldBeFalseIfNotSubUrl() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("http://nawak");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(false));
	}

	@Test
	public void isSubUrl_ShouldBeTrueIfSubUrl() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("http://toto/pictures");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(true));
	}

	@Test
	public void isSubUrl_WithoutAbsUrl_ShouldBeFalseIfBaseUrlStartWithRedirect() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("");
		when(elementMock.attr("href")).thenReturn("/pictures");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(false));
	}
	
	@Test
	public void isSubUrl_WithoutAbsUrl_ShouldBeFalseIfBaseUrlStartWithDoubleDot() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("");
		when(elementMock.attr("href")).thenReturn("../pictures");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(false));
	}
	
	@Test
	public void isSubUrl_WithoutAbsUrl_ShouldBeFalseIfBaseUrlContainsDoubleDot() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("");
		when(elementMock.attr("href")).thenReturn("pictures/../toto");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(false));
	}

	@Test
	public void isSubUrl_WithoutAbsUrl_ShouldBeTrueIfBaseUrlStartWithoutRedirect() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("");
		when(elementMock.attr("href")).thenReturn("pictures");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		boolean result = subject.isSubUrl(elementMock);

		// Then
		assertThat(result, is(true));
	}

	@Test
	public void isValidExtension_ShouldReturnTrueForValidExtensions() throws Exception {
		// Given
		subject = new ScraperService("zip", "png");

		Element elementOk1Mock = mock(Element.class);
		when(elementOk1Mock.attr("href")).thenReturn("http://toto/pictures/toto.png");
		Element elementOk2Mock = mock(Element.class);
		when(elementOk2Mock.attr("href")).thenReturn("http://toto/pictures/toto.Zip");
		Element elementKoMock = mock(Element.class);
		when(elementKoMock.attr("href")).thenReturn("http://toto/pictures/toto.tar");

		// When
		boolean validEltOk1 = subject.isValidExtension(elementOk1Mock);
		boolean validEltOk2 = subject.isValidExtension(elementOk2Mock);
		boolean validEltKo = subject.isValidExtension(elementKoMock);

		// Then
		assertThat(validEltOk1, is(true));
		assertThat(validEltOk2, is(true));
		assertThat(validEltKo, is(false));
	}

	@Test
	public void isValidExtension_ShouldReturnTrueForSubUrl() throws Exception {
		// Given
		subject = new ScraperService();

		Element elementSubUrlMock = mock(Element.class);
		when(elementSubUrlMock.attr("href")).thenReturn("http://toto/pictures/");

		// When
		boolean validSubUrl = subject.isValidExtension(elementSubUrlMock);

		// Then
		assertThat(validSubUrl, is(true));
	}

	@Test
	public void mapUrl_ShouldReturnAbsUrlWhenGiven() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("http://toto/pictures");
		when(elementMock.attr("href")).thenReturn("pic");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		String result = subject.mapUrl(elementMock);

		// Then
		assertThat(result, is("http://toto/pictures"));
	}
	
	
	@Test
	public void mapUrl_ShouldReturnAttrUrlWhenAbsUrlNotGiven() throws Exception {
		// Given
		Element elementMock = mock(Element.class);
		when(elementMock.absUrl("href")).thenReturn("");
		when(elementMock.attr("href")).thenReturn("pictures");
		when(elementMock.baseUri()).thenReturn("http://toto");

		// When
		String result = subject.mapUrl(elementMock);

		// Then
		assertThat(result, is("pictures"));
	}

	@Test
	public void isQueryFilter_ShouldReturnTrueWithQueryFilter() throws Exception {
		// Given
		Element elementOk1Mock = mock(Element.class);
		when(elementOk1Mock.attr("href")).thenReturn("?key=val");
		Element elementKoMock = mock(Element.class);
		when(elementKoMock.attr("href")).thenReturn("pictures");

		// When
		boolean validEltOk1 = subject.isQueryFilter(elementOk1Mock);
		boolean validEltKo = subject.isQueryFilter(elementKoMock);

		// Then
		assertThat(validEltOk1, is(true));
		assertThat(validEltKo, is(false));
	}

	@Test
	public void scrapDocument_ShouldManageDocument() throws Exception {
		// Given
		StringBuilder sb = new StringBuilder();
		
		sb.append("<!DOCTYPE html>");
		sb.append("<html lang=\"en-US\">");
		sb.append("<head>");
		sb.append("<meta charset=\"utf-8\">");
		sb.append("<title>TITLE</title>");
		 
		sb.append("<meta name=\"twitter:title\" content=\"Latest Posts\">");
		sb.append("<meta name=\"twitter:site\" content=\"@foat_akhmadeev\">");
		sb.append("<meta name=\"twitter:creator\" content=\"@foat_akhmadeev\">");
		sb.append("<meta name=\"twitter:card\" content=\"summary\">");
		sb.append("<meta name=\"twitter:image\" content=\"https://foat.me/images/logo.jpg\">");
		sb.append("<meta name=\"description\" content=\"DESCRIPTION1\">");
		sb.append("<meta name=\"description\" content=\"DESCRIPTION2\">");
		sb.append("<meta name=\"description\" content=\"DESCRIPTION3\">");
		sb.append("<meta name=\"description\" content=\"DESCRIPTION4\">");
		
		 
		sb.append("<meta property=\"og:locale\" content=\"en_US\">");
		sb.append("<meta property=\"og:type\" content=\"article\">");
		sb.append("<meta property=\"og:title\" content=\"Latest Posts\">");
		sb.append("<meta property=\"og:url\" content=\"https://foat.me/\">");
		sb.append("<meta property=\"og:site_name\" content=\"Foat Akhmadeev\">");
		sb.append("<link href=\"https://foat.me/atom.xml\" type=\"application/atom+xml\" rel=\"alternate\" title=\"Foat Akhmadeev Atom Feed\">");
		sb.append("<link href=\"https://foat.me/sitemap.xml\" type=\"application/xml\" rel=\"sitemap\" title=\"Sitemap\">");
		sb.append("<meta name=\"HandheldFriendly\" content=\"True\">");
		sb.append("<meta name=\"MobileOptimized\" content=\"320\">");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		sb.append("<meta http-equiv=\"cleartype\" content=\"on\">");
		sb.append("<link rel=\"stylesheet\" href=\"https://foat.me/css/main.css\">");
		 
		sb.append("</head>");
		sb.append("<body id=\"js-body\">");
		sb.append("<div class=\"inner-wrap\">");
		sb.append("<a href=\"https://foat.me1/\" class=\"site-title\">Foat Akhmadeev</a>");
		sb.append("<nav role=\"navigation\" class=\"menu top-menu\">");
		sb.append("<a href=\"https://foat.me2/\" class=\"site-title\">Foat Akhmadeev</a>");
		sb.append("<ul class=\"menu-item\">");
		sb.append("<a href=\"https://foat.me3/\" class=\"site-title\">Foat Akhmadeev</a>");
		sb.append("<div class=\"social-icons\">");
		sb.append("<a href=\"https://foat.me4/\" class=\"site-title\">Foat Akhmadeev</a>");
		sb.append("<li class=\"home\"><a href=\"/ToExclude\">Foat Akhmadeev</a></li>");
		sb.append("<div class=\"left\">");
		sb.append("</html>");
		
		String htmlContent = sb.toString();
		Document document = Jsoup.parse(htmlContent);
		
		// When
		Content result = subject.scrap(document);
		
		// Then
		assertThat(result.getTitle(), is("TITLE"));
		assertThat(result.getDescription(), is("DESCRIPTION1"));
		assertThat(result.getUrls(), containsInAnyOrder("https://foat.me1/","https://foat.me2/","https://foat.me3/","https://foat.me4/"));
	}

}
