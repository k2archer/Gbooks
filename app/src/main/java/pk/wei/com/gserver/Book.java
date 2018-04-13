package pk.wei.com.gserver;

import android.os.Parcel;
import android.os.Parcelable;


public class Book implements Parcelable {

    public String name;

    public Book(String name) {
        this.name = name;
    }
    protected Book(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
