'use strict';

angular.module('scrutmydocsApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ui.select2',
  'ui.bootstrap',
  'rxI18n',
  'smartTable.table'
])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    })
    .factory('Message', function(i18nUtils) {
        var getMessage = function(key) {
            return i18nUtils.translate(key);
        };
        return {
            error: function(key, params) {
                var message = getMessage(key);
                alertify.error(message);
                console.log(message, params);
            },
            success: function(key) {
                var message = getMessage(key);
                alertify.success(message);
            },
            dialog: {
                alert: function (key) {
                    alertify.alert(getMessage(key));
                },
                confirm: function (key, handler) {
                    alertify.confirm(getMessage(key), handler);
                },
                prompt: function (key, handler) {
                    alertify.prompt(getMessage(key), handler);
                }
            }
        }
    })
;
