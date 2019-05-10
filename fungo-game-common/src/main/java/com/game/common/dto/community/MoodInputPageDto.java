package com.game.common.dto.community;


import com.game.common.api.InputPageDto;

public class MoodInputPageDto extends InputPageDto {

    /**
     * 文章数据行ID
     */
    private Long rowId;

    /**
     * 最后更新时间 yyyy-MM-dd HH:mm:ss
     */
    private String lastUpdateDate;

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
