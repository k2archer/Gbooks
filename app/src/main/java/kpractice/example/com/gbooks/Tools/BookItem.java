package kpractice.example.com.gbooks.Tools;

import android.os.Parcel;
import android.os.Parcelable;

public class BookItem implements Parcelable{
    private String name;

    public BookItem(String name) {
        this.name = name;
    }

    protected BookItem(Parcel in) {
        name = in.readString();
    }

    public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
        @Override
        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
