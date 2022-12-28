package com.rbtsb.lms.util;

import com.rbtsb.lms.constant.GlobalConstant;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

public class FileUtil {
    public static byte[] compressImage(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while(!deflater.finished()){
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        try{
            outputStream.close();
        }
        catch(Exception ex){

        }
        return outputStream.toByteArray();

    }

    public static byte[] decompressImage(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        try{
            while(!inflater.finished()){
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0 , count);
            }
        }catch(Exception ex){

        }

        return outputStream.toByteArray();


    }

    public static String compressFile(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());

        String fileName = getFileName(file.getOriginalFilename());

        String filePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + fileExtension;
        String compFilePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + "Compressed" + fileExtension;



        try{
            FileInputStream fileRead = new FileInputStream(filePath);
            FileOutputStream fileWrite = new FileOutputStream(compFilePath);

            DeflaterOutputStream comp = new DeflaterOutputStream(fileWrite);

            int data;

            while((data=fileRead.read())!=-1){
                comp.write(data);
            }

            fileRead.close();
            comp.close();

            long originalSize = new File(filePath).length();
            long compSize = new File(compFilePath).length();

            if(originalSize > compSize){
                return "compress successfully.";
            }
            else{
                File fileToDelete = new File(compFilePath);
                fileToDelete.delete();
                return "Can't compress file. The uploaded file is not saved.";
            }

        }
        catch(Exception ex){
            return "compress failed";
        }
        //Path fileNameAndPAth = Paths.get(GlobalConstant.ATTACHMENT_PATH, file.getOriginalFilename())
        //Files.write(fileNameAndPAth, file.getBytes());
    }

    public static String getFileExtension(String fileName){
        int getExtensionIndex = fileName.lastIndexOf('.');
        if(getExtensionIndex > 0){
            String fileExtension = fileName.substring(getExtensionIndex);
            return fileExtension;
        }
        else{
            return null;
        }

    }

    public static String getFileName(String file){
        int getExtensionIndex = file.lastIndexOf('.');
        if(getExtensionIndex > 0){
            String fileName = file.substring(0, getExtensionIndex);
            return fileName;
        }
        else{
            return null;
        }

    }

    public static String decompressFile(String file){
        String fileExtension = getFileExtension(file);

        String fileName = getFileName(file);

        String filePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + fileExtension;
        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + "Decompressed" + fileExtension;

        try{
            FileInputStream fileRead = new FileInputStream(filePath);
            FileOutputStream fileWrite = new FileOutputStream(decompFilePath);

            InflaterOutputStream decomp = new InflaterOutputStream(fileWrite);

            int data;

            while((data=fileRead.read())!=-1){
                decomp.write(data);
            }

            fileRead.close();
            decomp.close();

            long originalSize = new File(filePath).length();
            long decompSize = new File(decompFilePath).length();

            if(originalSize < decompSize){
                return "decompress successfully.";
            }
            else{
                File fileToDelete = new File(decompFilePath);
                fileToDelete.delete();
                return "Can't decompress file. The uploaded file is not saved.";
            }

        } catch (FileNotFoundException e) {
            return "decompress failed due to file not exists ";
        } catch (IOException e) {
            return "decompress failed due to file read error";
        }
    }
}
