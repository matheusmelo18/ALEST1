import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Path arquivoEntrada = Paths.get("./ALEST1/CSV/transacoes_00500.csv");
        Path arquivoSaida = Paths.get("resultados.txt");

        try (BufferedReader br = Files.newBufferedReader(arquivoEntrada);
                PrintWriter pd = new PrintWriter(new FileWriter(arquivoSaida.toFile()))) {

            Map<String, Integer> transacoes = new HashMap<>();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                String produto = dados[0];
                int quantidade = Integer.parseInt(dados[1]);
                transacoes.put(produto, transacoes.getOrDefault(produto, 0) + quantidade);
            }

            // Adding a header to the output file
            pd.println("Product: Quantity");

            for (Map.Entry<String, Integer> entry : transacoes.entrySet()) {
                String produto = entry.getKey();
                int quantidade = entry.getValue();
                // Formatting the output with a specific separator
                pd.println(produto + " - " + quantidade);
            }

        } catch (IOException e) {
            System.out.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}
