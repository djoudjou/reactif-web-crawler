function retrieveScans(id, url) {
	var url = '/crawlScan?method=AKKA&filter=zip&url=' + url;
	$("#source_span_"+id).addClass("glyphicon-refresh-animate");
	$("#resultsBlock").load(url, function() {
		$("#source_span_"+id).removeClass("glyphicon-refresh-animate");
	});
	
}