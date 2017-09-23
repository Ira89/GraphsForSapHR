package ru.polynkina.irina.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class UserFrame extends JFrame {

    private static final int MAX_AMOUNT_PANEL = 8;
    private static final int MAX_AMOUNT_FIELDS = 8;

    private static final int MIN_INDEX_YEAR = 2017;
    private static final int MAX_INDEX_YEAR = 2117;
    private static final int MIN_INDEX_MONTH = 1;
    private static final int MAX_INDEX_MONTH = 12;
    private static final int MIN_NORM_TIME = 104;
    private static final int MAX_NORM_TIME = 200;
    private static final int OVERAGE_NORM_TIME = 168;
    private static final int WORK_HOURS_IN_DAY = 8;
    private static final String[] calendars = {"RU", "RB", "RT", "RC", "RI", "RD", "RK", "RS", "YF"};

    private Calendar calendar = Calendar.getInstance();

    private int width;
    private int height;

    private JPanel panelYear;
    private JPanel panelMonth;
    private JPanel panelNormTime;
    private JPanel panelShortDays;
    private JPanel panelHolidays;
    private JPanel panelOffDays;
    private JPanel panelCalendars;
    private JPanel panelAction;

    private JLabel textYear;
    private JLabel textMonth;
    private JLabel textNormTime;
    private JLabel textShortDays;
    private JLabel textHolidays;
    private JLabel textOffDays;
    private JLabel textCalendars;
    private JLabel textErrors;

    private JSpinner spinnerYear;
    private JSpinner spinnerMonth;
    private JSpinner spinnerNormTime;
    private JTextField[] fieldsShortDays;
    private JTextField[] fieldsHolidays;
    private JTextField[] fieldsOffDays;
    private JCheckBox[] checkBoxCalendars;
    private JButton action;

    public UserFrame() {
        super("Генерация графиков");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        width = size.width / 2;
        height = size.height / 2;
        setSize(width, height);
        setLocationRelativeTo(null);
        initFrame();
        setVisible(true);
    }

    private void initFrame() {
        setLayout(new GridLayout(MAX_AMOUNT_PANEL, 1));
        initPanelYear();
        initPanelMonth();
        initPanelNormTime();
        initPanelShortDays();
        initPanelHolidays();
        initPanelOffDays();
        initPanelCalendars();
        initPanelAction();
    }

    private void initPanelYear() {
        panelYear = new JPanel();
        textYear = new JLabel("Год");
        spinnerYear = new JSpinner(new SpinnerNumberModel(calendar.get(Calendar.YEAR), MIN_INDEX_YEAR, MAX_INDEX_YEAR, 1));
        panelYear.add(textYear);
        panelYear.add(spinnerYear);
        add(panelYear);
    }

    private void initPanelMonth() {
        panelMonth = new JPanel();
        textMonth = new JLabel("Месяц");
        spinnerMonth = new JSpinner(new SpinnerNumberModel(calendar.get(Calendar.MONTH) + 1, MIN_INDEX_MONTH, MAX_INDEX_MONTH, 1));
        panelMonth.add(textMonth);
        panelMonth.add(spinnerMonth);
        add(panelMonth);
    }

    private void initPanelNormTime() {
        panelNormTime = new JPanel();
        textNormTime = new JLabel("Норма времени");
        spinnerNormTime = new JSpinner(new SpinnerNumberModel(OVERAGE_NORM_TIME, MIN_NORM_TIME, MAX_NORM_TIME, WORK_HOURS_IN_DAY));
        panelNormTime.add(textNormTime);
        panelNormTime.add(spinnerNormTime);
        add(panelNormTime);
    }

    private void initPanelShortDays() {
        panelShortDays = new JPanel();
        textShortDays = new JLabel("Короткие дни");
        panelShortDays.add(textShortDays);

        fieldsShortDays = new JTextField[MAX_AMOUNT_FIELDS];
        for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
            fieldsShortDays[i] = new JTextField("         ");
            panelShortDays.add(fieldsShortDays[i]);
        }

        add(panelShortDays);
    }

    private void initPanelHolidays() {
        panelHolidays = new JPanel();
        textHolidays = new JLabel("Праздничные дни");
        panelHolidays.add(textHolidays);

        fieldsHolidays = new JTextField[MAX_AMOUNT_FIELDS];
        for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
            fieldsHolidays[i] = new JTextField("         ");
            panelHolidays.add(fieldsHolidays[i]);
        }

        add(panelHolidays);
    }

    private void initPanelOffDays() {
        panelOffDays = new JPanel();
        textOffDays = new JLabel("Выходные дни");
        panelOffDays.add(textOffDays);

        fieldsOffDays = new JTextField[MAX_AMOUNT_FIELDS];
        for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
            fieldsOffDays[i] = new JTextField("         ");
            panelOffDays.add(fieldsOffDays[i]);
        }

        add(panelOffDays);
    }

    private void initPanelCalendars() {
        panelCalendars = new JPanel();
        textCalendars = new JLabel("Календари для генерации");
        panelCalendars.add(textCalendars);

        checkBoxCalendars = new JCheckBox[MAX_AMOUNT_FIELDS];
        for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
            checkBoxCalendars[i] = new JCheckBox(calendars[i]);
            checkBoxCalendars[i].setSelected(true);
            panelCalendars.add(checkBoxCalendars[i]);
        }

        add(panelCalendars);
    }

    private void initPanelAction() {
        panelAction = new JPanel();
        textErrors = new JLabel("");
        action = new JButton("Генерировать");

        panelAction.setLayout(new BorderLayout());
        panelAction.add(textErrors);
        panelAction.add(action, BorderLayout.EAST);
        add(panelAction);
    }
}
