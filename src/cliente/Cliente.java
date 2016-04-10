/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import Reply.Reply;
import Request.Request;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author ademir
 */
public class Cliente {

    public static final String IP = "127.0.0.1";
    public static final int PORTA = 10300;
    public static final String dir = "/home/ademir/OneBoxCliente/";
    
    private static Request request;
    private static final long serialVersionUID = 1L;
    
    private long tamanhoPermitidoKB = 5120;
    
    private static Usuario usuario;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        
        System.out.println("OneBox\n\n [1] - registrar\n[2] - login");
        Scanner s = new Scanner(System.in);
        
        int i = s.nextInt();
        s.nextLine();
        
        switch(i){
            
            case 1:
                
                System.out.print("Digite o login:");
                String login1 = s.nextLine();
                System.out.print("Digite a senha:");
                String senha1 = s.nextLine();
                System.out.print("Confirme a senha:");
                String senha2 = s.nextLine();
                
                if(senha1.equals(senha2)){
                    registrar(login1,senha1);
                } else {
                    System.out.println("Senhas erradas!");
                }
                
                break;
                
            case 2:
                
                System.out.println("Digite o login");
                String login = s.nextLine();
                System.out.println("Digite a senha");
                String senha = s.nextLine();
                
                if(logar(login, senha)){
                    syncronaizer();
                } else {
                    
                }
                
                break;
            default:
                break;
        }
        
        
            FileInputStream fis;
            try {
            
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setDialogTitle("Escolha o arquivo");
                
                int returnVal = chooser.showOpenDialog(null);
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    
                    File fileSelected = chooser.getSelectedFile();
                    
                    byte[] bFile = new byte[(int) fileSelected.length()];
                    fis = new FileInputStream(fileSelected);
                    fis.read(bFile);
                    fis.close();
                    
                    long kbSize = fileSelected.length() / 1024;
                    
                    //request = new Request(fileSelected.getName(),bFile,"/home/ademir/OneBox/",new Date());
                    
                    //enviarArquivoServidor();
                    
                    Socket socket = new Socket(IP, PORTA); 
                    
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(request);
                    
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Request request = (Request) ois.readObject();
                    System.out.println(request.getNome());
                    
                    socket.close();
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
 
    }
   
    
    private static void registrar(String login, String senha) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(IP, PORTA); 
        
        Request requestRegister = new Request(login,senha,"registrar");
        
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(requestRegister);
        
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Reply reply = (Reply) ois.readObject();
        System.out.println(reply.getObs());

        socket.close();
    }
    
    public static boolean logar(String login, String senha) throws IOException, ClassNotFoundException{
        
        Socket socket = new Socket(IP, PORTA); 
        
        Request requestRegister = new Request(login,senha,"logar");
        
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(requestRegister);
        
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Reply reply = (Reply) ois.readObject();
        System.out.println(reply.getObs());
        
        usuario = new Usuario();
        usuario.setId(reply.getUserId());
        usuario.setLogin(reply.getLogin());
        usuario.setSenha(reply.getSenha());
        
        socket.close();
        
        if(reply.getObs().equals("Autenticado !")){
            return true;
        } else {
            return false;
        }   
    
    }
    
    public static void syncronaizer(){
        
        while(true){
            
        }
        
    }
    
    
}
