package com.report.dto;

/**
 * @author song.j
 * @create 2017-08-09 15:15:53
 **/
public class DataDTO {
    private Integer productId;
    private Integer pv;
    private Integer uv;

    public DataDTO(Integer productId, Integer pv, Integer uv) {
        this.productId = productId;
        this.pv = pv;
        this.uv = uv;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }
}
