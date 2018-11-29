package vyst.business.pojo.orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Others {


    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("helpUs")
    @Expose
    private String helpUs;


    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getHelpUs() {
        return helpUs;
    }

    public void setHelpUs(String helpUs) {
        this.helpUs = helpUs;
    }


}