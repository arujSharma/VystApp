package vyst.business.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 4/18/2018.
 */

public class FeedbackJson {

    @SerializedName("token")
    @Expose
    private String token ;

    @SerializedName("user_id")
    @Expose
    private String user_id ;

    @SerializedName("feedback")
    @Expose
    private String feedback ;

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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
