import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Scanner;


public class Main{
  


    public static void main(String[] args) {
        String arquivoCSV = "transacoes_00020.csv";
        String linha;
        String[] dados;
        int[][] vendasPorLojaMes = new int[4][12];
        int totalGeralVendas = 0;
        
        Leitura leitura = new Leitura();
        try(BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))){
            br.readLine();
            while((linha = br.readLine()) != null){
                dados = linha.split(",");
                int loja = Integer.parseInt(dados[2]);
                int mes = Integer.parseInt(dados[1]);
                int valor = Integer.parseInt(dados[3]);
                vendasPorLojaMes[loja-1][mes-1] += valor;
                totalGeralVendas += valor;

                int indiceLoja = obterIndiceLoja(String.valueOf(loja));
                int indiceMes = obterIndiceMes(String.valueOf(mes));
                vendasPorLojaMes[indiceLoja][indiceMes] += valor;

                totalGeralVendas += valor;
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        int menorVenda = Integer.MAX_VALUE;
        int indiceMenorVenda=-1;
        int[] maiorVendas = new int[3];
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        try{
            FileWriter write = new FileWriter("resultados.txt");
            
            write.write("Matriz de vendas");
            write.write(" ");

            for(String mes : meses){
                write.write(mes + "  ");
            }
            write.write("\n");
            String[] lojas = {"Matriz", "Filial Sul", "Filial Norte", "Filial Nordeste"};
            for(int i=0; i<4; i++){
                write.write(lojas[i] + " ");
                for(int j=0; j<12; j++){
                    write.write(vendasPorLojaMes[i][j] + " ");
                }
                write.write("\n");
            }

            write.write("\n tres meses com mais vendas");
            for(int i=0; i<3; i++){
                int maiorVenda = Integer.MIN_VALUE;
                for(int j=0; j<4; j++){
                    for(int k=0; k<12; k++){
                        if(vendasPorLojaMes[j][k] > maiorVenda){
                            maiorVenda = vendasPorLojaMes[j][k];
                            maiorVendas[i] = maiorVenda;
                        }
                    }
                }
                write.write(maiorVenda + " ");
            }

            write.write("\nmenor venda");
            for(int i=0; i < 12; i++){
                for(int j=0; j<4; j++){
                    if(vendasPorLojaMes[j][i] < menorVenda){
                        menorVenda = vendasPorLojaMes[j][i];
                        indiceMenorVenda = i;
                    }
                }
            }
            write.write(meses[indiceMenorVenda] + " " + menorVenda);


            write.write("total geral de vendas");
            write.write(String.valueOf(totalGeralVendas));


            write.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    
    
    
    }

    private static int obterIndiceLoja(String loja) {
        switch (loja) {
            case "Matriz":
                return 0;
            case "Filial Sul":
                return 1;
            case "Filial Norte":
                return 2;
            case "Filial Nordeste":
                return 3;
            default:
                return -1;
        }
    }



        private static int obterIndiceMes(String mes) {
            switch (mes) {
                case "Janeiro":
                    return 1;
                case "Fevereiro":
                    return 2;
                case "Março":
                    return 3;
                case "Abril":
                    return 4;
                case "Maio":
                    return 5;
                case "Junho":
                    return 6;
                case "Julho":
                    return 7;
                case "Agosto":
                    return 8;
                case "Setembro":
                    return 9;
                case "Outubro":
                    return 10;
                case "Novembro":
                    return 11;
                case "Dezembro":
                    return 12;
                default:
                    return -1;
            }
        }
    } 

