package TestGame;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Socket sock = null;
        InputStream instr = null;
        OutputStream outint = null;
        byte[] buff = new byte[1024];
        Card[] cards = new Card[7];
        int change, m;
        StringBuilder receivedData = new StringBuilder();
        int count = 1;
        int[] percent = new int[7];
        int ran;

        percent[0] = 10;
        percent[1] = 40;
        percent[2] = 10;
        percent[3] = 12;
        percent[4] = 3;
        percent[5] = 15;
        percent[6] = 10;
        cards[0] = new Card("大剣　　", 7, 0, 0);
        cards[1] = new Card("剣　　　", 5, 0, 1);
        cards[2] = new Card("反射剣　", 5, 0, 2);
        cards[3] = new Card("回復薬　", 0, 7, 3);
        cards[4] = new Card("反射薬　", 0, 7, 4);
        cards[5] = new Card("毒薬　　", 0, 0, 5);
        cards[6] = new Card("解毒薬　", 0, 0, 6);

        ArrayList<Integer> hund = new ArrayList<Integer>(5);
        for (int i = 0; i < 5; i++) {
            ran = (int) (Math.random() * 100);
            if(0 <= ran && ran < percent[0] ){
                hund.add(0);
            }else if(percent[0] <= ran && ran < percent[0]+percent[1] ){
                hund.add(1);
            }else if( percent[0]+percent[1]<= ran && ran < percent[0]+percent[1]+percent[2]){
                hund.add(2);
            }else if(percent[0]+percent[1]+percent[2] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]){
                hund.add(3);
            }else if(percent[0]+percent[1]+percent[2]+percent[3] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]+percent[4]){
                hund.add(4);
            }else if(percent[0]+percent[1]+percent[2]+percent[3]+percent[4] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]+percent[4]+percent[5]){
                hund.add(5);
            }else {
                hund.add(6);
            }
        }

        try {
            sock = new Socket(args[0], Integer.parseInt(args[1]));
            instr = sock.getInputStream();
            outint = sock.getOutputStream();
        } catch (Exception e) {
            System.err.println("ネットワークエラーです");
            System.exit(1);
        }

        while (true) {
            System.out.println();
            System.out.println("　待機中...");
            System.out.println();

            // 受信
            try {
                int n = 0;
                String data;
                System.out.println();
                System.out.println(" -------------------- [ Turn "+count+" ] --------------------");
                System.out.println();
                System.out.println();
                System.out.println();


                while ((n = instr.read(buff)) != -1) {
                    System.out.write(buff, 0, n);
                    data = new String(buff, 0, n);
                    receivedData.append(data);

                    if (receivedData.toString().endsWith("You win.\n") || receivedData.toString().endsWith("You lose.\n")) {
                        outint.close();
                        sock.close();
                        instr.close();
                        System.exit(1);
                    }
                    if (receivedData.toString().endsWith(" <  Poison Level [1]  >\n")) {
                    } else if (receivedData.toString().endsWith(" <  Poison Level [2]  >\n") ) {
                    } else if (receivedData.toString().endsWith(" <  Poison Level [3]  >\n") ) {
                    } else if (receivedData.toString().endsWith("\n")) {
                        break;
                    }
                }
                System.out.println();
                System.out.println();
                System.out.println("");
                // カード表示
                for (int i = 0; i < 5; i++) {
                    System.out.println(" hand number[" + i + "]" + " card number[" + cards[hund.get(i)].num + "] | " +
                            cards[hund.get(i)].name + " Attack power:" + cards[hund.get(i)].at + " Recovery amount:" + cards[hund.get(i)].he);
                }

                // カード入力
                while (true) {
                    System.out.println();
                    System.out.print("　Choose your hand number  → ");
                    change = sc.nextInt();
                    System.out.print("　Choose your card number  → ");
                    m = System.in.read(buff);
                    String check = new String(buff, 0, m).trim();
		    if(change>=5){
			System.out.println();
                        System.out.println("　!!!!!入力ミス!!!!!");
			continue;
		    }
		    if(Integer.parseInt(check)>=7){
			System.out.println();
                        System.out.println("　!!!!!入力ミス!!!!!");
			continue;
		    }
                    if (hund.get(change) != Integer.parseInt(check)) {
                        System.out.println();
                        System.out.println("　!!!!!入力ミス!!!!!");
                    }else {
                        break;
                    }
                }
                System.out.println("");
                System.out.println(" Damage done      : "+cards[hund.get(change)].at);
                System.out.println(" Recovery amount　: "+cards[hund.get(change)].he);



                outint.write(buff, 0, m);
                hund.set(change, (int) (Math.random() * cards.length));
                ran = (int) (Math.random() * 100);
                if(0 <= ran && ran < percent[0] ){
                    hund.set(change, 0);
                }else if(percent[0] <= ran && ran < percent[0]+percent[1] ){
                    hund.set(change, 1);
                }else if( percent[0]+percent[1]<= ran && ran < percent[0]+percent[1]+percent[2]){
                    hund.set(change, 2);
                }else if(percent[0]+percent[1]+percent[2] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]){
                    hund.set(change, 3);
                }else if(percent[0]+percent[1]+percent[2]+percent[3] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]+percent[4]){
                    hund.set(change, 4);
                }else if(percent[0]+percent[1]+percent[2]+percent[3]+percent[4] <= ran && ran < percent[0]+percent[1]+percent[2]+percent[3]+percent[4]+percent[5]){
                    hund.set(change, 5);
                }else {
                    hund.add(6);
                }







                while ((n = instr.read(buff)) != -1) {
                    System.out.write(buff, 0, n);
                    data = new String(buff, 0, n);
                    if (receivedData.toString().endsWith(" -------------------- [ Turn End ] --------------------\n")) {
                        System.out.println();
                        System.out.println();
                        System.out.println();
                    }
                    receivedData.append(data);
                    //見やすくするため
                    if (receivedData.toString().endsWith("\n\nYou win.\n")){
                        outint.close();
                        sock.close();
                        instr.close();
                        System.exit(1);
                    } else if (receivedData.toString().endsWith(" detoxification\n") ) {
                    } else if (receivedData.toString().endsWith(" Poison damage : 1\n") ) {
                    } else if (receivedData.toString().endsWith(" Poison damage : 2\n") ) {
                    } else if (receivedData.toString().endsWith(" Poison damage : 3\n") ) {
                    } else if (receivedData.toString().endsWith(" Counter Damage\n") ) {
                    } else if (receivedData.toString().endsWith(" -------------------- [ Turn End ] --------------------\n")) {
                        break;
                    }
                }



                count ++;
                System.out.println();

            } catch (Exception e) {
                System.out.println("　エラー");
                System.exit(1);
            }
        }
    }
}


//javac -encoding UTF-8 TestGame/Client.java
//java TestGame/Client 172.30.96.1 6000