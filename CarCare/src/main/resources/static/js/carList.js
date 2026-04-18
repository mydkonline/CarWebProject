document.addEventListener("DOMContentLoaded", function () {
	document.querySelectorAll(".js-car-card").forEach(function (card) {
		card.addEventListener("click", function () {
			window.location.href = "/cars/" + encodeURIComponent(card.dataset.id) + "/options";
		});

		card.addEventListener("keydown", function (event) {
			if (event.key === "Enter" || event.key === " ") {
				event.preventDefault();
				card.click();
			}
		});
	});
});
