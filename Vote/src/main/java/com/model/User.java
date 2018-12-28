package com.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class User implements Serializable {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * ture已经冻结  false未冻结
     */
    private Boolean isFreeze;

    /**
     * 签到天数
     */
    private Integer signNumber;

    /**
     * 签到时间
     */
    private Date signTime;

    /**
     * 所属分组
     */
    private String kindGroup;

    private static final long serialVersionUID = 1L;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(Boolean isFreeze) {
        this.isFreeze = isFreeze;
    }

    public Integer getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(Integer signNumber) {
        this.signNumber = signNumber;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getkindGroup() {
        return kindGroup;
    }

    public void setkindGroup(String kindGroup) {
        this.kindGroup = kindGroup;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
            && (this.getIdentityCard() == null ? other.getIdentityCard() == null : this.getIdentityCard().equals(other.getIdentityCard()))
            && (this.getMobilePhone() == null ? other.getMobilePhone() == null : this.getMobilePhone().equals(other.getMobilePhone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getIsFreeze() == null ? other.getIsFreeze() == null : this.getIsFreeze().equals(other.getIsFreeze()))
            && (this.getSignNumber() == null ? other.getSignNumber() == null : this.getSignNumber().equals(other.getSignNumber()))
            && (this.getSignTime() == null ? other.getSignTime() == null : this.getSignTime().equals(other.getSignTime()))
            && (this.getkindGroup() == null ? other.getkindGroup() == null : this.getkindGroup().equals(other.getkindGroup()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getIdentityCard() == null) ? 0 : getIdentityCard().hashCode());
        result = prime * result + ((getMobilePhone() == null) ? 0 : getMobilePhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getIsFreeze() == null) ? 0 : getIsFreeze().hashCode());
        result = prime * result + ((getSignNumber() == null) ? 0 : getSignNumber().hashCode());
        result = prime * result + ((getSignTime() == null) ? 0 : getSignTime().hashCode());
        result = prime * result + ((getkindGroup() == null) ? 0 : getkindGroup().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", realName=").append(realName);
        sb.append(", identityCard=").append(identityCard);
        sb.append(", mobilePhone=").append(mobilePhone);
        sb.append(", email=").append(email);
        sb.append(", isFreeze=").append(isFreeze);
        sb.append(", signNumber=").append(signNumber);
        sb.append(", signTime=").append(signTime);
        sb.append(", kindGroup=").append(kindGroup);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}