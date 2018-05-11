package kpractice.example.com.gbooks.DataMangement;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kpractice.example.com.gbooks.Tools.NewOrderListener;
import pk.wei.com.gserver.Book;
import pk.wei.com.gserver.IBinderPool;
import pk.wei.com.gserver.INewBookPushListener;
import pk.wei.com.gserver.IRemoteBookManager;

public class DataManager {

    private Context context;
    private static volatile DataManager instance = null;
    private IRemoteBookManager remoteBookManager;
    private IBinderPool remoteBinderPool;
    private String userName;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }

        return instance;
    }

    private DataManager() {
        context = BaseApplication.getContext();
        bindRemoteService();
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            remoteBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            remoteBinderPool = null;
            Log.i(this.getClass().getName(), "binderDied: Connection fail.");
            Toast.makeText(context, "Connection fail.", Toast.LENGTH_SHORT).show();
            bindRemoteService();
        }

    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                remoteBinderPool.asBinder().linkToDeath(deathRecipient, 0);
                IBinder binder = remoteBinderPool.queryBinder(100);
                remoteBookManager = IRemoteBookManager.Stub.asInterface(binder);
                registerNewBookPush();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    private synchronized void bindRemoteService() {
        Intent i = new Intent();
        i.setComponent(new ComponentName("pk.wei.com.gserver", "pk.wei.com.gserver.BookService"));
        context.bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    public int login(String name) {
        int check = -1;
        try {
            if (remoteBookManager != null) {
                check = remoteBookManager.login(name);
                if (check == 1)
                    userName = name;
            } else {
                check = -2;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return check;
    }

    public ArrayList<Book> getRecommend() {
        ArrayList<Book> books = null;

        try {
            books = (ArrayList<Book>) remoteBookManager.getRecommend();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Book> findBook(String name) {
        ArrayList<Book> books = null;

        try {
            books = (ArrayList<Book>) remoteBookManager.findBook(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return books;
    }

    public String getUserName() {
        return userName;
    }

    public int subscribeBook(String book) {
        int check = 0;
        try {
            check = remoteBookManager.subscribeBook(userName, book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;
    }

    public int unSubscribeBook(String book) {
        int check = 0;
        try {
            check = remoteBookManager.unSubscribeBook(userName, book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return check;

    }

    public ArrayList<Book> getOrderedList() {
        ArrayList<Book> orderedList = new ArrayList<>();
        try {
            String user = getUserName();
            orderedList = (ArrayList<Book>) remoteBookManager.getOrderedList(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return orderedList;
    }

    public ArrayList<String> getMessagesList() {
        ArrayList<String> messagesList = new ArrayList<>();
        try {
            String user = getUserName();
            messagesList = (ArrayList<String>) remoteBookManager.getMessagesList(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return messagesList;
    }

    public void registerNewBookPush() {
        try {
            remoteBookManager.registerNewBookPush(new INewBookPushListener.Stub() {
                @Override
                public void NewBookArrived(Book newBook) throws RemoteException {
                    Log.i(this.getClass().getName(), "NewBookArrived: ");
                }

                @Override
                public void NewOrderArrived(Book newBook) throws RemoteException {
                    for (NewOrderListener it : mNewOrderListener) {
                        it.NewBookArrived(newBook);
                    }
                }

            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    void unRegisterNewBookPush(INewBookPushListener listener) {
        try {
            remoteBookManager.unRegisterNewBookPush(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<NewOrderListener> mNewOrderListener = new ArrayList<>();
    public void registerNewOrderListener(NewOrderListener listener) {
        mNewOrderListener.add(listener);
    }
    public void unRegisterNewOrderListener(NewOrderListener listener) {
        mNewOrderListener.remove(listener);
    }

}
