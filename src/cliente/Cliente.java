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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        
        boolean close = false;
        
        Socket socket = new Socket(IP, PORTA); 
        
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        
        while(!close) {
            
            System.out.println("OneBox\n\n[1] - registrar\n[2] - login\n[3] - Sair");
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
                        registrar(login1,senha1,oos,ois);
                    } else {
                        System.out.println("Senhas erradas!");
                    }
                    
                    break;

                case 2:

                    System.out.println("Digite o login");
                    String login = s.nextLine();
                    System.out.println("Digite a senha");
                    String senha = s.nextLine();
                    
                    if(logar(login, senha, socket, oos, ois)){

                        syncronaizer(socket);
                        
                    } else {}
                    
                    break;

                case 3:
                    close = true;
                    break;
                    
                default:
                    break;
            }
            
            
        }
        
        oos.close();
        ois.close();
        socket.close();
        
        
//                    File fileSelected = chooser.getSelectedFile();
//                    
//                    byte[] bFile = new byte[(int) fileSelected.length()];
//                    fis = new FileInputStream(fileSelected);
//                    fis.read(bFile);
//                    fis.close();
//                    
//                    long kbSize = fileSelected.length() / 1024;
//                    
//                    //request = new Request(fileSelected.getName(),bFile,"/home/ademir/OneBox/",new Date());
//                    
//                    //enviarArquivoServidor();
//                    
//                    Socket socket = new Socket(IP, PORTA); 
//                    
//                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                    oos.writeObject(request);
//                    
//                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                    Request request = (Request) ois.readObject();
//                    System.out.println(request.getNome());
//                    
//                    socket.close();
                    
 
    }
   
    
    private static void registrar(String login, String senha, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
       
        
        Request requestRegister = new Request(login,senha,"registrar");
        
        oos.writeObject(requestRegister);
        
        Reply reply = (Reply) ois.readObject();
        
        System.out.println(reply.getObs());
        
    }
    
    public static boolean logar(String login, String senha, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException{
        
        Request requestRegister = new Request(login,senha,"logar");
        
        oos.writeObject(requestRegister);
        
        Reply reply = (Reply) ois.readObject();
        
        usuario = new Usuario();
        usuario.setId(reply.getUserId());
        usuario.setLogin(reply.getLogin());
        usuario.setSenha(reply.getSenha());
        
        System.out.println(reply.getObs());
        
        if(reply.getObs().equals("Autenticado !")){
            return true;
        } else {
            return false;
        }
    
    }
    
    public static void syncronaizer(Socket socket) throws InterruptedException, IOException, ClassNotFoundException{
        
        System.out.println("...Sincronizando...");
        ListFilesUtil listFilesUtil = new ListFilesUtil();
        List<String> paths = new ArrayList<String>();
        
        while(true){
            
            Request request = new Request();

            request.setLogin(usuario.getLogin());
            request.setSenha(usuario.getSenha());
            request.setOperacao("e/r lista de arquivos");
            request.setPaths(paths);

            Reply reply = sendAndReceiverObjectRequestReply(request, socket, oos, ois);

            for(String key : reply.getPaths()){    
                System.out.println(key);
            }
            
//            paths = listFilesUtil.listFilesAndFilesSubDirectories(paths, dir);
//            
//            for( String key : paths ){
//                System.out.println(key);
//            }
            
            boolean equals = false;
            
//            for(String key : reply.getPaths()){    
//                
//                for(String key2 : paths){
//                    
//                    if(key.equals(key2)){
//                        equals = true;
//                    } else {
//                        equals = false;
//                    }
//                    
//                }
//                
//                if(!equals){
//                    System.out.println(key);
//                }
//                
//            }
            
            
            
            Thread.sleep(7000);
            
        }
    }
    
    public static Reply sendAndReceiverObjectRequestReply(Request request, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException{
        
        oos.writeObject(request);
        
        Reply reply = (Reply) ois.readObject();
        
        System.out.println(reply.getObs());
        
        return reply;
        
    }
    
    
}
