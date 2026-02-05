package io.github.jukejuke.tool.license;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class LicenseInfo {
    private String licenseId;
    private String productName;
    private String productVersion;
    private String licensee;
    private Date issueDate;
    private Date expirationDate;
    private String hardwareId;
    private Map<String, Boolean> features;
    private Map<String, Object> extraInfo;

    public LicenseInfo() {
        this.licenseId = UUID.randomUUID().toString();
        this.issueDate = new Date();
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getLicensee() {
        return licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isExpired() {
        if (expirationDate == null) {
            return false;
        }
        return new Date().after(expirationDate);
    }

    public boolean hasFeature(String featureName) {
        if (features == null) {
            return false;
        }
        return Boolean.TRUE.equals(features.get(featureName));
    }

    @Override
    public String toString() {
        return "LicenseInfo{" +
                "licenseId='" + licenseId + '\'' +
                ", productName='" + productName + '\'' +
                ", productVersion='" + productVersion + '\'' +
                ", licensee='" + licensee + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", hardwareId='" + hardwareId + '\'' +
                ", features=" + features +
                ", extraInfo=" + extraInfo +
                '}';
    }
}