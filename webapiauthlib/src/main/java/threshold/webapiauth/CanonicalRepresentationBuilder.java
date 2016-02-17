package threshold.webapiauth;

import okhttp3.HttpUrl;

/**
 * Canonical Representation
 * Created by Threshold on 2015/12/28.
 */
public class CanonicalRepresentationBuilder implements IBuildMessageRepresentation  {

    private String mUserId;

    public CanonicalRepresentationBuilder(String mUserId) {
        this.mUserId = mUserId;
    }

    @Override
    public String buildRequestRepresentation(HttpUrl url,String method,String greenwichTime, String contentType,String contentMd5) throws Exception {
        String content_Type ;
        if (method.equalsIgnoreCase("GET")) {
            content_Type = "";
        } else {
            content_Type = contentType;
        }
        String absolutePath = url.uri().getPath();
        return method + "\n" + contentMd5 + "\n" + content_Type + "\n" + greenwichTime + "\n" + mUserId + "\n" + absolutePath;
    }
}
