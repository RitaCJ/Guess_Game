package utils;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Array {

    //Array de números inteiros
    public static int[] newRandomArray(Scanner sc) {

        int numMin = 0;
        int numMax = 0;
        int num = 0;

        boolean correctInput = false;

        while(correctInput == false) {

            try{

                System.out.println("Introduza o valor minimo aleatorio:");
                numMin = sc.nextInt();
                //Limpa o buffer.
                sc.nextLine();
                correctInput = true;

            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;

        while(correctInput == false){

            try{

                System.out.println("Introduza o valor maximo aleatorio:");
                numMax = sc.nextInt();
                sc.nextLine();

                if(numMax < numMin) {
                    System.out.println("O numero maximo deve ser maior que o numero minimo.");
                }else{
                    correctInput = true;
                }
            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;

        while(correctInput == false){

            try{

                System.out.println("Introduza o numero de elementos aleatorios:");
                num = sc.nextInt();
                sc.nextLine();

                if(num <= 0) {
                    System.out.println("O array precisa ter elementos");

                }else{
                    correctInput = true;
                }
            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        //Array
        int[] randomArray = new int[num];

        //Gerador de numero aleatorios
        Random rand = new Random();

        //Preencher o array com valores aleatorios
        for(int i = 0; i < num; i++) {
            randomArray[i] = rand.nextInt(numMax, numMin + 1);
        }

        return randomArray;
    }



    //Valores decimais
    public static double[] newDoubleRandomArray(Scanner sc) {

        double numMin = 0;
        double numMax = 0;
        int num = 0;

        boolean correctInput = false;

        while(correctInput == false) {

            try{

                System.out.println("Introduza o valor minimo aleatorio:");
                numMin = sc.nextDouble();
                //Limpa o buffer.
                sc.nextLine();
                correctInput = true;

            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;

        while(correctInput == false){

            try{

                System.out.println("Introduza o valor maximo aleatorio:");
                numMax = sc.nextDouble();
                sc.nextLine();

                if(numMax < numMin) {
                    System.out.println("O numero maximo deve ser maior que o numero minimo.");
                }else{
                    correctInput = true;
                }
            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor decimal.");
                sc.nextLine();
            }
        }

        correctInput = false;

        while(correctInput == false){

            try{

                System.out.println("Introduza o numero de elementos aleatorios:");
                num = sc.nextInt();
                sc.nextLine();

                if(num <= 0) {
                    System.out.println("O array precisa ter elementos");

                }else{
                    correctInput = true;
                }
            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        //Array
        double[] randomArray = new double[num];

        //Gerador de numero aleatorios
        Random rand = new Random();

        //Preencher o array com valores aleatorios
        for(int i = 0; i < num; i++) {
            randomArray[i] = rand.nextDouble(numMax, numMin + 1);
        }

        return randomArray;
    }

    public static int[] deleteFromArray(int[] array, int value) {

        int counter = 0;
        for(int k : array) {
            if(k == value) {
                counter++;
            }
        }

        int[] newArray = new int[array.length - counter];

        int j = 0;
        for(int k : array) {
            if(k != value) {
                newArray[j++] = k;
            }
        }
        return newArray;
    }

    public static int[] invertArray(int[] array) {

        int[] newArray = new int[array.length];

        for(int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static void invertArrayVoid(int[] array) {
        int temp;

        for(int i = 0; i < array.length / 2; i++) {
            temp = array[array.length - i - 1];
            array[array.length - i - 1] = array[i];
            array[i] = temp;
        }
    }

    public static int[][] newRandom2DArray(Scanner sc) {
        int numMin = 0;
        int numMax = 0;
        int numRows = 0;
        int numColumns = 0;

        boolean correctInput = false;

        while(correctInput == false) {
            try{
                System.out.println("Introduza o valor minimo aleatorio para colocar no array bidimensional: ");
                numMin = sc.nextInt();
                sc.nextLine();
                correctInput = true;

            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;
        while(correctInput == false){
            try{
                System.out.println("Introduza o valor maximo aleatorio para colocar no array bidimensional: ");
                numMax = sc.nextInt();
                sc.nextLine();

                if(numMax < numMin) {
                    System.out.println("O numero maximo deve ser maior que o numero minimo.");
                }else{
                    correctInput = true;
                }

            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;
        while(correctInput == false){

            try{
                System.out.println("Introduza o numero linhas a criar no array bidimensional: ");
                numRows = sc.nextInt();
                sc.nextLine();

                if(numRows < 2) {
                    System.out.println("O array deve ter pelo menos 2 linhas.");
                }else{
                    correctInput = true;
                }

            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        correctInput = false;
        while(correctInput == false){

            try{
                System.out.println("Introduza o numero colunas a criar no array bidimensional: ");
                numColumns = sc.nextInt();
                sc.nextLine();

                if(numColumns < 2) {
                    System.out.println("O array deve ter pelo menos 2 colunas.");
                }else{
                    correctInput = true;
                }
            }catch (InputMismatchException e) {
                System.out.println("Por favor, introduza um valor inteiro.");
                sc.nextLine();
            }
        }

        int[][] random2DArray = new int[numRows][numColumns];
        Random rand = new Random();

        for(int l = 0; l < numRows; l++) {
            for(int c = 0; c < numColumns; c++) {
                random2DArray[l][c] = rand.nextInt(numMin, numMax + 1);
            }
        }

        return random2DArray;
    }

    public static void print2DArray(int[][] array2D) {
        int numRows = array2D.length;
        int numColumns = array2D[0].length;

        for(int[] row : array2D) {
            System.out.println();

            for(int c = 0; c < numColumns; c++) {
                System.out.print("\t" + row[c]);
            }
        }

        System.out.println();
    }
}


