/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.parceler;

import android.os.Parcelable;
import android.util.SparseArray;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Static utility class used to wrap an `@Parcel` annotated class with the generated `Parcelable` wrapper.
 *
 * @author John Ericksen
 */
public final class Parcels {

    public static final String PARCELS_NAME = "Parcels";
    public static final String PARCELS_REPOSITORY_NAME = "Parceler$$Parcels";
    public static final String PARCELS_PACKAGE = "org.parceler";
    public static final String IMPL_EXT = "Parcelable";

    private static final ParcelCodeRepository REPOSITORY = new ParcelCodeRepository();

    private Parcels(){
        // private utility class constructor
    }

    /**
     * Testing method for replacing the Parceler$Parcels class with one referenced in the given classloader.
     *
     * @param classLoader ClassLoader to use when loading repository.
     */
    protected static void update(ClassLoader classLoader){
        REPOSITORY.loadRepository(classLoader);
    }




    /**
     * Wraps the input `@Parcel` annotated class with a `Parcelable` wrapper.
     *
     * @throws ParcelerRuntimeException if there was an error looking up the wrapped Parceler$Parcels class.
     * @param input Parcel
     * @return Parcelable wrapper
     */
    @SuppressWarnings("unchecked")
    public static <T> Parcelable wrap(T input) {
        if(input == null){
            return null;
        }
        ParcelableFactory parcelableFactory = REPOSITORY.get(input.getClass());

        return parcelableFactory.buildParcelable(input);
    }

    /**
     * Unwraps the input wrapped `@Parcel` `Parcelable`
     *
     * @throws ClassCastException if the input Parcelable does not implement ParcelWrapper with the correct parameter type.
     * @param input Parcelable implementing ParcelWrapper
     * @param <T> type of unwrapped `@Parcel`
     * @return Unwrapped `@Parcel`
     */
    @SuppressWarnings("unchecked")
    public static <T> T unwrap(Parcelable input) {
        if(input == null){
            return null;
        }
        ParcelWrapper<T> wrapper = (ParcelWrapper<T>) input;
        return wrapper.getParcel();
    }

    /**
     * Wraps the input list containing `@Parcel` annotated classes with a `Parcelable` wrapper.
     *
     * @param input List of Parcel
     * @param <T>   List with type of wrapped `Parcelable`
     * @return List of wrapped `Parcelable`
     */
    public static <T> List<Parcelable> wrap(List<T> input) {
        if (input == null) {
            return null;
        }

        ArrayList<Parcelable> data = new ArrayList<Parcelable>();
        for (T t : input) {
            Parcelable p = wrap(t);
            data.add(p);
        }

        return data;
    }

    /**
     * Unwraps the input list wrapped `@Parcel` `Parcelable`
     *
     * @param input List of Parcelable
     * @param <T>   List with type of unwrapped `@Parcel`
     * @return List of unwrapped `@Parcel`
     */
    public static <T> List<T> unwrap(List<Parcelable> input) {
        if (input == null) {
            return null;
        }

        ArrayList<T> data = new ArrayList<T>();
        for (Parcelable p : input) {
            T t = unwrap(p);
            data.add(t);
        }

        return data;
    }

    /**
     * Wraps the input array containing `@Parcel` annotated classes with a `Parcelable` wrapper.
     *
     * @param input Array of Parcel
     * @param <T>   Array with type of wrapped `Parcelable`
     * @return Array of wrapped `Parcelable`
     */
    public static <T> Parcelable[] wrap(T[] input) {
        if (input == null) {
            return null;
        }

        Parcelable[] data = new Parcelable[input.length];
        for (int x = 0; x < input.length; x++) {
            Parcelable p = wrap(input[x]);
            data[x] = p;
        }

        return data;
    }

    /**
     * Unwraps the input array wrapped `@Parcel` `Parcelable`
     *
     * @param input Array of Parcelable
     * @param <T>   Array with type of unwrapped `@Parcel`
     * @return Array of unwrapped `@Parcel`
     */
    public static <T> T[] unwrap(Parcelable[] input) {
        if (input == null) {
            return null;
        }

        T[] data = (T[]) new Object[input.length];
        for (int x = 0; x < input.length; x++) {
            T t = unwrap(input[x]);
            data[x] = t;
        }

        return data;
    }

