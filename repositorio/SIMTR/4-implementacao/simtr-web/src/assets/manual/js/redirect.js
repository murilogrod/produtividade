$(document).ready(function(){
    $('a').click(function(e){
        e.preventDefault();
        var url = e.currentTarget.href + "index.html"
       window.open(url, '_self');
       return false;
    }); 
 });