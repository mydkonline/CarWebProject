document.addEventListener("DOMContentLoaded", function () {
	document.querySelectorAll(".js-center-card").forEach(function (card) {
		card.addEventListener("click", function () {
			window.location.href = "/centers/"
				+ encodeURIComponent(card.dataset.id)
				+ "/options/"
				+ encodeURIComponent(card.dataset.optionId)
				+ "/date";
		});
	});
});
