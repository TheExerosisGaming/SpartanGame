package me.exerosis.spartangame.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import gov.pppl.blah.R;
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
    private static Map<UUID, NetworkEntity> entities = new HashMap<>();
    private UUID uuid;
    private static final int screenWidth = GameView.getScreenWidth();
    private static final int screenHeight = GameView.getScreenHeight();

    public static void onMove(String message){
        String[] components = message.split(":");
        UUID uuidOurs = UUID.fromString(components[0]);

        if(entities.containsKey(uuidOurs)){
            NetworkEntity entity = entities.get(uuidOurs);
            entity.x = (int) (Double.valueOf(components[1]) * screenWidth);
            entity.y = (int) (Double.valueOf(components[2]) * screenHeight);
        }
    }

    public static void onSpawn(String message){
        String[] components = message.split(":");
        UUID uuidOurs = UUID.fromString(components[0]);
        UUID uuidHost = UUID.fromString(components[1]);

        /*if (uuids.containsKey(uuidHost))
            return;
*/

        Log.e("SPAWNED", "Spawned");

        Bitmap bitmap = BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftidle);
        new NetworkEntity(BitmapFactory.decodeResource(GameView.getGameResources(), R.drawable.blueleftidle), Integer.valueOf(components[3]), Integer.valueOf(components[4]), Integer.valueOf(components[5]), uuidOurs);

        uuids.put(uuidOurs, uuidHost);
    }

    public static NetworkEntity newInstance(int texture, int x, int y, int layer) {
        Bitmap bitmap = BitmapFactory.decodeResource(GameView.getGameResources(), texture);
        NetworkEntity entity = new NetworkEntity(bitmap, x, y, layer, UUID.randomUUID());
        UUID uuidClient = UUID.randomUUID();

        uuids.put(entity.uuid, uuidClient);

        RedisMessager.sendMessage("game.spawn", uuidClient.toString() + ":" + entity.uuid.toString() + ":" + texture + ":" + x + ":" + y + ":" + layer, MainActivity.getSettings());
        return entity;
    }

    private NetworkEntity(Bitmap bitmap, int x, int y, int layer, UUID uuid) {
        super(bitmap, x, y, layer);
        this.uuid = uuid;
        entities.put(uuid, this);
        setGravity(false);
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


    public NetworkEntity getPairEntity() {
        return entities.get(uuid);
    }

    private void updateLocation() {
        RedisMessager.sendMessage("game.move", getPairUUID() + ":" + (double) (getX() / screenWidth) + ":" + (double) (getY() / screenHeight), MainActivity.getSettings());
    }
}
