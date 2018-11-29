package vyst.business.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import vyst.business.R;


/**
 * Created by Admin on 8/24/2017.
 */

public class DialogPopUps {


    static Dialog dialog;

    public static void showAlertWithButtons(final Context activity,String titleText, String message
             , String positive, String negative, String neutral, boolean setCancelable,
              final CommonListeners.AlertCallBackWithButtonsInterface alertCallBackWithButtonsInterface) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setMessage(message)
                    .setCancelable(setCancelable);


            if(negative.equalsIgnoreCase("")&& neutral.equalsIgnoreCase("")){
                builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertCallBackWithButtonsInterface.positiveClick();
                        dialog.dismiss();
                    }
                });
            }
            else if(neutral.equalsIgnoreCase("")){
                builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertCallBackWithButtonsInterface.positiveClick();
                        dialog.dismiss();
                    }
                }).setNegativeButton(negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                alertCallBackWithButtonsInterface.negativeClick();
                                dialog.dismiss();
                            }
                        });
            }

            else {

                builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertCallBackWithButtonsInterface.positiveClick();
                        dialog.dismiss();
                    }
                }).setNeutralButton(neutral, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertCallBackWithButtonsInterface.negativeClick();
                        dialog.dismiss();
                    }
                })
                        .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                alertCallBackWithButtonsInterface.negativeClick();
                                dialog.dismiss();
                            }
                        });

            }



            try {
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(titleText);
                alert.show();
            } catch (Exception e) {
                Log.v("Popup", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void discountTypesPopup(final Context activity
            , final CommonListeners.DiscountType alertCallBackInterface) {
        try {
            hideDialog();

            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.discount_popup);
            // Configure dialog box
            // Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up_anim);
            dialog.show();

            TextView txt_flat =  dialog.findViewById(R.id.txt_flat);
            TextView txt_percentage =  dialog.findViewById(R.id.txt_percentage);
            TextView txt_no_discount =  dialog.findViewById(R.id.txt_no_discount);
            TextView cancel_text =  dialog.findViewById(R.id.cancel_text);

            cancel_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.cancel();

                }
            });

            txt_flat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.flat();

                }
            });
            txt_percentage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.percentage();

                }
            });
            txt_no_discount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.noDiscount();

                }
            });



            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("CPS_Dialog_crash", e.getMessage());
        }
    }



    public static void deliveryTime(final Context activity
            , final CommonListeners.DeliveryTime alertCallBackInterface) {
        try {
            hideDialog();

            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.delivery_popup);
            // Configure dialog box
            // Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up_anim);
            dialog.show();

            TextView txt_now =  dialog.findViewById(R.id.txt_now);
            TextView txt_morning =  dialog.findViewById(R.id.txt_morning);
            TextView txt_evening =  dialog.findViewById(R.id.txt_evening);
            TextView txt_all_orders =  dialog.findViewById(R.id.txt_all_orders);

            txt_all_orders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.allOrders();

                }
            });

            txt_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.now();

                }
            });
            txt_morning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.morning();

                }
            });
            txt_evening.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.evening();

                }
            });



            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("CPS_Dialog_crash", e.getMessage());
        }
    }



    public static void options(final Context activity
            , final CommonListeners.Options alertCallBackInterface) {
        try {
            hideDialog();

            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.options_popup);
            // Configure dialog box
            // Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up_anim);
            dialog.show();

            TextView txt_share =  dialog.findViewById(R.id.txt_share);
            TextView txt_feedback =  dialog.findViewById(R.id.txt_feedback);
            TextView txt_call_us =  dialog.findViewById(R.id.txt_call_us);
            TextView txt_about_us =  dialog.findViewById(R.id.txt_about_us);
            TextView txt_rate_us =  dialog.findViewById(R.id.txt_rate_us);
            TextView txt_logout =  dialog.findViewById(R.id.txt_logout);
            TextView cancel_text =  dialog.findViewById(R.id.cancel_text);


            txt_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.share();

                }
            });

            txt_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.feedback();

                }
            });

            txt_call_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.callUs();

                }
            });

            txt_about_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.aboutUs();

                }
            });

            txt_rate_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.rateUs();

                }
            });

            txt_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.logOut();

                }
            });


            cancel_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.cancel();

                }
            });


            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("CPS_Dialog_crash", e.getMessage());
        }
    }




    public static void openCamera(final Context activity
            , final CommonListeners.AlertCallBackWithButtonsInterface alertCallBackInterface) {
        try {
            hideDialog();

            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.camera_gallery_popup);
            // Configure dialog box
            // Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up_anim);
            dialog.show();

            TextView camera_text = (TextView) dialog.findViewById(R.id.camera_text);

            // RelativeLayout dialog_rel = (RelativeLayout) dialogBox.findViewById(R.id.dialog_rel);

            TextView open_gallery_text = (TextView) dialog.findViewById(R.id.open_gallery_text);
            //remove_photo_text= (TextView) dialogBox.findViewById(R.id.remove_photo_text);
            TextView cancel_text = (TextView) dialog.findViewById(R.id.cancel_text);

            cancel_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    alertCallBackInterface.neutralClick();

                }
            });

            //remove_photo_text.setVisibility(View.GONE);
            camera_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertCallBackInterface.negativeClick();

                }
            });

            open_gallery_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertCallBackInterface.positiveClick();


                }
            });
            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("CPS_Dialog_crash", e.getMessage());
        }
    }





    // hide the progress dialog
    public static void hideDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
