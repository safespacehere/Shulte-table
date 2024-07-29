package ru.shulte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Vector;

public class Main {
    private int id = 0;
    private Connection conn;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private TestWindow testWindow;
    private ResultWindow resultWindow;
    private String mainMenuName = "MainMenu";
    private String entryMenuName = "EntryMenu";
    private String authorizationWindowName = "AuthorizationWindow";
    private String registrationWindowName = "RegistrationWindow";
    private String testWindowName = "TestWindow";
    private String referenceWindowName = "ReferenceWindow";
    private String resultWindowName = "ResultWindow";

    Main() {
        createConnect();

        frame = new JFrame("Тестирование Шульте");
        frame.setLayout(new BorderLayout());
        setMinSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        EntryMenu entryMenu = new EntryMenu();
        cardPanel.add(entryMenu);
        cardLayout.addLayoutComponent(entryMenu, entryMenuName);

        MainMenu mainMenu = new MainMenu();
        cardPanel.add(mainMenu);
        cardLayout.addLayoutComponent(mainMenu, mainMenuName);

        AuthorizationWindow autoWindow = new AuthorizationWindow();
        cardPanel.add(autoWindow);
        cardLayout.addLayoutComponent(autoWindow, authorizationWindowName);

        RegistrationWindow regWindow = new RegistrationWindow();
        cardPanel.add(regWindow);
        cardLayout.addLayoutComponent(regWindow, registrationWindowName);

        ReferenceWindow refWindow = new ReferenceWindow();
        cardPanel.add(refWindow);
        cardLayout.addLayoutComponent(refWindow, referenceWindowName);

        resultWindow = new ResultWindow();
        cardPanel.add(resultWindow);
        cardLayout.addLayoutComponent(resultWindow, resultWindowName);

        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main();
            }
        });
    }

    private void createConnect() {
        try {
            String url = resource.url;
            String username = resource.username;
            String password = resource.password;
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    private void setMinSize() {
        frame.setBounds(700, 400, 475, 125);
    }

    private void setBigSize() {
        frame.setBounds(700, 200, 600, 600);
    }

    class AuthorizationWindow extends JPanel {
        private JTextField userLoginField;
        private JTextField userPasswordField;

        public AuthorizationWindow() {
            this.setLayout(new GridLayout(3, 2, 1, 1));
            JLabel userLogin = new JLabel("Введите логин");
            userLoginField = new JTextField("", 1);

            JLabel userPassword = new JLabel("Введите пароль");
            userPasswordField = new JTextField("", 1);

            JButton returnButton = new JButton("Назад");
            JButton authorizationButton = new JButton("Войти");

            returnButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, entryMenuName);
                }
            });
            authorizationButton.addActionListener(new AuthorizationAction());

            this.add(userLogin);
            this.add(userLoginField);
            this.add(userPassword);
            this.add(userPasswordField);
            this.add(returnButton);
            this.add(authorizationButton);
        }

        class AuthorizationAction implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = Authorization.validation(conn, userLoginField.getText(), userPasswordField.getText());
                String messageName = "Ошибка";

                if (result == -1) {
                    JOptionPane.showMessageDialog(null, "Введён неверный логин и/или пароль", messageName, JOptionPane.PLAIN_MESSAGE);
                } else if (result == -2) {
                    JOptionPane.showMessageDialog(null, "Подключение не удалось", messageName, JOptionPane.PLAIN_MESSAGE);
                } else {
                    id = result;
                    messageName = "Вход";
                    JOptionPane.showMessageDialog(null, "Добро пожаловать!", messageName, JOptionPane.PLAIN_MESSAGE);
                    cardLayout.show(cardPanel, mainMenuName);
                }
            }
        }
    }

    class RegistrationWindow extends JPanel {
        private JTextField userLoginField;
        private JTextField userNameField;
        private JTextField userSurnameField;
        private JTextField userPasswordField;

        public RegistrationWindow() {
            this.setLayout(new GridLayout(3, 4, 1, 1));
            JLabel userLogin = new JLabel("Введите логин");
            userLoginField = new JTextField("", 1);

            JLabel userName = new JLabel("Введите Имя");
            userNameField = new JTextField("", 1);

            JLabel userSurname = new JLabel("Введите Фамилию");
            userSurnameField = new JTextField("", 1);

            JLabel userPassword = new JLabel("Введите пароль");
            userPasswordField = new JTextField("", 1);

            JButton returnButton = new JButton("Назад");
            JButton authorizationButton = new JButton("Подтвердить");

            returnButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, entryMenuName);
                }
            });
            authorizationButton.addActionListener(new RegistrationAction());

            this.add(userLogin);
            this.add(userLoginField);
            this.add(userName);
            this.add(userNameField);
            this.add(userSurname);
            this.add(userSurnameField);
            this.add(userPassword);
            this.add(userPasswordField);
            this.add(new JLabel("")); //Костыль для красоты
            this.add(returnButton);
            this.add(authorizationButton);
        }

        class RegistrationAction implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = Registration.verification(conn, userLoginField.getText(), userPasswordField.getText(),
                        userNameField.getText(), userSurnameField.getText());
                String messageName = "Вход";

                if (result == 1) {
                    id = Authorization.validation(conn, userLoginField.getText(), userPasswordField.getText());
                    JOptionPane.showMessageDialog(null, "Добро пожаловать!", messageName, JOptionPane.PLAIN_MESSAGE);
                    cardLayout.show(cardPanel, mainMenuName);
                } else if (result == 1062) {
                    messageName = "Ошибка";
                    JOptionPane.showMessageDialog(null, "Выбранынй логин занят", messageName, JOptionPane.PLAIN_MESSAGE);
                } else if (result == -1) {
                    messageName = "Ошибка";
                    JOptionPane.showMessageDialog(null, "Необходимо заполнить все поля", messageName, JOptionPane.PLAIN_MESSAGE);
                } else {
                    messageName = "Ошибка";
                    JOptionPane.showMessageDialog(null, "Подключение не удалось", messageName, JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    public class EntryMenu extends JPanel {
        public EntryMenu() {
            JButton autoButton = new JButton("Авторизация");
            JButton regButton = new JButton("Регистрация");
            autoButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, authorizationWindowName);
                }
            });
            regButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, registrationWindowName);
                }
            });
            this.add(autoButton);
            this.add(regButton);
        }
    }

    public class MainMenu extends JPanel {
        public MainMenu() {
            JButton refButton = new JButton("Справка");
            JButton testButton = new JButton("Пройти тест");
            JButton resButton = new JButton("Результаты");

            //Кнопка перехода к тесту
            testButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    testWindow = new TestWindow();
                    cardPanel.add(testWindow);
                    cardLayout.addLayoutComponent(testWindow, testWindowName);
                    cardLayout.show(cardPanel, testWindowName);
                    setBigSize();
                }
            });
            //Кнопка перехода к справке
            refButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, referenceWindowName);
                    setBigSize();
                }
            });

            resButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardPanel.remove(resultWindow);
                    cardLayout.removeLayoutComponent(resultWindow);

                    resultWindow = new ResultWindow();
                    cardPanel.add(resultWindow);
                    cardLayout.addLayoutComponent(resultWindow, resultWindowName);
                    cardLayout.show(cardPanel, resultWindowName);
                    setBigSize();
                }
            });

            this.add(refButton);
            this.add(testButton);
            this.add(resButton);
        }
    }

    public class TestWindow extends JPanel {
        private Integer[] array = new Integer[25];
        private int previous = 0;
        private int currentPage = 0;
        private long startTime = 0;
        private long endTime = 0;
        private int[] attemptsTime = new int[5];
        private CardLayout cl;
        private JPanel cards;

        public TestWindow() {
            this.setLayout(new BorderLayout());
            for (int i = 1; i <= 25; i++) {
                array[i - 1] = i;
            }
            JButton nextButton = new JButton("Далее");
            JButton returnButton = new JButton("В главное меню");

            cl = new CardLayout();
            cards = new JPanel(cl);
            JPanel bottomPanel = new JPanel();

            nextButton.addActionListener(new nextAction());
            returnButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardPanel.remove(testWindow);
                    cardLayout.removeLayoutComponent(testWindow);
                    cardLayout.show(cardPanel, mainMenuName);
                    setMinSize();
                }
            });

            bottomPanel.add(returnButton);
            bottomPanel.add(nextButton);

            for (int j = 0; j < 5; j++) {
                shuffleArray();
                JPanel panel = new JPanel(new GridLayout(5, 5, 1, 1));
                cards.add(panel);
                cl.addLayoutComponent(panel, String.valueOf(j));

                for (int i = 1; i <= 25; i++) {
                    JButton button = new JButton(array[i - 1].toString());
                    panel.add(button);
                    button.setPreferredSize(new Dimension(50, 50));

                    button.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(previous == 25){
                                JOptionPane.showMessageDialog(null,
                                        "Вы прошли текущую таблицу, нажмите \"Далее\"", "Уведомление", JOptionPane.PLAIN_MESSAGE);
                            }
                            else if (Integer.parseInt(button.getText()) != previous + 1) {
                                JOptionPane.showMessageDialog(null,
                                        "Выбрано неверное число", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                previous += 1;
                            }
                        }
                    });
                }
            }
            this.add(cards, BorderLayout.CENTER);
            this.add(bottomPanel, BorderLayout.SOUTH);

            startTime = System.currentTimeMillis();
        }

        private void shuffleArray() {
            Random rnd = new Random();

            for (int i = array.length - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);

                Integer temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        class nextAction implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(previous == 25) {
                    if (currentPage < 4) {
                        endTime = System.currentTimeMillis();
                        attemptsTime[currentPage] = (int)((endTime - startTime) / 1000);
                        System.out.println(attemptsTime[currentPage]);
                        previous = 0;
                        currentPage += 1;
                        startTime = System.currentTimeMillis();
                        cl.next(cards);
                    } else {
                        endTime = System.currentTimeMillis();
                        attemptsTime[currentPage] = (int)((endTime - startTime) / 1000);
                        float[] result = Calculation.getResult(attemptsTime);


                        System.out.println(result[0]);
                        System.out.println(result[1]);
                        System.out.println(result[2]);

                        Result res = new Result(0, id, new Timestamp(System.currentTimeMillis()), attemptsTime, result[0], result[1], result[2]);
                        if(Sending.send(conn, res) == 1){
                            JOptionPane.showMessageDialog(null,
                                    "Результаты сохранены", "Успешно", JOptionPane.PLAIN_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null,
                                    "Не удалось сохранить результаты", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                        }

                        cardPanel.remove(testWindow);
                        cardLayout.removeLayoutComponent(testWindow);
                        cardPanel.remove(resultWindow);
                        cardLayout.removeLayoutComponent(resultWindow);

                        resultWindow = new ResultWindow();
                        cardPanel.add(resultWindow);
                        cardLayout.addLayoutComponent(resultWindow, resultWindowName);
                        cardLayout.show(cardPanel, resultWindowName);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,
                            "Перед тем как перейти к следующей таблице, завершите текущую", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    class ReferenceWindow extends JPanel{
             ReferenceWindow(){
                 JTextPane text = new JTextPane();
                 text.setText("\t\t\tСправка\n" +
                         "Испытуемому необходимо поочередно в пяти таблицах,\n" +
                         "на которых в случайном порядке расположены числа от 1 до 25,\n" +
                         "отыскивать и указывать числа, в порядке их возрастания.\n" +
                         "Испытуемый должен выполнить задание как можно быстрее.");
                 text.setFont(new Font("Serif", Font.TRUETYPE_FONT, 18));
                 this.add(text);
                 JButton returnButton = new JButton("В главное меню");
                 returnButton.addActionListener(new ActionListener() {

                     @Override
                     public void actionPerformed(ActionEvent e) {
                         cardLayout.show(cardPanel, mainMenuName);
                         setMinSize();
                     }
                 });
                 this.add(returnButton);
             }
    }
    class ResultWindow extends JPanel{
        private JLabel resId;
        private JLabel date;
        private JLabel fullTime;
        private JLabel T1;
        private JLabel T2;
        private JLabel T3;
        private JLabel T4;
        private JLabel T5;
        private JLabel jobEfficiency;
        private JLabel workabilityDegree;
        private JLabel mentalStability;
        private Result res;
        ResultWindow(){
            this.setLayout(new BorderLayout());
            JPanel centrePanel = new JPanel(new GridLayout(11, 1, 1, 1));
            JPanel topPanel = new JPanel();
            resId = new JLabel("");
            date = new JLabel("");
            fullTime = new JLabel("");
            T1 = new JLabel("");
            T2 = new JLabel("");
            T3 = new JLabel("");
            T4 = new JLabel("");
            T5 = new JLabel("");
            jobEfficiency  = new JLabel("");
            workabilityDegree = new JLabel("");
            mentalStability = new JLabel("");

            centrePanel.add(resId);
            centrePanel.add(date);
            centrePanel.add(fullTime);
            centrePanel.add(T1);
            centrePanel.add(T2);
            centrePanel.add(T3);
            centrePanel.add(T4);
            centrePanel.add(T5);
            centrePanel.add(jobEfficiency);
            centrePanel.add(workabilityDegree);
            centrePanel.add(mentalStability);

            JLabel title = new JLabel("В выпадающем списке выберите дату нужного теста");
            JButton returnButton = new JButton("В главное меню");
            returnButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, mainMenuName);
                    setMinSize();
                }
            });

            Vector<Result> data = Receiving.receive(conn, id);
            Vector<String> dates = new Vector<String>();

            if(data != null){
                for (Result el : data) {
                    dates.add(el.date.toString());
                }
                JComboBox<String> comboBox = new JComboBox<String>(dates);
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        res = data.get(comboBox.getSelectedIndex());
                        setText();
                    }
                });
                topPanel.add(title);
                topPanel.add(comboBox);
            }
            else {
                JLabel label = new JLabel("Вы пока не прошли ни одного теста");

                this.add(label, BorderLayout.CENTER);
            }
            this.add(topPanel, BorderLayout.NORTH);
            this.add(centrePanel, BorderLayout.CENTER);
            this.add(returnButton, BorderLayout.SOUTH);
        }
        public void setText(){
            resId.setText("Идентификатор результата: " + res.resId);
            date.setText("Дата получения результата: " + res.date.toString());
            fullTime.setText("Суммарное время прохождения всех таблиц(в секундах): " + (res.T[0]+res.T[1]+res.T[2]+res.T[3]+res.T[4]));
            T1.setText("Время прохождения первой таблицы(в секундах): " + res.T[0]);
            T2.setText("Время прохождения второй таблицы(в секундах): " + res.T[1]);
            T3.setText("Время прохождения третьей таблицы(в секундах): " + res.T[2]);
            T4.setText("Время прохождения четвёртой таблицы(в секундах): " + res.T[3]);
            T5.setText("Время прохождения пятой таблицы(в секундах): " + res.T[4]);
            jobEfficiency.setText("Эффективность работы: " + res.jobEfficiency);
            workabilityDegree.setText("Степень врабатываемости: " + res.workabilityDegree);
            mentalStability.setText("Психическая устойчивость: " + res.mentalStability);
        }
    }
}