(function() {
    'use strict';

    angular
        .module('pru1App')
        .controller('PaisDetailController', PaisDetailController);

    PaisDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pais', 'Ciudad'];

    function PaisDetailController($scope, $rootScope, $stateParams, entity, Pais, Ciudad) {
        var vm = this;

        vm.pais = entity;

        var unsubscribe = $rootScope.$on('pru1App:paisUpdate', function(event, result) {
            vm.pais = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
