package me.exerosis.spartangame.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by Exerosis on 7/21/2015.
 */
public class EntityTest extends Entity {
    //Here UUID, there UUID
    private static Map<UUID, UUID> uuids = new HashMap<>();
    private UUID uuid;

    static {
        new RedisMessageListener("game.spawn", "game.move") {
            @Override
            public void onMessage(String channel, String message) {
                String[] components = message.split(":");
                UUID uuidHost = UUID.fromString(components[0]);

                if (channel.equals("game.move")) {
                    for (Entity entity : Entity.getInstances()) {
                        if (entity instanceof EntityTest)
                            if (uuidHost.equals(((EntityTest) entity).uuid)) {
                                entity.setX(Integer.valueOf(components[1]));
                                entity.setY(Integer.valueOf(components[2]));
                            }
                    }
                    return;
                }

                UUID uuidOurs = UUID.fromString(components[1]);

                if (uuids.containsKey(uuidHost))
                    return;

                Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), Integer.valueOf(components[2]));
                new EntityTest(bitmap, Integer.valueOf(components[3]), Integer.valueOf(components[4]), Integer.valueOf(components[5]), uuidOurs);

                uuids.put(uuidOurs, uuidHost);
            }
        };
    }

    public static EntityTest newInstance(int texture, int x, int y, int layer) {
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), texture);
        EntityTest entity = new EntityTest(bitmap, x, y, layer, UUID.randomUUID());
        UUID uuidClient = UUID.randomUUID();

        uuids.put(entity.uuid, uuidClient);

        RedisMessager.sendMessage("game.spawn", entity.uuid.toString() + ":" + uuidClient.toString() + ":" + texture + ":" + x + ":" + y + ":" + layer, MainActivity.getSettings());
        return entity;
    }

    private EntityTest(Bitmap bitmap, int x, int y, int layer, UUID uuid) {
        super(bitmap, x, y, layer);
        this.uuid = uuid;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        updateLocation();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        updateLocation();
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getPairUUID() {
        return uuids.get(uuid);
    }

    private void updateLocation() {
        RedisMessager.sendMessage("game.move", getPairUUID() + ":" + getX() + ":" + getY(), MainActivity.getSettings());
    }
}
