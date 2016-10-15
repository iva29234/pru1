(function() {
    'use strict';

    angular
        .module('pru1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pais', {
            parent: 'entity',
            url: '/pais?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pru1App.pais.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pais/pais.html',
                    controller: 'PaisController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pais');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pais-detail', {
            parent: 'entity',
            url: '/pais/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pru1App.pais.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pais/pais-detail.html',
                    controller: 'PaisDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pais');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pais', function($stateParams, Pais) {
                    return Pais.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('pais.new', {
            parent: 'pais',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pais/pais-dialog.html',
                    controller: 'PaisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                paisId: null,
                                paisNombre: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pais', null, { reload: true });
                }, function() {
                    $state.go('pais');
                });
            }]
        })
        .state('pais.edit', {
            parent: 'pais',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pais/pais-dialog.html',
                    controller: 'PaisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pais', function(Pais) {
                            return Pais.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pais', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pais.delete', {
            parent: 'pais',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pais/pais-delete-dialog.html',
                    controller: 'PaisDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pais', function(Pais) {
                            return Pais.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pais', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
