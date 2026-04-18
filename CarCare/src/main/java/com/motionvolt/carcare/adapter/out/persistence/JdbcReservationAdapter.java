package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveReservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Repository
public class JdbcReservationAdapter implements ReservationPort {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsKakaoUser(long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM kakaouserinfos WHERE id=?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public int saveKakaoUser(KakaoUser user) {
        return jdbcTemplate.update(
                "INSERT INTO kakaouserinfos (id, nickname, connected_at) VALUES (?, ?, ?)",
                user.getId(),
                user.getNickname(),
                Timestamp.from(user.getConnectedAt())
        );
    }

    @Override
    public int saveReservation(TestDriveReservation reservation) {
        return jdbcTemplate.update(
                "INSERT INTO schedule_drive (center_id, kakaouser_id, car_option_id, reservation_date, state) VALUES (?, ?, ?, ?, ?)",
                reservation.getCenterId(),
                reservation.getKakaoUserId(),
                reservation.getCarOptionId(),
                Date.valueOf(reservation.getReservationDate()),
                reservation.isState()
        );
    }
}
