package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class RisingWaveConnect {

    // true for queries 1-3, false for 4-6
    private static final boolean QUERY_RESULT_FORMAT1 = false;
    private static final boolean QUERY_RESULT_FORMAT_JOIN = false;

    public static void main (String arg[]) throws SQLException{
        String url = "jdbc:postgresql://localhost:4566/dev";
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "secret");
        props.setProperty("ssl", "false");
        Connection conn = DriverManager.getConnection(url, props);

        String sqlQuery1 =
                """
                        SELECT window_start, window_end, MAX(stressLevel) AS max_stress
                                FROM TUMBLE (stressStream, timestamp, INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery2 =
                """
                        SELECT window_start, window_end, MAX(stressLevel) AS max_stress
                                FROM HOP (stressStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery3 =
                """
                        SELECT window_start, window_end, id, MAX(stressLevel) AS max_stress
                        FROM TUMBLE (stressStream, timestamp, INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery4 =
                """
                        SELECT window_start, window_end, id, MAX(stressLevel) AS max_stress
                        FROM HOP (stressStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery5 =
                """
                        SELECT
                                    window_start, window_end, id,
                                    MAX(stressLevel) AS maxStress
                                FROM (
                                    SELECT
                                        *,
                                        first_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_start,
                                        last_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_end
                                    FROM stressStream
                                )
                                GROUP BY id, window_start, window_end
                                ORDER BY id, window_start, window_end;
                """
                ;


        String sqlQuery6 =
                """
                        SELECT window_start, window_end, MIN(stressLevel) AS min_stress
                                FROM TUMBLE (stressStream, timestamp, INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery7 =
                """
                        SELECT window_start, window_end, MIN(stressLevel) AS min_stress
                                FROM HOP (stressStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery8 =
                """
                        SELECT window_start, window_end, id, MIN(stressLevel) AS min_stress
                        FROM TUMBLE (stressStream, timestamp, INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery9 =
                """
                        SELECT window_start, window_end, id, MIN(stressLevel) AS min_stress
                        FROM HOP (stressStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery10 =
                """
                        SELECT
                                window_start, window_end, id,
                                MIN(stressLevel) AS minStress
                                FROM (
                                    SELECT
                                        *,
                                        first_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_start,
                                        last_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_end
                                    FROM stressStream
                                )
                                GROUP BY id, window_start, window_end
                                ORDER BY id, window_start, window_end;
                """
                ;

        String sqlQuery11 =
                """
                        SELECT window_start, window_end, AVG(weight) AS avg_weight
                                FROM TUMBLE (weightStream, timestamp, INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery12 =
                """
                        SELECT window_start, window_end, AVG(weight) AS avg_weight
                                FROM HOP (weightStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery13 =
                """
                        SELECT window_start, window_end, id, AVG(weight) AS avg_weight
                        FROM TUMBLE (weightStream, timestamp, INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery14 =
                """
                        SELECT window_start, window_end, id, AVG(weight) AS avg_weight
                        FROM HOP (weightStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery15 =
                """
                        SELECT
                                window_start, window_end, id,
                                AVG(weight) AS avgStress
                                FROM (
                                    SELECT
                                        *,
                                        first_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_start,
                                        last_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_end
                                    FROM weightStream
                                )
                                GROUP BY id, window_start, window_end
                                ORDER BY id, window_start, window_end;
                """
                ;

        String sqlQuery16 =
                """
                        SELECT window_start, window_end, COUNT(*) AS numberOfEvents
                                FROM TUMBLE (weightStream, timestamp, INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery17 =
                """
                        SELECT window_start, window_end, COUNT(*) AS numberOfEvents
                                FROM HOP (weightStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                                GROUP BY window_start, window_end
                                ORDER BY window_start ASC;
                """
                ;

        String sqlQuery18 =
                """
                        SELECT window_start, window_end, id, COUNT(*) AS numberOfEvents
                        FROM TUMBLE (weightStream, timestamp, INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery19 =
                """
                        SELECT window_start, window_end, id, COUNT(*) AS numberOfEvents
                        FROM HOP (weightStream, timestamp, INTERVAL '5 SECONDS', INTERVAL '10 SECONDS')
                        GROUP BY window_start, window_end, id
                        ORDER BY window_start ASC;
                """
                ;

        String sqlQuery20 =
                """
                        SELECT
                                window_start, window_end, id,
                                COUNT(*) AS numberOfEvents
                                FROM (
                                    SELECT
                                        *,
                                        first_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_start,
                                        last_value(timestamp) OVER (
                                            PARTITION BY id ORDER BY timestamp
                                            SESSION WITH GAP INTERVAL '3 SECONDS'
                                        ) AS window_end
                                    FROM weightStream
                                )
                                GROUP BY id, window_start, window_end
                                ORDER BY id, window_start, window_end;
                """
                ;


        String sqlQueryJoin =
                """
                       SELECT
                           s.id as id,
                           s.timestamp as stressTs,
                           w.timestamp as weightTs,
                           s.status as status,
                           s.stressLevel as stressLevel,
                           w.timestamp as weightTS,
                           w.weight as weight
                       FROM stressStream s JOIN weightStream w
                       ON s.id = w.id AND
                       w.timestamp between s.timestamp - INTERVAL '5' SECOND and s.timestamp + INTERVAL '5' SECOND;
               """
                ;

        PreparedStatement st = conn.prepareStatement(sqlQuery20); //Define a query and pass it to a PreparedStatement object.
        ResultSet rs = st.executeQuery();

        while (rs.next()) {

            String l = "";
            if (QUERY_RESULT_FORMAT1){
                String windowStart = rs.getTimestamp("window_start").toString().replace(".0","");
                String windowEnd = rs.getTimestamp("window_end").toString().replace(".0","");
                l = windowStart + ',' + windowEnd + ',';
                String avgStress = String.valueOf(rs.getInt("numberOfEvents"));
                l = l + avgStress;
            } else if (QUERY_RESULT_FORMAT_JOIN){
                String stressTs = rs.getTimestamp("stressTs").toString().replace(".0","");
                String id = String.valueOf(rs.getInt("id"));
                String stressLevel = String.valueOf(rs.getInt("stressLevel"));
                String status = rs.getString("status");
                String weightTS = rs.getTimestamp("weightTS").toString().replace(".0","");
                String weight = String.valueOf(rs.getDouble("weight"));
                l= stressTs + ',' + id + ',' + status + ',' + stressLevel + ',' + weightTS + ',' + weight;
            } else {
                String windowStart = rs.getTimestamp("window_start").toString().replace(".0","");
                String windowEnd = rs.getTimestamp("window_end").toString().replace(".0","");
                l = windowStart + ',' + windowEnd + ',';
                String id = String.valueOf(rs.getInt("id"));
                String maxStress = String.valueOf(rs.getInt("numberOfEvents"));
                l = l +  id + ',' + maxStress;
            }
            System.out.println(l);
            try {
                FileWriter csvWriter = new FileWriter("Files/Output/output20.csv",true);
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