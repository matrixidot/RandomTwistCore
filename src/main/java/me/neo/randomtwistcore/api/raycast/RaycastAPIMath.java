package me.neo.randomtwistcore.api.raycast;


import org.bukkit.util.Vector;

/**
 * A class that is used to do math for a {@link me.neo.randomtwistcore.api.raycast.Raycast}.
 */
@SuppressWarnings("unused")
public class RaycastAPIMath {
    /**
     * Returns the cosine of a.
     * Calls the {@link java.lang.Math#cos(double)} method
     * @param a an angle, in radians.
     * @return The cosine of a.
     */
    public static double cos(double a) {
        return Math.cos(a);
    }

    /**
     * Returns the sine of a.
     * Calls the {@link java.lang.Math#sin(double)} method
     * @param a an angle, in radians.
     * @return The sine of a.
     */
    public static double sin(double a) {
        return Math.sin(a);
    }

    /**
     * Returns the tangent of a.
     * Calls the {@link java.lang.Math#tan(double)} method
     * @param a an angle, in radians.
     * @return The tangent of a.
     */
    public static double tan(double a) {
        return Math.tan(a);
    }

    /**
     * Returns the arc cosine of a.
     * Calls the {@link java.lang.Math#acos(double)} method
     * @param a an angle, in radians.
     * @return The arc cosine of a.
     */
    public static double arccos(double a) {
        return Math.acos(a);
    }

    /**
     * Returns the arc sine of a.
     * Calls the {@link java.lang.Math#asin(double)} method
     * @param a an angle, in radians.
     * @return The arc sine of a.
     */
    public static double arcsin(double a) {
        return Math.asin(a);
    }

    /**
     * Returns the arc tangent of a.
     * Calls the {@link java.lang.Math#atan(double)} method
     * @param a an angle, in radians.
     * @return The arc tangent of a.
     */
    public static double arctan(double a) {
        return Math.atan(a);
    }

    /**
     * Converts a degree measure to radians.
     * Calls the {@link java.lang.Math#toRadians(double)} method
     * @param angdeg an angle, in degrees.
     * @return The angle but in radians.
     */
    public static double toRadians(double angdeg) {
        return Math.toRadians(angdeg);
    }


    /**
     * Converts a radian measure to degrees.
     * Calls the {@link java.lang.Math#toDegrees(double)} method
     * @param angrad an angle, in radians.
     * @return The angle but in degrees.
     */
    public static double toDeg(double angrad) {
        return Math.toDegrees(angrad);
    }

    /**
     * Returns the angle between 2 legs of a triangle.
     * @param width The wider leg of the triangle.
     * @param height The taller leg of the triangle.
     * @return The angle measure (in radians) between the 2 legs.
     */
    public static double getAngle(double width, double height) {
        if (width < 0.0D)
            width *= -1.0D;
        if (height < 0.0D)
            height *= -1.0D;
        if (width == 0.0D || height == 0.0D)
            return 0.0D;
        return arctan(height / width);
    }

    /**
     * Rotates a given {@link org.bukkit.util.Vector} by a specified yaw and pitch.
     * @param vect The vector to rotate.
     * @param yaw The yaw (horizontal rotation) in degrees.
     * @param pitch The pitch (vertical rotation) in degrees.
     * @return The rotated vector.
     */
    public static Vector rotate(Vector vect, double yaw, double pitch) {
        yaw = toRadians(yaw);
        pitch = toRadians(pitch);
        vect = rotateX(vect, pitch);
        vect = rotateY(vect, -yaw);
        return vect;
    }

    /**
     * Rotates a given {@link org.bukkit.util.Vector} along the x-axis.
     * @param vect The vector to rotate.
     * @param a The amount to rotate in degrees.
     * @return The rotated vector.
     */
    public static Vector rotateX(Vector vect, double a) {
        double y = cos(a) * vect.getY() - sin(a) * vect.getZ();
        double z = sin(a) * vect.getY() + cos(a) * vect.getZ();
        return vect.setY(y).setZ(z);
    }

    /**
     * Rotates a given {@link org.bukkit.util.Vector} along the y-axis.
     * @param vect The vector to rotate.
     * @param b The amount to rotate in degrees.
     * @return The rotated vector.
     */
    public static Vector rotateY(Vector vect, double b) {
        double x = cos(b) * vect.getX() + sin(b) * vect.getZ();
        double z = -sin(b) * vect.getX() + cos(b) * vect.getZ();
        return vect.setX(x).setZ(z);
    }

    /**
     * Rotates a given {@link org.bukkit.util.Vector} along the z-axis.
     * @param vect The vector to rotate.
     * @param c The amount to rotate in degrees.
     * @return The rotated vector.
     */
    public static Vector rotateZ(Vector vect, double c) {
        double x = cos(c) * vect.getX() - sin(c) * vect.getY();
        double y = sin(c) * vect.getX() + cos(c) * vect.getY();
        return vect.setX(x).setY(y);
    }
}

