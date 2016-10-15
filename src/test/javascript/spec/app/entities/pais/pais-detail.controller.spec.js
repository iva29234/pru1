'use strict';

describe('Controller Tests', function() {

    describe('Pais Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPais, MockCiudad;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPais = jasmine.createSpy('MockPais');
            MockCiudad = jasmine.createSpy('MockCiudad');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pais': MockPais,
                'Ciudad': MockCiudad
            };
            createController = function() {
                $injector.get('$controller')("PaisDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pru1App:paisUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
