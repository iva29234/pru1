(function() {
    'use strict';

    angular
        .module('pru1App')
        .controller('CiudadDetailController', CiudadDetailController);

    CiudadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ciudad', 'Pais'];

    function CiudadDetailController($scope, $rootScope, $stateParams, entity, Ciudad, Pais) {
        var vm = this;

        vm.ciudad = entity;

        var unsubscribe = $rootScope.$on('pru1App:ciudadUpdate', function(event, result) {
            vm.ciudad = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
