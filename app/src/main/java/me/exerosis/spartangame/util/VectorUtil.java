/*package me.exerosis.spartangame.util;

*//**
 * Created by The Exerosis on 7/30/2015.
 *//*
public class VectorUtil {
    public static Vector calculateVelocity(Vector from, Vector to, double gravity, int heightGain){
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));
        double maxGain = heightGain > (endGain + heightGain) ? heightGain : (endGain + heightGain);
        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
        double vy = Math.sqrt(maxGain * gravity);
        double vh = vy / slope;
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;
        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector(vx, vy, vz);
    }
}*/
