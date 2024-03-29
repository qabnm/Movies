package dc.android.tools;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jun.liu
 * @date: 2020/12/28 11:10
 * @des:
 */
public final class LiveDataBus {
    private final Map<String, BusMutableLiveData<Object>> liveDataBus;

    private LiveDataBus() {
        liveDataBus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataBus instance = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return SingletonHolder.instance;
    }

    public MutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!liveDataBus.containsKey(key)) liveDataBus.put(key, new BusMutableLiveData<>());
        return (MutableLiveData<T>) liveDataBus.get(key);
    }

    private static class ObserverWrapper<T> implements Observer<T> {
        private Observer<T> observer;

        public ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(T t) {
            if (null != observer) {
                if (isCallOnObserve()) return;
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace){
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) && "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class BusMutableLiveData<T> extends MutableLiveData<T>{
        private Map<Observer ,Observer> observerMap = new HashMap<>();

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void observeForever(@NonNull Observer<? super T> observer) {
            if (!observerMap.containsKey(observer)) observerMap.put(observer , new ObserverWrapper(observer));
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<? super T> observer) {
            Observer realObserver;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            super.removeObserver(realObserver);
        }

        private void hook(@NonNull Observer<? super T> observer) throws Exception {
            //get wrapper's version
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be bull!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);

            //get livedata's version
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion);
        }
    }
}
