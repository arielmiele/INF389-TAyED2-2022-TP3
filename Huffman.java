import java.io.*;
import java.util.Scanner;

/*
 * INF389 - Taller de Algoritmos y Estructura de Datos II
 * Trabajo Práctico N° 3
 * Nombre: Ariel Gerardo Miele
 * Legajo: VINF011482
 * DNI: 34.434.704
 */

public class Huffman {

    /**
     * Método que permite borrar la consola para una mejor utilización del sistema
     */
    public static void ClearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name"); // Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Sub clases
    class ArbolHuffman {
        // Clase Nodo del arbol de Huffman
        class NodoArbol {
            char clave;
            int frecuencia;
            boolean esdato;
            NodoArbol izquierda, derecha;

            public NodoArbol(char car, int frec, boolean EsD) {
                clave = car;
                frecuencia = frec;
                esdato = EsD;
                izquierda = derecha = null;
            }
        }

        // Clase Nodo de una lista (lista de arboles)}
        class NodoLista {
            public NodoArbol raiz;
            public NodoLista siguiente;

            public NodoLista(NodoArbol Nodo) {
                raiz = Nodo;
                siguiente = null;
            }
        }

        // Nodo inicial de la lista, dato referencial
        NodoLista NodoInicial;

        // Tabla de Huffman para caracteres ASCII
        int TablaHuffman[];

        // Tabla de Huffman para las frecuencias
        String TablaHuffmanCodigos[]; // Crea la el array de strings
        int tamanotabla = 0; // Inicializa el tamaño de tabla en 0

        int ByteToUnsignedByte(byte dato) {
            int resultado = (int) dato;
            if (dato < 0) {
                resultado = (int) dato + 256;
            }
            return resultado;
        }

        // Creamos la tabla de Huffman en el arreglo (Tabla de frecuencias)
        void CargarTablaDeArchivo(String NombreArchivo) {
            NodoInicial = null;
            TablaHuffman = new int[256];
            TablaHuffmanCodigos = new String[256];
            for (int i = 0; i <= 255; i++) {
                TablaHuffman[i] = 0;
                TablaHuffmanCodigos[i] = "";
            }
            ClearConsole();
            System.out.println("Se carga el siguiente archivo a comprimir: " + NombreArchivo);
            try {
                RandomAccessFile file = new RandomAccessFile(NombreArchivo, "r");
                byte dato;
                int entero;
                long cont = 0;
                long tamano = file.length();
                System.out.println("\nTamaño original del archivo: " + tamano);
                while (cont < tamano) {
                    file.seek(cont);
                    dato = file.readByte();
                    entero = ByteToUnsignedByte(dato);
                    TablaHuffman[entero]++;
                    cont++;
                }
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i <= 255; i++) {
                if (TablaHuffman[i] > 0) {
                    tamanotabla++;
                }
            }

            System.out.print("\nTamaño de la tabla: " + tamanotabla + "\n");
        }

        // Crear Lista de Arboles
        void InsertarNodoLista(NodoLista NodoAInsertar) {
            NodoLista AUXposnodo, AUXposnodoant, auxnodo;
            // Insertar el nodo en la lista
            if (NodoInicial == null) {
                NodoInicial = NodoAInsertar;
            } else {
                AUXposnodo = NodoInicial;
                AUXposnodoant = null;

                while ((NodoAInsertar.raiz.frecuencia >= AUXposnodo.raiz.frecuencia) & (AUXposnodo.siguiente != null)) {
                    AUXposnodoant = AUXposnodo;
                    AUXposnodo = AUXposnodo.siguiente;
                }

                if (AUXposnodoant == null) {
                    if (NodoAInsertar.raiz.frecuencia >= AUXposnodo.raiz.frecuencia) {
                        AUXposnodo.siguiente = NodoAInsertar;
                        // NodoAInsertar.siguiente=null;
                    } else {
                        NodoAInsertar.siguiente = NodoInicial;
                        NodoInicial = NodoAInsertar;
                    }
                } else {
                    // AUXposnodoant.siguiente = NodoAInsertar;
                    if (NodoAInsertar.raiz.frecuencia >= AUXposnodo.raiz.frecuencia) {
                        auxnodo = AUXposnodo.siguiente;
                        AUXposnodo.siguiente = NodoAInsertar;
                        NodoAInsertar.siguiente = auxnodo;
                    } else {
                        AUXposnodoant.siguiente = NodoAInsertar;
                        NodoAInsertar.siguiente = AUXposnodo;
                    }
                }
            }
        }

        void MostrarListaDeArboles() {
            NodoLista AUXnodo = NodoInicial;
            int count = 0;
            System.out.print("\nLista de arboles: \n");
            while (AUXnodo != null) {
                count++;
                System.out.print("Arbol: " + count + " | ");
                System.out.print(" Carácter: " + "'" + AUXnodo.raiz.clave + "' | ");
                System.out.print(" Frecuencia: (");
                System.out.print(AUXnodo.raiz.frecuencia);
                System.out.print(")\n");
                AUXnodo = AUXnodo.siguiente;
            }
            System.out.println(" ");
        }

