angular.module('starter.filters', [])
	.filter('capitalize', function() {
	  return function(input, scope) {
	    if (input!=null)
	    input = input.toLowerCase();
	    return input.substring(0,1).toUpperCase()+input.substring(1);
	  }
	})
	.filter('split', function() {
		return function(input, splitChar, splitIndex) {
			var value = input.split(splitChar);
			if (value.length >= (splitIndex + 1)) {
				return value[splitIndex];
			}
		}
	})
	.filter('cpfFilter', function() {
		return function(input) {
			var str = input + '';
			if (str.length <= 11) {
				str = str.replace(/\D/g, '');
				str = str.replace(/(\d{3})(\d)/, '$1.$2');
				str = str.replace(/(\d{3})(\d)/, '$1.$2');
				str = str.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
			}
			return str;
		};
	})
	.filter('telefoneFilter', function (){
		return function (input) {
			var str = input + '';
			var regex10 = /^([0-9]{2})([0-9]{4})([0-9]{4})$/;
			var regex11 = /^([0-9]{2})([0-9]{5})([0-9]{4})$/;
			var parts = null;
			
			if(input){
				if (str.length == 10) {
					parts = input.match(regex10);
				} else {
					parts = input.match(regex11);
				}
				if (parts == null) {
					return str;
				}
				return '(' + parts[1] + ') ' + parts[2] + '-' + parts[3];
			}else{
				return '';
			}
		}
	});
  	

	
	
