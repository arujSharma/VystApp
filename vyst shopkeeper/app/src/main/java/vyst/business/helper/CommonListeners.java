package vyst.business.helper;

/**
 * Created by Admin on 2/7/2018.
 */

public interface CommonListeners {

     interface AlertCallBackWithButtonsInterface {
        void positiveClick();
        void neutralClick();
        void negativeClick();
    }


     interface DiscountType {
        void flat();
        void percentage();
        void noDiscount();
        void cancel();
    }

     interface DeliveryTime {
        void now();
        void morning();
        void evening();
        void allOrders();
    }

    interface Options {
        void share();
        void feedback();
        void callUs();
        void aboutUs();
        void rateUs();
        void logOut();
        void cancel();
    }



}
