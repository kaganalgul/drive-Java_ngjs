angular.module('drive-app')
.component('sharedFilesMe', {
    templateUrl: '/app/template/shared-files-me.html',
    controller: function($scope, FileApi) {
        $scope.init = function() {
            $scope.files = FileApi.sharedFilesMe();
            $scope.paging = {
                currentPage : 1,
                pageSize : 10
            }
        }

        $scope.deleteSharedMe = function (file) {
            let ret = FileApi.deleteSharedMe({ id: file.id });
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

        $scope.init();
    }
})