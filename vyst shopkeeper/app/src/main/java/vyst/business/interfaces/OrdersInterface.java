package vyst.business.interfaces;

/**
 * Created by Admin on 4/18/2018.
 */

public interface OrdersInterface {

    void call(String phone_no);

    void acceptOrder(String order_id);
    void cancelOrder(String order_id);
    void deliverOrder(String order_id, String received_amount);

    void acceptOrderSize(int size);
    void pendingOrderSize(int size);

}
