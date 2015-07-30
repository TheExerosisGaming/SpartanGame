package me.exerosis.spartangame.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by The Exerosis on 7/30/2015.
 */
public class ClientEntity extends Entity{
    private static HashMap<UUID, ClientEntity> entities = new HashMap<>();

    public static void listen(final String hostName){
        new RedisMessageListener("game.spawn"){
            @Override
            public void onMessage(String message) {
                String[] components = message.split(":");
                if(!components[0].equals(hostName))
                    return;

                UUID uuid = UUID.fromString(components[1]);

                if(!entities.containsKey(uuid)) {
                    Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), Integer.valueOf(components[2]));
                    ClientEntity entity = new ClientEntity(bitmap, Integer.valueOf(components[3]), Integer.valueOf(components[4]), uuid);
                    entities.put(uuid, entity);
                }
            }
        };
    }

    private ArrayList<Field> fields = new ArrayList<Field>();

    public ClientEntity(Bitmap texture, int x, int y, final UUID uuid){
        super(texture, x, y);

        fields.addAll(Arrays.asList(ClientEntity.class.getFields()));
        fields.addAll(Arrays.asList(ClientEntity.class.getDeclaredFields()));

        new RedisMessageListener("game.entity") {
            @Override
            public void onMessage(String message) {
                String[] components = message.split("&");

                if (components[0].equals(uuid.toString()))
                    return;

                for (int a = 1; a < components.length; a++){
                    String[] parts = components[a].split(":");

                    for(Field field : fields){
                        if(!field.getName().equals(parts[0]))
                            continue;

                        try {
                            field.set(ClientEntity.this, parts[1]);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }
}
