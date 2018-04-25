package UI;

import server.Server;
import utils.TextAreaLogAppender;
import javax.swing.*;
import org.apache.log4j.*;
import utils.XMLUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class HttpServerUI{

    private JFrame serverPage = new JFrame("ygowill's server");
    private JPanel serverPanel = new JPanel();
    private JTextArea logArea = new JTextArea();
    private JScrollPane logScrollPane = new JScrollPane(logArea);
    private JButton startService = new JButton("开启服务器");
    private JButton stopService = new JButton("关闭服务器并退出");
    private JButton clear = new JButton("清除");
    private JLabel portLabel = new JLabel("端口号:");
    private JLabel space = new JLabel("   ");
    private JTextField portField = new JTextField();
    private Logger logger = Logger.getLogger(HttpServerUI.class);
    private Thread serverThread;
    private Thread logThread;
    private Server httpServer=new Server(false);

    public HttpServerUI(){

        //初始化组件
        startService.setEnabled(true);
        stopService.setEnabled(false);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setSelectedTextColor(new Color(0, 0, 0));
        logArea.setSelectionColor(new Color(219, 230, 253));
        logArea.setBackground(new Color(255, 255, 255));
        logArea.setFont(new Font("TimesRoman", Font.BOLD, 20));
        logArea.setWrapStyleWord(true);
        logArea.setText("计算机网络课程作业\n秦锴 2016220302008\n        2018.4.19\n\n\n在浏览器中输入 localhost:8089/home即可访问主页\n");

        portField.setFont(new Font("TimesRoman", Font.BOLD, 20));
        portField.setText("8089");

        //布置页面
        GridBagLayout layout = new GridBagLayout();
        serverPage.setLayout(layout);
        serverPage.add(logScrollPane);
        serverPage.add(space);
        serverPage.add(portLabel);
        serverPage.add(portField);
        serverPage.add(startService);
        serverPage.add(stopService);
        serverPage.add(clear);

        GridBagConstraints s= new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        //设置textarea的形状
        s.gridwidth=0;
        s.gridheight=4;
        s.weightx=1;
        s.weighty=1;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(logScrollPane,s);
        //设置portlabel的形状
        s.gridwidth=1;
        s.gridheight=1;
        s.weightx=0;
        s.weighty=0;
        s.anchor=GridBagConstraints.WEST;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(space,s);
        layout.setConstraints(portLabel,s);
        //设置portfield的形状
        s.gridwidth=3;
        s.gridheight=1;
        s.weightx=1;
        s.weighty=0;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(portField,s);
        //设置startservice按钮的形状
        s.gridwidth=1;
        s.gridheight=1;
        s.weightx=0;
        s.weighty=0;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(startService,s);
        //设置stopservice按钮的形状
        s.gridwidth=1;
        s.gridheight=1;
        s.weightx=0;
        s.weighty=0;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(stopService,s);
        //设置clear按钮的形状
        s.gridwidth=1;
        s.gridheight=1;
        s.weightx=0;
        s.weighty=0;
        s.insets=new Insets(5,5,5,5);
        layout.setConstraints(clear,s);

        Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
        int WIDTH = (int) SCREEN.getWidth();
        int HEIGHT = (int) SCREEN.getHeight();
        serverPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        serverPage.setLocation(WIDTH / 4, HEIGHT / 4);
        serverPage.setSize(650, 400);
        serverPage.setResizable(false);
        serverPage.setVisible(true);

        //注册事件监听
        myevent();

        //绑定日志输出
        initLog();
    }

    //注册事件监听
    private void myevent(){
        startService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceStart();
            }
        });

        stopService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceStop();
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.setText("");
            }
        });

        portField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                portField.selectAll();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    portField.setText("8089");
                    logArea.requestFocus();
                }
            }
        });
    }

    private boolean isLegal(String str) {
        try {
            BigDecimal port=new BigDecimal(str);
            int portVal=port.intValue();
            if(portVal>1024 && portVal<49151){
                return true;
            }
            else{
                JOptionPane.showMessageDialog(serverPage, "端口号输入不合法！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(serverPage, "端口号输入不合法！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void ServiceStart(){
        //初始化并开启线程
        logArea.setText("");
        logger.info("用户开启服务器");
        if(portField.getText().isEmpty()){
            JOptionPane.showMessageDialog(serverPage, "端口号为空！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        else if(isLegal(portField.getText())){
            serverThread = new Thread(httpServer);
            XMLUtil.updateElement("/Users/qinkai/Desktop/simpleHttpServer/src/server.xml","port",portField.getText());
            serverThread.start();
            startService.setEnabled(false);
            stopService.setEnabled(true);
        }
    }

    private void ServiceStop(){
        //关闭线程
        httpServer.stop();
        serverThread.interrupt();

        logger.info("用户关闭服务器");
        startService.setEnabled(true);
        stopService.setEnabled(false);
        logger.info("谢谢使用");
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void initLog(){
        try{
            logThread = new TextAreaLogAppender(logArea,logScrollPane);
            logThread.start();
        }catch (Exception e){
            JOptionPane.showMessageDialog(serverPage, e, "绑定日志输出组件错误", JOptionPane.ERROR_MESSAGE);
            logger.error("绑定日志输出组件错误");
        }
    }

}
