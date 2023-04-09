package Controller;

import Model.ConnectionDB;
import Model.Reminder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Scene1 {
    public Scene2 control2;
    public Stage stage2;
    public void setControl2 (Stage stage2, Scene2 control2) {
        this.stage2 = stage2;
        this.control2 = control2;
    }
    private ConnectionDB connectionDB;

    public void setConnectionDB(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }
    @FXML
    public GridPane lich = new GridPane();
    @FXML
    public AnchorPane main;
    public Label MONTH, YEAR, DAY;
    @FXML
    public Label SUN, MON, TUE, WED, THU, FRI, SAT;
    private final LocalDate date = LocalDate.now();
    private int month = date.getMonth().getValue(), year = date.getYear(), day = date.getDayOfMonth();
    final private int curMONTH = date.getMonth().getValue(), curYEAR = date.getYear(), curDAY = date.getDayOfMonth();
    @FXML
    public Button Left, Right, Lleft, Rright;

    public Button add, delete;
    DateTimeFormatter m = DateTimeFormatter.ofPattern("MMM");


    public void right () {
        month = month%12 + 1;
        hienThiLich(month, year);
    }

    public void left () {
        month = (month+10)%12 + 1;
        hienThiLich(month, year);
    }

    public void rright () {
        year += 1;
        hienThiLich(month, year);
    }

    public void lleft () {
        year -= 1;
        hienThiLich(month, year);
    }


    public TableView nhacNho = new TableView();
    public HashMap<LocalDate, ObservableList<Reminder>> dayList = new HashMap<LocalDate, ObservableList<Reminder>>();
    public TableColumn<Reminder, LocalTime> beginTime;
    public TableColumn<Reminder, String> title;
    public TableColumn<Reminder, Boolean> status;
    public TableColumn<Reminder, LocalDate> endDay;
    public TableColumn<Reminder, LocalTime> endTime;
    public Label ngayHienTai;

    public void setTableView () {
        Callback<TableColumn<Reminder, Boolean>, TableCell<Reminder, Boolean>> cellFactory = new Callback<TableColumn<Reminder, Boolean>, TableCell<Reminder, Boolean>>() {
            @Override
            public TableCell<Reminder, Boolean> call(TableColumn<Reminder, Boolean> reminderBooleanTableColumn) {
                final TableCell<Reminder, Boolean> cell = new TableCell<Reminder, Boolean>() {
                    public void updateItem(Boolean abc,boolean empty) {
                        TableRow<Reminder> reminderTableRow = getTableRow();
                            if (reminderTableRow.getItem() != null) {
                                if (reminderTableRow.getItem().getDone() == true) {
                                    setText("True");
                                    reminderTableRow.setId("table-row-celldone");
                                } else {
                                    setText("False");
                                    reminderTableRow.setId("table-row-cellnotdone");
                                }
                            } else {
                                reminderTableRow.setId("");
                            }
                    }
                };
                return cell;
            }
        };
        beginTime.setCellValueFactory(new PropertyValueFactory<Reminder, LocalTime>("beginTime"));
        title.setCellValueFactory(new PropertyValueFactory<Reminder, String>("title"));
        status.setCellValueFactory(new PropertyValueFactory<Reminder, Boolean>("done"));
        endDay.setCellValueFactory(new PropertyValueFactory<Reminder, LocalDate>("endDay"));
        endTime.setCellValueFactory(new PropertyValueFactory<Reminder, LocalTime>("endTime"));
        status.setCellFactory(cellFactory);
        nhacNho.setOnMouseClicked(event -> {
            // Make sure the user clicked on a populated item
            if (nhacNho.getSelectionModel().getSelectedItem() != null) {
                Reminder reminder = (Reminder) nhacNho.getSelectionModel().getSelectedItem();
                int index = dayList.get(reminder.getBeginDay()).indexOf((Reminder) nhacNho.getSelectionModel().getSelectedItem());
                control2.Edit(reminder, index);
                System.out.println(reminder.getMaR());
                control2.setBeginDay(reminder.getBeginDay());
                stage2.show();
            }
        });
    }
    ///Edit///
    public void Sua (Reminder reminder, int index) {
        Reminder currentReminder = (Reminder) nhacNho.getSelectionModel().getSelectedItem();
        currentReminder.setContent(reminder.getContent());
        currentReminder.setTitle(reminder.getTitle());
        currentReminder.setEndDay(reminder.getEndDay());
        currentReminder.setBeginTime(reminder.getBeginTime());
        currentReminder.setEndTime(reminder.getEndTime());
        currentReminder.setDone(reminder.getDone());
        int xong = toInt(reminder.getDone());
        String CauLenh = "UPDATE reminder \n" +
                "SET \n" +
                "title = '" + reminder.getTitle() + "', \n" +
                "beginTime = '" + reminder.getBeginTime() + "',\n" +
                "endDay = '" + reminder.getEndDay().format(dayFormatterSQL) + "',\n" +
                "endTime = '" + reminder.getEndTime() + "',\n" +
                "noiDung = ' " + reminder.getContent() + " ',\n" +
                "done = " + xong + "\n" +
                "WHERE MASO = " + dayList.get(reminder.getBeginDay()).get(index).getMaR();
        connectionDB.ThucThiCauLenhUpdate(CauLenh);
        setTableView();
        nhacNho.setItems(dayList.get(reminder.getBeginDay()));
    }
    ///Xóa reminder cũ///
    public void delete () {
        Reminder selected = (Reminder) nhacNho.getSelectionModel().getSelectedItem();
        if (selected != null) {
            LocalDate current = selected.getBeginDay();
            dayList.get(current).remove(selected);
            DeleteData(selected);
            if (dayList.get(current).isEmpty()) {
                int ngay = selected.getBeginDay().getDayOfMonth();
                int dow = LocalDate.of(year, month, 1).getDayOfWeek().getValue();
                int val = ngay+dow-1;
                Button button = (Button) lich.getChildren().get(val);
                if (val%7==0) {
                    button.setId("button123");
                } else {
                    button.setId("");
                }
            }
        }
    }
    public void DeleteData (Reminder reminder) {
        int MaSo = reminder.getMaR();
        String CauLenh = "DELETE FROM reminder WHERE MASO =" + MaSo;
        connectionDB.ThucThiCauLenhUpdate(CauLenh);
    }

    ////Thêm reminder mới///
    public void add () {
        LocalDate date1 = LocalDate.of(year, month, Integer.valueOf(DAY.getText()));
        stage2.show();
        control2.setMenu(-10);
        control2.Clear();
        control2.setBeginDay(date1);
    }
    public void add(Reminder reminder) {
        LocalDate current = reminder.getBeginDay();
        dayList.get(current).add(reminder);
        SaveData(reminder);
        int ngay = reminder.getBeginDay().getDayOfMonth();
        int dow = LocalDate.of(year, month, 1).getDayOfWeek().getValue();
        Button button = (Button) lich.getChildren().get(ngay+dow-1);
        button.setId("buttonMark");
    }


//    row*7+col-dow + 1= ngay
    private int toInt (boolean done) {
        if(done == true) return 1;
        return 0;
    }

    DateTimeFormatter dayFormatterSQL = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private void SaveData(Reminder reminder) {
        int xong = toInt(reminder.getDone());
        String CauLenh = "INSERT INTO reminder\n" +
                "VALUES (\n" +
                reminder.getMaR() + ",\n'" +
                reminder.getTitle() + "', \n'" +
                reminder.getBeginDay().format(dayFormatterSQL) + "', \n'" +
                reminder.getBeginTime() + "', \n'" +
                reminder.getEndDay().format(dayFormatterSQL) + "', \n'" +
                reminder.getEndTime() + "', \n'" +
                reminder.getContent() + "', \n" +
                xong + "\n" +
                ")";
        connectionDB.ThucThiCauLenhUpdate(CauLenh);
    }

    public void setLich () {
        YEAR.setId("label1");
        MONTH.setId("label1");
        DAY.setId("label1");
        YEAR.setText(year + "");
        MONTH.setText(month + "");
        DAY.setText(day + "");
        main.getStyleClass().add("pane");

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Button button = new Button();
                lich.add(button, col, row, 1, 1);
            }
        }
        hienThiLich(month,year);
    }

    public void initialize () {
        ngayHienTai.setText("Ngày "+curDAY+" tháng "+curMONTH+" năm "+curYEAR+"");
        if (dayList.isEmpty()) {
            LocalDate date1 = LocalDate.of(year, month, day);
            ObservableList<Reminder> currentDay = FXCollections.observableArrayList();
            dayList.put(date1, currentDay);
        }
        add.setId("button123");
        delete.setId("button123");
        UpdateCalendar.update(connectionDB, dayList);
        setTableView();
        setLich();
    }

    public void hienThiLich(int mm, int yy) {
        LocalDate localDate = LocalDate.of(yy, mm, 1);
        LocalDate before = LocalDate.of(yy, (mm+10)%12+1, 1);
        int dow = DayOfWeek.from(localDate).getValue();
        int dom1 = localDate.lengthOfMonth();
        int dom2 = before.lengthOfMonth();
        String MM = localDate.format(m);
        String YY = yy+"";
        YEAR.setText(YY);
        MONTH.setText(MM);
        LocalDate localDate1;
        for (int i=0; i<42; i++) {
            Button button = (Button) lich.getChildren().get(i);
            if (i%7 == 0) button.setId("button123");
                else button.setId("");
            button.setDisable(false);
            int date = i-dow+1;
            if (date < 1) {
                date = date + dom2;
                button.setDisable(true);
            }else {
                if (date > dom1) {
                    date = date - dom1;
                    button.setDisable(true);
                } else {
                    localDate1 = LocalDate.of(yy, mm, date);
                    if(dayList.containsKey(localDate1) && !dayList.get(localDate1).isEmpty()) {
                        button.setId("buttonMark");
                    }
                }
            }
            button.setText(date + "");
        }
        for (Node e: lich.getChildren()) {
            Button button = (Button) e;
            button.setOnAction(event ->{
                String str = button.getText();
                DAY.setText(str);
                LocalDate date1 = LocalDate.of(year, month, Integer.valueOf(str));
                if (dayList.containsKey(date1)) {
                    nhacNho.setItems(dayList.get(date1));
                } else {
                    ObservableList<Reminder> currentDay = FXCollections.observableArrayList();
                    dayList.put(date1, currentDay);
                    nhacNho.setItems(dayList.get(date1));
                }
            });
        }
    }

}