package com.learner.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RestController
public class PostgresqlApplication {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/actorsname")
	public List<String> getActorsName(@RequestParam(name = "name") String name){
		PreparedStatementSetter pss = ps -> ps.setString(1,name);
		return 	jdbcTemplate.query("SELECT * FROM actor where first_name=?", pss , new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("first_name") +" "+ rs.getString("last_name");
			}
		});
	}

	@GetMapping("/actors")
	public List<Actor> getActors(@RequestParam(name = "name")String name){
		PreparedStatementSetter preparedStatementSetter = ps -> ps.setString(1, name);
		return jdbcTemplate.query("SELECT * FROM actor WHERE last_name = ?",preparedStatementSetter, (rs, rowNum) -> {
			Actor actor = new Actor();
			actor.setId(rs.getInt(1));
			actor.setFirstName(rs.getString(2));
			actor.setLastName(rs.getString(3));
			actor.setLastUpdate(rs.getDate(4));
			return actor;
		});
	}


	public static void main(String[] args) {
		SpringApplication.run(PostgresqlApplication.class, args);
	}
}
