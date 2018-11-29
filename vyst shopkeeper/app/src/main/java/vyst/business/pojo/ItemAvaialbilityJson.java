package vyst.business.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 4/18/2018.
 */

public class ItemAvaialbilityJson {

    @SerializedName("token")
    @Expose
    private String token ;

    @SerializedName("user_id")
    @Expose
    private String user_id ;

    @SerializedName("item_id")
    @Expose
    private String item_id ;

    @SerializedName("availability")
    @Expose
    private boolean availability  ;

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

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
