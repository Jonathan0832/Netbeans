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

public class LZWDescompresion {

    // Definir un HashMap y otras variables 
    public HashMap<Integer, String> dictionary = new HashMap<>();
    public String[] Array_char;
    public int dictSize = 256;
    public int currword;
    public int priorword;
    public byte[] buffer = new byte[3];
    public boolean onleft = true;

    
    public void LZW_Decompress(String input) throws IOException {
        // DictSize construye hasta 4k, Array_Char tiene estos valores
        Array_char = new String[4096];

        for (int i = 0; i < 256; i++) {
            dictionary.put(i, Character.toString((char) i));
            Array_char[i] = Character.toString((char) i);
        }

        // Lee entradas como archivo no comprimido
        RandomAccessFile in = new RandomAccessFile(input, "r");
        String[] archivo = input.split("\\.");
        RandomAccessFile out = new RandomAccessFile(archivo[0] + ".3jvc", "rw"); //Como el de compresión, utilizamos la extensión que queramos

        try {
            // Toma la primera palabra en codigo y saca su char correspondiente
            buffer[0] = in.readByte();
            buffer[1] = in.readByte();
            priorword = getvalue(buffer[0], buffer[1], onleft);
            onleft = !onleft;
            out.writeBytes(Array_char[priorword]);

            // Lee cada 3 bytes y genera los caracteres correspondientes
            while (true) {
                if (onleft) {
                    buffer[0] = in.readByte();
                    buffer[1] = in.readByte();
                    currword = getvalue(buffer[0], buffer[1], onleft);
                } else {
                    buffer[2] = in.readByte();
                    currword = getvalue(buffer[1], buffer[2], onleft);
                }
                onleft = !onleft;

                if (currword >= dictSize) {
                    if (dictSize < 4096) {
                        Array_char[dictSize] = Array_char[priorword]
                                + Array_char[priorword].charAt(0);
                    }
                    dictSize++;
                    out.writeBytes(Array_char[priorword]
                            + Array_char[priorword].charAt(0));
                } else {
                    if (dictSize < 4096) {
                        Array_char[dictSize] = Array_char[priorword]
                                + Array_char[currword].charAt(0);
                    }
                    dictSize++;
                    out.writeBytes(Array_char[currword]);
                }
                priorword = currword;
            }
        } catch (EOFException e) {
            in.close();
            out.close();
        }
    }

   
    public int getvalue(byte b1, byte b2, boolean onleft) {
        String temp1 = Integer.toBinaryString(b1);
        String temp2 = Integer.toBinaryString(b2);

        while (temp1.length() < 8) {
            temp1 = "0" + temp1;
        }
        if (temp1.length() == 32) {
            temp1 = temp1.substring(24, 32);
        }
        while (temp2.length() < 8) {
            temp2 = "0" + temp2;
        }
        if (temp2.length() == 32) {
            temp2 = temp2.substring(24, 32);
        }

        if (onleft) {
            return Integer.parseInt(temp1 + temp2.substring(0, 4), 2);
        } else {
            return Integer.parseInt(temp1.substring(4, 8) + temp2, 2);
        }
    }

    //Main
    public static void main(String[] args) throws IOException {
        try {
            LZWDescompresion lzw = new LZWDescompresion();

            Scanner input = new Scanner(System.in);

            System.out.println("Ingrese el nombre de su (nombredearchivo.txt.vic) archivo...");//Nombre del archivo comprimido

            String str = input.nextLine();

            File file = new File(str);

            Scanner fileScanner = new Scanner(file);

            String line = "";

            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();
                System.out.println("Contenido de su archivo estan siendo descomprimidos:\n"
                        + line);
            }
            lzw.LZW_Decompress(str);
            System.out.println("¡Descompresion de su archivo esta completo!");
            System.out.println("El nombre de su archivo es: "
                    + str.replace(".vic", ""));
        } catch (FileNotFoundException e) {
            System.out.println("¡Archivo no encontrado!");
        }
    }
}