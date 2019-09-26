package com.example.android.placar;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    /**
     * As variaveis globais
     */

    public int pontosA = 0;
    public int pontosB = 0;
    public int tempoDeJogo = 1;
    public int tempoMaximo = 1; //copia de tempoDeJogo que so e atualizada ao iniciar o contador
    public long tempoParaAcabar = 0L;
    public boolean acionado = false; //relogio esta contando ou pausado?
    public boolean gatilho = false; //relogio comecou a contar?

    private Button startButton;

    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerValue = (TextView) findViewById(R.id.mostradorTempo);

        startButton = (Button) findViewById(R.id.botao);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(acionado == false){
                    if(gatilho == true){
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                        acionado = true;
                    }
                    else {
                        tempoMaximo = tempoDeJogo;
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                        acionado = true;
                        gatilho = true;
                    }
                    startButton.setText("Pausar");
                }
                else{
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    acionado = false;
                    startButton.setText("Acionar");
                }
            }
        });
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            if (gatilho == false) return;
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;
            
            tempoParaAcabar = tempoMaximo * 60 * 1000 - updatedTime; /* tempo de jogo e em minutos */
            if (tempoParaAcabar <= 0) {
                tempoParaAcabar = 0;
                timeSwapBuff = 0;
                startButton.setText("Acionar");
                acionado = false;
                gatilho = false;
            }
            exibirmostradorTempo(tempoParaAcabar);
            customHandler.postDelayed(this, 0);
        }

    };
    
    
    /**
     *  As funcoes de exibir conteudo nos textViews
     */

    void exibirA (int conteudo){
        TextView pontos = (TextView) findViewById(R.id.placarA);
        pontos.setText(String.valueOf(conteudo));
    }

    void exibirB (int conteudo){
        TextView pontos = (TextView) findViewById(R.id.placarB);
        pontos.setText(String.valueOf(conteudo));
    }

    void exibirmostradorTempo (long conteudo){
        TextView mostrador = (TextView) findViewById(R.id.mostradorTempo);

        int secs = (int) (conteudo / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        mostrador.setText("" + mins + ":"
                + String.format("%02d", secs));
    }

    void exibirTempoDeJogo (int conteudo){
        TextView pontos = (TextView) findViewById(R.id.tempoDeJogo);
        pontos.setText(String.valueOf(conteudo));
    }


    /**
     *  As funcoes de alterar placares
     */

    public void maisGolA (View v){
        pontosA ++;
        exibirA(pontosA);
    }

    public void menosGolA (View v){
        if(pontosA > 0) {
            pontosA --;
            exibirA(pontosA);
        }
    }

    public void maisGolB (View v){
        pontosB ++;
        exibirB(pontosB);
    }

    public void menosGolB (View v){
        if(pontosB > 0) {
            pontosB --;
            exibirB(pontosB);
        }
    }


    public void reiniciar (View v){
        pontosA = 0;
        pontosB = 0;
        tempoMaximo = tempoDeJogo;
        tempoParaAcabar = tempoMaximo * 60 * 1000;
        timeSwapBuff = 0;
        startTime = SystemClock.uptimeMillis();
        gatilho = false;
        acionado = false;
        exibirA(pontosA);
        exibirB(pontosB);
        exibirmostradorTempo(tempoParaAcabar);
        startButton.setText("Acionar");
    }

    public void menosMinutos (View v){
        if(tempoDeJogo > 1) {
            tempoDeJogo --;
            exibirTempoDeJogo(tempoDeJogo);
            if (gatilho == false){
                exibirmostradorTempo(tempoDeJogo * 60 * 1000);
            }
        }
    }

    public void maisMinutos (View v){
        tempoDeJogo ++;
        exibirTempoDeJogo(tempoDeJogo);
        if (gatilho == false){
            exibirmostradorTempo(tempoDeJogo * 60 * 1000);
        }
    }

}
