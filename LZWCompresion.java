package proyecto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vic
 */
import java.io.*;
import java.util.*;

public class LZWCompresion {

    // Definir un HashMap y otras variables
    public HashMap<String, Integer> dictionary = new HashMap<>();
    public int dictSize = 256;
    public String str = "";
    public byte inputByte;
    public byte[] buffer = new byte[3];
    public boolean onleft = true;

    
    public void compress(String uncompressed) throws IOException {
        // Este es el limite de tamaño del diccionario, se construye el diccionario
        for (int i = 0; i < 256; i++) {
            dictionary.put(Character.toString((char) i), i);
        }
        String[] archivo;
        // Leer el archivo no comprimido y escribir archivo comiprimido
        RandomAccessFile read = new RandomAccessFile(uncompressed, "r");
        
        //Aquí puse la extensión con la que se va guardar el archivo comprimido ".lzw"
        archivo = uncompressed.split("\\.");
        RandomAccessFile out = new RandomAccessFile(archivo[0] + ".lzw", "rw"); 
        
        try {
            // Lee el primer caracter del archivo ingresado en el string
            inputByte = read.readByte();
            int i = new Byte(inputByte).intValue();
            if (i < 0) {
                i += 256;
            }
            char ch = (char) i;
            str = "" + ch;

            // Lee caracter x caracter
            while (true) {
                inputByte = read.readByte();
                i = new Byte(inputByte).intValue();

                if (i < 0) {
                    i += 256;
                }
                System.out.print(i + ", ");
                ch = (char) i;

                // Si str + ch está en el diccionario...
                // asignar str a str + ch
                if (dictionary.containsKey(str + ch)) {
                    str = str + ch;
                } else {
                    String s12 = to12bit(dictionary.get(str));

                    // Almacena los 12 bits en un arreglo y luego lo escribe en un archivo de salida 
                    
                    if (onleft) {
                        buffer[0] = (byte) Integer.parseInt(s12.substring(0, 8), 2);
                        buffer[1] = (byte) Integer.parseInt(s12.substring(8, 12) + "0000", 2);
                    } else {
                        buffer[1] += (byte) Integer.parseInt(s12.substring(0, 4), 2);
                        buffer[2] = (byte) Integer.parseInt(s12.substring(4, 12), 2);
                        for (int b = 0; b < buffer.length; b++) {out.writeByte(buffer[b]);
                            buffer[b] = 0;
                        }
                    }
                    onleft = !onleft;

                    // Agrega str + ch al diccionario
                    if (dictSize < 4096) {
                        dictionary.put(str + ch, dictSize++);
                    }

                    // asigna str a ch
                    str = "" + ch;
                }
            }
            
        } catch (IOException e) {
            String str12bit = to12bit(dictionary.get(str));
            if (onleft) {
                buffer[0] = (byte) Integer.parseInt(str12bit.substring(0, 8), 2);
                buffer[1] = (byte) Integer.parseInt(str12bit.substring(8, 12) + "0000", 2);
                out.writeByte(buffer[0]);
                out.writeByte(buffer[1]);
            } else {
                buffer[1] += (byte) Integer.parseInt(str12bit.substring(0, 4), 2);
                buffer[2] = (byte) Integer.parseInt(str12bit.substring(4, 12), 2);

                for (int b = 0; b < buffer.length; b++) {
                    out.writeByte(buffer[b]);
                    buffer[b] = 0;
                }
            }
            read.close();
            out.close();
        }
    }

    /**
     * Convierte 8 bit a 12 bits
     */
     
    public String to12bit(int i) {
        String str = Integer.toBinaryString(i);
        while (str.length() < 12) {
            str = "0" + str;
        }
        return str;
    }

   //Ya el main
    public static void main(String[] args) throws IOException {
        try {
            LZWCompresion lzw = new LZWCompresion(); //Instancia de la clase

            Scanner input = new Scanner(System.in);

            System.out.println("Ingrese el nombre de su (nombredearchivo.txt) archivo...");

            String str = input.nextLine(); //Para ingresar el nombre del objeto que tenemos almacenado

            File file = new File(str);

            Scanner fileScanner = new Scanner(file);

            String line = "";

            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();
                System.out.println("Los contenidos de su archivo estan siendo comprimidos: \n" + line);
                
            }
            lzw.compress(str);
            System.out.println("\n¡La compresion ha sido exitosa!");
            System.out.println("El nombre de su nuevo archivo es: " + str.concat(".txt.vic"));
        } catch (FileNotFoundException e) {
            System.out.println("¡Archivo no encontrado!");
        }
    }
}