package vyst.business.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 4/18/2018.
 */

public class UpdateOrderJson {

    @SerializedName("token")
    @Expose
    private String token ;

    @SerializedName("user_id")
    @Expose
    private String user_id ;

    @SerializedName("order_id")
    @Expose
    private int order_id ;

    @SerializedName("received_amount")
    @Expose
    private String received_amount ;


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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getReceived_amount() {
        return received_amount;
    }

    public void setReceived_amount(String received_amount) {
        this.received_amount = received_amount;
    }
}
