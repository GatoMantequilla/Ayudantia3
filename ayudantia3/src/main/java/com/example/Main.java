package com.example;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        iniciarJuego();
    }

    //-----------------------------------------------------------------------------------------------------------------

    public static void iniciarJuego() {
        int size_MAPA = 10;
        String[][] mapa = inicializarMapa(size_MAPA);
        int[] posicionJugador = generarPosicionAleatoria(mapa, size_MAPA);
        int[] posicionEnemigo = generarPosicionAleatoria(mapa, size_MAPA);
        int[] posicionDestino = {8, 8};
        int saludJugador = 100;
        int ataqueJugador = 15;
        int saludEnemigo = 45;
        int ataqueEnemigo = 10;
        Scanner scanner = new Scanner(System.in);

        configurarJuego(mapa, posicionJugador, posicionEnemigo, posicionDestino);
        ejecutarJuego(mapa, posicionJugador, posicionDestino, scanner, saludJugador, ataqueJugador, saludEnemigo, ataqueEnemigo, posicionEnemigo);
    }



    public static void configurarJuego(String[][] mapa, int[] posicionJugador, int[] posicionEnemigo, int[] posicionDestino) {
        colocarEnMapa(mapa, posicionJugador, "P");
        colocarEnMapa(mapa, posicionEnemigo, "E");
        colocarEnMapa(mapa, posicionDestino, "X");
        colocarCofresYObstaculos(mapa);
    }


    public static void ejecutarJuego(String[][] mapa, int[] posicionJugador, int[] posicionDestino, Scanner scanner, int saludJugador, int ataqueJugador, int saludEnemigo, int ataqueEnemigo, int[] posicionEnemigo) {
        while (true) {
            imprimirMapa(mapa);
            System.out.println("Tu salud: " + saludJugador);
            posicionJugador = moverJugador(mapa, posicionJugador, scanner, saludJugador, ataqueJugador, saludEnemigo, ataqueEnemigo, posicionEnemigo);
            if (saludJugador <= 0) {
                System.out.println("Te moriste");
                break;
            }
            if (llegoADestino(posicionJugador, posicionDestino)) {
                System.out.println("Llegaste al destino");
                break;
            }
        }
    }


    public static boolean llegoADestino(int[] posicionJugador, int[] posicionDestino) {
        return posicionJugador[0] == posicionDestino[0] && posicionJugador[1] == posicionDestino[1];
    }


    public static String[][] inicializarMapa(int size_MAPA) {
        String[][] nuevoMapa = new String[size_MAPA][size_MAPA];
        for (int i = 0; i < size_MAPA; i++) {
            for (int j = 0; j < size_MAPA; j++) {
                nuevoMapa[i][j] = (i == 0 || i == size_MAPA - 1 || j == 0 || j == size_MAPA - 1) ? "#" : ".";
            }
        }
        return nuevoMapa;
    }


    public static int[] generarPosicionAleatoria(String[][] mapa, int size_MAPA) {
        Random aleatorio = new Random();
        int x, y;
        do {
            x = aleatorio.nextInt(size_MAPA);
            y = aleatorio.nextInt(size_MAPA);
        } while (!mapa[x][y].equals("."));
        return new int[]{x, y};
    }


    public static void colocarEnMapa(String[][] mapa, int[] posicion, String simbolo) {
        mapa[posicion[0]][posicion[1]] = simbolo;
    }


    public static void colocarCofresYObstaculos(String[][] mapa) {
        for (int i = 0; i < 10; i++) {
            colocarEnMapa(mapa, generarPosicionAleatoria(mapa, mapa.length), "C");
            colocarEnMapa(mapa, generarPosicionAleatoria(mapa, mapa.length), "#");
        }
    }


    public static void imprimirMapa(String[][] mapa) {
        for (String[] fila : mapa) {
            for (String celda : fila) {
                System.out.print(celda + " ");
            }
            System.out.println();
        }
    }


    public static int[] moverJugador(String[][] mapa, int[] posicionJugador, Scanner scanner, int saludJugador, int ataqueJugador, int saludEnemigo, int ataqueEnemigo, int[] posicionEnemigo) {
        String movimiento = obtenerMovimiento(scanner);
        int[] nuevaPosicion = calcularNuevaPosicion(posicionJugador, movimiento);
        if (esMovimientoValido(mapa, nuevaPosicion)) {
            saludJugador = verificarPosicion(mapa, nuevaPosicion, posicionEnemigo, saludJugador, ataqueJugador, saludEnemigo, ataqueEnemigo, scanner);
            if (saludJugador > 0) {
                actualizarMapa(mapa, posicionJugador, nuevaPosicion);
                posicionJugador = nuevaPosicion;
            }
        } else {
            System.out.println("No puedes moverte hacia allí");
        }
        return posicionJugador;
    }


    public static String obtenerMovimiento(Scanner scanner) {
        System.out.println("Arriba (w) ");
        System.out.println("Abajo (s)");
        System.out.println("Izquierda (a)");
        System.out.println("Derecha (d)");
        return scanner.nextLine();
    }


    public static int[] calcularNuevaPosicion(int[] posicionJugador, String movimiento) {
        int nuevaX = posicionJugador[0];
        int nuevaY = posicionJugador[1];
        switch (movimiento) {
            case "w": nuevaX--; break;
            case "a": nuevaY--; break;
            case "s": nuevaX++; break;
            case "d": nuevaY++; break;
            default: System.out.println("Movimiento inválido"); break;
        }
        return new int[]{nuevaX, nuevaY};
    }


    public static boolean esMovimientoValido(String[][] mapa, int[] nuevaPosicion) {
        int nuevaX = nuevaPosicion[0];
        int nuevaY = nuevaPosicion[1];
        return nuevaX >= 0 && nuevaX < mapa.length && nuevaY >= 0 && nuevaY < mapa.length && !mapa[nuevaX][nuevaY].equals("#");
    }


    public static void actualizarMapa(String[][] mapa, int[] posicionAnterior, int[] nuevaPosicion) {
        mapa[posicionAnterior[0]][posicionAnterior[1]] = ".";
        mapa[nuevaPosicion[0]][nuevaPosicion[1]] = "P";
    }


    public static int verificarPosicion(String[][] mapa, int[] nuevaPosicion, int[] posicionEnemigo, int saludJugador, int ataqueJugador, int saludEnemigo, int ataqueEnemigo, Scanner scanner) {
        int nuevaX = nuevaPosicion[0], nuevaY = nuevaPosicion[1];
        if (nuevaX == posicionEnemigo[0] && nuevaY == posicionEnemigo[1]) {
            saludJugador = iniciarCombate(saludJugador, ataqueJugador, saludEnemigo, ataqueEnemigo, scanner);
        }
        else if (mapa[nuevaX][nuevaY].equals("C")) {
            saludJugador = abrirCofre(saludJugador, scanner);
            mapa[nuevaX][nuevaY] = ".";
        }
        return saludJugador;
    }


    public static int iniciarCombate(int saludJugador, int ataqueJugador, int saludEnemigo, int ataqueEnemigo, Scanner scanner) {
        System.out.println("Te has encontrado a un enemigo");
        while (saludEnemigo > 0 && saludJugador > 0) {
            String accion = obtenerAccionCombate(scanner);
            if (accion.equalsIgnoreCase("atacar")) {
                saludEnemigo = realizarAtaqueJugador(ataqueJugador, saludEnemigo);
                if (saludEnemigo > 0) {
                    saludJugador = realizarAtaqueEnemigo(ataqueEnemigo, saludJugador);
                }
            } else if (accion.equalsIgnoreCase("huir")) {
                return escaparDelCombate(saludJugador);
            } else {
                System.out.println("Acción inválida");
            }
        }
        if (saludEnemigo <= 0) {
            notificarVictoria();
        }
        return saludJugador;
    }


    public static String obtenerAccionCombate(Scanner scanner) {
        System.out.println("atacar / huir");
        return scanner.nextLine();
    }


    public static int realizarAtaqueJugador(int ataqueJugador, int saludEnemigo) {
        saludEnemigo -= ataqueJugador;
        System.out.println("Navajazo, salud del enemigo: " + saludEnemigo);
        return saludEnemigo;
    }


    public static int realizarAtaqueEnemigo(int ataqueEnemigo, int saludJugador) {
        saludJugador -= ataqueEnemigo;
        System.out.println("Te navajearon, tù salud: " + saludJugador);
        return saludJugador;
    }


    public static int escaparDelCombate(int saludJugador) {
        System.out.println("Saliste corriendo, gallina");
        return saludJugador;
    }


    public static void notificarVictoria() {
        System.out.println("Se murió el enemigo :o");
    }


    public static int abrirCofre(int saludJugador, Scanner scanner) {
        Random aleatorio = new Random();
        System.out.println("Encontraste un cofre ¿Abrir? (s/n)");
        String decision = scanner.nextLine();
        if (decision.equalsIgnoreCase("s")) {
            if (aleatorio.nextBoolean()) {
                System.out.println("Es una trampa, -20 puntos de salud.");
                saludJugador -= 20;
                if (saludJugador < 0) {
                    saludJugador = 0;
                }
            } else {
                System.out.println("Poción de vida. +20 puntos de salud");
                saludJugador += 20;
            }
        } else {
            System.out.println("Decidiste no abrir el cofre.");
        }
        return saludJugador;
    }
}
