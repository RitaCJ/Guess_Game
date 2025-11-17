package Servidor;

import utils.Array;
import utils.InputValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

public class ServerThread extends Thread {

    private final Socket jogadorSocket;
    private final Phaser phaser;
    private final Semaphore semaphore;
    private int numMin;
    private int numMax;
    private int randomNumber;


    public ServerThread(Socket jogadorSocket, Phaser phaser, Semaphore semaphore, int numMin, int numMax, int randomNumber) {
       this.jogadorSocket = jogadorSocket;
       this.phaser = phaser;
       this.semaphore = semaphore;
       this.numMin = numMin;
       this.numMax = numMax;
       this.randomNumber = randomNumber;

       //Registrar as Threads
       phaser.register();
    }

    @Override
    public void run() {

        try(
                PrintWriter out = new PrintWriter(jogadorSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(jogadorSocket.getInputStream()));

                ){

            boolean logado = false;
            int numTentativas = 0;

            while(!logado && numTentativas < 3){

                String username = in.readLine();
                String password = in.readLine();

                if(Server.authenticateUsers(username, password)) {
                    int login = Server.updateLoginStatus(username, 1);

                    if(login == 1) {
                        System.out.println("Usuario " + username + " conectado!");
                        out.println("logado!");
                        logado = true;

                    }else if(login == 0) {
                        out.println("Sessao ja iniciada em outro computador!");
                        //break;
                         numTentativas++;
                    }
                }else{
                    System.out.println("User " + username + " não encontrado!");
                    out.println("User or password incorreta!");
                    numTentativas++;
                }
            }

            if(logado) {
                out.println("O numero está entre " + numMin + " e " + numMax);
            }



        } catch (IOException e) {
            System.err.println("Thread " + getName() + " Erro na criação do buffers do socket ");
            phaser.arriveAndAwaitAdvance();
            System.exit(3);
        }

        phaser.arriveAndAwaitAdvance();

    }

}
