package com.example.myapplication.utils;


import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileUtils {

    public static FileOutputStream openOutputStream(@NonNull File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        return new FileOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(@NonNull String filePath) throws IOException {
        File file = new File(filePath);
        return openOutputStream(file);
    }
    public static FileChannel openFileOutputRChannel(@NonNull File file) throws IOException {
        return openOutputRStream(file).getChannel();
    }
    public static FileOutputStream openOutputRStream(@NonNull File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        return new FileOutputStream(file, false);
    }
    public static FileChannel openFileOutputChannel(@NonNull File file) throws IOException {
        return openOutputStream(file).getChannel();
    }

    public static FileChannel openFileOutputChannel(@NonNull String filePath) throws IOException {
        return openOutputStream(filePath).getChannel();
    }

    public static FileInputStream openFileInputStream(@NonNull File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static FileChannel openFileInputChannel(@NonNull File file) throws FileNotFoundException {
        return openFileInputStream(file).getChannel();
    }

    /**
     * Transfere os bytes do arquivo de origem para o de destino.
     * @see FileChannel#transferFrom(ReadableByteChannel, long, long)
     *
     * @param src  Arquivo de origem.
     * @param dest Arquivo de destino.
     * @return Retorna true se e apenas se a quantidade de bytes copiados para o canal de destino
     * for igual ou maior que a quantidade de bytes do canal de origem.
     * @throws IOException caseo não exista um arquivo no local especificado.
     */
    public static boolean transferFrom(@NonNull File src, @NonNull File dest, boolean re) throws IOException {
        if (src.exists()) {
            try (FileChannel srcChannel = FileUtils.openFileInputChannel(src);
                 FileChannel destChannel = FileUtils.openFileOutputChannel(dest)
            ) {
                //ring de =src.getParent();
                if(!re){
                    File[] listFiles = dest.getParentFile().listFiles(new FileFilter() {
                        public boolean accept(File pathname) {
                            return pathname.getName().startsWith("servix_db-"); // apenas arquivos que começam com a letra "a"
                        }
                    });

                    for(File f : listFiles) {
                        f.delete();
                    }
                }
                long bytesTransfered = destChannel.transferFrom(srcChannel, 0, srcChannel.size());
                return bytesTransfered >= srcChannel.size();
            }
        }else{
            throw new FileNotFoundException("Não existe um arquivo de banco de dados no local especificado.");
        }
    }
}
