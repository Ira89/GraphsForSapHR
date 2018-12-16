package ru.polynkina.irina.gui;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.polynkina.irina.beans.MinMaxNormTime;
import ru.polynkina.irina.beans.MinMaxYear;
import ru.polynkina.irina.beans.SAPCalendar;
import ru.polynkina.irina.beans.UserDates;
import ru.polynkina.irina.graphs.GraphsContainer;
import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.hours.LibHours;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.period.UserPeriod;
import ru.polynkina.irina.regions.RegionsContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class UserFrame extends JFrame {

    private static ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("./spring/spring.xml");
    private static UserDates userDates = ctx.getBean(UserDates.class);
    private static final int MAX_AMOUNT_FIELDS = userDates.getAmount();
    private static final int MAX_AMOUNT_PANEL = 8;
    private static final int MIN_INDEX_MONTH = 1;
    private static final int MAX_INDEX_MONTH = 12;
    private static final int OVERAGE_NORM_TIME = 168;
    private static final int WORK_HOURS_IN_DAY = 8;

    private int userYear;
    private int userMonth;
    private int userNormTime;
    private Set<Integer> setShortDays = new HashSet<Integer>();
    private Set<Integer> setHolidays = new HashSet<Integer>();
    private Set<Integer> setOffDays = new HashSet<Integer>();

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

    private JPopupMenu popupMenu;

    public UserFrame() {
        super("Генерация графиков");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        width = size.width / 2;
        height = size.height / 3 * 2;
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
        initPopupMenu();
    }

    private void initPanelYear() {
        panelYear = new JPanel();
        textYear = new JLabel("Год");
        MinMaxYear minMaxYear = ctx.getBean(MinMaxYear.class);
        spinnerYear = new JSpinner(new SpinnerNumberModel(calendar.get(Calendar.YEAR),
                minMaxYear.getMinYear(), minMaxYear.getMaxYear(), 1));
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
        MinMaxNormTime minMaxNormTime = ctx.getBean(MinMaxNormTime.class);
        spinnerNormTime = new JSpinner(new SpinnerNumberModel(OVERAGE_NORM_TIME,
                minMaxNormTime.getMinNormTime(), minMaxNormTime.getMaxNormTime(), WORK_HOURS_IN_DAY));
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
        textOffDays = new JLabel("Перенесенные выходные");
        panelOffDays.add(textOffDays);

        fieldsOffDays = new JTextField[MAX_AMOUNT_FIELDS];
        for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
            fieldsOffDays[i] = new JTextField("         ");
            panelOffDays.add(fieldsOffDays[i]);
        }

        add(panelOffDays);
    }

    private void initPanelCalendars() {
        SAPCalendar calendars = ctx.getBean(SAPCalendar.class);

        panelCalendars = new JPanel();
        textCalendars = new JLabel("Календари для генерации");
        panelCalendars.add(textCalendars);

        int MAX_AMOUNT_GRAPHS = calendars.getCalendars().size();
        checkBoxCalendars = new JCheckBox[MAX_AMOUNT_GRAPHS];
        for(int i = 0; i < MAX_AMOUNT_GRAPHS; ++i) {
            checkBoxCalendars[i] = new JCheckBox(calendars.getCalendarByIndex(i));
            checkBoxCalendars[i].setSelected(true);
            panelCalendars.add(checkBoxCalendars[i]);
        }

        add(panelCalendars);
    }

    private void initPanelAction() {
        panelAction = new JPanel();
        textErrors = new JLabel("");
        action = new JButton("Генерировать");
        action.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(inputValueIsCorrect()) {
                    action.setEnabled(false);
                    action.doClick();
                    generateGraphs();
                }
            }
        });
        panelAction.setLayout(new BorderLayout());
        panelAction.add(new JScrollPane(textErrors));
        panelAction.add(action, BorderLayout.EAST);
        add(panelAction);
    }

    private void initPopupMenu() {

        class MousePopupListener extends MouseAdapter {

            public void mouseReleased(MouseEvent e) {
                checkPopup(e);
            }

            private void checkPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(panelYear, e.getX(), e.getY());
                }
            }
        }

        popupMenu = new JPopupMenu();
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InfoFrame infoFrame = new InfoFrame(null, "О программе",
                        "version: 6.1.0" +
                                "<br>release: 16/12/2018" +
                                "<br>author: Irina Polynkina" +
                                "<br>email: irina.polynkina.dev@yandex.ru");
                infoFrame.setVisible(true);
            }
        };
        JMenuItem info = new JMenuItem("О программе");
        info.addActionListener(menuListener);
        popupMenu.add(info);
        addMouseListener(new MousePopupListener());
    }

    private boolean inputValueIsCorrect() {
        try {
            inputYearIsCorrect();
            inputMonthIsCorrect();
            inputNormTimeIsCorrect();
            inputDaysIsCorrect();
            findEqualsValues();
            return true;
        } catch(Exception exc) {
            reset();
            textErrors.setForeground(Color.RED);
            textErrors.setText(exc.getMessage());
            return false;
        }
    }

    private void inputYearIsCorrect() throws NumberFormatException {
        try {
            userYear = Integer.parseInt(spinnerYear.getValue().toString());
        } catch(NumberFormatException exc) {
            throw new NumberFormatException("Некорректно введен год!");
        }
    }

    private void inputMonthIsCorrect() throws NumberFormatException {
        try {
            userMonth = Integer.parseInt(spinnerMonth.getValue().toString());
        } catch(NumberFormatException exc) {
            throw new NumberFormatException("Некорректно введен месяц!");
        }
    }

    private void inputNormTimeIsCorrect() throws NumberFormatException {
        try {
            userNormTime = Integer.parseInt(spinnerNormTime.getValue().toString());
        } catch(NumberFormatException exc) {
            throw new NumberFormatException("Некорректно введена норма времени!");
        }
    }

    private void inputDaysIsCorrect() throws NumberFormatException, IndexOutOfBoundsException {
        parseDays(fieldsShortDays, setShortDays);
        parseDays(fieldsHolidays, setHolidays);
        parseDays(fieldsOffDays, setOffDays);
    }

    private void parseDays(JTextField[] fields, Set<Integer> listDays) {
        int valueDay = 0;
        try {
            for(int i = 0; i < MAX_AMOUNT_FIELDS; ++i) {
                String textDay = fields[i].getText().trim();
                if(!textDay.equals("")) {
                    valueDay = Integer.parseInt(textDay);
                    if(valueDay < 1 || valueDay > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        throw new IndexOutOfBoundsException();
                    } else listDays.add(valueDay);
                }
            }
        } catch(NumberFormatException exc) {
            throw new NumberFormatException("Некорректно введены дни!");
        } catch(IndexOutOfBoundsException exc) {
            throw new IndexOutOfBoundsException("В месяце " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH) +
                    " дней, дата " + valueDay + " некорректна");
        }
    }

    private void findEqualsValues() throws IndexOutOfBoundsException {
        if(!setShortDays.isEmpty() && !setHolidays.isEmpty()) {
            if(!Collections.disjoint(setShortDays, setHolidays))
                throw new IndexOutOfBoundsException("Короткие дни не могут одновременно являться праздничными!");
        }

        if(!setHolidays.isEmpty() && !setOffDays.isEmpty()) {
            if(!Collections.disjoint(setHolidays, setOffDays))
                throw new IndexOutOfBoundsException("Праздничные дни не могут одновременно являться выходными!");
        }

        if(!setShortDays.isEmpty() && !setOffDays.isEmpty()) {
            if(!Collections.disjoint(setShortDays, setOffDays))
                throw new IndexOutOfBoundsException("Короткие дни не могут одновременно являться выходными!");
        }
    }

    private void generateGraphs() {
        try {
            ReportingPeriod period = new UserPeriod(userYear, userMonth, userNormTime, setShortDays, setHolidays, setOffDays);
            Hours libHours = new LibHours();
            RegionsContainer regions = new RegionsContainer(checkBoxCalendars);
            GraphsContainer graphs = new GraphsContainer(period);
            graphs.startGenerating(period, libHours);
            graphs.writeGraphsInFile(period, regions);
            graphs.deleteOldCounter(period);
            textErrors.setForeground(Color.BLUE);
            textErrors.setText(graphs.getCheckResults());
        } catch(Exception exc) {
            textErrors.setForeground(Color.RED);
            textErrors.setText(exc.getMessage());
        }

        action.setEnabled(true);
        reset();
    }

    private void reset() {
        userYear = 0;
        userMonth = 0;
        userNormTime = 0;
        setShortDays.clear();
        setHolidays.clear();
        setOffDays.clear();
    }
}
