package io.jsonwebtoken.gson.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;

public class GsonSerializer<T> implements Serializer<T> {

    static final Gson DEFAULT_GSON = new GsonBuilder().disableHtmlEscaping().create();
    private Gson gson;

    @SuppressWarnings("unused") //used via reflection by RuntimeClasspathDeserializerLocator
    public GsonSerializer() {
        this(DEFAULT_GSON);
    }

    @SuppressWarnings("WeakerAccess") //intended for end-users to use when providing a custom gson
    public GsonSerializer(Gson gson) {
        Assert.notNull(gson, "gson cannot be null.");
        this.gson = gson;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        Assert.notNull(t, "Object to serialize cannot be null.");
        try {
            return writeValueAsBytes(t);
        } catch (Exception e) {
            String msg = "Unable to serialize object: " + e.getMessage();
            throw new SerializationException(msg, e);
        }
    }

    @SuppressWarnings("WeakerAccess") //for testing
    protected byte[] writeValueAsBytes(T t) {
        Object o;
        if (t instanceof byte[]) {
            o = Encoders.BASE64.encode((byte[]) t);
        } else if (t instanceof char[]) {
            o = new String((char[]) t);
        } else {
            o = t;
        }
        return this.gson.toJson(o).getBytes(Strings.UTF_8);
    }
}
