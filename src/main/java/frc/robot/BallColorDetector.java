package frc.robot;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class BallColorDetector implements ColorDistanceSensor {
  // Global thread for multiple color sensors
  private static final DualColorSensorThread m_colorSensor = new DualColorSensorThread(Port.kMXP);
  private double m_thresholdMillimeters;
  private boolean m_state = false;
  private double m_hysterisisMillimeters = 5.0;
  private int m_sensorIdx;

  public BallColorDetector(double threshold_mm, int sensorIdx) {
    m_thresholdMillimeters = threshold_mm;
    m_sensorIdx = sensorIdx;
  }

  /**
   * Get the color either Color.kRed, or Color.kBlue if the game piece is present.
   *
   * @return eithr Color.kRed or Color.kBlue
   */
  @Override
  public Color getColor() {
    if (!isPresent()) {
      return Color.kBlack;
    }

    var color = m_colorSensor.getRawColor(m_sensorIdx);

    // Add extra gain to 'equal out' the spectral response. See Figure 1. of the APDS-9151 datasheet
    double blue = (double) color.blue * 1.666666;
    double red = (double) color.red * 1.11111111;

    if (red > blue) {
      return Color.kRed;
    } else {
      return Color.kBlue;
    }
  }

  /**
   * Return the distance of the game piece if it is present.
   *
   * @return distance of the game piece if present
   */
  @Override
  public double getDistanceMillimeters() {
    return m_colorSensor.getProximityMillimeters(m_sensorIdx);
  }

  /**
   * Return if the game piece is in range
   *
   * @return true if game piece is in range
   */
  public boolean isPresent() {
    double distance = getDistanceMillimeters();
    if (distance < m_thresholdMillimeters) {
      m_state = true;
    } else if (distance > m_thresholdMillimeters + m_hysterisisMillimeters) {
      m_state = false;
    }
    return m_state;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    ColorDistanceSensor.super.initSendable(builder);
    builder.addBooleanProperty("State", () -> m_state, null);
    builder.addDoubleProperty(
        "Threshold mm", () -> m_thresholdMillimeters, (val) -> m_thresholdMillimeters = val);
  }
}
