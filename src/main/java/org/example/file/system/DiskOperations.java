package org.example.file.system;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskOperations {
    private static final String storageDirectoryPath="storage";
    public static void appendDocument(String fileName, JSONObject document) throws IOException {
        String documentPath=storageDirectoryPath+"/"+fileName+".json";
        if(!directoryOrFileExists(documentPath)){
            createAndAdd(fileName,document);
            return;
        }
        RandomAccessFile readerWriter = new RandomAccessFile(storageDirectoryPath+"/"+fileName+".json", "rw");
        FileChannel channel = readerWriter.getChannel();
        int index= (int) channel.size();
        ByteBuffer buff;
        if(index==2){
            buff = ByteBuffer.wrap((document.toJSONString()+"]").getBytes());
        }else{
            buff = ByteBuffer.wrap((","+document.toJSONString()+"]").getBytes());
        }
        channel.write(buff,index-1);
        channel.close();
        readerWriter.close();
    }
    public static void createDirectoryIfNotFound(String directory) throws IOException {
        Files.createDirectories(Paths.get(directory));
    }
    public static void createAndAdd(String fileName,JSONObject document){
        String documentPath=storageDirectoryPath+"/"+fileName+".json";
        try (FileWriter file = new FileWriter(documentPath)) {
            JSONArray jsonArray=new JSONArray();
            jsonArray.add(document);
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean directoryOrFileExists(String pathForFileOrDirectory){
        Path path = Paths.get(pathForFileOrDirectory);
        return Files.exists(path);
    }
    public static JSONArray readJsonArray(String fileName){
        JSONParser jsonParser = new JSONParser();
        String documentPath=storageDirectoryPath+"/"+fileName+".json";
        try (FileReader reader = new FileReader(documentPath))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            System.out.println(jsonArray);
            return jsonArray;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
