angular.module('drive-app')
.component('home', {
    templateUrl: '/app/template/home.html',
    controller: function($scope, FileApi, $location, $window, $uibModal) {

        $scope.upload = function() {
            FileApi.upload({file: $scope.file}, 
                function() {
                    toastr.info("Dosya Başarıyla Yüklendi")
                $window.location = '/home';
            })
        };

        $scope.showModal = function (file) {
             var modalInstance = $uibModal.open({
             templateUrl: '/app/template/share-modal.html',
             controller: 'ShareModalController',
             size: 'md',
             resolve: {
                 File: function () {
                        file = angular.copy(file);
                        file.sharedList = '';
                       return file;
                 }
             }
             });

             modalInstance.result.then(function() {
                $location.path('/shared-files-others');
             })
        }

        $scope.deleteMain = function (file) {
            let ret = FileApi.deleteMain({ id: file.id });
            ret.$promise.then(function(){
                _.remove($scope.files, { id: file.id });
            });
        }

        $scope.download = function(file) {
            let name = file.name;
            FileApi.download({name},
                function() {
                    toastr.info("Dosya indirme başarılı");
                    $location.path('/home')
                })
        }

        $scope.init = function(){
            $scope.files = FileApi.list();
            $scope.file = { };
            $scope.paging = {
                currentPage : 1,
                pageSize : 10
            };
        };
        $scope.init();
    }
})

    .controller("ShareModalController", function ($scope, File, $uibModalInstance, FileApi, $window) {

        $scope.share = function () {
            $scope.file.sharedList = $scope.file.sharedList.split(",");
            FileApi.share({id: $scope.file.id}, {users: $scope.file.sharedList}, function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.init = function () {
            $scope.file = File;
        };
        $scope.init();
    });