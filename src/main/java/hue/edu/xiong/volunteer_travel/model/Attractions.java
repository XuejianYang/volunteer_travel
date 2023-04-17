package hue.edu.xiong.volunteer_travel.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "attractions")
public class Attractions {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "image")
    private String image;
    @Column(name = "images")
    private String images;
    @Column(name = "attractionsName")
    private String name;
    @Column(name = "attractionsAddress")
    private String address;
    @Column(name = "attractionsDescribe")
    private String describe;
    @Column(name = "attractionsStatus")
    private Integer status;
    @Column(name = "createDate")
    private Date createDate;
    @Transient
    private Integer likeNum;
    @Transient
    private Integer preNum;
    @Transient
    private List<String> imageList;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getPreNum() {
        return preNum;
    }

    public void setPreNum(Integer preNum) {
        this.preNum = preNum;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getDescribe() { return describe; }

    public void setDescribe(String describe) { this.describe = describe; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }
}
