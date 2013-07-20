(function($) {

	var LOWER = /[a-z]/,
		UPPER = /[A-Z]/,
		DIGIT = /[0-9]/,
		DIGITS = /[0-9].*[0-9]/,
		SPECIAL = /[^a-zA-Z0-9]/,
		SAME = /^(.)\1+$/;

	function rating(rate, message) {
		return {
			rate: rate,
			messageKey: message
		};
	}

	function uncapitalize(str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	$.validator.passwordRating = function(password, username) {
		if (!password || password.length < 8)
			return rating(0, "too-short");
		if (username && password.toLowerCase().match(username.toLowerCase()))
			return rating(0, "similar-to-username");
		if (SAME.test(password))
			return rating(1, "very-weak");

		var lower = LOWER.test(password),
			upper = UPPER.test(uncapitalize(password)),
			digit = DIGIT.test(password),
			digits = DIGITS.test(password),
			special = SPECIAL.test(password);

		if (password.length > 13 && (lower && upper && digit || lower && digits && special || upper && digits && special))
			return rating(5, "strong");
		if (password.length > 11 && (lower && upper || lower && digit || upper && digit))
			return rating(4, "good");
        if (lower && upper || lower && digit || upper && digit)
            return rating(3, "ok");
		return rating(2, "weak");
	}

	$.validator.passwordRating.messages = {
		"similar-to-username": "Too similar to username",
		"too-short": "Too short",
		"very-weak": "Very weak",
		"weak": "Weak",
        "ok": "Ok",
		"good": "Good",
		"strong": "Strong"
	}

	$.validator.addMethod("password", function(value, element, usernameField) {
		// use untrimmed value
		var password = element.value,
		// get username for comparison, if specified
			username = $(typeof usernameField != "boolean" ? usernameField : []);

		var rating = $.validator.passwordRating(password, username.val());
		// update message for this field

		var meter = $(".password-meter", element.form);

		meter.find(".password-meter-bar").removeClass().addClass("password-meter-bar").addClass("password-meter-" + rating.messageKey);
		meter.find(".password-meter-message")
		.removeClass()
		.addClass("password-meter-message")
		.addClass("password-meter-message-" + rating.messageKey)
		.text($.validator.passwordRating.messages[rating.messageKey]);
		// display process bar instead of error message

		return rating.rate > 2;
	}, "&nbsp;");
	// manually add class rule, to make username param optional
	$.validator.classRuleSettings.password = { password: true };

})(jQuery);
