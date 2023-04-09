package Controller;

import Model.Reminder;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Scene2 {
    private Scene1 control1;
    public void setControl1 (Scene1 control1) {
        this.control1 = control1;
    }
    public AnchorPane main;
    public Button Save, Clear;
    public Label Begin, End, Title, Content;
    public DatePicker beginDay, endDay;
    public Spinner<LocalTime> beginTime;
    public Spinner<LocalTime> endTime;
    public TextField title;
    public TextArea content;
    public CheckBox done;

    public Label Warning;

    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
    StringConverter<LocalDate> localDate = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dayFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dayFormatter);
            } else {
                return null;
            }
        }
    };

    SpinnerValueFactory<LocalTime> valueFactory1 = new SpinnerValueFactory<LocalTime>() {
        @Override
        public void decrement(int i) {
            if (getValue() == null) {
                LocalTime now = LocalTime.now();
                setValue(LocalTime.parse(timeFormatter.format(now)));
            } else {
                LocalTime localTime = (LocalTime) getValue();
                setValue(localTime.minusMinutes(1));
            }
        }

        @Override
        public void increment(int i) {
            if (getValue() == null) {
                LocalTime now = LocalTime.now();
                setValue(LocalTime.parse(timeFormatter.format(now)));
            } else {
                LocalTime localTime = (LocalTime) getValue();
                setValue(localTime.plusMinutes(1));
            }
        }
    };

    SpinnerValueFactory<LocalTime> valueFactory2 = new SpinnerValueFactory<LocalTime>() {
        @Override
        public void decrement(int i) {
            if (getValue() == null) {
                LocalTime now = LocalTime.now();
                setValue(LocalTime.parse(timeFormatter.format(now)));
            } else {
                LocalTime localTime = (LocalTime) getValue();
                setValue(localTime.minusMinutes(1));
            }
        }

        @Override
        public void increment(int i) {
            if (getValue() == null) {
                LocalTime now = LocalTime.now();
                setValue(LocalTime.parse(timeFormatter.format(now)));
            } else {
                LocalTime localTime = (LocalTime) getValue();
                setValue(localTime.plusMinutes(1));
            }
        }
    };

    public void setBeginDay (LocalDate localDate) {
        this.beginDay.setValue(localDate);
        this.beginDay.setDisable(true);
    }
    public void setForm () {
        beginDay.setConverter(localDate);
        endDay.setConverter(localDate);
        beginTime.setValueFactory(valueFactory1);
        endTime.setValueFactory(valueFactory2);
    }
    public void initialize () {
        setForm();
    }

    private static int menu;

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public void Save (Event event) {
        Reminder newReminder = new Reminder();
        String str0 = "", str1 = "", str2 = "", str3 = "";
        if (!newReminder.setTitle(title.getText())) {
            str0 = "Chua dien title ";
        }
        if (!newReminder.setBeginDay(beginDay.getValue())) {
            str1 = "Ngay bat dau sai ";
        }
        if (!newReminder.setEndDay(endDay.getValue())) {
            str2 = "Ngay ket thuc sai ";
        } else {
            if (!newReminder.setBeginTime(beginTime.getValue())) {
                str3 = "Thoi gian bat dau sai ";
            }

            if (!newReminder.setEndTime(endTime.getValue())) {
                str3 = "Thoi gian ket thuc sai ";
            }
        }

        String str = str0+str1+str2+str3;
        if (str.equals("")) {
            newReminder.setContent(content.getText());
            newReminder.setDone(done.isSelected());
            if (menu<0) {
                control1.add(newReminder);
            } else {
                control1.Sua(newReminder, menu);
            }
            Warning.setText("");
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Clear();
            stage.close();
        } else {
            Warning.setText(str);
        };
    }


    public void Edit (Reminder reminder, int index) {
        setMenu(index);
        beginDay.setValue(reminder.getBeginDay());
        beginTime.getValueFactory().setValue(reminder.getBeginTime());
        endDay.setValue(reminder.getEndDay());
        endTime.getValueFactory().setValue(reminder.getEndTime());
        title.setText(reminder.getTitle());
        done.setSelected(reminder.getDone());
        content.setText(reminder.getContent());
    }

    public void Clear () {
        title.setText("");
        endDay.setValue(null);
        beginTime.getValueFactory().setValue(null);
        endTime.getValueFactory().setValue(null);
        done.setSelected(false);
        content.setText("");
    }

    public void Delete () {
    }
}
