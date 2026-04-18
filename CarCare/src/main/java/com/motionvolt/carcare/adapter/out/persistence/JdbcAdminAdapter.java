package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.application.port.out.AdminPort;
import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
import com.motionvolt.carcare.domain.model.SelectionOption;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcAdminAdapter implements AdminPort {
    private static final String SELECT_DRIVE_SCHEDULE_VIEW = "SELECT * FROM drive_schedule_view";
    private static final String SELECT_DRIVE_BY_ID = "SELECT * FROM drive_schedule_view WHERE id=?";
    private static final String UPDATE_SCHEDULED_DRIVE_INFO =
            "UPDATE schedule_drive SET reservation_date=?, state=?, car_option_id=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcAdminAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsAdmin(String username, String password) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM admin WHERE username=? AND password=?",
                Integer.class,
                username,
                password
        );
        return count != null && count > 0;
    }

    @Override
    public int createAdmin(String id, String username, String password) {
        return jdbcTemplate.update(
                "INSERT INTO admin(id, username, password) VALUES (?, ?, ?)",
                id,
                username,
                password
        );
    }

    @Override
    public List<DriveSchedule> findDriveSchedules() {
        return jdbcTemplate.query(SELECT_DRIVE_SCHEDULE_VIEW, (rs, rowNum) -> new DriveSchedule(
                rs.getInt("id"),
                rs.getInt("option_id"),
                toLocalDate(rs.getDate("date")),
                rs.getString("model"),
                rs.getString("nickname"),
                rs.getInt("cc"),
                rs.getString("color"),
                rs.getString("grade"),
                rs.getInt("km"),
                rs.getDouble("price"),
                rs.getBoolean("state") ? DriveSchedule.States.RESERVED : DriveSchedule.States.FAILED
        ));
    }

    @Override
    public DriveSchedule findDriveSchedule(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_DRIVE_BY_ID, (rs, rowNum) -> new DriveSchedule(
                    rs.getInt("id"),
                    rs.getInt("option_id"),
                    toLocalDate(rs.getDate("date")),
                    rs.getString("model"),
                    rs.getString("nickname"),
                    rs.getInt("cc"),
                    rs.getString("color"),
                    rs.getString("grade"),
                    rs.getInt("km"),
                    rs.getDouble("price"),
                    rs.getBoolean("state") ? DriveSchedule.States.RESERVED : DriveSchedule.States.FAILED
            ), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int updateDriveSchedule(int id, DriveSchedule schedule) {
        // SPIKE: keep the admin update focused on mutable reservation fields until user/profile editing is separated.
        return jdbcTemplate.update(
                UPDATE_SCHEDULED_DRIVE_INFO,
                Date.valueOf(schedule.getDate()),
                schedule.getState().getValue() ? 1 : 0,
                schedule.getCarId(),
                id
        );
    }

    @Override
    public int deleteDriveSchedule(int id) {
        return jdbcTemplate.update("DELETE FROM schedule_drive WHERE id=?", id);
    }

    @Override
    public List<ProductOption> findProducts() {
        return jdbcTemplate.query("SELECT * FROM car_adminlist_view", (rs, rowNum) -> new ProductOption(
                rs.getInt("id"),
                rs.getInt("car_id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("color"),
                rs.getInt("cc"),
                rs.getInt("km"),
                rs.getDouble("price"),
                rs.getString("grade")
        ));
    }

    @Override
    public int deleteProductOption(int id) {
        return jdbcTemplate.update("DELETE FROM car_options WHERE id=?", id);
    }

    @Override
    public int createBrand(String name) {
        return jdbcTemplate.update("INSERT INTO car_brands (name) VALUES (?)", name);
    }

    @Override
    public int createModel(int brandId, String name) {
        return jdbcTemplate.update("INSERT INTO cars (car_brand_id, name) VALUES (?, ?)", brandId, name);
    }

    @Override
    public int createCarOption(int carId, String color, int cc, int km, double price, String grade) {
        return jdbcTemplate.update(
                "INSERT INTO car_options (car_id, color, cc, km, price, grade) VALUES (?, ?, ?, ?, ?, ?)",
                carId,
                color,
                cc,
                km,
                price,
                grade
        );
    }

    @Override
    public List<SelectionOption> findBrands() {
        return jdbcTemplate.query(
                "SELECT id, name FROM car_brands ORDER BY id",
                (rs, rowNum) -> new SelectionOption(rs.getInt("id"), rs.getString("name"))
        );
    }

    @Override
    public List<SelectionOption> findModels() {
        return jdbcTemplate.query(
                "SELECT id, name FROM car_product_list_view ORDER BY id",
                (rs, rowNum) -> new SelectionOption(rs.getInt("id"), rs.getString("name"))
        );
    }

    private LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
