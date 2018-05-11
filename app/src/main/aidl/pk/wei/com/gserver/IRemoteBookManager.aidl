// RemoteBookManager.aidl
package pk.wei.com.gserver;

// Declare any non-default types here with import statements

import pk.wei.com.gserver.Book;
import pk.wei.com.gserver.INewBookPushListener;

interface IRemoteBookManager {

    int login(in String name);
    List<Book> findBook(String name);
    List<Book> getRecommend();
    int subscribeBook(String user, String book);
    int unSubscribeBook(String user, String book);
    List<Book> getOrderedList(String user);
    List<String> getMessagesList(String user);
//
    void registerNewBookPush(INewBookPushListener listener);
    void unRegisterNewBookPush(INewBookPushListener listener);
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
}
