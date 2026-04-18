package com.motionvolt.carcare.adapter.in.web;

import com.motionvolt.carcare.application.port.in.KakaoLoginUseCase;
import com.motionvolt.carcare.application.port.in.ReservationUseCase;
import com.motionvolt.carcare.domain.model.KakaoUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Instant;

@Controller
@SessionAttributes({"centerId", "date", "optionId"})
public class ReservationController {
    private final ReservationUseCase reservationUseCase;
    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final String kakaoClientId;
    private final String kakaoRedirectUri;

    public ReservationController(ReservationUseCase reservationUseCase,
                                 KakaoLoginUseCase kakaoLoginUseCase,
                                 @Value("${kakao.client-id}") String kakaoClientId,
                                 @Value("${kakao.redirect-uri}") String kakaoRedirectUri) {
        this.reservationUseCase = reservationUseCase;
        this.kakaoLoginUseCase = kakaoLoginUseCase;
        this.kakaoClientId = kakaoClientId;
        this.kakaoRedirectUri = kakaoRedirectUri;
    }

    @GetMapping("/centers/{centerId}/options/{optionId}/date")
    public String date(@PathVariable int centerId,
                       @PathVariable int optionId,
                       Model model) {
        model.addAttribute("id", centerId);
        model.addAttribute("optionId", optionId);
        return "date";
    }

    @PostMapping({"/date", "/date.car"})
    public String legacyDate(@RequestParam("id") int centerId,
                             @RequestParam int optionId,
                             Model model) {
        return date(centerId, optionId, model);
    }

    @PostMapping("/reservations/start")
    public String userByReservationStart(@RequestParam("id") int centerId,
                                         @RequestParam int optionId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                         Model model) {
        return user(centerId, optionId, date, model);
    }

    @PostMapping({"/user", "/user.car"})
    public String user(@RequestParam("id") int centerId,
                       @RequestParam int optionId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Model model) {
        model.addAttribute("centerId", centerId);
        model.addAttribute("date", date);
        model.addAttribute("optionId", optionId);
        model.addAttribute("kakaoClientId", kakaoClientId);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);
        return "user";
    }

    @GetMapping({"/oauth/kakao/callback", "/kakao", "/kakao.car"})
    public String kakao(@RequestParam String code, Model model) {
        KakaoUser user = kakaoLoginUseCase.login(code);
        model.addAttribute("kakaoId", user.getId());
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("connected_at", user.getConnectedAt().toString());
        return "testdrive";
    }

    // SPIKE: development-only login bypass for local demos without Kakao OAuth setup.
    @GetMapping({"/dev/kakao-login", "/demo/kakao"})
    public String demoKakao(Model model) {
        model.addAttribute("kakaoId", 9000000001L);
        model.addAttribute("nickname", "샘플예약자");
        model.addAttribute("connected_at", Instant.now().toString());
        return "testdrive";
    }

    @PostMapping({"/reservations", "/testdrive", "/testdrive.car"})
    public String reserve(@RequestParam long kakaoId,
                          @RequestParam int centerId,
                          @RequestParam int optionId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          RedirectAttributes redirectAttributes) {
        boolean reserved = reservationUseCase.reserve(centerId, kakaoId, optionId, date);
        redirectAttributes.addFlashAttribute("message", reserved ? "시승 신청이 완료되었습니다." : "시승 신청에 실패했습니다.");
        return "redirect:/main";
    }
}
