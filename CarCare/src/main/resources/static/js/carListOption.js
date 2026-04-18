document.addEventListener("DOMContentLoaded", function () {
	document.querySelectorAll(".js-option-card").forEach(function (card) {
		card.addEventListener("click", function () {
			window.location.href = "/options/" + encodeURIComponent(card.dataset.id) + "/centers";
		});
	});
});
