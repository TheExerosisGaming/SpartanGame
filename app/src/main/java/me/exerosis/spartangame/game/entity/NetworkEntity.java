package me.exerosis.spartangame.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.exerosis.spartangame.game.GameView;
import me.exerosis.spartangame.menu.MainActivity;
import me.exerosis.spartangame.util.redis.RedisMessageListener;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by Exerosis on 7/21/2015.
 */
public class NetworkEntity extends Entity {
    //Here UUID, there UUID
    private static Map<UUID, UUID> uuids = new HashMap<>();
    private UUID uuid;
    private static final int screenWidth = GameView.getScreenWidth();
    private static final int screenHeight = GameView.getScreenHeight();

    static {
        new RedisMessageListener("game.spawn", "game.move") {
            @Override
            public void onMessage(String channel, String message) {
                String[] components = message.split(":");
                UUID uuidOurs = UUID.fromString(components[0]);

                if (channel.equals("android.game.move")) {
                    for (Entity entity : Entity.getInstances()) {
                        if (entity instanceof NetworkEntity)
                            if (uuidOurs.equals(((NetworkEntity) entity).uuid)) {
                                entity.setX(Integer.valueOf(components[1]) * screenWidth);
                                entity.setY(Integer.valueOf(components[2]) * screenHeight);
                            }
                    }
                    return;
                }


                UUID uuidHost = UUID.fromString(components[1]);

                if (uuids.containsKey(uuidHost))
                    return;

                Bitmap bitmap = BitmapFactory.decodeResource(GameView.getGameResources(), Integer.valueOf(components[2]));
                new NetworkEntity(bitmap, Integer.valueOf(components[3]), Integer.valueOf(components[4]), Integer.valueOf(components[5]), uuidOurs);

                uuids.put(uuidOurs, uuidHost);
            }
        };
    }

    public static NetworkEntity newInstance(int texture, int x, int y, int layer) {
        Bitmap bitmap = BitmapFactory.decodeResource(GameView.getGameResources(), texture);
        NetworkEntity entity = new NetworkEntity(bitmap, x, y, layer, UUID.randomUUID());
        UUID uuidClient = UUID.randomUUID();

        uuids.put(entity.uuid, uuidClient);

        RedisMessager.sendMessage("game.spawn", uuidClient.toString() + ":" + entity.uuid.toString() + ":"  + texture + ":" + x + ":" + y + ":" + layer, MainActivity.getSettings());
        return entity;
    }

    private NetworkEntity(Bitmap bitmap, int x, int y, int layer, UUID uuid) {
        super(bitmap, x, y, layer);
        setGravity(false);
        this.uuid = uuid;
    }

    public void setX(int x) {
        super.setX(x);
        updateLocation();
    }

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
        RedisMessager.sendMessage("game.move", getPairUUID() + ":" + getX()/screenWidth + ":" + getY()/screenHeight, MainActivity.getSettings());
    }
}
