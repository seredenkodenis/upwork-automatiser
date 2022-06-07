package main.annotations;

import main.exceptions.ValueException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ValueProcessor {

    public static void fillValues(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Map<String, Field> fields = serialize(clazz);
        setValues(fields, object);
    }

    private static Map<String, Field> serialize(Class<?> clazz) {
        Map<String, Field> result = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Value annotation = field.getAnnotation(Value.class);
            if (annotation != null) {
                result.put(annotation.key(), field);
            }
        }
        return result;
    }

    private static void setValues(Map<String, Field> fields, Object object) throws IllegalAccessException {
        Yaml yaml = new Yaml();

        InputStream inputStream = ValueProcessor.class
                .getResourceAsStream("/main/resources/messages/messages_en.yml");

        Map<String, Object> parsed = yaml.load(inputStream);

        //TODO:maybe make Context add this initialization at start of appplication

        for (Map.Entry<String, Field> field : fields.entrySet()) {
            Object data = parsed.get(field.getKey());

            if (data == null) {
                throw new ValueException("Field with such key was not found! " + field.getValue().toGenericString());
            }

            field.getValue().setAccessible(true);
            field.getValue().set(object, data);
        }
    }

}
