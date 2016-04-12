package cliente;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * @author ademir
 */
/**
 * Contains some methods to list files and folders from a directory
 *
 * @author Loiane Groner
 * http://loiane.com (Portuguese)
 * http://loianegroner.com (English)
 */
public class ListFilesUtil {

    public ListFilesUtil() {
    }
    
    /**
     * List all the files and folders from a directory
     * @param directoryName to be listed
     */
    public void listFilesAndFolders(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            System.out.println(file.getName());
        }
    }
    /**
     * List all the files under a directory
     * @param directoryName to be listed
     */
    public void listFiles(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
                System.out.println(file.getName());
            }
        }
    }
    /**
     * List all the folder under a directory
     * @param directoryName to be listed
     */
    public void listFolders(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isDirectory()){
                System.out.println(file.getName());
            }
        }
    }
    /**
     * List all files from a directory and its subdirectories
     * @param directoryName to be listed
     */
    public Map<String, String> listFilesAndFilesSubDirectories(Map<String, String> paths, String directoryName) throws IOException{
        
        for(File key : new File(directoryName).listFiles()){
            if(key.isDirectory() && !key.getName().equals("pasta sem nome")){
                paths.put(key.getAbsolutePath(),"isDirectory");
                listFilesAndFilesSubDirectories(paths, directoryName+"/"+key.getName());
            } else {
                
                if(!key.getName().equals("novo ficheiro")){
                    Date lastModified = new Date(key.lastModified()); 
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                    String formattedDateString = formatter.format(lastModified);

                    paths.put(key.getAbsolutePath(), formattedDateString);
                }
                
            }
        }
        
        return paths;
        
    }
    
}
