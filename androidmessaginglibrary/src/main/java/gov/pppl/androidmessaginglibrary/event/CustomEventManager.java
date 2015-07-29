package gov.pppl.androidmessaginglibrary.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class CustomEventManager {
    private HashMap<Class<?>, HashMap<Object, HashMap<Method, CustomEventHandler>>> listeners = new HashMap<Class<?>, HashMap<Object, HashMap<Method, CustomEventHandler>>>();

    public synchronized void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            CustomEventHandler eventListener = method.getAnnotation(CustomEventHandler.class);

            if (eventListener == null || method.getParameterTypes().length != 1)
                continue;

            Class<?> c = method.getParameterTypes()[0];

            HashMap<Object, HashMap<Method, CustomEventHandler>> eventData = listeners.get(c);

            if (eventData == null) {
                eventData = new HashMap<Object, HashMap<Method, CustomEventHandler>>();
                listeners.put(c, eventData);
            }

            HashMap<Method, CustomEventHandler> methods = eventData.get(listener);

            if (methods == null) {
                methods = new HashMap<Method, CustomEventHandler>();
                eventData.put(listener, methods);
            }

            methods.put(method, eventListener);

        }

    }

    public synchronized <T> void callEvent(T event) {

        List<Entry<Object, Entry<Method, CustomEventHandler>>> listenerMethods = getListenerMethods(event);

        fireEvent(event, listenerMethods, false);
        fireEvent(event, listenerMethods, true);
    }

    public synchronized void unregisterListener(Object listener) {
        Set<Class<?>> remove = new HashSet<Class<?>>();

        for (Entry<Class<?>, HashMap<Object, HashMap<Method, CustomEventHandler>>> entry : listeners.entrySet()) {

            entry.getValue().remove(listener);
            if (entry.getValue().isEmpty())
                remove.add(entry.getKey());
        }
        for (Class<?> clazz : remove) {
            listeners.remove(clazz);
        }
    }

    private void fireEvent(Object event, List<Entry<Object, Entry<Method, CustomEventHandler>>> listenerMethods, boolean postEvent) {

        for (Entry<Object, Entry<Method, CustomEventHandler>> entry : listenerMethods) {

            if (((entry.getValue().getValue().ignoreCancelled()) && ((event instanceof Cancellable)) && (((Cancellable) event).isCancelled()))
                    || postEvent != entry.getValue().getValue().postEvent()) {
                continue;
            }

            try {
                entry.getValue().getKey().invoke(entry.getKey(), event);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

                e.printStackTrace();
            }

        }

    }

    private List<Entry<Object, Entry<Method, CustomEventHandler>>> getListenerMethods(Object event) {

        List<Entry<Object, Entry<Method, CustomEventHandler>>> listenerMethods = new ArrayList<Entry<Object, Entry<Method, CustomEventHandler>>>();

        for (Entry<Class<?>, HashMap<Object, HashMap<Method, CustomEventHandler>>> entry : listeners.entrySet()) {

            if (entry.getKey().isInstance(event)) {
                for (Entry<Object, HashMap<Method, CustomEventHandler>> entry2 : entry.getValue().entrySet()) {

                    for (Entry<Method, CustomEventHandler> entry3 : entry2.getValue().entrySet()) {
                        listenerMethods.add(new SimpleEntry<Object, Entry<Method, CustomEventHandler>>(entry2.getKey(), entry3));
                    }
                }
            }
        }

        if (listenerMethods.isEmpty())
            return listenerMethods;

        Collections.sort(listenerMethods, new Comparator<Entry<Object, Entry<Method, CustomEventHandler>>>() {
            @Override
            public int compare(Entry<Object, Entry<Method, CustomEventHandler>> o1, Entry<Object, Entry<Method, CustomEventHandler>> o2) {
                return (o1.getValue().getValue().priority().getSlot() > o2.getValue().getValue().priority().getSlot()) ? 1 : -1;

            }
        });

        return listenerMethods;
    }


    public synchronized <T> void callEvent(T event, CustomEventExecutor<T> executor) {

        List<Entry<Object, Entry<Method, CustomEventHandler>>> listenerMethods = getListenerMethods(event);

        fireEvent(event, listenerMethods, false);
        if (!((event instanceof Cancellable)) || !(((Cancellable) event).isCancelled())) {
            executor.execute(event);
        }
        fireEvent(event, listenerMethods, true);
    }

    @Target(ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface CustomEventHandler {

        public abstract Priority priority() default Priority.NORMAL;

        public abstract boolean postEvent() default false;

        public abstract boolean ignoreCancelled() default true;

    }

    public interface CustomEventExecutor<T> {

        public abstract void execute(T event);

    }

    public interface Cancellable {

        public boolean isCancelled();

        public void setCancelled();
    }

    public enum Priority {

        LOWEST(0), LOW(1), NORMAL(2), HIGH(3), HIGHEST(4), MONITOR(5);

        private final int slot;

        private Priority(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }
    }

}