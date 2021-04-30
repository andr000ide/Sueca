package com.example.sueca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.TypedValue;

public class Carta {
    public boolean tocada=false;
    public boolean mover = false;
    public int xMover,yMover;
    private int x;
    private int y;
    public String nome;
    private int xSpeed = 10;
    private int aux2;
    private int ySpeed = 10;


    private GameView gameView;
    private Bitmap bmp;
    private Bitmap back;



    public Carta(GameView gameView, Bitmap bmp,String nome,Bitmap back){
        this.gameView=gameView;
        this.bmp=bmp;
        this.nome=nome;
        this.back=back;
    }

    public Bitmap getCarta(){
        return bmp;
    }


    public boolean isColliding(int xT,int yT,int i) {
        if (i == 9) {
            if (between(x, x + bmp.getWidth(), xT) && between(y, y + bmp.getHeight(), yT)) {
                return true;
            }
        }
        else {
            if (between(x, x + aux2, xT) && between(y, y + bmp.getHeight(), yT)) {
                return true;
            }
        }
        return false;
    }

    public boolean between(int min,int max,int numero){
        if(numero<=max && numero >= min){
            return true;
        }
        return false;
    }

    public void update() {
        if(x> gameView.getWidth() - bmp.getWidth() - xSpeed){
            xSpeed = -xSpeed;
        }
        if(x+xSpeed<0){
            xSpeed= 5;
        }
        x=x+xSpeed;

        if( y>gameView.getHeight() - bmp.getHeight()-ySpeed){
            ySpeed = -ySpeed;
        }
        if(y+ySpeed < 0){
            ySpeed = 5;
        }
        y=y+ySpeed;
    }

    public boolean isPlayed(){
        if(yMover < (gameView.getHeight()/1.5)){
            return true;
        }


        return false;
    }

    public void posicao(int angle,int position,int numeroCartas){
        if(tocada==false){
            int height = gameView.getHeight();
            int width = gameView.getWidth();
            int height_carta = bmp.getHeight();
            int width_carta = bmp.getWidth();
            int start;

            if(numeroCartas==1){
                this.x = ((width/2)-(width_carta/2));
            }
            else if(numeroCartas == 2){
                int aux = width-(50*2);

                int aux3 = aux-(width_carta*3);

                int ondecomeca = aux3/2;

                if(position==0){
                    this.x= (50+ondecomeca);
                }
                if(position==1){
                    this.x = (50+ondecomeca+(width_carta*2));
                }
            }
            else{
                int aux = width-(50*2);

                start = (aux/numeroCartas);

                int max = start*numeroCartas;

                int tamanhocartas = max-width_carta;

                int tamanhocada = tamanhocartas/(numeroCartas-1);

                //int aux333 = ((width-max)/2);

                int ondecomeca = aux/start;

                aux2 = start;
                this.x = (50+ondecomeca+(position*tamanhocada));
            }
            this.y = height-height_carta;
        }
        else{
        }
    }

    public void onDraw(Canvas canvas,int angle,int position,int numeroCartas){
        //update();
        //canvas.drawColor(Color.WHITE);
        //Matrix matrix = new Matrix();
        //matrix.setRotate(angle, x, y);
        //canvas.drawBitmap(bmp, matrix, null);

        posicao(angle,position,(numeroCartas));

        int height = gameView.getHeight();
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();

        if(mover){
            canvas.drawBitmap(bmp,(xMover-(width_carta/2)),(yMover-(height_carta/2)),null);
        }
        else{
            canvas.drawBitmap(bmp,x,y,null);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public void onDrawTrunfo(Canvas canvas){
        //update();
        //canvas.drawColor(Color.WHITE);
        //Matrix matrix = new Matrix();
        //matrix.setRotate(angle, x, y);
        //canvas.drawBitmap(bmp, matrix, null);


        int height = gameView.getHeight();
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();
            canvas.drawBitmap(getResizedBitmap(bmp,114,176),20,20,null);
    }

    //onDraw da jogada
    public void onDraw2(Canvas canvas,String jogador){
        int height = gameView.getHeight();
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();
        int xaux=0;
        int yaux=0;
        switch (jogador){
            case "player":{
                xaux = (width/2 - (width_carta/2));
                yaux = (height/2 + (height_carta/2));
                break;
            }
            case "bot1":{
                xaux = ((width/2) + width_carta/2);
                yaux = (height/2 - height_carta/2);
                break;
            }
            case "teammate":{
                xaux = (width/2 - (width_carta/2));
                yaux = (height/2 - (height_carta/2) - height_carta);
                break;
            }
            case "bot2":{
                xaux = ((width/2) - width_carta - (width_carta/2));
                yaux = (height/2 - height_carta/2);
                break;
            }
        }
        canvas.drawBitmap(bmp,xaux,yaux,null);

    }

    public void posicaoEsquerdo(int position,int numeroCartas){
        int height = gameView.getHeight();

        int divisor = 50;

        int meio=height/2;
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();
        int start=0;

        //par

        if(numeroCartas%2 == 0){
            //start=meio+(divisor*((numeroCartas/2)));
            start=meio-(width_carta/2)-(divisor*((numeroCartas/2)-1));
        }
        //impar
        else{
            start=meio-(width_carta/2)-(divisor*((numeroCartas/2)));
            //start=meio+((divisor*((numeroCartas-1)/2))+(divisor/2));
        }

        this.x = width-(height_carta/2);
        this.y =start + divisor*position;
       // this.y = meio-(width_carta/2);
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void onDrawEsquerdo(Canvas canvas,int posicao,int numeroCartas){



        posicaoEsquerdo(posicao,numeroCartas);



        canvas.drawBitmap(RotateBitmap(back,90),x,y,null);
    }

    public void onDrawDireito(Canvas canvas,int posicao,int numeroCartas){



        posicaoDireito(posicao,numeroCartas);



        canvas.drawBitmap(RotateBitmap(back,90),x,y,null);
    }


    public void posicaoDireito(int position,int numeroCartas){
        int height = gameView.getHeight();

        int divisor = 50;

        int meio=height/2;
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();
        int start=0;

        //par

        if(numeroCartas%2 == 0){
            //start=meio+(divisor*((numeroCartas/2)));
            start=meio-(width_carta/2)-(divisor*((numeroCartas/2)-1));
        }
        //impar
        else{
            start=meio-(width_carta/2)-(divisor*((numeroCartas/2)));
            //start=meio+((divisor*((numeroCartas-1)/2))+(divisor/2));
        }

        this.x = 0-(height_carta/2);
        this.y =start + divisor*position;
        // this.y = meio-(width_carta/2);
    }

    public void posicaoCima(int position,int numeroCartas){
        int height = gameView.getHeight();
        int width = gameView.getWidth();
        int height_carta = bmp.getHeight();
        int width_carta = bmp.getWidth();

        int divisor = 50;
        int meio= width/2;
        int start=0;

        if(numeroCartas%2 == 0){
            //start=meio+(divisor*((numeroCartas/2)));
            start=meio-(width_carta/2)+(divisor*((numeroCartas/2)-1));
        }
        //impar
        else{
            start=meio-(width_carta/2)+(divisor*((numeroCartas/2)));
            //start=meio+((divisor*((numeroCartas-1)/2))+(divisor/2));
        }


        this.x=start-divisor*position;
        this.y= 0-(height_carta/2);
        //this.y =0 - (height_carta/2);
    }



    public void onDrawTop(Canvas canvas,int posicao,int numeroCartas){



        posicaoCima(posicao,numeroCartas);


        canvas.drawBitmap(RotateBitmap(back,180),x,y,null);
    }


}
