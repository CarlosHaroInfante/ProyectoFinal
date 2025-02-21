package VistaPrueba2.Utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Base64;

public class ByteArrayToBase64TypeAdapter extends TypeAdapter<byte[]> {

    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String base64 = Base64.getEncoder().encodeToString(value);
        out.value(base64);
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        String base64 = in.nextString();
        return Base64.getDecoder().decode(base64);
    }
}

