package kpractice.example.com.gbooks.Tools;


import android.os.Parcel;
import android.os.Parcelable;

public class MessageItem implements Parcelable {

    public String mText;
    private String mSender;
    private String mReceiver;

    public MessageItem(String text) {
        this.mText = text;
    }

    protected MessageItem(Parcel in) {
        mText = in.readString();
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel in) {
            return new MessageItem(in);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
    }
}
