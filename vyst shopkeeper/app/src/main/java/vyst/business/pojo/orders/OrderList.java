
package vyst.business.pojo.orders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {

    @SerializedName("bill")
    @Expose
    private String bill;
    @SerializedName("created_time")
    @Expose
    private Long createdTime;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("location_id")
    @Expose
    private LocationId locationId;
//    @SerializedName("others")
//    @Expose
//    private String others;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("products_id")
    @Expose
    private List<ProductsId> productsId=null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("total_payable_amount")
    @Expose
    private String totalPayableAmount;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public LocationId getLocationId() {
        return locationId;
    }


    public void setLocationId(LocationId locationId) {
        this.locationId = locationId;
    }

//    public void setOthers(Others others) {
//        this.others = others;
//    }
//    public Others getOthers() {
//        return others;
//    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<ProductsId> getProductsId() {
        return productsId;
    }

    public void setProductsId(List<ProductsId> productsId) {
        this.productsId = productsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(String totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
