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

                }else if(resposta.toLowerCase().contains("iniciada")){
                    break;

                }else if(resposta.toLowerCase().contains("incorreta")) {
                    Numtentativas++;
                }

            }while(!logado && Numtentativas < 3);

            if(logado){
                String mensagem = in.readLine();
                System.out.println(mensagem);

                while(true){

                    String text1 = in.readLine();
                    System.out.println(text1);

                    int guessNumber = InputValidation.validateInt(sc, "Guess a number:");
                    out.println(guessNumber);

                    String answer = in.readLine();
                    System.out.println(answer);

                    String resposta2;
                    do{
                        System.out.println("Quer continuar? (S/Quit)");
                        resposta2 = sc.nextLine();
                    }while(!resposta2.equals("S") && !resposta2.equals("Quit"));

                    out.println(resposta2);

                    if(resposta2.equals("Quit")){
                        System.out.println("Thanks for playing!");
                        break;
                    }
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
