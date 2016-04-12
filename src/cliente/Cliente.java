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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        Map<String, String> paths;
        Map<String, String> pathsDownload = null;
        
        while(true){
            
            paths = new HashMap<String, String>();
            pathsDownload = new HashMap<String, String>();
            Request request = new Request();
            
            request.setOperacao("e/r lista de arquivos");
            
            Reply reply = sendAndReceiverObjectRequestReply(request, socket, oos, ois);
            
            System.out.println("--------MEU SERVIDOR-----");

            for(String key : reply.getPaths().keySet()){    
                System.out.println(key+" - "+reply.getPaths().get(key));
            }
            
            System.out.println("-------EU / CLIENTE-----");
            
            paths = listFilesUtil.listFilesAndFilesSubDirectories(paths, dir);
            
            paths = getSplitPaths(paths, dir);
            
            for(String key : paths.keySet()){    
                System.out.println(key+" - "+paths.get(key));
            }
            
            System.out.println("------ Sinchro ----- ");
            
            for(String key : paths.keySet()){    
                
                boolean equals = false;
                
                for(String key2 : reply.getPaths().keySet() ){
                    
                    if(key.equals(key2) && reply.getPaths().get(key).equals( paths.get(key2) ) ){
                        equals = true;
                        System.out.println("Arquivo sincronizado -> "+key2);
                    } else {
                    }
                    
                }
                
                if(!equals){
                    
                    System.out.println("Arquivo para sincronizar -> "+key);
                    
                    Request requestUpload = new Request();
                    
                    File file = new File(dir+key);
                    
                    requestUpload.setNome(key);
                    requestUpload.setOperacao("upload");
                    
                    if(file.isDirectory()) { 
                        requestUpload.setDirectory(true);
                        
                    } else { 
                        byte[] bFile = fileToByteArray(file);
                        requestUpload.setDirectory(false); 
                        requestUpload.setConteudo( bFile );
                        requestUpload.setLastModified(file.lastModified());
                    }
                    
                    sendAndReceiverObjectRequestReply(requestUpload, socket, oos, ois);
                    
                    System.out.println("Arquivo "+key+" sincronizado");
                    
                    break;
                }
                
            }
            
            request.setOperacao("e/r lista de arquivos");
            
            Reply replyGetFiles = sendAndReceiverObjectRequestReply(request, socket, oos, ois);
            
            pathsDownload = listFilesUtil.listFilesAndFilesSubDirectories(pathsDownload, dir);
            
            pathsDownload = getSplitPaths(pathsDownload, dir);
            
            for(String key : replyGetFiles.getPaths().keySet()){    

                boolean equals = false; 

                for( String key2 : paths.keySet() ){

                    System.out.println(key + "-" + key2);
                    if(key.equals(key2) && replyGetFiles.getPaths().get(key).equals( paths.get(key2) ) ){
                        equals = true;

                    } else {}

                }  

                if(!equals){

                    System.out.println(" Arquivo que vai baixar -> "+key);
                    System.out.println(" Value -> "+replyGetFiles.getPaths().get(key));

                    Request requestDownload = new Request();

                    requestDownload.setNome(key);
                    requestDownload.setOperacao("download");

                    Reply replyDownload = sendAndReceiverObjectRequestReply(requestDownload, socket, oos, ois);

                    createFilesFromReply(replyDownload);

                    System.out.println("Arquivo "+key+" baixado do servidor");

                    break;
                    
                }
                
                
               
            }
            
            Thread.sleep(3000);
            
        }
    }
    
    public static Reply sendAndReceiverObjectRequestReply(Request request, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException{
        
        oos.writeObject(request);
        
        Reply reply = (Reply) ois.readObject();
        
        System.out.println(reply.getObs());
        
        return reply;
        
    }
    
    public static Map<String,String> getSplitPaths(Map<String,String> paths, String split){
        
        Map<String, String> tempPaths = new HashMap<String, String>();
        
        for(Map.Entry<String, String> entry : paths.entrySet()) {
            
            String fullPath = entry.getKey();
            String array[] = fullPath.split(split);
            tempPaths.put(array[1], entry.getValue());

        }
        
        return tempPaths;
    }
    
    public static byte[] fileToByteArray(File file) throws FileNotFoundException, IOException{
        FileInputStream fis;
        byte[] bFile = new byte[(int) file.length()];
        fis = new FileInputStream(file);
        fis.read(bFile);
        fis.close();
        return bFile;
    }
    
    public static void createFilesFromReply(Reply reply) throws FileNotFoundException, IOException{
        System.out.println("teste1"+reply.getNome());
        if(reply.getNome() != null){
           if(reply.isDirectory() && !reply.getNome().equals("pasta sem nome")){
                new File(dir+reply.getNome()).mkdirs();
            } else {
                System.out.println("Arquivo sendo criado");
                FileOutputStream fos = new FileOutputStream(dir+reply.getNome());
                File file = new File(dir+reply.getNome());
                file.setLastModified(reply.getLastModified());
                fos.write(reply.getConteudo());
                fos.close();
            } 
        }
        
    }
    
    
}
