package yelm.io.avestal.reg_ver.registration.phone_registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("auth")
    @Expose
    private Boolean auth;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    @NotNull
    @Override
    public String toString() {
        return "AuthResponse{" +
                "hash='" + hash + '\'' +
                ", auth=" + auth +
                '}';
    }
}
