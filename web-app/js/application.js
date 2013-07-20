
var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}

$.validator.addMethod("validateRegex", function(value, element, pattern) {
    var regex = new RegExp(pattern);
    var isMatch = regex.test(value);
    return isMatch;
});

$(document).ready(function() {
    if (jQuery('.errors').size() > 0) {
        jQuery('input.errors:first').focus();
        jQuery('.errors input:visible:first').focus();
    } else {
        jQuery('input:text:visible:first').focus();
    }
});
