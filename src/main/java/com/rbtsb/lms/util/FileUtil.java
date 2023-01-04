package com.rbtsb.lms.util;

import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.error.ErrorAction;
import jdk.nashorn.internal.objects.Global;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.*;
import java.util.List;

public class FileUtil {
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        try {
            outputStream.close();
        } catch (Exception ex) {

        }
        return outputStream.toByteArray();

    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
        } catch (Exception ex) {

        }

        return outputStream.toByteArray();


    }

    @Deprecated
    public static byte[] compressFileWithFileName(String originalFileName) throws IOException {
        String fileExtension = getFileExtension(originalFileName);

        String fileName = getFileName(originalFileName);

        String filePath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName + fileExtension;
        String compFilePath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName + "_Compressed" + fileExtension;

        try {
            String compDirectory = getFileName(compFilePath) + "_" +
                    DateTimeUtil.yyyyMMddhhmmssDateTime(new Date()) +
                    fileExtension;

            FileInputStream fileRead = new FileInputStream(filePath);
            FileOutputStream fileWrite = new FileOutputStream(compDirectory);
            ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();

            DeflaterOutputStream comp = new DeflaterOutputStream(fileWrite);
            DeflaterOutputStream compDB = new DeflaterOutputStream(dbWrite);

            int data;

            while ((data = fileRead.read()) != -1) {
                comp.write(data);
                compDB.write(data);
            }

            fileRead.close();
            comp.close();
            compDB.close();

            long originalSize = new File(filePath).length();
            long compSize = new File(compDirectory).length();

            if (originalSize > compSize) {
                return dbWrite.toByteArray();
            } else {
                File fileToDelete = new File(compDirectory);
                fileToDelete.delete();
                //return "Can't compress file. The uploaded file is not saved.";
                return new byte[0];
            }

        } catch (Exception ex) {
            return new byte[0];
            //return "compress failed";
        }
        //Path fileNameAndPAth = Paths.get(GlobalConstant.ATTACHMENT_PATH, file.getOriginalFilename())
        //Files.write(fileNameAndPAth, file.getBytes());
    }

    public static byte[] compressFile(byte[] fileData, String sourcePath) throws IOException {

        try {
            String fileName = getFileName(sourcePath);
            String fileExtension = getFileExtension(sourcePath);
            String compDirectory = sourcePath;

            ByteArrayInputStream byteRead = new ByteArrayInputStream(fileData);

            FileOutputStream fileWrite = new FileOutputStream(compDirectory);
            ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();

            DeflaterOutputStream compFile = new DeflaterOutputStream(fileWrite);
            DeflaterOutputStream compDB = new DeflaterOutputStream(dbWrite);

            int data;

            while ((data = byteRead.read()) != -1) {
                compFile.write(data);
                compDB.write(data);
            }

            byteRead.close();
            compFile.close();
            compDB.close();

            long originalSize = fileData.length;
            long compSize = new File(compDirectory).length();

            if (originalSize > compSize) {
                String file = readFile(compDirectory);
                return dbWrite.toByteArray();
            } else {
                File fileToDelete = new File(compDirectory);
                fileToDelete.delete();
                //return "Can't compress file. The uploaded file is not saved.";
                return new byte[0];
            }

        } catch (Exception ex) {
            return new byte[0];
            //return "compress failed";
        }
    }

    @Deprecated
    public static byte[] compressFileNoSaveInFolder(byte[] fileData) {
        try {
            String compDirectory = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" +
                    DateTimeUtil.yyyyMMddhhmmssDateTime(new Date()) + "_Compressed" + ".txt";

            ByteArrayInputStream byteRead = new ByteArrayInputStream(fileData);

            //FileOutputStream fileWrite = new FileOutputStream(compDirectory);
            ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();

            //DeflaterOutputStream compFile = new DeflaterOutputStream(fileWrite);
            DeflaterOutputStream compDB = new DeflaterOutputStream(dbWrite);

            int data;

            while ((data = byteRead.read()) != -1) {
                //compFile.write(data);
                compDB.write(data);
            }

            byteRead.close();
            //compFile.close();
            compDB.close();

            long originalSize = fileData.length;
            long compSize = new File(compDirectory).length();

            if (originalSize > compSize) {
                return dbWrite.toByteArray();
            } else {
                File fileToDelete = new File(compDirectory);
                fileToDelete.delete();
                //return "Can't compress file. The uploaded file is not saved.";
                return new byte[0];
            }

        } catch (Exception ex) {
            return new byte[0];
            //return "compress failed";
        }
    }

    @Deprecated
    public static byte[] decompressFileFromFolder(String file, byte[] fileData) {
        String fileExtension = getFileExtension(file);

        String fileName = getFileName(file);

        String filePath = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" + fileName + fileExtension;
        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" + fileName + "Decompressed" + fileExtension;

        ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();

        try {
            FileInputStream fileRead = new FileInputStream(filePath);
            FileOutputStream fileWrite = new FileOutputStream(decompFilePath);
            InflaterOutputStream decomp = new InflaterOutputStream(fileWrite);

            int data;

            while ((data = fileRead.read()) != -1) {
                decomp.write(data);
            }

            fileRead.close();
            decomp.close();

            long originalSize = new File(filePath).length();
            long decompSize = new File(decompFilePath).length();

            if (originalSize < decompSize) {
                //return "decompress successfully.";
                return fileData;
            } else {
                File fileToDelete = new File(decompFilePath);
                fileToDelete.delete();
                return new byte[0];
                //return "Can't decompress file. The uploaded file is not saved.";
            }

        } catch (FileNotFoundException e) {
            //return "decompress failed due to file not exists";
        } catch (IOException e) {
            //return "decompress failed due to file read error";
        }
        return new byte[0];
    }

    public static byte[] decompressFileFromDB(String file, byte[] fileData) {
        String fileExtension = getFileExtension(file);
        String fileName = getFileName(file);
        String originalPath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName + fileExtension;
        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" + fileName + fileExtension;
        //File fileDirectory = new File(decompFilePath);

        ByteArrayInputStream dbRead = new ByteArrayInputStream(fileData);
        ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();
        //InflaterOutputStream decompByteArray = new InflaterOutputStream(dbWrite);

        try {
            String decompDirectory = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" +
                    getFileName(decompFilePath) + "_" +
                    DateTimeUtil.yyyyMMddhhmmssDateTime(new Date()) +
                    fileExtension;
            FileOutputStream fileWrite = new FileOutputStream(decompDirectory);
            InflaterOutputStream decompFile = new InflaterOutputStream(fileWrite);
            //check for file exist: if(fileDirectory.exists() && !fileDirectory.isDirectory())

            //byte[] buffer = new byte[1024];
            //byte[] bufferFile = new byte[1024];
            int data;
            while ((data = dbRead.read()) != -1) {
                //decompByteArray.write(data);
                //decompFile.write(data);
                fileWrite.write(data);
                dbWrite.write(data);
            }
//            for (int length; (length = dbRead.read(buffer)) != -1; ) {
//                decompByteArray.write(buffer, 0, length);
//            }

            dbRead.close();
            decompFile.close();
            //decompByteArray.close();
            return dbWrite.toByteArray();
//            long originalSize = new File(originalPath).length();
//            long decompFileSize = new File(decompDirectory).length();
//
//            if(originalSize < decompFileSize){
//                return dbWrite.toByteArray();
//            }
//            else{
//                File fileToDelete = new File(decompDirectory);
//                fileToDelete.delete();
//                return "Can't decompress file. The uploaded file is not saved.".getBytes(StandardCharsets.UTF_8);
//            }


        } catch (FileNotFoundException e) {
            return ("decompress failed due to file not exists." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ("decompress failed due to file read error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (ParseException e) {
            return ("decompress failed due to date time error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return ("decompress failed due to internal error. " + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } finally {

        }
        //return new byte[0];
        //return "File Name: " + fileDirectory + " decompress successfully.";
    }

    public static byte[] decompressZipFileFromDB(String fileName, byte[] fileData) {
        String fileExtension = getFileExtension(fileName);
        String fileNameWithoutExtension = getFileName(fileName);
        String originalPath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName;
        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" + fileName;
        //File fileDirectory = new File(decompFilePath);

        //Create directory
        File downloadPath = new File(GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\");
        if (!(downloadPath.isDirectory() && downloadPath.exists())) {
            downloadPath.mkdir(); //create directory
        }

        ByteArrayInputStream dbRead = new ByteArrayInputStream(fileData);
        ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();
        //InflaterOutputStream decompByteArray = new InflaterOutputStream(dbWrite);
        List<String> fileNames = new ArrayList<>();
        byte[] buffer = new byte[1024];

        try {
            String decompDirectory = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" +
                    getFileName(decompFilePath) + "_" +
                    DateTimeUtil.yyyyMMddhhmmssDateTime(new Date()) +
                    fileExtension;

            ByteArrayInputStream bais = new ByteArrayInputStream(fileData);
            ZipInputStream zis = new ZipInputStream(bais);
            ZipEntry ze = zis.getNextEntry();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileOutputStream fos = new FileOutputStream(decompDirectory);
            //ZipOutputStream zos = new ZipOutputStream(baos);

            while (ze != null) {
                String zipFileName = ze.getName();
                File newFile = new File(decompDirectory);
                System.out.println("Unzipping and Download to " + newFile.getAbsolutePath());
                fileNames.add(newFile.getAbsolutePath());

                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();

                int len;

                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                    fos.write(buffer, 0, len);
                }
                baos.close();

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();


            return baos.toByteArray();


        } catch (FileNotFoundException e) {
            return ("decompress failed due to file not exists." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ("decompress failed due to file read error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (ParseException e) {
            return ("decompress failed due to date time error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return ("decompress failed due to internal error. " + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } finally {

        }
        //return new byte[0];
        //return "File Name: " + fileDirectory + " decompress successfully.";
    }

    public static byte[] decompressZipFileFromDBWithoutCreatingFile(byte[] fileData){
//        String fileExtension = getFileExtension(fileName);
//        String fileNameWithoutExtension = getFileName(fileName);
//        String originalPath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName;
//        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" + fileName;
//        //File fileDirectory = new File(decompFilePath);

        //Create directory
//        File downloadPath = new File(GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\");
//        if (!(downloadPath.isDirectory() && downloadPath.exists())) {
//            downloadPath.mkdir(); //create directory
//        }

        ByteArrayInputStream dbRead = new ByteArrayInputStream(fileData);
        ByteArrayOutputStream dbWrite = new ByteArrayOutputStream();
        //InflaterOutputStream decompByteArray = new InflaterOutputStream(dbWrite);
        List<String> fileNames = new ArrayList<>();
        byte[] buffer = new byte[1024];

        try {
//            String decompDirectory = GlobalConstant.ATTACHMENT_PATH + "\\downloadBackup\\" +
//                    getFileName(decompFilePath) + "_" +
//                    DateTimeUtil.yyyyMMddhhmmssDateTime(new Date()) +
//                    fileExtension;

            ByteArrayInputStream bais = new ByteArrayInputStream(fileData);
            ZipInputStream zis = new ZipInputStream(bais);
            ZipEntry ze = zis.getNextEntry();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            FileOutputStream fos = new FileOutputStream(decompDirectory);
            //ZipOutputStream zos = new ZipOutputStream(baos);

            while (ze != null) {
                String zipFileName = ze.getName();
//                File newFile = new File(decompDirectory);
//                System.out.println("Unzipping and Download to " + newFile.getAbsolutePath());
//                fileNames.add(newFile.getAbsolutePath());

                //create directories for sub directories in zip
//                new File(newFile.getParent()).mkdirs();

                int len;

                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
//                    fos.write(buffer, 0, len);
                }
                baos.close();

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();


            return baos.toByteArray();


        } catch (FileNotFoundException e) {
            return ("decompress failed due to file not exists." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ("decompress failed due to file read error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        }
//        catch (ParseException e) {
//            return ("decompress failed due to date time error." + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
//        }
        catch (Exception ex) {
            return ("decompress failed due to internal error. " + ErrorAction.ERROR_ACTION).getBytes(StandardCharsets.UTF_8);
        } finally {

        }
    }

    @Deprecated
    public static void zipFiles(String... filePaths) {
        try {
            File firstFile = new File(filePaths[0]);
            String zipFileName = firstFile.getName().concat(".zip");

            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String aFile : filePaths) {
                zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(aFile));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }

            zos.close();

        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }

    public static List<String> unzipFiles(String zipFilePath, String destDir) {
        //destDir = createFolder(destDir);
        File dir = new File(destDir);
        List<String> fileNames = new ArrayList<>();

        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;

        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                fileNames.add(newFile.getAbsolutePath());

                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();

                ByteArrayOutputStream fos = new ByteArrayOutputStream();
                int len;

                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                FileUtil.compressFile(fos.toByteArray(), newFile.getAbsolutePath());

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();

            return fileNames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Deprecated
    public static String convertByteToString(byte[] data) {
        StringBuilder content = new StringBuilder();
        for (byte x : data) {
            content.append("" + x);
        }
        String finalContent = new String(content.toString().getBytes(StandardCharsets.US_ASCII));
        return finalContent;
    }

    public static String readFile(String directory) throws IOException {
        FileInputStream readFile = new FileInputStream(directory);
        byte[] buffers = new byte[10];
        StringBuilder sb = new StringBuilder();
        while (readFile.read(buffers) != -1) {
            sb.append(new String(buffers));
            buffers = new byte[10];
        }
        readFile.close();
        return sb.toString();
    }

    public static byte[] readByteAndReturnStringBytes(byte[] readByte) {

        String outputStr = "";
        try{
            outputStr = new String(readByte, StandardCharsets.UTF_8);
//            ByteArrayInputStream readByteArray = new ByteArrayInputStream(readByte);
//            ByteArrayOutputStream writeByteArray = new ByteArrayOutputStream();
//
//            int len;
//            while ((len = readByteArray.read()) != -1) {
//                writeByteArray.write(len);
//            }
        }
//        catch(FileNotFoundException fnfEx){
//            outputStr = fnfEx.toString();
//        }
//        catch(IOException ioEx){
//            outputStr = ioEx.toString();
//        }
        catch(Exception ex){
            outputStr = ex.toString();
        }

        return outputStr.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] readZipFileAndReturnBytes(String directory) throws IOException {
        FileInputStream readFile = new FileInputStream(directory);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
//        DeflaterOutputStream compFile = new DeflaterOutputStream(byteOutput);

        int len;
        while ((len = readFile.read()) != -1) {
            byteOutput.write(len);
        }

        readFile.close();
        byteOutput.close();

        return byteOutput.toByteArray();
    }

    // TODO: Fix save image into folder in Insert,
    public static void writeImage(String fileName, byte[] data) throws IOException {
        String fileNameWithoutExt = getFileName(fileName);
        String ext = getFileExtension(fileName);
        String directory = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + fileName;

        // create the object of ByteArrayInputStream class
        // and initialized it with the byte array.
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(data);

        // read image from byte array
        BufferedImage newImage = ImageIO.read(inStreambj);

        // write output image
        ImageIO.write(newImage, ext, new File(directory));
        //System.out.println("Image generated from the byte array.");
    }

    public static String getFileExtension(String fileName) {
        int getExtensionIndex = fileName.lastIndexOf('.');
        if (getExtensionIndex > 0) {
            String fileExtension = fileName.substring(getExtensionIndex);
            return fileExtension;
        } else {
            return null;
        }

    }

    public static String getFileName(String file) {
        int getExtensionIndex = file.lastIndexOf('.');
        int getPathSlashIndex = file.lastIndexOf('\\');
        String fileName = "";
        if (getExtensionIndex > 0) {
            if (getPathSlashIndex > 0) {
                fileName = file.substring(getPathSlashIndex + 1, getExtensionIndex);
            } else {
                fileName = file.substring(0, getExtensionIndex);
            }
            return fileName;
        } else {
            return null;
        }

    }

    public static String getDirectory(String path) {
        int lastSlashIndex = path.lastIndexOf('\\');
        int lastExtIndex = path.lastIndexOf('.');
        int fileNameIndex = path.lastIndexOf(getFileName(path)); //put fileName

        if (fileNameIndex > 0) {
            String subPath = path.substring(0, fileNameIndex - 1);
            return subPath;
        } else {
            return path;
        }
    }

    @Deprecated
    public static String createFolder(String targetPath) {
        String newDirectory = getDirectory(targetPath) + "\\" + getFileName(targetPath);
        File f = new File(newDirectory);

        // check if the directory can be created
        // using the specified path name
        if (f.mkdir() == true) {
            return newDirectory;
        } else {
            return "Directory cannot be created";
        }
    }

    //    public static byte[] decompressFileWithUnzip(String file, byte[] fileData){
//        String fileExtension = getFileExtension(file);
//
//        String fileName = getFileName(file);
//
//        String filePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + fileExtension;
//        String compFilePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + "Compressed" + fileExtension;
//        String decompFilePath = GlobalConstant.ATTACHMENT_PATH + "\\" + fileName + "Decompressed" + fileExtension;
//        Path tgt = Paths.get(decompFilePath);
//
//        try (OutputStream out = Files.newOutputStream(tgt)) {
//            FileInputStream fileRead = new FileInputStream(filePath);
//            ZipOutputStream zip = new ZipOutputStream(out);
//            zip.putNextEntry(new ZipEntry(compFilePath));
//
//            int data;
//
//            while((data=fileRead.read())!=-1){
//                zip.write(fileRead.read());
//            }
//
//            // write the other files here
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new byte[0];
//    }
}
