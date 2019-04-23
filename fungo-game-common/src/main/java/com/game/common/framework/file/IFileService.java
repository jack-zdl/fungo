package com.game.common.framework.file;

import java.io.File;
import java.io.InputStream;
import java.util.Map;


public interface IFileService {
    public String saveFile(String dir, String type, File file) throws Exception ;
    public String saveFile(String dir, String type, InputStream fileContent) throws Exception ;
    public Map<String,String> getOssToken()throws Exception;

    
}
