(function() {
    'use strict';

    angular
        .module('pru1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ciudad', {
            parent: 'entity',
            url: '/ciudad',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pru1App.ciudad.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ciudad/ciudads.html',
                    controller: 'CiudadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ciudad');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ciudad-detail', {
            parent: 'entity',
            url: '/ciudad/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pru1App.ciudad.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ciudad/ciudad-detail.html',
                    controller: 'CiudadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ciudad');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ciudad', function($stateParams, Ciudad) {
                    return Ciudad.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('ciudad.new', {
            parent: 'ciudad',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ciudad/ciudad-dialog.html',
                    controller: 'CiudadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                codCiudad: null,
                                nombre: null,
                                cantidad: null,
                                ano: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ciudad', null, { reload: true });
                }, function() {
                    $state.go('ciudad');
                });
            }]
        })
        .state('ciudad.edit', {
            parent: 'ciudad',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ciudad/ciudad-dialog.html',
                    controller: 'CiudadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ciudad', function(Ciudad) {
                            return Ciudad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ciudad', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ciudad.delete', {
            parent: 'ciudad',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ciudad/ciudad-delete-dialog.html',
                    controller: 'CiudadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ciudad', function(Ciudad) {
                            return Ciudad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ciudad', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
