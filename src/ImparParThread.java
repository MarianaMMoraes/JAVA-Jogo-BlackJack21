public class ImparParThread implements Runnable{

    private boolean mostrarPar;

    public ImparParThread(boolean mostrarPar) {
        this.mostrarPar = mostrarPar;
    }

    @Override
    public void run() {
        String tiponumero = mostrarPar ? "Par" : "Ímpar";
        int totalnumero = 20;

        for (int i = mostrarPar ? 0 : 1; i <= totalnumero; i += 2) {
            System.out.println(tiponumero + ": " + i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ImparParThread parThread = new ImparParThread(true);
        ImparParThread imparThread = new ImparParThread(false);

        Thread t1 = new Thread(parThread);
        Thread t2 = new Thread(imparThread);

        t1.start();
        t2.start();
    }
}
