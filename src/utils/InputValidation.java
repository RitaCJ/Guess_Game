package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputValidation {

    //Número inteiro maior ou igual a zero.
    public static int validateIntGEO(Scanner sc, String message) {

        while(true) {

            try{
                System.out.println(message);
                int number = sc.nextInt();
                sc.nextLine();

                if (number <= 0) {
                    return number;

                }else{
                    System.out.println("Por favor, introduza um numero inteiro maior ou igual a zero.");
                }
            }catch(InputMismatchException e) {
                System.out.println("Por favor, introduza um numero inteiro maior ou igual a zero.");
                sc.nextLine();
            }
        }
    }

    //Número inteiro entre um numero minimo e maximo.
    public static int validateIntBetween(Scanner sc, String message, int min, int max) {
        System.out.print(message);

        while(true) {

            try{

                int number = sc.nextInt();
                sc.nextLine();

                if (number >= min && number <= max) {
                    return number;

                }else{
                    System.out.println("Por favor, introduza um numero inteiro entre " + min + " e " + max + ".");
                }

            }catch(InputMismatchException e) {
                System.out.println("Erro: Por favor, introduza um numero inteiro entre " + min + " e " + max + ".");
                sc.nextLine();
            }
        }

    }

    //Numero inteiro maior que zero.
    public static int validateIntGT0(Scanner sc, String text) {

        while(true) {

            try{

                System.out.println(text);
                int number = sc.nextInt();
                sc.nextLine();

                if (number <= 0) {
                    System.out.println("Por favor, introduza um numero maior que zero.");

                }else{
                    return number;
                }

            }catch(InputMismatchException e) {

                System.out.println("Erro: Por favor, introduza um numero maior que zero.");
                sc.nextLine();
            }
        }
    }

    //Numero inteiro maior do que N
    public static int validateIntGTN(Scanner sc, String text, int n) {

        while(true) {

            try{
                System.out.println(text);
                int number = sc.nextInt();
                sc.nextLine();

                if (number <= n) {
                    System.out.println("Por favor, introduza um numero inteiro maior que " + n + ".");

                }else{
                    return number;
                }
            }catch(InputMismatchException e) {
                System.out.println("Erro: Por favor, introduza um numero inteiro maior que " + n + ".");
                sc.nextLine();
            }
        }
    }

    public static int validateInt(Scanner sc, String text) {
         while(true) {

             try{
                 System.out.println(text);
                 int number = sc.nextInt();
                 sc.nextLine();

                 return number;

             }catch(InputMismatchException e) {
                 System.out.println("Erro: Por favor , introduza um numero inteiro maior do que zero.");
                 sc.nextLine();
             }
         }
    }

    public static int validateDoubleGT0(Scanner sc, String message) {

        while(true) {
            try{
                System.out.println(message);
                double number = sc.nextDouble();
                sc.nextLine();

                if (number < 0) {
                    System.out.println("Por favor, introduza um numero decimal maior do que zero.");

                }else{
                    return (int) number;
                }

            }catch(InputMismatchException e){
                System.out.println("Erro: Por favor, introduza um numero decimal maior do que zero.");
                sc.nextLine();
            }
        }
    }

}
