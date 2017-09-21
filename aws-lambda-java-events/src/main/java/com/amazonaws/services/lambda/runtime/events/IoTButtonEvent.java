package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;

/**
 * represents a click of an IoT Button
 */
public class IoTButtonEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 8699582353606993478L;

    private String serialNumber;

    private String clickType;

    private String batteryVoltage;

    /**
     * default constructor
     */
    public IoTButtonEvent() {}

    /**
     * @return serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber serial number
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @param serialNumber serial number
     * @return IotButtonEvent
     */
    public IoTButtonEvent withSerialNumber(String serialNumber) {
        setSerialNumber(serialNumber);
        return this;
    }

    /**
     * @return click type
     */
    public String getClickType() {
        return clickType;
    }

    /**
     * @param clickType click type
     */
    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    /**
     * @param clickType click type
     * @return IoTButtonEvent
     */
    public IoTButtonEvent withClickType(String clickType) {
        setClickType(clickType);
        return this;
    }

    /**
     * @return battery voltage
     */
    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    /**
     * @param batteryVoltage battery voltage
     */
    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    /**
     * @param batteryVoltage battery voltage
     * @return IoTButtonEvent
     */
    public IoTButtonEvent withBatteryVoltage(String batteryVoltage) {
        setBatteryVoltage(batteryVoltage);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getSerialNumber() != null)
            sb.append("serialNumber: ").append(getSerialNumber()).append(",");
        if (getClickType() != null)
            sb.append("clickType: ").append(getClickType()).append(",");
        if (getBatteryVoltage() != null)
            sb.append("batteryVoltage: ").append(getBatteryVoltage());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof IoTButtonEvent == false)
            return false;
        IoTButtonEvent other = (IoTButtonEvent) obj;
        if (other.getSerialNumber() == null ^ this.getSerialNumber() == null)
            return false;
        if (other.getSerialNumber() != null && other.getSerialNumber().equals(this.getSerialNumber()) == false)
            return false;
        if (other.getClickType() == null ^ this.getClickType() == null)
            return false;
        if (other.getClickType() != null && other.getClickType().equals(this.getClickType()) == false)
            return false;
        if (other.getBatteryVoltage() == null ^ this.getBatteryVoltage() == null)
            return false;
        if (other.getBatteryVoltage() != null && other.getBatteryVoltage().equals(this.getBatteryVoltage()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + ((getSerialNumber() == null) ? 0 : getSerialNumber().hashCode());
        hashCode = prime * hashCode + ((getClickType() == null) ? 0 : getClickType().hashCode());
        hashCode = prime * hashCode + ((getBatteryVoltage() == null) ? 0 : getBatteryVoltage().hashCode());
        return hashCode;
    }

    @Override
    public IoTButtonEvent clone() {
        try {
            return (IoTButtonEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
