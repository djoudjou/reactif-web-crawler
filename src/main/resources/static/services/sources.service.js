(function() {
    'use strict';

    angular
        .module('mangaCrawler')
        .factory('SourcesService', SourcesService);

    SourcesService.$inject = ['$q', '$http'];

    function SourcesService($q, $http) {

        var dataPromise;

        var service = {
            getSources : getSources
        };

        return service;

        function getSources() {
            if (angular.isUndefined(dataPromise)) {
                dataPromise = $http.get('api/sources').then(function(result) {
                    return result.data;
                });
            }
            return dataPromise;
        }
    }
})();
