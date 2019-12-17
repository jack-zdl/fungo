package com.fungo.system.controller;


import com.fungo.system.function.MallSeckillOrderFileService;
import com.fungo.system.upload.bean.req.UploadInput;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.framework.file.IFileService;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "文件上传")
public class UploadController {
    @Autowired
    private IFileService fileService;
    @Autowired
    private MallSeckillOrderFileService mallSeckillOrderFileService;

    //	private String allowSuffix = "jpg,png,gif,jpeg";//允许文件格式
    private String allowSuffix = "jpg,png,gif,jpeg";//允许文件格式
    private long allowSize = 10L;//允许文件大小

    public String getAllowSuffix() {
        return allowSuffix;
    }

    public long getAllowSize() {
        return allowSize * 1024 * 1024;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @ApiOperation(value = "图片上传", notes = "")
    @RequestMapping(value = "/api/upload/image", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "验证码", paramType = "form", dataType = "file"),
    })
    public ResultDto<Map<String, String>> upload(@RequestParam("image") MultipartFile appFile) throws Exception {
        String suffix = appFile.getOriginalFilename().substring(appFile.getOriginalFilename().lastIndexOf(".") + 1);
        int length = getAllowSuffix().indexOf(suffix.toLowerCase());
        if (length == -1) {
            throw new Exception("请上传允许格式的文件");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String datePath = sdf.format(new Date());
        String name = UUID.randomUUID().toString();
        String imagePath = fileService.saveFile(datePath + "/" + name + "." + suffix, suffix, appFile.getInputStream());
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", imagePath);
        re.setData(map);
        re.setMessage("上传成功");
        return re;
    }

    @ApiOperation(value = "获取临时token", notes = "")
    @RequestMapping(value = "/api/upload/osstoken", method = RequestMethod.POST)
    public ResultDto<Map<String, String>> osstoken() throws Exception {
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        re.setData(this.fileService.getOssToken());
        return re;
    }

    @ApiOperation(value = "图片上传", notes = "")
    @RequestMapping(value = "/api/upload/imagebyurl", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageUrl", value = "", paramType = "json", dataType = "String"),
    })
    public ResultDto<Map<String, String>> uploadFile(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody UploadInput input) throws Exception {

        URL url = null;

        String imageUrl = input.getImageUrl();

        if(imageUrl.startsWith("//")) {
            imageUrl = "https:"+imageUrl;
        }

        //后缀
        String suffix = imageUrl.substring(imageUrl.lastIndexOf(".")+1).toLowerCase();
        List<String> asList = Arrays.asList(getAllowSuffix().split(","));

        if(!asList.contains(suffix)) {
            suffix = "jpg";
        }

		try {

			url = new URL(imageUrl);
		} catch (MalformedURLException e) {
			LOGGER.info("图片上传失败! url: {}",input.getImageUrl());
			return ResultDto.error("-1","不支持的图片地址,需要https或http协议头");
		}

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String datePath = sdf.format(new Date());
        String name = UUID.randomUUID().toString();
//        String imagePath=fileService.saveFile(datePath+"/"+name+".jpg","jpg",url.openStream());
        String imagePath = fileService.saveFile(datePath + "/" + name + "." + suffix, suffix, url.openStream());
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", imagePath);
        re.setData(map);
        re.setMessage("上传成功");

        LOGGER.info("图片上传成功! url: {}", input.getImageUrl());
        return re;
    }

    @ApiOperation(value = "视频上传", notes = "")
    @RequestMapping(value = "/api/upload/video", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "验证码", paramType = "form", dataType = "file"),
    })
    public void uploadVedio(@RequestParam("video") MultipartFile file) throws Exception {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String datePath = sdf.format(new Date());
        String fileName = UUID.randomUUID().toString();
        try {
            this.uploadFile(file.getBytes(), "/opt/temp/" + datePath + "/", fileName + "." + suffix);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String orignPath = "/opt/temp/" + datePath + "/" + fileName + "." + suffix;
        String filePath = "/opt/temp/" + datePath + "/";
//     	fileService
//		return ResultDto.success();
    }

//	@ApiOperation(value="视频回调", notes="")
//	@RequestMapping(value="/vd/callback", method= RequestMethod.POST)
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "image",value = "验证码",paramType = "form",dataType = "file"),
//	})
//	public void callBack(@RequestBody String video) {
//		VideoBean videoBean = JSON.parseObject(video, VideoBean.class);
//		System.out.println("Id:"+videoBean.getVideoId());
//		System.out.println("S1tatus:"+videoBean.getStatus());
//		
//		List<StreamInfo> streamInfos = videoBean.getStreamInfos();
//		for(StreamInfo s:streamInfos) {
//			System.out.println("---------------------------------------------------------------------");
//			System.out.println("Bitrate:"+s.getBitrate());
//			System.out.println("Format:"+s.getFormat());
//			System.out.println("Height:"+s.getHeight());
//			System.out.println("Width:"+s.getWidth());
//			System.out.println("Sizi:"+s.getSize());
//			System.out.println("Url:"+s.getFileUrl());
//		}
//	}


    private String allowExcelSuffix = "xlsx,xls";//允许文件格式
    private long allowExcelSize = 10L;//允许文件大小

    @ApiOperation(value = "上传excel", notes = "上传excel")
    @RequestMapping(value = "/api/portal/system/mine/excel", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excelfile", value = "excel", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String, String>> uploadAvatar(MemberUserProfile memberUserPrefile, @RequestParam("excelfile") MultipartFile appFile) throws Exception {
        String suffix = appFile.getOriginalFilename().substring(appFile.getOriginalFilename().lastIndexOf(".") + 1);
        suffix = suffix.toLowerCase();
        int length = allowExcelSuffix.indexOf(suffix);
        if (length == -1) {
            throw new Exception("请上传允许格式的文件");
        }
        File file = null;
        InputStream ins =appFile.getInputStream();
        file = new File( appFile.getOriginalFilename() );
//        CommonsMultipartFile cf= (CommonsMultipartFile)appFile;
//        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
//        file = fi.getStoreLocation();
        inputStreamToFile(ins,file);
        mallSeckillOrderFileService.excuteParserToFungoCoin(file);
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        re.setData(map);
        re.setMessage("编辑成功");
        return re;
    }


    public void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
