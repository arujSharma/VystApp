package vyst.business.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 4/16/2018.
 */

public class AddEditJson {


    @SerializedName("token")
    @Expose
    private String token ;

    @SerializedName("user_id")
    @Expose
    private String user_id ;

    @SerializedName("type")
    @Expose
    private String type ;

    @SerializedName("availability")
    @Expose
    private boolean availability ;

    @SerializedName("description")
    @Expose
    private String description ;

    @SerializedName("discount")
    @Expose
    private String discount ;

    @SerializedName("discount_type")
    @Expose
    private String discount_type ;

    @SerializedName("image_name")
    @Expose
    private String image_name ;


    @SerializedName("item_id")
    @Expose
    private String item_id ;

    @SerializedName("item_name")
    @Expose
    private String item_name ;

    @SerializedName("price")
    @Expose
    private String price ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
