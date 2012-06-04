/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package P2_640.Archivos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 *
 * @author CarlosArmando
 */
public class JBank {
    RandomAccessFile rCuentas;
    RandomAccessFile rLog;
    
    public JBank(){
        try{
            rCuentas = new RandomAccessFile("cuentas.bac", "rw");
            rLog = new RandomAccessFile("log.bac", "rw");
            initCodigos();
        }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void initCodigos() throws IOException {
        RandomAccessFile rCod = new RandomAccessFile("codigos.bac","rw");
        
        if( rCod.length() == 0 ){
            rCod.writeInt(1);
        }
        
        rCod.close();
        
    }
    
    private int getCodigo( )throws IOException{
        RandomAccessFile rCod = new RandomAccessFile("codigos.bac","rw");
        
        int cod = rCod.readInt(); //leo el codigo disponible
        rCod.seek(0); //me regreso de nuevo
        rCod.writeInt(cod + 1);//actualizo el archivo
        rCod.close();
        
        return cod;
    }
    
    public void addCuenta( String cliente )throws IOException{
        //me voy al final
        rCuentas.seek( rCuentas.length() );
        //escribo el codigo
        rCuentas.writeInt( getCodigo() );
        //nombre
        rCuentas.writeUTF(cliente);
        //saldo
        rCuentas.writeDouble(500);
        //fecha ultima
        rCuentas.writeLong( new Date().getTime() );
        //activo
        rCuentas.writeBoolean(true);
    }
    
    public void listar(boolean activas)throws IOException{
        //desde el inicio
        rCuentas.seek(0);
        
        while( rCuentas.getFilePointer() < rCuentas.length() ){
            int cod = rCuentas.readInt();
            String client = rCuentas.readUTF();
            double sal = rCuentas.readDouble();
            long fechault = rCuentas.readLong();
            
            if( rCuentas.readBoolean() == activas ){
                System.out.println(cod + "-" + client + " Lps " + sal +
                        " ultima vez usada: " + new Date(fechault) );
            }
        }
    }
    
    public boolean buscar(int codcuenta)throws IOException{
        //desde el inicio
        rCuentas.seek(0);
        while( rCuentas.getFilePointer() < rCuentas.length() ){
            //guardo la pos antes de comenzar a leer el registro
            long pos = rCuentas.getFilePointer();
            
            int cod = rCuentas.readInt();
            rCuentas.readUTF();
            //me salto el saldo + fecha + activo = 17 bytes
            rCuentas.seek( rCuentas.getFilePointer() + 17);
            
            if( cod == codcuenta ){
                rCuentas.seek(pos);
                return true;
            }
            
        }
        return false;
    }
    
    
}