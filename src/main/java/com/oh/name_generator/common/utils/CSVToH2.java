package com.oh.name_generator.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Slf4j
public class CSVToH2 {

    @Value("${spring.datasource.url}")
    private String jdbcURL;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${path.csv_file}")
    private String csvFilePath;

    public void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8))) {

            String line;
            String sql = "INSERT INTO GEN.CF_NAME_STATS (YEAR_RANK, NAME, YEAR_COUNT, GENDER, YEARS, TOTAL_RANK) VALUES (?, ?, ?, ?, ?, 0)";
            PreparedStatement statement = connection.prepareStatement(sql);

            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                log.info("==1== {}", values[0]);
                log.info("==2== {}", values[1]);
                log.info("==3== {}", values[3]);
                log.info("==4== {}", values[4]);
                log.info("==5== {}", values[5]);

                statement.setInt(1, Integer.parseInt(values[0]));
                statement.setString(2, values[1]);
                statement.setInt(3, Integer.parseInt(values[3]));
                statement.setString(4, values[4]);
                statement.setInt(5, Integer.parseInt(values[5]));

                statement.executeUpdate();
            }

            log.info("CSV 데이터를 성공적으로 삽입했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
