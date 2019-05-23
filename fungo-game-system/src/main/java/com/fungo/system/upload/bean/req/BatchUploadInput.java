package com.fungo.system.upload.bean.req;

import java.util.List;

/**
 * <p>
 *      批量上传入参封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class BatchUploadInput {
    private List<String> imageUrl;

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

}
