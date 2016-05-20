package fr.djoutsop.crawler.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ContentTest {

	@Test
	public void equals_WithNullObjectShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = null;
		
		// When
		boolean result = elt1.equals(elt2);
		
		// Then
		assertThat(result, is(false));
	}
	
	@Test
	public void equals_WithDifferentTitleShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title1", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title2", "description", Arrays.asList("url1","url2"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithNullTitleShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title1", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content(null, "description", Arrays.asList("url1","url2"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithDifferentDescriptionShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description1", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description2", Arrays.asList("url1","url2"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithNullDescriptionShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description1", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", null, Arrays.asList("url1","url2"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithDifferentUrlsShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url1","url2", "url3"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithNullUrlsShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", null);
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
	}
	
	@Test
	public void equals_WithSameContentShouldBeTrue() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url1","url2"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(true));
		assertThat(result2, is(true));
	}
	
	@Test
	public void equals_WithSameContentButNotInOrderUrlShouldBeTrue() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url2","url1"));
		
		// When
		boolean result1 = elt1.equals(elt2);
		boolean result2 = elt2.equals(elt1);
		
		// Then
		assertThat(result1, is(true));
		assertThat(result2, is(true));
	}
	
	@Test
	public void equals_WithSameInstanceShouldBeTrue() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		
		// When
		boolean result = elt1.equals(elt1);
		
		// Then
		assertThat(result, is(true));
	}
	
	
	@Test
	public void equals_WithDifferentClassShouldBeFalse() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		
		// When
		boolean result = elt1.equals(new Integer(1));
		
		// Then
		assertThat(result, is(false));
	}
	
	
	@Test
	public void hashCode_WithDifferentTitleShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title1", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title2", "description", Arrays.asList("url1","url2"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithNullTitleShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title1", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content(null, "description", Arrays.asList("url1","url2"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithDifferentDescriptionShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title", "description1", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description2", Arrays.asList("url1","url2"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithNullDescriptionShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title", "description1", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", null, Arrays.asList("url1","url2"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithDifferentUrlsShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url1","url2", "url3"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithNullUrlsShouldBeDifferent() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", null);
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, not(equalTo(result2)));
	}
	
	@Test
	public void hashCode_WithSameContentShouldBeEquals() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url1","url2"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, equalTo(result2));
	}
	
	@Test
	public void hashCode_WithSameContentButNotInOrderUrlShouldBeEquals() throws Exception {
		// Given
		Content elt1 = new Content("title", "description", Arrays.asList("url1","url2"));
		Content elt2 = new Content("title", "description", Arrays.asList("url2","url1"));
		
		// When
		int result1 = elt1.hashCode();
		int result2 = elt2.hashCode();
		
		// Then
		assertThat(result1, equalTo(result2));
	}
	
	@Test
	public void getTitle_ShouldReturnTitle() throws Exception {
		// Given
		Content elt = new Content("titre", "azerty", Arrays.asList("url1","url2"));
		
		// When
		String result = elt.getTitle();
		
		// Then
		assertThat(result, equalTo("titre"));
	}
	
	@Test
	public void getUrls_ShouldReturnUrls() throws Exception {
		// Given
		Content elt = new Content("titre", "azerty", Arrays.asList("url1","url2"));
		
		// When
		Set<String> result = elt.getUrls();
		
		// Then
		assertThat(result, containsInAnyOrder("url1", "url2"));
	}
	
	@Test
	public void getDescription_ShouldReturnUrls() throws Exception {
		// Given
		Content elt = new Content("titre", "azerty", Arrays.asList("url1","url2"));
		
		// When
		String result = elt.getDescription();
		
		// Then
		assertThat(result, equalTo("azerty"));
	}
	
	@Test
	public void toString_ShouldOutputFields() throws Exception {
		// Given
		Content elt = new Content("titre", "azerty", Arrays.asList("url1","url2"));
		
		// When
		String result = elt.toString();
		
		// Then
		assertThat(result, equalTo("Content(title=titre, description=azerty, urls=[url1, url2])"));
	}
	
}
