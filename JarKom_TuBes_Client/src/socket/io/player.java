package socket.io;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.Color;
import board.Game;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class player {
    public Socket socket;
    private int number;
    private int score = 0;

    public player(String url, Game field) {
        try {
            socket = IO.socket(url);
            
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    System.out.println("Client connected");
                }
            });
            
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    System.out.println("Client disconnect");
                }
            });
            
            socket.on("chat", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int pl = Integer.parseInt(os[0].toString())+1;
                    System.out.println("Message From Player-" + pl + ": " + os[1].toString());
                    board.Game.tA.append("[P" + pl + "]: "+ os[1].toString() +'\n');
                    field.tF.setText("");
                }
            });
            
            socket.on("create", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    System.out.println("Player Number: " + os[0].toString());
                    number = Integer.parseInt(os[0].toString());
                    
                    if(number == 0)  field.pnlTurn1.setBackground(Color.BLUE);
                    else if(number == 1) field.pnlTurn1.setBackground(Color.RED);
                    else if(number == 2) field.pnlTurn1.setBackground(Color.GREEN);
                    else if(number == 3) field.pnlTurn1.setBackground(Color.CYAN);
                }
            });
            
            socket.on("checkpoint", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int choosenBlock = Integer.parseInt(os[1].toString());
                    int playerNumber = Integer.parseInt(os[2].toString());
                    score = Integer.parseInt(os[0].toString());
                    Color color = Color.white;
                    int tambah = 0;
                    switch(playerNumber){
                            case 0 : color = Color.BLUE; break;
                            case 1 : color = Color.red; break;
                            case 2 : color = Color.green; break;
                            case 3 : color = Color.cyan; break;
                    }
                    
                    System.out.println("Block Number: " + choosenBlock);
                    //CHECK HORIZONTAL
                    if(choosenBlock%10 >= 1 && choosenBlock%10 <= 8){ //CHECK 1 BLOCK IN THE LEFT SIDE & RIGHT SIDE
                        if(field.pnl[choosenBlock-1].getBackground().equals(color) && field.pnl[choosenBlock+1].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }
                    if(choosenBlock%10 >= 2){ //CHECK 2 BLOCK IN THE LEFT SIDE
                        if(field.pnl[choosenBlock-1].getBackground().equals(color) && field.pnl[choosenBlock-2].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }
                    if(choosenBlock%10 <= 7){ //CHECK 2 BLOCK IN THE RIGHT SIDE
                        if(field.pnl[choosenBlock+1].getBackground().equals(color) && field.pnl[choosenBlock+2].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }

                    //CHECK VERTICAL
                    if(choosenBlock >= 10 && choosenBlock <= 89){ //CHECK 1 BLOCK IN THE UPPER SIDE & LOWER SIDE
                        if(field.pnl[choosenBlock-10].getBackground().equals(color) && field.pnl[choosenBlock+10].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }
                    if(choosenBlock >= 20){ //CHECK 2 BLOCK IN THE UPPER SIDE
                        if(field.pnl[choosenBlock-10].getBackground().equals(color) && field.pnl[choosenBlock-20].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }
                    if(choosenBlock <= 79){ //CHECK 2 BLOCK IN THE LOWER SIDE
                        if(field.pnl[choosenBlock+10].getBackground().equals(color) && field.pnl[choosenBlock+20].getBackground().equals(color)){
                            tambah+= 10;
                        }
                    }

                    //CHECK DIAGONAL
                    if(choosenBlock/10<=7 && choosenBlock%10<=7){ //CHECK 2 BLOCK TO DOWN RIGHT
                        if(field.pnl[choosenBlock+11].getBackground().equals(color) && field.pnl[choosenBlock+22].getBackground().equals(color)){
                            tambah += 10;
                        }
                    }
                    if(choosenBlock/10>=2 && choosenBlock%10>=2){ //CHECK 2 BLOCK TO UP LEFT
                        if(field.pnl[choosenBlock-11].getBackground().equals(color) && field.pnl[choosenBlock-22].getBackground().equals(color)){
                            tambah += 10;
                        }
                    }
                    if(choosenBlock/10>=2 && choosenBlock%10<=7){ //CHECK 2 BLOCK TO UP RIGHT
                        if(field.pnl[choosenBlock-9].getBackground().equals(color) && field.pnl[choosenBlock-18].getBackground().equals(color)){
                            tambah += 10;
                        }
                    }
                    if(choosenBlock/10<=7 && choosenBlock%10>=2){ //CHECK 2 BLOCK TO DOWN LEFT
                        if(field.pnl[choosenBlock+9].getBackground().equals(color) && field.pnl[choosenBlock+18].getBackground().equals(color)){
                            tambah += 10;
                        }
                    }
                    if((choosenBlock/10<=8 && choosenBlock%10<=8)&&(choosenBlock/10<=8 && choosenBlock%10>=1)&&(choosenBlock/10>=1 && choosenBlock%10<=8)&&(choosenBlock/10>=1 && choosenBlock%10>=1)){
                        if(field.pnl[choosenBlock+9].getBackground().equals(color) && field.pnl[choosenBlock-9].getBackground().equals(color)){
                            tambah += 10;
                        }
                        if(field.pnl[choosenBlock-11].getBackground().equals(color) && field.pnl[choosenBlock+11].getBackground().equals(color)){
                            tambah += 10;
                        }
                    }
                    tambah += 1;
                    socket.emit("setpointplus", playerNumber, tambah);
                }
            });
            
            socket.on("kick", new Emitter.Listener() {

                @Override
                public void call(Object... os) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,"Permainan sudah dimulai");
                    field.dispose();
                }
            });
            
            socket.on("setscore", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    score = Integer.parseInt(os[0].toString());
                    int playerNumber = Integer.parseInt(os[1].toString());
                    System.out.println("\tScore Player-" + playerNumber + ": " + score);
                    field.rS[playerNumber].setText(Integer.toString(score));
                    int count=0;
                    for(int i = 0 ; i < 100 ; i++){
                        if(!field.pnl[i].getBackground().equals(Color.white)){
                            count++;
                        }
                    }
                    if(count==100){
                        playerNumber++;
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame,"Pemenang player ke : "+playerNumber);
                        field.dispose();
                        disconnect();
                    }
                }
            });
            
            socket.on("choosen", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int choosenBlock = Integer.parseInt(os[0].toString());
                    int turn = Integer.parseInt(os[1].toString());
                    Color color = Color.white;
                    int tambah = 0;
                    if(field.pnl[choosenBlock].getBackground().equals(Color.white)){
                        switch(turn){
                            case 0 : field.pnl[choosenBlock].setBackground(Color.BLUE); color = Color.BLUE; break;
                            case 1 : field.pnl[choosenBlock].setBackground(Color.red); color = Color.red; break;
                            case 2 : field.pnl[choosenBlock].setBackground(Color.green); color = Color.green; break;
                            case 3 : field.pnl[choosenBlock].setBackground(Color.cyan); color = Color.cyan; break;
                        }                                                                       
                    }                    
                    int numberPlayer = Integer.parseInt(os[2].toString());                    
                    
                    if(turn == 0){
                        if(turn >= numberPlayer-1) field.pnlTurn2.setBackground(Color.BLUE);
                        else field.pnlTurn2.setBackground(Color.RED);
                    } else if(turn == 1){
                        if(turn >= numberPlayer-1) field.pnlTurn2.setBackground(Color.BLUE);
                        else field.pnlTurn2.setBackground(Color.GREEN);
                    } else if(turn == 2){ 
                        if(turn >= numberPlayer-1) field.pnlTurn2.setBackground(Color.BLUE);
                        else field.pnlTurn2.setBackground(Color.CYAN);
                    } else if(turn == 3) field.pnlTurn2.setBackground(Color.BLUE);
                }
            });
            
            socket.on("choose", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    System.out.println(os[0].toString());
                }
            });
            
            socket.on("end", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int urutan = Integer.parseInt(os[0].toString());
                    int max = Integer.parseInt(os[1].toString());
                    System.out.println("Pemenang : player ke : " + urutan + " dengan score : " + max);
                    JFrame frame = new JFrame();
                    urutan++;
                    JOptionPane.showMessageDialog(frame,"Pemenang player ke : "+urutan);
                    field.dispose();
                    System.exit(0);
                    disconnect();
                }
            });
          
            
            
            socket.connect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void create(){
        socket.emit("create", "");
    }
    
    public void disconnect(){
        socket.disconnect();
    }
    
    public void choose(int x){
        System.out.println(this.number);
        socket.emit("choose", this.number, x);
    }
    public void win(int x){
        socket.emit("winner",this.number);
    }
    public void send(String msg){
        socket.emit("chat", this.number, msg);
    }
}