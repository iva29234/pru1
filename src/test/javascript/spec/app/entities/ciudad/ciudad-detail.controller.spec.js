'use strict';

describe('Controller Tests', function() {

    describe('Ciudad Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCiudad, MockPais;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCiudad = jasmine.createSpy('MockCiudad');
            MockPais = jasmine.createSpy('MockPais');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ciudad': MockCiudad,
                'Pais': MockPais
            };
            createController = function() {
                $injector.get('$controller')("CiudadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pru1App:ciudadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
