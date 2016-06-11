
	'use strict';
	
	angular.module('mangaCrawler.sourcesView', [ 'ngRoute' ])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/sources', {
			templateUrl : 'sourcesView/sources.html',
			controller : 'SourcesCtrl'
		});
	} ])
	.controller('SourcesCtrl', SourcesCtrl);
	
	
	SourcesCtrl.$inject = ['$http'];
	
	function SourcesCtrl($http) {
			var that = this;
			
			that.sources = [
					{
						id : 1,
						loading : false,
						name : 'one punchman',
						url : 'http://wallagain.cc/content/comics/one_punchman_56161ed820296/'
					},
					{
						id : 2,
						loading : false,
						name : 'fairy tail',
						url : 'http://www.wallagain.cc/content/comics/fairy_tail_5146fe64d7cb0/'
					} ];
			
			that.retrieveScans = retrieveScans;
			
			that.scans = []
			
			function retrieveScans(source) {
				source.loading = true;
				var url = '/crawl?method=AKKA&filter=zip&url=' + source.url;
				$http.get(url).success( function(response) {
						that.scans = response;
						source.loading = false;
		            });
				
			}
	}
	

