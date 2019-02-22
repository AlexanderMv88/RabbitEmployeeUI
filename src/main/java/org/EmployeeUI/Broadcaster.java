package org.EmployeeUI;

import org.EmployeeUI.ui.NavigatorUI;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@EnableScheduling
public class Broadcaster implements Serializable {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void receiveBroadcast(WebSocketMsg message);
    }

    private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
    private static Map<BroadcastListener, String> userlist = new ConcurrentHashMap<BroadcastListener, String>();

    public static synchronized void register(BroadcastListener newlistener) {
        listeners.add(newlistener);
        LinkedList<BroadcastListener> removeList = new LinkedList<BroadcastListener>();
        for (final BroadcastListener listener: listeners) {
            if (listener != newlistener && !((NavigatorUI)listener).getPushConnection().isConnected()) {
                removeList.add(listener);
            }
        }
        for (final BroadcastListener listener: removeList) {
            listeners.remove(listener);
            userlist.remove(listener);
            //WebSocketMsg msg = new WebSocketMsg("deluser", "", listener.toString(), "");
            //broadcast(msg);
        }
    }

    public static void pushUserList(BroadcastListener targetListener, String username) {
        if (userlist.get(targetListener) != null) {
            userlist.remove(targetListener);
            userlist.put(targetListener, username);
        } else {
            userlist.put(targetListener, username);
        }
        for (final BroadcastListener listener: listeners) {
            //WebSocketMsg msg = new WebSocketMsg("adduser", "", listener.toString(), userlist.get(listener));
            //broadcast(msg);
        }
    }

    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(final WebSocketMsg message) {
        for (final BroadcastListener listener: listeners)
            executorService.execute(() -> {
                listener.receiveBroadcast(message);
            });
    }
/*
    @Scheduled(fixedRate=1000)
    public static void cyclicPush(){
        FileSystem fileSystem= FileSystems.getDefault();
        WatchService watcher;
        try {
            String dir = "/var/spool/gammu/inbox";

            watcher = fileSystem.newWatchService();
            Path msgDir = fileSystem.getPath(dir);
            msgDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey watchKey = watcher.take();

            List<WatchEvent<?>> events = watchKey.pollEvents();

            for(WatchEvent event : events){
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE){
                    System.out.println("created = " + event.context().toString());
                    //TODO: parse sms
                    try(FileInputStream inputStream = new FileInputStream(dir + event.context().toString())){
                        String smsTxt = IOUtils.toString(inputStream);
                        System.out.println("smsTxt = " + smsTxt);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Broadcaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Broadcaster.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
*/
    @Scheduled(fixedRate=1000)
    public static void timer(){
        WebSocketMsg msg = new WebSocketMsg("time");
        broadcast(msg);
    }
}