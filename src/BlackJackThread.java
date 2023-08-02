import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

// Enumerados que representam os naipes (Suits) das cartas
enum Naipe {
    COPAS,
    OUROS,
    ESPADAS,
    PAUS
}

// Enumerados que representam os valores (Ranks) das cartas
enum Valor {
    DOIS(2), TRES(3), QUATRO(4), CINCO(5), SEIS(6), SETE(7), OITO(8), NOVE(9), DEZ(10),
    VALETE(10), DAMA(10), REI(10), AS(1);

    private final int valor;

    Valor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}

// Classe que representa uma carta
class Carta {
    private Valor valor;
    private Naipe naipe;

    public Carta(Valor valor, Naipe naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public Valor getValor() {
        return valor;
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public int getValorCarta() {
        return valor.getValor();
    }
}

// Classe que representa o baralho
class Baralho {
    private List<Carta> cartas;

    public Baralho() {
        // Inicializa o baralho com todas as cartas
        cartas = new ArrayList<>();
        for (Naipe naipe : Naipe.values()) {
            for (Valor valor : Valor.values()) {
                cartas.add(new Carta(valor, naipe));
            }
        }
    }

    public Carta distribuirCarta() {
        // Distribui uma carta aleatória e a remove do baralho
        int indice = (int) (Math.random() * cartas.size());
        return cartas.remove(indice);
    }

}

public class BlackJackThread implements Runnable {
    private static final int NUM_PLAYERS = 2;
    private List<Jogador> jogadores;
    protected Baralho baralho;
    private Lock lock;

    public BlackJackThread() {
        jogadores = new ArrayList<>();
        baralho = new Baralho();

        jogadores.add(new Jogador("Jogador 1", baralho));
        jogadores.add(new Jogador("Jogador 2", baralho));

        lock = new ReentrantLock();
    }

    @Override
    public void run() {
        // Inicia as threads dos jogadores
        Thread[] threads = new Thread[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            threads[i] = new Thread(jogadores.get(i));
            threads[i].start();
        }

        // Aguarda até que todos os jogadores terminem de jogar
        for (int i = 0; i < NUM_PLAYERS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Determina o vencedor
        int indiceVencedor = -1;
        int maiorPontuacao = 0;
        for (int i = 0; i < NUM_PLAYERS; i++) {
            int pontuacaoJogador = jogadores.get(i).getPontuacao();
            if (pontuacaoJogador <= 21 && pontuacaoJogador > maiorPontuacao) {
                maiorPontuacao = pontuacaoJogador;
                indiceVencedor = i;
            }
        }

        // Exibe o resultado
        System.out.println("\n Resultado:\n");
        for (int i = 0; i < NUM_PLAYERS; i++) {
            System.out.println(jogadores.get(i).getNome() + ": " + jogadores.get(i).getPontuacao());
        }

        if (indiceVencedor == -1) {
            System.out.println("\n Nenhum jogador venceu!");
        } else {
            System.out.println("\n O jogador " + jogadores.get(indiceVencedor).getNome() + " venceu!");
        }
    }

    public static void main(String[] args) {
        BlackJackThread jogoBlackJack = new BlackJackThread();
        jogoBlackJack.run();
    }
}

class Jogador implements Runnable {
    private String nome;
    private int pontuacao;
    private Baralho baralho;

    public Jogador(String nome, Baralho baralho) {
        this.nome = nome;
        this.pontuacao = 0;
        this.baralho = baralho;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    @Override
    public void run() {
        while (pontuacao < 21) {
            Carta carta = baralho.distribuirCarta(); // Acessa o baralho através da variável
            pontuacao += carta.getValorCarta();
            System.out.println(nome + " pegou um(a) " + carta.getValor() + " de " + carta.getNaipe() + ". Pontuação total: " + pontuacao);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



