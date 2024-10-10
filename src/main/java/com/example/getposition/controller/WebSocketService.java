package com.example.getposition.controller;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//@component （把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）
//泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
@Component
//这个注解可以理解为@RequestMapping，后面是接口地址和参数
@ServerEndpoint("/webSocket/{username}")//encoders = { ServerEncoder.class },可选值，指定编码转换器，传输对象时用到,这里直接转json就ok
public class WebSocketService {
    //定义的存储类，用于保存对应的用户名，向对应的用户推送消息
    private static final Map<String, Session> TOKEN_SESSION = new HashMap<>();
    private static final Map<String,String> SESSION_ID_TOKEN = new HashMap<>();
    //定个时间格式
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");

    @PostConstruct
    public void refreshDate(){
        //开启定时任务，1秒一次向前台发送当前时间
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(()->{
            //调用sendMessage方法，发送时间。
            //FORMAT.format代表格式化，按照上面定义的时间格式发送
            sendMessage(FORMAT.format(new Date()));
        },1000,1000, TimeUnit.MILLISECONDS);
    }
    //链接成功时调用的方法
    @OnOpen
    //onopen是websocket的依赖注解，包含了可选参数session，里面是请求的信息，username是用户发来的id
    public void onOpen(Session session,@PathParam("username")String username){
        System.out.println("新的连接进来了"+"识别码："+session.getId());
        //username是用户发来的id
        if (username == null){
            try {
                //检测路径参数为空时断开链接。session.close是断开链接的意思。
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT,"username参数为空"));
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }else {
            //往存储类中添加用户的信息。
            SESSION_ID_TOKEN.put(session.getId(),username);
            TOKEN_SESSION.put(username,session);
            System.out.println(username);
            //这里是用来从新建立链接的时候继续执行定时器，发送时间到前台，如果不写断链接后从新链接就不会执行定时器了，因为没有触发到。
//            this.refreshDate();
        }
    }
    //链接关闭时的方法
    @OnClose
    public void onColse(Session session){
        System.out.println("断开连接"+session.getId());
    }
    //客户端发过来的数据
    @OnMessage
    public void onMessage(Session session,String message){
        System.out.println("收到客户端发来的消息:"+message+"识别码："+session.getId());
        //接收到客户发来的消息后调用私发信息方法告诉用户已收到信息，参数为：对应id的人（用于区分用户）和发送的信息
        sendMessageToTarget(SESSION_ID_TOKEN.get(session.getId()),message+"已收到");
    }
    //群发信息的方法
    public void sendMessage(String message){
        System.out.println("发送全体消息");
        //循环全部人员信息
        TOKEN_SESSION.values().forEach((session)->{
            //向每个用户发送文本信息。这里getAsyncRemote（）解释一下，向用户发送文本信息有两种方式，一种是getBasicRemote，一种是getAsyncRemote
            //区别：getAsyncRemote是异步的，不会阻塞，而getBasicRemote是同步的，会阻塞，由于同步特性，第二行的消息必须等待第一行的发送完成才能进行。
            // 而第一行的剩余部分消息要等第二行发送完才能继续发送，所以在第二行会抛出IllegalStateException异常。所以如果要使用getBasicRemote()同步发送消息
            // 则避免尽量一次发送全部消息，使用部分消息来发送，可以看到下面sendMessageToTarget方法内就用的getBasicRemote，因为这个方法是根据用户id来私发的，所以不是全部一起发送。
            session.getAsyncRemote().sendText(message);
        });
    }
    //私发信息的方法（根据用户id把信息只发送给对应的人）
    //参数为：token是用户的id。后面t是发送的信息
    public <T> void sendMessageToTarget(String token,Object t){
        System.out.println("发送指定token消息");
        try {
            //发送信息，从TOKEN_SESSION存储类中找到对应id的用户，用getBasicRemote().sendText发送信息
            TOKEN_SESSION.get(token).getBasicRemote().sendText((String)t);
        } catch (Exception e) {
            //如果报错就抛出异常
            e.printStackTrace();
        }
    }
}


