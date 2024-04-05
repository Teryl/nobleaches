package main.java.org.example;
import java.util.concurrent.*;
import java.io.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) {
        int n = 20 ;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        String s = "abcd" ;
        for ( int i = 0 ; i < s.length(); i++) {
        executorService.execute(
        new PrintStr( String.valueOf( s.charAt(i) ) , n));
        }
        executorService.shutdown();
    }
}

class PrintStr implements Runnable {
    String s; int times;
    PrintStr(String s, int times){
    this .s = s; this .times = times;
    }
    @Override
    public void run () {
    for ( int i = 0 ; i < times; i++){
    System.out.print(s + i + " " );
    }
    System.out.println();
    }
   }