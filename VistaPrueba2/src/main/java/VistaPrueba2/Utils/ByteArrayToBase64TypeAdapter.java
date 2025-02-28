package VistaPrueba2.Utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adaptador de Gson para convertir arreglos de bytes en cadenas Base64 y viceversa.
 * <p>
 * Este adaptador se utiliza para serializar y deserializar campos de tipo byte[] en objetos JSON,
 * convirtiéndolos a su representación en Base64 y viceversa.
 * </p>
 * 27/02/2025 - CHI
 */
public class ByteArrayToBase64TypeAdapter extends TypeAdapter<byte[]> {

    private static final Logger log = LoggerFactory.getLogger(ByteArrayToBase64TypeAdapter.class);

    /**
     * Escribe un arreglo de bytes como una cadena Base64 en el JsonWriter.
     *
     * @param out   El JsonWriter en el que se escribirá la cadena.
     * @param value El arreglo de bytes a convertir. Si es nulo, se escribe un valor nulo.
     * @throws IOException Si ocurre un error al escribir en el JsonWriter.
     */
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        try {
            if (value == null) {
                out.nullValue();
                return;
            }
            String base64 = Base64.getEncoder().encodeToString(value);
            out.value(base64);
        } catch (Exception e) {
            log.error("Error al escribir el arreglo de bytes como Base64", e);
            throw new IOException("Error al escribir el arreglo de bytes como Base64", e);
        }
    }

    /**
     * Lee una cadena Base64 del JsonReader y la convierte en un arreglo de bytes.
     *
     * @param in El JsonReader del que se leerá la cadena.
     * @return El arreglo de bytes decodificado a partir de la cadena Base64, o null si el valor es nulo o vacío.
     * @throws IOException Si ocurre un error al leer o decodificar la cadena.
     */
    @Override
    public byte[] read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String base64 = in.nextString();
            if (base64 == null || base64.isEmpty()) {
                return null;
            }
            return Base64.getDecoder().decode(base64);
        } catch (Exception e) {
            log.error("Error al leer y decodificar la cadena Base64", e);
            throw new IOException("Error al leer y decodificar la cadena Base64", e);
        }
    }
}
