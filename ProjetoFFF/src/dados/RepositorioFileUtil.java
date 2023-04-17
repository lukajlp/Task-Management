package dados;

import java.io.*;

public class RepositorioFileUtil implements Serializable {
    
    public static Object lerDoArquivo(String filename) {
        Object instanciaLocal = null;

        File in = new File(filename);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(in);
            ois = new ObjectInputStream(fis);

            System.out.println("falta ler");
            
            // Se alguma exceção ocorrer, um objeto NULL será retornado
            instanciaLocal = ois.readObject();

            System.out.println("leu");
        } catch (Exception e) {
            System.out.println("Não há arquivo com o nome '" + filename + "' para ser processado. Um novo arquivo será criado");
            System.out.println(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }

        return instanciaLocal;
    }

    public static void salvarArquivo(Object instance, String filename) {
        if (instance == null) {
            return;
        }
        File out = new File(filename);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(out);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(instance);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + filename);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
