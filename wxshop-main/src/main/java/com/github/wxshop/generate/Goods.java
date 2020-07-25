package com.github.wxshop.generate;

import java.io.Serializable;
import java.util.Date;

public class Goods implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.SHOP_ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Long shopId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.NAME
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.DESCRIPTION
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.IMG_URL
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private String imgUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.PRICE
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Long price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.STOCK
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Integer stock;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.STATUS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.CREATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Date createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.UPDATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private Date updatedAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column GOODS.DETAILS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private String details;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table GOODS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.ID
     *
     * @return the value of GOODS.ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.ID
     *
     * @param id the value for GOODS.ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.SHOP_ID
     *
     * @return the value of GOODS.SHOP_ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.SHOP_ID
     *
     * @param shopId the value for GOODS.SHOP_ID
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.NAME
     *
     * @return the value of GOODS.NAME
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.NAME
     *
     * @param name the value for GOODS.NAME
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.DESCRIPTION
     *
     * @return the value of GOODS.DESCRIPTION
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.DESCRIPTION
     *
     * @param description the value for GOODS.DESCRIPTION
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.IMG_URL
     *
     * @return the value of GOODS.IMG_URL
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.IMG_URL
     *
     * @param imgUrl the value for GOODS.IMG_URL
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.PRICE
     *
     * @return the value of GOODS.PRICE
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Long getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.PRICE
     *
     * @param price the value for GOODS.PRICE
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.STOCK
     *
     * @return the value of GOODS.STOCK
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.STOCK
     *
     * @param stock the value for GOODS.STOCK
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.STATUS
     *
     * @return the value of GOODS.STATUS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.STATUS
     *
     * @param status the value for GOODS.STATUS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.CREATED_AT
     *
     * @return the value of GOODS.CREATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.CREATED_AT
     *
     * @param createdAt the value for GOODS.CREATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.UPDATED_AT
     *
     * @return the value of GOODS.UPDATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.UPDATED_AT
     *
     * @param updatedAt the value for GOODS.UPDATED_AT
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column GOODS.DETAILS
     *
     * @return the value of GOODS.DETAILS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public String getDetails() {
        return details;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column GOODS.DETAILS
     *
     * @param details the value for GOODS.DETAILS
     *
     * @mbg.generated Sat Jul 25 17:03:36 CST 2020
     */
    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }
}