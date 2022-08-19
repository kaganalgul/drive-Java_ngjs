var app = angular.module("drive-app",
    [
        'ngRoute',
        'ngResource',
        'ui.bootstrap',
        'ngFileUpload',
        'filesize',
        'timeago'
    ]);

    app.config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);

        $routeProvider
            .when('/login', {
                template: '<login></login>'
            })
            .when('/', {
                template: '<login></login>'
            })
            .when('/register', {
                template: '<register></register>'
             })
             .when('/home', {
                template: '<home></home>'
             })
             .when('/shared-files-me', {
                template: '<shared-files-me></shared-files-me>'
             })
             .when('/shared-files-others', {
                template: '<shared-files-others></shared-files-others>'
             })
            .otherwise({
                redirectTo: '/'
            })
    })

    app.factory('AccountApi', ['$resource', function($resource) {
        var baseUrl = '/user';

        return $resource('/', { }, {
            login: {
                method: 'POST',
                url: baseUrl + '/login'
            },
            register: {
                method: 'POST',
                url: baseUrl + '/register'
            }
        })
    }])

    app.factory('FileApi', ['$resource', function($resource) {
        var baseUrl= '/file';

        return $resource('/:id', { id: '@id'}, {
            upload: {
                method: "POST",
                url: baseUrl + "/upload",
                transformRequest: function (data) {
                    let file = data.file;
                    var fd = new FormData();
                    fd.append("file", file);
                    return fd;
                },
                headers: { "Content-Type": undefined }
            },
            list: {
                method: "GET",
                url: baseUrl + "/list",
                isArray: true
            },
            deleteMain: {
                method: 'POST',
                url: baseUrl + '/delete-main/:id'
            },
            deleteSharedMe: {
                method: 'POST',
                url: baseUrl + '/delete-shared-me/:id'
            },
            share: {
                method: 'POST',
                url: baseUrl + '/share/:id'
            },
            sharedFilesMe: {
                method: 'GET',
                url: baseUrl + '/shared-files-me',
                isArray: true
            },
            sharedFilesOthers: {
                method: 'GET',
                url: baseUrl + '/shared-files-others',
                isArray: true
            },
            deleteFromOthers: {
                method: 'POST',
                url: baseUrl + '/delete-from-others/:id'
            },
            sharedList: {
                method: 'GET',
                url: baseUrl + '/share-list/:id',
                isArray: true
            } 
        })
    }])


    app.factory('AccountService', function() {
        let user;
        let loggedIn = false;
        let observers = [];

        function setUser(user1) {
            user = user1;
            for(let i = 0; i < observers.length; i++) {
                observers[i](user);
            }
        }

        function getUser() {
            return user;
        }

        function getLoggedIn() {
            return loggedIn;
        }

        function setLoggedIn() {
            loggedIn = l;
            for(let i = 0; i < observers.length; i++) {
                observers[i](l);
            }
        }

        function addObserver(o) {
            observers.push(o);
        }

        return {
            setL : setLoggedIn,
            gelL : getLoggedIn,
            addO : addObserver,
            setU : setUser,
            getU : getUser
        }
    });

    app.controller("MenuController", function ($scope, AccountService) {

        let pathsWithMenu = ['<home></home>', '<shared-files-me></shared-files-me>', '<shared-files-others></shared-files-other>'];
        
        $scope.setUser = function(username){
            $scope.user.email = username;
            angular.element("#menu").show();
        }
        
    
        $scope.init = function () {
            
            $scope.user = {email: ''};

            $scope.showMenu = false;
            
            AccountService.addO($scope.setUser);
            
            if(current_user != null) {
                $scope.setUser(current_user.email);
            }

            $scope.$on('$routeChangeSuccess', function ($event, current, prev) {
                // ... you could trigger something here ...
                if (pathsWithMenu.includes(current.template)) {
                    $scope.showMenu = true;
                }
    
            });
    
        };
    
        $scope.init();
    });