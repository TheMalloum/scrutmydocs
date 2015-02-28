'use strict';

angular.module('scrutmydocsApp')
  .controller('MainCtrl', function ($scope, $http) {
    $scope.query="";

    $scope.search = function() {
        $http.post('/api/2/document/_search', {
          "search": $scope.query
        }).
          success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $scope.result=data;
          }).
          error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
          });
    };

    $scope.search();
  });
