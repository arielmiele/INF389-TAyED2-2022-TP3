import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;

/*
 * INF389 - Taller de Algoritmos y Estructura de Datos II
 * Trabajo Práctico N° 3
 * Nombre: Ariel Gerardo Miele
 * Legajo: VINF011482
 * DNI: 34.434.704
 */

public class Archivos {

    /**
     * Método que permite borrar la consola para una mejor utilización del sistema
     */
    public static void ClearConsole() {
        try {
            // Verifica el sistema donde se está ejecutando el programa
            String operatingSystem = System.getProperty("os.name");

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

        // Tamaño de la tabla inicial
        int tamanotabla = 0; // Inicializa el tamaño de tabla en 0

        /**
         * Transforma un dato del tipo Byte a un dato del mismo tipo pero sin signo
         * 
         * @param dato que se necesita transformar
         * @return el resultado del dato transformado
         */
        int ByteToUnsignedByte(byte dato) {
            int resultado = (int) dato;
            if (dato < 0) {
                resultado = (int) dato + 256;
            }
            return resultado;
        }

        /**
         * Crea la tabla de Huffman en el arreglo (Tabla de Frecuencias)
         * 
         * @param NombreArchivo es la dirección al archivo, contiene el nombre del
         *                      archivo
         */
        void CargarTablaDeArchivo(String NombreArchivo) {
            NodoInicial = null;
            TablaHuffman = new int[256];
            TablaHuffmanCodigos = new String[256];
            for (int i = 0; i <= 255; i++) {
                TablaHuffman[i] = 0;
                TablaHuffmanCodigos[i] = "";
            }
            ClearConsole(); // Limpia la consola
            System.out.println("Se carga el siguiente archivo a comprimir: " + NombreArchivo); // Muestra el archivo a
                                                                                               // cargar
            try {
                RandomAccessFile file = new RandomAccessFile(NombreArchivo, "r"); // Abre el archivo
                byte dato;
                int entero;
                long cont = 0;
                long tamano = file.length(); // Obtiene el tamaño del archivo
                System.out.println("\nTamaño original del archivo: " + tamano); // Muestra el tamaño inicial del archivo

                // Comienza la carga de datos a partir del archivo en la Tabla Huffman
                while (cont < tamano) {
                    file.seek(cont);
                    dato = file.readByte();
                    entero = ByteToUnsignedByte(dato);
                    TablaHuffman[entero]++;
                    cont++;
                }
                // Finaliza la carga de datos a partir del archivo en la Tabla Huffman

                file.close(); // Cierra el archivo

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Calcula el tamaño de la Tabla Huffman
            for (int i = 0; i <= 255; i++) {
                if (TablaHuffman[i] > 0) {
                    tamanotabla++;
                }
            }

            // Muestra por pantalla el tamaño de la Tabla Huffman
            System.out.print("\nTamaño de la tabla: " + tamanotabla + "\n");
        }

        /**
         * Inserta un nuevo Nodo en la lista de árboles
         * 
         * @param NodoAInsertar es el Nodo que se insertará
         */
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
                    } else {
                        NodoAInsertar.siguiente = NodoInicial;
                        NodoInicial = NodoAInsertar;
                    }
                } else {
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

        /**
         * Muestra la Lista de Árboles por consola
         */
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

        /**
         * Crea la Lista de Árboles y la muestra por consola
         */
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

        /**
         * Este proceso reduce la lista de árboles a un sólo arbol con un Nodo Inicial
         */
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

        /**
         * Genera los códigos de Huffman a partir del Árbol de Huffman
         * 
         * @param AUXNODORAIZ es el Nodo del Árbol de Huffman para el que se quiere
         *                    crear el código
         * @param codigo      es la cadena inicial para la generación del código
         */
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

        /**
         * Transforma una string a una cadena de bytes
         * 
         * @param strtobyte cadena a transformar
         * @return la cadena de bytes transformada
         */
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

        /**
         * Procesa el buffer del archivo
         * 
         * @param STRBUFF es el buffer que debe procesarse
         * @param archivo al que pertenece el buffer
         * @return string auxiliar procesado del buffer del archivo
         * @throws IOException
         */
        String procesarbuffer(String STRBUFF, RandomAccessFile archivo) throws IOException {
            String Auxstr = STRBUFF, STRINGBYTE = "";
            while (Auxstr.length() >= 8) {
                STRINGBYTE = Auxstr.substring(0, 8);
                Auxstr = Auxstr.substring(8, Auxstr.length());
                archivo.writeByte(stringbytetobyte(STRINGBYTE));
            }
            return Auxstr;
        }

        /**
         * Genera el archivo comprimido
         * 
         * @param NombreArchivoO archivo de origen
         * @param NombreArchivoD archivo de destino
         */
        void GenerarArchivoComprimido(String NombreArchivoO, String NombreArchivoD) {
            // Grabamos el archivo a comprimir
            String STRBuffer = "";
            String STRBuffertmp = "";
            // Borrar si ya existe el archivo
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
                while (cont < tamano) {
                    archivoorigen.seek(cont);
                    dato = archivoorigen.readByte();
                    entero = ByteToUnsignedByte(dato);
                    // Codificar en buffer de string
                    STRBuffer = STRBuffer + TablaHuffmanCodigos[entero];
                    STRBuffertmp = STRBuffertmp + TablaHuffmanCodigos[entero] + " ";
                    STRBuffer = procesarbuffer(STRBuffer, archivodestino);
                    cont++;
                }
                System.out.println("Buffer temporal del archivo: \n" + STRBuffertmp + "\n");

                long tamanioD = archivodestino.length(); // Calcula el tamaño del archivo de destino (comprimido)

                // Muestra el tamaño del archivo origen
                System.out.println("Tamaño del archivo de origen: " + tamano + " bytes.");
                // Muestra el tamaño del archivo de destino
                System.out.println("Tamaño del archivo de destino: " + tamanioD + " bytes.");
                // Cierra los archivos abiertos
                System.out.println("Se disminuyó el tamaño original del archivo en " + (tamano - tamanioD)
                        + " bytes. Impresionante!");

                // Cierra los archivos abiertos
                archivoorigen.close();
                archivodestino.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Huffman
    public ArbolHuffman AH;

    /**
     * Realiza la compresión del archivo
     * 
     * @param NombreArchivo es el archivo a comprimir
     */
    void Comprimir(String NombreArchivo) {
        ArbolHuffman AH = new ArbolHuffman();
        if (AH != null) {
            AH.CargarTablaDeArchivo(NombreArchivo);
            AH.CrearListaDeArboles();
            AH.ProcesarListaDeArboles();
            System.out.println("Se muestra el código generado por el Algoritmo de Huffman para cada clave:");
            AH.GenerarCodigosDeHuffman_recursivo(AH.NodoInicial.raiz, "");
            AH.GenerarArchivoComprimido(NombreArchivo, NombreArchivo + ".compress");
        }
    }

    // Main para la ejecución del programa
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        int opcion = 0;
        ClearConsole();

        // Consulta al usuario qué archivo desea comprimir
        while (opcion != 1 && opcion != 2) {
            System.out.println("¡Bienvenido al programa de prueba del Algoritmo de Huffman! Elija una opción:\n");
            System.out.println("1 - Comprimir Archivo (abre diálogo de selección)\n2 - Salir\n");
            System.out.print("Opción elegida: ");
            if (entrada.hasNextInt()) {
                opcion = entrada.nextInt();
            }

            if (opcion != 1 && opcion != 2) {
                System.out.println(
                        "La opción seleccionada no es válida. Presione Enter para continuar...");
                try {
                    System.in.read();
                    entrada.nextLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClearConsole();
            }
            ClearConsole();
        }

        if (opcion == 1) {
            System.out.print("Seleccione un archivo para comprimir en la ventana abierta.");
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);
            File file = null;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                ClearConsole();
                // Crear arbol de Huffman
                Archivos AHuffman3 = new Archivos();
                String linkArchivo = file.getAbsolutePath();
                // Ejecuta la compresión del archivo
                AHuffman3.Comprimir(linkArchivo);
            } else {
                System.out.println("No se seleccionó un archivo válido.");
            }
            entrada.close();
            System.out.println("\nMuchas gracias por usar el programa de compresión.\n");
        } else {
            entrada.close();
            System.out.println("\nMuchas gracias por usar el programa de compresión.\n");
        }
    }
}