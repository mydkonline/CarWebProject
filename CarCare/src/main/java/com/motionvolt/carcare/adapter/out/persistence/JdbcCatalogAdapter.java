package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.application.port.out.CatalogPort;
import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.CarSummary;
import com.motionvolt.carcare.domain.model.Center;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCatalogAdapter implements CatalogPort {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCatalogAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CarSummary> findCars() {
        return jdbcTemplate.query(
                "SELECT * FROM car_list_view",
                (rs, rowNum) -> new CarSummary(rs.getInt("id"), rs.getString("brand"), rs.getString("model"))
        );
    }

    @Override
    public List<CarOption> findOptions(int carId) {
        return jdbcTemplate.query(
                "SELECT * FROM car_option_view WHERE car_id=?",
                (rs, rowNum) -> new CarOption(
                        rs.getInt("id"),
                        rs.getInt("car_id"),
                        rs.getString("color"),
                        rs.getInt("cc"),
                        rs.getInt("km"),
                        rs.getString("price"),
                        rs.getString("grade")
                ),
                carId
        );
    }

    @Override
    public List<Center> findCenters() {
        return jdbcTemplate.query(
                "SELECT * FROM centers",
                (rs, rowNum) -> new Center(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("number"),
                        rs.getString("address")
                )
        );
    }
}
