package TestGame;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {
        ServerSocket servsock = null;
        Socket sock[] = new Socket[2];
        OutputStream[] out = new OutputStream[2];
        BufferedReader in = null;
        String  outstr2, outstr3, outstr4, outstr5,outstr6,outstr7;
        byte[] data = new byte[1024];
        int playerCn = 0;
        int cardNm;
        String s;
        int turn = (int) (Math.random() * 2);
        Player p1 = new Player(30);
        Player p2 = new Player(30);
        //


        // カード情報
        Card[] cards = new Card[7];
        cards[0] = new Card("大剣　　", 7, 0, 0);
        cards[1] = new Card("剣　　　", 5, 0, 1);
        cards[2] = new Card("反射剣　", 5, 0, 2);
        cards[3] = new Card("回復薬　", 0, 7, 3);
        cards[4] = new Card("反射薬　", 0, 7, 4);
        cards[5] = new Card("毒薬　　", 0, 0, 5);
        cards[6] = new Card("解毒薬　", 0, 0, 6);

        // サーバー接続
        try {
            servsock = new ServerSocket(6000, 300);
            System.out.println("ゲームが起動しました(port=" + servsock.getLocalPort() + ")");
        } catch (IOException e) {
            System.out.println("ゲームを起動できません");
            System.exit(1);
        }

        // プレイヤー接続
        try {
            while (playerCn != 2) {
                sock[playerCn] = servsock.accept();
                System.out.println("接続されました" + sock[playerCn].getRemoteSocketAddress());
                playerCn++;
            }
        } catch (IOException e) {
            System.out.println("接続されませんでした");
            System.exit(1);
        }

        // メイン処理
        try {
            outstr2 = "\n\nYou win.\n";
            outstr5 = "You lose.\n";

            //HP調整
            if(turn%2==0){
                p2.HP += 4;
            }else {
                p1.HP += 4;
            }

            while (true) {
                if ((turn % 2) == 0) {
                    // 通常処理

                    //毒データ送信
                    if (p1.poison == 1 ) {
                        outstr6 = " <  Poison Level [1]  >\n";
                        out[0] = sock[0].getOutputStream();
                        for (int i = 0; i < outstr6.length(); ++i) {
                            out[0].write((int) outstr6.charAt(i));
                        }
                        out[0].flush();
                    }else if (p1.poison == 2 ) {
                        outstr6 = " <  Poison Level [2]  >\n";
                        out[0] = sock[0].getOutputStream();
                        for (int i = 0; i < outstr6.length(); ++i) {
                            out[0].write((int) outstr6.charAt(i));
                        }
                        out[0].flush();
                    }else if (p1.poison == 3 ) {
                        outstr6 = " <  Poison Level [3]  >\n";
                        out[0] = sock[0].getOutputStream();
                        for (int i = 0; i < outstr6.length(); ++i) {
                            out[0].write((int) outstr6.charAt(i));
                        }
                        out[0].flush();
                    }

                    // データを送信
                    outstr3 = "     [ Your HP : " + p1.HP + "   ||   opponent's HP : " + p2.HP + " ]\n";
                    out[0] = sock[0].getOutputStream();
                    for (int i = 0; i < outstr3.length(); ++i) {
                        out[0].write((int) outstr3.charAt(i));
                    }
                    System.out.println();
                    System.out.println("送信完了:" + outstr3);

                    // データを受信
                    in = new BufferedReader(new InputStreamReader(sock[0].getInputStream()));
                    s = in.readLine();
                    System.out.println("受信：" + s);
                    cardNm = Integer.parseInt(s);
                    System.out.println("変換：" + cardNm);
                    p2.HP -= cards[cardNm].at;
                    p1.HP += cards[cardNm].he;

                    //反射
                    if (cardNm == 2 || cardNm == 4) {
                        p1.Counter = true;
                    }
                    if(p2.Counter){
                        p1.HP -= cards[cardNm].at*2;
                        p2.Counter = false;
                        outstr6 = " Counter Damage   : "+cards[cardNm].at*2+"\n";
                        out[0] = sock[0].getOutputStream();
                        for (int i = 0; i < outstr6.length(); ++i) {
                            out[0].write((int) outstr6.charAt(i));
                        }
                        out[0].flush();

                    }

                    //解毒
                    if (cardNm == 6) {
                        p1.poison = 0;
                        outstr6 = " detoxification\n";
                        out[0] = sock[0].getOutputStream();
                        for (int i = 0; i < outstr6.length(); ++i) {
                            out[0].write((int) outstr6.charAt(i));
                        }
                        out[0].flush();

                    }
                    if (cardNm == 5) {
                        if (p2.poison <= 3) {
                            p2.poison += 1;
                        }
                    }
                    if (p1.poison > 0) {
                        if(p1.poison == 1) {
                            outstr6 = " Poison damage    : 2\n";
                            out[0] = sock[0].getOutputStream();
                            for (int i = 0; i < outstr6.length(); ++i) {
                                out[0].write((int) outstr6.charAt(i));
                            }
                            out[0].flush();
                        }else if(p1.poison == 2) {
                            outstr6 = " Poison damage    : 4\n";
                            out[0] = sock[0].getOutputStream();
                            for (int i = 0; i < outstr6.length(); ++i) {
                                out[0].write((int) outstr6.charAt(i));
                            }
                            out[0].flush();
                        }else {
                            outstr6 = " Poison damage    : 6\n";
                            out[0] = sock[0].getOutputStream();
                            for (int i = 0; i < outstr6.length(); ++i) {
                                out[0].write((int) outstr6.charAt(i));
                            }
                            out[0].flush();
                        }
                    }

                    if (p1.poison != 0 && p1.HP>=(p1.poison*2)+1) {
                        p1.HP -= p1.poison*2;
                    }

                    outstr6 = " -------------------- [ Turn End ] --------------------\n";
                    out[0] = sock[0].getOutputStream();
                    for (int i = 0; i < outstr6.length(); ++i) {
                        out[0].write((int) outstr6.charAt(i));
                    }
                    out[0].flush();

                    turn++;

                    if (p2.HP <= 0) {
                        // 勝利処理
                        for (int i = 0; i < outstr2.length(); ++i) {
                            out[0].write((int) outstr2.charAt(i));
                        }
                        for (int i = 0; i < outstr5.length(); ++i) {
                            out[1].write((int) outstr5.charAt(i));
                        }

                        out[1].close();
                        sock[1].close();
                        out[0].close();
                        sock[0].close();
                        System.exit(1);
                    }
                    if (p1.HP <= 0) {
                        // 敗北処理
                        for (int i = 0; i < outstr2.length(); ++i) {
                            out[1].write((int) outstr2.charAt(i));
                        }
                        for (int i = 0; i < outstr5.length(); ++i) {
                            out[0].write((int) outstr5.charAt(i));
                        }

                        out[1].close();
                        sock[1].close();
                        out[0].close();
                        sock[0].close();
                        System.exit(1);
                    }

                } else {
                    // 通常処理

                    //毒データ送信
                    if (p2.poison == 1 ) {
                        outstr7 = " <  Poison Level [1]  >\n";
                        out[1] = sock[1].getOutputStream();
                        for (int i = 0; i < outstr7.length(); ++i) {
                            out[1].write((int) outstr7.charAt(i));
                        }
                        out[1].flush();
                    }else if (p2.poison == 2 ) {
                        outstr7 = " <  Poison Level [2]  >\n";
                        out[1] = sock[1].getOutputStream();
                        for (int i = 0; i < outstr7.length(); ++i) {
                            out[1].write((int) outstr7.charAt(i));
                        }
                        out[1].flush();
                    }else if (p2.poison == 3 ) {
                        outstr7 = " <  Poison Level [3]  >\n";
                        out[1] = sock[1].getOutputStream();
                        for (int i = 0; i < outstr7.length(); ++i) {
                            out[1].write((int) outstr7.charAt(i));
                        }
                        out[1].flush();
                    }

                    // データを送信
                    outstr4 = "     [ Your HP : " + p2.HP + "   ||   opponent's HP : " + p1.HP + " ]\n";
                    out[1] = sock[1].getOutputStream();
                    for (int i = 0; i < outstr4.length(); ++i) {
                        out[1].write((int) outstr4.charAt(i));
                    }
                    out[1].flush();
                    System.out.println();
                    System.out.println("送信完了:" + outstr4);

                    // データを受信
                    in = new BufferedReader(new InputStreamReader(sock[1].getInputStream()));
                    s = in.readLine();
                    System.out.println("受信：" + s);
                    cardNm = Integer.parseInt(s);
                    System.out.println("変換：" + cardNm);
                    p1.HP -= cards[cardNm].at;
                    p2.HP += cards[cardNm].he;

                    //反射
                    if (cardNm == 2 || cardNm == 4) {
                        p2.Counter = true;
                    }

                    if(p1.Counter){
                        p2.HP -= cards[cardNm].at*2;
                        p1.Counter = false;
                        outstr7 = " Counter Damage   : "+cards[cardNm].at*2+"\n";
                        out[1] = sock[1].getOutputStream();
                        for (int i = 0; i < outstr7.length(); ++i) {
                            out[1].write((int) outstr7.charAt(i));
                        }
                        out[1].flush();
                    }

                    if (cardNm == 6) {
                        p2.poison = 0;
                        outstr7 = " detoxification\n";
                        out[1] = sock[1].getOutputStream();
                        for (int i = 0; i < outstr7.length(); ++i) {
                            out[1].write((int) outstr7.charAt(i));
                        }
                        out[1].flush();
                    }
                    if (cardNm == 5) {
                        if (p1.poison <= 3) {
                            p1.poison += 1;
                        }
                    }
                    if(p2.poison > 0){
                        if(p2.poison == 1) {
                            outstr7 = " Poison damage    : 2\n";
                            out[1] = sock[1].getOutputStream();
                            for (int i = 0; i < outstr7.length(); ++i) {
                                out[1].write((int) outstr7.charAt(i));
                            }
                            out[1].flush();
                        }else if(p2.poison == 2) {
                            outstr7 = " Poison damage    : 4\n";
                            out[1] = sock[1].getOutputStream();
                            for (int i = 0; i < outstr7.length(); ++i) {
                                out[1].write((int) outstr7.charAt(i));
                            }
                            out[1].flush();
                        }else {
                            outstr7 = " Poison damage    : 6\n";
                            out[1] = sock[1].getOutputStream();
                            for (int i = 0; i < outstr7.length(); ++i) {
                                out[1].write((int) outstr7.charAt(i));
                            }
                            out[1].flush();
                        }
                    }


                    if (p2.poison != 0 && p2.HP>=(p2.poison*2)+1) {
                        p2.HP -= p2.poison*2;
                    }

                    outstr7 = " -------------------- [ Turn End ] --------------------\n";
                    out[1] = sock[1].getOutputStream();
                    for (int i = 0; i < outstr7.length(); ++i) {
                        out[1].write((int) outstr7.charAt(i));
                    }
                    out[1].flush();

                    turn++;

                    if (p1.HP <= 0) {
                        // 勝利処理
                        for (int i = 0; i < outstr2.length(); ++i) {
                            out[1].write((int) outstr2.charAt(i));
                        }
                        for (int i = 0; i < outstr5.length(); ++i) {
                            out[0].write((int) outstr5.charAt(i));
                        }

                        out[1].close();
                        sock[1].close();
                        out[0].close();
                        sock[0].close();
                        System.exit(1);
                    }
                    if (p2.HP <= 0) {
                        // 敗北処理
                        for (int i = 0; i < outstr2.length(); ++i) {
                            out[0].write((int) outstr2.charAt(i));
                        }
                        for (int i = 0; i < outstr5.length(); ++i) {
                            out[1].write((int) outstr5.charAt(i));
                        }

                        out[1].close();
                        sock[1].close();
                        out[0].close();
                        sock[0].close();
                        System.exit(1);
                    }
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }
}

//javac -encoding UTF-8 TestGame/Server.java
//java TestGame/Server
