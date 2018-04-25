package br.com.senaijandira.cotacaodecriptomoedas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txt_bitcoin, txt_ltc, txt_bcash, txt_consulta;

//    criando a formato de dinheiro para mostras na tela o valor e R$
    NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("pt", "br"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        conectando com os elementos visuais
        txt_bitcoin = (TextView) findViewById(R.id.txt_bitcoin);
        txt_ltc = (TextView) findViewById(R.id.txt_ltc);
        txt_bcash = (TextView) findViewById(R.id.txt_bcash);
        txt_consulta = (TextView) findViewById(R.id.txt_consulta);

//        chamando o método de consulta, para assim que o app iniciar, já consultar o valor da cotação
        consultarCotacao();

//        chamando o método de setar data e hora da consulta
        setarDataHora();

    }

    public void consultarCotacao(){

//        URL's das consultas das api's
        final String urlBtc = "https://www.mercadobitcoin.net/api/BTC/ticker/";
        final String urlLtc = "https://www.mercadobitcoin.net/api/LTC/ticker/";
        final String urlBch = "https://www.mercadobitcoin.net/api/BCH/ticker/";

//        AsyncTask = Tarefas assincronas
        new AsyncTask<Void, Void, Void>(){
//            criando as variaveis que vai receber o que voltar da consulta
            String retorno_btc="";
            String retorno_ltc ="";
            String retorno_bch="";

            @Override
            protected Void doInBackground(Void... voids) {
//                Tudo que estiver no doInBackground(), será executado em segundo plano

//                fazer a conslta na web
                retorno_btc = HttpConnection.get(urlBtc);
                retorno_ltc = HttpConnection.get(urlLtc);
                retorno_bch = HttpConnection.get(urlBch);

//                Exibir resultado no logcast
//                Log.d("consultarCotação", btc);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                try {
//                    criando o onjeto com o resultado da consulta
                    JSONObject objetoBTC = new JSONObject(retorno_btc);
                    JSONObject objetoLTC = new JSONObject(retorno_ltc);
                    JSONObject objetoBCH = new JSONObject(retorno_bch);

//                    resgatando o objeto ticker da consulta
                    JSONObject  ticker_BTC = objetoBTC.getJSONObject("ticker");
                    JSONObject  ticker_LTC = objetoLTC.getJSONObject("ticker");
                    JSONObject  ticker_BCH = objetoBCH.getJSONObject("ticker");

//                    resgatando o atributo correto de cada objeto
                    Double  cotacaoBtc = Double.valueOf(ticker_BTC.getString("last"));
                    Double  cotacaoltc = Double.valueOf(ticker_LTC.getString("last"));
                    Double  cotacaoBcH = Double.valueOf(ticker_BCH.getString("last"));

//                    setando o txt com o que foi resgatado
                    txt_bitcoin.setText(f.format(cotacaoBtc));
                    txt_ltc.setText(f.format(cotacaoltc));
                    txt_bcash.setText(f.format(cotacaoBcH));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

//    criando o menu na barra superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    setando a ação do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        ao clicar no item no menu, ele atualiza a consulta e a hora que foi consultado
        consultarCotacao();
        setarDataHora();
        return super.onOptionsItemSelected(item);
    }

    public void setarDataHora(){
        //        setando a data e hora da consulta
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        String dataAtual = String.format("%02d/%02d/%d ás %02d:%02d", dia, mes+1, ano, hora, minutos);
        txt_consulta.setText(dataAtual);
    }
}
