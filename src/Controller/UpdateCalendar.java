package Controller;

import Model.ConnectionDB;
import Model.Reminder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class UpdateCalendar {
    public static void update (ConnectionDB connectionDB, HashMap<LocalDate, ObservableList<Reminder>> dayList) {
        String CauLenh = "SELECT * FROM reminder ";
        ResultSet rs = connectionDB.ThucThiLechSelect(CauLenh);
        try {
            while (rs.next()) {
                int MaSo = rs.getInt("MASO");
                String title = rs.getNString("title");
                LocalDate beginDay = rs.getDate("beginDay").toLocalDate();
                LocalTime beginTime = rs.getTime("beginTime").toLocalTime();
                LocalDate endDay = rs.getDate("endDay").toLocalDate();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                String noiDung = rs.getNString("noiDung");
                Boolean done = rs.getBoolean("done");
                Reminder reminder = new Reminder(MaSo, title, beginDay, beginTime, endDay, endTime, noiDung, done);
                if (dayList.containsKey(beginDay)) {
                    dayList.get(beginDay).add(reminder);
                } else {
                    ObservableList<Reminder> currentDay = FXCollections.observableArrayList();
                    currentDay.add(reminder);
                    dayList.put(beginDay, currentDay);
                }
                Reminder.setMASO(MaSo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
