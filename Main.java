import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        String linhas[] = new String[100000];
        int numLinhas = 0;

        Path filePath = Paths.get("C:\\Users\\mathe\\OneDrive\\Área de Trabalho\\TrabAlest\\ALEST1\\CSV\\transacoes_00020.csv");

        // Ler o arquivo
        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
            String line;
            boolean primeiraLinha = true;
            while ((line = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula a primeira linha (cabeçalhos)
                }
                linhas[numLinhas] = line;
                numLinhas++;
            }
        } catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: %s%n", e);
            return;
        }

        // Matriz para armazenar o valor total de vendas por loja x mês
        int[][] vendasPorLojaMes = new int[4][12]; // 4 lojas, 12 meses

        for (int i = 0; i < numLinhas; i++) {
            String[] campos = linhas[i].split(",");

            // Verificar se há registros incompletos
            if (campos.length != 4) {
                System.err.println("Registro incompleto na linha " + (i + 2));
                continue; // Pula para a próxima linha
            }

            // Determinar o mês da transação
            int mes = getMonthIndex(campos[1]);

            // Determinar a loja da transação
            String loja = campos[2];

            // Verificar se o valor é numérico
            if (!isNumeric(campos[3])) {
                System.err.println("Valor inválido na linha " + (i + 2));
                continue; // Pula para a próxima linha
            }

            // Determinar o valor da transação
            int valor = Integer.parseInt(campos[3]);

            // Encontrar o índice correspondente à loja
            int lojaIndex = getStoreIndex(loja);

            // Ignorar lojas inválidas
            if (lojaIndex == -1) {
                System.err.println("Loja inválida na linha " + (i + 2));
                continue; // Pula para a próxima linha
            }

            // Atualizar o valor total de vendas para a loja e mês correspondentes
            vendasPorLojaMes[lojaIndex][mes] += valor;
        }

        // Encontrar os três meses com maior valor de vendas
        int[] topMonths = findTopMonths(vendasPorLojaMes);

        // Encontrar o mês com menor venda
        int lowestMonth = findLowestMonth(vendasPorLojaMes);

        // Calcular o total geral de vendas
        int totalSales = calculateTotalSales(vendasPorLojaMes);

        // Escrever os resultados no arquivo resultados.txt
        Path outputPath = Paths.get(".\\resultados.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, Charset.defaultCharset())) {
            // Escrever a matriz de vendas
            writer.write("                    Janeiro     Fevereiro   Março       Abril       Maio        Junho       Julho       Agosto      Setembro    Outubro     Novembro    Dezembro    \n");
            String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
            String [] nomes = {"Matriz, Filial Sul, Filial Norte, Filia Nordeste"};
            for (int i = 0; i < 4; i++) {
                writer.write(String.format("%-20s", i));
                for (int j = 0; j < 12; j++) {
                    writer.write(String.format("%-12s", vendasPorLojaMes[i][j]));
                }
                writer.write("\n");
            }

            // Escrever os três meses com maior valor de vendas
            writer.write("\nTrês meses com maior venda\n");
            for (int i = 0; i < 3; i++) {
                writer.write(meses[topMonths[i]] + " " + vendasPorLojaMes[0][topMonths[i]] + "\n");
            }

            // Escrever o mês com menor venda
            writer.write("\nMês com menos vendas\n");
            if (lowestMonth != -1) {
                writer.write(meses[lowestMonth] + " " + vendasPorLojaMes[0][lowestMonth] + "\n");
            } else {
                writer.write("Nenhum mês registrou vendas\n");
            }

            // Escrever o total geral de vendas e a média por transação
            writer.write("\nTotal geral de vendas: " + totalSales + "\n");
            writer.write("\nMédia por transação: " + ((double) totalSales / numLinhas) + "\n");
        } catch (IOException e) {
            System.err.format("Erro na escrita do arquivo: %s%n", e);
        }
    }

    private static int getStoreIndex(String loja) {
            String matriz="Matriz";
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

                return 4;

        }
    }

    private static int getMonthIndex(String mes) {
        switch (mes) {
            case "Janeiro":
                return 0;
            case "Fevereiro":
                return 1;
            case "Marco":
                return 2;
            case "Abril":
                return 3;
            case "Maio":
                return 4;
            case "Junho":
                return 5;
            case "Julho":
                return 6;
            case "Agosto":
                return 7;
            case "Setembro":
                return 8;
            case "Outubro":
                return 9;
            case "Novembro":
                return 10;
            case "Dezembro":
                return 11;
            default:
                return -1;
        }
    }

    private static int[] findTopMonths(int[][] vendasPorLojaMes) {
        int[] topMonths = new int[3];
        int[] sortedMonths = new int[12];
        for (int i = 0; i < 12; i++) {
            sortedMonths[i] = i;
        }
        for (int i = 0; i < 12; i++) {
            for (int j = i + 1; j < 12; j++) {
                if (vendasPorLojaMes[0][sortedMonths[j]] > vendasPorLojaMes[0][sortedMonths[i]]) {
                    int temp = sortedMonths[i];
                    sortedMonths[i] = sortedMonths[j];
                    sortedMonths[j] = temp;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            topMonths[i] = sortedMonths[i];
        }
        return topMonths;
    }

    private static int findLowestMonth(int[][] vendasPorLojaMes) {
        int lowestMonth = 0;
        for (int i = 1; i < 12; i++) {
            if (vendasPorLojaMes[0][i] < vendasPorLojaMes[0][lowestMonth]) {
                lowestMonth = i;
            }
        }
        return lowestMonth;
    }

    private static int calculateTotalSales(int[][] vendasPorLojaMes) {
        int totalSales = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                totalSales += vendasPorLojaMes[i][j];
            }
        }
        return totalSales;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
