package com.fungo.system.controller;



import com.fungo.system.service.impl.FileBatchUploadService;
import com.fungo.system.upload.bean.req.BatchUploadInput;
import com.fungo.system.upload.bean.resp.UploadFileOutBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.ArrayList;
import java.util.List;
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


    @Autowired
    private FileBatchUploadService fileBatchUploadService;

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


        asyncTask.onTimeout(
                new Callable<ResultDto<List<UploadFileOutBean>>>() {
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


//---------
}
