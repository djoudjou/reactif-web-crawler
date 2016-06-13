(function() {
    'use strict';

    angular
        .module('mangaCrawler')
        .factory('ScansService', ScansService);

    ScansService.$inject = ['$q', '$http'];

    function ScansService($q, $http) {

        var dataPromise;

        var service = {
        		loadScans : loadScans
        };

        return service;

        function loadScans(source) {
            if (angular.isUndefined(dataPromise)) {
                
            	var url = 'crawl?method=AKKA&filter=zip&url=' + source.url;
            	
            	dataPromise = $http.get(url).then(function(result) {
                    return result.data;
                });
            }
            return dataPromise;
        }
    }
})();
