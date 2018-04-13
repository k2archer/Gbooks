// IMyAidlInterface.aidl
package pk.wei.com.gserver;

// Declare any non-default types here with import statements

interface IBinderPool {

    IBinder queryBinder(int binderCode);

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
}
