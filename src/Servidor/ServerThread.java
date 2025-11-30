package Servidor;

import utils.Array;
import utils.InputValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

       //Register the Threads.
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

            String username = null;

            while(!logado && numTentativas < 3){

                username = in.readLine();
                String password = in.readLine();

                if(Server.authenticateUsers(username, password)) {
                    int login = Server.updateLoginStatus(username, 1);

                    if(login == 1) {
                        System.out.println("User " + username + " connected!");
                        out.println("Successfully logged in!");
                        logado = true;

                    }else if(login == 0) {
                        out.println("Session already started on another computer!");
                        break;
                    }
                }else{
                    System.out.println("User " + username + " not found!");
                    out.println("User or password incorrect!");
                    numTentativas++;
                }
            }

            if(logado){

               //When the time to accept new players ends.
                try{
                    semaphore.acquire();
                }catch (InterruptedException e){

                }

                out.println(randomNumber);
                System.out.println("The player will be informed that the game is about to begin!");
                out.println("The time to accept new players has ended. The game is about to begin.");

                while(!Server.Game_End){

                    if(Server.winner){
                        out.println("The player " + Server.userWinner + " guessed the number correctly! The right number is: " + randomNumber +
                                "The game end!");
                        break;
                    }

                    out.println("The number is between " + numMin + " e " + numMax);

                    int numeroGuess;
                    String msg = in.readLine();

                    if(msg.equals("Quit")){
                        System.out.println("The player " + username +" gave up!");
                        break;
                    }

                    try{
                        numeroGuess = Integer.parseInt(msg);
                        System.out.println("The player chose the number " + numeroGuess);
                    }catch(NumberFormatException e){
                        System.err.println("Error (ServerThread): Invalid number");
                        out.println("Invalid number");
                        continue;
                    }


                    if(numeroGuess == randomNumber) {
                        Server.userWinner = username;
                        System.out.println("The player " + Server.userWinner + " guessed the number correctly!");
                        out.println("You got it right!");
                         Server.winner = true;

                         break;

                    }else if(numeroGuess > randomNumber){
                        System.out.println("The player entered a higher number than " + randomNumber);
                        out.println("Too High!");
                    }else if(numeroGuess < randomNumber){
                        System.out.println("Jogador digitou um numero menor que " + randomNumber);
                        out.println("Too Low!");
                    }

                    //Colocar um if else, e colocar uma flag para verificar se já alguém ganhou.
                    if(Server.Game_End){
                        System.out.println("O tempo de jogo terminou");
                        out.println("Jogador tempo de jogo terminou");
                       // out.println("Exit");
                        break;
                    }

                }
            }


        } catch (IOException e) {
            System.err.println("Thread " + getName() + " Erro na criação do buffers do socket ");
            phaser.arriveAndAwaitAdvance();
            System.exit(3);
        }

        phaser.arriveAndAwaitAdvance();

    }

}
