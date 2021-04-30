package com.example.sueca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GameView extends SurfaceView {
    public Canvas canvas;
    public List<String> order = new ArrayList<>();
    int width;
    int height;
    String nomeCarta;
    private SurfaceHolder holder;
    private int contadorJogar = 0;
    private GameThread gameThread;
    private Boolean playerJogarTurn;
    private Boolean seguroJogar = true;
    private Boolean helper = true;
    private List<Integer> listaImagem = new ArrayList<Integer>();
    private List<String> jogadaPlayer = new ArrayList<>();
    private List<Carta> deck = new ArrayList<>();
    private List<Carta> playerHand = new ArrayList<Carta>();
    private List<JogadorCarta> jogada = new ArrayList<>();
    private List<Carta> teammate = new ArrayList<>();
    private List<Carta> bot1 = new ArrayList<>();
    private List<Carta> bot2 = new ArrayList<>();
    private String trunfo;
    private Carta carta_trunfo;
    private int HeightTab;
    private int WidthTab;
    private Bitmap imagemAux;
    private SpinKitView spinkit;
    private ProgressBar pb;

    Bitmap background = BitmapFactory.decodeResource(getResources(),R.drawable.background2);
    int pontEles=0;
    int pontNost=0;

    public GameView(final Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context){
        pb = new ProgressBar(context);
        Sprite doubleBounce = new DoubleBounce();
        pb.setIndeterminateDrawable(doubleBounce);
        pb.setVisibility(VISIBLE);
        criarBackGround();
        gameThread = new GameThread(this);
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameThread.setRunning(true);
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                while (retry) {
                    try {
                        // join method waits for this thread to die
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });

        loadDeck();
        Collections.shuffle(deck);
        distribuirMaos();

        for (int i = 0; i < playerHand.size(); i++) {
            playerHand.get(i).posicao(0, i, playerHand.size());
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public void distribuirMaos() {
        for (int i = 0; i < 10; i++) {
            playerHand.add(deck.get(i));
        }
        trunfo = getNaipe(playerHand.get(0).nome);
        Log.d("trunfo ", "naipe -> " + trunfo);
        carta_trunfo= playerHand.get(0);
        for (int i = 10; i < 20; i++) {
            bot1.add(deck.get(i));
        }
        for (int i = 20; i < 30; i++) {
            teammate.add(deck.get(i));
        }
        for (int i = 30; i < 40; i++) {
            bot2.add(deck.get(i));
        }
    }


    public void loadDeck() {
        deck.add(createCarta(R.drawable.h2, "copas_dois"));
        deck.add(createCarta(R.drawable.s2, "espadas_dois"));
        deck.add(createCarta(R.drawable.c2, "paus_dois"));
        deck.add(createCarta(R.drawable.d2, "ouros_dois"));
        deck.add(createCarta(R.drawable.h3, "copas_tres"));
        deck.add(createCarta(R.drawable.s3, "espadas_tres"));
        deck.add(createCarta(R.drawable.c3, "paus_tres"));
        deck.add(createCarta(R.drawable.d3, "ouros_tres"));
        deck.add(createCarta(R.drawable.h4, "copas_quatro"));
        deck.add(createCarta(R.drawable.s4, "espadas_quatro"));
        deck.add(createCarta(R.drawable.c4, "paus_quatro"));
        deck.add(createCarta(R.drawable.d4, "ouros_quatro"));
        deck.add(createCarta(R.drawable.h5, "copas_cinco"));
        deck.add(createCarta(R.drawable.s5, "espadas_cinco"));
        deck.add(createCarta(R.drawable.c5, "paus_cinco"));
        deck.add(createCarta(R.drawable.d5, "ouros_cinco"));
        deck.add(createCarta(R.drawable.h6, "copas_seis"));
        deck.add(createCarta(R.drawable.s6, "espadas_seis"));
        deck.add(createCarta(R.drawable.c6, "paus_seis"));
        deck.add(createCarta(R.drawable.d6, "ouros_seis"));
        deck.add(createCarta(R.drawable.h7, "copas_sete"));
        deck.add(createCarta(R.drawable.s7, "espadas_sete"));
        deck.add(createCarta(R.drawable.c7, "paus_sete"));
        deck.add(createCarta(R.drawable.d7, "ouros_sete"));
        deck.add(createCarta(R.drawable.hq, "copas_dama"));
        deck.add(createCarta(R.drawable.sq, "espadas_dama"));
        deck.add(createCarta(R.drawable.cq, "paus_dama"));
        deck.add(createCarta(R.drawable.dq, "ouros_dama"));
        deck.add(createCarta(R.drawable.hj, "copas_valete"));
        deck.add(createCarta(R.drawable.sj, "espadas_valete"));
        deck.add(createCarta(R.drawable.cj, "paus_valete"));
        deck.add(createCarta(R.drawable.dj, "ouros_valete"));
        deck.add(createCarta(R.drawable.hk, "copas_rei"));
        deck.add(createCarta(R.drawable.sk, "espadas_rei"));
        deck.add(createCarta(R.drawable.ck, "paus_rei"));
        deck.add(createCarta(R.drawable.dk, "ouros_rei"));
        deck.add(createCarta(R.drawable.ha, "copas_as"));
        deck.add(createCarta(R.drawable.sa, "espadas_as"));
        deck.add(createCarta(R.drawable.ca, "paus_as"));
        deck.add(createCarta(R.drawable.da, "ouros_as"));
    }


    public void criarBackGround(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width=size.x;
        height=size.y;


        int actionBarHeight=0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        background= getResizedBitmap(background,size.x,size.y);
        System.out.println("deded");
    }

    private Carta createCarta(int resource, String nome) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        Bitmap back = BitmapFactory.decodeResource(getResources(),R.drawable.blue_back);

        back = getResizedBitmap(back, 172, 264);
        bmp = getResizedBitmap(bmp, 172, 264);
        return new Carta(this, bmp, nome,back);
    }

    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        if (canvas != null) {
            canvas.drawColor(Color.parseColor("#22B14C"));

            HeightTab=this.getHeight();
            WidthTab=this.getWidth();
            int widthcarta=172;
            int heightcarta=264;

            Paint myPaint = new Paint();
            myPaint.setStrokeWidth(50);
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.parseColor("#B5E61D"));
            canvas.drawRect(((WidthTab/2)-heightcarta-(widthcarta/2)),(((HeightTab/2)-heightcarta-(widthcarta))), ((WidthTab/2)+heightcarta+(widthcarta/2)),(((HeightTab/2)+heightcarta+(widthcarta))), myPaint);

            Paint myPaint2 = new Paint();
            myPaint2.setColor(Color.parseColor("#008000"));
            myPaint2.setStyle(Paint.Style.FILL);
            canvas.drawRect(((WidthTab/2)-heightcarta-(widthcarta/2)),(((HeightTab/2)-heightcarta-(widthcarta))), ((WidthTab/2)+heightcarta+(widthcarta/2)),(((HeightTab/2)+heightcarta+(widthcarta))), myPaint2);
            //canvas.drawBitmap(background,0,0-150,null);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);

            canvas.drawText("Nós "+pontNost,width-200,50,paint);
            canvas.drawText("Eles "+pontEles,width-200,100,paint);
            carta_trunfo.onDrawTrunfo(canvas);

            for (int i = 0; i < playerHand.size(); i++) {
                if(!playerHand.get(i).mover){
                    playerHand.get(i).onDraw(canvas, 0, i, playerHand.size());
                }
            }

            for (int i = 0; i < playerHand.size(); i++) {
                if(playerHand.get(i).mover){
                    playerHand.get(i).onDraw(canvas, 0, i, playerHand.size());
                }
            }

            for(int i=0;i<bot1.size();i++){
                bot1.get(i).onDrawEsquerdo(canvas,i,bot1.size());
            }

            for(int i=0;i<bot2.size();i++){
                bot2.get(i).onDrawDireito(canvas,i,bot2.size());
            }

            for(int i=0;i<teammate.size();i++){
                teammate.get(i).onDrawTop(canvas,i,teammate.size());
            }


            if (helper==true) {
                imprimirJogada(canvas);
                helper=false;
            }
            else{
                //este if é feito no concluir jogada. aqui é so para testar.
                if(jogada.size()==4){
                    Log.d("pontos nos",pontNost+"",null);
                    Log.d("pontos eles",pontEles+"",null);
                    jogada = new ArrayList<>();
                    try {
                        gameThread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                imprimirJogada(canvas);
                jogar(canvas);
            }
            //for(Carta carta : cartas){
        }
    }

    public void jogar(Canvas canvas) {

        if (seguroJogar) {
            if (contadorJogar != 0) {
                try {
                    gameThread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (contadorJogar) {
                    case 1: {
                        cartaAJogar(1);
                        contadorJogar = 2;
                    }
                    break;
                    case 2: {
                        cartaAJogar(2);
                        contadorJogar = 3;
                    }
                    break;
                    case 3: {
                        cartaAJogar(3);
                        contadorJogar = 0;
                    }
                    break;
                }

                int index = verificarJogada2();
                if (index == 5) {

                } else {
                    concluirJogada(index);
                }
            }
        }
    }

    public void acabarJogo(){

        if(playerHand.size()==0 && bot1.size()==0 && bot2.size()==0 && teammate.size()==0){

        }
    }


    // falta aqui quando o naipe puxado e o trunfo


    //se não tiver nenhuma carta jogada
        //jogar algum às que não seja trunfo
        // caso nao tenha nenhum às jogar uma carta que nao tenha pontos


    // caso ja haja cartas jogadas
         // caso haja uma carta jogada
                // caso tenha cartas para assistir
                    // foi jogado o as?
                        // assiste com carta baixa
                    // nao foi jogado o as
                        // assiste com carta alta sem ser o 7
                //  nao tem cartas para assistir
                    // se os pontos da carta forem > 10
                        // se tem trunfo para cortar
                            //corta
                        // nao tem trunfo para cortar
                            //tem carta sem pontos
                                //joga
                            // nao tem carta sem pontos
                                //joga a que da menos pontos
                    // se os pontos forem < 10
                        // tem carta sem ser trunfo
                            // nao corta
                        // nao tem carta sem trunfo
                            // corta com a mais baixa

        // caso haja duas cartas jogadas
            //caso esteja a ganhar
                // caso o companheiro tenha jogado o As
                    // caso tenha para assistir
                        //tem carta com pontos para assistir
                            //assiste com a que da mais pontos
                        // nao tem carta com pontos para assistir
                            // assiste com a mais baixa
                    // nao tem para assistir
                        // tem carta sem ser trunfo
                            //joga a com mais pontos
                        // so tem trunfo
                            //joga o trunfo mais baixo
            // caso esteja  a perder
                // caso tenha para assistir
                    // ve se tem carta mais alta sem ser o 7
                        //caso tenha joga
                        // caso nao tenha joga a mais baixa
                // caso nao tenha para assistir
                    // se os pontos forem < 10
                        // nao corta
                    // se forem > 10
                        // corta com a mais baixa
                    // se forem > 20
                        //corta com a mais alta

        // caso haja tres cartas jogadas
            // caso esteja a ganhar
                // tem para assistir
                    // tem cartas com pontos
                        // joga a com mais pontos
                    // nao tem cartas com pontos
                        //joga  mais baixa
                // nao tem para assistir
                    // tem cartas sem trunfo
                        // tem cartas com pontos
                            // joga carta com mais pontos
                        // nao tem cartas com pontos
                            // joga carta mais baixa
                    // nao tem cartas sem trunfo
                        // joga o trunfo mais baixo

            // caso esteja a perder
                // tem para assistir
                    // se carta mais alta consegue ganhar
                        // joga carta mais alta
                    // se carta mais alta nao consegue ganhar
                        // joga carta mais baixa

                // nao tem para assistir
                    // tem trunfo
                        // se o algum trunfo consegue ganhar joga-o
                        // se nao consegue ganhar
                            // ve se tem cartas sem trunfo
                                // tem cartas sem trunfo
                                    // joga a mais baixa
                                // nao tem cartas sem trunfo
                                    // joga o trunfo mais baixo
                    // nao tem trunfo
                        // joga carta mais baixa



    public void cartaAJogar(int jogador) {
        int index=0;
        String nomeJogador="error";
        List<Carta> maoJogador= new ArrayList<>();
        if(jogador==1){
            maoJogador = bot1;
            nomeJogador="bot1";
        }
        else if(jogador==2){
            maoJogador= teammate;
            nomeJogador="teammate";
        }
        else if(jogador==3){
            maoJogador= bot2;
            nomeJogador="bot2";
        }

        int tamanhoJogada = jogada.size();

        if(tamanhoJogada==0){
            if(temAses(maoJogador)){
                //joga as
                index=getAs(maoJogador);
            }
            else{
                if(temCartaSemTrunfo(maoJogador)){
                    //joga carta mais baixa sem ser trunfo
                    index=getCartaMaisBaixaSemSerTrunfo(maoJogador);
                }
                else{
                    //joga carta mais baixa
                    index=getCartaMaisBaixa(maoJogador);
                }
            }
        }

        else if(tamanhoJogada==1){
            if(temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                if(getValue(jogada.get(0).carta.nome)==11){
                    index=getCartaMaisBaixaSemSerTrunfoAssiste(maoJogador,getNaipe(jogada.get(0).carta.nome));
                }
                else{
                    index=getCartaMaisAltaSemSerSete(maoJogador,getNaipe(jogada.get(0).carta.nome));
                }
            }
            else{
                if(getNaipe(jogada.get(0).carta.nome).equals(trunfo)){
                    index=getCartaMaisBaixaTrunfo(maoJogador);
                }
                else{
                    if(getPontosJogada()>=10){
                        if(getNaipe(jogada.get(0).carta.nome).equals(trunfo)){
                            if(temAsTrunfo(maoJogador)){
                                index=getAsTrunfo(maoJogador);
                            }
                        }
                        else{
                            if(temTrunfo(maoJogador)){
                                index=getCartaMaisBaixaTrunfo(maoJogador);
                            }
                            else{
                                index= getCartaMaisBaixa(maoJogador);
                            }
                        }
                    }
                    else{
                        if(temCartaSemTrunfo(maoJogador)){
                            index= getCartaMaisBaixaSemSerTrunfo(maoJogador);
                        }
                        else{
                            index=getCartaMaisBaixa(maoJogador);
                        }

                    }
                }

            }
        }

        else if(tamanhoJogada==2){
            if(estaAganhar()){
                // caso o companheiro de equipa tenha jogado o as
                if(getValue(jogada.get(0).carta.nome) == 11){
                    if(temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                        index=getCartaMaisAltaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                    else{
                        index=getCartaMaisBaixa(maoJogador);
                    }
                }
                else{
                    if(temAsAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                        index=getAsAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                    else if (temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                        index=getCartaMaisBaixaAssiste(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                    else{
                        index=getCartaMaisBaixa(maoJogador);
                    }
                }
            }
            else{
                //verificar este else acho que esta mal
                if(temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                    // idk bem. tenho de mandar as 2 cartas maybe
                    if(temCartaConsigaGanharAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome),getNaipe(jogada.get(1).carta.nome),getValue(jogada.get(1).carta.nome))){
                        index= getCartaConsigaGanharAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome),getNaipe(jogada.get(1).carta.nome),getValue(jogada.get(1).carta.nome));
                    }
                    else{
                        index=getCartaMaisBaixaAssiste(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                }
                else{
                    if(trunfo.equals(jogada.get(0).carta.nome)){
                        index=getCartaMaisBaixa(maoJogador);
                    }
                    // nao é puxado a trunfo
                    else{
                        if(getPontosJogada()<10){
                            //naocorta
                            index=getCartaMaisBaixaSemSerTrunfo(maoJogador);
                        }
                        else if(getPontosJogada()>20){
                            if(temTrunfo(maoJogador)){
                                index= getCartaMaisBaixaTrunfo(maoJogador);
                            }
                            else{
                                index=getCartaMaisBaixa(maoJogador);
                            }

                        }
                        else if(getPontosJogada()>10){
                            if(temTrunfo(maoJogador)){
                                index=getCartaMaisAltaTrunfo(maoJogador);
                            }
                            else{
                                index= getCartaMaisBaixa(maoJogador);
                            }

                        }
                    }
                }
            }
        }
        else if(tamanhoJogada==3){
            // caso esteja a ganhar
            if(estaAganharTres()){
                if(temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                    index= getCartaMaisAltaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome));
                }
                else{
                    index=getCartaMaisAlta(maoJogador);
                }
            }
            // caso esteja a perder
            else{
                if(temCartaParaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome))){
                    if(cartaMaisAltaGanha(maoJogador)){
                        index= getCartaMaisAltaAssistir(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                    else{
                        index=getCartaMaisBaixaAssiste(maoJogador,getNaipe(jogada.get(0).carta.nome));
                    }
                }
                else{
                    index= getCartaMaisBaixa(maoJogador);
                }
            }
        }
        nomeCarta=maoJogador.get(index).nome;
        JogadorCarta jogadorCarta = new JogadorCarta(nomeJogador,maoJogador.get(index));
        jogada.add(jogadorCarta);
        imprimirJogada(canvas);
        maoJogador.remove(index);
        Log.d(nomeJogador,"jogou -> "+nomeCarta);
    }


    public Boolean cartaMaisAltaGanha(List<Carta> mao){
        String assistir = getNaipe(jogada.get(0).carta.nome);
        String segundoPlayer = getNaipe(jogada.get(1).carta.nome);
        String terceiroPlayer = getNaipe(jogada.get(2).carta.nome);
        int max=0;
        int index=0;

        for(int i=0;i<mao.size();i++){
            if(getNaipe(mao.get(i).nome).equals(assistir)){
                if(getValue(mao.get(i).nome)>max){
                    max=getValue(mao.get(i).nome);
                    index=i;
                }
            }
        }


        if(assistir.equals(trunfo)){
            if(max>getValue(jogada.get(0).carta.nome) && max > getValue(jogada.get(2).carta.nome)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(terceiroPlayer.equals(trunfo)){
                return false;
            }
            else{
                if(max>getValue(jogada.get(0).carta.nome)){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
    }



    public Boolean estaAganharTres(){
        String assistir = getNaipe(jogada.get(0).carta.nome);
        String segundoPlayer = getNaipe(jogada.get(1).carta.nome);
        String terceiroPlayer = getNaipe(jogada.get(2).carta.nome);

        if(assistir.equals(trunfo)){
            if(segundoPlayer.equals(trunfo) && terceiroPlayer.equals(trunfo)){
                if(getValue(jogada.get(1).carta.nome)>getValue(jogada.get(2).carta.nome) && getValue(jogada.get(1).carta.nome)>getValue(jogada.get(0).carta.nome)){
                    return true;
                }
                else{
                    return false;
                }
            }
            if(segundoPlayer.equals(trunfo) && !terceiroPlayer.equals(trunfo)){
                if(getValue(jogada.get(1).carta.nome)>getValue(jogada.get(0).carta.nome)){
                    return true;
                }
                else{
                    return false;
                }
            }
            if(!segundoPlayer.equals(trunfo) && terceiroPlayer.equals(trunfo)){
                return false;
            }
        }
        else{
            if(segundoPlayer.equals(trunfo) && !terceiroPlayer.equals(trunfo)){
                return true;
            }
            if(segundoPlayer.equals(trunfo) && terceiroPlayer.equals(trunfo)){
                if(getValue(jogada.get(1).carta.nome) < getValue(jogada.get(2).carta.nome)){
                    return false;
                }
                else{
                    return true;
                }
            }
            if(!segundoPlayer.equals(trunfo) && terceiroPlayer.equals(trunfo)){
                return false;
            }
        }
        return false;
    }


    public int getCartaMaisAlta(List<Carta> mao){
        int max = 0;
        int index=0;

        for(int i=0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)>max){
                max=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }


    public Boolean temCartaConsigaGanharAssistir(List<Carta> mao,String naipeAssistir, String naipeJogado, int valueAdv ){
        // se o naipe jogado e o naipe que é puxado forem iguais
        if(naipeAssistir.equals(naipeJogado)){
            // se o naipe puxado for trunfo
                for(int i=0;i<mao.size();i++){
                    if(getNaipe(mao.get(i).nome).equals(naipeAssistir)){
                        if(getValue(mao.get(i).nome)>valueAdv){
                            return true;
                        }
                    }
                }
        }
        else{
            if(naipeJogado.equals(trunfo)){
                return false;
            }
        }
        return false;
    }



    public int getCartaConsigaGanharAssistir(List<Carta> mao,String naipeAssistir, String naipeJogado, int valueAdv ){
        if(naipeAssistir.equals(naipeJogado)){
            // se o naipe puxado for trunfo
            for(int i=0;i<mao.size();i++){
                if(getNaipe(mao.get(i).nome).equals(naipeAssistir)){
                    if(getValue(mao.get(i).nome)>valueAdv){
                        return i;
                    }
                }
            }
        }
        return 0;
    }



    public int getAsAssistir(List<Carta> mao,String assistir){
        for(int i=0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)==11 && getNaipe(mao.get(i).nome).equals(assistir)){
                return i;
            }
        }
        return 0;
    }

    public int getCartaMaisBaixaAssiste(List<Carta> mao,String assistir){
        int minimo=30;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(minimo==30 && getNaipe(mao.get(i).nome).equals(assistir)){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }

            if(getValue(mao.get(i).nome)<minimo && getNaipe(mao.get(i).nome).equals(assistir)){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }


    public Boolean temAsAssistir(List<Carta> mao,String assistir){

        for(int i=0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)==11 && getNaipe(mao.get(i).nome).equals(assistir)){
                return true;
            }
        }
        return false;
    }


    public Boolean estaAganhar(){
        String assistir = getNaipe(jogada.get(0).carta.nome);
        String naipeSegundoJogador = getNaipe(jogada.get(1).carta.nome);
        if(assistir==naipeSegundoJogador){
            if(getValue(jogada.get(0).carta.nome)>getValue(jogada.get(1).carta.nome)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(assistir==trunfo){
                return true;
            }
            if(naipeSegundoJogador==trunfo){
                return false;
            }
        }

        return false;
    }

    public int getAs(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            int aux=getValue(mao.get(i).nome);
            if(aux==11){
                return i;
            }
        }
        return 0;
    }

    public Boolean temTrunfo(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            String aux=getNaipe(mao.get(i).nome);
            if(aux.equals(trunfo)){
                return true;
            }
        }
        return false;
    }


    public Boolean temAsTrunfo(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            String aux=getNaipe(mao.get(i).nome);
            if(aux.equals(trunfo) && getValue(mao.get(i).nome)==11){
                return true;
            }
        }
        return false;
    }

    public int getAsTrunfo(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            String aux=getNaipe(mao.get(i).nome);
            if(aux.equals(trunfo) && getValue(mao.get(i).nome)==11){
                return i;
            }
        }
        return 0;
    }


    public Boolean temAses(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            int aux=getValue(mao.get(i).nome);
            if(aux==11){
                return true;
            }
        }
        return false;
    }


    public int getPontosJogada(){
        int pontuacao = 0;
        for(int i=0;i<jogada.size();i++){
            int aux=getValue(jogada.get(i).carta.nome);
            switch (aux){
                case 7: pontuacao+= 2;break;
                case 8: pontuacao+= 3;break;
                case 9: pontuacao+= 4;break;
                case 10: pontuacao+= 10; break;
                case 11: pontuacao+= 11 ;break;
            }
        }
        return pontuacao;
    }

    public Boolean temCartaSemTrunfo(List<Carta> mao){
        for(int i=0;i<mao.size();i++){
            String aux=getNaipe(mao.get(i).nome);
            if(!aux.equals(trunfo)){
                return true;
            }
        }
        return false;
    }

    public int getCartaMaisBaixaSemSerTrunfo (List<Carta> mao){
        int minimo=30;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(minimo==30 && (!getNaipe(mao.get(i).nome).equals(trunfo))){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
            if(getValue(mao.get(i).nome)<minimo && (!getNaipe(mao.get(i).nome).equals(trunfo))){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }


    public int getCartaMaisBaixaTrunfo (List<Carta> mao){
        int minimo=30;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(minimo==30 && (getNaipe(mao.get(i).nome).equals(trunfo))){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
            if(getValue(mao.get(i).nome)<minimo && (getNaipe(mao.get(i).nome).equals(trunfo))){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }

    public int getCartaMaisAltaTrunfo (List<Carta> mao){
        int max=0;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)>max && (getNaipe(mao.get(i).nome).equals(trunfo))){
                max=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }



    public int getCartaMaisBaixa(List<Carta> mao){
        int minimo=30;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(minimo==30){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
            if(getValue(mao.get(i).nome)<minimo ){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }

    public Boolean temCartaParaAssistir(List<Carta> mao , String assistir){
        for(int i =0;i<mao.size();i++){
            if(getNaipe(mao.get(i).nome).equals(assistir) ){
                return true;
            }
        }
        return false;
    }


    // falta a parte sem ser o 7
    public int getCartaMaisAltaSemSerSete(List<Carta> mao,String assistir){
        int max=0;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)>max && getNaipe(mao.get(i).nome).equals(assistir)){
                max=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }

    public int getCartaMaisBaixaSemSerTrunfoAssiste (List<Carta> mao,String assistir){
        int minimo=30;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(minimo==30 && getNaipe(mao.get(i).nome).equals(assistir)){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
            else if(getValue(mao.get(i).nome)<minimo && (!getNaipe(mao.get(i).nome).equals(trunfo)) && getNaipe(mao.get(i).nome).equals(assistir)){
                minimo=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }


    public int getCartaMaisAltaAssistir(List<Carta> mao,String assistir){
        int max=0;
        int index=0;
        for(int i =0;i<mao.size();i++){
            if(getValue(mao.get(i).nome)>max && getNaipe(mao.get(i).nome).equals(assistir)){
                max=getValue(mao.get(i).nome);
                index=i;
            }
        }
        return index;
    }



    // tem todos naipes iguais
        // ver quem tem o valor mais alto
    // nao tem todos os naipes iguais
        // ver quem ganha dos 2 primeiros e ver quem ganah dos 2 ultimos
            //ver quem ganha do resultado dos 2



    public int verificarJogada2(){
        if(jogada.size()==4){
            JogadorCarta p1, p2, p3, p4;
            p1 = jogada.get(0);
            p2 = jogada.get(1);
            p3 = jogada.get(2);
            p4 = jogada.get(3);

            String play1 = p1.jogador;
            String play2 = p2.jogador;
            String play3 = p3.jogador;
            String play4 = p4.jogador;
            jogadaPlayer = new ArrayList<>();
            jogadaPlayer.add(play1);
            jogadaPlayer.add(play2);
            jogadaPlayer.add(play3);
            jogadaPlayer.add(play4);

            //getNaipes
            String naipe1 = getNaipe(p1.carta.nome);
            String naipe2 = getNaipe(p2.carta.nome);
            String naipe3 = getNaipe(p3.carta.nome);
            String naipe4 = getNaipe(p4.carta.nome);

            //getValue

            int value1 = getValue(p1.carta.nome);
            int value2 = getValue(p2.carta.nome);
            int value3 = getValue(p3.carta.nome);
            int value4 = getValue(p4.carta.nome);
            int indexGanhou = 0;
            int indexAux;
            int indexAux2;

            if(naipe2.equals(naipe1) && naipe3.equals(naipe1) && naipe4.equals(naipe1)){
                indexGanhou=compara(value1,value2,value3,value4);
                return indexGanhou;
            }
            else{
                indexAux=compara2Cartas(0,1,naipe1);
                indexAux2=compara2Cartas(2,3,naipe1);
                indexGanhou=compara2Cartas(indexAux,indexAux2,naipe1);
                return indexGanhou;
            }
        }
        return  5;
    }


    public int compara2Cartas(int cartaUm,int cartaDois,String assistir){
        if(assistir.equals(trunfo)){
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                if(getValue(jogada.get(cartaUm).carta.nome) > getValue(jogada.get(cartaDois).carta.nome)){
                    return cartaUm;
                }
                else{
                    return cartaDois;
                }
            }
            if(!getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                return cartaDois;
            }
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && !getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                return cartaUm;
            }
            else{
                return cartaUm;
            }
        }
        else{
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                if(getValue(jogada.get(cartaUm).carta.nome) > getValue(jogada.get(cartaDois).carta.nome)){
                    return cartaUm;
                }
                else{
                    return cartaDois;
                }
            }
            if(!getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                return cartaDois;
            }
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(trunfo) && !getNaipe(jogada.get(cartaDois).carta.nome).equals(trunfo)){
                return cartaUm;
            }
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(assistir) && getNaipe(jogada.get(cartaDois).carta.nome).equals(assistir)){
                if(getValue(jogada.get(cartaUm).carta.nome) > getValue(jogada.get(cartaDois).carta.nome)){
                    return cartaUm;
                }
                else{
                    return cartaDois;
                }
            }
            if(!getNaipe(jogada.get(cartaUm).carta.nome).equals(assistir) && getNaipe(jogada.get(cartaDois).carta.nome).equals(assistir)){
                return cartaDois;
            }
            if(getNaipe(jogada.get(cartaUm).carta.nome).equals(assistir) && !getNaipe(jogada.get(cartaDois).carta.nome).equals(assistir)){
                return cartaUm;
            }
            //idk
            else{
                return cartaUm;
            }
        }

    }



    public int verificarJogada() {
        if (jogada.size() == 4) {
            seguroJogar = false;
            JogadorCarta p1, p2, p3, p4;
            p1 = jogada.get(0);
            p2 = jogada.get(1);
            p3 = jogada.get(2);
            p4 = jogada.get(3);

            String play1 = p1.jogador;
            String play2 = p2.jogador;
            String play3 = p3.jogador;
            String play4 = p4.jogador;
            jogadaPlayer = new ArrayList<>();
            jogadaPlayer.add(play1);
            jogadaPlayer.add(play2);
            jogadaPlayer.add(play3);
            jogadaPlayer.add(play4);

            //getNaipes
            String naipe1 = getNaipe(p1.carta.nome);
            String naipe2 = getNaipe(p2.carta.nome);
            String naipe3 = getNaipe(p3.carta.nome);
            String naipe4 = getNaipe(p4.carta.nome);

            //getValue

            int value1 = getValue(p1.carta.nome);
            int value2 = getValue(p2.carta.nome);
            int value3 = getValue(p3.carta.nome);
            int value4 = getValue(p4.carta.nome);
            int indexGanhou = 0;

            //naipes todos diferentes
            if (!naipe2.equals(naipe1) && !naipe3.equals(naipe1) && !naipe4.equals(naipe1)) {
                indexGanhou = 0;
                return indexGanhou;
                //p1 ganhou
            }

            //todos naipes iguais naipe1
            else if (naipe2.equals(naipe1) && naipe3.equals(naipe1) && naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, value2, value3, value4);
                return indexGanhou;
            }

            //naipe3 e naipe 4 = naipe1
            else if (!naipe2.equals(naipe1) && naipe3.equals(naipe1) && naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, 0, value3, value4);
                return indexGanhou;
            }
            //naipe2 e naipe 3
            else if (naipe2.equals(naipe1) && naipe3.equals(naipe1) && !naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, value2, value3, 0);
                return indexGanhou;
            }
            //naipe 2 e naipe 4
            else if (naipe2.equals(naipe1) && !naipe3.equals(naipe1) && naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, value2, 0, value4);
                return indexGanhou;
            }


            //naipe4 igual ao naipe1
            else if (!naipe2.equals(naipe1) && !naipe3.equals(naipe1) && naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, 0, 0, value4);
                return indexGanhou;
            }
            //naipe2
            else if (naipe2.equals(naipe1) && !naipe3.equals(naipe1) && !naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, value2, 0, 0);
                return indexGanhou;
            }
            //naipe3
            else if (!naipe2.equals(naipe1) && naipe3.equals(naipe1) && !naipe4.equals(naipe1)) {
                indexGanhou = compara(value1, 0, value3, 0);
                return indexGanhou;
            }
        }
        return 5;
    }


    public void imprimirJogada(Canvas canvas) {
        for (int i = 0; i < jogada.size(); i++) {
            jogada.get(i).carta.onDraw2(canvas, jogada.get(i).jogador);
        }
    }

    public void concluirJogada(int indexGanhou) {

        int pontos = getPontosJogada();
        int aux=0;
        Log.d("indexGanhou ", "index -> " + indexGanhou);
        switch (indexGanhou) {
            case 0: {
                aux = verificarPlayer(0);
                contadorJogar = aux;
            }
            break;
            case 1: {
                aux = verificarPlayer(1);
                contadorJogar = aux;
            }
            break;
            case 2: {
                aux = verificarPlayer(2);
                contadorJogar = aux;
            }
            break;
            case 3: {
                aux = verificarPlayer(3);
                contadorJogar = aux;
            }
            break;
        }

        if(aux==0){
            pontNost+=pontos;
        }
        else if(aux==1){
            pontEles+=pontos;
        }
        else if(aux==2){
            pontNost+=pontos;
        }
        else if(aux==3){
            pontEles+=pontos;
        }

        // ver quem ganhou o ponto.
        // colocar o contador em quem tem de jogar
        //jogada = new ArrayList<>();
        seguroJogar = true;
    }


    // ver que player esta em que index
    public int verificarPlayer(int index) {
        switch (jogadaPlayer.get(index)) {
            case "player":
                return 0;
            case "bot1":
                return 1;
            case "teammate":
                return 2;
            case "bot2":
                return 3;
            default:
                return 0;
        }
    }

    public int compara(int naipe1, int naipe2, int naipe3, int naipe4) {
        ArrayList<Integer> aux = new ArrayList<>();
        int max = 0;
        int index = 0;
        aux.add(naipe1);
        aux.add(naipe2);
        aux.add(naipe3);
        aux.add(naipe4);
        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i) > max) {
                max = aux.get(i);
                index = i;
            }
        }
        return index;
    }

    public String getNaipe(String carta) {
        String[] parts = carta.split("_");
        String result = parts[0];
        return result;
    }

    public int getValue(String carta) {
        String[] parts = carta.split("_");
        String result = parts[1];
        switch (result) {
            case "dois":
                return 2;
            case "tres":
                return 3;
            case "quatro":
                return 4;
            case "cinco":
                return 5;
            case "seis":
                return 6;
            case "sete":
                return 10;
            case "dama":
                return 7;
            case "valete":
                return 8;
            case "rei":
                return 9;
            case "as":
                return 11;
            default:
                return 0;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    Carta carta_tocada = null;
                    int tempx = (int) event.getX();
                    int tempy = (int) event.getY();
                    for (int i = playerHand.size() - 1; i >= 0; i--) {
                        carta_tocada = playerHand.get(i);
                        if (carta_tocada.isColliding(tempx, tempy, i)) {
                            playerHand.get(i).tocada = true;
                            playerHand.get(i).mover = true;
                            playerHand.get(i).xMover = (int) event.getX();
                            playerHand.get(i).yMover = (int) event.getY();
                            //cartas.remove(i);
                        } else {
                            playerHand.get(i).tocada = false;
                        }
                    }
                    //Log.d("down", "down");
                    break;

                }
                case MotionEvent.ACTION_MOVE: {
                    Carta carta_tocada = null;
                    int tempx = (int) event.getX();
                    int tempy = (int) event.getY();
                    int contador = 0;
                    //ve se alguma está a mover
                    for (int i = playerHand.size() - 1; i >= 0; i--) {
                        if (playerHand.get(i).mover == true) {
                            carta_tocada = playerHand.get(i);
                            contador = 1;
                        }
                    }
                    // se alguma estiver a mover
                    if (contador == 1) {
                        // ve se está a dar colide em alguma que não esteja a mover
                        /*for (int i = playerHand.size() - 1; i >= 0; i--) {
                            if (playerHand.get(i).isColliding(tempx, tempy, i)) {
                                carta_tocada.mover = false;
                                carta_tocada.tocada = false;
                                carta_tocada = playerHand.get(i);
                                carta_tocada.mover = true;
                                carta_tocada.tocada = true;
                            }
                        }*/
                        carta_tocada.xMover = tempx;
                        carta_tocada.yMover = tempy;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (contadorJogar == 0) {
                        //Log.d("up", "up");
                        for (int i = playerHand.size() - 1; i >= 0; i--) {
                            if (playerHand.get(i).tocada == true) {
                                playerHand.get(i).mover = false;
                                playerHand.get(i).tocada = false;
                                if (playerHand.get(i).isPlayed()) {
                                    if(jogada.size()>0){
                                        if(temCartaParaAssistir(playerHand,getNaipe(jogada.get(0).carta.nome))){
                                            if(getNaipe(playerHand.get(i).nome).equals(getNaipe(jogada.get(0).carta.nome))){
                                                JogadorCarta jogadorCarta = new JogadorCarta("player", playerHand.get(i));
                                                jogada.add(jogadorCarta);

                                                helper=true;

                                                //isto nao ta a fazer nada
                                                imprimirJogada(canvas);

                                                Log.d("player ", "jogou -> " + playerHand.get(i).nome);
                                                playerHand.remove(i);
                                                int aux = verificarJogada2();
                                                if (aux == 5) {
                                                    contadorJogar = 1;
                                                } else {
                                                    concluirJogada(aux);
                                                }
                                            }
                                        }
                                        else{
                                            JogadorCarta jogadorCarta = new JogadorCarta("player", playerHand.get(i));
                                            jogada.add(jogadorCarta);

                                            helper=true;

                                            //isto nao ta a fazer nada
                                            imprimirJogada(canvas);

                                            Log.d("player ", "jogou -> " + playerHand.get(i).nome);
                                            playerHand.remove(i);
                                            int aux = verificarJogada2();
                                            if (aux == 5) {
                                                contadorJogar = 1;
                                            } else {
                                                concluirJogada(aux);
                                            }
                                        }
                                    }else{
                                        JogadorCarta jogadorCarta = new JogadorCarta("player", playerHand.get(i));
                                        jogada.add(jogadorCarta);

                                        helper=true;

                                        //isto nao ta a fazer nada
                                        imprimirJogada(canvas);

                                        Log.d("player ", "jogou -> " + playerHand.get(i).nome);
                                        playerHand.remove(i);
                                        int aux = verificarJogada2();
                                        if (aux == 5) {
                                            contadorJogar = 1;
                                        } else {
                                            concluirJogada(aux);
                                        }
                                    }
                                }
                                //cartas.remove(i);
                            } else {
                                playerHand.get(i).tocada = false;
                            }
                        }
                        break;
                    }
                    if (contadorJogar != 0) {
                        for (int i = playerHand.size() - 1; i >= 0; i--) {
                            if (playerHand.get(i).tocada == true) {
                                playerHand.get(i).mover = false;
                                playerHand.get(i).tocada = false;
                            } else {
                                playerHand.get(i).tocada = false;
                            }
                        }
                        break;
                    }

                }
            }
        }

        return true;//super.onTouchEvent(event);
    }

}