        void CrearListaDeArboles() {
            int pos = 0;
            System.out.println("\nTabla: ");
            for (int i = 0; i <= 255; i++) {
                if (TablaHuffman[i] > 0) {
                    pos++;
                    // Crear un nodo raiz de arbol que contiene DATOS (flag en true)
                    NodoArbol AUXNA = new NodoArbol((char) i, TablaHuffman[i], true);
                    NodoLista AUXNL = new NodoLista(AUXNA);
                    // Contenido de la tabla
                    System.out.print("Nodo: " + pos + " | ");
                    System.out.print("Carácter: " + (char) i + " | ");
                    System.out.println("Frecuencia: (" + TablaHuffman[i] + ")");
                    InsertarNodoLista(AUXNL);
                }
            }
            System.out.println(" ");
            MostrarListaDeArboles();
        }

        // Proceso para reducir la lista a un solo nodo con un solo árbol
        void ProcesarListaDeArboles() {
            if (NodoInicial != null) {
                NodoLista AUXRECORIDONL, AUXNL1, AUXNL2;
                AUXRECORIDONL = NodoInicial;
                // Verificar que exista la lista
                if (AUXRECORIDONL != null) {
                    // Mientras la lista tenga más de 2 nodos, procesar
                    while (AUXRECORIDONL.siguiente != null) {
                        // Buscar los 2 primeros nodos
                        AUXNL1 = AUXRECORIDONL;
                        AUXNL2 = AUXRECORIDONL.siguiente;
                        // Eliminar ambos nodos de la lista
                        NodoInicial = AUXNL2.siguiente;
                        // Crear un nuevo nodo raíz que tenga los 2 subarboles que NO contiene DATOS
                        // (flag en false) con la sumatoria de frecuencias
                        NodoArbol AUXNA = new NodoArbol('*', AUXNL1.raiz.frecuencia + AUXNL2.raiz.frecuencia, false);
                        AUXNA.izquierda = AUXNL1.raiz;
                        AUXNA.derecha = AUXNL2.raiz;
                        // Creamos un nuevo nodo de lista para insertar en reemplazo de los 2 eliminados
                        NodoLista AUXNL = new NodoLista(AUXNA);
                        // Insertar nodo nuevo a la lista en forma ordenada
                        InsertarNodoLista(AUXNL);
                        AUXRECORIDONL = NodoInicial;
                    }
                }
            }
        }

        void GenerarCodigosDeHuffman_recursivo(NodoArbol AUXNODORAIZ, String codigo) {
            // Método a desarrollar
            // Como primer paso analizamos si el nodo AUXNODORAIZ es una hoja del arbol
            if (AUXNODORAIZ.izquierda == null && AUXNODORAIZ.derecha == null) {
                // En caso de que sea una hoja, mostramos la CLAVE (caracter) y el código de
                // Huffman generado
                System.out.println("Clave: " + AUXNODORAIZ.clave + " | Código: " + codigo);
                // Luego almacenamos el código de Huffman dentro de la Tabla
                // TablaHuffmanCodigos, en la posición de referencia de la clave
                TablaHuffmanCodigos[AUXNODORAIZ.clave] = codigo;
                return;
            }
            // Se vuelve a llamar al método de forma recursiva avanzando por el árbol hacia
            // la izquierda, agregando el código 0 a la String codigo
            GenerarCodigosDeHuffman_recursivo(AUXNODORAIZ.izquierda, codigo + "0");
            // Se vuelve a llamar al método de forma recursiva avanzando por el árbol hacia
            // la derecha, agregando el código 1 a la String codigo
            GenerarCodigosDeHuffman_recursivo(AUXNODORAIZ.derecha, codigo + "1");
        }

        byte stringbytetobyte(String strtobyte) {
            byte Byteresult = 0;
            int Intresult = 0;
            if (strtobyte.length() > 0)
                if (Integer.parseInt(strtobyte.substring(0, 1)) > 0)
                    Intresult = Intresult + 128;
            if (strtobyte.length() > 1)
                if (Integer.parseInt(strtobyte.substring(1, 2)) > 0)
                    Intresult = Intresult + 64;
            if (strtobyte.length() > 2)
                if (Integer.parseInt(strtobyte.substring(2, 3)) > 0)
                    Intresult = Intresult + 32;
            if (strtobyte.length() > 3)
                if (Integer.parseInt(strtobyte.substring(3, 4)) > 0)
                    Intresult = Intresult + 16;
            if (strtobyte.length() > 4)
                if (Integer.parseInt(strtobyte.substring(4, 5)) > 0)
                    Intresult = Intresult + 8;
            if (strtobyte.length() > 5)
                if (Integer.parseInt(strtobyte.substring(5, 6)) > 0)
                    Intresult = Intresult + 4;
            if (strtobyte.length() > 6)
                if (Integer.parseInt(strtobyte.substring(6, 7)) > 0)
                    Intresult = Intresult + 2;
            if (strtobyte.length() > 7)
                if (Integer.parseInt(strtobyte.substring(7, 8)) > 0)
                    Intresult = Intresult + 1;
            Byteresult = (byte) Intresult;
            return Byteresult;
        }

