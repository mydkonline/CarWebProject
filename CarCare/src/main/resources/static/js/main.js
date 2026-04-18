document.addEventListener("DOMContentLoaded", function () {
	const bannerSwiper = new Swiper(".swiper.banner-swiper", {
        loop: true,
        autoplay: { delay: 3000 },
    });

	// Content Swiper: 메인 컨텐츠를 위한 Swiper 설정
    const contentSwiper = new Swiper(".swiper.content-swiper", {
        loop: true,
        mousewheel: { invert: true },
        autoplay: {
            delay: 2000,
            reverseDirection: true,
        },
        slidesPerView: 4,
        freeMode: true,
        spaceBetween: 20,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
        pagination: {
            el: ".swiper-pagination",
            clickable: true,
        },
        breakpoints: {
            480: { slidesPerView: 1 },
            768: { slidesPerView: 2 },
            1024: { slidesPerView: 3 },
        },
        watchSlidesProgress: true,
    });

    // Banner Swiper: 배너 이미지를 위한 Swiper 설정
    
    //마우스 이벤트 적용
    document.querySelectorAll(".swiper").forEach(function (el) {
        el.addEventListener("mouseover", function () {
            // 현재 마우스가 오버된 Swiper의 autoplay 중지
            if (el.classList.contains('content-swiper')) {
                contentSwiper.autoplay.stop();
            } else if (el.classList.contains('banner-swiper')) {
                bannerSwiper.autoplay.stop();
            }
        });
        el.addEventListener("mouseout", function () {
            // 현재 마우스가 벗어난 Swiper의 autoplay 시작
            if (el.classList.contains('content-swiper')) {
                contentSwiper.autoplay.start();
            } else if (el.classList.contains('banner-swiper')) {
                bannerSwiper.autoplay.start();
            }
        });
    });
});
