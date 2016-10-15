(function() {
    'use strict';

    angular
        .module('pru1App')
        .controller('CiudadDeleteController',CiudadDeleteController);

    CiudadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ciudad'];

    function CiudadDeleteController($uibModalInstance, entity, Ciudad) {
        var vm = this;

        vm.ciudad = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ciudad.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
