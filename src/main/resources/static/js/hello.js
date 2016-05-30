//angular.module('hello', []).controller('home', function($http) {
//	var self = this;
//	$http.get('resource/').then(function(response) {
//		self.greeting = response.data;
//	})
//});


angular.module('hello', [])
  .controller('home', function() {
    this.greeting = {id: 'xxx', content: 'Hello World!'}
})