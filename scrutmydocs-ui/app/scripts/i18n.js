'use strict';

angular.module('rxI18n', [])

    .directive('rxTranslate', function(i18nUtils) {
        return {
            restrict: 'A',
            compile: function(element, attr) {
                element.html(i18nUtils.translate(attr.rxTranslate));
            }
        };
    })

    .factory('i18nUtils', function() {
        var factory =  {
            translate: function(key) {
                var label = window.labels[key];
                return label ? label : key;
            }
        };
        return factory;
    })
;