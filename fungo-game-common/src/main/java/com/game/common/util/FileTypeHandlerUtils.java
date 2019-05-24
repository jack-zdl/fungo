package com.game.common.util;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * <p>
 *   文件类型处理工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class FileTypeHandlerUtils {

    /**
     * 常见文件类型
     */
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    /**
     * 允许上传的图片文件格式
     */
    private static final String ALLOW_IMG_SUFFIX = "jpg,png,gif,bmp,jpeg,tif,webp";

    /**
     * 文件种类：
     * 1 - 图片
     */
    public static final Integer FILE_TYPE_IMG = 1;


    //初始化文件类型信息
    static {
        getAllFileType();
    }

    /**
     * 常见的文件类型与文件头信息的对应关系
     */
    private static void getAllFileType() {
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE_MAP.put("mpg", "000001BA");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
        FILE_TYPE_MAP.put("webp", "52494646");//webp 52 49 46 46
    }


    /**
     * 获取图片文件实际类型,若不是图片则返回null
     * @param f 图片文件对象
     * @return
     */
    public final static String getImageFileType(File f) {
        if (isImage(f)) {
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(f);
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName().toLowerCase();
            } catch (IOException e) {
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    /**
     * 获取图片文件实际类型,若不是图片则返回null
     * @param filePath 图片文件地址
     * @return
     */
    public final static String getImageFileType(String filePath) {

        if (null != filePath && filePath.lastIndexOf(".") > 0) {
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
            return suffix;
        }
        return null;
    }


    /**
     * 验证是否符合上传要求
     * @param fileSuffix 文件的后缀名
     * @param type 后缀类型 1 图片文件
     * @return true 符合，false 不符合
     */
    public static final boolean isAllow(String fileSuffix, Integer type) {

        List<String> asList = null;
        switch (type) {
            case 1: {
                asList = Arrays.asList(ALLOW_IMG_SUFFIX.split(","));
            }
            break;
            default:
                break;
        }
        if (null != asList && asList.contains(fileSuffix.toLowerCase())) {
            return true;
        }
        return false;
    }


    /**
     * 获取文件类型,包括图片,若格式不是定义的文件类型,则返回null
     * @param file
     * @return
     */
    public final static String getFileByFile(File file) {
        String filetype = null;
        byte[] b = new byte[50];
        try {
            InputStream is = new FileInputStream(file);
            is.read(b);
            filetype = getFileTypeByStream(b);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filetype;
    }


    /**
     * 根据文件的二进制流获取文件类型
     * @param b
     * @return
     */
    public final static String getFileTypeByStream(byte[] b) {
        String filetypeHex = String.valueOf(getFileHexString(b));
        Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Map.Entry<String, String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }


    /**
     * 验证文件是否为图片
     * @param file
     * @return
     */
    public static final boolean isImage(File file) {
        boolean flag = false;
        try {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if (width == 0 || height == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (IOException e) {
            flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取文件的十六进制数据
     * @param b
     * @return
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) throws Exception {

       /* URL fileURL = new URL("https://gss3.bdstatic.com/7Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=ab0b804ee2f81a4c323fe49bb6430b3c/4034970a304e251f32f2d6c2ae86c9177f3e5386.jpg");
        InputStream fileInputStream = fileURL.openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 1024;
        byte tmp[] = new byte[len];
        int i;
        while ((i = fileInputStream.read(tmp, 0, len)) > 0) {
            baos.write(tmp, 0, i);
        }
        String fileTypeSuffix = FileTypeHandlerUtils.getFileTypeByStream(baos.toByteArray());*/

        String fileTypeSuffix = FileTypeHandlerUtils.getFileByFile(new File("D:\\software/Explosion_1080.webp"));

        System.out.println("===================" + fileTypeSuffix);
    }

    //---------
}
