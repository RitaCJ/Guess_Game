package Cliente;

import Servidor.ServerThread;
import utils.InputValidation;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Player {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Introduza o hostname do servidor: ");
        String hostname = sc.nextLine();

        int portNumber = InputValidation.validateIntBetween(sc, "Introduza o numero do porto em que o servidor " +
                "esta a escutar (entre 1024 e 65535): ", 1024, 65535);


        try(
                Socket jogadorSocket = new Socket(hostname, portNumber);

                PrintWriter out = new PrintWriter(jogadorSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(jogadorSocket.getInputStream()))

                ){

            boolean logado = false;
            int Numtentativas = 0;

            do{
                System.out.println("Username: ");
                String username = sc.nextLine();
                out.println(username);

                System.out.println("Password: ");
                String password = sc.nextLine();
                out.println(password);

                String resposta = in.readLine();
                if(resposta == null){
                    System.out.println("Server desconnect");
                    break;
                }

                System.out.println(resposta);

                if(resposta.toLowerCase().contains("logado")) {
                    logado = true;

                }else if(resposta.toLowerCase().contains("incorreta")) {
                    Numtentativas++;
                }

            }while(!logado && Numtentativas < 3);

            int numeroCerto;
            if(logado){
                String mensagem = in.readLine();
                System.out.println(mensagem);

                System.out.println("Guess a number: ");
                int guessNumber = sc.nextInt();

                numeroCerto = Integer.parseInt(in.readLine());

                if(guessNumber == numeroCerto){
                    System.out.println("Congratulations!");
                }

            }


        } catch (UnknownHostException e) {
            System.err.println("Host unknown: " + hostname);
            System.exit(2);
        }catch (IOException e){
            System.err.println("Erro de IO: " + e.getMessage());
            System.exit(3);
        }finally {
            sc.close();
        }

    }
}
