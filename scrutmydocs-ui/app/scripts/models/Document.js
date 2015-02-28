'use strict';

angular.module('rxDocument', [
        'ngResource'
    ])

    .factory('Document', function ($resource, $http) {
        var res = $resource('/api/2/document/_search',
          {'id': '@_id'},
          {update: {method:'PUT'}, 'get':  {method:'GET', isArray:false}});

        return  angular.extend(res,
            {
                findAll: function(callback) {
                    $http.get('/api/2/document/_search')
                        .success(function(data) {
                            if (callback) {
                                callback(data);
                            }
                        });
                }
            });
    }
);
