package com.github.wxshop.generate;

import java.util.Date;

public class Shop {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.NAME
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.DESCRIPTION
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.IMG_URL
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private String imgUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.OWNER_USER_ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private Long ownerUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.STATUS
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.CREATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private Date createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SHOP.UPDATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    private Date updatedAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.ID
     *
     * @return the value of SHOP.ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.ID
     *
     * @param id the value for SHOP.ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.NAME
     *
     * @return the value of SHOP.NAME
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.NAME
     *
     * @param name the value for SHOP.NAME
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.DESCRIPTION
     *
     * @return the value of SHOP.DESCRIPTION
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.DESCRIPTION
     *
     * @param description the value for SHOP.DESCRIPTION
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.IMG_URL
     *
     * @return the value of SHOP.IMG_URL
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.IMG_URL
     *
     * @param imgUrl the value for SHOP.IMG_URL
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.OWNER_USER_ID
     *
     * @return the value of SHOP.OWNER_USER_ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public Long getOwnerUserId() {
        return ownerUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.OWNER_USER_ID
     *
     * @param ownerUserId the value for SHOP.OWNER_USER_ID
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.STATUS
     *
     * @return the value of SHOP.STATUS
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.STATUS
     *
     * @param status the value for SHOP.STATUS
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.CREATED_AT
     *
     * @return the value of SHOP.CREATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.CREATED_AT
     *
     * @param createdAt the value for SHOP.CREATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SHOP.UPDATED_AT
     *
     * @return the value of SHOP.UPDATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SHOP.UPDATED_AT
     *
     * @param updatedAt the value for SHOP.UPDATED_AT
     *
     * @mbg.generated Sat May 23 18:56:39 CST 2020
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}