
	'use strict';
	
	angular.module('mangaCrawler.sourcesView', [ 'ngRoute' ])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/sources', {
			templateUrl : 'sourcesView/sources.html',
			controller : 'SourcesCtrl'
		});
	} ])
	.controller('SourcesCtrl', SourcesCtrl);
	
	
	SourcesCtrl.$inject = ['ScansService','SourcesService','$http'];
	

	function SourcesCtrl(ScansService, SourcesService, $http) {
			var that = this;
			
			that.sources = [];
			
			that.retrieveScans = retrieveScans;
			
			that.scans = [];
			
			SourcesService.getSources().then(function(response) {
				that.sources = response;
			});
			
			function retrieveScans(source) {
				source.loading = true;
				
				ScansService.loadScans(source).then(function(response) {
					that.scans = response;
					source.loading = false;
				},function(error){
					source.loading = false;
				});
				
			}
	}
	

