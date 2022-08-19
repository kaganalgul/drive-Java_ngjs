angular.module('drive-app')
.component('sharedFilesOthers', {
    templateUrl: '/app/template/shared-files-others.html',
    controller: function($scope, FileApi, $uibModal) {
        $scope.init = function() {
            $scope.files = FileApi.sharedFilesOthers();
            $scope.paging = {
                currentPage : 1,
                pageSize : 10
            }
        }

        $scope.showModal = function (file) {
            var modalInstance = $uibModal.open({
            templateUrl: '/app/template/delete-modal.html',
            controller: 'DeleteModalController',
            size: 'md',
            resolve: {
                File: function () {
                       file = angular.copy(file);
                       file.sharedList = '';
                      return file;
                }
            }
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

        $scope.init();
    }
})

.controller("DeleteModalController", function ($scope, File, $uibModalInstance, FileApi, $window) {

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.deleteFromOthers = function (user) {
        let ret = FileApi.deleteFromOthers({ id: user.id },  $scope.file.id, function (data) {
            $uibModalInstance.close(data);
            $window.location = '/shared-files-others';
        });
        ret.$promise.then(function(){
            _.remove($scope.users, { id: user.id });
        });
    }

    $scope.init = function () {
        let fileId = File.id;
        $scope.file = File;
        console.log($scope.file);
        $scope.users = FileApi.sharedList({id : fileId});
    };
    $scope.init();
});