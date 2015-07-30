package me.exerosis.spartangame.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import me.exerosis.spartangame.menu.SettingsActivity;
import me.exerosis.spartangame.util.redis.RedisMessager;

/**
 * Created by The Exerosis on 7/30/2015.
 */
public class HostEntity extends Entity {
    private Bundle settings;
    private UUID uuid;
    private ArrayList<Field> fields = new ArrayList<Field>();

    public HostEntity(int bitmapID, int x, int y, Bundle settings, String... methodsToSave) {
        super(BitmapFactory.decodeResource(Resources.getSystem(), bitmapID), x, y);
        this.settings = settings;
        uuid = UUID.randomUUID();

        StringBuilder builder = new StringBuilder(settings.getString(SettingsActivity.ARGS_PLAYER_NAME));
        builder.append(":").append(uuid.toString()).append(":").append(bitmapID);
        builder.append(":").append(x).append(":").append(y);

        fields.addAll(Arrays.asList(ClientEntity.class.getFields()));
        fields.addAll(Arrays.asList(ClientEntity.class.getDeclaredFields()));

        RedisMessager.sendMessage("game.spawn", builder.toString(), settings);
    }


    public void updateField(String... fieldNames) {
        StringBuilder builder = new StringBuilder(uuid.toString());
        for (String name : fieldNames)
            for (Field field : fields)
                if (field.getName().equals(name))
                    try {
                        builder.append("&").append(field.getName() + ":" + field.get(this).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
    }


}
