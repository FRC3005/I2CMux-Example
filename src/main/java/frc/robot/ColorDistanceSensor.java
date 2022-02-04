package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.util.Color;

public interface ColorDistanceSensor extends Sendable {
  public double getDistanceMillimeters();

  public Color getColor();

  public default double getDistanceInches() {
    return getDistanceMillimeters() * 25.4;
  }

  @Override
  public default void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("R", () -> getColor().red, null);
    builder.addDoubleProperty("G", () -> getColor().green, null);
    builder.addDoubleProperty("B", () -> getColor().blue, null);
    builder.addDoubleProperty("Distance mm", () -> getDistanceMillimeters(), null);
  }
}
