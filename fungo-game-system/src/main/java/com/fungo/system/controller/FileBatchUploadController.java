package com.fungo.system.controller;



import com.fungo.system.service.impl.FileBatchUploadService;
import com.fungo.system.service.impl.MemberServiceImpl;
import com.fungo.system.upload.bean.req.BatchUploadInput;
import com.fungo.system.upload.bean.resp.UploadFileOutBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.framework.file.IFileService;
import com.game.common.util.annotation.Anonymous;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * <p>
 *      文件批量上传接口
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class FileBatchUploadController {

    private static final Logger logger = LoggerFactory.getLogger( FileBatchUploadController.class);

    @Autowired
    private FileBatchUploadService fileBatchUploadService;
    @Autowired
    private IFileService fileService;

    /**
     *  批量上传其他平台的图片文件
     * @param uploadInput
     * @return
     */
    @RequestMapping(value = "/api/upload/imgs", method = RequestMethod.POST)
    public WebAsyncTask uploadFileWithOtherOriginalNet(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody BatchUploadInput uploadInput) {
        List<String> imageUrlList = uploadInput.getImageUrl();
        List<UploadFileOutBean> uploadFileOutBeanList = new ArrayList<>();
        Callable<ResultDto<List<UploadFileOutBean>>> callable = new Callable<ResultDto<List<UploadFileOutBean>>>() {
            @Override
            public ResultDto<List<UploadFileOutBean>> call() throws Exception {
                if (null == imageUrlList || imageUrlList.isEmpty()) {
                    return ResultDto.error("-1", "上传文件不能为空");
                }
                //执行上传
                fileBatchUploadService.excuteUpload(imageUrlList, uploadFileOutBeanList);
                ResultDto<List<UploadFileOutBean>> resultDto = new ResultDto<>();
                resultDto.setData(uploadFileOutBeanList);
                resultDto.setStatus(1);
                resultDto.setMessage("上传完成");
                return resultDto;
            }
        };
        WebAsyncTask asyncTask = new WebAsyncTask(8000, callable);
        asyncTask.onTimeout( new Callable<ResultDto<List<UploadFileOutBean>>>() {
                    @Override
                    public ResultDto<List<UploadFileOutBean>> call() throws Exception {
                        ResultDto<List<UploadFileOutBean>> resultDto = new ResultDto<>();
                        resultDto.setData(uploadFileOutBeanList);
                        resultDto.setStatus(-1);
                        resultDto.setMessage("上传超时，请继续上传未上传的图片");
                        return resultDto;
                    }
                }
        );
        return asyncTask;
    }

    /**
     *  批量上传apk包
     * @param appFile
     * @return
     */
    @PostMapping(value = "/api/upload/apk")
    public ResultDto<Map<String,Object>> uploadFileWithApk(@Anonymous MemberUserProfile memberUserPrefile, @RequestParam("file") MultipartFile appFile) {
        String allowSuffix = "jpg,png,gif,jpeg,apk";
        ResultDto<Map<String,Object>> resultDto = new ResultDto<>(  );
        Map<String,Object> map = new HashMap<>();
        try {
            String suffix = appFile.getOriginalFilename().substring(appFile.getOriginalFilename().lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();
            int length = allowSuffix.indexOf(suffix);
            if (length == -1) {
//                return ResultDto.ResultDtoFactory.buildError( "请上传允许格式的文件" );
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            String datePath = sdf.format(new Date());
            String name = UUID.randomUUID().toString();
            String imagePath =  fileService.saveFile(datePath + "/" + name + "." + suffix, suffix, appFile.getInputStream());
            map.put("url", imagePath);
            resultDto.setData( map);
        }catch (Exception e){
            logger.error( "批量上传apk包失败",e);
        }
        return resultDto;

    }


//---------
}
