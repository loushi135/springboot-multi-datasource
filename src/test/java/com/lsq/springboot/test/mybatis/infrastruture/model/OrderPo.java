

package com.lsq.springboot.test.mybatis.infrastruture.model;

public class OrderPo {
    /**
     * id BIGINT(19) 必填<br>
     * 
     */
    private Long id;

    /**
     * name VARCHAR(255)<br>
     * 
     */
    private String name;

    /**
     * title VARCHAR(255)<br>
     * 
     */
    private String title;

    /**
     * user_id INTEGER(10)<br>
     * 
     */
    private Integer userId;

    /**
     * id BIGINT(19) 必填<br>
     * 获得 
     */
    public Long getId() {
        return id;
    }

    /**
     * id BIGINT(19) 必填<br>
     * 设置 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * name VARCHAR(255)<br>
     * 获得 
     */
    public String getName() {
        return name;
    }

    /**
     * name VARCHAR(255)<br>
     * 设置 
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * title VARCHAR(255)<br>
     * 获得 
     */
    public String getTitle() {
        return title;
    }

    /**
     * title VARCHAR(255)<br>
     * 设置 
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * user_id INTEGER(10)<br>
     * 获得 
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * user_id INTEGER(10)<br>
     * 设置 
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", title=").append(title);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}