        String procesarbuffer(String STRBUFF, RandomAccessFile archivo) throws IOException {
            String Auxstr = STRBUFF, STRINGBYTE = "";
            while (Auxstr.length() >= 8) {
                STRINGBYTE = Auxstr.substring(0, 8);
                Auxstr = Auxstr.substring(8, Auxstr.length());
                archivo.writeByte(stringbytetobyte(STRINGBYTE));
            }
            return Auxstr;
        }

        void GenerarArchivoComprimido(String NombreArchivoO, String NombreArchivoD) {
            // Grabamos el archivo a comprimir
            String STRBuffer = "";
            String STRBuffertmp = "";
            // Borrar si existe el archivo
            File arch = new File(NombreArchivoD);
            if (arch.delete())
                System.out.println("\nArchivo antiguo borrado.\n");
            try {
                // Abriendo el archivo original de solo lectura
                RandomAccessFile archivoorigen = new RandomAccessFile(NombreArchivoO, "r");
                // Abriendo el archivo destino como lectura escritura
                RandomAccessFile archivodestino = new RandomAccessFile(NombreArchivoD, "rw");
                int entero;
                byte dato;
                long cont = 0;
                long tamano = archivoorigen.length();
                // System.out.println(STRBuffer);
                while (cont < tamano) {
                    archivoorigen.seek(cont);
                    dato = archivoorigen.readByte();
                    entero = ByteToUnsignedByte(dato);
                    // Codificar en buffer de string
                    // System.out.println(entero);
                    STRBuffer = STRBuffer + TablaHuffmanCodigos[entero];
                    STRBuffertmp = STRBuffertmp + " " + TablaHuffmanCodigos[entero];
                    // System.out.println(TablaHuffmanCodigos[entero]);
                    STRBuffer = procesarbuffer(STRBuffer, archivodestino);
                    cont++;
                }
                System.out.println("Buffer temporal del archivo: \n" + STRBuffertmp + "\n");
                archivoorigen.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Huffman
    public ArbolHuffman AH;

    void Comprimir(String NombreArchivo) {
        ArbolHuffman AH = new ArbolHuffman();
        if (AH != null) {
            AH.CargarTablaDeArchivo(NombreArchivo);
            // System.out.println("Crea lista: ");
            AH.CrearListaDeArboles();
            AH.ProcesarListaDeArboles();
            System.out.println("Se muestra el código generado por el Algoritmo de Huffman para cada clave:");
            AH.GenerarCodigosDeHuffman_recursivo(AH.NodoInicial.raiz, "");
            AH.GenerarArchivoComprimido(NombreArchivo, NombreArchivo + ".compress");
        }
    }

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        int opcion = 0;
        ClearConsole();

        while (opcion != 1 && opcion != 2) {
            System.out.println("¡Bienvenido al programa de prueba del Algoritmo de Huffman! Elija una opción: ");
            System.out.println(
                    "1 - Comprimir Archivo 1 ('estamos bien')\n2 - Comprimir Archivo 2 ('***yyyyyjjjjjjjjjj--------------------,,,,,,,,,,,,,,,,,,,,,,,,,')");
            System.out.println("\nOpción elegida: ");
            opcion = entrada.nextInt();
            ClearConsole();
        }

        if (opcion == 1) {
            // Crear arbol de Huffman
            Huffman AHuffman1 = new Huffman();
            // Cambiar el nombre del archivo por el archivo deseado
            AHuffman1.Comprimir(
                    "C:\\Users\\miele\\OneDrive\\Universidad Siglo 21\\2022\\2022 - 2B\\INF389 - Taller de Algoritmos y Estructura de Datos II\\Modulo 3\\INF389-TAyED2-2022-TP3\\prueba1_Huffman.txt");

        } else if (opcion == 2) {
            // Crear arbol de Huffman
            Huffman AHuffman2 = new Huffman();
            // Cambiar el nombre del archivo por el archivo deseado
            AHuffman2.Comprimir(
                    "C:\\Users\\miele\\OneDrive\\Universidad Siglo 21\\2022\\2022 - 2B\\INF389 - Taller de Algoritmos y Estructura de Datos II\\Modulo 3\\INF389-TAyED2-2022-TP3\\prueba2_Huffman.txt");
        }
        entrada.close();
    }
}