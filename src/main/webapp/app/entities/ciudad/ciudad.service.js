(function() {
    'use strict';
    angular
        .module('pru1App')
        .factory('Ciudad', Ciudad);

    Ciudad.$inject = ['$resource'];

    function Ciudad ($resource) {
        var resourceUrl =  'api/ciudads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