    /**
     * Wraps the input sparse array containing `@Parcel` annotated classes with a `Parcelable` wrapper.
     *
     * @param input SparseArray of Parcel
     * @param <T>   SparseArray with type of wrapped `Parcelable`
     * @return SparseArray of wrapped `Parcelable`
     */
    public static <T> SparseArray<Parcelable> wrap(SparseArray<T> input) {
        if (input == null) {
            return null;
        }

        SparseArray<Parcelable> data = new SparseArray<Parcelable>();
        for (int x = 0; x < input.size(); x++) {
            Parcelable t = wrap(input.valueAt(x));
            data.put(input.keyAt(x), t);
        }

        return data;
    }

    /**
     * Unwraps the sparse array wrapped `@Parcel` `Parcelable`
     *
     * @param input SparseArray of Parcelable
     * @param <T>   SparseArray with type of unwrapped `@Parcel`
     * @return SparseArray of unwrapped `@Parcel`
     */
    public static <T> SparseArray<T> unwrap(SparseArray<Parcelable> input) {
        if (input == null) {
            return null;
        }

        SparseArray<T> data = new SparseArray<T>();
        for (int x = 0; x < input.size(); x++) {
            T t = unwrap(input.valueAt(x));
            data.put(input.keyAt(x), t);
        }

        return data;
    }

    /**
     * Factory class for building a `Parcelable` from the given input.
     */
    public interface ParcelableFactory<T> {

        String BUILD_PARCELABLE = "buildParcelable";

        /**
         * Build the corresponding `Parcelable` class.
         *
         * @param input input to wrap with a Parcelable
         * @return Parcelable instance
         */
        Parcelable buildParcelable(T input);
    }

    private static final class ParcelableFactoryReflectionProxy<T> implements Parcels.ParcelableFactory<T> {

        private final Constructor<? extends Parcelable> constructor;

        public ParcelableFactoryReflectionProxy(Class<T> parcelClass, Class<? extends Parcelable> parcelWrapperClass) {
            try {
                this.constructor = parcelWrapperClass.getConstructor(parcelClass);
            } catch (NoSuchMethodException e) {
                throw new ParcelerRuntimeException("Unable to create ParcelFactory Type", e);
            }
        }

        @Override
        public Parcelable buildParcelable(T input) {
            try {
                return constructor.newInstance(input);
            } catch (InstantiationException e) {
                throw new ParcelerRuntimeException("Unable to create ParcelFactory Type", e);
            } catch (IllegalAccessException e) {
                throw new ParcelerRuntimeException("Unable to create ParcelFactory Type", e);
            } catch (InvocationTargetException e) {
                throw new ParcelerRuntimeException("Unable to create ParcelFactory Type", e);
            }
        }
    }

    private static final class ParcelCodeRepository {

        private ConcurrentMap<Class, ParcelableFactory> generatedMap = new ConcurrentHashMap<Class, ParcelableFactory>();

        public ParcelCodeRepository() {
            loadRepository(getClass().getClassLoader());
        }

        public ParcelableFactory get(Class clazz){
            ParcelableFactory result = generatedMap.get(clazz);
            if (result == null) {
                ParcelableFactory value = findClass(clazz);
                if(value == null){
                    throw new ParcelerRuntimeException("Unable to create ParcelableFactory for " + clazz.getName());
                }
                result = generatedMap.putIfAbsent(clazz, value);
                if (result == null) {
                    result = value;
                }
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        public ParcelableFactory findClass(Class clazz){
            try {
                Class parcelWrapperClass = Class.forName(clazz.getName() + "$$" + IMPL_EXT);
                return new ParcelableFactoryReflectionProxy(clazz, parcelWrapperClass);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        /**
         * Update the repository class from the given classloader.  If the given repository class cannot be instantiated
         * then this method will throw a ParcelerRuntimeException.
         *
         * @throws ParcelerRuntimeException
         * @param classLoader
         */
        @SuppressWarnings("unchecked")
        public void loadRepository(ClassLoader classLoader){
            try{
                Class repositoryClass = classLoader.loadClass(PARCELS_PACKAGE + "." + PARCELS_REPOSITORY_NAME);
                Repository<ParcelableFactory> instance = (Repository<ParcelableFactory>) repositoryClass.newInstance();
                generatedMap.putAll(instance.get());

            } catch (ClassNotFoundException e) {
                //nothing
            } catch (InstantiationException e) {
                throw new ParcelerRuntimeException("Unable to instantiate generated Repository", e);
            } catch (IllegalAccessException e) {
                throw new ParcelerRuntimeException("Unable to access generated Repository", e);
            }
        }
    }

}
