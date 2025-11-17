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

    private final static int numThreads = 12;
    private final static int socket_timeout_limit = 40000;
    private final static Phaser phaser = new Phaser();
    private final static Semaphore semaphore = new Semaphore(0);
    private final static int Max_Time_Game = 40;
    private static volatile boolean Game_End = false;

    //Caminho absoluto para o ficheiro csv
    private final static String userFilePath = System.getProperty("user.dir") + "\\src\\Servidor\\utilizadores.csv";

    public static boolean authenticateUsers(String username, String password) {

        try( BufferedReader br = new BufferedReader(new FileReader(userFilePath, StandardCharsets.ISO_8859_1))) {

            String line;
            //Executa até chegar ao final do arquivo
            while((line = br.readLine()) != null ) {
                 String[] tokens = line.split(";");

                 if(tokens[0].equals(username) && tokens[1].equals(password)) {
                     // updateLoginStatus(username, 1);
                     //Utilizador encontrado
                     return true;

                 }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar arquivo: " + userFilePath);
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
                       System.out.println("Usuario já esta logado");
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
           System.out.println("Erro ao escrever no arquivo: " + userFilePath);
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
            System.out.println("Erro ao ler o arquivo: " + userFilePath);
        }

        //Escrever no arquivo

        try( BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))){

            for(String linha1 : linhas) {
                bw.write(linha1);
                bw.newLine();
            }
        }catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo: " + userFilePath);
        }

    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numberPort;
        int numberPlayers = 0;
        int minNumber, maxNumber;
        int randomNumber;

        numberPort = InputValidation.validateIntBetween(sc, "Introduza o numero do porto que o servidor vai escutar " +
                "entre (1024 e 65535): ", 1024, 65535);


        //int[] number = Array.newRandomArray(sc);

        do{
            minNumber = InputValidation.validateInt(sc, "Insert the min number: ");
            maxNumber = InputValidation.validateInt(sc, "Insert the max number: ");

            if(minNumber > maxNumber) {
                System.out.println("Max must be greater than min");
            }

        }while(minNumber > maxNumber);

        Random rand = new Random();

        randomNumber = rand.nextInt(minNumber, maxNumber + 1);

        System.out.println("Teste number: " + randomNumber);

        sc.close();

        updateLoginStatusEndGame();

        try (
                //Cria um servidor e recebe uma porta para escutar as conexões.
                ServerSocket serverSocket = new ServerSocket(numberPort);

                //Cria uma pool (conjunto de threads). Recebe um número de threads predefinido
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        ) {

            //Tempo limite para aceitar novos jogadores(conexões).
            serverSocket.setSoTimeout(socket_timeout_limit);

            while (true) {

                System.out.println("Aguardando jogador...");

                try{

                    //Espera no máximo o tempo de (socket_timeout_limit) por um novo jogador (conexão).
                    Socket jogadorSocket = serverSocket.accept();
                    numberPlayers++;

                    System.out.println("Jogador " + numberPlayers + " conectado!");

                    //Exectutar uma nova tarefa em uma thread disponivel.
                    //O phaser coordena ou sincroniza as threads e o semaforo limita o acesso.
                    executor.execute(new ServerThread(jogadorSocket, phaser, semaphore, minNumber, maxNumber, randomNumber));

                } catch (SocketTimeoutException e) {
                    System.err.println("Erro: Tempo limite para aceitar novos jogadores atingido");

                    //Libera todas as threads bloqueadas.
                    semaphore.release(numberPlayers);
                    //Fecha a thread pool do executorService
                    executor.shutdownNow();

                    //updateLoginStatusEndGame();

                    break;
                }

            }

            //Tempo máximo do jogo
            if(!executor.awaitTermination(Max_Time_Game, TimeUnit.SECONDS)) {
                Game_End = true;
                System.out.println("O tempo de jogo terminou!");

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