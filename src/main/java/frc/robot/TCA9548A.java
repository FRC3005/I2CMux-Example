package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class TCA9548A {
  private final int kI2CAddress;
  private final I2C m_i2c;

  public TCA9548A(int addr) {
    kI2CAddress = addr;
    m_i2c = new I2C(Port.kMXP, addr);
  }

  public TCA9548A() {
    // Default address for the common PCB available
    this(0x70);
  }

  /**
   * Read list of enabled buses from the device.
   *
   * @return bit field of enabled buses
   */
  public int enabledBuses() {
    byte[] result = new byte[1];
    m_i2c.readOnly(result, 1);
    return result[0];
  }

  /**
   * Set the list of enabled buses
   *
   * @param buses list of buses to enable
   */
  public void setEnabledBuses(int... buses) {
    int writeValue = 0;
    for (int b : buses) {
      if (b >= availableBuses() || b < 0) {
        DriverStation.reportError("Invalid bus enabled on I2C Mux: " + b, true);
      } else {
        writeValue |= 1 << b;
      }
    }
    m_i2c.write(kI2CAddress, writeValue);
  }

  /**
   * Number of available buses
   *
   * @return number of available buses
   */  
  public int availableBuses() {
    return 8;
  }
}
