package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

public class ServerThread extends Thread {

    private final Socket jogadorSocket;
    private final Phaser phaser;
    private final Semaphore semaphore;


    public ServerThread(Socket jogadorSocket, Phaser phaser, Semaphore semaphore) {
       this.jogadorSocket = jogadorSocket;
       this.phaser = phaser;
       this.semaphore = semaphore;

       //Registrar as Threads
       phaser.register();
    }

    @Override
    public void run() {

        try(
                PrintWriter out = new PrintWriter(jogadorSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(jogadorSocket.getInputStream()));

                ){

            out.println("Username: ");
            String username = in.readLine();

            out.println("Password: ");
            String password = in.readLine();

            if(Server.authenticateUsers(username, password)) {
                int login = Server.updateLoginStatus(username, 1);

                if(login == 1) {
                    System.out.println("Usuario encontrado");
                    out.println("Logado");
                }else if(login == 0) {
                    out.println("Sessao ja iniciada em outro computador");
                }
            }else{
                System.out.println("Usuário não encontrado!");
                out.println("Utilizador ou senha incorreta");
            }


        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }


}
