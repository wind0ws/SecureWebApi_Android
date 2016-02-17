package threshold.securewebapi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Threshold on 2016/1/12.
 */
public class FileModel implements Parcelable {
    private String name,url, size;

    public FileModel() {
    }

    public FileModel(String name, String url, String size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.size);
    }

    protected FileModel(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.size = in.readString();
    }

    public static final Parcelable.Creator<FileModel> CREATOR = new Parcelable.Creator<FileModel>() {
        public FileModel createFromParcel(Parcel source) {
            return new FileModel(source);
        }

        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };
}
