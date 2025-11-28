package Servidor;

import utils.Array;
import utils.InputValidation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {

    private final static int numThreads = 5;
    private final static int socket_timeout_limit = 40000;
    private final static Phaser phaser = new Phaser();
    private final static Semaphore semaphore = new Semaphore(0);
    private final static int Max_Time_Game = 40;
    public static volatile boolean Game_End = false;
    public static volatile boolean Time_End = false;
    public static volatile boolean winner = false;
    public static volatile String userWinner = null;

    //Absolute path to the CSV file
    private final static String userFilePath = System.getProperty("user.dir") + "\\src\\Servidor\\utilizadores.csv";

    public static boolean authenticateUsers(String username, String password) {

        try( BufferedReader br = new BufferedReader(new FileReader(userFilePath, StandardCharsets.ISO_8859_1))) {

            String line;
            //Execute until the end of the file is reached.
            while((line = br.readLine()) != null ) {
                 String[] tokens = line.split(";");

                 if(tokens[0].equals(username) && tokens[1].equals(password)) {
                     // updateLoginStatus(username, 1);
                     //User found.
                     return true;

                 }
            }

        } catch (Exception e) {
            System.err.println("Error loading file: " + userFilePath);
            return false;
        }

        return false;

    }

    public static int updateLoginStatus(String username, int status) {

        List<String> linhas = new ArrayList<>();

       try( BufferedReader br = new BufferedReader(new FileReader(userFilePath, StandardCharsets.ISO_8859_1))){

           String linha;
           while((linha = br.readLine()) != null) {
               String[] tokens = linha.split(";");
               if(tokens[0].equals(username)) {
                   if(!tokens[2].equals(String.valueOf(status))) {
                       tokens[2] = String.valueOf(status);
                       linha = String.join(";", tokens);

                   }else{
                       System.out.println("The user is already logged in.");
                       return 0;
                   }
               }

               linhas.add(linha);
           }

       } catch (Exception e) {
           System.out.println("Erro ao ler o arquivo: " + userFilePath);
       }

        //Escrever no arquivo

       try( BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))){

           for(String linha1 : linhas) {
               bw.write(linha1);
               bw.newLine();
           }
       }catch (Exception e) {
           System.out.println("Error loading file: " + userFilePath);
       }
       return 1;
    }

    public static void updateLoginStatusEndGame() {

        List<String> linhas = new ArrayList<>();

        try( BufferedReader br = new BufferedReader(new FileReader(userFilePath, StandardCharsets.ISO_8859_1))){

            String linha;
            while((linha = br.readLine()) != null) {
                String[] tokens = linha.split(";");
                tokens[2] = String.valueOf(0);
                linha = String.join(";", tokens);

                linhas.add(linha);
            }

        } catch (Exception e) {
            System.out.println("Error loading file: " + userFilePath);
        }

        //Write to file.

        try( BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))){

            for(String linha1 : linhas) {
                bw.write(linha1);
                bw.newLine();
            }
        }catch (Exception e) {
            System.out.println("Error loading file: " + userFilePath);
        }

    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numberPort;
        int numberPlayers = 0;
        int minNumber, maxNumber;
        int randomNumber;

        numberPort = InputValidation.validateIntBetween(sc, "Enter the port number that the server will listen on " +
                "between(1024 and 65535): ", 1024, 65535);

        do{
            minNumber = InputValidation.validateInt(sc, "Insert the min number: ");
            maxNumber = InputValidation.validateInt(sc, "Insert the max number: ");

            if(minNumber > maxNumber) {
                System.out.println("Max must be greater than min");
            }

        }while(minNumber > maxNumber);

        Random rand = new Random();

        randomNumber = rand.nextInt(minNumber, maxNumber + 1);

        System.out.println("The random number: " + randomNumber);

        sc.close();

        updateLoginStatusEndGame();

        try (
                //Create a server and receive a port to listen for connections.
                ServerSocket serverSocket = new ServerSocket(numberPort);

                //Creates a thread pool (set of threads). Receives a predefined number of threads.
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        ) {

            //Time limit for accepting new players (connections).
            serverSocket.setSoTimeout(socket_timeout_limit);

            while (true) {

                System.out.println("Waiting for player...");

                try{

                    //Wait a maximum of (socket_timeout_limit) for a new player (connection).
                    Socket jogadorSocket = serverSocket.accept();
                    numberPlayers++;

                    System.out.println("Player " + numberPlayers + " connected!");

                    //Execute a new task in an available thread.
                    //The phaser coordinates or synchronizes the threads, and the semaphore limits access.
                    executor.execute(new ServerThread(jogadorSocket, phaser, semaphore, minNumber, maxNumber, randomNumber));

                } catch (SocketTimeoutException e) {
                    System.err.println("Error: Time limit for accepting new players reached.");
                    Time_End = true;

                    semaphore.release(numberPlayers);
                    //Closes the executorService thread pool.

                    serverSocket.close();
                    executor.shutdownNow();
                    break;
                }

            }

            //Tempo máximo do jogo
            if(!executor.awaitTermination(Max_Time_Game, TimeUnit.SECONDS)) {
                Game_End = true;
                System.err.println("O tempo de jogo terminou!");
            }

        } catch (IOException e) {

            System.err.println("Erro no Servidor(Main): Ocorreu um erro de I/O ao tentar criar o socket " +
                    "no porto " + numberPort);

            //Termina. Erro de I/O
            System.exit(2);

        } catch (InterruptedException e) {
            System.err.println("Erro no servidor (Main): Ocorreu um erro em awaitTermination");

            //Termina.
            System.exit(3);
        }
    }
}