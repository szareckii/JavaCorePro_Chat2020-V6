package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String nick;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        /****  Создать сервис - пул с одним потоком ****/
        ExecutorService service = Executors.newSingleThreadExecutor();

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            /**** создать поток в сервисе  ****/
            service.execute(() -> {

            try {
                //Если в течении 120 секунд не будет сообщений по сокету то вызовится исключение
                socket.setSoTimeout(120000);

                //цикл аутентификации
                while (true) {
                    String str = in.readUTF();

                    if (str.startsWith("/reg ")) {
                        String[] token = str.split(" ");

                        if (token.length < 4) {
                            continue;
                        }

                        boolean succeed = server
                                .getAuthService()
                                .registration(token[1], token[2], token[3]);
                        if (succeed) {
                            Server.LOGGER.log(Level.INFO,"Новый пользователь зарегистрировался");
                            sendMsg("Регистрация прошла успешно");
                        } else {
                            Server.LOGGER.log(Level.WARNING,"Не удалась регистрация нового пользователя");
                            sendMsg("Регистрация  не удалась. \n" +
                                    "Возможно логин уже занят, или данные содержат пробел");
                        }
                    }

                    if (str.startsWith("/auth ")) {
                        String[] token = str.split(" ");

                        if (token.length < 3) {
                            continue;
                        }

                        String newNick = server.getAuthService()
                                .getNicknameByLoginAndPassword(token[1], token[2]);

                        login = token[1];

                        if (newNick != null) {
                            if (!server.isLoginAuthorized(login)) {
                                sendMsg("/authok " + newNick);
                                nick = newNick;
                                server.subscribe(ClientHandler.this);

                                Server.LOGGER.log(Level.INFO,"Клиент " + nick + " подключился " + socket.getRemoteSocketAddress());
//                                System.out.println("Клиент: " + nick + " подключился" + socket.getRemoteSocketAddress());
                                socket.setSoTimeout(0);

                                //======Получение истории сообещений из БД========//
//                                    sendMsg(SQLHandler.getMessageForNick(nick));
                                //==============//

                                break;
                            } else {
                                sendMsg("С этим логином уже прошли аутентификацию");
                            }
                        } else {
                            sendMsg("Неверный логин / пароль");
                        }
                    }
                }

                //цикл работы
                while (true) {
                    String str = in.readUTF();

                    if (str.startsWith("/")) {
                        if (str.equals("/end")) {
                            sendMsg("/end");
                            break;
                        }
                        if (str.startsWith("/w ")) {
                            String[] token = str.split(" ", 3);

                            if (token.length < 3) {
                                continue;
                            }
                            Server.LOGGER.log(Level.INFO,"Клиент " + nick + " послал приватное сообщение");
                            server.privateMsg(ClientHandler.this, token[1], token[2]);
                        }

                        //==============//
                        if (str.startsWith("/chnick ")) {
                            String[] token = str.split(" ", 2);
                            if (token.length < 2) {
                                continue;
                            }
                            if (token[1].contains(" ")) {
                                sendMsg("Ник не может содержать пробелов");
                                continue;
                            }
                            if (server.getAuthService().changeNick(ClientHandler.this.nick, token[1])) {
                                sendMsg("/yournickis " + token[1]);
                                sendMsg("Ваш ник изменен на " + token[1]);
                                ClientHandler.this.nick = token[1];
                                server.broadcastClientList();
                            } else {
                                sendMsg("Не удалось изменить ник. Ник " + token[1] + " уже существует");
                            }
                        }
                        //==============//


                    } else {
                        Server.LOGGER.log(Level.INFO,"Клиент  " + nick + " послал сообщение");
                        server.broadcastMsg(nick, str);
                    }
                }
            } catch (SocketTimeoutException e) {
                sendMsg("/end");
            }
            ///////
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.unsubscribe(ClientHandler.this);

                /****  "заглушить" сервис  ****/
                service.shutdown();
                Server.LOGGER.log(Level.INFO,"Клиент  " + nick + " отключился");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            }).start();
        });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }
}
