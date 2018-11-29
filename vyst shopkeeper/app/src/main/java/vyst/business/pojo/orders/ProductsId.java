
package vyst.business.pojo.orders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsId {
    @SerializedName("qty")
    @Expose
    private String quantity;

    @SerializedName("product_info")
    @Expose
    private ProductList productList;

    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

    public String getQty() {
        return quantity;
    }

    public void setQty(String bill) {
        this.quantity = bill;
    }

}
