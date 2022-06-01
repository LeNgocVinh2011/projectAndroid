package com.example.app_rtsp.Models;

public class Camera_Link {
    private int Id;
    private String caName;
    private String caLink;

    public Camera_Link(){}
    public Camera_Link(int _id, String _caName, String _caLink) {
        this.Id = _id;
        this.caName = _caName;
        this.caLink = _caLink;
    }

    public Camera_Link(String _caName, String _caLink) {
        this.caName = _caName;
        this.caLink = _caLink;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public String getCaLink() {
        return caLink;
    }

    public void setCaLink(String caLink) {
        this.caLink = caLink;
    }

    @Override
    public String toString() {
        return "Camera_Link{" +
                "Id=" + Id +
                ", caName='" + caName + '\'' +
                ", caLink='" + caLink + '\'' +
                '}';
    }
}
