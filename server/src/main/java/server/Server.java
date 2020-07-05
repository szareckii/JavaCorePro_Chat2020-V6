package server;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.logging.*;


public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;
//    private static final Logger logger = Logger.getLogger(Server.class.getName());

    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log/log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Server.class.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Server() {
        clients = new Vector<>();

        //==============//
        if (!SQLHandler.connect()) {
            LOGGER.log(Level.WARNING,"Не удалось подключиться к БД");
            throw new RuntimeException("Не удалось подключиться к БД");
        }
        authService = new DBAuthServise();
        //==============//

        ServerSocket server = null;
        Socket socket;

        final int PORT = 8189;

        try {
            server = new ServerSocket(PORT);

            LOGGER.log(Level.INFO,"Сервер запущен!");

            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SQLHandler.disconnect();
            try {
                LOGGER.log(Level.INFO,"Сервер выключен");
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String nick, String msg) {
        //==============//
        SQLHandler.addMessage(nick,"null",msg,"once upon a time");
        //==============//
        for (ClientHandler c : clients) {
            c.sendMsg(nick + ": " + msg);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[ %s ] private [ %s ] : %s",
                sender.getNick(), receiver, msg);

        for (ClientHandler c : clients) {
            if (c.getNick().equals(receiver)) {
                c.sendMsg(message);

                //==============//
                SQLHandler.addMessage(sender.getNick(),receiver,msg,"once upon a time");
                //==============//

                if (!sender.getNick().equals(receiver)) {
                    sender.sendMsg(message);
                }
                return;
            }
        }

        sender.sendMsg("not found user: " + receiver);
    }


    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isLoginAuthorized(String login){
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientlist ");

        for (ClientHandler c : clients) {
            sb.append(c.getNick()).append(" ");
        }
        String msg = sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }
}
