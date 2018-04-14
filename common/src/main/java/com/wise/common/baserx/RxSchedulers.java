package com.wise.common.baserx;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RxJava调度管理 单例
 */
public class RxSchedulers {

    private final static Observable.Transformer schedulersTransformer = new  Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)  observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private final static Single.Transformer singleSchedulersTransformer = new Single.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Single) o).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static  <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    public static  <T> Single.Transformer<T, T> applySingleSchedulers() {
        return (Single.Transformer<T, T>) singleSchedulersTransformer;
    }

//    public static <T> Observable.Transformer<T, T> io_main() {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> observable) {
//                return observable.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }
}
