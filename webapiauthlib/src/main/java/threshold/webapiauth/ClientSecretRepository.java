package threshold.webapiauth;

import threshold.webapiauth.util.Pair;
import threshold.webapiauth.util.SHA1;
import threshold.webapiauth.util.TextUtils;

//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


/**
 * HttpClient Use this class to provide mUserId and secret.
 * Created by Threshold on 2015/12/28.
 */
public class ClientSecretRepository implements ISecretRepository {

    private Pair<String ,String> mUserIdPassword;
    private Pair<String,String> mUserIdSecret;

    public ClientSecretRepository(String userId,String userPassword) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userPassword)) {
            throw new IllegalArgumentException("userId and userPassword can't be null.");
        }
        this.mUserIdPassword = new Pair<>(userId, userPassword);
    }

//    public String getUserId() {
//        return mUserIdPassword.first;
//    }

    @Override
    public String getSecretForUser(String userId) {
        String secret;
        if (mUserIdSecret != null&&(secret=mUserIdSecret.second)!=null) {
            return secret;
        }
        if (!userId.equals(mUserIdPassword.first)) {
            throw new IllegalStateException("The UserId Has Changed,But you haven't change this class userId and password.");
        }
        String password = mUserIdPassword.second;
        if (userId.length() == 16) { //this is means userId is AppKey ,so password is AppSecret
            mUserIdSecret=new Pair<>(mUserIdPassword.first,password);
            return password;
        }
//        return password;//this password is secret
        try {
            secret= SHA1.calculateSecret(password);
            mUserIdSecret = new Pair<>(mUserIdPassword.first, secret);
            return secret;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
