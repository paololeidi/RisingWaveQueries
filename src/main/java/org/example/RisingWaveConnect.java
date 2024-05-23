package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class RisingWaveConnect {

    public static void main (String arg[]) throws SQLException{
        String url = "jdbc:postgresql://localhost:4566/dev";
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "secret");
        props.setProperty("ssl", "false");
        Connection conn = DriverManager.getConnection(url, props);

        String sqlQuery =
                """
                        CREATE TABLE exam_scores (
                                  score_id int,
                                  exam_id int,
                                  student_id int,
                                  score real,
                                  exam_date date
                                );
                """
                ;

        String sqlQuery2 =
                """
                        INSERT INTO exam_scores (score_id, exam_id, student_id, score, exam_date)
                                VALUES
                                  (1, 101, 1001, 85.5, '2022-01-10'),
                                  (2, 101, 1002, 92.0, '2022-01-10'),
                                  (3, 101, 1003, 78.5, '2022-01-10'),
                                  (4, 102, 1001, 91.2, '2022-02-15'),
                                  (12, 102, 1003, 88.9, '2022-02-15');
                """
                ;

        String sqlQuery3 =
                """
                        CREATE SOURCE stressStream (timestamp timestamp, id int, status varchar, stressLevel int)
                        WITH (
                           connector = 'kafka',
                           topic = 'stress',
                           properties.bootstrap.server = '10.56.117.15:9092',
                           scan.startup.mode = 'latest'
                        ) FORMAT PLAIN ENCODE CSV (
                           without_header = 'true',
                           delimiter = ','
                        );
                """
                ;

        String sqlQuery4 =
                """
                        SELECT * FROM stressStream;
                """
                ;

        String sqlQuery5 =
                """
                        SELECT window_start, window_end, id, max(stressLevel) as max_stress
                        FROM TUMBLE (stressStream, timestamp, INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        PreparedStatement st = conn.prepareStatement(sqlQuery5); //Define a query and pass it to a PreparedStatement object.
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            String windowStart = rs.getTimestamp("window_start").toString().replace(".0","").replace(":00","");
            String windowEnd = rs.getTimestamp("window_end").toString().replace(".0","").replace(":00","");
            String id = String.valueOf(rs.getInt("id"));
            String maxStress = String.valueOf(rs.getInt("max_stress"));
            String l = windowStart + ',' + windowEnd + ',' + id + ',' + maxStress;
            System.out.println(l);
            try {
                FileWriter csvWriter = new FileWriter("output.csv",true);
                csvWriter.append(l); // Writing the transformed string to the CSV file
                csvWriter.append("\n");
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }

        
        conn.close();
    }
}