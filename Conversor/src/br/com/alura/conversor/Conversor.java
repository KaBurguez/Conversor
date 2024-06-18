package br.com.alura.conversor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Conversor {

    private static final String URL_API = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        exibirMenu();
    }

    public static void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        String[] moedas = {"USD", "BRL", "ARS", "COP", "EUR"};

        while (true) {
            System.out.println("===========================================");
            System.out.println("Conversor de Moedas");
            System.out.println("===========================================");
            System.out.println("Etapa 1: Selecione a moeda que deseja converter:");
            for (int i = 0; i < moedas.length; i++) {
                System.out.println((i + 1) + ") " + traduzirMoeda(moedas[i]));
            }
            System.out.println("6) Sair");

            int escolhaBase = scanner.nextInt();
            if (escolhaBase == 6) {
                break;
            }

            System.out.println("Etapa 2: Selecione a moeda para qual deseja converter:");
            for (int i = 0; i < moedas.length; i++) {
                if (i + 1 != escolhaBase) {
                    System.out.println((i + 1) + ") " + traduzirMoeda(moedas[i]));
                }
            }
            System.out.println("6) Sair");

            int escolhaDestino = scanner.nextInt();
            if (escolhaDestino == 6) {
                break;
            }

            System.out.println("Digite o valor a ser convertido:");
            double valor = scanner.nextDouble();

            String moedaBase = moedas[escolhaBase - 1];
            String moedaDestino = moedas[escolhaDestino - 1];

            try {
                double taxa = obterTaxaDeConversao(moedaBase, moedaDestino);
                double valorConvertido = valor * taxa;
                System.out.printf("%.2f %s = %.2f %s\n", valor, moedaBase, valorConvertido, moedaDestino);
            } catch (Exception e) {
                System.out.println("Erro ao obter taxa de câmbio: " + e.getMessage());
            }

            System.out.println("Deseja realizar outra conversão? (s/n)");
            String continuar = scanner.next();
            if (!continuar.equalsIgnoreCase("s")) {
                break;
            }
        }

        scanner.close();
    }

    private static double obterTaxaDeConversao(String moedaBase, String moedaDestino) throws Exception {
        String url = URL_API + moedaBase;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return jsonObject.getAsJsonObject("rates").get(moedaDestino).getAsDouble();
    }

    private static String traduzirMoeda(String codigoMoeda) {
        switch (codigoMoeda) {
            case "USD":
                return "Dólar";
            case "BRL":
                return "Real Brasileiro";
            case "ARS":
                return "Peso Argentino";
            case "COP":
                return "Peso Colombiano";
            case "EUR":
                return "Euro";
            default:
                return codigoMoeda;
        }
    }
}
