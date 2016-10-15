(function() {
    'use strict';

    angular
        .module('pru1App')
        .controller('CiudadDialogController', CiudadDialogController);

    CiudadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ciudad', 'Pais'];

    function CiudadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ciudad, Pais) {
        var vm = this;

        vm.ciudad = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pais = Pais.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ciudad.id !== null) {
                Ciudad.update(vm.ciudad, onSaveSuccess, onSaveError);
            } else {
                Ciudad.save(vm.ciudad, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pru1App:ciudadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
