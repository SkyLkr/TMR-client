package com.example.tmr;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private static boolean initialized = false;
    private static Socket client;
    private static PrintStream out;
    private static Scanner in;

    public static boolean isConnected() {
        return initialized;
    }

    public static void connect(String ip, int port) throws IOException {
        if (!initialized) {
            client = new Socket(ip, port);

            out = new PrintStream(client.getOutputStream());
            in = new Scanner(client.getInputStream());

            initialized = true;
        }
    }

    public static String run(String command, Context ctx) {
        out.println(command);
        return getResponse(ctx);
    }

    public static String getResponse(Context ctx) {
        while(true) {
            if (in.hasNextLine()) {
                String msg = in.nextLine();
                String[] m = msg.split(":");

                switch (m[0].toUpperCase()) {
                    case "SUCCESS":
                        Toast.makeText(ctx, m[1], Toast.LENGTH_SHORT).show();
                        break;
                    case "ERROR":
                        Toast.makeText(ctx, String.format("Erro: %s", m[1]), Toast.LENGTH_SHORT).show();
                        break;
                }
                return msg;
            }
        }
    }
}
