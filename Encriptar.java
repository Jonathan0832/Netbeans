/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

/**
 *
 * @author Vic
 */
import java.io.FileInputStream;
import java.io.*;
import javax.swing.JOptionPane;

public class Encriptar {

    public Encriptar() {
    }

    public static void main(String[] args) {
        /*Encriptar a = new Encriptar();
    	Scanner teclado = new Scanner(System.in);

    	System.out.println("***Escriba su nombre***");
    	String nom = teclado.nextLine();
    	
    	System.out.println("***Escriba su mensaje***");
    	String mensaje= teclado.nextLine();
    			
    	a.crear_arc(mensaje,nom);
    	
    	a.encriptacion_por_sus(nom);
    			
       	a.desencriptacion_por_sus(nom);*/

    }

    public void crear_arc(String texto, String nombre) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nombre + ".txt"));
            PrintWriter pw = new PrintWriter(bw);
            pw.write(texto);
            pw.close();
            bw.close();
        } catch (Exception e) {
        }

        try {
            String archivoentrada = nombre + ".txt";
            FileInputStream entrada = new FileInputStream(archivoentrada);

            byte[] buf = new byte[1024];
            int len;
            while ((len = entrada.read(buf)) > 0) {

            }
            entrada.close();

        } catch (Exception e) {
        }
    }
    //La clave para la sustitucion es: 17615

    public String sustitucion(String cad) {
        char s;
        StringBuilder tmp = new StringBuilder();

        try {
            for (int j = 0; j < cad.length(); j += 5) {
                s = cad.charAt(j);
                s += 1;
                tmp.append(s);

                s = cad.charAt(j + 1);
                s += 7;
                tmp.append(s);

                s = cad.charAt(j + 2);
                s += 6;
                tmp.append(s);

                s = cad.charAt(j + 3);
                s += 1;
                tmp.append(s);

                s = cad.charAt(j + 4);
                s += 5;
                tmp.append(s);

            }
        } catch (Exception e) {

        }

        return tmp.toString();
    }

    public void encriptacion_por_sus(String archivo) {
        String cad;
        String msj = "";
        try {

            FileReader f = new FileReader(archivo + ".txt");
            BufferedReader b = new BufferedReader(f);
            while ((cad = b.readLine()) != null) {
                msj = cad;
            }
            b.close();

            String encriptado = sustitucion(msj);
            //crear_arc(encriptado, "Cifrado_de " + archivo);

            FileWriter fw = new FileWriter(archivo);
            //Escribimos el texto en el fichero
            fw.write(encriptado);
            fw.close();
            //JOptionPane.showMessageDialog(null, "Archivo creado Exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Cifrar Archivo.");
        }

    }

    public String des_sustitucion(String cad) {
        char s;
        StringBuilder tmp = new StringBuilder();

        try {
            for (int j = 0; j < cad.length(); j += 5) {
                s = cad.charAt(j);
                s -= 1;
                tmp.append(s);

                s = cad.charAt(j + 1);
                s -= 7;
                tmp.append(s);

                s = cad.charAt(j + 2);
                s -= 6;
                tmp.append(s);

                s = cad.charAt(j + 3);
                s -= 1;
                tmp.append(s);

                s = cad.charAt(j + 4);
                s -= 5;
                tmp.append(s);

            }
        } catch (Exception e) {

        }

        return tmp.toString();
    }
    //desencriptacion por sustitucion

    public void desencriptacion_por_sus(String archivo) {
        String cad;
        String msj = "";
        try {

            FileReader f = new FileReader(archivo + ".txt");
            BufferedReader b = new BufferedReader(f);
            while ((cad = b.readLine()) != null) {
                msj = cad;
            }
            b.close();

            String encriptado = des_sustitucion(msj);
            //crear_arc(encriptado, "Descifrado_de " + archivo);
            FileWriter fw = new FileWriter(archivo);
            //Escribimos el texto en el fichero
            fw.write(encriptado);
            fw.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Descifrar Archivo.");
        }
    }
}
