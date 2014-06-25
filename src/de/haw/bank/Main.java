package de.haw.bank;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("haspaa-payments", false, false, false, null);

        Main main = new Main();

        for(int i = 0; i < 500 ; i++) {
            channel.basicPublish("", "haspaa-payments", null, main.generateMsg(i));
        }
    }

    private byte[] generateMsg(int i) {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.putInt(getRandomAmount());

        String msg = String.format("Random account movement No.%d", i);
        byte[] msgBytes = msg.getBytes();
        bb.putInt(msgBytes.length);
        bb.put(msgBytes);

        return bb.array();
    }

    private int getRandomAmount() {
        int val = (int)Math.floor(Math.random() * 1000);
        if(Math.random() > 0.5d) {
            val = -val;
        }

        return val;
    }
}
