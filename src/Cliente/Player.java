package Cliente;

import utils.InputValidation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

            System.out.println("Iniciando servidor...");
            System.out.println(in.readLine());
            String username = sc.nextLine();
            out.println(username);

            System.out.println(in.readLine());
            String password = sc.nextLine();
            out.println(password);

            String resposta = in.readLine();
            System.out.println(resposta);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        sc.close();
    }
}
