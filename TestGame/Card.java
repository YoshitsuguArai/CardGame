package TestGame;

public class Card {
    String name;
    public int at;//攻撃値
    public int he;//回復値
    public int num;//カードナンバー
    public Card(String name,int at, int he,int num){
        this.name=name;
        this.at=at;
        this.he=he;
        this.num=num;
    }
}
